package party.lemons.biomemakeover.entity.render;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.util.math.MathHelper;
import party.lemons.biomemakeover.entity.TadpoleEntity;

public class TadpoleEntityModel extends CompositeEntityModel<TadpoleEntity>
{
	private final ModelPart body;
	private final ModelPart tail;
	private final ModelPart finw;
	private final ModelPart fine;
	private final ModelPart cube_r1;

	public TadpoleEntityModel()
	{
		textureWidth = 16;
		textureHeight = 16;

		body = new ModelPart(this);
		body.setPivot(2.0F, 23.0F, -3.0F);
		body.setTextureOffset(0, 0).addCuboid(-4.0F, -1.0F, -1.0F, 3.0F, 2.0F, 3.0F, 0.0F, false);
		body.setTextureOffset(0, 11).addCuboid(-4.0F, -2.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);
		body.setTextureOffset(8, 9).addCuboid(-2.0F, -2.0F, -1.0F, 1.0F, 1.0F, 1.0F, 0.0F, false);

		tail = new ModelPart(this);
		tail.setPivot(-2.5F, 0.0F, 2.0F);
		body.addChild(tail);
		tail.setTextureOffset(0, 0).addCuboid(0.0F, -1.6F, -1.0F, 0.0F, 3.0F, 6.0F, 0.0F, false);
		tail.setTextureOffset(4, 9).addCuboid(-0.5F, -0.5F, 0.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		finw = new ModelPart(this);
		finw.setPivot(-1.0F, 0.0F, 0.0F);
		body.addChild(finw);
		finw.setTextureOffset(0, 9).addCuboid(0.0F, 0.0F, -1.0F, 2.0F, 0.01F, 2.0F, 0.0F, false);

		fine = new ModelPart(this);
		fine.setPivot(-4.0F, 0.0F, 0.0F);
		body.addChild(fine);


		cube_r1 = new ModelPart(this);
		cube_r1.setPivot(0.0F, 0.0F, 0.0F);
		fine.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.0F, 0.0F, 0.0436F);
		cube_r1.setTextureOffset(7, 0).addCuboid(-2.0F, 0.0F, -1.0F, 2.0F, 0.01F, 2.0F, 0.0F, false);
	}

	@Override
	public void setAngles(TadpoleEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch)
	{
		float f = 1.0F;
		if(!entity.isTouchingWater())
		{
			f = 1.5F;
		}

		this.tail.yaw = -f * 0.45F * MathHelper.sin(0.6F * animationProgress);
		this.finw.pitch = f * 0.10F * MathHelper.sin(0.8F * animationProgress);
		this.fine.pitch = f * 0.10F * MathHelper.sin(0.8F * animationProgress);
	}

	@Override
	public Iterable<ModelPart> getParts()
	{
		return ImmutableList.of(body);
	}

	public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z)
	{
		modelRenderer.pitch = x;
		modelRenderer.yaw = y;
		modelRenderer.roll = z;
	}
}