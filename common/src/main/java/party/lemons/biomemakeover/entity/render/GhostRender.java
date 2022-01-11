package party.lemons.biomemakeover.entity.render;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.GhostEntity;

public class GhostRender extends MobRenderer<GhostEntity, GhostModel>
{
    private static final ResourceLocation TEXTURE = BiomeMakeover.ID("textures/entity/ghost.png");
    private static final ResourceLocation TEXTURE_ANGRY = BiomeMakeover.ID("textures/entity/ghost_angry.png");

    public GhostRender(EntityRendererProvider.Context context) {
        super(context, new GhostModel(context.bakeLayer(GhostModel.LAYER_LOCATION)), 0);

        addLayer(new ItemInHandLayer<>(this));
    }

    @Nullable
    @Override
    protected RenderType getRenderType(GhostEntity livingEntity, boolean bl, boolean bl2, boolean bl3) {
        return RenderType.entityTranslucent(getTextureLocation(livingEntity));
    }

    @Override
    public ResourceLocation getTextureLocation(GhostEntity entity) {
        return entity.isCharging() ? TEXTURE_ANGRY : TEXTURE;
    }
}
