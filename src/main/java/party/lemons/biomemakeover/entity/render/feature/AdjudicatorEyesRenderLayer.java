package party.lemons.biomemakeover.entity.render.feature;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.OwlEntity;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorState;
import party.lemons.biomemakeover.entity.render.AdjudicatorEntityModel;
import party.lemons.biomemakeover.entity.render.OwlEntityModel;

public class AdjudicatorEyesRenderLayer extends EyesFeatureRenderer<AdjudicatorEntity, AdjudicatorEntityModel<AdjudicatorEntity>>
{
	private static final RenderLayer TEXTURE = RenderLayer.getEyes(BiomeMakeover.ID("textures/entity/adjudicator_eyes.png"));

	public AdjudicatorEyesRenderLayer(FeatureRendererContext<AdjudicatorEntity, AdjudicatorEntityModel<AdjudicatorEntity>> ctx)
	{
		super(ctx);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AdjudicatorEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch)
	{
		if(entity.getState() != AdjudicatorState.WAITING)
			super.render(matrices, vertexConsumers, light, entity, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch);
	}

	@Override
	public RenderLayer getEyesTexture()
	{
		return TEXTURE;
	}
}
