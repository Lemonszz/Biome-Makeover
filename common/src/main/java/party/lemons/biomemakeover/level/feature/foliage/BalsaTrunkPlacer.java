package party.lemons.biomemakeover.level.feature.foliage;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import party.lemons.biomemakeover.init.BMWorldGen;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class BalsaTrunkPlacer extends TrunkPlacer
{
    public BalsaTrunkPlacer(int baseHeight, int firstRandomHeight, int secondRandomHeight)
    {
        super(baseHeight, firstRandomHeight, secondRandomHeight);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return BMWorldGen.MushroomFields.BLIGHTED_BALSA_TRUNK;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> biConsumer, RandomSource random, int trunkHeight, BlockPos pos, TreeConfiguration treeConfiguration) {
        setDirtAt(level, biConsumer, random, pos.below(), treeConfiguration);

        List<FoliagePlacer.FoliageAttachment> list = Lists.newArrayList();
        Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);

        int treeHeight = trunkHeight - random.nextInt(4) - 1;
        int j = 3 - random.nextInt(3);
        BlockPos.MutableBlockPos mpos = new BlockPos.MutableBlockPos();
        int xx = pos.getX();
        int zz = pos.getZ();
        int yy = 0;

        int yPosition;
        for(int n = 0; n < trunkHeight; ++n)
        {
            yPosition = pos.getY() + n;
            if(n >= treeHeight && j > 0)
            {
                xx += direction.getStepX();
                zz += direction.getStepZ();
                --j;
            }

            //Set Tree block?
            if(placeLog(level, biConsumer, random, mpos.set(xx, yPosition, zz), treeConfiguration))
            {
                yy = yPosition + 1;
            }
        }
        list.add(new FoliagePlacer.FoliageAttachment(new BlockPos(xx, yy, zz), 1, false));

        return list;
    }


    public static final Codec<BalsaTrunkPlacer> CODEC = RecordCodecBuilder.create((instance)->trunkPlacerParts(instance).apply(instance, BalsaTrunkPlacer::new));
}