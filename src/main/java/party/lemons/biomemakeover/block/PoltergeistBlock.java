package party.lemons.biomemakeover.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import party.lemons.biomemakeover.block.blockentity.PoltergeistBlockEntity;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.world.PoltergeistHandler;

import java.util.Random;

public class PoltergeistBlock extends BMBlock implements BlockEntityProvider
{
	public static BooleanProperty ENABLED = Properties.ENABLED;

	public PoltergeistBlock(Settings settings)
	{
		super(settings);
		this.setDefaultState(this.getDefaultState().with(ENABLED, true));
	}

	@Override
	public BlockState getPlacementState(ItemPlacementContext ctx)
	{
		return this.getDefaultState().with(ENABLED, !ctx.getWorld().isReceivingRedstonePower(ctx.getBlockPos()));
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify)
	{
		if(!world.isClient)
		{
			boolean currentlyEnabled = state.get(ENABLED);
			if(currentlyEnabled == world.isReceivingRedstonePower(pos))
			{
				if(currentlyEnabled)
				{
					world.getBlockTickScheduler().schedule(pos, this, 4);
				}else
				{
					world.setBlockState(pos, state.cycle(ENABLED), 2);
					doToggleEffects(world, pos);
				}
			}
		}
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
	{
		if(state.get(ENABLED) && world.isReceivingRedstonePower(pos))
		{
			world.setBlockState(pos, state.cycle(ENABLED), 2);
			doToggleEffects(world, pos);
		}
	}

	private void doToggleEffects(World world, BlockPos pos)
	{
		world.playSound(null, pos, BMEffects.POLTERGEIST_TOGGLE, SoundCategory.BLOCKS, 1F, 1F);
		PoltergeistHandler.doParticles(world, pos);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world)
	{
		return new PoltergeistBlockEntity();
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
		builder.add(ENABLED);
	}
}
