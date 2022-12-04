package party.lemons.biomemakeover.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.ToadEntity;

import java.util.HashMap;
import java.util.List;

public class ToadRender extends MobRenderer<ToadEntity, ToadModel>
{
    private static final ResourceLocation TEXTURE = BiomeMakeover.ID("textures/entity/toad.png");


    public ToadRender(EntityRendererProvider.Context context) {
        super(context, new ToadModel(context.bakeLayer(ToadModel.LAYER_LOCATION)), 0.25F);
        
        addLayer(new ToadToungeLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(ToadEntity entity) {
        return TEXTURE;
    }

    private class ToadToungeLayer extends RenderLayer<ToadEntity, ToadModel> {
        final ModelPart tounge;

        public ToadToungeLayer(ToadRender toadRender)
        {
            super(toadRender);

            this.tounge = getModel().getTounge();
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, ToadEntity entity, float f, float g, float h, float j, float k, float l)
        {
            ModelPart.Cube cube = tounge.getRandomCube(entity.getRandom());

            poseStack.pushPose();
            poseStack.translate(0, 1.5, 0);
            if(tounge.zRot != 0.0F)
            {
                poseStack.mulPose(Axis.ZP.rotationDegrees(tounge.zRot));
            }

            if(tounge.yRot != 0.0F)
            {
                poseStack.mulPose(Axis.YP.rotationDegrees(tounge.yRot));
            }

            if(tounge.xRot != 0.0F)
            {
                poseStack.mulPose(Axis.XP.rotationDegrees(tounge.xRot));
            }
            drawBox(poseStack, multiBufferSource.getBuffer(getModel().renderType(TEXTURE)), cube.minX, cube.minY, cube.minZ, cube.maxX, cube.maxY, cube.minZ - entity.tongueDistance, i, OverlayTexture.NO_OVERLAY);
            poseStack.popPose();

        }
        public static void drawBox(PoseStack matrices, VertexConsumer vertexConsumer, float x1, float y1, float z1, float x2, float y2, float z2, int light, int overlay)
        {
            ModelPart part = new ModelPart(List.of(new ModelPart.Cube(0, 30, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, 0, 0, 0, false, 64, 54)), new HashMap<>());
            part.render(matrices, vertexConsumer, light, overlay);
        }

        private static final ResourceLocation TEXTURE = BiomeMakeover.ID("textures/entity/toad_tounge.png");
    }
}
