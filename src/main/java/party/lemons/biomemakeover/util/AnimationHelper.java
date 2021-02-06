package party.lemons.biomemakeover.util;

import net.minecraft.client.model.ModelPart;
import net.minecraft.util.math.MathHelper;

public class AnimationHelper
{
	private static final float PI = (float) Math.PI;

	public static void setRotation(ModelPart part, float x, float y, float z)
	{
		part.pitch = x;
		part.yaw = y;
		part.roll = z;
	}

	public static void swingLimb(ModelPart left, ModelPart right, float limbAngle, float limbDistance, float scale)
	{
		if(right != null) right.pitch = MathHelper.cos(limbAngle * 1 + PI) * scale * limbDistance;
		if(left != null) left.pitch = MathHelper.cos(limbAngle * 1) * scale * limbDistance;
	}

	public static void rotateHead(ModelPart head, float headPitch, float headYaw)
	{
		head.pitch = -0.2618F + (headPitch * 0.0175F);
		head.yaw = headYaw * 0.0175F;
	}
}
