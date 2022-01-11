package party.lemons.biomemakeover.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.WorldGenLevel;

import java.util.Random;

public interface DirectionalDataHandler
{
    void handleDirectionalMetadata(String meta, Direction dir, BlockPos pos, WorldGenLevel world, Random random);
}