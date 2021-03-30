package party.lemons.biomemakeover.mixin;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Pair;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.enchantments.BMEnchantment;
import party.lemons.biomemakeover.init.BMEnchantments;
import party.lemons.biomemakeover.util.ItemUtil;
import party.lemons.biomemakeover.util.NetworkUtil;
import party.lemons.biomemakeover.util.TotemItem;
import party.lemons.biomemakeover.util.extensions.LootBlocker;
import party.lemons.biomemakeover.util.extensions.SlideEntity;

import java.util.Collection;
import java.util.Iterator;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements SlideEntity, LootBlocker
{
	/////////////////
	///Start Loot Block
	///Data gets saved to NBT below
	/////////////////
	@Unique
	private boolean isLootBlocked = false;

	@Override
	@Unique
	public boolean isLootBlocked()
	{
		return isLootBlocked;
	}

	@Override
	@Unique
	public void setLootBlocked(boolean block)
	{
		this.isLootBlocked = block;
	}

	@Inject(at = @At("HEAD"), method = "shouldDropLoot", cancellable = true)
	private void shouldDropLoot(CallbackInfoReturnable<Boolean> cbi)
	{
		if(isLootBlocked())
			cbi.setReturnValue(false);
	}

	/////////////////
	///End Loot Block
	/////////////////

	/////////////////
	///Start Totem Block
	/////////////////

	@Inject(at = @At("HEAD"), method = "tryUseTotem", cancellable = true)
	private void tryUseTotem(DamageSource source, CallbackInfoReturnable<Boolean> cbi)
	{
		if (!source.isOutOfWorld())
		{
			for(Hand hand : Hand.values())
			{
				ItemStack stack = this.getStackInHand(hand);
				if (stack.getItem() instanceof TotemItem && ((TotemItem) stack.getItem()).canActivate((LivingEntity)(Object)this)) {
					ItemStack activateStack = stack.copy();
					stack.decrement(1);

					((TotemItem)activateStack.getItem()).activateTotem((LivingEntity)(Object)this, activateStack);

					cbi.setReturnValue(true);
				}
			}

		}
	}

	/////////////////
	///End Totem Block
	/////////////////

	@Shadow
	public abstract ItemStack getEquippedStack(EquipmentSlot slot);

	@Shadow public abstract ItemStack getStackInHand(Hand hand);

	@Unique
	private int slideTime = 0;

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getSlipperiness()F"), method = "travel")
	public float getSlipperiness(Block block)
	{
		float slipperiness = block.getSlipperiness();
		int slipLevel = EnchantmentHelper.getLevel(BMEnchantments.SLIDING_CURSE, getEquippedStack(EquipmentSlot.FEET));

		if(slipLevel <= 0 || !isSliding()) return block.getSlipperiness();

		slipperiness = Math.max(0.98F, slipperiness);
		slipperiness += (slipLevel * 0.005F);
		slipperiness = Math.min(0.999F, slipperiness);
		return slipperiness;
	}

	@Inject(at = @At("TAIL"), method = "tick")
	public void tick(CallbackInfo cbi)
	{
		if(!getEntityWorld().isClient())
		{
			Iterator<Pair<EquipmentSlot, ItemStack>> it = attributeStacks.iterator();
			while(it.hasNext())
			{
				Pair<EquipmentSlot, ItemStack> pair = it.next();
				ItemStack st = pair.getRight();
				if(!hasStackEquipInSlot(st, pair.getLeft()))
				{
					ItemUtil.forEachEnchantment((en, stack, lvl)->
					{
						if(en instanceof BMEnchantment)
						{
							((BMEnchantment) en).removeAttributes((LivingEntity) (Object) this, pair.getLeft());
						}
					}, st, true);
					it.remove();
				}
			}

			for(EquipmentSlot slot : EquipmentSlot.values())
			{
				ItemStack stack = getEquippedStack(slot);
				if(!stack.isEmpty())
				{
					ItemUtil.forEachEnchantment((en, st, lvl)->
					{
						if(en instanceof BMEnchantment)
						{
							((BMEnchantment) en).onTick((LivingEntity) (Object) this, st, lvl);
							if(!hasAttributeStack(st) && ((BMEnchantment) en).addAttributes((LivingEntity) (Object) this, st, slot, lvl))
							{
								attributeStacks.add(new Pair<>(slot, st));
							}
						}
					}, stack);
				}
			}


			if(slideTime > 0)
			{
				slideTime--;
				if(slideTime <= 0)
				{
					syncSlideTime();
				}
			}else
			{
				if(random.nextInt(500) == 0)
				{
					slideTime = 250 + random.nextInt(800);
					syncSlideTime();
				}
			}
		}
	}

	@ModifyArg(method = "handleFallDamage", at = @At(value = "INVOKE", target="Lnet/minecraft/entity/LivingEntity;computeFallDamage(FF)I"), index = 0)
	private float changeFallDistance(float distance){
		int fallLevel = EnchantmentHelper.getEquipmentLevel(BMEnchantments.BUCKLING_CURSE, (LivingEntity)(Object)this);

		return distance + fallLevel;
	}

	@Unique
	private void syncSlideTime()
	{
		if((Object) this instanceof ServerPlayerEntity)
		{
			NetworkUtil.sendSlideTime((PlayerEntity) (Object) this, slideTime);
		}
	}

	@Unique
	private boolean hasStackEquipInSlot(ItemStack stack, EquipmentSlot slot)
	{
		return getEquippedStack(slot).equals(stack);
	}

	@Unique
	private final Collection<Pair<EquipmentSlot, ItemStack>> attributeStacks = Lists.newArrayList();

	@Unique
	public boolean hasAttributeStack(ItemStack stack)
	{
		for(Pair<EquipmentSlot, ItemStack> pair : attributeStacks)
		{
			if(pair.getRight().equals(stack)) return true;
		}
		return false;
	}

	@Override
	@Unique
	public boolean isSliding()
	{
		return slideTime > 0;
	}

	@Override
	@Unique
	public void setSlideTime(int slideTime)
	{
		this.slideTime = slideTime;
	}

	@Override
	@Unique
	public int getSlideTime()
	{
		return slideTime;
	}

	@Inject(at = @At("TAIL"), method = "writeCustomDataToTag")
	private void writeData(CompoundTag tag, CallbackInfo cbi)
	{
		tag.putInt("BMSlideTime", slideTime);
		tag.putBoolean("BMLootBlock", isLootBlocked);
	}

	@Inject(at = @At("TAIL"), method = "readCustomDataFromTag")
	private void readData(CompoundTag tag, CallbackInfo cbi)
	{
		slideTime = tag.getInt("BMSlideTime");
		isLootBlocked = tag.getBoolean("BMLootBlock");
	}

	protected LivingEntityMixin(EntityType<? extends LivingEntity> entityType, World world)
	{
		super(entityType, world);
	}
}
