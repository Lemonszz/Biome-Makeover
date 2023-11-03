package party.lemons.biomemakeover.mixin.plantplacement;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RootsBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.init.BMBlocks;

@Mixin(RootsBlock.class)
public class RootsBlockMixin
{
    @Inject(at = @At("TAIL"), method = "mayPlaceOn", cancellable = true)
    protected void mayPlaceOn(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CallbackInfoReturnable<Boolean> cbi)
    {
        if(blockState.is(BMBlocks.NYLIUM_PLANT_POTS))
            cbi.setReturnValue(true);
    }
}