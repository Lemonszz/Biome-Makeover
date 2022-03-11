package party.lemons.biomemakeover.level.feature.foliage;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import party.lemons.biomemakeover.init.BMFeatures;
import party.lemons.biomemakeover.init.BMWorldGen;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class HangingLeavesDecorator extends TreeDecorator
{
    public static final Codec<HangingLeavesDecorator> CODEC =BlockStateProvider.CODEC.fieldOf("provider").xmap(HangingLeavesDecorator::new, d -> d.provider).codec();

    public final BlockStateProvider provider;

    public HangingLeavesDecorator(BlockStateProvider blockStateProvider)
    {
        this.provider = blockStateProvider;
    }

    @Override
    protected TreeDecoratorType<?> type() {
        return BMFeatures.HANGING_LEAVES_DECORATOR.get();
    }

    @Override
    public void place(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> biConsumer, Random random, List<BlockPos> logs, List<BlockPos> leaves)
    {
        for(int i = 0; i < 4 + random.nextInt(8); i++)
        {
            BlockPos.MutableBlockPos downPos = leaves.get(random.nextInt(leaves.size())).below().mutable();
            if(TreeFeature.isAirOrLeaves(level, downPos) && level.isStateAtPosition(downPos.above(), (state)->state.is(BlockTags.LEAVES)))
            {
                biConsumer.accept(downPos, provider.getState(random, downPos));
            }
        }
    }
}
