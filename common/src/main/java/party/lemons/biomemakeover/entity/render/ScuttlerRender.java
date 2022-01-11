package party.lemons.biomemakeover.entity.render;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.ScuttlerEntity;

public class ScuttlerRender extends MobRenderer<ScuttlerEntity, ScuttlerModel>
{
    private static final ResourceLocation TEXTURE = BiomeMakeover.ID("textures/entity/scuttler.png");

    public ScuttlerRender(EntityRendererProvider.Context context) {
        super(context, new ScuttlerModel(context.bakeLayer(ScuttlerModel.LAYER_LOCATION)), 0.25F);
    }

    @Override
    public ResourceLocation getTextureLocation(ScuttlerEntity entity) {
        return TEXTURE;
    }
}
