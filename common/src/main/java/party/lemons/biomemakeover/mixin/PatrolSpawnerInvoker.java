package party.lemons.biomemakeover.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.PatrolSpawner;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.Random;

@Mixin(PatrolSpawner.class)
public interface PatrolSpawnerInvoker
{
    @Invoker
    boolean callSpawnPatrolMember(ServerLevel level, BlockPos pos, Random random, boolean isLeader);
}
