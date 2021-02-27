package party.lemons.biomemakeover.entity.render;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.OwlEntity;
import party.lemons.biomemakeover.entity.render.feature.OwlEyesRenderLayer;

public class OwlEntityRender extends MobEntityRenderer<OwlEntity, OwlEntityModel>
{
	private static final Identifier TEXTURE = BiomeMakeover.ID("textures/entity/owl.png");
	private static final Identifier TEXTURE_EASTEREGG = BiomeMakeover.ID("textures/entity/owl_2.png");

	public OwlEntityRender(EntityRenderDispatcher rd)
	{
		super(rd, new OwlEntityModel(), 0.5F);
		addFeature(new OwlEyesRenderLayer(this));
	}

	@Override
	public Identifier getTexture(OwlEntity entity)
	{
		String nameString = Formatting.strip(entity.getName().getString());
		if(nameString != null && nameString.equalsIgnoreCase("Hedwig"))
			return TEXTURE_EASTEREGG;

		return TEXTURE;
	}

	@Override
	public void render(OwlEntity owl, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i)
	{
		shadowRadius = 0.25F;
		matrixStack.push();
		if(owl.isBaby())
		{
			matrixStack.scale(0.5F, 0.5F, 0.5F);
			shadowRadius = 0.1F;
		}
		super.render(owl, f, g, matrixStack, vertexConsumerProvider, i);
		matrixStack.pop();
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
