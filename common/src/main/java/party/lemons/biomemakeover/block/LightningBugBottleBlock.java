package party.lemons.biomemakeover.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.block.blockentity.LightningBugBottleBlockEntity;
import party.lemons.taniwha.block.types.TBlock;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.WATERLOGGED;

public class LightningBugBottleBlock extends TBlock implements EntityBlock, SimpleWaterloggedBlock
{
    public static final BooleanProperty UPPER = BooleanProperty.create("up");

    private static final VoxelShape LOWER_SHAPE = Shapes.or(Block.box(4.0D, 0.0D, 4.0D, 12.0D, 8.0D, 12.0D), Block.box(6.0D, 8.0D, 6.0D, 10.0D, 12.0D, 10.0D));
    private static final VoxelShape UPPER_SHAPE = Shapes.or(Block.box(4.0D, 4.0D, 4.0D, 12.0D, 12.0D, 12.0D), Block.box(6.0D, 11.0D, 6.0D, 10.0D, 16.0D, 10.0D));

    public LightningBugBottleBlock(Properties properties)
    {
        super(properties);

        registerDefaultState(getStateDefinition().any().setValue(UPPER, false).setValue(WATERLOGGED, false));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        FluidState fluidState = ctx.getLevel().getFluidState(ctx.getClickedPos());
        return defaultBlockState().setValue(UPPER, ctx.getClickedFace() == Direction.DOWN).setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER);

    }

    @Override
    public FluidState getFluidState(BlockState state)
    {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return blockState.getValue(UPPER) ? UPPER_SHAPE : LOWER_SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(UPPER);
        builder.add(WATERLOGGED);
    }

    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
        return false;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new LightningBugBottleBlockEntity(blockPos, blockState);
    }
}