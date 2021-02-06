package party.lemons.biomemakeover.entity.render;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.util.Identifier;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.RootlingEntity;
import party.lemons.biomemakeover.entity.render.feature.RootlingFlowerFeatureRenderer;

public class RootlingEntityRender extends MobEntityRenderer<RootlingEntity, RootlingEntityModel>
{
	private static final Identifier TEXTURE = BiomeMakeover.ID("textures/entity/rootling/rootling.png");

	public RootlingEntityRender(EntityRenderDispatcher rd)
	{
		super(rd, new RootlingEntityModel(), 0.25F);
		addFeature(new RootlingFlowerFeatureRenderer(this));
	}

	@Override
	public Identifier getTexture(RootlingEntity entity)
	{
		return TEXTURE;
	}
}
