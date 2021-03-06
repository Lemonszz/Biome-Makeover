package party.lemons.biomemakeover.util;

import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;

import java.util.Random;

public interface DirectionalDataHandler
{
	void handleDirectionalMetadata(String meta, Direction dir, BlockPos pos, StructureWorldAccess world, Random random, BlockBox box);
}
