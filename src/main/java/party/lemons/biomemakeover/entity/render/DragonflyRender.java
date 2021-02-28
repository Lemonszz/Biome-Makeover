package party.lemons.biomemakeover.entity.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.DragonflyEntity;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.util.sound.EntityLoopSoundInstance;

public class DragonflyRender extends MobEntityRenderer<DragonflyEntity, DragonflyEntityModel>
{
	private static final Identifier[] TEXTURE = {BiomeMakeover.ID("textures/entity/dragonfly/dragonfly_0.png"), BiomeMakeover.ID("textures/entity/dragonfly/dragonfly_1.png"), BiomeMakeover.ID("textures/entity/dragonfly/dragonfly_2.png"), BiomeMakeover.ID("textures/entity/dragonfly/dragonfly_3.png"), BiomeMakeover.ID("textures/entity/dragonfly/dragonfly_4.png"),};

	public DragonflyRender(EntityRenderDispatcher rd)
	{
		super(rd, new DragonflyEntityModel(), 0.25F);
	}

	@Override
	public Identifier getTexture(DragonflyEntity entity)
	{
		if(!entity.hasPlayedLoop)
		{
			entity.hasPlayedLoop = true;
			MinecraftClient.getInstance().getSoundManager().playNextTick(new EntityLoopSoundInstance(entity, BMEffects.DRAGONFLY_LOOP));
		}

		return TEXTURE[entity.getVariant() % TEXTURE.length];
	}
}
