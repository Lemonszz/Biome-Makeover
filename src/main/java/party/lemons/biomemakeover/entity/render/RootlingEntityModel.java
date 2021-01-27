package party.lemons.biomemakeover.entity.render;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import party.lemons.biomemakeover.entity.RootlingEntity;
import party.lemons.biomemakeover.util.AnimationHelper;

public class RootlingEntityModel extends CompositeEntityModel<RootlingEntity> implements ModelWithHead, ModelWithArms
{
	private final ModelPart body;
	private final ModelPart bone;
	private final ModelPart arm_right;
	private final ModelPart arm_left;
	private final ModelPart leg_right;
	private final ModelPart leg_left;
	private final ModelPart head;
	private final ModelPart sprout_1_r1;
	private final ModelPart sprout2_r1;

	public RootlingEntityModel()
	{
		textureWidth = 64;
		textureHeight = 64;

		body = new ModelPart(this);
		body.setPivot(-0.5F, 18.5F, -0.5F);
		body.setTextureOffset(0, 0).addCuboid(-3.5F, -2.5F, -3.5F, 7.0F, 5.0F, 7.0F, 0.0F, false);
		body.setTextureOffset(0, 12).addCuboid(-2.5F, -3.5F, -2.5F, 5.0F, 1.0F, 5.0F, 0.0F, false);
		body.setTextureOffset(15, 12).addCuboid(-1.5F, 2.5F, -0.5F, 3.0F, 1.0F, 3.0F, 0.0F, false);

		bone = new ModelPart(this);
		bone.setPivot(0.0F, 0.0F, 0.0F);
		body.addChild(bone);

		arm_right = new ModelPart(this);
		arm_right.setPivot(-3.5F, -2.5F, 0.0F);
		body.addChild(arm_right);
		arm_right.setTextureOffset(0, 18).addCuboid(-1.0F, 0.0F, -1.5F, 1.0F, 7.0F, 3.0F, 0.0F, false);

		arm_left = new ModelPart(this);
		arm_left.setPivot(3.5F, -2.5F, 0.0F);
		body.addChild(arm_left);
		arm_left.setTextureOffset(8, 18).addCuboid(0.0F, 0.0F, -1.5F, 1.0F, 7.0F, 3.0F, 0.0F, false);

		leg_right = new ModelPart(this);
		leg_right.setPivot(-2.0F, 3.0F, 0.0F);
		body.addChild(leg_right);
		leg_right.setTextureOffset(0, 0).addCuboid(-0.5F, -0.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

		leg_left = new ModelPart(this);
		leg_left.setPivot(2.0F, 3.0F, 0.0F);
		body.addChild(leg_left);
		leg_left.setTextureOffset(3, 3).addCuboid(-0.5F, -0.5F, -0.5F, 1.0F, 3.0F, 1.0F, 0.0F, false);

		head = new ModelPart(this);
		head.setPivot(0.0F, -3.5F, 0.0F);
		body.addChild(head);
		head.setTextureOffset(17, 17).addCuboid(-1.5F, -5.0F, -1.5F, 3.0F, 5.0F, 3.0F, 0.0F, false);
		head.setTextureOffset(21, 0).addCuboid(-4.5F, -6.0F, -1.6F, 9.0F, 6.0F, 0.01F, 0.0F, false);

		sprout_1_r1 = new ModelPart(this);
		sprout_1_r1.setPivot(1.0F, -7.0F, -1.0F);
		head.addChild(sprout_1_r1);
		AnimationHelper.setRotation(sprout_1_r1, 0.0F, 0.7854F, 0.0F);
		sprout_1_r1.setTextureOffset(16, 25).addCuboid(-5.5F, -6.0F, 0.1F, 8.0F, 8.0F, 0.01F, 0.0F, false);

		sprout2_r1 = new ModelPart(this);
		sprout2_r1.setPivot(0.0F, -5.0F, 0.0F);
		head.addChild(sprout2_r1);
		AnimationHelper.setRotation(sprout2_r1, 0.0F, -0.7854F, 0.0F);
		sprout2_r1.setTextureOffset(16, 25).addCuboid(-4.0607F, -8.0F, 0.0393F, 8.0F, 8.0F, 0.01F, 0.0F, false);
	}

	@Override
	public void setAngles(RootlingEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch)
	{
		AnimationHelper.rotateHead(head, headPitch, headYaw);
		AnimationHelper.swingLimb(arm_left, arm_right, limbAngle, limbDistance, 1.4F);
		AnimationHelper.swingLimb(leg_left, leg_right, limbAngle, limbDistance, 2F);

		body.roll = (float)(Math.sin(limbAngle) / 4F) * limbDistance;
		head.roll = (float)(Math.cos(limbAngle) / 8F) * limbDistance;
	}

	@Override
	public Iterable<ModelPart> getParts()
	{
		return ImmutableList.of(body);
	}

	@Override
	public void setArmAngle(Arm arm, MatrixStack matrices) {
		this.getArm(arm).rotate(matrices);
	}

	protected ModelPart getArm(Arm arm)
	{
		return arm == Arm.LEFT ? this.arm_left : this.arm_right;
	}

	@Override
	public ModelPart getHead()
	{
		return head;
	}
}