package party.lemons.biomemakeover.entity.render;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import party.lemons.biomemakeover.entity.GhostEntity;

public class GhostRender extends MobEntityRenderer<GhostEntity, GhostEntityModel<GhostEntity>>
{
	private static final Identifier TEXTURE = new Identifier("textures/entity/illager/pillager.png");

	public GhostRender(EntityRenderDispatcher entityRenderDispatcher)
	{
		super(entityRenderDispatcher, new GhostEntityModel(0.0F, 0.0F, 64, 64), 0.5F);
		this.addFeature(new HeldItemFeatureRenderer(this));
	}

	@Override
	public void render(GhostEntity ghost, float f, float g, MatrixStack matrixStack, VertexConsumerProvider provider, int i)
	{
		super.render(ghost, f, g, matrixStack, provider, i);
	}

	public Identifier getTexture(GhostEntity entity)
	{
		return TEXTURE;
	}
}