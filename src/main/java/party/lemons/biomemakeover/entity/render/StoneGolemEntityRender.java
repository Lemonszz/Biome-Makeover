package party.lemons.biomemakeover.entity.render;

import com.google.common.collect.ImmutableMap;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.IronGolemCrackFeatureRenderer;
import net.minecraft.client.render.entity.model.IronGolemEntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.StoneGolemEntity;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;

import java.util.Map;

public class StoneGolemEntityRender extends MobEntityRenderer<StoneGolemEntity, StoneGolemEntityModel>
{
	public static final Identifier TEXTURE = BiomeMakeover.ID("textures/entity/stone_golem/stone_golem.png");

	public StoneGolemEntityRender(EntityRenderDispatcher rd)
	{
		super(rd, new StoneGolemEntityModel(), 1F);
		addFeature(new StoneGolemItemFeature(this));
		this.addFeature(new StoneGolemCrackFeatureRenderer(this));

	}

	@Override
	public void render(StoneGolemEntity entity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i)
	{
		matrixStack.push();
		boolean visible = this.isVisible(entity);
		boolean isInvis = !visible && !entity.isInvisibleTo(MinecraftClient.getInstance().player);
		boolean outline = MinecraftClient.getInstance().hasOutline(entity);
		matrixStack.scale(-1.0F, -1.0F, 1.0F);
		matrixStack.translate(0.0D, -1.5010000467300415D, 0.0D); //????
		RenderLayer renderLayer = this.getRenderLayer(entity, visible, isInvis, outline);
		if(renderLayer != null)
		{
			VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(renderLayer);
			int overlay = getOverlay(entity, this.getAnimationCounter(entity, g));
			getModel().renderBase(matrixStack, vertexConsumer, i, overlay,1F, 1F, 1F, isInvis ? 0.15F : 1.0F);
		}
		matrixStack.pop();

		super.render(entity, f, g, matrixStack, vertexConsumerProvider, i);
	}

	@Override
	public Identifier getTexture(StoneGolemEntity entity)
	{
		return TEXTURE;
	}

	private class StoneGolemItemFeature extends FeatureRenderer<StoneGolemEntity, StoneGolemEntityModel>
	{
		public StoneGolemItemFeature(FeatureRendererContext<StoneGolemEntity, StoneGolemEntityModel> context)
		{
			super(context);
		}

		@Override
		public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, StoneGolemEntity boss, float f, float g, float h, float j, float k, float l) {
			boolean rightHanded = boss.getMainArm() == Arm.RIGHT;
			ItemStack leftHand = rightHanded ? boss.getOffHandStack() : boss.getMainHandStack();
			ItemStack rightHand = rightHanded ? boss.getMainHandStack() : boss.getOffHandStack();
			if (!leftHand.isEmpty() || !rightHand.isEmpty()) {
				matrixStack.push();
				matrixStack.translate(0, 0.5F, 0);
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
				matrices.translate((isLeft ? -0.7F : 0.7F) / 16.0F, 0.125D, -1.75);
				MinecraftClient.getInstance().getHeldItemRenderer().renderItem(entity, stack, transformationMode, isLeft, matrices, vertexConsumers, light);
				matrices.pop();
			}
		}
	}

	public static class StoneGolemCrackFeatureRenderer extends FeatureRenderer<StoneGolemEntity, StoneGolemEntityModel> {
		private static final Map<IronGolemEntity.Crack, Identifier> DAMAGE_TO_TEXTURE;

		public StoneGolemCrackFeatureRenderer(FeatureRendererContext<StoneGolemEntity, StoneGolemEntityModel> featureRendererContext) {
			super(featureRendererContext);
		}

		public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, StoneGolemEntity ironGolemEntity, float f, float g, float h, float j, float k, float l) {
			if (!ironGolemEntity.isInvisible()) {
				IronGolemEntity.Crack crack = ironGolemEntity.getCrack();
				if (crack != IronGolemEntity.Crack.NONE) {
					Identifier identifier = DAMAGE_TO_TEXTURE.get(crack);
					renderModel(this.getContextModel(), identifier, matrixStack, vertexConsumerProvider, i, ironGolemEntity, 1.0F, 1.0F, 1.0F);
				}
			}
		}

		static {
			DAMAGE_TO_TEXTURE = ImmutableMap.of(IronGolemEntity.Crack.LOW, BiomeMakeover.ID("textures/entity/stone_golem/stone_golem_damaged_light.png"), IronGolemEntity.Crack.MEDIUM, BiomeMakeover.ID("textures/entity/stone_golem/stone_golem_damaged_medium.png"), IronGolemEntity.Crack.HIGH, BiomeMakeover.ID("textures/entity/stone_golem/stone_golem_damaged_high.png"));
		}
	}

}
