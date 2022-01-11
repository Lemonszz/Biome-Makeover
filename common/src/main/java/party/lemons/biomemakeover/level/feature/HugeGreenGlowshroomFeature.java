package party.lemons.biomemakeover.level.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.HugeMushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.AbstractHugeMushroomFeature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.HugeMushroomFeatureConfiguration;

import java.util.Random;

public class HugeGreenGlowshroomFeature extends AbstractHugeMushroomFeature
{

    public HugeGreenGlowshroomFeature(Codec<HugeMushroomFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<HugeMushroomFeatureConfiguration> ctx)
    {
        int i = this.getTreeHeight(ctx.random());
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();
        if(!this.isValidPosition(ctx.level(), ctx.origin(), i, mutable, ctx.config()))
        {
            return false;
        }
        else
        {
            BlockPos capPos = this.generateCrookedStem(ctx.level(), ctx.random(), ctx.origin(), ctx.config(), i, mutable);
            this.makeCap(ctx.level(), ctx.random(), capPos, 0, mutable, ctx.config());
            return true;
        }
    }

    protected BlockPos generateCrookedStem(LevelAccessor world, Random random, BlockPos pos, HugeMushroomFeatureConfiguration config, int height, BlockPos.MutableBlockPos mutable)
    {
        Direction dir = Direction.getRandom(random);
        mutable.set(pos);

        for(int i = 0; i < height; ++i)
        {
            if(!world.getBlockState(mutable).isSolidRender(world, mutable))
            {
                this.setBlock(world, mutable, config.stemProvider.getState(random, pos));
            }
            if(random.nextInt(2) == 0)
            {
                mutable.move(dir);
                this.setBlock(world, mutable, config.stemProvider.getState(random, pos));
            }
            mutable.move(Direction.UP);
        }
        return new BlockPos(mutable);
    }

    @Override
    protected int getTreeHeight(Random random)
    {
        int i = random.nextInt(3) + 6;
        if(random.nextInt(12) == 0)
        {
            i *= 2;
        }

        return i;
    }

    @Override
    protected int getTreeRadiusForHeight(int i, int j, int k, int l) {
        return 1;
    }

    @Override
    protected void makeCap(LevelAccessor level, Random random, BlockPos start, int y, BlockPos.MutableBlockPos mutable, HugeMushroomFeatureConfiguration config) {
        for (int yy = y - 3; yy <= y; ++yy) {
            int size = yy < y ? config.foliageRadius : config.foliageRadius - 1;
            int k = config.foliageRadius - 2;

            for (int xx = -size; xx <= size; ++xx) {
                for (int zz = -size; zz <= size; ++zz) {
                    boolean isMinX = xx == -size;
                    boolean isMaxX = xx == size;
                    boolean isMinZ = zz == -size;
                    boolean isMaxZ = zz == size;
                    boolean isXCorner = isMinX || isMaxX;
                    boolean isZCorner = isMinZ || isMaxZ;
                    boolean isMiddle = xx > -size && xx < size && zz > -size && zz < size;

                    if (yy >= y || (isXCorner != isZCorner || (yy == y - 3) && !isMiddle)) {
                        mutable.setWithOffset(start, xx, yy, zz);
                        if (!level.getBlockState(mutable).isSolidRender(level, mutable)) {
                            BlockState ss = (config.capProvider.getState(random, start).setValue(HugeMushroomBlock.UP, true)).setValue(HugeMushroomBlock.WEST, xx < -k).setValue(HugeMushroomBlock.EAST, xx > k).setValue(HugeMushroomBlock.NORTH, zz < -k).setValue(HugeMushroomBlock.SOUTH, zz > k);

                            if (yy == y) {
                                ss = ss.setValue(HugeMushroomBlock.EAST, true).setValue(HugeMushroomBlock.DOWN, false).setValue(HugeMushroomBlock.WEST, true).setValue(HugeMushroomBlock.NORTH, true).setValue(HugeMushroomBlock.SOUTH, true);
                            }
                            this.setBlock(level, mutable, ss);
                        }
                    }
                }
            }
        }

        mutable.setWithOffset(start, 0, y, 0);

        BlockState st = config.capProvider.getState(random, start);
        set(level, mutable.above(), st);

        boolean top = true;
        int off = config.foliageRadius;
        for (int i = 0; i < 2; i++) {
            level.setBlock(mutable.west(off), config.capProvider.getState(random, start).setValue(HugeMushroomBlock.EAST, top), 3);
            level.setBlock(mutable.east(off), config.capProvider.getState(random, start).setValue(HugeMushroomBlock.WEST, top), 3);
            level.setBlock(mutable.north(off), config.capProvider.getState(random, start).setValue(HugeMushroomBlock.SOUTH, top), 3);
            level.setBlock(mutable.south(off), config.capProvider.getState(random, start).setValue(HugeMushroomBlock.NORTH, top), 3);

            top = false;
            mutable.setWithOffset(start, 0, y - 4, 0);
        }
    }

    private void set(LevelAccessor world, BlockPos pos, BlockState state)
    {
        if(world.isEmptyBlock(pos)) world.setBlock(pos, state, 3);
    }
}
