package party.lemons.biomemakeover.level.feature.foliage;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.feature.treedecorators.LeaveVineDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;
import party.lemons.biomemakeover.block.WillowingBranchesBlock;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMWorldGen;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.List;
import java.util.Random;
import java.util.function.BiConsumer;

public class WillowingBranchDecorator extends TreeDecorator {
    public static final WillowingBranchDecorator INSTANCE = new WillowingBranchDecorator();
    public static final Codec<WillowingBranchDecorator> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    protected TreeDecoratorType<?> type() {
        return BMWorldGen.Swamp.WILLOWING_BRANCH_DECORATOR;
    }

    @Override
    public void place(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> biConsumer, Random random, List<BlockPos> list, List<BlockPos> leaves) {

        for(int i = 0; i < 10; i++)
        {
            BlockPos.MutableBlockPos pos = leaves.get(random.nextInt(leaves.size())).below().mutable();
            for(int j = 0; j < 3; j++)
            {
                if((level.isStateAtPosition(pos, BlockBehaviour.BlockStateBase::isAir) || level.isStateAtPosition(pos, (s)->s == Blocks.WATER.defaultBlockState())) && level.isStateAtPosition(pos.above(), (s)->s.is(BlockTags.LEAVES) || (s.is(BMBlocks.WILLOWING_BRANCHES) && s.getValue(WillowingBranchesBlock.STAGE) < WillowingBranchesBlock.MAX_STAGE)))
                {
                    boolean water = level.isStateAtPosition(pos, (s)->s == Blocks.WATER.defaultBlockState());
                    if(water || level.isStateAtPosition(pos, BlockBehaviour.BlockStateBase::isAir))
                    {
                        biConsumer.accept(pos, BMBlocks.WILLOWING_BRANCHES.defaultBlockState().setValue(WillowingBranchesBlock.STAGE, j).setValue(BlockStateProperties.WATERLOGGED, water));
                        pos.move(Direction.DOWN);
                    }else break;
                }else
                {
                    break;
                }
            }
        }
    }
}
