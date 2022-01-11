package party.lemons.biomemakeover.level.feature.foliage;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import party.lemons.biomemakeover.init.BMWorldGen;
import party.lemons.biomemakeover.util.HorizontalDirection;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class AncientOakTrunkPlacer extends TrunkPlacer
{
    public AncientOakTrunkPlacer(int i, int j, int k)
    {
        super(i, j, k);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return BMWorldGen.DarkForest.ANCIENT_OAK_TRUNK;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> biConsumer, Random random, int trunkHeight, BlockPos pos, TreeConfiguration treeConfiguration) {
        List<FoliagePlacer.FoliageAttachment> list = Lists.newArrayList();
        BlockPos blockPos = pos.below();
        setDirtAt(level, biConsumer, random, blockPos, treeConfiguration);
        setDirtAt(level, biConsumer, random, blockPos.east(), treeConfiguration);
        setDirtAt(level, biConsumer, random, blockPos.south(), treeConfiguration);
        setDirtAt(level, biConsumer, random, blockPos.south().east(), treeConfiguration);
        int knobSpot = Math.max(5, (trunkHeight / 2) - random.nextInt(4));
        //	int knobCount = 2 - random.nextInt(3);
        int knobCount = 1;
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();
        int foliageX = x;
        int foliageZ = z;
        int foliageY = y + trunkHeight - 2;

        for(int genY = 0; genY < trunkHeight; ++genY)
        {
            int placeY = y + genY;
            BlockPos placePos = new BlockPos(foliageX, placeY, foliageZ);
            if(TreeFeature.isAirOrLeaves(level, placePos))
            {

                placeLog(level, biConsumer, random, placePos, treeConfiguration);
                placeLog(level, biConsumer, random, placePos.east(), treeConfiguration);
                placeLog(level, biConsumer, random, placePos.south(), treeConfiguration);
                placeLog(level, biConsumer, random, placePos.east().south(), treeConfiguration);
            }
        }

        list.add(new FoliagePlacer.FoliageAttachment(new BlockPos(foliageX, foliageY + 3, foliageZ), 2, true));

        for(int offsetX = -1; offsetX <= 2; ++offsetX)
        {
            for(int offsetZ = -1; offsetZ <= 2; ++offsetZ)
            {
                if((offsetX < 0 || offsetX > 1 || offsetZ < 0 || offsetZ > 1) && random.nextInt(3) <= 0)
                {
                    int u = random.nextInt(3) + 2;

                    for(int v = 0; v < u; ++v)
                    {
                        placeLog(level, biConsumer, random, new BlockPos(x + offsetX, foliageY - v - 1, z + offsetZ), treeConfiguration);
                    }

                    list.add(new FoliagePlacer.FoliageAttachment(new BlockPos(foliageX + offsetX, foliageY, foliageZ + offsetZ), 0, false));
                }
            }
        }

        List<HorizontalDirection> directions = Lists.newArrayList();
        for(int i = 0; i < 1 + random.nextInt(4); i++)
        {
            makeBranch(level,biConsumer, random, list, directions, foliageX, y, foliageZ, RandomUtil.randomRange(trunkHeight - 13, trunkHeight - 7), trunkHeight, treeConfiguration);
        }

        return list;
    }

    public void makeBranch(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> biConsumer, Random random, List<FoliagePlacer.FoliageAttachment> foliage, List<HorizontalDirection> directions, int foliageX, int genY, int foliageZ, int branchY, int trunkHeight, TreeConfiguration config)
    {
        HorizontalDirection lastDirection = null;
        for(int i = 0; i < 2; i++)
        {
            HorizontalDirection direction;
            if(lastDirection == null)
            {
                direction = HorizontalDirection.random(random);
                while(directions.contains(direction)) direction = HorizontalDirection.random(random);

                directions.add(direction);
                directions.add(direction.opposite());
                lastDirection = direction;
            }else
            {
                direction = lastDirection.opposite();
                lastDirection = null;
            }
            int genX = foliageX + direction.x;
            int genZ = foliageZ + direction.z;
            int yy;
            int offsetCount = 0;
            for(yy = branchY; yy < trunkHeight - 5 + random.nextInt(4); ++yy)
            {
                int placeGenY = genY + yy;
                BlockPos placePos2 = new BlockPos(genX, placeGenY, genZ);
                if(TreeFeature.isAirOrLeaves(level, placePos2))
                {
                    placeLog(level, biConsumer, random, placePos2, config);
                    placeLog(level, biConsumer, random, placePos2.east(), config);
                    placeLog(level, biConsumer, random, placePos2.south(), config);
                    placeLog(level, biConsumer, random, placePos2.east().south(), config);
                }

                if(random.nextBoolean() && offsetCount <= 5)
                {
                    offsetCount++;
                    if(direction.isStraight)
                    {
                        genX += direction.x;
                        genZ += direction.z;
                    }else
                    {
                        if(yy % 2 == 0)
                        {
                            genX += direction.x;
                        }else
                        {
                            genZ += direction.z;
                        }
                    }
                }
            }
            foliage.add(new FoliagePlacer.FoliageAttachment(new BlockPos(genX, genY + yy + 1, genZ), 1, false));
        }
    }

    public static final Codec<AncientOakTrunkPlacer> CODEC = RecordCodecBuilder.create((instance)->trunkPlacerParts(instance).apply(instance, AncientOakTrunkPlacer::new));

}
