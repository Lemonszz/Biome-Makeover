package party.lemons.biomemakeover.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import party.lemons.biomemakeover.block.blockentity.AltarBlockEntity;
import party.lemons.biomemakeover.util.BlockWithItem;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.Random;

import static net.minecraft.state.property.Properties.WATERLOGGED;

public class AltarBlock extends BlockWithEntity implements BlockWithItem, Waterloggable
{
	public static final VoxelShape BOTTOM_SHAPE = Block.createCuboidShape(2.0D, 0.0D, 2.0D, 14.0D, 2.0D, 14.0D);
	public static final VoxelShape MIDDLE_SHAPE = Block.createCuboidShape(4.0D, 2.0D, 4D, 12.0D, 10.0D, 12.0D);
	public static final VoxelShape TOP_SHAPE = Block.createCuboidShape(2.0D, 10.0D, 2D, 14.0D, 12.0D, 14.0D);
	public static final VoxelShape SHAPE = VoxelShapes.union(BOTTOM_SHAPE, MIDDLE_SHAPE, TOP_SHAPE);
	public static BooleanProperty ACTIVE = BooleanProperty.of("active");

	public AltarBlock(Settings settings)
	{
		super(settings);
		this.setDefaultState(getStateManager().getDefaultState().with(ACTIVE, false).with(WATERLOGGED, false));
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world)
	{
		return new AltarBlockEntity();
	}

	public BlockState getPlacementState(ItemPlacementContext ctx)
	{
		FluidState fluidState = ctx.getWorld().getFluidState(ctx.getBlockPos());

		return getDefaultState().with(WATERLOGGED, fluidState.getFluid() == Fluids.WATER);
	}

	@Override
	public FluidState getFluidState(BlockState state)
	{
		return state.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
	}


	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
	{
		if(!world.isClient)
		{
			NamedScreenHandlerFactory screenHandlerFactory = state.createScreenHandlerFactory(world, pos);
			if(screenHandlerFactory != null)
			{
				player.openHandledScreen(screenHandlerFactory);
			}
		}
		return ActionResult.SUCCESS;
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved)
	{
		if(state.getBlock() != newState.getBlock())
		{
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if(blockEntity instanceof AltarBlockEntity)
			{
				ItemScatterer.spawn(world, pos, (AltarBlockEntity) blockEntity);
				world.updateComparators(pos, this);
			}
			super.onStateReplaced(state, world, pos, newState, moved);
		}
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context)
	{
		return SHAPE;
	}

	@Override
	public VoxelShape getCullingShape(BlockState state, BlockView world, BlockPos pos)
	{
		return SHAPE;
	}

	@Override
	public boolean hasSidedTransparency(BlockState state)
	{
		return true;
	}

	@Override
	public boolean canPathfindThrough(BlockState state, BlockView world, BlockPos pos, NavigationType type)
	{
		return false;
	}

	@Environment(EnvType.CLIENT)
	@Override
	public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random)
	{
		if(state.get(ACTIVE))
		{
			for(int i = 0; i < 5; i++)
			{
				double xSpeed = RandomUtil.randomRange(-1, 1) / 0.75F;
				double zSpeed = RandomUtil.randomRange(-1, 1) / 0.75F;
				double ySpeed = random.nextDouble() / 0.1F;

				world.addParticle(ParticleTypes.ENCHANT, (double) pos.getX() + 0.5F, (double) pos.getY() + 0.75F, (double) pos.getZ() + 0.5F, xSpeed, ySpeed, zSpeed);
			}
		}

		if(random.nextInt(5) == 0)
		{
			Direction direction = Direction.random(random);
			if(!direction.getAxis().isVertical())
			{
				double x = direction.getAxis() == Direction.Axis.X ? (0.5F + (0.3F * (float) RandomUtil.randomDirection(1))) : (float) RandomUtil.randomRange(2, 8) / 10F;
				double z = direction.getAxis() == Direction.Axis.Z ? (0.5F + (0.3F * (float) RandomUtil.randomDirection(1))) : (float) RandomUtil.randomRange(2, 8) / 10F;
				double y = 0.2F + (random.nextFloat() / 3F);

				world.addParticle(ParticleTypes.DRIPPING_OBSIDIAN_TEAR, (double) pos.getX() + x, (double) pos.getY() + y, (double) pos.getZ() + z, 0.0D, 0.0D, 0.0D);
			}
		}
	}

	@Override
	public boolean hasComparatorOutput(BlockState state)
	{
		return true;
	}

	@Override
	public int getComparatorOutput(BlockState state, World world, BlockPos pos)
	{
		return ScreenHandler.calculateComparatorOutput(world.getBlockEntity(pos));
	}

	@Override
	public BlockRenderType getRenderType(BlockState state)
	{
		return BlockRenderType.MODEL;
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
		super.appendProperties(builder);
		builder.add(ACTIVE);
		builder.add(WATERLOGGED);
	}
}
