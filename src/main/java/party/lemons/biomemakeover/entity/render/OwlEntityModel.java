package party.lemons.biomemakeover.entity.render;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import party.lemons.biomemakeover.entity.OwlEntity;

public class OwlEntityModel extends CompositeEntityModel<OwlEntity> implements ModelWithHead
{
	private final ModelPart chest;
	private final ModelPart chest_lower_r1;
	private final ModelPart head;
	private final ModelPart head_connection;
	private final ModelPart beak_lower;
	private final ModelPart brow_left;
	private final ModelPart brow_right;
	private final ModelPart wing_left;
	private final ModelPart wing_left_connection;
	private final ModelPart wing_upper_r1;
	private final ModelPart wing_tip_left;
	private final ModelPart wing_tip_r1;
	private final ModelPart wing_right;
	private final ModelPart wing_right_connection;
	private final ModelPart wing_upper_r2;
	private final ModelPart wing_tip_right;
	private final ModelPart wing_tip_r2;
	private final ModelPart leg_right;
	private final ModelPart thigh_right_r1;
	private final ModelPart foot_right;
	private final ModelPart leg_left;
	private final ModelPart thigh_left_r1;
	private final ModelPart foot_left;
	private final ModelPart tail;
	private final ModelPart tail_r1;
	private static boolean baby;

