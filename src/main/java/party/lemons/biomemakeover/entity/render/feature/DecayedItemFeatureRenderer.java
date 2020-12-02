package party.lemons.biomemakeover.entity.render.feature;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import party.lemons.biomemakeover.entity.DecayedEntity;

public class DecayedItemFeatureRenderer<T extends DecayedEntity, M extends EntityModel<T> & ModelWithArms> extends HeldItemFeatureRenderer<T, M>
{
	public DecayedItemFeatureRenderer(FeatureRendererContext<T, M> featureRendererContext)
	{
		super(featureRendererContext);
	}

	public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, T livingEntity, float f, float g, float h, float j, float k, float l) {
		boolean isRightHanded = livingEntity.getMainArm() == Arm.RIGHT;

		ItemStack leftHand = isRightHanded ? livingEntity.getOffHandStack() : livingEntity.getMainHandStack();
		ItemStack rightHand = isRightHanded ? livingEntity.getMainHandStack() : livingEntity.getOffHandStack();

		if (!leftHand.isEmpty() || !rightHand.isEmpty())
		{
			matrixStack.push();
			if (this.getContextModel().child) {
				float scale = 0.5F;
				matrixStack.translate(0.0D, 0.75D, 0.0D);
				matrixStack.scale(scale, scale, scale);
			}

			if(isShield(rightHand) && livingEntity.getActiveItem() == rightHand)
				renderShield(livingEntity, rightHand, ModelTransformation.Mode.THIRD_PERSON_RIGHT_HAND, Arm.RIGHT, matrixStack, vertexConsumerProvider, i);
			else
				this.renderItem(livingEntity, rightHand, ModelTransformation.Mode.THIRD_PERSON_RIGHT_HAND, Arm.RIGHT, matrixStack, vertexConsumerProvider, i);

			if(isShield(leftHand) && livingEntity.getActiveItem() == leftHand)
				this.renderShield(livingEntity, leftHand, ModelTransformation.Mode.THIRD_PERSON_LEFT_HAND, Arm.LEFT, matrixStack, vertexConsumerProvider, i);
			else
				this.renderItem(livingEntity, leftHand, ModelTransformation.Mode.THIRD_PERSON_LEFT_HAND, Arm.LEFT, matrixStack, vertexConsumerProvider, i);


			matrixStack.pop();
		}
	}

	private boolean isShield(ItemStack sheild)
	{
		return sheild.getItem() == Items.SHIELD;
	}

	private void renderShield(LivingEntity entity, ItemStack stack, ModelTransformation.Mode transformationMode, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		if (!stack.isEmpty())
		{
			matrices.push();

			this.getContextModel().setArmAngle(arm, matrices);
			matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-90.0F));
			matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
			boolean isLeft = arm == Arm.LEFT;
			matrices.translate(2F * (isLeft ? 1F : -1F) / 16.0F, 0.3D, -0.625D);
			Vector3f dirY = isLeft ? Vector3f.NEGATIVE_Y : Vector3f.POSITIVE_Y;
			Vector3f dirZ = isLeft ? Vector3f.POSITIVE_Z : Vector3f.NEGATIVE_Z;
			Vector3f dirX = Vector3f.NEGATIVE_X;

			matrices.multiply(dirY.getDegreesQuaternion(60));
			matrices.multiply(dirZ.getDegreesQuaternion(0));
			matrices.multiply(dirX.getDegreesQuaternion(25));

			MinecraftClient.getInstance().getHeldItemRenderer().renderItem(entity, stack, transformationMode, isLeft, matrices, vertexConsumers, light);
			matrices.pop();
		}
	}

	private void renderItem(LivingEntity entity, ItemStack stack, ModelTransformation.Mode transformationMode, Arm arm, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
		if (!stack.isEmpty()) {
			matrices.push();
			this.getContextModel().setArmAngle(arm, matrices);
			matrices.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(-90.0F));
			matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
			boolean isLeft = arm == Arm.LEFT;
			matrices.translate((float)(isLeft ? -1 : 1) / 16.0F, 0.125D, -0.625D);
			MinecraftClient.getInstance().getHeldItemRenderer().renderItem(entity, stack, transformationMode, isLeft, matrices, vertexConsumers, light);
			matrices.pop();
		}
	}
}
