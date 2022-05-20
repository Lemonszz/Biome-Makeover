package party.lemons.biomemakeover.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorMimicEntity;

public class AdjudicatorMimicRender extends MobRenderer<AdjudicatorMimicEntity, AdjudicatorModel<AdjudicatorMimicEntity>> {

    private static final ResourceLocation TEXTURE = BiomeMakeover.ID("textures/entity/adjudicator.png");


    public AdjudicatorMimicRender(EntityRendererProvider.Context context) {
        super(context, new AdjudicatorModel<>(context.bakeLayer(AdjudicatorModel.LAYER_LOCATION)), 0.25F);

        addLayer(new AdjudicatorRender.AdjudicatorEyesRenderLayer<>(this));
        addLayer(new AdjudicatorRender.AdjudicatorHeldItemRenderer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(AdjudicatorMimicEntity entity) {
        return TEXTURE;
    }
}
