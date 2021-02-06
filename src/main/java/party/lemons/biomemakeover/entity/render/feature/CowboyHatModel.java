package party.lemons.biomemakeover.entity.render.feature;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithHat;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;

public class CowboyHatModel<T extends Entity> extends EntityModel<T> implements ModelWithHead, ModelWithHat
{
	private final ModelPart head;
	private final ModelPart left;
	private final ModelPart main;
	private final ModelPart right;
	private final ModelPart bone;


	public CowboyHatModel()
	{
		textureWidth = 64;
		textureHeight = 64;

		head = new ModelPart(this);
		head.setPivot(0.0F, 0.0F, 0.0F);
		head.setTextureOffset(32, 32).addCuboid(-4.0F, -8.0F, -4.0F, 8.0F, 8.0F, 8.0F, 0.0F, true);

		left = new ModelPart(this);
		left.setPivot(6.0F, -6.0F, 0.0F);
		head.addChild(left);
		setRotationAngle(left, 0.0F, 0.0F, 0.2618F);
		left.setTextureOffset(32, 45).addCuboid(0.0F, -2.0F, -8.0F, 0.1F, 2.0F, 16.0F, 0.0F, false);

		main = new ModelPart(this);
		main.setPivot(0.0F, -6.0F, 0.0F);
		head.addChild(main);
		main.setTextureOffset(32, 0).addCuboid(-4.0F, -4.0F, -4.0F, 8.0F, 4.0F, 8.0F, 0.0F, false);

		right = new ModelPart(this);
		right.setPivot(-6.0F, -6.0F, 0.0F);
		head.addChild(right);
		setRotationAngle(right, 0.0F, 0.0F, -0.2618F);
		right.setTextureOffset(0, 45).addCuboid(0.0F, -2.0F, -8.0F, 0.1F, 2.0F, 16.0F, 0.0F, false);

		bone = new ModelPart(this);
		bone.setPivot(0.0F, 24.0F, 0.0F);
		head.addChild(bone);
		bone.setTextureOffset(0, 12).addCuboid(-8.0F, -30.0F, -8.0F, 16.0F, 0.1F, 16.0F, 0.0F, false);
	}

	@Override
	public void render(MatrixStack matrixStack, VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha)
	{
		head.render(matrixStack, buffer, packedLight, packedOverlay);
	}

	public void setRotationAngle(ModelPart ModelPart, float x, float y, float z)
	{
		ModelPart.pitch = x;
		ModelPart.yaw = y;
		ModelPart.roll = z;
	}

	@Override
	public void setAngles(T entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch)
	{

	}

	@Override
	public ModelPart getHead()
	{
		return head;
	}

	@Override
	public void setHatVisible(boolean visible)
	{
		head.visible = true;
	}
}