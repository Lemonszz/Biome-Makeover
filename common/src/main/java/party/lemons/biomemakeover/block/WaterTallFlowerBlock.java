package party.lemons.biomemakeover.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

public class WaterTallFlowerBlock extends BMTallFlowerBlock implements LiquidBlockContainer {
    public WaterTallFlowerBlock(Properties properties) {
        super(properties);

        registerDefaultState(getStateDefinition().any().setValue(HALF, DoubleBlockHalf.LOWER));
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        if(blockState.getValue(HALF) == DoubleBlockHalf.LOWER)
            return super.canSurvive(blockState, levelReader, blockPos) && levelReader.getFluidState(blockPos).getType() == Fluids.WATER;

        return super.canSurvive(blockState, levelReader, blockPos);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos pos = ctx.getClickedPos();

        return pos.getY() < ctx.getLevel().getMaxBuildHeight() - 1 && (ctx.getLevel().getBlockState(pos.above()).canBeReplaced(ctx) && ctx.getLevel().getFluidState(pos.above()).isEmpty()) ? super.getStateForPlacement(ctx) : null;
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, Fluid fluid) {
        return false;
    }

    @Override
    public boolean placeLiquid(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState, FluidState fluidState) {
        return false;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor level, BlockPos pos, BlockPos posFrom) {
        BlockState blockState = super.updateShape(state, direction, newState, level, pos, posFrom);
        if(!blockState.isAir())
        {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return blockState;
    }

    @Override
    protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
        return super.mayPlaceOn(floor, world, pos) || floor.is(BlockTags.SAND) || floor.is(Blocks.CLAY);
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        if(blockState.getValue(HALF) == DoubleBlockHalf.LOWER) return Fluids.WATER.getSource(false);

        return super.getFluidState(blockState);
    }
}
