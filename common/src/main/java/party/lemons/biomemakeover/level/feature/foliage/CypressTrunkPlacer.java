package party.lemons.biomemakeover.level.feature.foliage;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import party.lemons.biomemakeover.init.BMWorldGen;
import party.lemons.taniwha.util.HorizontalDirection;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class CypressTrunkPlacer  extends TrunkPlacer {

    public CypressTrunkPlacer(int baseHeight, int firstRandomHeight, int secondRandomHeight)
    {
        super(baseHeight, firstRandomHeight, secondRandomHeight);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return BMWorldGen.Swamp.CYPRESS_TRUNK;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> biConsumer, Random random, int trunkHeight, BlockPos pos, TreeConfiguration treeConfiguration) {
        setDirtAt(level, biConsumer, random, pos.below(), treeConfiguration);
        List<FoliagePlacer.FoliageAttachment> list = Lists.newArrayList();

        for(int i = 0; i < trunkHeight; ++i)
        {
            //Place trunk
            placeLog(level, biConsumer, random, pos.above(i), treeConfiguration);
        }
        list.add(new FoliagePlacer.FoliageAttachment(pos.above(trunkHeight), 1, true));

        for(int i = 0; i < 2 + random.nextInt(6); i++)
        {
            HorizontalDirection dir = HorizontalDirection.random(random);
            BlockPos offsetStart = dir.offset(pos);
            for(int j = 0; j < 1 + random.nextInt(8); j++)
            {
                placeLog(level, biConsumer, random, offsetStart.above(j), treeConfiguration);
            }
        }

        for(int i = 0; i < 1 + random.nextInt(4); i++)
        {
            int offset = 7 + random.nextInt(Math.max(1, trunkHeight - 4));
            Direction dir = Direction.getRandom(random);
            BlockPos trunkPos = pos.relative(dir).above(offset);

            setBranch(level,biConsumer, treeConfiguration, random, trunkPos);
            list.add(new FoliagePlacer.FoliageAttachment(trunkPos.relative(dir), 1, false));

        }

        return list;
    }

    protected static boolean setBranch(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> biConsumer, TreeConfiguration treeConfiguration, Random random, BlockPos blockPos)
    {
        if(TreeFeature.isAirOrLeaves(level, blockPos))
        {
            placeLog(level, biConsumer, random, blockPos, treeConfiguration);
            return true;
        }else
        {
            return false;
        }
    }

    public static final Codec<CypressTrunkPlacer> CODEC = RecordCodecBuilder.create((instance)->trunkPlacerParts(instance).apply(instance, CypressTrunkPlacer::new));

}
