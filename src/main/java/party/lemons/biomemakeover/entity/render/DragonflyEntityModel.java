package party.lemons.biomemakeover.entity.render;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.util.math.MatrixStack;
import party.lemons.biomemakeover.entity.DragonflyEntity;

public class DragonflyEntityModel extends CompositeEntityModel<DragonflyEntity> implements ModelWithHead
{
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart feeler_left;
	private final ModelPart feeler_left2;
	private final ModelPart wings_left;
	private final ModelPart wings_right;

	public DragonflyEntityModel()
	{
		textureWidth = 32;
		textureHeight = 32;

		body = new ModelPart(this);
		body.setPivot(0.0F, 24.0F, 0.0F);
		body.setTextureOffset(0, 0).addCuboid(-1.0F, -2.0F, -5.0F, 2.0F, 2.0F, 10.0F, 0.0F, false);
		body.setTextureOffset(0, 0).addCuboid(-0.5F, -1.5F, 5.0F, 1.0F, 1.0F, 2.0F, 0.0F, false);

		head = new ModelPart(this);
		head.setPivot(0.0F, 0.0F, 0.0F);
		body.addChild(head);
		head.setTextureOffset(0, 16).addCuboid(-1.5F, -2.5F, -8.0F, 3.0F, 3.0F, 3.0F, 0.0F, false);

		feeler_left = new ModelPart(this);
		feeler_left.setPivot(1.0F, -2.0F, -7.5F);
		head.addChild(feeler_left);
		setRotationAngle(feeler_left, 0.2618F, -0.48F, 0.0F);
		feeler_left.setTextureOffset(0, 2).addCuboid(0.0F, -2.0F, -0.5F, 0.0F, 2.0F, 1.0F, 0.0F, false);

		feeler_left2 = new ModelPart(this);
		feeler_left2.setPivot(-1.0F, -2.0F, -7.5F);
		head.addChild(feeler_left2);
		setRotationAngle(feeler_left2, 0.2618F, 0.48F, 0.0F);
		feeler_left2.setTextureOffset(2, 2).addCuboid(0.0F, -2.0F, -0.5F, 0.0F, 2.0F, 1.0F, 0.0F, false);

		wings_left = new ModelPart(this);
		wings_left.setPivot(1.0F, -2.0F, 0.0F);
		body.addChild(wings_left);
		setRotationAngle(wings_left, 0.0F, 0.0F, -0.2618F);
		wings_left.setTextureOffset(10, 12).addCuboid(0.0F, 0.0F, -4.0F, 5.0F, 0.0F, 4.0F, 0.0F, false);
		wings_left.setTextureOffset(0, 12).addCuboid(0.0F, 0.0F, 0.0F, 5.0F, 0.0F, 4.0F, 0.0F, false);

		wings_right = new ModelPart(this);
		wings_right.setPivot(-1.0F, -2.0F, 0.0F);
		body.addChild(wings_right);
		setRotationAngle(wings_right, 0.0F, 0.0F, 0.2618F);
		wings_right.setTextureOffset(10, 4).addCuboid(-5.0F, 0.0F, -4.0F, 5.0F, 0.0F, 4.0F, 0.0F, false);
		wings_right.setTextureOffset(10, 0).addCuboid(-5.0F, 0.0F, 0.0F, 5.0F, 0.0F, 4.0F, 0.0F, false);
	}

	@Override
	public void setAngles(DragonflyEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch)
	{

	}

	@Override
	public Iterable<ModelPart> getParts()
	{
		return ImmutableList.of(body);
	}

	public void setRotationAngle(ModelPart modelRenderer, float x, float y, float z) {
		modelRenderer.pitch = x;
		modelRenderer.yaw = y;
		modelRenderer.roll = z;
	}

	@Override
	public ModelPart getHead()
	{
		return head;
	}
}