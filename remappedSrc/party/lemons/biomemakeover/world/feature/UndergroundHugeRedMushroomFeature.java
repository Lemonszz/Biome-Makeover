package party.lemons.biomemakeover.world.feature;

import com.mojang.serialization.Codec;
import java.util.Random;
import net.minecraft.block.BlockState;
import net.minecraft.block.MushroomBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.HugeMushroomFeatureConfig;
import party.lemons.biomemakeover.world.feature.UndergroundHugeMushroomFeature;

public class UndergroundHugeRedMushroomFeature extends UndergroundHugeMushroomFeature
{
	public UndergroundHugeRedMushroomFeature(Codec<HugeMushroomFeatureConfig> codec) {
		super(codec);
	}

	protected void generateCap(WorldAccess world, Random random, BlockPos start, int y, BlockPos.Mutable mutable, HugeMushroomFeatureConfig config) {
		for(int i = y - 3; i <= y; ++i) {
			int j = i < y ? config.foliageRadius : config.foliageRadius - 1;
			int k = config.foliageRadius - 2;

			for(int l = -j; l <= j; ++l) {
				for(int m = -j; m <= j; ++m) {
					boolean bl = l == -j;
					boolean bl2 = l == j;
					boolean bl3 = m == -j;
					boolean bl4 = m == j;
					boolean bl5 = bl || bl2;
					boolean bl6 = bl3 || bl4;
					if (i >= y || bl5 != bl6) {
						mutable.set(start, l, i, m);
						if (!world.getBlockState(mutable).isOpaqueFullCube(world, mutable)) {
							this.setBlockState(world, mutable, config.capProvider.getBlockState(random, start).with(MushroomBlock.UP, i >= y - 1).with(MushroomBlock.WEST, l < -k).with(MushroomBlock.EAST, l > k).with(MushroomBlock.NORTH, m < -k).with(MushroomBlock.SOUTH, m > k));
						}
					}
				}
			}
		}

	}

	protected int getCapSize(int i, int j, int capSize, int y) {
		int k = 0;
		if (y < j && y >= j - 3) {
			k = capSize;
		} else if (y == j) {
			k = capSize;
		}

		return k;
	}
}
