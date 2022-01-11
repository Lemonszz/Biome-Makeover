package party.lemons.biomemakeover.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.resources.ResourceLocation;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.RootlingEntity;

public class RootlingRender extends MobRenderer<RootlingEntity, RootlingModel> {
    private static final ResourceLocation TEXTURE = BiomeMakeover.ID("textures/entity/rootling/rootling.png");

    public RootlingRender(EntityRendererProvider.Context context) {
        super(context, new RootlingModel(context.bakeLayer(RootlingModel.LAYER_LOCATION)), 0.25F);

        addLayer(new FlowerLayer(this, context.getModelSet()));
    }

    @Override
    public ResourceLocation getTextureLocation(RootlingEntity entity) {
        return TEXTURE;
    }

    private static class FlowerLayer extends RenderLayer<RootlingEntity, RootlingModel> {
        private static final ResourceLocation[] TEXTURES = {BiomeMakeover.ID("textures/entity/rootling/rootling_flower_blue.png"), BiomeMakeover.ID("textures/entity/rootling/rootling_flower_brown.png"), BiomeMakeover.ID("textures/entity/rootling/rootling_flower_cyan.png"), BiomeMakeover.ID("textures/entity/rootling/rootling_flower_grey.png"), BiomeMakeover.ID("textures/entity/rootling/rootling_flower_light_blue.png"), BiomeMakeover.ID("textures/entity/rootling/rootling_flower_purple.png"),};
        private final RootlingModel model;

        public FlowerLayer(RootlingRender rootlingRender, EntityModelSet modelSet) {
            super(rootlingRender);
            model = new RootlingModel(modelSet.bakeLayer(RootlingModel.LAYER_LOCATION));
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, RootlingEntity entity, float f, float g, float h, float j, float k, float l) {
            if(entity.hasFlower())
            {
                coloredCutoutModelCopyLayerRender(this.getParentModel(), this.model, TEXTURES[entity.getFlowerIndex()], poseStack, multiBufferSource, i, entity, f, g, j, k, l, h, 1.0F, 1.0F, 1.0F);
            }
        }
    }
}
