package party.lemons.biomemakeover.mixin.plantplacement;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.init.BMBlocks;

import java.util.function.BiConsumer;

@Mixin(TrunkPlacer.class)
public class TrunkPlacerMixin
{
    @Inject(at = @At("HEAD"), method = "setDirtAt", cancellable = true)
    private static void setDirtAt(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blocks, RandomSource random, BlockPos pos, TreeConfiguration cfg, CallbackInfo cbi) {
        if (level instanceof ServerLevelAccessor sl) {

            //If the current state is a big plant pot, don't replace the block
            BlockState state = sl.getBlockState(pos);
            if (state.is(BMBlocks.BIG_PLANT_POTS))
                cbi.cancel();
        }
    }
}
