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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import party.lemons.biomemakeover.entity.ToadEntity;
import party.lemons.biomemakeover.util.MathUtils;
import party.lemons.biomemakeover.util.RandomUtil;
import party.lemons.biomemakeover.util.access.CuboidAccessor;

public class ToadEntityModel extends CompositeEntityModel<ToadEntity> implements ModelWithHead
{
	private final ModelPart body;
	private final ModelPart backlege;
	private final ModelPart backlegw;
	private final ModelPart eyeballw;
	private final ModelPart eyeballe;
	private final ModelPart lips;
	private final ModelPart lipBottom;
	private final ModelPart lipTop;
	private final ModelPart frontlegw;
	private final ModelPart cube_r1;
	private final ModelPart frontlege;
	private final ModelPart cube_r2;
	private final ModelPart tongue;

	public ToadEntityModel()
	{
		textureWidth = 64;
		textureHeight = 64;

		body = new ModelPart(this);
		body.setPivot(0.0F, 24.0F, -0.2727F);
		body.setTextureOffset(0, 0).addCuboid(-4.0F, -8.0F, -2.0F, 8.0F, 8.0F, 8.0F, 0.0F, false);
		body.setTextureOffset(10, 24).addCuboid(1.0F, -10.0F, -1.0F, 2.0F, 2.0F, 3.0F, 0.0F, false);
		body.setTextureOffset(0, 24).addCuboid(-3.0F, -10.0F, -1.0F, 2.0F, 2.0F, 3.0F, 0.0F, false);

		backlege = new ModelPart(this);
		backlege.setPivot(-4.0F, -3.0F, 5.0F);
		body.addChild(backlege);
		backlege.setTextureOffset(8, 16).addCuboid(-2.0F, 2.0F, -4.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);
		backlege.setTextureOffset(12, 16).addCuboid(-2.0F, -1.0F, -2.0F, 2.0F, 4.0F, 4.0F, 0.0F, false);

		backlegw = new ModelPart(this);
		backlegw.setPivot(4.0F, -3.0F, 5.0F);
		body.addChild(backlegw);
		backlegw.setTextureOffset(0, 16).addCuboid(0.0F, -1.0F, -2.0F, 2.0F, 4.0F, 4.0F, 0.0F, false);
		backlegw.setTextureOffset(24, 0).addCuboid(0.0F, 2.0F, -4.0F, 2.0F, 1.0F, 2.0F, 0.0F, false);

		eyeballw = new ModelPart(this);
		eyeballw.setPivot(2.5F, -9.5F, 0.5F);
		body.addChild(eyeballw);
		eyeballw.setTextureOffset(18, 28).addCuboid(0.0F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);

		eyeballe = new ModelPart(this);
		eyeballe.setPivot(-2.5F, -9.5F, 0.5F);
		body.addChild(eyeballe);
		eyeballe.setTextureOffset(24, 3).addCuboid(-1.0F, 0.0F, -1.0F, 1.0F, 2.0F, 2.0F, 0.0F, false);

		lips = new ModelPart(this);
		lips.setPivot(0.0F, -4.0F, -2.0F);
		body.addChild(lips);

		lipBottom = new ModelPart(this);
		lipBottom.setPivot(0.0F, 0.0F, 0.0F);
		lips.addChild(lipBottom);
		lipBottom.setTextureOffset(20, 18).addCuboid(-3.0F, 1.0F, -1.0F, 6.0F, 1.0F, 1.0F, 0.0F, false);

		lipTop = new ModelPart(this);
		lipTop.setPivot(0.0F, 0.0F, 0.0F);
		lips.addChild(lipTop);
		lipTop.setTextureOffset(20, 16).addCuboid(-3.0F, 0.0F, -1.0F, 6.0F, 1.0F, 1.0F, 0.0F, false);

		frontlegw = new ModelPart(this);
		frontlegw.setPivot(4.0F, -5.0F, -1.0F);
		body.addChild(frontlegw);


		cube_r1 = new ModelPart(this);
		cube_r1.setPivot(0.0F, -1.0F, 1.0F);
		frontlegw.addChild(cube_r1);
		setRotationAngle(cube_r1, -0.3491F, 0.0F, 0.0F);
		cube_r1.setTextureOffset(22, 22).addCuboid(0.0F, 0.684F, -1.3794F, 2.0F, 6.0F, 2.0F, 0.0F, false);

		frontlege = new ModelPart(this);
		frontlege.setPivot(-4.0F, -5.0F, -1.0F);
		body.addChild(frontlege);


		cube_r2 = new ModelPart(this);
		cube_r2.setPivot(0.0F, 5.0F, -2.0F);
		frontlege.addChild(cube_r2);
		setRotationAngle(cube_r2, -0.3491F, 0.0F, 0.0F);
		cube_r2.setTextureOffset(0, 0).addCuboid(-2.0F, -6.0F, 0.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);

		tongue = new TonguePart(this);
		tongue.setPivot(0.1F, 0.0F, -4.1273F);
		body.addChild(tongue);
		tongue.setTextureOffset(0, 30).addCuboid(-1.0F, -1.5F, -0.8F, 2.0F, 1.0F, 1.0F, 0.0F, false);
	}


