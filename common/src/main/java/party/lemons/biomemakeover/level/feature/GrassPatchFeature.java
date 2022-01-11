package party.lemons.biomemakeover.level.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.features.OreFeatures;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import party.lemons.biomemakeover.level.feature.config.GrassPatchFeatureConfig;
import party.lemons.biomemakeover.util.RandomUtil;

public class GrassPatchFeature extends Feature<GrassPatchFeatureConfig>
{
    public GrassPatchFeature(Codec configCodec)
    {
        super(configCodec);
    }

    @Override
    public boolean place(FeaturePlaceContext<GrassPatchFeatureConfig> context)
    {
        int range = RandomUtil.randomRange(4, 4 + context.config().size);
        BlockPos.MutableBlockPos mp = new BlockPos.MutableBlockPos();

        System.out.println(context.origin());
        for(int yy = range - 1; yy > -range; yy--)
        {
            for(int xx = -range; xx < range; xx++)
            {
                for(int zz = -range; zz < range; zz++)
                {
                    mp.setWithOffset(context.origin(), xx, yy, zz);

                    BlockState currentState = context.level().getBlockState(mp);
                    boolean isSide = yy == -range || yy == range - 1 || xx == -range || xx == range - 1 || zz == -range || zz == range - 1;
                    if(isSide && context.random().nextBoolean()) continue;

                    if(OreFeatures.NATURAL_STONE.test(currentState, context.random()))
                    {
                        if(context.level().isEmptyBlock(mp.above()))
                            context.level().setBlock(mp, context.config().grass.getState(context.random(), mp), 3);
                        else if(context.level().getBlockState(mp.above()).is(context.config().grass.getState(context.random(), mp).getBlock()) || context.level().getBlockState(mp.above(2)).is(context.config().grass.getState(context.random(), mp).getBlock()) || (context.level().getBlockState(mp.above(3)).is(context.config().grass.getState(context.random(), mp).getBlock()) && context.random().nextBoolean()))
                            context.level().setBlock(mp, context.config().dirt.getState(context.random(), mp), 3);
                    }
                }
            }
        }
        return true;
    }
}