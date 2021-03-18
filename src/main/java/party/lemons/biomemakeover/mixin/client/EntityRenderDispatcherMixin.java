package party.lemons.biomemakeover.mixin.client;

import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonEntity;
import net.minecraft.entity.boss.dragon.EnderDragonPart;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.entity.EntityPart;
import party.lemons.biomemakeover.entity.MultiPartEntity;

import java.util.List;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin
{
	@Shadow protected abstract void drawBox(MatrixStack matrix, VertexConsumer vertices, Entity entity, float red, float green, float blue);

	/**
		Render multipart entity hitbox
	 **/
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/EntityRenderDispatcher;drawBox(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/entity/Entity;FFF)V", ordinal = 0), method = "renderHitbox")
	private void onRenderHitBox(MatrixStack matrices, VertexConsumer vertices, Entity entity, float tickDelta, CallbackInfo cbi)
	{
		if (entity instanceof MultiPartEntity) {
			double rX = -MathHelper.lerp(tickDelta, entity.lastRenderX, entity.getX());
			double rY = -MathHelper.lerp(tickDelta, entity.lastRenderY, entity.getY());
			double rZ = -MathHelper.lerp(tickDelta, entity.lastRenderZ, entity.getZ());
			List<EntityPart> parts = ((MultiPartEntity)entity).getParts();

			for(int i = 0; i < parts.size(); ++i) {
				EntityPart part = parts.get(i);
				matrices.push();
				double x = rX + MathHelper.lerp(tickDelta, part.lastRenderX, part.getX());
				double y = rY + MathHelper.lerp(tickDelta, part.lastRenderY, part.getY());
				double z = rZ + MathHelper.lerp(tickDelta, part.lastRenderZ, part.getZ());
				matrices.translate(x, y, z);
				this.drawBox(matrices, vertices, part, 0.3F, 0.2F, 0.65F);
				matrices.pop();
			}
		}
	}
}
