package party.lemons.biomemakeover.entity.render;

import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.TntMinecartEntityRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import party.lemons.biomemakeover.entity.TumbleweedEntity;
import party.lemons.biomemakeover.init.BMBlocks;

public class TumbleweedRender extends EntityRenderer<TumbleweedEntity>
{
	public TumbleweedRender(EntityRenderDispatcher dispatcher)
	{
		super(dispatcher);
	}

	@Override
	public void render(TumbleweedEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light)
	{
		super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);

		matrices.push();
		matrices.translate(0.0D, 0.5D, 0.0D);
		matrices.multiply(slerp(entity.prevQuaternion, entity.quaternion, tickDelta));

		matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-90.0F));
		matrices.translate(-0.5D, -0.5D, 0.5D);
		matrices.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(90.0F));

		MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(BMBlocks.TUMBLEWEED.getDefaultState(), matrices, vertexConsumers, light,  OverlayTexture.DEFAULT_UV);

		matrices.pop();
	}

	public static Quaternion slerp(Quaternion v0, Quaternion v1, float t)
	{
		// From https://en.wikipedia.org/w/index.php?title=Slerp&oldid=928959428
		// License: CC BY-SA 3.0 https://creativecommons.org/licenses/by-sa/3.0/

		float dot = v0.getX() * v1.getX() + v0.getY() * v1.getY() + v0.getZ() * v1.getZ() + v0.getW() * v1.getW();
		if (dot < 0.0f) {
			v1 = new Quaternion(-v1.getX(), -v1.getY(), -v1.getZ(), -v1.getW());
			dot = -dot;
		}

		if (dot > 0.9995F) {
			float x = MathHelper.lerp(t, v0.getX(), v1.getX());
			float y = MathHelper.lerp(t, v0.getY(), v1.getY());
			float z = MathHelper.lerp(t, v0.getZ(), v1.getZ());
			float w = MathHelper.lerp(t, v0.getW(), v1.getW());
			return new Quaternion(x,y,z,w);
		}

		float angle01 = (float)Math.acos(dot);
		float angle0t = angle01*t;
		float sin0t = MathHelper.sin(angle0t);
		float sin01 = MathHelper.sin(angle01);
		float sin1t = MathHelper.sin(angle01 - angle0t);

		float s1 = sin0t / sin01;
		float s0 = sin1t / sin01;

		return new Quaternion(
				s0 * v0.getX() + s1 * v1.getX(),
				s0 * v0.getY() + s1 * v1.getY(),
				s0 * v0.getZ() + s1 * v1.getZ(),
				s0 * v0.getW() + s1 * v1.getW()
		);
	}


	@Override
	public Identifier getTexture(TumbleweedEntity entity)
	{
		return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
	}
}
