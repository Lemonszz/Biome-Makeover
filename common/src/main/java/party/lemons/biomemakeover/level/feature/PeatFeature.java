package party.lemons.biomemakeover.level.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.Iterator;
import java.util.Random;

public class PeatFeature extends Feature<NoneFeatureConfiguration>
{
    public PeatFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        boolean isSuccessful = false;
        int radius = RandomUtil.randomRange(4, 8);
        BlockPos origin = ctx.origin();
        WorldGenLevel level = ctx.level();
        Random random = ctx.random();

        BlockPos centerPos = new BlockPos(origin.getX(), 61, origin.getZ());
        if(!isWaterNearby(ctx.level(), centerPos)) return false;

        int placeCount = 0;
        for(int xx = centerPos.getX() - radius; xx <= centerPos.getX() + radius; xx++)
        {
            for(int zz = centerPos.getZ() - radius; zz <= centerPos.getZ() + radius; zz++)
            {
                int offsetX = xx - centerPos.getX();
                int offsetZ = zz - centerPos.getZ();
                if(offsetX * offsetX + offsetZ * offsetZ <= radius * radius)
                {
                    BlockPos placePos = new BlockPos(xx, centerPos.getY(), zz);
                    BlockState upState = level.getBlockState(placePos.above());
                    if(upState.is(Blocks.GRASS_BLOCK))
                    {
                        placePeat(placePos, level, random, 5);
                        if(random.nextInt(20) == 0)
                        {
                            level.setBlock(placePos.above(), BMBlocks.MOSSY_PEAT.get().defaultBlockState(), 2);
                        }
                        placeCount++;
                        isSuccessful = true;
                    }
                }
            }
        }

        if(placeCount > 5)
        {
            for(int xx = centerPos.getX() - radius; xx <= centerPos.getX() + radius; xx++)
            {
                for(int zz = centerPos.getZ() - radius; zz <= centerPos.getZ() + radius; zz++)
                {
                    int offsetX = xx - centerPos.getX();
                    int offsetZ = zz - centerPos.getZ();
                    if(offsetX * offsetX + offsetZ * offsetZ <= radius * radius)
                    {
                        BlockPos placePos = new BlockPos(xx, centerPos.getY(), zz);
                        BlockState upState = level.getBlockState(placePos.above());
                        if(random.nextInt(5) == 0 && level.getBlockState(placePos).canOcclude() && upState.is(Blocks.WATER) && level.getBlockState(placePos.above(2)).isAir())
                        {
                            level.setBlock(placePos, BMBlocks.MOSSY_PEAT.get().defaultBlockState(), 2);
                        }
                    }
                }
            }
        }

        return isSuccessful;
    }

    private static boolean isWaterNearby(WorldGenLevel level, BlockPos pos)
    {
        Iterator<BlockPos> iterators = BlockPos.betweenClosed(pos.offset(-3, 0, -3), pos.offset(3, 1, 3)).iterator();

        BlockPos blockPos;
        do
        {
            if(!iterators.hasNext())
            {
                return false;
            }

            blockPos = iterators.next();
        }while(!level.getFluidState(blockPos).is(FluidTags.WATER));

        return true;
    }

    private void placePeat(BlockPos pos, WorldGenLevel level, Random random, int chance)
    {
        level.setBlock(pos, BMBlocks.PEAT.get().defaultBlockState(), 2);
        if(random.nextInt(chance) == 0)
        {
            placePeat(pos.below(), level, random, chance + 2);
        }
    }

}