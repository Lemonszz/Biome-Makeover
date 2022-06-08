package party.lemons.biomemakeover.mixin.multipart.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.entity.mutipart.EntityPart;
import party.lemons.biomemakeover.entity.mutipart.MultiPartEntity;

import java.util.List;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin
{
    @Inject(at = @At("HEAD"), method = "shouldRender", cancellable = true)
    private <E extends Entity> void shouldRender(E entity, Frustum frustum, double d, double e, double f, CallbackInfoReturnable<Boolean> cbi)
    {
        if(entity instanceof EntityPart<?>)
            cbi.setReturnValue(false);
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;renderLineBox(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/phys/AABB;FFFF)V", ordinal = 0), method = "renderHitbox")
    private static void onRenderHitBox(PoseStack matrices, VertexConsumer vertices, Entity entity, float tickDelta, CallbackInfo cbi)
    {
        if (entity instanceof MultiPartEntity) {


            double rX = -Mth.lerp(tickDelta, entity.xOld, entity.getX());
            double rY = -Mth.lerp(tickDelta, entity.yOld, entity.getY());
            double rZ = -Mth.lerp(tickDelta, entity.zOld, entity.getZ());
            List<EntityPart<?>> parts = ((MultiPartEntity)entity).getParts();
            LevelRenderer.renderLineBox(matrices, vertices, entity.getBoundingBoxForCulling().move(-entity.getX(), -entity.getY(), -entity.getZ()), 0.5F, 0F, 0.5F, 0.5f);

            for (EntityPart<?> part : parts) {
                matrices.pushPose();
                double x = rX + Mth.lerp(tickDelta, part.xOld, part.getX());
                double y = rY + Mth.lerp(tickDelta, part.yOld, part.getY());
                double z = rZ + Mth.lerp(tickDelta, part.zOld, part.getZ());
                matrices.translate(x, y, z);

                float r = (Mth.sin(0.3F * entity.tickCount + 0) * 127F + 128F) / 255F;
                float g = (Mth.sin(0.3F * entity.tickCount + 2) * 127F + 128F) / 255F;
                float b = (Mth.sin(0.3F * entity.tickCount + 4) * 127F + 128F) / 255F;



                LevelRenderer.renderLineBox(matrices, vertices, part.getBoundingBox().move(-part.getX(), -part.getY(), -part.getZ()), r, g, b, 1.0f);
                matrices.popPose();
            }
        }
    }
}
