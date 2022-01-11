package party.lemons.biomemakeover.entity.render;

import net.minecraft.client.model.IllagerModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.IllagerRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.CowboyEntity;
import party.lemons.biomemakeover.entity.render.feature.HatLayer;

public class CowboyRender extends IllagerRenderer<CowboyEntity> {
    private static final ResourceLocation TEXTURE = BiomeMakeover.ID("textures/entity/cowboy.png");
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(BiomeMakeover.ID("cowboy"), "main");

    public CowboyRender(EntityRendererProvider.Context context) {
        super(context, new IllagerModel<>(context.bakeLayer(LAYER_LOCATION)), 0.5F);

        this.addLayer(new ItemInHandLayer<>(this));
        this.addLayer(new HatLayer<>(this, context.getModelSet(), -0.2F));

    }

    @Override
    public ResourceLocation getTextureLocation(CowboyEntity entity) {
        return TEXTURE;
    }
}
