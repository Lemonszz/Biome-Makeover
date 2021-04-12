package party.lemons.biomemakeover.util.access;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public interface PillagerSpawnerAccess
{
	void bm_spawn(ServerWorld world, BlockPos pos, boolean leader);
}
