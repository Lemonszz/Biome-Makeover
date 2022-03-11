package party.lemons.biomemakeover.level.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.material.Fluids;
import party.lemons.biomemakeover.init.BMBlocks;

import java.util.Random;

public class ReedFeature extends Feature<NoneFeatureConfiguration>
{
    private static final int TRIES = 20;
    private static final int SPREADX = 4;
    private static final int SPREADZ = 4;
    private static final int SPREADY = 0;
    private static final BlockStateProvider STATES = new WeightedStateProvider(SimpleWeightedRandomList .<BlockState>builder()
                .add(BMBlocks.REED.get().defaultBlockState(), 10)
                .add(BMBlocks.CATTAIL.get().defaultBlockState(), 5)
        );
    public ReedFeature(Codec<NoneFeatureConfiguration> codec)
    {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        WorldGenLevel level = ctx.level();
        BlockPos origin = ctx.origin();
        Random random = ctx.random();


        BlockPos centerPos = level.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, origin).below();

        int successes = 0;
        BlockPos.MutableBlockPos placePos = new BlockPos.MutableBlockPos();

        for(int j = 0; j < TRIES; ++j)
        {
            BlockState blockState = STATES.getState(random, placePos);

            placePos.setWithOffset(centerPos, random.nextInt(SPREADX + 1) - random.nextInt(SPREADX + 1), random.nextInt(SPREADY + 1) - random.nextInt(SPREADY + 1), random.nextInt(SPREADZ + 1) - random.nextInt(SPREADZ + 1));
            BlockPos genPos = placePos.below();
            BlockPos floorPos = genPos.below();
            if(level.getFluidState(genPos).getType() == Fluids.WATER && level.getFluidState(placePos).isEmpty() && level.getFluidState(floorPos).isEmpty())
            {
                if (blockState.getBlock() instanceof DoublePlantBlock) {
                    if (level.isEmptyBlock(genPos.above())) {
                        DoublePlantBlock.placeAt(level, blockState, genPos, 2);
                        successes++;
                    }
                } else {
                    level.setBlock(genPos, blockState, 2);
                    successes++;
                }
            }

        }

        return successes > 0;
    }
}
