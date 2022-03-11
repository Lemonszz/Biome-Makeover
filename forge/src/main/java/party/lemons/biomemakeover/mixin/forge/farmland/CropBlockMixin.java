package party.lemons.biomemakeover.mixin.forge.farmland;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.init.BMBlocks;

@Mixin(CropBlock.class)
public class CropBlockMixin
{
    @Inject(at = @At("HEAD"), method = "mayPlaceOn", cancellable = true)
    private void mayPlaceOn(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CallbackInfoReturnable<Boolean> cbi)
    {
        if(blockState.is(BMBlocks.PEAT_FARMLAND.get()))
            cbi.setReturnValue(true);

    }

    @Redirect(at = @At(value = "INVOKE", target = "net/minecraft/world/level/block/state/BlockState.is(Lnet/minecraft/world/level/block/Block;)Z", ordinal = 0), method = "getGrowthSpeed")
    private static boolean isFarmland(BlockState blockState, Block block)
    {
        return blockState.is(Blocks.FARMLAND) || blockState.is(BMBlocks.PEAT_FARMLAND.get());
    }
}
