package party.lemons.biomemakeover.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.util.EntityUtil;
import party.lemons.biomemakeover.util.RandomUtil;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin
{
    /////////////////
    ///Start Projectile Resistance
    /////////////////
    @Inject(at = @At("HEAD"), method = "damage", cancellable = true)
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cbi)
    {
        if(source.isProjectile())
        {
            double protection = EntityUtil.getProjectileResistance((LivingEntity)(Object)this);
            if(protection > 0D && (RandomUtil.RANDOM.nextDouble() * 30D) < protection)
                cbi.setReturnValue(true);
        }
    }

    /////////////////
    ///End Projectile Resistance
    /////////////////

}
