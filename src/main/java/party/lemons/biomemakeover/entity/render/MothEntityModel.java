package party.lemons.biomemakeover.entity.render;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.model.ModelUtil;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.util.math.MathHelper;
import party.lemons.biomemakeover.entity.MothEntity;
import party.lemons.biomemakeover.util.AnimationHelper;

public class MothEntityModel extends CompositeEntityModel<MothEntity> implements ModelWithHead
{
	private final ModelPart body;
	private final ModelPart cube_r1;
	private final ModelPart cube_r2;
	private final ModelPart cube_r3;
	private final ModelPart head;
	private final ModelPart cube_r4;
	private final ModelPart wing_right;
	private final ModelPart wing_right_joint;
	private final ModelPart wing_left;
	private final ModelPart wing_left_joint;
	private float bodyPitch;

	public MothEntityModel()
	{
		textureWidth = 64;
		textureHeight = 64;
		body = new ModelPart(this);
		body.setPivot(0.0F, 13.0F, -1.0F);


		cube_r1 = new ModelPart(this);
		cube_r1.setPivot(1.0F, 7.5F, 4.5F);
		body.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.7854F, 0.0F, 0.0F);
		cube_r1.setTextureOffset(0, 18).addCuboid(-4.0F, -5.5F, -2.5F, 6.0F, 8.0F, 6.0F, 0.0F, false);

		cube_r2 = new ModelPart(this);
		cube_r2.setPivot(0.0F, -0.3827F, -0.0761F);
		body.addChild(cube_r2);
		setRotationAngle(cube_r2, 0.3927F, 0.0F, 0.0F);
		cube_r2.setTextureOffset(0, 0).addCuboid(-4.0F, -5.0F, -4.0F, 8.0F, 10.0F, 8.0F, 0.0F, false);

		cube_r3 = new ModelPart(this);
		cube_r3.setPivot(-5.5F, 1.5F, -3.5F);
		body.addChild(cube_r3);
		setRotationAngle(cube_r3, 0.3927F, 0.0F, 0.0F);
		cube_r3.setTextureOffset(22, 30).addCuboid(-0.5F, -3.5F, -4.5F, 0.01F, 7.0F, 6.0F, 0.0F, false);
		cube_r3.setTextureOffset(32, 5).addCuboid(11.5F, -3.5F, -4.5F, 0.01F, 7.0F, 6.0F, 0.0F, false);
		cube_r3.setTextureOffset(4, 43).addCuboid(-0.5F, -3.5F, 1.5F, 2.0F, 7.0F, 0.01F, 0.0F, false);
		cube_r3.setTextureOffset(0, 43).addCuboid(9.5F, -3.5F, 1.5F, 2.0F, 7.0F, 0.01F, 0.0F, false);

		head = new ModelPart(this);
		head.setPivot(0.0F, -5.0F, -2.0F);
		body.addChild(head);
		head.setTextureOffset(24, 24).addCuboid(-3.0F, -5.0F, -3.9F, 6.0F, 6.0F, 6.0F, 0.0F, false);
		head.setTextureOffset(0, 0).addCuboid(0.0F, -3.0F, -8.0F, 0.0F, 4.0F, 4.0F, 0.0F, false);

		cube_r4 = new ModelPart(this);
		cube_r4.setPivot(-2.5F, -7.3F, -1.0F);
		head.addChild(cube_r4);
		setRotationAngle(cube_r4, 0.3927F, 0.0F, 0.0F);
		cube_r4.setTextureOffset(24, 18).addCuboid(-1.5F, -4.5F, 0.0F, 3.0F, 7.0F, 0.01F, 0.0F, false);
		cube_r4.setTextureOffset(24, 0).addCuboid(3.5F, -4.5F, 0.0F, 3.0F, 7.0F, 0.01F, 0.0F, false);

		wing_right = new ModelPart(this);
		wing_right.setPivot(-1.0F, -5.0F, 1.7F);
		body.addChild(wing_right);
		setRotationAngle(wing_right, 0.3927F, 0.0F, 0.0F);

		wing_right_joint = new ModelPart(this);
		wing_right_joint.setPivot(0.0F, 0.0F, 0.0F);
		wing_right.addChild(wing_right_joint);
		wing_right_joint.setTextureOffset(32, 0).addCuboid(-11.0F, -8.0F, 0.6F, 11.0F, 11.0F, 0.01F, 0.0F, false);
		wing_right_joint.setTextureOffset(34, 36).addCuboid(-8.0F, 3.0F, 0.6F, 8.0F, 7.0F, 0.01F, 0.0F, false);

		wing_left = new ModelPart(this);
		wing_left.setPivot(1.0F, -5.0F, 1.8F);
		body.addChild(wing_left);
		setRotationAngle(wing_left, 0.3927F, 0.0F, 0.0F);

		wing_left_joint = new ModelPart(this);
		wing_left_joint.setPivot(0.0F, 0.0F, 0.0F);
		wing_left.addChild(wing_left_joint);
		wing_left_joint.setTextureOffset(42, 18).addCuboid(0.0F, 3.0F, 0.6F, 8.0F, 7.0F, 0.01F, 0.0F, false);
		wing_left_joint.setTextureOffset(0, 32).addCuboid(0.0F, -8.0F, 0.6F, 11.0F, 11.0F, 0.01F, 0.0F, false);
	}

	public void animateModel(MothEntity moth, float f, float g, float h) {
		super.animateModel(moth, f, g, h);
		this.bodyPitch = moth.getBodyPitch(h);
	}

	@Override
	public void setAngles(MothEntity moth, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch)
	{
		AnimationHelper.rotateHead(head, headPitch, headYaw);

		this.body.pitch = 0.0F;
		this.body.pivotY = 19.0F;
		float animProgress = animationProgress * 2.1F;

		this.wing_left_joint.yaw = (-1F + (MathHelper.cos(animProgress) * 3.1415927F * 0.1F));
		this.wing_right_joint.yaw = -this.wing_left_joint.yaw;

		this.body.pitch = 0.0F;
		this.body.yaw = 0.0F;
		this.body.roll = 0.0F;

		if (!moth.isTargeting())
		{
			this.body.pitch = 0.0F;
			this.body.yaw = 0.0F;
			this.body.roll = 0.0F;
			animProgress = MathHelper.cos(animationProgress * 0.18F);
			this.body.pitch = 0.1F + animProgress * 3.1415927F * 0.025F;
			this.body.pivotY = 19.0F - MathHelper.cos(animationProgress * 0.18F) * 0.9F;
		}

		if (this.bodyPitch > 0.0F) {
			this.body.pitch = ModelUtil.interpolateAngle(this.body.pitch, 3.0915928F, this.bodyPitch);
		}

	}

	@Override
	public Iterable<ModelPart> getParts()
	{
		return ImmutableList.of(body);
	}

	public void setRotationAngle(ModelPart bone, float x, float y, float z)
	{
		bone.pitch = x;
		bone.yaw = y;
		bone.roll = z;
	}

	@Override
	public ModelPart getHead()
	{
		return head;
	}
}
