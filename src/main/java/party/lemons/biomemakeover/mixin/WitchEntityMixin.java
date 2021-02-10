package party.lemons.biomemakeover.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.DisableableFollowTargetGoal;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectType;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.crafting.witch.WitchQuest;
import party.lemons.biomemakeover.crafting.witch.WitchQuestEntity;
import party.lemons.biomemakeover.crafting.witch.WitchQuestHandler;
import party.lemons.biomemakeover.crafting.witch.WitchQuestList;
import party.lemons.biomemakeover.entity.ai.WitchLookAtCustomerGoal;
import party.lemons.biomemakeover.entity.ai.WitchStopFollowingCustomerGoal;
import party.lemons.biomemakeover.init.BMItems;
import party.lemons.biomemakeover.init.BMPotions;
import party.lemons.biomemakeover.util.access.StatusEffectAccess;

@Mixin(WitchEntity.class)
public abstract class WitchEntityMixin extends RaiderEntity implements WitchQuestEntity
{
	@Shadow
	private DisableableFollowTargetGoal<PlayerEntity> attackPlayerGoal;

	@Shadow
	public abstract boolean isDrinking();

	@Shadow
	private int drinkTimeLeft;

	@Shadow
	public abstract void setDrinking(boolean drinking);

	@Shadow
	@Final
	private static EntityAttributeModifier DRINKING_SPEED_PENALTY_MODIFIER;
	private PlayerEntity customer;
	private WitchQuestList quests;
	private int replenishTime;
	private int despawnShield = 0;

	@Inject(at = @At("TAIL"), method = "<init>")
	public void onConstruct(EntityType<? extends WitchEntity> entityType, World world, CallbackInfo cbi)
	{
		quests = new WitchQuestList();
		quests.populate(getRandom());
		replenishTime = world.random.nextInt(24000);
	}

	@Inject(at = @At("TAIL"), method = "initGoals")
	public void initGoals(CallbackInfo cbi)
	{
		this.targetSelector.remove(attackPlayerGoal);
		attackPlayerGoal = new DisableableFollowTargetGoal<>(this, PlayerEntity.class, 10, true, false, (e)->e.getType() == EntityType.PLAYER && !canInteract((PlayerEntity) e));
		this.targetSelector.add(3, attackPlayerGoal);

		this.goalSelector.add(1, new WitchStopFollowingCustomerGoal(((WitchEntity) (Object) this)));
		this.goalSelector.add(1, new WitchLookAtCustomerGoal(((WitchEntity) (Object) this)));
	}

