package party.lemons.biomemakeover.entity.render;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.OwlEntity;
import party.lemons.biomemakeover.entity.render.feature.OwlEyesRenderLayer;

public class OwlEntityRender extends MobEntityRenderer<OwlEntity, OwlEntityModel>
{
	private static final Identifier TEXTURE = BiomeMakeover.ID("textures/entity/owl.png");

	public OwlEntityRender(EntityRenderDispatcher rd)
	{
		super(rd, new OwlEntityModel(), 0.5F);
		addFeature(new OwlEyesRenderLayer(this));
	}

	@Override
	public Identifier getTexture(OwlEntity entity)
	{
		return TEXTURE;
	}

	@Override
	protected void setupTransforms(OwlEntity owl, MatrixStack matrixStack, float f, float g, float h)
	{
		super.setupTransforms(owl, matrixStack, f, g, h);
		if(owl.isInSittingPose()) matrixStack.translate(0, -0.1F, 0);

		float i = owl.getLeaningPitch(h);

		matrixStack.translate(0, (i / 7F) / 2F, (i / 7F) / 2F);
		if(i > 0.0F)
		{
			matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(MathHelper.lerp(i, 0, -7.0F)));
		}
		matrixStack.scale(0.75F, 0.75F, 0.75F);
	}
}
