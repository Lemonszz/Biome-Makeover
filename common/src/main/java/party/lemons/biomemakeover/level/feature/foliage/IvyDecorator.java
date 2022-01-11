package party.lemons.biomemakeover.level.feature.foliage;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import party.lemons.biomemakeover.block.IvyBlock;
import party.lemons.biomemakeover.init.BMBlocks;
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
        return BMWorldGen.DarkForest.IVY_DECORATOR;
    }

    @Override
    public void place(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> biConsumer, Random random, List<BlockPos> logPositions, List<BlockPos> leafPositions) {
        logPositions.forEach((pos) ->
        {
            BlockPos placePos;
            if (random.nextInt(8) == 0) {
                placePos = pos.west();
                if (Feature.isAir(level, placePos)) {
                    this.placeIvy(level, placePos, Direction.EAST, biConsumer);
                }
            }

            if (random.nextInt(8) == 0) {
                placePos = pos.east();
                if (Feature.isAir(level, placePos)) {
                    this.placeIvy(level, placePos, Direction.WEST, biConsumer);
                }
            }

            if (random.nextInt(8) == 0) {
                placePos = pos.north();
                if (Feature.isAir(level, placePos)) {
                    this.placeIvy(level, placePos, Direction.SOUTH, biConsumer);
                }
            }

            if (random.nextInt(8) == 0) {
                placePos = pos.south();
                if (Feature.isAir(level, placePos)) {
                    this.placeIvy(level, placePos, Direction.NORTH, biConsumer);
                }
            }

        });
    }

    protected void placeIvy(LevelSimulatedReader level, BlockPos pos, Direction dir, BiConsumer<BlockPos, BlockState> biConsumer)
    {
        biConsumer.accept(pos, BMBlocks.IVY.defaultBlockState().setValue(IvyBlock.getPropertyForDirection(dir), true));
    }
}
