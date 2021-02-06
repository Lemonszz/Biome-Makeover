package party.lemons.biomemakeover.entity.render.feature;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.RootlingEntity;
import party.lemons.biomemakeover.entity.render.RootlingEntityModel;

public class RootlingFlowerFeatureRenderer extends FeatureRenderer<RootlingEntity, RootlingEntityModel>
{
	private static final Identifier[] TEXTURES = {BiomeMakeover.ID("textures/entity/rootling/rootling_flower_blue.png"), BiomeMakeover.ID("textures/entity/rootling/rootling_flower_brown.png"), BiomeMakeover.ID("textures/entity/rootling/rootling_flower_cyan.png"), BiomeMakeover.ID("textures/entity/rootling/rootling_flower_grey.png"), BiomeMakeover.ID("textures/entity/rootling/rootling_flower_light_blue.png"), BiomeMakeover.ID("textures/entity/rootling/rootling_flower_purple.png"),};
	private final RootlingEntityModel model = new RootlingEntityModel();

	public RootlingFlowerFeatureRenderer(FeatureRendererContext<RootlingEntity, RootlingEntityModel> featureRendererContext)
	{
		super(featureRendererContext);
	}

	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, RootlingEntity entity, float f, float g, float h, float j, float k, float l)
	{

		if(entity.hasFlower())
		{
			render(this.getContextModel(), this.model, TEXTURES[entity.getFlowerIndex()], matrixStack, vertexConsumerProvider, i, entity, f, g, j, k, l, h, 1.0F, 1.0F, 1.0F);
		}
	}

}
