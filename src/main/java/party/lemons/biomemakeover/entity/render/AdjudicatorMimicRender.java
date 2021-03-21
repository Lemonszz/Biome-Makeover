package party.lemons.biomemakeover.entity.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorMimicEntity;
import party.lemons.biomemakeover.entity.render.feature.AdjudicatorEyesRenderLayer;
import party.lemons.biomemakeover.entity.render.feature.AdjudicatorMimicEyesRenderLayer;

public class AdjudicatorMimicRender extends MobEntityRenderer<AdjudicatorMimicEntity, AdjudicatorEntityModel<AdjudicatorMimicEntity>>
{
	private static final Identifier TEXTURE = BiomeMakeover.ID("textures/entity/adjudicator.png");

	public AdjudicatorMimicRender(EntityRenderDispatcher rd)
	{
		super(rd, new AdjudicatorEntityModel(), 0);
		addFeature(new AdjudicatorMimicEyesRenderLayer(this));
		addFeature(new AdjudicatorMimicHeldItemRenderer(this));

	}

	@Override
	public Identifier getTexture(AdjudicatorMimicEntity entity)
	{
		return TEXTURE;
	}

	private static class AdjudicatorMimicHeldItemRenderer extends HeldItemFeatureRenderer<AdjudicatorMimicEntity, AdjudicatorEntityModel<AdjudicatorMimicEntity>>
	{

		public AdjudicatorMimicHeldItemRenderer(FeatureRendererContext<AdjudicatorMimicEntity, AdjudicatorEntityModel<AdjudicatorMimicEntity>> featureRendererContext)
		{
			super(featureRendererContext);
		}

		@Override
		public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, AdjudicatorMimicEntity boss, float f, float g, float h, float j, float k, float l) {
			boolean rightHanded = boss.getMainArm() == Arm.RIGHT;
			ItemStack leftHand = rightHanded ? boss.getOffHandStack() : boss.getMainHandStack();
			ItemStack rightHand = rightHanded ? boss.getMainHandStack() : boss.getOffHandStack();
			if (!leftHand.isEmpty() || !rightHand.isEmpty()) {
				matrixStack.push();
				matrixStack.translate(0, 0.75F, 0);
				this.renderItem(boss, rightHand, ModelTransformation.Mode.THIRD_PERSON_RIGHT_HAND, Arm.RIGHT, matrixStack, vertexConsumerProvider, i);
				this.renderItem(boss, leftHand, ModelTransformation.Mode.THIRD_PERSON_LEFT_HAND, Arm.LEFT, matrixStack, vertexConsumerProvider, i);
				matrixStack.pop();
			}
		}

		private void renderItem(LivingEntity entity, ItemStack stack, ModelTransformation.Mode transformationMode, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
			if (!stack.isEmpty()) {
				matrices.push();
				((ModelWithArms)this.getContextModel()).setArmAngle(arm, matrices);
				matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-90.0F));
				matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
				boolean isLeft = arm == Arm.LEFT;
				matrices.translate((isLeft ? -0.7F : 0.7F) / 16.0F, 0.125D, -0.625D);
				MinecraftClient.getInstance().getHeldItemRenderer().renderItem(entity, stack, transformationMode, isLeft, matrices, vertexConsumers, light);
				matrices.pop();
			}
		}
	}
}
