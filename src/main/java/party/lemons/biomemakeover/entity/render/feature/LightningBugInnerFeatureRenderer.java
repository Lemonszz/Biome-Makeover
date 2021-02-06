package party.lemons.biomemakeover.entity.render.feature;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.math.BlockPos;
import party.lemons.biomemakeover.entity.LightningBugEntity;
import party.lemons.biomemakeover.entity.render.LightningBugEntityModel;
import party.lemons.biomemakeover.util.MathUtils;

public class LightningBugInnerFeatureRenderer extends FeatureRenderer<LightningBugEntity, LightningBugEntityModel>
{
	private final EntityModel<LightningBugEntity> model = new LightningBugEntityModel();

	public LightningBugInnerFeatureRenderer(FeatureRendererContext<LightningBugEntity, LightningBugEntityModel> context)
	{
		super(context);
	}

	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, LightningBugEntity entity, float f, float g, float delta, float j, float k, float l)
	{
		if(!entity.isInvisible())
		{
			this.getContextModel().copyStateTo(this.model);
			this.model.animateModel(entity, f, g, delta);
			this.model.setAngles(entity, f, g, j, k, l);
			VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucent(this.getTexture(entity)));

			Vector3f color = getColor(entity, delta);
			this.model.render(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlay(entity, 0.0F), color.getX(), color.getY(), color.getZ(), 1.0F);
		}
	}

	public Vector3f getColor(LightningBugEntity entity, float delta)
	{
		BlockPos pos = entity.getBlockPos();
		int redHash = pos.hashCode();
		int greenHash = (pos.getX() + pos.getY() * 31) * 31 + pos.getZ();
		int blueHash = (pos.getZ() + pos.getX() * 31) * 31 + pos.getY();
		float drawRed = 1F, drawGreen = 1F, drawBlue = 1F;

		float rTarget = (redHash % 255) / 255F;
		float bTarget = (greenHash % 255) / 255F;
		float gTarget = (blueHash % 255) / 255F;

		if(entity.prevRed == -1)
		{
			drawRed = rTarget;
			drawGreen = gTarget;
			drawBlue = bTarget;
		}else
		{
			drawRed = MathUtils.approachValue(entity.prevRed, rTarget, 0.025F * delta);
			drawGreen = MathUtils.approachValue(entity.prevGreen, gTarget, 0.025F * delta);
			drawBlue = MathUtils.approachValue(entity.prevBlue, bTarget, 0.025F * delta);
		}
		entity.prevRed = drawRed;
		entity.prevGreen = drawGreen;
		entity.prevBlue = drawBlue;
		return new Vector3f(drawRed, drawGreen, drawBlue);
	}
}