package party.lemons.biomemakeover.block.forge;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;

public class AloeVeraBlockImpl {
    public static boolean onPreGrow(ServerLevel level, BlockPos pos, BlockState state, boolean isGrowing) {
        return ForgeHooks.onCropsGrowPre(level, pos, state, isGrowing);
    }

    public static void onPostGrow(ServerLevel level, BlockPos pos, BlockState state)
    {
        ForgeHooks.onCropsGrowPost(level, pos, state);
    }
}
