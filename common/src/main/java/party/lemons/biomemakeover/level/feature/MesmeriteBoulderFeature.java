package party.lemons.biomemakeover.level.feature;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;
import party.lemons.biomemakeover.block.IlluniteClusterBlock;
import party.lemons.biomemakeover.init.BMBlocks;

import java.util.List;
import java.util.Random;

public class MesmeriteBoulderFeature extends Feature<BlockStateConfiguration>
{
    public MesmeriteBoulderFeature(Codec<BlockStateConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<BlockStateConfiguration> ctx) {
        BlockPos blockPos = ctx.origin();
        RandomSource random = ctx.random();
        WorldGenLevel level = ctx.level();

        for(; blockPos.getY() > 3; blockPos = blockPos.below())
        {
            if(!level.isEmptyBlock(blockPos.below()))
            {
                BlockState block = level.getBlockState(blockPos.below());
                if(isDirt(block) || isStone(block))
                {
                    break;
                }
            }
        }

        if(blockPos.getY() <= 3)
        {
            return false;
        }
        else
        {
            List<BlockPos> rockPositions = Lists.newArrayList();
            for(int i = 0; i < 4; ++i)
            {
                int xSize = random.nextInt(3);
                int ySize = random.nextInt(4);
                int zSize = random.nextInt(3);
                float distance = (float) (xSize + ySize + zSize) * 0.333F + 0.5F;

                for(BlockPos genPos : BlockPos.betweenClosed(blockPos.offset(-xSize, -ySize, -zSize), blockPos.offset(xSize, ySize, zSize)))
                {
                    if(genPos.distSqr(blockPos) <= (double) (distance * distance))
                    {
                        BlockPos placePos = level.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, genPos);
                        BlockState belowState = level.getBlockState(placePos.below());
                        int maxAttempts = 100;

                        while(maxAttempts > 0 && (belowState.getMaterial().isReplaceable() || belowState.is(BlockTags.LOGS) || belowState.is(BlockTags.LEAVES)))
                        {
                            placePos = placePos.below();
                            belowState = level.getBlockState(placePos.below());
                            maxAttempts--;
                        }
                        if(maxAttempts <= 0)
                            return false;

                        if(!(belowState.is(BMBlocks.MESMERITE.get()) || isDirt(belowState) || isStone(belowState) || belowState.getBlock() == Blocks.GRAVEL))
                            continue;

                        level.setBlock(placePos, ctx.config().state, 4);
                        rockPositions.add(placePos);
                    }
                }
                blockPos = blockPos.offset(-1 + random.nextInt(2), -random.nextInt(2), -1 + random.nextInt(2));
            }
            for(BlockPos placePositions : rockPositions)
            {
                if(random.nextInt(10) == 0)
                {
                    for(Direction direction : Direction.values())
                    {
                        BlockPos offsetPos = placePositions.relative(direction);
                        if(level.isEmptyBlock(offsetPos) && random.nextBoolean())
                        {
                            level.setBlock(offsetPos, BMBlocks.ILLUNITE_CLUSTER.get().defaultBlockState().setValue(IlluniteClusterBlock.FACING, direction), 16);
                        }
                    }
                }
            }

            return true;
        }
    }
}
