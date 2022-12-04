package party.lemons.biomemakeover.level.feature.foliage;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacerType;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import party.lemons.biomemakeover.init.BMFeatures;

import java.util.function.BiConsumer;

public class WillowFoliagePlacer extends FoliagePlacer
{
    protected final int height;
    protected final boolean doWillows;

    public WillowFoliagePlacer(IntProvider radius, IntProvider offset, int height, boolean doWillows)
    {
        super(radius, offset);
        this.height = height;
        this.doWillows = doWillows;
    }
    @Override
    protected FoliagePlacerType<?> type() {
        return BMFeatures.WILLOW_FOLIAGE.get();
    }

    @Override
    protected void createFoliage(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> biConsumer, RandomSource random, TreeConfiguration treeConfiguration, int trunkHeight, FoliageAttachment attachment, int foliageHeight, int radius, int offset) {
        BoundingBox box = new BoundingBox(attachment.pos());


        for(int placeOffset = offset; placeOffset >= offset - foliageHeight; --placeOffset)
        {
            int baseHeight;
            if(attachment.radiusOffset() > 0)
                baseHeight = Math.max(radius + attachment.radiusOffset() - 1 - placeOffset / 2, 0);
            else baseHeight = Math.max(radius + attachment.radiusOffset() - placeOffset / 2, 0);

            this.placeLeavesRow(level, biConsumer, random, treeConfiguration, attachment.pos(), baseHeight, placeOffset, attachment.doubleTrunk(), box);
        }
    }

    protected void placeLeavesRow(LevelSimulatedReader levelSimulatedReader, BiConsumer<BlockPos, BlockState> biConsumer, RandomSource random, TreeConfiguration treeConfiguration, BlockPos blockPos, int i, int j, boolean bl, BoundingBox box) {
        int k = bl ? 1 : 0;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        for (int l = -i; l <= i + k; ++l) {
            for (int m = -i; m <= i + k; ++m) {
                if (this.shouldSkipLocationSigned(random, l, j, m, i, bl)) continue;
                mutableBlockPos.setWithOffset(blockPos, l, j, m);
                FoliagePlacer.tryPlaceLeaf(levelSimulatedReader, biConsumer, random, treeConfiguration, mutableBlockPos);

                box.encapsulate(new BlockPos(mutableBlockPos));
            }
        }
    }

    @Override
    public int foliageHeight(RandomSource random, int i, TreeConfiguration treeConfiguration) {
        return this.height;
    }

    @Override
    protected boolean shouldSkipLocation(RandomSource random, int baseHeight, int dx, int dy, int dz, boolean giantTrunk) {
        if(baseHeight + dy >= 4)
        {
            return true;
        }else
        {
            return baseHeight * baseHeight + dy * dy > dz * dz;
        }
    }

    protected static <P extends WillowFoliagePlacer> Products.P4<RecordCodecBuilder.Mu<P>, IntProvider, IntProvider, Integer, Boolean> buildCodec(RecordCodecBuilder.Instance<P> instance)
    {
        return foliagePlacerParts(instance).and(Codec.intRange(0, 16).fieldOf("height").forGetter((cdc)->cdc.height)).and(Codec.BOOL.fieldOf("doWillows").forGetter((cdc)->cdc.doWillows));
    }

    public static final Codec<WillowFoliagePlacer> CODEC = RecordCodecBuilder.create((instance)->buildCodec(instance).apply(instance, WillowFoliagePlacer::new));
}
