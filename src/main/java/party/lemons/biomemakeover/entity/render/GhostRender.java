package party.lemons.biomemakeover.entity.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.SlimeEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.util.Identifier;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.GhostEntity;

public class GhostRender extends MobEntityRenderer<GhostEntity, GhostEntityModel>
{
	private static final Identifier TEXTURE = BiomeMakeover.ID("textures/entity/ghost.png");

	public GhostRender(EntityRenderDispatcher entityRenderDispatcher)
	{
		super(entityRenderDispatcher, new GhostEntityModel(), 0F);
		this.addFeature(new HeldItemFeatureRenderer<>(this));
	}

	@Override
	protected RenderLayer getRenderLayer(GhostEntity entity, boolean showBody, boolean translucent, boolean showOutline)
	{
		return RenderLayer.getEntityTranslucent(this.getTexture(entity));
	}

	public Identifier getTexture(GhostEntity entity)
	{
		return TEXTURE;
	}
}