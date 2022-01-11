package party.lemons.biomemakeover.util;

import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;

public class AnimationHelper
{
    private static final float PI = (float) Math.PI;

    public static void setRotation(ModelPart part, float x, float y, float z)
    {
        part.xRot = x;
        part.yRot = y;
        part.zRot = z;
    }

    public static void swingLimb(ModelPart left, ModelPart right, float limbAngle, float limbDistance, float scale)
    {
        if(right != null) right.xRot = Mth.cos(limbAngle * 1 + PI) * scale * limbDistance;
        if(left != null) left.xRot = Mth.cos(limbAngle * 1) * scale * limbDistance;
    }

    public static void rotateHead(ModelPart head, float headPitch, float headYaw)
    {
        head.xRot = -0.2618F + (headPitch * 0.0175F);
        head.yRot = headYaw * 0.0175F;
    }
}
