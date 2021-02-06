package party.lemons.biomemakeover.entity.render;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.MathHelper;
import party.lemons.biomemakeover.entity.ScuttlerEntity;

public class ScuttlerModel extends CompositeEntityModel<ScuttlerEntity> implements ModelWithHead
{
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart jaw;
	private final ModelPart left_front_leg;
	private final ModelPart right_front_leg;
	private final ModelPart right_back_leg;
	private final ModelPart left_back_leg;
	private final ModelPart tail;
	private final ModelPart tail2;
	private final ModelPart tail3;
	private final ModelPart rattler;
	private static boolean baby;

	public ScuttlerModel()
	{
		this.textureHeight = 64;
		this.textureWidth = 64;


		body = new ModelPart(this);
		body.setPivot(0.0F, 22.0F, -4.0F);
		setRotationAngle(body, 0.2618F, 0.0F, 0.0F);
		body.setTextureOffset(0, 0).addCuboid(-3.0F, -1.5F, 0.0F, 6.0F, 3.0F, 12.0F, 0.0F, false);
		body.setTextureOffset(0, 9).addCuboid(-0.5F, -2.5F, 1.5F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		body.setTextureOffset(0, 9).addCuboid(-0.5F, -2.5F, 5.5F, 1.0F, 1.0F, 2.0F, 0.0F, false);
		body.setTextureOffset(0, 9).addCuboid(-0.5F, -2.5F, 9.5F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		head = new BigHeadPart(this);
		head.setPivot(0.0F, 0.0F, 0.0F);
		body.addChild(head);
		setRotationAngle(head, -0.2618F, 0.0F, 0.0F);
		head.setTextureOffset(0, 15).addCuboid(-2.5F, -2.0F, -4.5F, 5.0F, 2.0F, 5.0F, 0.0F, false);
		head.setTextureOffset(0, 9).addCuboid(-0.5F, -3.0F, -3.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		jaw = new ModelPart(this);
		jaw.setPivot(0.0F, 0.0F, 0.0F);
		head.addChild(jaw);
		setRotationAngle(jaw, 0.1309F, 0.0F, 0.0F);
		jaw.setTextureOffset(0, 22).addCuboid(-2.0F, 0.0F, -4.0F, 4.0F, 1.0F, 4.0F, 0.0F, false);

		left_front_leg = new ModelPart(this);
		left_front_leg.setPivot(3.0F, 1.0F, 2.0F);
		body.addChild(left_front_leg);
		setRotationAngle(left_front_leg, -0.0873F, 1.1345F, 0.0436F);
		left_front_leg.setTextureOffset(24, 5).addCuboid(0.0F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, 0.0F, false);

		right_front_leg = new ModelPart(this);
		right_front_leg.setPivot(-3.0F, 1.0F, 2.0F);
		body.addChild(right_front_leg);
		setRotationAngle(right_front_leg, -0.0873F, 2.0071F, -0.0436F);
		right_front_leg.setTextureOffset(24, 5).addCuboid(0.0F, -1.0F, -1.0F, 4.0F, 2.0F, 2.0F, 0.0F, false);

		right_back_leg = new ModelPart(this);
		right_back_leg.setPivot(-3.0F, 1.1566F, 9.1895F);
		body.addChild(right_back_leg);
		setRotationAngle(right_back_leg, -0.2618F, 0.0F, 0.0F);
		right_back_leg.setTextureOffset(26, 15).addCuboid(-1.0F, -0.7978F, -1.2235F, 2.0F, 4.0F, 2.0F, 0.0F, false);

		left_back_leg = new ModelPart(this);
		left_back_leg.setPivot(3.0F, 1.1566F, 9.1895F);
		body.addChild(left_back_leg);
		setRotationAngle(left_back_leg, -0.2618F, 0.0F, 0.0F);
		left_back_leg.setTextureOffset(26, 15).addCuboid(-1.0F, -0.7978F, -1.2235F, 2.0F, 4.0F, 2.0F, 0.0F, false);

		tail = new ModelPart(this);
		tail.setPivot(0.0F, 0.0F, 12.0F);
		body.addChild(tail);
		setRotationAngle(tail, 0.6981F, 0.0F, 0.0F);
		tail.setTextureOffset(24, 0).addCuboid(-2.0F, -1.0F, -0.5F, 4.0F, 2.0F, 3.0F, 0.0F, false);

		tail2 = new ModelPart(this);
		tail2.setPivot(0.0F, -0.5448F, 3.5642F);
		tail.addChild(tail2);
		setRotationAngle(tail2, 0.4363F, 0.0F, 0.0F);
		tail2.setTextureOffset(0, 0).addCuboid(-1.5F, -1.0F, -1.5F, 3.0F, 2.0F, 3.0F, 0.0F, false);

		tail3 = new ModelPart(this);
		tail3.setPivot(0.0F, 0.0783F, 1.2687F);
		tail2.addChild(tail3);
		setRotationAngle(tail3, 0.7854F, 0.0F, 0.0F);
		tail3.setTextureOffset(13, 25).addCuboid(-1.0F, -1.4605F, -0.3184F, 2.0F, 2.0F, 3.0F, 0.0F, false);

		rattler = new ModelPart(this);
		rattler.setPivot(0.0F, -0.6005F, 1.5F);
		tail3.addChild(rattler);
		setRotationAngle(rattler, 0.1309F, 0.0F, 0.0F);
		rattler.setTextureOffset(15, 17).addCuboid(-1.5F, -1.4463F, -0.0312F, 3.0F, 3.0F, 5.0F, 0.0F, false);
	}

	@Override
	public void setAngles(ScuttlerEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch)
	{
		baby = entity.isBaby();

		setRotationAngle(body, 0.2618F, 0.0F, 0.0F);
		setRotationAngle(head, -0.2618F, 0.0F, 0.0F);
		setRotationAngle(jaw, 0.1309F, 0.0F, 0.0F);
		setRotationAngle(left_front_leg, -0.0873F, 1.1345F, 0.0436F);
		setRotationAngle(right_front_leg, -0.0873F, 2.0071F, -0.0436F);
		setRotationAngle(right_back_leg, -0.2618F, 0.0F, 0.0F);
		setRotationAngle(left_back_leg, -0.2618F, 0.0F, 0.0F);
		setRotationAngle(tail, 0.6981F, 0.0F, 0.0F);
		setRotationAngle(tail2, 0.4363F, 0.0F, 0.0F);
		setRotationAngle(tail3, 0.7854F, 0.0F, 0.0F);
		setRotationAngle(rattler, 0.1309F, 0.0F, 0.0F);

		float pi = (float) Math.PI;
		this.head.pitch = -0.2618F + (headPitch * 0.0175F);
		this.head.yaw = headYaw * 0.0175F;
		this.right_back_leg.pitch = MathHelper.cos(limbAngle * 0.66F) * 1.4F * limbDistance;
		this.left_back_leg.pitch = MathHelper.cos(limbAngle * 0.66F + pi) * 1.4F * limbDistance;
		this.right_front_leg.yaw = (pi + -(1.1345F + (MathHelper.cos(limbAngle * 0.66F) * 1.0F * limbDistance)));
		this.left_front_leg.yaw = 1.1345F + (MathHelper.cos(limbAngle * 0.66F) * 1.0F * limbDistance);

		//this.tail.pitch = 5;
		this.tail.roll = MathHelper.cos(limbAngle * 0.66F + pi) * 0.7F * limbDistance;
		this.tail3.roll = -MathHelper.cos(limbAngle * 0.66F + pi) * 0.5F * limbDistance;
		this.rattler.roll = -MathHelper.cos(limbAngle * 0.66F + pi) * 0.7F * limbDistance;
		this.rattler.pitch = -MathHelper.cos(limbAngle * 0.66F + pi) * 1F * (limbDistance / 2);

		this.tail.roll += MathHelper.sin(entity.rattleTime);
		this.tail3.roll += -MathHelper.sin(entity.rattleTime);
		this.rattler.roll += -MathHelper.sin(entity.rattleTime);

		if(entity.getDataTracker().get(ScuttlerEntity.EATING))
			this.head.pitch = -MathHelper.cos(entity.eatTime * 0.6F + pi) * 0.3F;
	}


	public Iterable<ModelPart> getParts()
	{
		return ImmutableList.of(this.body);
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
		return this.head;
	}

	private static class BigHeadPart extends ModelPart
	{

		BigHeadPart(Model model)
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
