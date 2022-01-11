package party.lemons.biomemakeover.mixin;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.util.EntityUtil;
import party.lemons.biomemakeover.util.RandomUtil;

@Mixin(Player.class)
public abstract class PlayerMixin
{
    @Shadow public abstract void playSound(SoundEvent soundEvent, float f, float g);

    @Inject(at = @At("HEAD"), method = "hurt", cancellable = true)
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cbi)
    {
        if(EntityUtil.attemptProjectileResistanceBlock((LivingEntity) (Object)this, source))
        {
            cbi.setReturnValue(true);
        }
    }

}
