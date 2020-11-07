package party.lemons.biomemakeover.entity.render;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.util.Identifier;
import party.lemons.biomemakeover.entity.GhostEntity;

public class GhostRender extends MobEntityRenderer<GhostEntity, GhostEntityModel<GhostEntity>>
{
	private static final Identifier TEXTURE = new Identifier("textures/entity/illager/pillager.png");

	public GhostRender(EntityRenderDispatcher entityRenderDispatcher)
	{
		super(entityRenderDispatcher, new GhostEntityModel(0.0F, 0.0F, 64, 64), 0F);
		this.addFeature(new HeldItemFeatureRenderer(this));
	}

	public Identifier getTexture(GhostEntity entity)
	{
		return TEXTURE;
	}

}