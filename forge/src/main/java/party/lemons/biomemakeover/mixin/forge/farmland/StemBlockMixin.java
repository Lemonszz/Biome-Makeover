package party.lemons.biomemakeover.mixin.forge.farmland;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.init.BMBlocks;

@Mixin(StemBlock.class)
public class StemBlockMixin {

    @Inject(at = @At("HEAD"), method = "mayPlaceOn", cancellable = true)
    public void mayPlaceOn(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CallbackInfoReturnable<Boolean> cbi)
    {
        if(blockState.is(BMBlocks.PEAT_FARMLAND.get()))
            cbi.setReturnValue(true);
    }
}
