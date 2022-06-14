package party.lemons.biomemakeover.block.blockentity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import party.lemons.biomemakeover.block.LightningBugBottleBlock;
import party.lemons.biomemakeover.block.blockentity.LightningBugBottleBlockEntity;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.util.RandomUtil;

public class LightningBugBottleRenderer implements BlockEntityRenderer<LightningBugBottleBlockEntity>
{
    private final RandomSource random = RandomSource.create();

    public LightningBugBottleRenderer(BlockEntityRendererProvider.Context context)
    {

    }

    @Override
    public void render(LightningBugBottleBlockEntity be, float tickDelta, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay) {
        poseStack.pushPose();

        poseStack.translate(0.5D, 0.0D, 0.5D);
        if(be.getBlockState().is(BMBlocks.LIGHTNING_BUG_BOTTLE.get()) && be.getBlockState().getValue(LightningBugBottleBlock.UPPER))
        {
            poseStack.translate(0, 0.25F, 0);
        }

        Entity entity = be.getEntity();
        if(entity != null)
        {
            if(random.nextInt(100) == 0)
                entity.setPos(random.nextInt(500), random.nextInt(200), random.nextInt(500));

            Minecraft.getInstance().getEntityRenderDispatcher().render(entity, 0.0D, 0.0D, 0.0D, 0.0F, tickDelta, poseStack, multiBufferSource, light);
        }
        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(LightningBugBottleBlockEntity blockEntity) {
        return BlockEntityRenderer.super.shouldRenderOffScreen(blockEntity);
    }
}
