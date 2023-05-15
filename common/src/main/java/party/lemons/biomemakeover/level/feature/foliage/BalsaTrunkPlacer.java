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
import party.lemons.biomemakeover.init.BMFeatures;

import java.util.List;
import java.util.function.BiConsumer;

public class BalsaTrunkPlacer extends TrunkPlacer
{
    public BalsaTrunkPlacer(int baseHeight, int firstRandomHeight, int secondRandomHeight)
    {
        super(baseHeight, firstRandomHeight, secondRandomHeight);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return BMFeatures.BLIGHTED_BALSA_TRUNK.get();
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

        int possibleBranches = random.nextInt(2);

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
            else if(yPosition > pos.getY() + 4 && possibleBranches > 0 && random.nextInt(4) == 0)
            {
                BlockPos.MutableBlockPos branchPos = new BlockPos.MutableBlockPos(xx, yPosition, zz);
                possibleBranches--;
                Direction branchDir;
                do{
                    branchDir = Direction.Plane.HORIZONTAL.getRandomDirection(random);
                }while(branchDir == direction);

                int branchLength = random.nextInt(2, 4);
                for(int b = 0; b < branchLength; b++)
                {
                    branchPos.move(branchDir);
                    placeLog(level, biConsumer, random, branchPos, treeConfiguration);
                }
                list.add(new FoliagePlacer.FoliageAttachment(branchPos.move(Direction.UP).immutable(), 1, false));
            }

            //Set Tree block?
            if(placeLog(level, biConsumer, random, mpos.set(xx, yPosition, zz), treeConfiguration))
            {
                yy = yPosition + 1;
            }
        }
        list.add(new FoliagePlacer.FoliageAttachment(new BlockPos(xx, yy, zz), 2, false));

        return list;
    }


    public static final Codec<BalsaTrunkPlacer> CODEC = RecordCodecBuilder.create((instance)->trunkPlacerParts(instance).apply(instance, BalsaTrunkPlacer::new));
}