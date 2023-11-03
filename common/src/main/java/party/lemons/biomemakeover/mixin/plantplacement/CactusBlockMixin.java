package party.lemons.biomemakeover.mixin.plantplacement;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.init.BMBlocks;

@Mixin(CactusBlock.class)
public class CactusBlockMixin
{
    @Inject(at = @At("TAIL"), method = "canSurvive", cancellable = true)
    public void canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos, CallbackInfoReturnable<Boolean> cbi) {
        if(levelReader.getBlockState(blockPos.below()).is(BMBlocks.SAND_PLANT_POTS) && !levelReader.getBlockState(blockPos.above()).liquid())
            cbi.setReturnValue(true);
    }
}