package party.lemons.biomemakeover.block.blockentity.render;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.TexturedRenderLayers;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.block.TapestryBlock;
import party.lemons.biomemakeover.block.TapestryWallBlock;
import party.lemons.biomemakeover.block.blockentity.TapestryBlockEntity;

import java.util.List;

public class TapestryBlockEntityRenderer extends BlockEntityRenderer<TapestryBlockEntity>
{
	private final ModelPart banner = createTapestry();

	private final ModelPart pillar = new ModelPart(64, 64, 44, 0);
	private final ModelPart crossbar = new ModelPart(64, 64, 0, 42);;

	public TapestryBlockEntityRenderer(BlockEntityRenderDispatcher r)
	{
		super(r);
		this.pillar.addCuboid(-1.0F, -30.0F, -1.0F, 2.0F, 42.0F, 2.0F, 0.0F);
		this.crossbar.addCuboid(-10.0F, -32.0F, -1.0F, 20.0F, 2.0F, 2.0F, 0.0F);
	}

	private ModelPart createTapestry()
	{
		ModelPart tapestry = new ModelPart(64, 64, 0, 0);
		tapestry.setPivot(0.0F, -16.0F, -1.7F);
		tapestry.setTextureOffset(0, 36).addCuboid(-10.0F, 35.0F, -0.3F, 4.0F, 5.0F, 1.0F, 0.0F, false);
		tapestry.setTextureOffset(10, 36).addCuboid(-2.0F, 35.0F, -0.3F, 4.0F, 5.0F, 1.0F, 0.0F, false);
		tapestry.setTextureOffset(20, 36).addCuboid(6.0F, 35.0F, -0.3F, 4.0F, 5.0F, 1.0F, 0.0F, false);
		tapestry.setTextureOffset(0, 0).addCuboid(-10.0F, 0.0F, -0.3F, 20.0F, 35.0F, 1.0F, 0.0F, false);

		return tapestry;
	}

	public void render(TapestryBlockEntity tapestry, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
		float scale = 0.666F;
		boolean inInventory = tapestry.getWorld() == null;
		matrixStack.push();
		long time;
		if (inInventory)
		{
			time = 0L;
			matrixStack.translate(0.5D, 0.5D, 0.5D);
			this.pillar.visible = true;
		}
		else
		{
			time = tapestry.getWorld().getTime();
			BlockState blockState = tapestry.getCachedState();
			float rotation;
			if (blockState.getBlock() instanceof TapestryWallBlock)
			{
				matrixStack.translate(0.5D, -0.1666666716337204D, 0.5D);
				rotation = -blockState.get(TapestryWallBlock.FACING).asRotation();
				matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(rotation));
				matrixStack.translate(0.0D, -0.3125D, -0.4375D);
				this.pillar.visible = false;
			}
			else
			{
				matrixStack.translate(0.5D, 0.5D, 0.5D);
				rotation = (float)(-(Integer)blockState.get(TapestryBlock.ROTATION) * 360) / 16.0F;
				matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(rotation));
				this.pillar.visible = true;
			}
		}

		matrixStack.push();
		matrixStack.scale(scale, -scale, -scale);
		VertexConsumer vertexConsumer = ModelLoader.BANNER_BASE.getVertexConsumer(vertexConsumerProvider, RenderLayer::getEntitySolid);
		this.pillar.render(matrixStack, vertexConsumer, i, j);
		this.crossbar.render(matrixStack, vertexConsumer, i, j);
		BlockPos blockPos = tapestry.getPos();
		float n = ((float)Math.floorMod((long)(blockPos.getX() * 7 + blockPos.getY() * 9 + blockPos.getZ() * 13) + time, 100L) + f) / 100.0F;
		this.banner.pitch = (-0.0125F + 0.01F * MathHelper.cos(6.2831855F * n)) * 3.1415927F;
		this.banner.pivotY = -32.0F;
		renderTapestry(matrixStack, vertexConsumerProvider, i, j, this.banner, tapestry.getColor());
		matrixStack.pop();
		matrixStack.pop();
	}

	public static void renderTapestry(MatrixStack matrixStack, VertexConsumerProvider vertexConsumer, int light, int overlay, ModelPart bannerPart, DyeColor color) {
		Identifier id = BiomeMakeover.ID("textures/tapestry/" + color.getName() + "_tapestry.png");
		VertexConsumer vc = vertexConsumer.getBuffer(RenderLayer.getEntitySolid(id));
		bannerPart.render(matrixStack, vc, light, overlay);
	}
}
