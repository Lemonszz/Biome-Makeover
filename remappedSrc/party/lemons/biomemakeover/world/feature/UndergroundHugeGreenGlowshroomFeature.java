package party.lemons.biomemakeover.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.block.MushroomBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.HugeMushroomFeature;
import net.minecraft.world.gen.feature.HugeMushroomFeatureConfig;
import net.minecraft.world.gen.feature.HugeRedMushroomFeature;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.Random;

public class UndergroundHugeGreenGlowshroomFeature extends HugeMushroomFeature
{
	public UndergroundHugeGreenGlowshroomFeature(Codec<HugeMushroomFeatureConfig> codec) {
		super(codec);
	}

	protected void generateCap(WorldAccess world, Random random, BlockPos start, int y, BlockPos.Mutable mutable, HugeMushroomFeatureConfig config) {
		for(int yy = y - 3; yy <= y; ++yy) {
			int size = yy < y ? config.foliageRadius : config.foliageRadius - 1;
			int k = config.foliageRadius - 2;

			for(int xx = -size; xx <= size; ++xx) {
				for(int zz = -size; zz <= size; ++zz) {
					boolean isTop = size == config.foliageRadius -1;
					boolean isMinX = xx == -size;
					boolean isMaxX = xx == size;
					boolean isMinZ = zz == -size;
					boolean isMaxZ = zz == size;
					boolean isXCorner = isMinX || isMaxX;
					boolean isZCorner = isMinZ || isMaxZ;
					boolean isMiddle = xx > -size && xx < size && zz > -size && zz < size;

					if (yy >= y || (isXCorner != isZCorner || (yy == y - 3) && !isMiddle)) {
						mutable.set(start, xx, yy, zz);
						if (!world.getBlockState(mutable).isOpaqueFullCube(world, mutable)) {
							BlockState ss = (config.capProvider.getBlockState(random, start)
									.with(MushroomBlock.UP, true))
									.with(MushroomBlock.WEST, xx < -k)
									.with(MushroomBlock.EAST, xx > k)
									.with(MushroomBlock.NORTH, zz < -k)
									.with(MushroomBlock.SOUTH, zz > k);

							if(yy == y) {
								ss = ss.with(MushroomBlock.EAST, true).with(MushroomBlock.DOWN, false).with(MushroomBlock.WEST, true).with(MushroomBlock.NORTH, true).with(MushroomBlock.SOUTH, true);
							}
							this.setBlockState(world, mutable, ss);
						}
					}
				}
			}
		}

		mutable.set(start, 0, y, 0);

		BlockState st = config.capProvider.getBlockState(random, start);
		set(world, mutable.up(), st);

		boolean top = true;
		int off = config.foliageRadius;
		for(int i = 0; i < 2; i++)
		{
			world.setBlockState(mutable.west(off), config.capProvider.getBlockState(random, start).with(MushroomBlock.EAST, top), 3);
			world.setBlockState(mutable.east(off), config.capProvider.getBlockState(random, start).with(MushroomBlock.WEST, top), 3);
			world.setBlockState(mutable.north(off), config.capProvider.getBlockState(random, start).with(MushroomBlock.SOUTH, top), 3);
			world.setBlockState(mutable.south(off), config.capProvider.getBlockState(random, start).with(MushroomBlock.NORTH, top), 3);

			top = false;
			mutable.set(start, 0, y - 4, 0);
		}

	}

	private void set(WorldAccess world, BlockPos pos, BlockState state)
	{
		if(world.isAir(pos))
			world.setBlockState(pos, state, 3);
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
		int k = 0;
		if (y < j && y >= j - 3) {
			k = capSize;
		} else if (y == j) {
			k = capSize;
		}

		return k;
	}
}
