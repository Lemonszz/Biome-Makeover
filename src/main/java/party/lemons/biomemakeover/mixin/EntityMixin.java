package party.lemons.biomemakeover.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.init.BMEnchantments;

@Mixin(Entity.class)
public abstract class EntityMixin
{
	@Shadow public abstract void setSwimming(boolean swimming);

	@Shadow private int fireTicks;

	@Shadow public abstract int getAir();

	@Shadow public abstract void setAir(int air);

	@Inject(at = @At("HEAD"), method = "getMaxAir", cancellable = true)
	public void getMaxAir(CallbackInfoReturnable<Integer> cbi)
	{
		if((Entity) ((Object)this) instanceof LivingEntity)
		{
			if(((Entity) ((Object) this)).age > 20) //Max air gets checked before inventory is created, this prevents a npe
			{
				float level = (float) EnchantmentHelper.getEquipmentLevel(BMEnchantments.SUFFOCATION_CURSE, (LivingEntity) ((Object) this));
				if(level > 0)
				{
					int maxAir = (int) (300F / ((level) * 1.5F));
					if(getAir() > maxAir)
						setAir(maxAir);
					cbi.setReturnValue(maxAir);
				}
			}
		}
	}

	@Inject(at = @At("HEAD"), method = "setFireTicks", cancellable = true)
	public void setFireTicks(int ticks, CallbackInfo cbi)
	{
		if(ticks > fireTicks)
		{
			if((Entity) ((Object)this) instanceof LivingEntity)
			{
				float level = (float)EnchantmentHelper.getEquipmentLevel(BMEnchantments.FLAMMABILITY_CURSE, (LivingEntity) ((Object)this));
				if(level > 0)
				{
					ticks += ((float)ticks * (level / 2));
					fireTicks = ticks;
					cbi.cancel();
				}
			}
		}
	}


	@Inject(at = @At("HEAD"), method = "updateSwimming", cancellable = true)
	public void updateSwimming(CallbackInfo cbi)
	{
		if((Entity) ((Object)this) instanceof LivingEntity)
		{
			if(EnchantmentHelper.getEquipmentLevel(BMEnchantments.DEPTH_CURSE, (LivingEntity) ((Object) this)) > 0)
			{
				setSwimming(false);
			}
		}
	}

	@Inject(at = @At("HEAD"), method = "onBubbleColumnCollision", cancellable = true)
	public void onBubbleColumnCollision(boolean drag, CallbackInfo cbi)
	{
		if(!drag && (Entity) ((Object)this) instanceof LivingEntity)
		{
			if(EnchantmentHelper.getEquipmentLevel(BMEnchantments.DEPTH_CURSE, (LivingEntity) ((Object) this)) > 0)
			{
				cbi.cancel();
			}
		}
	}

	@Inject(at = @At("HEAD"), method = "onBubbleColumnSurfaceCollision", cancellable = true)
	public void onBubbleColumnSurfaceCollision(boolean drag, CallbackInfo cbi)
	{
		if(!drag && (Entity) ((Object)this) instanceof LivingEntity)
		{
			if(EnchantmentHelper.getEquipmentLevel(BMEnchantments.DEPTH_CURSE, (LivingEntity) ((Object) this)) > 0)
			{
				cbi.cancel();
			}
		}
	}
}
