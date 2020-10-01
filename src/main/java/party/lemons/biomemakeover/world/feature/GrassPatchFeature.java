package party.lemons.biomemakeover.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.OreFeature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import party.lemons.biomemakeover.util.RandomUtil;
import party.lemons.biomemakeover.world.feature.config.GrassPatchFeatureConfig;

import java.util.Random;

public class GrassPatchFeature extends Feature<GrassPatchFeatureConfig> {
    public GrassPatchFeature(Codec configCodec) {
        super(configCodec);
    }

    @Override
    public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, GrassPatchFeatureConfig featureConfig) {
        int range = RandomUtil.randomRange(4, 4 + featureConfig.size);
        BlockPos.Mutable mp = new BlockPos.Mutable();

        for(int yy = -range; yy < range; yy++)
        {
            for(int xx = -range; xx < range; xx++)
            {
                for(int zz = -range; zz < range; zz++)
                {
                    mp.set(blockPos, xx, yy, zz);

                    BlockState currentState = world.getBlockState(mp);
                    boolean isSide = yy == -range || yy == range || xx == -range || xx == range || zz == -range || zz == range;
                    if(isSide && random.nextBoolean())
                        continue;

                    if(OreFeatureConfig.Rules.BASE_STONE_OVERWORLD.test(currentState, random))
                    {
                        if(world.isAir(mp.up()))
                            world.setBlockState(mp, featureConfig.grass.getBlockState(random, mp), 3);
                        else
                            world.setBlockState(mp, featureConfig.dirt.getBlockState(random, mp), 3);
                    }
                }
            }
        }
        return true;
    }
}
