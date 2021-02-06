package party.lemons.biomemakeover.entity.render.feature;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.HorseEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.util.Identifier;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.util.HorseHat;

public class HorseHatFeatureRenderer<M extends EntityModel<HorseEntity>> extends FeatureRenderer<HorseEntity, M>
{
	public static final Identifier HAT_TEXTURE = BiomeMakeover.ID("textures/misc/cowboy_hat.png");

	//TODO: Support more hat models

	public HorseHatFeatureRenderer(FeatureRendererContext<HorseEntity, M> context)
	{
		super(context);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, HorseEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch)
	{
		if(((HorseHat) entity).hasHat())
		{
			matrices.scale(1.05F, 1.05F, 1.05F);
			CowboyHatModel<HorseEntity> hatModel = new CowboyHatModel<>();
			((ModelPart) ((HorseEntityModel) this.getContextModel()).getHeadParts().iterator().next()).rotate(matrices);
			matrices.translate(0F, -0.23F, 0.15);
			matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-25F));

			VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumers, hatModel.getLayer(this.getTexture(entity)), true, false);
			hatModel.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1F, 1F, 1F, 1F);
		}
	}

	@Override
	protected Identifier getTexture(HorseEntity entity)
	{
		return HAT_TEXTURE;
	}
}
