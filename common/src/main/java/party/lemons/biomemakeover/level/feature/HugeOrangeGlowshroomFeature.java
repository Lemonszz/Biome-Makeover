package party.lemons.biomemakeover.level.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.AbstractHugeMushroomFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.Random;

public class HugeOrangeGlowshroomFeature extends AbstractHugeMushroomFeature {

    public HugeOrangeGlowshroomFeature(Codec<HugeMushroomFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<HugeMushroomFeatureConfiguration> ctx) {
        int i = this.getTreeHeight(ctx.random());
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        if (!this.isValidPosition(ctx.level(), ctx.origin(), i, mutable, ctx.config())) {
            return false;
        } else {
            BlockPos capPos = this.generateCrookedStem(ctx.level(), ctx.random(), ctx.origin(), ctx.config(), i, mutable);
            this.makeCap(ctx.level(), ctx.random(), capPos, 0, mutable, ctx.config());
            return true;
        }
    }

    protected BlockPos generateCrookedStem(LevelAccessor world, Random random, BlockPos pos, HugeMushroomFeatureConfiguration config, int height, BlockPos.MutableBlockPos mutable) {
        Direction dir = Direction.getRandom(random);
        mutable.set(pos);

        for (int i = 0; i < height; ++i) {
            if (!world.getBlockState(mutable).isSolidRender(world, mutable)) {
                this.setBlock(world, mutable, config.stemProvider.getState(random, pos));
            }
            if (random.nextInt(2) == 0) {
                mutable.move(dir);
                this.setBlock(world, mutable, config.stemProvider.getState(random, pos));
            }
            mutable.move(Direction.UP);
        }
        return new BlockPos(mutable);
    }

    @Override
    protected int getTreeHeight(Random random) {
        int i = random.nextInt(3) + 6;
        if (random.nextInt(12) == 0) {
            i *= 2;
        }

        return i;
    }

    @Override
    protected int getTreeRadiusForHeight(int i, int j, int k, int l) {
        return l < 3 ? 0 : 2;
    }

    @Override
    protected void makeCap(LevelAccessor level, Random random, BlockPos start, int y, BlockPos.MutableBlockPos mutable, HugeMushroomFeatureConfiguration config) {
        int size = config.foliageRadius + RandomUtil.randomRange(1, 3);

        for (int xx = -size; xx <= size; ++xx) {
            for (int zz = -size; zz <= size; ++zz) {
                boolean isMinX = xx == -size;
                boolean isMaxX = xx == size;
                boolean isMinZ = zz == -size;
                boolean isMaxZ = zz == size;
                boolean isXCorner = isMinX || isMaxX;
                boolean isZCorner = isMinZ || isMaxZ;
                if (!isXCorner || !isZCorner) {
                    mutable.setWithOffset(start, xx, y, zz);
                    if (!level.getBlockState(mutable).isSolidRender(level, mutable)) {
                        boolean isWest = isMinX || isZCorner && xx == 1 - size;
                        boolean isEast = isMaxX || isZCorner && xx == size - 1;
                        boolean isNorth = isMinZ || isXCorner && zz == 1 - size;
                        boolean isSouth = isMaxZ || isXCorner && zz == size - 1;
                        boolean isMiddle = xx > -size && xx < size && zz > -size && zz < size;

                        BlockState st = config.capProvider.getState(random, start).setValue(HugeMushroomBlock.WEST, isWest).setValue(HugeMushroomBlock.EAST, isEast).setValue(HugeMushroomBlock.NORTH, isNorth).setValue(HugeMushroomBlock.DOWN, false).setValue(HugeMushroomBlock.SOUTH, isSouth);

                        level.setBlock(mutable, st, 3);

                        if (isMiddle)
                            level.setBlock(mutable.above(), config.capProvider.getState(random, start), 3);
                        else
                            level.setBlock(mutable.below(), config.capProvider.getState(random, start).setValue(HugeMushroomBlock.WEST, isWest).setValue(HugeMushroomBlock.EAST, isEast).setValue(HugeMushroomBlock.NORTH, isNorth).setValue(HugeMushroomBlock.SOUTH, isSouth), 3);
                    }
                }
            }

        }
        mutable.setWithOffset(start, 0, y - 2, 0);
        level.setBlock(mutable.west(size), config.capProvider.getState(random, start).setValue(HugeMushroomBlock.EAST, false), 3);
        level.setBlock(mutable.east(size), config.capProvider.getState(random, start).setValue(HugeMushroomBlock.WEST, false), 3);
        level.setBlock(mutable.north(size), config.capProvider.getState(random, start).setValue(HugeMushroomBlock.SOUTH, false), 3);
        level.setBlock(mutable.south(size), config.capProvider.getState(random, start).setValue(HugeMushroomBlock.NORTH, false), 3);
    }

    private void set(LevelAccessor world, BlockPos pos, BlockState state) {
        if (world.isEmptyBlock(pos) || world.getFluidState(pos).is(FluidTags.WATER)) world.setBlock(pos, state, 3);
    }

    protected boolean isValidPosition(LevelAccessor levelAccessor, BlockPos blockPos, int i, BlockPos.MutableBlockPos mutableBlockPos, HugeMushroomFeatureConfiguration hugeMushroomFeatureConfiguration) {
        int j = blockPos.getY();
        if (j < levelAccessor.getMinBuildHeight() + 1 || j + i + 1 >= levelAccessor.getMaxBuildHeight()) {
            return false;
        }
        BlockState blockState = levelAccessor.getBlockState(blockPos.below());
        if (!AbstractHugeMushroomFeature.isDirt(blockState) && !blockState.is(BlockTags.MUSHROOM_GROW_BLOCK)) {
            return false;
        }
        for (int k = 0; k <= i; ++k) {
            int l = this.getTreeRadiusForHeight(-1, -1, hugeMushroomFeatureConfiguration.foliageRadius, k);
            for (int m = -l; m <= l; ++m) {
                for (int n = -l; n <= l; ++n) {
                    BlockState blockState2 = levelAccessor.getBlockState(mutableBlockPos.setWithOffset(blockPos, m, k, n));
                    if(!blockState2.isAir() && !levelAccessor.getFluidState(mutableBlockPos).is(FluidTags.WATER) && !blockState2.is(BlockTags.LEAVES))
                        return false;
                }
            }
        }
        return true;
    }
}
