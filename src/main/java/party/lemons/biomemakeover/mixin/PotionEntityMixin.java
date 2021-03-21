package party.lemons.biomemakeover.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.util.math.Box;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import party.lemons.biomemakeover.entity.EntityPart;

import java.util.List;

@Mixin(PotionEntity.class)
public class PotionEntityMixin
{
	@Inject(at = @At(value = "INVOKE_ASSIGN", target = "net/minecraft/world/World.getNonSpectatingEntities(Ljava/lang/Class;Lnet/minecraft/util/math/Box;)Ljava/util/List;"), method = "applySplashPotion", locals = LocalCapture.CAPTURE_FAILSOFT)
	private void onApplySplashPotion(List<StatusEffectInstance> statusEffects, @Nullable Entity entity, CallbackInfo cbi, Box box, List list)
	{
		if(entity instanceof EntityPart && !list.contains(((EntityPart<?>) entity).owner))
			list.add(((EntityPart) entity).owner);
	}
}
