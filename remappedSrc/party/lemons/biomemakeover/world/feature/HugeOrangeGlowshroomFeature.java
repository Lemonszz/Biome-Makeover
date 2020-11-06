package party.lemons.biomemakeover.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.MushroomBlock;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.HugeMushroomFeature;
import net.minecraft.world.gen.feature.HugeMushroomFeatureConfig;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.Random;

public class HugeOrangeGlowshroomFeature extends HugeMushroomFeature
{
	public HugeOrangeGlowshroomFeature(Codec<HugeMushroomFeatureConfig> codec) {
		super(codec);
	}

	protected void generateCap(WorldAccess world, Random random, BlockPos start, int y, BlockPos.Mutable mutable, HugeMushroomFeatureConfig config) {
		int size = config.foliageRadius;

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

						world.setBlockState(mutable, st, 3);

						if(isMiddle) world.setBlockState(mutable.up(), config.capProvider.getBlockState(random, start), 3);
						else world.setBlockState(mutable.down(), config.capProvider.getBlockState(random, start).with(MushroomBlock.DOWN, false), 3);
					}
				}
			}

		}
		BlockState st = config.capProvider.getBlockState(random, start);
		mutable.set(start, 0, y - 1, 0);
		for(int i = 0; i < 2; i++)
		{
			set(world, mutable.west(2), st.with(MushroomBlock.EAST, false));
			set(world, mutable.east(2), st.with(MushroomBlock.WEST, false));
			set(world, mutable.north(2), st.with(MushroomBlock.SOUTH, false));
			set(world, mutable.south(2), st.with(MushroomBlock.NORTH, false));

			mutable.set(start, 0, y - 2, 0);
		}

	}

	private void set(WorldAccess world, BlockPos pos, BlockState state)
	{
		if(world.isAir(pos) || world.getFluidState(pos).isIn(FluidTags.WATER))
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
		int i = random.nextInt(3) + 3;
		if (random.nextInt(12) == 0) {
			i *= 2;
		}

		return i;
	}

	protected boolean canGenerate(WorldAccess world, BlockPos pos, int height, BlockPos.Mutable mutable, HugeMushroomFeatureConfig config) {
		int i = pos.getY();
		if (i >= 1 && i + height + 1 < 256) {
			for(int j = 0; j <= height; ++j) {
				int k = this.getCapSize(-1, -1, config.foliageRadius, j);

				for(int l = -k; l <= k; ++l) {
					for(int m = -k; m <= k; ++m) {
						BlockState blockState = world.getBlockState(mutable.set(pos, l, j, m));
						if (!blockState.isAir() && !world.getFluidState(mutable).isIn(FluidTags.WATER) && !blockState.isIn(BlockTags.LEAVES)) {
							return false;
						}
					}
				}
			}

			return true;
		}
		return false;

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
