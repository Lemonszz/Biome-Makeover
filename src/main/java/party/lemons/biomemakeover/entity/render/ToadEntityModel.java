package party.lemons.biomemakeover.entity.render;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.entity.model.CompositeEntityModel;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.client.util.math.Vector4f;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import party.lemons.biomemakeover.entity.ToadEntity;
import party.lemons.biomemakeover.util.RandomUtil;
import party.lemons.biomemakeover.util.access.CuboidAccessor;

public class ToadEntityModel extends CompositeEntityModel<ToadEntity> implements ModelWithHead
{
	private final ModelPart body;
	private final ModelPart cube_r1;
	private final ModelPart leg_back_left_top;
	private final ModelPart leg_back_left_bottom;
	private final ModelPart leg_back_left_top2;
	private final ModelPart leg_back_left_bottom2;
	private final ModelPart leg_front_left;
	private final ModelPart cube_r2;
	private final ModelPart leg_front_right;
	private final ModelPart cube_r3;
	private final ModelPart head;
	private final ModelPart top;
	private final ModelPart cube_r4;
	private final ModelPart cube_r5;
	private final ModelPart jaw;
	private final ModelPart tounge;

	public ToadEntityModel()
	{
		textureWidth = 64;
		textureHeight = 64;

		body = new ModelPart(this);
		body.setPivot(0.0F, 24.0F, 0.0F);


		cube_r1 = new ModelPart(this);
		cube_r1.setPivot(0.0F, -5.5F, -1.0F);
		body.addChild(cube_r1);
		setRotationAngle(cube_r1, -0.2618F, 0.0F, 0.0F);
		cube_r1.setTextureOffset(0, 0).addCuboid(-2.5F, -2.5F, -4.0F, 5.0F, 5.0F, 10.0F, 0.0F, false);

		leg_back_left_top = new ModelPart(this);
		leg_back_left_top.setPivot(1.5F, -4.5F, 2.5F);
		body.addChild(leg_back_left_top);
		setRotationAngle(leg_back_left_top, 0.3927F, 0.0F, 0.0F);
		leg_back_left_top.setTextureOffset(0, 22).addCuboid(0.0F, -1.8415F, -2.7304F, 2.0F, 3.0F, 4.0F, 0.0F, false);

		leg_back_left_bottom = new ModelPart(this);
		leg_back_left_bottom.setPivot(1.0F, 1.9239F, 3.1173F);
		leg_back_left_top.addChild(leg_back_left_bottom);
		leg_back_left_bottom.setTextureOffset(26, 27).addCuboid(-1.0F, -0.8328F, -5.6951F, 2.0F, 3.0F, 2.0F, 0.0F, false);

		leg_back_left_top2 = new ModelPart(this);
		leg_back_left_top2.setPivot(-3.9F, -5.5F, 2.5F);
		body.addChild(leg_back_left_top2);
		setRotationAngle(leg_back_left_top2, 0.3927F, 0.0F, 0.0F);
		leg_back_left_top2.setTextureOffset(20, 0).addCuboid(0.4F, -0.9176F, -3.1131F, 2.0F, 3.0F, 4.0F, 0.0F, false);

		leg_back_left_bottom2 = new ModelPart(this);
		leg_back_left_bottom2.setPivot(1.0F, 2.8478F, 2.7346F);
		leg_back_left_top2.addChild(leg_back_left_bottom2);
		leg_back_left_bottom2.setTextureOffset(20, 24).addCuboid(-0.6F, -0.8328F, -5.6951F, 2.0F, 3.0F, 2.0F, 0.0F, false);

		leg_front_left = new ModelPart(this);
		leg_front_left.setPivot(0.0F, 0.0F, 0.0F);
		body.addChild(leg_front_left);


		cube_r2 = new ModelPart(this);
		cube_r2.setPivot(2.5F, -5.5F, -3.0F);
		leg_front_left.addChild(cube_r2);
		setRotationAngle(cube_r2, -0.2618F, 0.0F, 0.0F);
		cube_r2.setTextureOffset(12, 24).addCuboid(-1.0F, -0.4659F, -1.2588F, 2.0F, 6.0F, 2.0F, 0.0F, false);

		leg_front_right = new ModelPart(this);
		leg_front_right.setPivot(-3.0F, -4.9659F, -2.2588F);
		body.addChild(leg_front_right);


		cube_r3 = new ModelPart(this);
		cube_r3.setPivot(0.5F, -0.5341F, -0.7412F);
		leg_front_right.addChild(cube_r3);
		setRotationAngle(cube_r3, -0.2618F, 0.0F, 0.0F);
		cube_r3.setTextureOffset(0, 0).addCuboid(-1.0F, -0.4659F, -1.2588F, 2.0F, 6.0F, 2.0F, 0.0F, false);

		head = new ModelPart(this);
		head.setPivot(-0.025F, -6.7F, -3.375F);
		body.addChild(head);
		head.setTextureOffset(0, 29).addCuboid(-1.475F, -1.0F, -2.0F, 3.0F, 1.0F, 1.0F, 0.0F, false);

		top = new ModelPart(this);
		top.setPivot(0.0F, 0.0F, 0.0F);
		head.addChild(top);
		top.setTextureOffset(15, 17).addCuboid(-2.475F, -2.3F, -4.625F, 5.0F, 2.0F, 5.0F, 0.0F, false);

		cube_r4 = new ModelPart(this);
		cube_r4.setPivot(-1.775F, -2.7F, -2.625F);
		top.addChild(cube_r4);
		setRotationAngle(cube_r4, 0.0F, 0.0F, -0.1745F);
		cube_r4.setTextureOffset(28, 0).addCuboid(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);

		cube_r5 = new ModelPart(this);
		cube_r5.setPivot(1.725F, -2.7F, -2.625F);
		top.addChild(cube_r5);
		setRotationAngle(cube_r5, 0.0F, 0.0F, 0.1745F);
		cube_r5.setTextureOffset(20, 7).addCuboid(-1.0F, -0.5F, -1.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);

		jaw = new ModelPart(this);
		jaw.setPivot(0.025F, 6.7F, 3.375F);
		head.addChild(jaw);
		jaw.setTextureOffset(0, 15).addCuboid(-2.5F, -7.0F, -8.0F, 5.0F, 2.0F, 5.0F, 0.0F, false);

		tounge = new TonguePart(this);
		tounge.setPivot(0.025F, -0.5F, -1.5F);
		head.addChild(tounge);
		tounge.setTextureOffset(0, 29).addCuboid(-1.475F, -1.0F, -2.0F, 3.0F, 1.0F, 1.0F, 0.0F, false);
	}

