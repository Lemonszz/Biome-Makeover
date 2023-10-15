package party.lemons.biomemakeover.block.fabric;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;

public class AloeVeraBlockImpl {
    public static boolean onPreGrow(ServerLevel level, BlockPos pos, BlockState state, boolean isGrowing)
    {
        return isGrowing;
    }

    public static void onPostGrow(ServerLevel level, BlockPos pos, BlockState state) {
    }
}
