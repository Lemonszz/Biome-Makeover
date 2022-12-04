package party.lemons.biomemakeover.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import org.jetbrains.annotations.Nullable;
import party.lemons.taniwha.block.types.TBlock;

import java.util.Random;

public class WillowingBranchesBlock extends TBlock implements SimpleWaterloggedBlock {

    public static final int MAX_STAGE = 2;

    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, MAX_STAGE);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public WillowingBranchesBlock(Properties properties)
    {
        super(properties);

        registerDefaultState(getStateDefinition().any().setValue(STAGE, 0).setValue(WATERLOGGED, false));
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState newState, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos posFrom) {
        if(direction == Direction.UP)
        {
            if(newState.is(this) && newState.getValue(STAGE) != blockState.getValue(STAGE) - 1)
            {
                int newStage = newState.getValue(STAGE);
                if(newStage != MAX_STAGE)
                    return blockState.setValue(STAGE, newStage + 1);
            }
        }

        if(!canSurvive(blockState, levelAccessor, blockPos))
        {
            levelAccessor.scheduleTick(blockPos, this, 1);
        }

        return super.updateShape(blockState, direction, newState, levelAccessor, blockPos, posFrom);
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource random) {

        if(random.nextInt(7) == 0)
        {
            if(blockState.getValue(STAGE) < MAX_STAGE)
            {
                BlockState placeState = defaultBlockState().setValue(STAGE, blockState.getValue(STAGE) + 1);
                BlockPos below = blockPos.below();
                if(serverLevel.isEmptyBlock(below) && canSurvive(placeState, serverLevel, below))
                {
                    FluidState st = serverLevel.getFluidState(below);
                    serverLevel.setBlock(below, placeState.setValue(WATERLOGGED, st.getType() == Fluids.WATER), 3);
                }
            }
        }

        super.randomTick(blockState, serverLevel, blockPos, random);
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource random) {
        if(!blockState.canSurvive(serverLevel, blockPos))
            serverLevel.destroyBlock(blockPos, true);
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader level, BlockPos pos) {
        BlockPos blockPos = pos.above();
        BlockState upState = level.getBlockState(blockPos);
        return (upState.is(this) && upState.getValue(STAGE) < MAX_STAGE) || upState.is(BlockTags.LEAVES);
    }

    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
        return pathComputationType == PathComputationType.AIR && !this.hasCollision || super.isPathfindable(blockState, blockGetter, blockPos, pathComputationType);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx)
    {
        BlockPos pos = ctx.getClickedPos();
        BlockState upState = ctx.getLevel().getBlockState(pos.above());
        FluidState fluidState = ctx.getLevel().getFluidState(pos);
        boolean waterlog = fluidState.getType() == Fluids.WATER;

        if(upState.is(this))
        {
            if(upState.getValue(STAGE) < MAX_STAGE)
            {
                return this.defaultBlockState().setValue(STAGE, upState.getValue(STAGE) + 1).setValue(WATERLOGGED, waterlog);
            }
        }

        return super.getStateForPlacement(ctx).setValue(WATERLOGGED, waterlog);
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE, WATERLOGGED);
        super.createBlockStateDefinition(builder);
    }
}
