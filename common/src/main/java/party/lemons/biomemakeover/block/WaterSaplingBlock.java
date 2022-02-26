package party.lemons.biomemakeover.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;
import party.lemons.taniwha.block.types.TSaplingBlock;

import java.util.Random;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class WaterSaplingBlock extends TSaplingBlock implements SimpleWaterloggedBlock {
    private final int maxDepth;

    public WaterSaplingBlock(AbstractTreeGrower abstractTreeGrower, int maxDepth, Properties settings)
    {
        super(abstractTreeGrower, settings);
        this.maxDepth = maxDepth;
    }

    public void advanceTree(ServerLevel serverLevel, BlockPos blockPos, BlockState blockState, Random random) {
        if(blockState.getValue(WATERLOGGED))
        {
            if(serverLevel.getFluidState(blockPos.above(maxDepth)).getType() == Fluids.WATER) return;
        }

        super.advanceTree(serverLevel, blockPos, blockState, random);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx)
    {
        BlockPos pos = ctx.getClickedPos();
        FluidState fluidState = ctx.getLevel().getFluidState(pos);

        return super.getStateForPlacement(ctx).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED);
    }
}
