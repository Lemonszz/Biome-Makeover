package party.lemons.biomemakeover.mixin;

import com.google.common.collect.Lists;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Pair;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.enchantments.BMEnchantment;
import party.lemons.biomemakeover.init.BMEnchantments;
import party.lemons.biomemakeover.init.BMNetwork;
import party.lemons.biomemakeover.util.ItemStackUtil;
import party.lemons.biomemakeover.util.NetworkUtil;
import party.lemons.biomemakeover.util.SlideEntity;

import java.util.Collection;
import java.util.Iterator;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements SlideEntity
{
	@Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);
	private int slideTime = 0;

	@Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getSlipperiness()F"), method = "travel")
	public float getSlipperiness(Block block)
	{
		float slipperiness = block.getSlipperiness();
		int slipLevel = EnchantmentHelper.getLevel(BMEnchantments.SLIDING_CURSE, getEquippedStack(EquipmentSlot.FEET));

		if(slipLevel <= 0 || !isSliding())
			return block.getSlipperiness();

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
					ItemStackUtil.forEachEnchantment((en, stack, lvl)->
					{
						if(en instanceof BMEnchantment)
						{
							((BMEnchantment) en).removeAttributes((LivingEntity)(Object)this, pair.getLeft());
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
					ItemStackUtil.forEachEnchantment((en, st, lvl)->
					{
						if(en instanceof BMEnchantment)
						{
							((BMEnchantment) en).onTick((LivingEntity)(Object)this, st, lvl);
							if(!hasAttributeStack(st) && ((BMEnchantment)en).addAttributes((LivingEntity)(Object)this, st, slot, lvl))
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
			}
			else
			{
				if(random.nextInt(500) == 0)
				{
					slideTime = 250 + random.nextInt(800);
					syncSlideTime();
				}
			}
		}
	}

	private void syncSlideTime()
	{
		if((Object) this instanceof ServerPlayerEntity)
		{
			NetworkUtil.sendSlideTime((PlayerEntity)(Object)this, slideTime);
		}
	}

	private boolean hasStackEquipInSlot(ItemStack stack, EquipmentSlot slot)
	{
		return getEquippedStack(slot).equals(stack);
	}

	private Collection<Pair<EquipmentSlot, ItemStack>> attributeStacks = Lists.newArrayList();

	public boolean hasAttributeStack(ItemStack stack)
	{
		for(Pair<EquipmentSlot, ItemStack> pair : attributeStacks)
		{
			if(pair.getRight().equals(stack))
				return true;
		}
		return false;
	}

	@Override
	public boolean isSliding()
	{
		return slideTime > 0;
	}

	@Override
	public void setSlideTime(int slideTime)
	{
		this.slideTime = slideTime;
	}

	@Override
	public int getSlideTime()
	{
		return slideTime;
	}

	@Inject(at = @At("TAIL"), method = "writeCustomDataToTag")
	private void writeData(CompoundTag tag, CallbackInfo cbi)
	{
		tag.putInt("SlideTime", slideTime);
	}

	@Inject(at = @At("TAIL"), method = "readCustomDataFromTag")
	private void readData(CompoundTag tag, CallbackInfo cbi)
	{
		slideTime = tag.getInt("SlideTime");
	}

	protected LivingEntityMixin(EntityType<? extends LivingEntity> entityType, World world)
	{
		super(entityType, world);
	}
}
