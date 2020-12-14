package party.lemons.biomemakeover.mixin;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.DisableableFollowTargetGoal;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.crafting.witch.WitchQuest;
import party.lemons.biomemakeover.crafting.witch.WitchQuestEntity;
import party.lemons.biomemakeover.crafting.witch.WitchQuestList;
import party.lemons.biomemakeover.init.BMItems;

import java.util.function.Predicate;

@Mixin(WitchEntity.class)
public abstract class WitchEntityMixin extends RaiderEntity implements WitchQuestEntity
{
	@Shadow private DisableableFollowTargetGoal<PlayerEntity> attackPlayerGoal;
	private PlayerEntity customer;
	private WitchQuestList quests;

	protected WitchEntityMixin(EntityType<? extends RaiderEntity> entityType, World world)
	{
		super(entityType, world);
		quests = new WitchQuestList();
	}

	@Inject(at = @At("TAIL"), method = "initGoals")
	public void initGoals(CallbackInfo cbi)
	{
		this.targetSelector.remove(attackPlayerGoal);
		attackPlayerGoal = new DisableableFollowTargetGoal<>(this, PlayerEntity.class, 10, true, false, (e)->e.getType() == EntityType.PLAYER && !canInteract((PlayerEntity) e));
		this.targetSelector.add(3, attackPlayerGoal);

		System.out.println("REPLACE SUCCESS??");
	}

	public void setCurrentCustomer(PlayerEntity customer)
	{
		this.customer = customer;
	}

	@Override
	public boolean canTarget(LivingEntity target)
	{
		if(target.getType() == EntityType.PLAYER && canInteract((PlayerEntity) target))
			return false;

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
		//TODO:

	}

	public SoundEvent getYesSound()
	{
		return SoundEvents.ENTITY_WITCH_CELEBRATE;
	}

	public void writeCustomDataToTag(CompoundTag tag)
	{
		super.writeCustomDataToTag(tag);
		tag.put("Quests", quests.toTag());
	}

	@Override
	public void readCustomDataFromTag(CompoundTag tag)
	{
		super.readCustomDataFromTag(tag);
		quests = new WitchQuestList(tag.getCompound("Quests"));
	}
}
