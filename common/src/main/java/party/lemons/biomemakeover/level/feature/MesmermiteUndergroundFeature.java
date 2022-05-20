package party.lemons.biomemakeover.level.feature;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.BulkSectionAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import party.lemons.biomemakeover.block.IlluniteClusterBlock;
import party.lemons.biomemakeover.init.BMBlocks;

import java.util.BitSet;
import java.util.List;
import java.util.Random;

public class MesmermiteUndergroundFeature extends OreFeature
{
    public MesmermiteUndergroundFeature(Codec<OreConfiguration> codec) {
        super(codec);
    }

    protected boolean doPlace(WorldGenLevel worldGenLevel, RandomSource random, OreConfiguration oreConfiguration, double d, double e, double f, double g, double h, double i, int j, int k, int l, int m, int n) {
        List<BlockPos> placePositons = Lists.newArrayList();

        double v;
        double u;
        double t;
        double s;
        int q;
        int o = 0;
        BitSet bitSet = new BitSet(m * n * m);
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        int p = oreConfiguration.size;
        double[] ds = new double[p * 4];
        for (q = 0; q < p; ++q) {
            float r = (float)q / (float)p;
            s = Mth.lerp((double)r, d, e);
            t = Mth.lerp((double)r, h, i);
            u = Mth.lerp((double)r, f, g);
            v = random.nextDouble() * (double)p / 16.0;
            double w = ((double)(Mth.sin((float)Math.PI * r) + 1.0f) * v + 1.0) / 2.0;
            ds[q * 4 + 0] = s;
            ds[q * 4 + 1] = t;
            ds[q * 4 + 2] = u;
            ds[q * 4 + 3] = w;
        }
        for (q = 0; q < p - 1; ++q) {
            if (ds[q * 4 + 3] <= 0.0) continue;
            for (int x = q + 1; x < p; ++x) {
                if (ds[x * 4 + 3] <= 0.0 || !((v = ds[q * 4 + 3] - ds[x * 4 + 3]) * v > (s = ds[q * 4 + 0] - ds[x * 4 + 0]) * s + (t = ds[q * 4 + 1] - ds[x * 4 + 1]) * t + (u = ds[q * 4 + 2] - ds[x * 4 + 2]) * u)) continue;
                if (v > 0.0) {
                    ds[x * 4 + 3] = -1.0;
                    continue;
                }
                ds[q * 4 + 3] = -1.0;
            }
        }
        try (BulkSectionAccess bulkSectionAccess = new BulkSectionAccess(worldGenLevel)){


            for (int x = 0; x < p; ++x) {
                s = ds[x * 4 + 3];
                if (s < 0.0) continue;
                t = ds[x * 4 + 0];
                u = ds[x * 4 + 1];
                v = ds[x * 4 + 2];
                int y = Math.max(Mth.floor(t - s), j);
                int z = Math.max(Mth.floor(u - s), k);
                int aa = Math.max(Mth.floor(v - s), l);
                int ab = Math.max(Mth.floor(t + s), y);
                int ac = Math.max(Mth.floor(u + s), z);
                int ad = Math.max(Mth.floor(v + s), aa);
                for (int ae = y; ae <= ab; ++ae) {
                    double af = ((double)ae + 0.5 - t) / s;
                    if (!(af * af < 1.0)) continue;
                    for (int ag = z; ag <= ac; ++ag) {
                        double ah = ((double)ag + 0.5 - u) / s;
                        if (!(af * af + ah * ah < 1.0)) continue;
                        block11: for (int ai = aa; ai <= ad; ++ai) {
                            LevelChunkSection levelChunkSection;
                            int ak;
                            double aj = ((double)ai + 0.5 - v) / s;
                            if (!(af * af + ah * ah + aj * aj < 1.0) || worldGenLevel.isOutsideBuildHeight(ag) || bitSet.get(ak = ae - j + (ag - k) * m + (ai - l) * m * n)) continue;
                            bitSet.set(ak);
                            mutableBlockPos.set(ae, ag, ai);
                            if (!worldGenLevel.ensureCanWrite(mutableBlockPos) || (levelChunkSection = bulkSectionAccess.getSection(mutableBlockPos)) == null) continue;
                            int al = SectionPos.sectionRelative(ae);
                            int am = SectionPos.sectionRelative(ag);
                            int an = SectionPos.sectionRelative(ai);
                            BlockState blockState = levelChunkSection.getBlockState(al, am, an);
                            for (OreConfiguration.TargetBlockState targetBlockState : oreConfiguration.targetStates) {
                                if (!OreFeature.canPlaceOre(blockState, bulkSectionAccess::getBlockState, random, oreConfiguration, targetBlockState, mutableBlockPos)) continue;
                                levelChunkSection.setBlockState(al, am, an, targetBlockState.state, false);
                                placePositons.add(mutableBlockPos.immutable());
                                ++o;
                                continue block11;
                            }
                        }
                    }
                }
            }
        }

        for(BlockPos pos : placePositons)
        {
            if(random.nextInt(25) == 0)
            {
                for(Direction direction : Direction.values())
                {
                    BlockPos offsetPos = pos.relative(direction);
                    if(worldGenLevel.isEmptyBlock(offsetPos) && random.nextBoolean())
                    {
                        worldGenLevel.setBlock(offsetPos, BMBlocks.ILLUNITE_CLUSTER.get().defaultBlockState().setValue(IlluniteClusterBlock.FACING, direction), 16);
                    }
                }
            }
        }

        return o > 0;
    }
}
