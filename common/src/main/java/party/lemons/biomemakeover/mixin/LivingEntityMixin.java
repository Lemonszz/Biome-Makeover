package party.lemons.biomemakeover.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.util.EntityUtil;

@Mixin(LivingEntity.class)
public class LivingEntityMixin
{
    @Inject(at = @At("HEAD"), method = "hurt", cancellable = true)
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cbi)
    {
        if(EntityUtil.attemptProjectileResistanceBlock((LivingEntity) (Object)this, source))
        {
            cbi.setReturnValue(true);
        }
    }
}
