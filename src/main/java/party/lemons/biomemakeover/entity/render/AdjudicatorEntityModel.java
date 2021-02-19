package party.lemons.biomemakeover.entity.render;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.client.render.entity.model.CrossbowPosing;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.mob.IllagerEntity;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;
import party.lemons.biomemakeover.util.AnimationHelper;

public class AdjudicatorEntityModel extends CompositeEntityModel<AdjudicatorEntity> implements ModelWithHead, ModelWithArms
{
	private final ModelPart body;
	private final ModelPart robe;
	private final ModelPart head;
	private final ModelPart nose;
	private final ModelPart arm_left;
	private final ModelPart arm_right;
	private final ModelPart leg_left;
	private final ModelPart leg_right;

	public AdjudicatorEntityModel()
	{
		textureWidth = 64;
		textureHeight = 64;
		body = new ModelPart(this);
		body.setPivot(0.0F, 11.25F, 0.0F);
		body.setTextureOffset(0, 20).addCuboid(-4.0F, -11.25F, -3.0F, 8.0F, 12.0F, 6.0F, 0.0F, false);

		robe = new ModelPart(this);
		robe.setPivot(0.0F, 1.25F, 0.0F);
		body.addChild(robe);
		robe.setTextureOffset(28, 28).addCuboid(-4.0F, -0.5F, -3.0F, 8.0F, 9.0F, 6.0F, 0.0F, false);

		head = new ModelPart(this);
		head.setPivot(0.0F, -11.25F, 0.0F);
		body.addChild(head);
		head.setTextureOffset(38, 44).addCuboid(-4.0F, 0.0F, -4.0F, 8.0F, 4.0F, 1.0F, 0.0F, false);
		head.setTextureOffset(0, 0).addCuboid(-4.0F, -12.0F, -4.0F, 8.0F, 12.0F, 8.0F, 0.0F, false);

		nose = new ModelPart(this);
		nose.setPivot(0.0F, -2.5F, -4.0F);
		head.addChild(nose);
		nose.setTextureOffset(0, 0).addCuboid(-1.0F, -0.5F, -2.0F, 2.0F, 5.0F, 2.0F, 0.0F, false);

		arm_left = new ModelPart(this);
		arm_left.setPivot(5.5F, -9.25F, 0.5F);
		body.addChild(arm_left);
		arm_left.setTextureOffset(14, 38).addCuboid(-1.5F, -2.0F, -1.5F, 3.0F, 12.0F, 3.0F, 0.0F, false);

		arm_right = new ModelPart(this);
		arm_right.setPivot(-5.5F, -9.25F, 0.5F);
		body.addChild(arm_right);
		arm_right.setTextureOffset(26, 43).addCuboid(-1.5F, -2.0F, -1.5F, 3.0F, 12.0F, 3.0F, 0.0F, false);

		leg_left = new ModelPart(this);
		leg_left.setPivot(2.0F, 1.75F, 0.0F);
		body.addChild(leg_left);
		leg_left.setTextureOffset(32, 0).addCuboid(-1.5F, -1.0F, -2.0F, 3.0F, 12.0F, 4.0F, 0.0F, false);

		leg_right = new ModelPart(this);
		leg_right.setPivot(-2.0F, 1.75F, 0.0F);
		body.addChild(leg_right);
		leg_right.setTextureOffset(0, 38).addCuboid(-1.5F, -1.0F, -2.0F, 3.0F, 12.0F, 4.0F, 0.0F, false);
	}

	@Override
	public void setAngles(AdjudicatorEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch)
	{
		AnimationHelper.rotateHead(head, headPitch, headYaw);

		this.body.yaw = 0.0F;


		AnimationHelper.swingLimb(arm_left, arm_right, limbAngle, limbDistance, 2F);
		AnimationHelper.swingLimb(leg_left, leg_right, limbAngle, limbDistance, 1.25F);

		if (this.riding)
		{
			arm_right.pitch += -0.63F;
			arm_left.pitch += -0.63F;
			this.leg_right.pitch = -1.4F;
			this.leg_right.yaw = 0.31F;
			this.leg_right.roll = 0.078F;
			this.leg_left.pitch = -1.41F;
			this.leg_left.yaw = -0.31F;
			this.leg_left.roll = -0.078F;
		}

		switch(entity.getState())
		{
			case WAITING:
				this.arm_right.roll = MathHelper.lerpAngleDegrees(animationProgress, arm_right.roll, 0);
				this.arm_left.roll = MathHelper.lerpAngleDegrees(animationProgress, arm_left.roll, 0);
				this.arm_right.pitch = 0;
				this.arm_left.pitch = 0;
				break;
			case TELEPORT:
				this.arm_right.pitch = MathHelper.cos(animationProgress * 0.6662F) * 0.25F;
				this.arm_left.pitch = MathHelper.cos(animationProgress * 0.6662F) * 0.25F;
				this.arm_right.roll = 2.3561945F;
				this.arm_left.roll = -2.3561945F;
				break;
			case WAKING:
				break;
			case FIGHTING:
				this.arm_right.roll = MathHelper.lerp(animationProgress, arm_right.roll, 0);
				this.arm_left.roll = MathHelper.lerp(animationProgress, arm_left.roll, 0);
				//this.arm_right.pitch = 0;
			//	this.arm_left.pitch = 0;
				break;
			case HIDDEN:
				break;
			case RIDING:
				break;
		}
	}

	@Override
	public Iterable<ModelPart> getParts()
	{
		return ImmutableList.of(body);
	}

	public void setRotationAngle(ModelPart bone, float x, float y, float z) {
		bone.pitch = x;
		bone.yaw = y;
		bone.roll = z;
	}

	@Override
	public void setArmAngle(Arm arm, MatrixStack matrices)
	{
		this.getAttackingArm(arm).rotate(matrices);
	}

	private ModelPart getAttackingArm(Arm arm) {
		return arm == Arm.LEFT ? this.arm_left : this.arm_right;
	}

	@Override
	public ModelPart getHead()
	{
		return head;
	}
}
