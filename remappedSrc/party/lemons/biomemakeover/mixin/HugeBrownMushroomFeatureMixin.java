package party.lemons.biomemakeover.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.MushroomBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.HugeBrownMushroomFeature;
import net.minecraft.world.gen.feature.HugeMushroomFeatureConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.Random;

@Mixin(HugeBrownMushroomFeature.class)
public class HugeBrownMushroomFeatureMixin
{
	@Inject(at = @At("HEAD"), method = "generateCap", cancellable = true)
	public void generateCap(WorldAccess world, Random random, BlockPos start, int y, BlockPos.Mutable mutable, HugeMushroomFeatureConfig config, CallbackInfo cbi)
	{
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
							else world.setBlockState(mutable.down(), config.capProvider.getBlockState(random, start)
									.with(MushroomBlock.WEST, isWest)
									.with(MushroomBlock.EAST, isEast)
									.with(MushroomBlock.NORTH, isNorth)
									.with(MushroomBlock.SOUTH, isSouth), 3);
						}
					}
				}
			}

		}
		cbi.cancel();
	}
}
