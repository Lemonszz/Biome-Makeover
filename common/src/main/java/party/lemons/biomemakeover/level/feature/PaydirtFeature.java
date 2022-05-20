package party.lemons.biomemakeover.level.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import party.lemons.biomemakeover.init.BMBlocks;

import java.util.Random;

public class PaydirtFeature extends Feature<NoneFeatureConfiguration>
{
    public PaydirtFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx)
    {
        RandomSource random = ctx.random();
        BlockPos blockPos = ctx.origin();
        WorldGenLevel level = ctx.level();
        for(int i = 0; i < 3; ++i)
        {
            int xSize = random.nextInt(4);
            int ySize = random.nextInt(4);
            int zSize = random.nextInt(4);
            float distance = (float) (xSize + ySize + zSize) * 0.333F + 0.5F;

            for(BlockPos generatePos : BlockPos.betweenClosed(blockPos.offset(-xSize, -ySize, -zSize), blockPos.offset(xSize, ySize, zSize)))
            {
                BlockState currentState = level.getBlockState(generatePos);
                if(currentState.getBlock() == Blocks.WATER || currentState.isAir() || !currentState.canOcclude()) continue;

                if(generatePos.distSqr(blockPos) <= (double) (distance * distance))
                {
                    boolean found = false;
                    for(Direction dir : Direction.values())
                    {
                        Block st = level.getBlockState(generatePos.relative(dir)).getBlock();
                        if(st == Blocks.WATER || (st == BMBlocks.PAYDIRT && random.nextBoolean()))
                        {
                            found = true;
                            break;
                        }
                    }

                    if(!found) continue;

                    level.setBlock(generatePos, BMBlocks.PAYDIRT.get().defaultBlockState(), 4);
                }
            }

            blockPos = blockPos.offset(-1 + random.nextInt(2), -random.nextInt(2), -1 + random.nextInt(2));
        }
        return true;
    }
}
