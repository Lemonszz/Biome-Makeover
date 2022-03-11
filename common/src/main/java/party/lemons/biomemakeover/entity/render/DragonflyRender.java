package party.lemons.biomemakeover.entity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.DragonflyEntity;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.util.sound.EntityLoopSoundInstance;

public class DragonflyRender extends MobRenderer<DragonflyEntity, DragonflyModel>
{
    private static final ResourceLocation[] TEXTURE = {BiomeMakeover.ID("textures/entity/dragonfly/dragonfly_0.png"), BiomeMakeover.ID("textures/entity/dragonfly/dragonfly_1.png"), BiomeMakeover.ID("textures/entity/dragonfly/dragonfly_2.png"), BiomeMakeover.ID("textures/entity/dragonfly/dragonfly_3.png"), BiomeMakeover.ID("textures/entity/dragonfly/dragonfly_4.png"),};

    public DragonflyRender(EntityRendererProvider.Context context) {
        super(context, new DragonflyModel(context.bakeLayer(DragonflyModel.LAYER_LOCATION)), 0.25F);
    }

    @Override
    public ResourceLocation getTextureLocation(DragonflyEntity entity) {
        if(!entity.hasPlayedLoop)
        {
            entity.hasPlayedLoop = true;
            Minecraft.getInstance().getSoundManager().play(new EntityLoopSoundInstance(entity, BMEffects.DRAGONFLY_LOOP.get()));
        }

        return TEXTURE[entity.getVariant() % TEXTURE.length];
    }
}
