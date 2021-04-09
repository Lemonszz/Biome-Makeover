package party.lemons.biomemakeover.entity.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.EnergySwirlOverlayFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HeldItemFeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.WitherEntityModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.boss.WitherEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;
import party.lemons.biomemakeover.entity.render.feature.AdjudicatorEyesRenderLayer;

public class AdjudicatorEntityRender extends MobEntityRenderer<AdjudicatorEntity, AdjudicatorEntityModel<AdjudicatorEntity>>
{
	private static final Identifier TEXTURE = BiomeMakeover.ID("textures/entity/adjudicator.png");

	public AdjudicatorEntityRender(EntityRenderDispatcher rd)
	{
		super(rd, new AdjudicatorEntityModel(), 0.25F);
		addFeature(new AdjudicatorHeldItemRenderer(this));
		addFeature(new AdjudicatorEyesRenderLayer(this));
		addFeature(new InvulnerableFeatureRenderer(this));
	}

	@Override
	protected void setupTransforms(AdjudicatorEntity entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta)
	{
		super.setupTransforms(entity, matrices, animationProgress, bodyYaw, tickDelta);

		switch(entity.getState())
		{
			case WAITING:
				float waitAng = MathHelper.lerpAngleDegrees(tickDelta, entity.renderRotPrevious, entity.stateTime * waitingRotSpeed(tickDelta));
				matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(waitAng));
				matrices.translate(0, Math.sin(animationProgress / 25F) / 4F, 0);
				entity.renderRotPrevious = waitAng;
				break;
			case TELEPORT:
				float teleAngle = MathHelper.lerpAngleDegrees(tickDelta, entity.renderRotPrevious, entity.stateTime * teleRotSpeed(entity.stateTime));
				matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(teleAngle));
				entity.renderRotPrevious = teleAngle;
				break;
		}
	}

	private float teleRotSpeed(int time)
	{
		return time;
	}

	private float waitingRotSpeed(float delta)
	{
		return 1F;
	}


	@Override
	public Identifier getTexture(AdjudicatorEntity entity)
	{
		return TEXTURE;
	}

	private static class AdjudicatorHeldItemRenderer extends HeldItemFeatureRenderer<AdjudicatorEntity, AdjudicatorEntityModel<AdjudicatorEntity>>
	{

		public AdjudicatorHeldItemRenderer(FeatureRendererContext<AdjudicatorEntity, AdjudicatorEntityModel<AdjudicatorEntity>> featureRendererContext)
		{
			super(featureRendererContext);
		}

		@Override
		public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, AdjudicatorEntity boss, float f, float g, float h, float j, float k, float l) {
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

	private class InvulnerableFeatureRenderer extends EnergySwirlOverlayFeatureRenderer<AdjudicatorEntity, AdjudicatorEntityModel<AdjudicatorEntity>> {
		private final Identifier SKIN = new Identifier("textures/entity/wither/wither_armor.png");
		private final AdjudicatorEntityModel<AdjudicatorEntity> model = new AdjudicatorEntityModel<>();

		public InvulnerableFeatureRenderer(FeatureRendererContext<AdjudicatorEntity,AdjudicatorEntityModel<AdjudicatorEntity>> featureRendererContext) {
			super(featureRendererContext);
		}

		protected float getEnergySwirlX(float partialAge) {
			return MathHelper.cos(partialAge * 0.02F) * 3.0F;
		}

		protected Identifier getEnergySwirlTexture() {
			return SKIN;
		}

		protected AdjudicatorEntityModel<AdjudicatorEntity> getEnergySwirlModel() {
			return this.model;
		}
	}

}