	@ModifyVariable(at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/WitchEntity;equipStack(Lnet/minecraft/entity/EquipmentSlot;Lnet/minecraft/item/ItemStack;)V", ordinal = 1), method = "tickMovement")
	public Potion changePotion(Potion potion)
	{
		if(random.nextFloat() < 0.10)
		{
			for(StatusEffectInstance effect : getStatusEffects())
				if(((StatusEffectAccess)effect.getEffectType()).getType() == StatusEffectType.HARMFUL)
				{
					return BMPotions.ANTIDOTE_POT;
				}
		}
		return potion;
	}

	@Inject(at = @At("TAIL"), method = "tickMovement")
	public void tickMovement(CallbackInfo cbi)
	{
		if(!isDrinking())
		{
			if(random.nextFloat() < 0.10)
			{
				boolean found = false;
				for(StatusEffectInstance effect : getStatusEffects())
					if(((StatusEffectAccess) effect.getEffectType()).getType() == StatusEffectType.HARMFUL)
					{
						found = true;
						break;
					}

				if(found)
				{
					this.equipStack(EquipmentSlot.MAINHAND, PotionUtil.setPotion(new ItemStack(Items.POTION), BMPotions.ANTIDOTE_POT));
					this.drinkTimeLeft = this.getMainHandStack().getMaxUseTime();
					this.setDrinking(true);
					if(!this.isSilent())
					{
						this.world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_WITCH_DRINK, this.getSoundCategory(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
					}

					EntityAttributeInstance entityAttributeInstance = this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED);
					entityAttributeInstance.removeModifier(DRINKING_SPEED_PENALTY_MODIFIER);
					entityAttributeInstance.addTemporaryModifier(DRINKING_SPEED_PENALTY_MODIFIER);
				}
			}
		}
	}

	@Override
	protected ActionResult interactMob(PlayerEntity player, Hand hand)
	{
		ItemStack itemStack = player.getStackInHand(hand);
		if(itemStack.getItem() != Items.VILLAGER_SPAWN_EGG && this.isAlive() && !this.hasCustomer() && canInteract(player))
		{
			if(!this.world.isClient)
			{
				despawnShield = 12000;
				this.setCurrentCustomer(player);
				this.sendQuests(player, this.getDisplayName());
			}
			return ActionResult.success(this.world.isClient);
		}else
		{
			return super.interactMob(player, hand);
		}
	}

	@Override
	protected void dropLoot(DamageSource source, boolean causedByPlayer)
	{
		if(causedByPlayer && random.nextInt(20) == 0)
		{
			dropStack(new ItemStack(BMItems.WITCH_HAT));
		}
		super.dropLoot(source, causedByPlayer);
	}

	@Override
	protected void mobTick()
	{
		super.mobTick();
		if(!world.isClient())
		{
			if(despawnShield > 0) despawnShield--;

			if(replenishTime > 0) replenishTime--;
			else
			{
				for(int i = quests.size(); i < 3; i++)
				{
					quests.add(WitchQuestHandler.createQuest(random));
				}
				replenishTime = 3000 + random.nextInt(21000);
			}
		}
	}

	protected void resetCustomer()
	{
		this.setCurrentCustomer(null);
	}

	@Override
	public void onDeath(DamageSource source)
	{
		super.onDeath(source);
		this.resetCustomer();
	}

	@Override
	public boolean cannotDespawn()
	{
		if(despawnShield > 0) return true;

		return super.cannotDespawn();
	}

	@Override
	public boolean canImmediatelyDespawn(double distanceSquared)
	{
		if(despawnShield > 0) return false;

		return super.canImmediatelyDespawn(distanceSquared);
	}

	public void setCurrentCustomer(PlayerEntity customer)
	{
		this.customer = customer;
	}

	public boolean hasCustomer()
	{
		return getCurrentCustomer() != null;
	}

	@Override
	public boolean canTarget(LivingEntity target)
	{
		if(target.getType() == EntityType.PLAYER && canInteract((PlayerEntity) target)) return false;

		return super.canTarget(target);
	}

	@Override
	public boolean canInteract(PlayerEntity player)
	{
		return getTarget() == null && !hasActiveRaid() && player.getEquippedStack(EquipmentSlot.HEAD).getItem() == BMItems.WITCH_HAT;
	}

	public PlayerEntity getCurrentCustomer()
	{
		return customer;
	}

	public WitchQuestList getQuests()
	{
		return quests;
	}

	public void setQuestsFromServer(WitchQuestList quests)
	{
		this.quests = quests;
	}

	public void completeQuest(WitchQuest quest)
	{

	}

	@Override
	public World getWitchWorld()
	{
		return world;
	}

	public SoundEvent getYesSound()
	{
		return SoundEvents.ENTITY_WITCH_CELEBRATE;
	}

	public void writeCustomDataToTag(CompoundTag tag)
	{
		super.writeCustomDataToTag(tag);
		tag.put("Quests", quests.toTag());
		tag.putInt("DespawnShield", despawnShield);
		tag.putInt("ReplenishTime", replenishTime);
	}

	@Override
	public void readCustomDataFromTag(CompoundTag tag)
	{
		super.readCustomDataFromTag(tag);
		quests = new WitchQuestList(tag.getCompound("Quests"));
		despawnShield = tag.getInt("DespawnShield");
		replenishTime = tag.getInt("ReplenishTime");
	}

	protected WitchEntityMixin(EntityType<? extends RaiderEntity> entityType, World world)
	{
		super(entityType, world);
	}
}
