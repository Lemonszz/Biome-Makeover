package party.lemons.biomemakeover.entity.render;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.GhoulFishEntity;

public class GhoulFishRender extends MobEntityRenderer<GhoulFishEntity, GhoulFishModel>
{
	private static final Identifier TEXTURE = new Identifier(BiomeMakeover.MODID, "textures/entity/ghoul_fish.png");

	public GhoulFishRender(EntityRenderDispatcher entityRenderDispatcher)
	{
		super(entityRenderDispatcher, new GhoulFishModel(), 0.0F);
	}

	@Override
	public Identifier getTexture(GhoulFishEntity entity)
	{
		return TEXTURE;
	}
}