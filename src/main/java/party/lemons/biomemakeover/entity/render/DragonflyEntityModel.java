package party.lemons.biomemakeover.entity.render;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.client.render.entity.model.ModelWithHead;
import party.lemons.biomemakeover.entity.DragonflyEntity;

public class DragonflyEntityModel extends CompositeEntityModel<DragonflyEntity> implements ModelWithHead
{
	private final ModelPart body;
	private final ModelPart head;
	private final ModelPart wingsw;
	private final ModelPart right_top;
	private final ModelPart topwing_r1;
	private final ModelPart right_bottom;
	private final ModelPart bottomwing_r1;
	private final ModelPart wingse;
	private final ModelPart left_top;
	private final ModelPart topwing_r2;
	private final ModelPart left_bottom;
	private final ModelPart bottomwing_r2;


	public DragonflyEntityModel() {
		textureWidth = 32;
		textureHeight = 32;

		body = new ModelPart(this);
		body.setPivot(-1.0F, 22.0F, -0.5F);
		body.setTextureOffset(0, 0).addCuboid(1.5F, 0.9F, 0.5F, 1.0F, 1.0F, 0.0F, 0.0F, false);
		body.setTextureOffset(0, 0).addCuboid(1.5F, 0.9F, -0.5F, 1.0F, 1.0F, 0.0F, 0.0F, false);
		body.setTextureOffset(0, 0).addCuboid(1.5F, 0.9F, 1.5F, 1.0F, 1.0F, 0.0F, 0.0F, false);
		body.setTextureOffset(0, 0).addCuboid(-1.5F, 0.9F, -0.5F, 1.0F, 1.0F, 0.0F, 0.0F, false);
		body.setTextureOffset(0, 0).addCuboid(-1.5F, 0.9F, 0.5F, 1.0F, 1.0F, 0.0F, 0.0F, false);
		body.setTextureOffset(0, 0).addCuboid(-1.5F, 0.9F, 1.5F, 1.0F, 1.0F, 0.0F, 0.0F, false);
		body.setTextureOffset(0, 8).addCuboid(-0.5F, -1.1F, -1.5F, 2.0F, 2.0F, 3.0F, 0.0F, false);
		body.setTextureOffset(0, 2).addCuboid(0.0F, -0.6F, 1.5F, 1.0F, 1.0F, 5.0F, 0.0F, false);

		head = new ModelPart(this);
		head.setPivot(0.5F, 0.4F, -1.5F);
		body.addChild(head);
		head.setTextureOffset(10, 10).addCuboid(-1.5F, -1.0F, -2.0F, 3.0F, 2.0F, 2.0F, 0.0F, false);

		wingsw = new ModelPart(this);
		wingsw.setPivot(-0.5F, -1.0F, -0.5F);
		body.addChild(wingsw);


		right_top = new ModelPart(this);
		right_top.setPivot(0.0F, 0.0F, 0.0F);
		wingsw.addChild(right_top);


		topwing_r1 = new ModelPart(this);
		topwing_r1.setPivot(0.0F, -0.1F, 0.0F);
		right_top.addChild(topwing_r1);
		setRotationAngle(topwing_r1, 0.0F, 0.0F, 0.7854F);
		topwing_r1.setTextureOffset(11, 0).addCuboid(-5.5F, 0.1F, -1.0F, 6.0F, 0.0F, 2.0F, 0.0F, true);

		right_bottom = new ModelPart(this);
		right_bottom.setPivot(0.0F, 0.0F, 0.0F);
		wingsw.addChild(right_bottom);


		bottomwing_r1 = new ModelPart(this);
		bottomwing_r1.setPivot(0.0F, 0.4F, 0.0F);
		right_bottom.addChild(bottomwing_r1);
		setRotationAngle(bottomwing_r1, 0.0F, 0.0F, -0.2618F);
		bottomwing_r1.setTextureOffset(5, 5).addCuboid(-5.0F, -0.4F, -1.0F, 5.0F, 0.0F, 2.0F, 0.0F, true);

		wingse = new ModelPart(this);
		wingse.setPivot(1.5F, -1.0F, -0.5F);
		body.addChild(wingse);


		left_top = new ModelPart(this);
		left_top.setPivot(0.0F, 0.0F, 0.0F);
		wingse.addChild(left_top);


		topwing_r2 = new ModelPart(this);
		topwing_r2.setPivot(0.0F, 0.0F, 0.0F);
		left_top.addChild(topwing_r2);
		setRotationAngle(topwing_r2, 0.0F, 0.0F, -0.7854F);
		topwing_r2.setTextureOffset(11, 0).addCuboid(-0.5F, 0.0F, -1.0F, 6.0F, 0.0F, 2.0F, 0.0F, false);

		left_bottom = new ModelPart(this);
		left_bottom.setPivot(0.3F, 0.0F, 0.0F);
		wingse.addChild(left_bottom);


		bottomwing_r2 = new ModelPart(this);
		bottomwing_r2.setPivot(-0.3F, 0.0F, 0.0F);
		left_bottom.addChild(bottomwing_r2);
		setRotationAngle(bottomwing_r2, 0.0F, 0.0F, 0.2618F);
		bottomwing_r2.setTextureOffset(5, 5).addCuboid(0.0F, 0.0F, -1.0F, 5.0F, 0.0F, 2.0F, 0.0F, false);
	}

	@Override
	public void setAngles(DragonflyEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch)
	{
		float change = 0.436332F; // 20 degrees
		if(entity.age % 2 == 0)
		{
			right_top.roll = change;
			left_top.roll = change;
			right_bottom.roll = -change;
			left_bottom.roll = -change;
		}
		else
		{
			right_top.roll = -change;
			left_top.roll = -change;
			left_bottom.roll = change;
			right_bottom.roll = change;
		}
	}

	@Override
	public Iterable<ModelPart> getParts()
	{
		return ImmutableList.of(body);
	}

	public void setRotationAngle(ModelPart modelPart, float x, float y, float z) {
		modelPart.pitch = x;
		modelPart.yaw = y;
		modelPart.roll = z;
	}

	@Override
	public ModelPart getHead()
	{
		return head;
	}
}