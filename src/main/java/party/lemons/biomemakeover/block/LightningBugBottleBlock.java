package party.lemons.biomemakeover.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import party.lemons.biomemakeover.block.blockentity.LightningBugBottleBlockEntity;

public class LightningBugBottleBlock extends BMBlock implements BlockEntityProvider
{
	public static final BooleanProperty UPPER = BooleanProperty.of("up");

	private static VoxelShape LOWER_SHAPE = VoxelShapes.union(Block.createCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 8.0D, 12.0D), Block.createCuboidShape(6.0D, 8.0D, 6.0D, 10.0D, 12.0D, 10.0D));
	private static VoxelShape UPPER_SHAPE = VoxelShapes.union(Block.createCuboidShape(4.0D, 4.0D, 4.0D, 12.0D, 12.0D, 12.0D), Block.createCuboidShape(6.0D, 11.0D, 6.0D, 10.0D, 16.0D, 10.0D));

	public LightningBugBottleBlock(FabricBlockSettings settings)
	{
		super(settings);

		this.setDefaultState(this.getStateManager().getDefaultState().with(UPPER, false));
	}

	public BlockState getPlacementState(ItemPlacementContext ctx) {
		return getDefaultState().with(UPPER, ctx.getSide() == Direction.DOWN);
	}

	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
	{
		return state.get(UPPER) ? UPPER_SHAPE : LOWER_SHAPE;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
		super.appendProperties(builder);
		builder.add(UPPER);
	}

	public PistonBehavior getPistonBehavior(BlockState state) {
		return PistonBehavior.DESTROY;
	}


	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world)
	{
		return new LightningBugBottleBlockEntity();
	}
}
