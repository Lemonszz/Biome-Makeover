package party.lemons.biomemakeover.entity.render.feature;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import party.lemons.biomemakeover.entity.LightningBugEntity;
import party.lemons.biomemakeover.entity.render.LightningBugEntityModel;
import party.lemons.biomemakeover.entity.render.LightningBugOuterEntityModel;

public class LightningBugOuterFeatureRenderer extends FeatureRenderer<LightningBugEntity, LightningBugEntityModel>
{
	private final EntityModel<LightningBugEntity> model = new LightningBugOuterEntityModel();

	public LightningBugOuterFeatureRenderer(FeatureRendererContext<LightningBugEntity, LightningBugEntityModel> context)
	{
		super(context);
	}

	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, LightningBugEntity entity, float f, float g, float h, float j, float k, float l)
	{
		if(!entity.isInvisible())
		{
			this.getContextModel().copyStateTo(this.model);
			this.model.animateModel(entity, f, g, h);
			this.model.setAngles(entity, f, g, j, k, l);
			VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(RenderLayer.getEntityTranslucent(this.getTexture(entity)));
			this.model.render(matrixStack, vertexConsumer, i, LivingEntityRenderer.getOverlay(entity, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
		}
	}
}