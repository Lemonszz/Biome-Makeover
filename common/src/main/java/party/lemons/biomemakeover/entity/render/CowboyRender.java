package party.lemons.biomemakeover.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.IllagerRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.CowboyEntity;
import party.lemons.biomemakeover.entity.render.feature.CowboyHatRenderLayer;

public class CowboyRender extends IllagerRenderer<CowboyEntity> {
    private static final ResourceLocation TEXTURE = BiomeMakeover.ID("textures/entity/cowboy.png");
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(BiomeMakeover.ID("cowboy"), "main");

    public CowboyRender(EntityRendererProvider.Context context) {
        super(context, new IllagerModel<>(context.bakeLayer(LAYER_LOCATION)), 0.5F);

        this.addLayer(new ItemInHandLayer<>(this, context.getItemInHandRenderer()));
        addLayer(new CowboyHatRenderLayer<>(this, context.getModelSet()) {
            @Override
            protected void setup(PoseStack poseStack) {
                getParentModel().getHead().translateAndRotate(poseStack);
                poseStack.translate(0, -0.2F, 0);
            }
        });
    }

    @Override
    public ResourceLocation getTextureLocation(CowboyEntity entity) {
        return TEXTURE;
    }
}
