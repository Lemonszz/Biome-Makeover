package party.lemons.biomemakeover.level.golem;

import com.mojang.datafixers.util.Pair;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.pattern.BlockPattern;
import com.google.common.collect.Lists;
import party.lemons.biomemakeover.entity.PlayerCreatable;

import java.util.List;

public final class GolemHandler
{
    private static List<GolemInstance> golems = Lists.newArrayList();

    public static void addPattern(BlockPattern fullPattern, BlockPattern basePattern, GolemResult result)
    {
        golems.add(new GolemInstance(fullPattern, basePattern, result));
    }

    public static boolean canDispenseGolem(LevelReader levelReader, BlockPos blockPos)
    {
        for(GolemInstance golemInstance : golems)
        {
            if(golemInstance.basePattern.find(levelReader, blockPos) != null)
                return true;
        }
        return false;
    }

    public static boolean checkAndCreateGolem(Level level, BlockPos pos)
    {
        for(GolemInstance golem : golems)
        {
            BlockPattern.BlockPatternMatch fullMatch = golem.fullPattern.find(level, pos);
            if (fullMatch == null)  //Didn't find the pattern.
                continue;

            for (int x = 0; x < golem.fullPattern.getWidth(); ++x) {
                for (int y = 0; y < golem.fullPattern.getHeight(); ++y)
                {
                    BlockInWorld block = fullMatch.getBlock(x, y, 0);
                    level.setBlock(block.getPos(), Blocks.AIR.defaultBlockState(), 2);
                    level.levelEvent(LevelEvent.PARTICLES_DESTROY_BLOCK, block.getPos(), Block.getId(block.getState()));
                }
            }
            BlockPos placePosition = fullMatch.getBlock(golem.fullPattern.getWidth() / 2, golem.fullPattern.getHeight() - 1, 0).getPos();
            if(golem.result.spawnGolem(level, golem, pos, placePosition))
            {
                for (int x = 0; x < golem.fullPattern.getWidth(); ++x) {
                    for (int y = 0; y < golem.fullPattern.getHeight(); ++y) {
                        BlockInWorld removePosition = fullMatch.getBlock(x, y, 0);
                        level.blockUpdated(removePosition.getPos(), Blocks.AIR);
                    }
                }
            }
            return true;
        }
        return false;
    }

    public record GolemInstance(BlockPattern fullPattern, BlockPattern basePattern, GolemResult result)
    {
    }

    @FunctionalInterface
    public interface GolemResult
    {
        boolean spawnGolem(Level level, GolemInstance golemInstance, BlockPos pumpkinPos, BlockPos placePosition);
    }

    public static class SummonGolemResult<T extends AbstractGolem & PlayerCreatable> implements GolemResult
    {
        private final EntityType<T> golemType;

        public SummonGolemResult(EntityType<T> golem)
        {
            this.golemType = golem;
        }

        @Override
        public boolean spawnGolem(Level level, GolemInstance golemInstance, BlockPos pumpkinPos, BlockPos placePosition)
        {
            T golem = golemType.create(level);
            golem.setPlayerCreated(true);
            golem.moveTo((double)placePosition.getX() + 0.5, (double)placePosition.getY() + 0.05, (double)placePosition.getZ() + 0.5, 0.0f, 0.0f);
            level.addFreshEntity(golem);
            for (ServerPlayer serverPlayer : level.getEntitiesOfClass(ServerPlayer.class, golem.getBoundingBox().inflate(5.0))) {
                CriteriaTriggers.SUMMONED_ENTITY.trigger(serverPlayer, golem);
            }

            return true;
        }
    }

    private GolemHandler()
    {

    }
}
