package party.lemons.biomemakeover.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;
import party.lemons.biomemakeover.entity.TumbleweedEntity;
import party.lemons.biomemakeover.init.BMBlocks;

public class TumbleweedRender extends EntityRenderer<TumbleweedEntity> {
    public TumbleweedRender(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void render(TumbleweedEntity entity, float f, float delta, PoseStack poseStack, MultiBufferSource multiBufferSource, int i) {
        super.render(entity, f, delta, poseStack, multiBufferSource, i);

        poseStack.pushPose();
        poseStack.translate(0.0D, 0.5D, 0.0D);
        poseStack.mulPose(slerp(entity.prevQuaternion, entity.quaternion, delta));

        poseStack.mulPose(Axis.YP.rotationDegrees(-90.0F));
        poseStack.translate(-0.5D, -0.5D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));

        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(BMBlocks.TUMBLEWEED.get().defaultBlockState(), poseStack, multiBufferSource, i, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();
    }

    public static Quaternionf slerp(Quaternionf v0, Quaternionf v1, float t)
    {
        // From https://en.wikipedia.org/w/index.php?title=Slerp&oldid=928959428
        // License: CC BY-SA 3.0 https://creativecommons.org/licenses/by-sa/3.0/

        float dot = v0.dot(v1);
        if(dot < 0.0f)
        {
            v1 = new Quaternionf(-v1.x(), -v1.y(), -v1.z(), -v1.w());
            dot = -dot;
        }

        if(dot > 0.9995F)
        {
            float x = Mth.lerp(t, v0.x(), v1.x());
            float y = Mth.lerp(t, v0.y(), v1.y());
            float z = Mth.lerp(t, v0.z(), v1.z());
            float w = Mth.lerp(t, v0.w(), v1.w());
            return new Quaternionf(x, y, z, w);
        }

        float angle01 = (float) Math.acos(dot);
        float angle0t = angle01 * t;
        float sin0t = Mth.sin(angle0t);
        float sin01 = Mth.sin(angle01);
        float sin1t = Mth.sin(angle01 - angle0t);

        float s1 = sin0t / sin01;
        float s0 = sin1t / sin01;

        return new Quaternionf(s0 * v0.x() + s1 * v1.x(), s0 * v0.y() + s1 * v1.y(), s0 * v0.z() + s1 * v1.z(), s0 * v0.w() + s1 * v1.w());
    }

    @Override
    public ResourceLocation getTextureLocation(TumbleweedEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
