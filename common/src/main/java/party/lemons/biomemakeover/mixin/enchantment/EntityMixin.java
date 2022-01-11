package party.lemons.biomemakeover.mixin.enchantment;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.init.BMEnchantments;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow
    private int remainingFireTicks;

    @Shadow public abstract void setSwimming(boolean swimming);
    @Shadow public abstract int getAirSupply();
    @Shadow public abstract void setAirSupply(int air);

    @Inject(at = @At("HEAD"), method = "setRemainingFireTicks", cancellable = true)
    public void onSetFire(int ticks, CallbackInfo cbi)
    {
        if(ticks > remainingFireTicks)
        {
            if((Entity) ((Object)this) instanceof LivingEntity living)
            {
                float level = (float) EnchantmentHelper.getEnchantmentLevel(BMEnchantments.FLAMMABILITY_CURSE, living);
                if(level > 0)
                {
                    ticks += ((float)ticks * (level / 2));
                    remainingFireTicks = ticks;
                    cbi.cancel();
                }
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "updateSwimming", cancellable = true)
    public void updateSwimming(CallbackInfo cbi)
    {
        if((Entity) ((Object)this) instanceof LivingEntity living)
        {
            if(EnchantmentHelper.getEnchantmentLevel(BMEnchantments.DEPTH_CURSE,living) > 0)
            {
                setSwimming(false);
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "getMaxAirSupply", cancellable = true)
    public void getMaxAir(CallbackInfoReturnable<Integer> cbi)
    {
        if((Entity) ((Object)this) instanceof LivingEntity living)
        {
            if(((Entity) ((Object) this)).tickCount > 20) //Max air gets checked before inventory is created, this prevents a npe
            {
                float level = (float) EnchantmentHelper.getEnchantmentLevel(BMEnchantments.SUFFOCATION_CURSE,living);
                if(level > 0)
                {
                    int maxAir = (int) (300F / ((level) * 1.5F));
                    if(getAirSupply() > maxAir)
                        setAirSupply(maxAir);
                    cbi.setReturnValue(maxAir);
                }
            }
        }
    }

}
