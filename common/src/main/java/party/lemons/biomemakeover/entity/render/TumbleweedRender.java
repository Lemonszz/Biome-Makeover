package party.lemons.biomemakeover.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
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

        poseStack.mulPose(Vector3f.YP.rotationDegrees(-90.0F));
        poseStack.translate(-0.5D, -0.5D, 0.5D);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(90.0F));

        Minecraft.getInstance().getBlockRenderer().renderSingleBlock(BMBlocks.TUMBLEWEED.defaultBlockState(), poseStack, multiBufferSource, i, OverlayTexture.NO_OVERLAY);

        poseStack.popPose();
    }

    public static Quaternion slerp(Quaternion v0, Quaternion v1, float t)
    {
        // From https://en.wikipedia.org/w/index.php?title=Slerp&oldid=928959428
        // License: CC BY-SA 3.0 https://creativecommons.org/licenses/by-sa/3.0/

        float dot = v0.i() * v1.i() + v0.j() * v1.j() + v0.k() * v1.k() + v0.r() * v1.r();
        if(dot < 0.0f)
        {
            v1 = new Quaternion(-v1.i(), -v1.j(), -v1.k(), -v1.r());
            dot = -dot;
        }

        if(dot > 0.9995F)
        {
            float x = Mth.lerp(t, v0.i(), v1.i());
            float y = Mth.lerp(t, v0.j(), v1.j());
            float z = Mth.lerp(t, v0.k(), v1.k());
            float w = Mth.lerp(t, v0.r(), v1.r());
            return new Quaternion(x, y, z, w);
        }

        float angle01 = (float) Math.acos(dot);
        float angle0t = angle01 * t;
        float sin0t = Mth.sin(angle0t);
        float sin01 = Mth.sin(angle01);
        float sin1t = Mth.sin(angle01 - angle0t);

        float s1 = sin0t / sin01;
        float s0 = sin1t / sin01;

        return new Quaternion(s0 * v0.i() + s1 * v1.i(), s0 * v0.j() + s1 * v1.j(), s0 * v0.k() + s1 * v1.k(), s0 * v0.r() + s1 * v1.r());
    }

    @Override
    public ResourceLocation getTextureLocation(TumbleweedEntity entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }
}
