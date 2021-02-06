package party.lemons.biomemakeover.block.blockentity.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import party.lemons.biomemakeover.block.LightningBugBottleBlock;
import party.lemons.biomemakeover.block.blockentity.LightningBugBottleBlockEntity;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.util.RandomUtil;

public class LightningBugBottleBlockRenderer extends BlockEntityRenderer<LightningBugBottleBlockEntity>
{
	public LightningBugBottleBlockRenderer(BlockEntityRenderDispatcher dispatcher)
	{
		super(dispatcher);
	}

	@Override
	public void render(LightningBugBottleBlockEntity be, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay)
	{
		matrices.push();

		matrices.translate(0.5D, 0.0D, 0.5D);
		if(be.getCachedState().isOf(BMBlocks.LIGHTNING_BUG_BOTTLE) && be.getCachedState().get(LightningBugBottleBlock.UPPER))
		{
			matrices.translate(0, 0.25F, 0);
		}

		Entity entity = be.getEntity();
		if(entity != null)
		{
			if(RandomUtil.RANDOM.nextInt(100) == 0)
				entity.setPos(RandomUtil.RANDOM.nextInt(500), RandomUtil.RANDOM.nextInt(200), RandomUtil.RANDOM.nextInt(500));

			MinecraftClient.getInstance().getEntityRenderDispatcher().render(entity, 0.0D, 0.0D, 0.0D, 0.0F, tickDelta, matrices, vertexConsumers, light);
		}
		matrices.pop();
	}
}