	public OwlEntityModel()
	{
		textureWidth = 64;
		textureHeight = 64;

		chest = new ModelPart(this);
		chest.setPivot(0.0F, 14.975F, 0.3125F);
		chest.setTextureOffset(0, 0).addCuboid(-3.0F, -5.575F, -3.3125F, 6.0F, 8.0F, 8.0F, 0.0F, false);

		chest_lower_r1 = new ModelPart(this);
		chest_lower_r1.setPivot(0.0F, 1.425F, 0.6875F);
		chest.addChild(chest_lower_r1);
		setRotationAngle(chest_lower_r1, 0.2182F, 0.0F, 0.0F);
		chest_lower_r1.setTextureOffset(21, 21).addCuboid(-3.0F, -1.0F, -3.0F, 6.0F, 6.0F, 7.0F, 0.0F, false);

		head_connection = new ModelPart(this);
		head_connection.setPivot(0.0F, -8.575F, 0.6875F);
		chest.addChild(head_connection);

		head = new BigHeadPart(this);
		head.setPivot(0.0F, 3.0F, 0.0F);
		head_connection.addChild(head);
		head.setTextureOffset(0, 16).addCuboid(-4.0F, -6.0F, -3.0F, 8.0F, 6.0F, 6.0F, 0.0F, false);
		head.setTextureOffset(0, 5).addCuboid(-0.6F, -2.0F, -5.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		beak_lower = new ModelPart(this);
		beak_lower.setPivot(1.5F, 13.5F, 1.0F);
		head.addChild(beak_lower);
		beak_lower.setTextureOffset(4, 5).addCuboid(-2.1F, -14.5F, -5.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		brow_left = new ModelPart(this);
		brow_left.setPivot(0.0F, 4.0F, 0.0F);
		head.addChild(brow_left);
		brow_left.setTextureOffset(11, 28).addCuboid(1.0F, -9.0F, -4.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);

		brow_right = new ModelPart(this);
		brow_right.setPivot(0.0F, 4.0F, 0.0F);
		head.addChild(brow_right);
		brow_right.setTextureOffset(11, 30).addCuboid(-5.0F, -9.0F, -4.0F, 4.0F, 1.0F, 1.0F, 0.0F, false);

		wing_left_connection = new ModelPart(this);
		wing_left_connection.setPivot(3.0F, -5.575F, 0.6875F);
		chest.addChild(wing_left_connection);


		wing_left = new ModelPart(this);
		wing_left.setPivot(0.0F, 0.0F, -3.0F);
		wing_left_connection.addChild(wing_left);


		wing_upper_r1 = new ModelPart(this);
		wing_upper_r1.setPivot(0.0F, 1.0F, 2.0F);
		wing_left.addChild(wing_upper_r1);
		setRotationAngle(wing_upper_r1, 0.2618F, 0.0F, 0.0F);
		wing_upper_r1.setTextureOffset(0, 28).addCuboid(0.0F, -1.0F, -2.0F, 2.0F, 11.0F, 7.0F, 0.0F, false);

		wing_tip_left = new ModelPart(this);
		wing_tip_left.setPivot(0.5F, 9.0F, 7.0F);
		wing_left.addChild(wing_tip_left);


		wing_tip_r1 = new ModelPart(this);
		wing_tip_r1.setPivot(-0.5F, -4.0F, -2.0F);
		wing_tip_left.addChild(wing_tip_r1);
		setRotationAngle(wing_tip_r1, 0.5672F, 0.0F, 0.0F);
		wing_tip_r1.setTextureOffset(18, 34).addCuboid(0.0F, -1.0F, -2.0F, 1.0F, 8.0F, 4.0F, 0.0F, false);

		wing_right_connection = new ModelPart(this);
		wing_right_connection.setPivot(-3.0F, -5.575F, 0.6875F);
		chest.addChild(wing_right_connection);


		wing_right = new ModelPart(this);
		wing_right.setPivot(0.0F, 0.0F, -3.0F);
		wing_right_connection.addChild(wing_right);


		wing_upper_r2 = new ModelPart(this);
		wing_upper_r2.setPivot(0.0F, 1.0F, 2.0F);
		wing_right.addChild(wing_upper_r2);
		setRotationAngle(wing_upper_r2, 0.2618F, 0.0F, 0.0F);
		wing_upper_r2.setTextureOffset(28, 0).addCuboid(-2.0F, -1.0F, -2.0F, 2.0F, 11.0F, 7.0F, 0.0F, false);

		wing_tip_right = new ModelPart(this);
		wing_tip_right.setPivot(-0.5F, 9.0F, 7.0F);
		wing_right.addChild(wing_tip_right);


		wing_tip_r2 = new ModelPart(this);
		wing_tip_r2.setPivot(0.5F, -4.0F, -2.0F);
		wing_tip_right.addChild(wing_tip_r2);
		setRotationAngle(wing_tip_r2, 0.5672F, 0.0F, 0.0F);
		wing_tip_r2.setTextureOffset(28, 34).addCuboid(-1.0F, -1.0F, -2.0F, 1.0F, 8.0F, 4.0F, 0.0F, false);

		leg_right = new ModelPart(this);
		leg_right.setPivot(-1.5F, 6.925F, 1.6875F);
		chest.addChild(leg_right);

		thigh_right_r1 = new ModelPart(this);
		thigh_right_r1.setPivot(0.0F, 0.0F, 0.0F);
		leg_right.addChild(thigh_right_r1);
		setRotationAngle(thigh_right_r1, 0.4363F, 0.0F, 0.0F);
		thigh_right_r1.setTextureOffset(0, 0).addCuboid(-1.0F, -1.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, true);

		foot_right = new ModelPart(this);
		foot_right.setPivot(0.0F, 2.0F, 0.0F);
		leg_right.addChild(foot_right);
		foot_right.setTextureOffset(16, 4).addCuboid(-1.5F, 0.0F, -3.0F, 3.0F, 0.01F, 4.0F, 0.0F, false);

		leg_left = new ModelPart(this);
		leg_left.setPivot(1.5F, 6.925F, 1.6875F);
		chest.addChild(leg_left);

		thigh_left_r1 = new ModelPart(this);
		thigh_left_r1.setPivot(0.0F, 0.0F, 0.0F);
		leg_left.addChild(thigh_left_r1);
		setRotationAngle(thigh_left_r1, 0.4363F, 0.0F, 0.0F);
		thigh_left_r1.setTextureOffset(0, 0).addCuboid(-1.0F, -1.0F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);

		foot_left = new ModelPart(this);
		foot_left.setPivot(0.0F, 2.0F, 0.0F);
		leg_left.addChild(foot_left);
		foot_left.setTextureOffset(16, 0).addCuboid(-1.5F, 0.0F, -3.0F, 3.0F, 0.01F, 4.0F, 0.0F, false);

		tail = new ModelPart(this);
		tail.setPivot(0.0F, 5.925F, 5.1875F);
		chest.addChild(tail);
		setRotationAngle(tail, 0.3054F, 0.0F, 0.0F);

		tail_r1 = new ModelPart(this);
		tail_r1.setPivot(0.0F, 0.0F, 0.0F);
		tail.addChild(tail_r1);
		setRotationAngle(tail_r1, 0.5236F, 0.0F, 0.0F);
		tail_r1.setTextureOffset(38, 38).addCuboid(-3.0F, -0.5F, 0.0F, 6.0F, 5.0F, 0.01F, 0.0F, false);
	}

	@Override
	public void setAngles(OwlEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch)
	{
		float pi = (float) Math.PI;
		baby = entity.isBaby();

		if(entity.getStandingState() != OwlEntity.StandingState.FLYING)
		{
			this.head.pitch = -0.2618F + (headPitch * 0.0175F);
			this.head.yaw = headYaw * 0.0175F;

			this.wing_left_connection.yaw = 0;
			this.wing_tip_left.roll = 0;

			this.wing_right_connection.yaw = 0;
			this.wing_tip_right.roll = 0;


			this.leg_right.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance;
			this.leg_left.pitch = MathHelper.cos(limbAngle * 0.6662F + pi) * 1.4F * limbDistance;
			this.wing_right_connection.yaw = MathHelper.cos(limbAngle * 0.6662F) * 0.5F * limbDistance;
			this.wing_left_connection.yaw = MathHelper.cos(limbAngle * 0.6662F + pi) * 1F * limbDistance;

			if(entity.isInSittingPose())
			{
				leg_right.pitch = -1.5708F;
				leg_left.pitch = -1.5708F;
			}

		}else
		{
			this.wing_left_connection.yaw = (float) Math.cos(limbAngle / 2F) / 2F;
			this.wing_tip_left.roll = -((float) Math.sin(limbAngle / 2F) / 4F);

			this.wing_right_connection.yaw = -((float) Math.cos(limbAngle / 2F) / 2F);
			this.wing_tip_right.roll = ((float) Math.sin(limbAngle / 2F) / 4F);

			this.leg_right.pitch = MathHelper.cos(limbAngle * 0.6662F) * 1F * limbDistance;
			this.leg_left.pitch = MathHelper.cos(limbAngle * 0.6662F + pi) * 1F * limbDistance;
		}
	}

	@Override
	public void animateModel(OwlEntity entity, float limbAngle, float limbDistance, float tickDelta)
	{
		super.animateModel(entity, limbAngle, limbDistance, tickDelta);


		switch(entity.getStandingState())
		{
			case STANDING:

				break;
			case FLYING:

				break;
		}

		float rad90 = 1.5708F;
		float rad155 = 2.70526F;
		float rad70 = 1.22173F;
		float wingAngle = limbAngle;
		float rad40 = 0.698132F;

		float leanProgress = entity.getLeaningPitch(tickDelta) / 7F;
		setRotationAngle(wing_right, 0, rad70 * leanProgress, rad90 * leanProgress);
		setRotationAngle(wing_left, 0, -rad70 * leanProgress, -rad90 * leanProgress);
		setRotationAngle(wing_tip_left, -rad155 * leanProgress, 0, 0);
		setRotationAngle(wing_tip_right, -rad155 * leanProgress, 0, 0);
		setRotationAngle(foot_left, rad90 * leanProgress, 0, 0);
		setRotationAngle(foot_right, rad90 * leanProgress, 0, 0);

		setRotationAngle(head_connection, -rad40 * leanProgress, 0, 0);
	}

	@Override
	public Iterable<ModelPart> getParts()
	{
		return ImmutableList.of(chest);
	}

	public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z)
	{
		modelRenderer.pitch = x;
		modelRenderer.yaw = y;
		modelRenderer.roll = z;
	}

	@Override
	public ModelPart getHead()
	{
		return head;
	}

	public static class BigHeadPart extends ModelPart
	{
		public BigHeadPart(Model model)
		{
			super(model);
		}

		public BigHeadPart(Model model, int textureOffsetU, int textureOffsetV)
		{
			super(model, textureOffsetU, textureOffsetV);
		}

		public BigHeadPart(int textureWidth, int textureHeight, int textureOffsetU, int textureOffsetV)
		{
			super(textureWidth, textureHeight, textureOffsetU, textureOffsetV);
		}

		@Override
		public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha)
		{
			matrices.push();
			if(baby) matrices.scale(1.6F, 1.6F, 1.6F);
			super.render(matrices, vertices, light, overlay, red, green, blue, alpha);
			matrices.pop();
		}
	}
}