	static float tongueDistance;
	static ToadEntity entity;

	@Override
	public void setAngles(ToadEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch)
	{
		ToadEntityModel.entity = entity;
		float pi = (float) Math.PI;
		float legAmount = 1.4F;

		this.frontlege.pitch = MathHelper.cos(limbAngle * 1 + pi) * 1.4F * limbDistance;
		this.backlege.pitch = MathHelper.cos(limbAngle * 1) * legAmount * limbDistance;

		this.frontlegw.pitch = MathHelper.cos(limbAngle * 1) * 1.4F * limbDistance;
		this.backlegw.pitch = MathHelper.cos(limbAngle * 1 + pi) * legAmount * limbDistance;

		if(!entity.isOnGround())
		{
			this.backlegw.pitch = 2F;
			this.backlege.pitch = 2F;
		}

		if(entity.hasTongueEntity())
		{
			entity.mouthDistance = MathUtils.approachValue(entity.mouthDistance, 1, 0.5F);
			Entity e = entity.world.getEntityById(entity.getTongueEntityID());
			if(e != null && entity.isTongueReady())
			{
				tongueDistance = (entity.distanceTo(e) * 16) - ((float) (e.getBoundingBox().maxX - e.getBoundingBox().minX) * 16F);
			}else tongueDistance = 0;
		}else
		{
			entity.mouthDistance = MathUtils.approachValue(entity.mouthDistance, 0, 0.10F);
		}
		lipTop.pivotY = -entity.mouthDistance;
		lipBottom.pivotY = entity.mouthDistance;

		tongue.pitch = -0.2618F + (headPitch * 0.0175F);
		tongue.yaw = headYaw * 0.0175F;
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
		return tongue;
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
			matrices.push();
			if(this.roll != 0.0F)
			{
				matrices.multiply(Vector3f.POSITIVE_Z.getRadialQuaternion(this.roll));
			}

			if(this.yaw != 0.0F)
			{
				matrices.multiply(Vector3f.POSITIVE_Y.getRadialQuaternion(this.yaw));
			}

			if(this.pitch != 0.0F)
			{
				matrices.multiply(Vector3f.POSITIVE_X.getRadialQuaternion(this.pitch));
			}


			drawBox(matrices, vertices, cube.minX, cube.minY, cube.minZ, cube.maxX, cube.maxY, cube.minZ - entity.tongueDistance, light, overlay);

			matrices.pop();
			super.render(matrices, vertices, light, overlay, red, green, blue, alpha);
		}

		public static void drawBox(MatrixStack matrices, VertexConsumer vertexConsumer, float x1, float y1, float z1, float x2, float y2, float z2, int light, int overlay)
		{
			Cuboid cuboid = new Cuboid(0, 30, x1, y1, z1, x2 - x1, y2 - y1, z2 - z1, 0, 0, 0, false, 64, 54);

			MatrixStack.Entry me = matrices.peek();
			Matrix4f matrix4f = me.getModel();
			Matrix3f matrix3f = me.getNormal();

			ModelPart.Quad[] var13 = ((CuboidAccessor) cuboid).bm_getSides();
			int var14 = var13.length;

			for(int var15 = 0; var15 < var14; ++var15)
			{
				ModelPart.Quad quad = var13[var15];
				Vector3f vector3f = quad.direction.copy();
				vector3f.transform(matrix3f);

				for(int i = 0; i < 4; ++i)
				{
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
