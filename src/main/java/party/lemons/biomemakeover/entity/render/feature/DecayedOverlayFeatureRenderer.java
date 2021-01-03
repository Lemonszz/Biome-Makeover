package party.lemons.biomemakeover.entity.render.feature;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.DecayedEntity;
import party.lemons.biomemakeover.entity.render.DecayedEntityModel;

public class DecayedOverlayFeatureRenderer extends FeatureRenderer<DecayedEntity, DecayedEntityModel>
{
	private static final Identifier TEXTURE = BiomeMakeover.ID("textures/entity/decayed_outer_layer.png");
	private final DecayedEntityModel model = new DecayedEntityModel(0.25F, 0.0F, 64, 64);

	public DecayedOverlayFeatureRenderer(FeatureRendererContext<DecayedEntity, DecayedEntityModel> featureRendererContext) {
		super(featureRendererContext);
	}

	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, DecayedEntity drownedEntity, float f, float g, float h, float j, float k, float l) {
		render(this.getContextModel(), this.model, TEXTURE, matrixStack, vertexConsumerProvider, i, drownedEntity, f, g, j, k, l, h, 1.0F, 1.0F, 1.0F);
	}
}
