package party.lemons.biomemakeover.level.feature.foliage;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import party.lemons.biomemakeover.block.IvyBlock;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMFeatures;
import party.lemons.biomemakeover.init.BMWorldGen;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class IvyDecorator extends TreeDecorator
{
    public static final IvyDecorator INSTANCE = new IvyDecorator();
    public static final Codec<IvyDecorator> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    protected TreeDecoratorType<?> type() {
        return BMFeatures.IVY_DECORATOR.get();
    }

    @Override
    public void place(Context context)
    {
        RandomSource random = context.random();
        context.logs().forEach((pos) ->
        {
            BlockPos placePos;
            if (random.nextInt(8) == 0) {
                placePos = pos.west();
                if (context.isAir(placePos)) {
                    this.placeIvy(placePos, Direction.EAST, context);
                }
            }

            if (random.nextInt(8) == 0) {
                placePos = pos.east();
                if (context.isAir(placePos)) {
                    this.placeIvy(placePos, Direction.WEST, context);
                }
            }

            if (random.nextInt(8) == 0) {
                placePos = pos.north();
                if (context.isAir(placePos)) {
                    this.placeIvy(placePos, Direction.SOUTH, context);
                }
            }

            if (random.nextInt(8) == 0) {
                placePos = pos.south();
                if (context.isAir(placePos)) {
                    this.placeIvy(placePos, Direction.NORTH, context);
                }
            }

        });
    }

    protected void placeIvy(BlockPos pos, Direction dir,  Context context)
    {
        context.setBlock(pos, BMBlocks.IVY.get().defaultBlockState().setValue(IvyBlock.getPropertyForDirection(dir), true));
    }
}
