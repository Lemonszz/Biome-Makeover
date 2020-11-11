package party.lemons.biomemakeover.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMItems;
import party.lemons.biomemakeover.item.BlockStateItem;

import java.util.Random;

public class BarrelCactusBlock extends BMBlock
{
	protected static final VoxelShape SHAPE = Block.createCuboidShape(4.0D, 0.0D, 4.0D, 12.0D, 7.0D, 12.0D);
	public static final BooleanProperty FLOWERED = BooleanProperty.of("flowered");

	public BarrelCactusBlock(FabricBlockSettings settings)
	{
		super(settings.ticksRandomly());
		this.setDefaultState(stateManager.getDefaultState().with(FLOWERED, false));
	}

	@Override
	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos)
	{
		BlockState checkState = world.getBlockState(pos.down());
		return checkState.isOf(Blocks.SAND) || checkState.isOf(Blocks.RED_SAND);
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
	{
		if(!state.get(FLOWERED) && world.getBaseLightLevel(pos, 0) >= 9 && random.nextInt(7) == 0)
			world.setBlockState(pos, state.with(FLOWERED, true));
	}

	@Override
	public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState newState, WorldAccess world, BlockPos pos, BlockPos posFrom)
	{
		if (!state.canPlaceAt(world, pos)) {
			world.getBlockTickScheduler().schedule(pos, this, 1);
		}
		return state;
	}

	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (!state.canPlaceAt(world, pos)) {
			world.breakBlock(pos, true);
		}
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
	{
		return SHAPE;
	}

	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		entity.damage(DamageSource.CACTUS, 1.0F);
	}

	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type) {
		return false;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
		builder.add(FLOWERED);
	}

	@Override
	public void registerItem(Identifier id)
	{
		Registry.register(Registry.ITEM, id, new BlockItem(this, makeItemSettings()));

		BMItems.BARREL_CACTUS_FLOWERED = new BlockStateItem(getDefaultState().with(FLOWERED, true), "flowered", makeItemSettings());
		Registry.register(Registry.ITEM, BiomeMakeover.ID(id.getPath() + "_flowered"), BMItems.BARREL_CACTUS_FLOWERED);
	}

	public static boolean isFloweredItem(ItemStack stack)
	{
		return stack.getItem() == BMItems.BARREL_CACTUS_FLOWERED;
	}
}
