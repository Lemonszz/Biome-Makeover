package party.lemons.biomemakeover.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.StandingAndWallBlockItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.block.AbstractTapestryBlock;
import party.lemons.biomemakeover.block.blockentity.TapestryBlockEntity;
import party.lemons.biomemakeover.init.BMBlocks;

@Mixin(BlockEntityWithoutLevelRenderer.class)
public class BlockEntityWithoutLevelRendererMixin {

    @Shadow @Final private BlockEntityRenderDispatcher blockEntityRenderDispatcher;
    private static TapestryBlockEntity TAPESTRY;


    @Inject(at = @At("HEAD"), method = "renderByItem", cancellable = true)
    public void renderByItem(ItemStack stack, ItemTransforms.TransformType transformType, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int j, CallbackInfo cbi) {
        if(TAPESTRY == null)
            TAPESTRY = new TapestryBlockEntity(BlockPos.ZERO, BMBlocks.TAPESTRY_FLOOR_BLOCKS.get(0).get().defaultBlockState());

        if(stack.getItem() instanceof StandingAndWallBlockItem standingBlockItem)
        {
            if(standingBlockItem.getBlock() instanceof AbstractTapestryBlock)
            {
                TAPESTRY.setBlockState(standingBlockItem.getBlock().defaultBlockState());
                blockEntityRenderDispatcher.renderItem(TAPESTRY, poseStack, multiBufferSource, i, j);
                cbi.cancel();
            }
        }
    }
}
