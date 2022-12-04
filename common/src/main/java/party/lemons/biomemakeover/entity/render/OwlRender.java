package party.lemons.biomemakeover.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.EyesLayer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.OwlEntity;

public class OwlRender extends MobRenderer<OwlEntity, OwlModel>
{
    private static final ResourceLocation TEXTURE = BiomeMakeover.ID("textures/entity/owl.png");
    private static final ResourceLocation TEXTURE_EASTEREGG = BiomeMakeover.ID("textures/entity/owl_2.png");


    public OwlRender(EntityRendererProvider.Context context) {
        super(context, new OwlModel(context.bakeLayer(OwlModel.LAYER_LOCATION)), 0.5F);
        addLayer(new OwlRender.OwnEyesRenderer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(OwlEntity entity) {
        String nameString = ChatFormatting.stripFormatting(entity.getName().getString());
        if(nameString != null && nameString.equalsIgnoreCase("Hedwig"))
            return TEXTURE_EASTEREGG;

        return TEXTURE;
    }

    @Override
    protected void renderNameTag(OwlEntity entity, Component component, PoseStack poseStack, MultiBufferSource multiBufferSource, int i)
    {
        poseStack.pushPose();
        if(entity.isBaby())
            poseStack.translate(0, 0.5F, 0);

        super.renderNameTag(entity, component, poseStack, multiBufferSource, i);

        poseStack.popPose();
    }

    @Override
    protected void setupRotations(OwlEntity owl, PoseStack poseStack, float f, float g, float h) {
        super.setupRotations(owl, poseStack, f, g, h);
        if(owl.isInSittingPose()) poseStack.translate(0, -0.1F, 0);

        float i = owl.getSwimAmount(h);

        poseStack.translate(0, (i / 7F) / 2F, (i / 7F) / 2F);
        if(i > 0.0F)
        {
            poseStack.mulPose(Axis.XP.rotationDegrees(Mth.lerp(i, 0, -7.0F)));
        }
        poseStack.scale(0.75F, 0.75F, 0.75F);
    }

    public static class OwnEyesRenderer extends EyesLayer<OwlEntity, OwlModel>
    {
        private static RenderType renderType = RenderType.eyes(BiomeMakeover.ID("textures/entity/owl_eyes.png"));

        public OwnEyesRenderer(RenderLayerParent<OwlEntity, OwlModel> renderLayerParent) {
            super(renderLayerParent);
        }

        @Override
        public RenderType renderType() {
            return renderType;
        }
    }
}
