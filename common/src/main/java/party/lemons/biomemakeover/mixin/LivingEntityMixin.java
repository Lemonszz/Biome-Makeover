package party.lemons.biomemakeover.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.util.EntityUtil;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin
{
    @Shadow @Nullable public abstract AttributeInstance getAttribute(Attribute attribute);

    @Inject(at = @At("HEAD"), method = "hurt", cancellable = true)
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cbi)
    {
        if(EntityUtil.attemptProjectileResistanceBlock((LivingEntity) (Object)this, source))
        {
            cbi.setReturnValue(true);
        }
    }

    @ModifyVariable(method = "getDamageAfterMagicAbsorb", at = @At(value = "STORE", ordinal = 1))
    private int applyProjectileResistance(int value)
    {
        return value + (int) Math.round(this.getAttribute(BMEntities.ATT_PROJECTILE_RESISTANCE).getValue());
    }

    @Inject(method = "createLivingAttributes", at = @At("RETURN"))
    private static void addProjectileResistanceAttribute(CallbackInfoReturnable<AttributeSupplier.Builder> cir)
    {
        cir.getReturnValue().add(BMEntities.ATT_PROJECTILE_RESISTANCE);
    }
}