	static float tongueDistance;
	static ToadEntity entity;

	@Override
	public void setAngles(ToadEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch)
	{
		ToadEntityModel.entity = entity;
		float pi = (float)Math.PI;
		this.head.pitch = -0.2618F + (headPitch * 0.0175F);
		this.head.yaw = headYaw * 0.0175F;

		if(entity.hasTongueEntity())
		{
			Entity e = entity.world.getEntityById(entity.getTongueEntityID());
			if(e != null && entity.isToungeReady())
			{
				tongueDistance = (entity.distanceTo(e) * 16) - ((float)(e.getBoundingBox().maxX - e.getBoundingBox().minX) * 16F);
			}
			else
				tongueDistance = 0;
		}
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

	@Override
	public ModelPart getHead()
	{
		return head;
	}

	private static class TonguePart extends ModelPart
	{
		TonguePart(Model model)
		{
			super(model);
		}

		@Override
		public void render(MatrixStack matrices, VertexConsumer vertices, int light, int overlay, float red, float green, float blue, float alpha)
		{
			ModelPart.Cuboid cube = getRandomCuboid(RandomUtil.RANDOM);
			drawBox(matrices, vertices, cube.minX, cube.minY, cube.minZ, cube.maxX, cube.maxY, cube.minZ - entity.toungeDistance, light, overlay);
			super.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		}

		public static void drawBox(MatrixStack matrices, VertexConsumer vertexConsumer, float x1, float y1, float z1, float x2, float y2, float z2, int light, int overlay) {
			Cuboid cuboid = new Cuboid(0, 30, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, 0, 0, 0, false, 64, 54);

			MatrixStack.Entry me = matrices.peek();
			Matrix4f matrix4f = me.getModel();
			Matrix3f matrix3f = me.getNormal();

			ModelPart.Quad[] var13 = ((CuboidAccessor)cuboid).getSides();
			int var14 = var13.length;

			for(int var15 = 0; var15 < var14; ++var15) {
				ModelPart.Quad quad = var13[var15];
				Vector3f vector3f = quad.direction.copy();
				vector3f.transform(matrix3f);
				float f = vector3f.getX();
				float g = vector3f.getY();
				float h = vector3f.getZ();

				for(int i = 0; i < 4; ++i) {
					ModelPart.Vertex vertex = quad.vertices[i];
					float j = vertex.pos.getX() / 16.0F;
					float k = vertex.pos.getY() / 16.0F;
					float l = vertex.pos.getZ() / 16.0F;
					Vector4f vector4f = new Vector4f(j, k, l, 1.0F);
					vector4f.transform(matrix4f);
					vertexConsumer.vertex(vector4f.getX(), vector4f.getY(), vector4f.getZ(), 1, 0, 0, 1, 0.1F, 0.1F, overlay, light, 1, 1, 1);
				}
			}
		}
	}
}
