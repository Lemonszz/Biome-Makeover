package party.lemons.biomemakeover.entity.render;// Made with Blockbench 3.7.2
// Exported for Minecraft version 1.15
// Paste this class into your mod and generate all required imports


import com.google.common.collect.ImmutableList;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.VindicatorEntityRenderer;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.client.render.entity.model.ModelWithArms;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Arm;
import net.minecraft.util.math.MathHelper;
import party.lemons.biomemakeover.entity.GhostEntity;

public class GhostEntityModel extends CompositeEntityModel<GhostEntity> implements ModelWithHead, ModelWithArms
{
private final ModelPart body;
private final ModelPart head;
private final ModelPart nose;
private final ModelPart rightarm;
private final ModelPart leftarm;
private final ModelPart bodylower;
private final ModelPart bodylower2;
private final ModelPart bodylower3;

	public GhostEntityModel()
	{
		textureWidth = 64;
		textureHeight = 64;

		body = new ModelPart(this);
		body.setPivot(0.0F, 0.0F, 0.0F);
		setRotationAngle(body, 0.1745F, 0.0F, 0.0F);
		body.setTextureOffset(0, 19).addCuboid(-4.0F, 0.0F, -3.0F, 8.0F, 15.0F, 6.0F, 0.5F, true);

		head = new ModelPart(this);
		head.setPivot(0.0F, 2.0F, -2.0F);
		body.addChild(head);
		head.setTextureOffset(0, 0).addCuboid(-4.0F, -10.0F, -6.0F, 8.0F, 10.0F, 8.0F, 0.0F, true);

		nose = new ModelPart(this);
		nose.setPivot(0.0F, -2.0F, -2.0F);
		head.addChild(nose);
		nose.setTextureOffset(0, 0).addCuboid(-1.0F, -1.0F, -6.0F, 2.0F, 4.0F, 2.0F, 0.0F, true);

		rightarm = new ModelPart(this);
		rightarm.setPivot(5.0F, 2.0F, 0.0F);
		body.addChild(rightarm);
		setRotationAngle(rightarm, -0.6545F, 0.0F, 0.0F);
		rightarm.setTextureOffset(28, 28).addCuboid(-1.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, true);

		leftarm = new ModelPart(this);
		leftarm.setPivot(-5.0F, 2.0F, 0.0F);
		body.addChild(leftarm);
		setRotationAngle(leftarm, -0.829F, 0.0F, 0.0F);
		leftarm.setTextureOffset(0, 40).addCuboid(-3.0F, -2.0F, -2.0F, 4.0F, 12.0F, 4.0F, 0.0F, false);

		bodylower = new ModelPart(this);
		bodylower.setPivot(0.0F, 15.1194F, -0.6351F);
		body.addChild(bodylower);
		setRotationAngle(bodylower, 0.3927F, 0.0F, 0.0F);
		bodylower.setTextureOffset(25, 11).addCuboid(-3.5F, -1.1194F, -2.3649F, 7.0F, 5.0F, 7.0F, 0.0F, false);

		bodylower2 = new ModelPart(this);
		bodylower2.setPivot(0.0F, 2.7614F, 0.6122F);
		bodylower.addChild(bodylower2);
		setRotationAngle(bodylower2, 0.3927F, 0.0F, 0.0F);
		bodylower2.setTextureOffset(32, 0).addCuboid(-2.5F, -0.1049F, -2.3737F, 5.0F, 5.0F, 5.0F, 0.0F, false);

		bodylower3 = new ModelPart(this);
		bodylower3.setPivot(0.0F, 4.0061F, -0.491F);
		bodylower2.addChild(bodylower3);
		setRotationAngle(bodylower3, 0.3927F, 0.0F, 0.0F);
		bodylower3.setTextureOffset(16, 40).addCuboid(-1.5F, -0.011F, -1.3542F, 3.0F, 5.0F, 3.0F, 0.0F, false);
	}

	@Override
	public void setAngles(GhostEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch)
	{
		setRotationAngle(body, 0.1745F, 0.0F, 0.0F);
		setRotationAngle(rightarm, -0.829F, 0.0F, 0.0F);
		setRotationAngle(leftarm, -0.829F, 0.0F, 0.0F);
		setRotationAngle(bodylower, 0.3927F, 0.0F, 0.0F);
		setRotationAngle(bodylower2, 0.3927F, 0.0F, 0.0F);
		setRotationAngle(bodylower3, 0.3927F, 0.0F, 0.0F);

		float pi = (float)Math.PI;
		this.head.pitch = -0.2618F + (headPitch * 0.0175F);
		this.head.yaw = headYaw * 0.0175F;

		this.rightarm.pitch = -0.85F + (MathHelper.cos(limbAngle * 0.6662F + pi) * 0.5F * limbDistance * 0.5F);
		this.leftarm.pitch = -0.85F + (MathHelper.cos(limbAngle * 0.6662F) * 0.5F * limbDistance * 0.5F);

		this.bodylower3.pitch = 0.3927F + MathHelper.cos(limbAngle * 0.6662F) * 1.4F * limbDistance * 0.5F;
		this.bodylower2.roll = 0.3927F + MathHelper.cos(limbAngle * 0.6662F) * 0.25F * limbDistance * 0.5F;
		this.bodylower.pitch = 0.3927F + MathHelper.cos(limbAngle * 0.6662F + pi) * 0.1F * limbDistance * 0.5F;
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha)
	{
		body.render(matrices, vertices, light, overlay);
	}

	@Override
	public Iterable<ModelPart> getParts()
	{
		return ImmutableList.of(body);
	}

	public void setRotationAngle(ModelPart part, float x, float y, float z) {
		part.pitch = x;
		part.yaw = y;
		part.roll = z;
	}

	@Override
	public void setArmAngle(Arm arm, MatrixStack matrices)
	{
		if(arm == Arm.RIGHT)
			rightarm.rotate(matrices);
		else
			leftarm.rotate(matrices);
	}

	@Override
	public ModelPart getHead()
	{
		return head;
	}
}