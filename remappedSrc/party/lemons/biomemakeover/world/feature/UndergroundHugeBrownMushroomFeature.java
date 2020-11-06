package party.lemons.biomemakeover.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.MushroomBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.HugeMushroomFeature;
import net.minecraft.world.gen.feature.HugeMushroomFeatureConfig;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.Random;

public class UndergroundHugeBrownMushroomFeature extends HugeMushroomFeature
{
	public UndergroundHugeBrownMushroomFeature(Codec<HugeMushroomFeatureConfig> codec) {
		super(codec);
	}

	protected void generateCap(WorldAccess world, Random random, BlockPos start, int y, BlockPos.Mutable mutable, HugeMushroomFeatureConfig config) {
		int size = config.foliageRadius + RandomUtil.randomRange(-1, 2);
		boolean isFlat = random.nextInt(6) == 0;

		for(int xx = -size; xx <= size; ++xx)
		{
			for(int zz = -size; zz <= size; ++zz)
			{
				boolean isMinX = xx == -size;
				boolean isMaxX = xx == size;
				boolean isMinZ = zz == -size;
				boolean isMaxZ = zz == size;
				boolean isXCorner = isMinX || isMaxX;
				boolean isZCorner = isMinZ || isMaxZ;
				if (!isXCorner || !isZCorner) {
					mutable.set(start, xx, y, zz);
					if (!world.getBlockState(mutable).isOpaqueFullCube(world, mutable)) {
						boolean isWest = isMinX || isZCorner && xx == 1 - size;
						boolean isEast = isMaxX || isZCorner && xx == size - 1;
						boolean isNorth = isMinZ || isXCorner && zz == 1 - size;
						boolean isSouth = isMaxZ || isXCorner && zz == size - 1;
						boolean isMiddle = xx > -size && xx < size && zz > -size && zz < size;

						BlockState st = config.capProvider.getBlockState(random, start)
								.with(MushroomBlock.WEST, isWest)
								.with(MushroomBlock.EAST, isEast)
								.with(MushroomBlock.NORTH, isNorth)
								.with(MushroomBlock.SOUTH, isSouth)
								.with(MushroomBlock.UP, !isMiddle || isFlat);

						world.setBlockState(mutable, st, 3);

						if(!isFlat)
						{
							if(isMiddle) world.setBlockState(mutable.up(), config.capProvider.getBlockState(random, start), 3);
							else world.setBlockState(mutable.down(), config.capProvider.getBlockState(random, start), 3);
						}
					}
				}
			}

		}
	}

	protected int getCapSize(int i, int j, int capSize, int y) {
		return y <= 3 ? 0 : capSize;
	}
}
