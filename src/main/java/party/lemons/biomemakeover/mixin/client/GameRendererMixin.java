package party.lemons.biomemakeover.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ItemFrameEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import party.lemons.biomemakeover.entity.EntityPart;

@Mixin(GameRenderer.class)
public class GameRendererMixin
{
	@Shadow @Final private MinecraftClient client;

	@Inject(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/util/math/Vec3d;squaredDistanceTo(Lnet/minecraft/util/math/Vec3d;)D", ordinal = 1), method = "updateTargetedEntity", locals = LocalCapture.CAPTURE_FAILSOFT)
	private void onUpdateTargetEntity(float tickDelta, CallbackInfo cbi, Entity entity, double d, Vec3d vec3d, boolean bl, int i, double e, Vec3d vec3d2, Vec3d vec3d3, float f, Box box, EntityHitResult entityHitResult, Entity entity2, Vec3d vec3d4, double g)
	{
		if (g < e || this.client.crosshairTarget == null) {
			this.client.crosshairTarget = entityHitResult;
			if (entity2 instanceof EntityPart) {
				this.client.targetedEntity = entity2;
			}
		}
	}
}
