package party.lemons.biomemakeover.level.generate.foliage;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import party.lemons.biomemakeover.init.BMWorldGen;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class WillowTrunkPlacer extends TrunkPlacer {
    private static final Direction[] dirs = new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST,};

    public WillowTrunkPlacer(int base, int first, int second) {
        super(base, first, second);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return BMWorldGen.Swamp.WILLOW_TRUNK;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> biConsumer, Random random, int trunkHeight, BlockPos pos, TreeConfiguration cfg) {
        //Change ground under to dirt
        setDirtAt(level, biConsumer, random, pos.below(), cfg);

        List<FoliagePlacer.FoliageAttachment> nodes = Lists.newArrayList();
        for(int i = 0; i < trunkHeight; ++i)
        {
            //Place trunk
            placeLog(level, biConsumer, random, pos.above(i), cfg);
        }
        nodes.add(new FoliagePlacer.FoliageAttachment(pos.above(trunkHeight), 1, false));

        BlockPos start = pos.above(RandomUtil.randomRange(trunkHeight / 2, Math.min(trunkHeight, (trunkHeight / 2) + 2)));
        for(Direction dir : dirs)
        {
            int length = 1 + random.nextInt(4);
            BlockPos.MutableBlockPos lP = new BlockPos.MutableBlockPos(start.relative(dir).getX(), start.getY(), start.relative(dir).getZ());

            for(int i = 0; i < length; i++)
            {
                BlockState st = cfg.trunkProvider.getState(random, lP).setValue(RotatedPillarBlock.AXIS, dir.getAxis());
                placeBlock(st, level, biConsumer, lP, Function.identity());

                if(i != length - 1)
                {
                    nodes.add(new FoliagePlacer.FoliageAttachment(lP, -1, false));
                    lP.move(dir);
                    if(random.nextBoolean())
                        lP.move(random.nextBoolean() ? dir.getClockWise() : dir.getCounterClockWise());

                    if(random.nextInt(2) == 0) lP.move(Direction.UP);
                }else nodes.add(new FoliagePlacer.FoliageAttachment(lP, 0, false));

            }
        }
        return nodes;
    }

    protected static boolean placeBlock(BlockState state, LevelSimulatedReader levelSimulatedReader, BiConsumer<BlockPos, BlockState> biConsumer, BlockPos blockPos, Function<BlockState, BlockState> function) {
        if (TreeFeature.validTreePos(levelSimulatedReader, blockPos)) {
            biConsumer.accept(blockPos, function.apply(state));
            return true;
        }
        return false;
    }



    public static final Codec<WillowTrunkPlacer> CODEC = RecordCodecBuilder.create((instance)->trunkPlacerParts(instance).apply(instance, WillowTrunkPlacer::new));

}