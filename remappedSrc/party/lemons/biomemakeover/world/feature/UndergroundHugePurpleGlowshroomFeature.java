package party.lemons.biomemakeover.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.MushroomBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.HugeMushroomFeature;
import net.minecraft.world.gen.feature.HugeMushroomFeatureConfig;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.Random;

public class UndergroundHugePurpleGlowshroomFeature extends HugeMushroomFeature
{
	public UndergroundHugePurpleGlowshroomFeature(Codec<HugeMushroomFeatureConfig> codec) {
		super(codec);
	}

	protected void generateCap(WorldAccess world, Random random, BlockPos start, int y, BlockPos.Mutable mutable, HugeMushroomFeatureConfig config) {
		int size = config.foliageRadius + RandomUtil.randomRange(1, 3);

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
								.with(MushroomBlock.DOWN, false)
								.with(MushroomBlock.SOUTH, isSouth);

						//if(isMiddle)
						//	st = st.with(MushroomBlock.DOWN, false);

						world.setBlockState(mutable, st, 3);

						if(isMiddle) world.setBlockState(mutable.up(),
								config.capProvider.getBlockState(random, start), 3);
						else world.setBlockState(mutable.down(),
								config.capProvider.getBlockState(random, start)
								.with(MushroomBlock.WEST, isWest)
								.with(MushroomBlock.EAST, isEast)
								.with(MushroomBlock.NORTH, isNorth)
								.with(MushroomBlock.SOUTH, isSouth), 3);
					}
				}
			}

		}
		mutable.set(start, 0, y - 2, 0);
		world.setBlockState(mutable.west(size), config.capProvider.getBlockState(random, start).with(MushroomBlock.EAST, false), 3);
		world.setBlockState(mutable.east(size), config.capProvider.getBlockState(random, start).with(MushroomBlock.WEST, false), 3);
		world.setBlockState(mutable.north(size), config.capProvider.getBlockState(random, start).with(MushroomBlock.SOUTH, false), 3);
		world.setBlockState(mutable.south(size), config.capProvider.getBlockState(random, start).with(MushroomBlock.NORTH, false), 3);
	}

	public boolean generate(StructureWorldAccess structureWorldAccess, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, HugeMushroomFeatureConfig hugeMushroomFeatureConfig) {
		int i = this.getHeight(random);
		BlockPos.Mutable mutable = new BlockPos.Mutable();
		if (!this.canGenerate(structureWorldAccess, blockPos, i, mutable, hugeMushroomFeatureConfig)) {
			return false;
		} else {
			BlockPos capPos = this.generateCrookedStem(structureWorldAccess, random, blockPos, hugeMushroomFeatureConfig, i, mutable);
			this.generateCap(structureWorldAccess, random, capPos, 0, mutable, hugeMushroomFeatureConfig);
			return true;
		}
	}

	protected BlockPos generateCrookedStem(WorldAccess world, Random random, BlockPos pos, HugeMushroomFeatureConfig config, int height, BlockPos.Mutable mutable) {
		Direction dir = Direction.random(random);
		mutable.set(pos);

		for(int i = 0; i < height; ++i) {
			if (!world.getBlockState(mutable).isOpaqueFullCube(world, mutable)) {
				this.setBlockState(world, mutable, config.stemProvider.getBlockState(random, pos));
			}
			if(random.nextInt(2) == 0)
			{
				mutable.move(dir);
				this.setBlockState(world, mutable, config.stemProvider.getBlockState(random, pos));
			}
			mutable.move(Direction.UP);
		}
		return new BlockPos(mutable);
	}

	protected int getHeight(Random random) {
		int i = random.nextInt(3) + 6;
		if (random.nextInt(12) == 0) {
			i *= 2;
		}

		return i;
	}

	protected int getCapSize(int i, int j, int capSize, int y) {
		return y <= 3 ? 0 : capSize;
	}
}
