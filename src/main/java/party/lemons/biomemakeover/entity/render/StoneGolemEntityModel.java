package party.lemons.biomemakeover.entity.render;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.client.render.entity.model.CrossbowPosing;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import party.lemons.biomemakeover.entity.StoneGolemEntity;
import party.lemons.biomemakeover.util.AnimationHelper;

public class StoneGolemEntityModel extends CompositeEntityModel<StoneGolemEntity> implements ModelWithHead, ModelWithArms
{
	private final ModelPart lower_base;
	private final ModelPart body;
	private final ModelPart arm_left;
	private final ModelPart arm_right;
	private final ModelPart head;
	private final ModelPart horns;

	public StoneGolemEntityModel()
	{
		textureWidth = 128;
		textureHeight = 128;

		lower_base = new ModelPart(this);
		lower_base.setPivot(0.0F, 16.0F, 0.0F);
		lower_base.setTextureOffset(0, 22).addCuboid(-7.0F, 0.0F, -7.0F, 14.0F, 8.0F, 14.0F, 0.0F, false);
		lower_base.setTextureOffset(56, 0).addCuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);

		body = new ModelPart(this);
		body.setPivot(0.0F, 9.7F, 0.0F);
		body.setTextureOffset(0, 0).addCuboid(-9.0F, -16.7F, -5.0F, 18.0F, 12.0F, 10.0F, 0.0F, false);
		body.setTextureOffset(42, 22).addCuboid(-7.0F, -4.7F, -3.0F, 14.0F, 5.0F, 6.0F, 0.0F, false);

		arm_left = new ModelPart(this);
		arm_left.setPivot(8.5F, -14.2F, 0.0F);
		body.addChild(arm_left);
		arm_left.setTextureOffset(0, 44).addCuboid(0.5F, 1.5F, -3.0F, 4.0F, 26.0F, 6.0F, 0.0F, false);
		arm_left.setTextureOffset(40, 62).addCuboid(0.5F, -2.5F, -4.0F, 6.0F, 8.0F, 8.0F, 0.0F, false);

		arm_right = new ModelPart(this);
		arm_right.setPivot(-8.5F, -14.2F, 0.0F);
		body.addChild(arm_right);
		arm_right.setTextureOffset(20, 44).addCuboid(-4.5F, 1.5F, -3.0F, 4.0F, 26.0F, 6.0F, 0.0F, false);
		arm_right.setTextureOffset(64, 33).addCuboid(-6.5F, -2.5F, -4.0F, 6.0F, 8.0F, 8.0F, 0.0F, false);

		head = new ModelPart(this);
		head.setPivot(0.0F, -15.7F, -3.5F);
		body.addChild(head);
		head.setTextureOffset(40, 44).addCuboid(-4.0F, -11.0F, -3.5F, 8.0F, 10.0F, 8.0F, 0.0F, false);
		head.setTextureOffset(60, 62).addCuboid(-5.0F, -6.0F, -5.5F, 10.0F, 2.0F, 2.0F, 0.0F, false);
		head.setTextureOffset(6, 22).addCuboid(-1.0F, -4.0F, -5.5F, 2.0F, 4.0F, 2.0F, 0.0F, false);
		head.setTextureOffset(56, 16).addCuboid(-5.0F, -2.0F, -4.5F, 10.0F, 3.0F, 3.0F, 0.0F, false);

		horns = new ModelPart(this);
		horns.setPivot(4.5F, -10.5F, -3.5F);
		head.addChild(horns);
		setRotationAngle(horns, 0.7854F, 0.0F, 0.0F);
		horns.setTextureOffset(0, 22).addCuboid(-9.5F, -2.5F, -1.0F, 1.0F, 6.0F, 2.0F, 0.0F, false);
		horns.setTextureOffset(0, 0).addCuboid(-0.5F, -2.5F, -1.0F, 1.0F, 6.0F, 2.0F, 0.0F, false);
	}

	@Override
	public void setAngles(StoneGolemEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch)
	{
		AnimationHelper.rotateHead(head, headPitch, headYaw);

		IllagerEntity.State state = entity.getState();

		if(state == IllagerEntity.State.NEUTRAL)
		{
			AnimationHelper.setRotation(arm_left, 0, 0,0 );
			AnimationHelper.setRotation(arm_right, 0, 0,0 );
		}
		else if(state == IllagerEntity.State.ATTACKING)
		{
			if (entity.getMainHandStack().isEmpty()) {
				CrossbowPosing.method_29352(this.arm_left, this.arm_right, true, this.handSwingProgress, animationProgress);
			} else {
				CrossbowPosing.method_29351(this.arm_right, this.arm_left, entity, this.handSwingProgress, animationProgress);
			}
		}
		else if (state == IllagerEntity.State.CROSSBOW_HOLD)
		{
			hold(this.arm_right, this.arm_left, this.head, true);
		}
		else if (state == IllagerEntity.State.CROSSBOW_CHARGE)
		{
			charge(this.arm_right, this.arm_left, entity, true);
		}
	}

	public static void charge(ModelPart holdingArm, ModelPart pullingArm, LivingEntity actor, boolean rightArmed) {
		ModelPart modelPart = rightArmed ? holdingArm : pullingArm;
		ModelPart pullArm = rightArmed ? pullingArm : holdingArm;
		modelPart.yaw = rightArmed ? -0.8F : 0.8F;
		modelPart.pitch = -0.97079635F;
		pullArm.pitch = modelPart.pitch;
		float f = (float) CrossbowItem.getPullTime(actor.getActiveItem());
		float g = MathHelper.clamp((float)actor.getItemUseTime(), 0.0F, f);
		float h = g / f;
		pullArm.yaw = MathHelper.lerp(h, 0.2F, 0.4F) * (float)(rightArmed ? 1 : -1);
		pullArm.pitch = MathHelper.lerp(h, pullArm.pitch, -1);
	}

	public static void hold(ModelPart holdingArm, ModelPart otherArm, ModelPart head, boolean rightArmed) {
		ModelPart modelPart = rightArmed ? holdingArm : otherArm;
		ModelPart modelPart2 = rightArmed ? otherArm : holdingArm;
		modelPart.yaw = (rightArmed ? -0.3F : 0.3F) + head.yaw;
		modelPart2.yaw = (rightArmed ? 0.6F : -0.6F) + head.yaw;
		modelPart.pitch = -1.25F + head.pitch + 0.1F;
		modelPart2.pitch = -1.25F + head.pitch;
	}



	@Override
	public Iterable<ModelPart> getParts()
	{
		return ImmutableList.of(body);
	}

	public void renderBase(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha) {
		lower_base.render(matrices, vertices, light, overlay, red, green, blue, alpha);
	}

	public void setRotationAngle(ModelPart bone, float x, float y, float z) {
		bone.pitch = x;
		bone.yaw = y;
		bone.roll = z;
	}

	@Override
	public void setArmAngle(Arm arm, MatrixStack matrices)
	{
		if(arm == Arm.LEFT)
			arm_left.rotate(matrices);
		else
			arm_right.rotate(matrices);
	}

	@Override
	public ModelPart getHead()
	{
		return head;
	}
}