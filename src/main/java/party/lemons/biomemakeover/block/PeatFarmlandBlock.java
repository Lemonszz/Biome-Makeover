package party.lemons.biomemakeover.block;

import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.pathing.NavigationType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.*;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.util.BlockWithItem;

import java.util.Iterator;
import java.util.Random;

public class PeatFarmlandBlock extends FarmlandBlock implements BlockWithItem
{
	public PeatFarmlandBlock(AbstractBlock.Settings settings)
	{
		super(settings);
	}

	@Override
	public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
	{
		if(!state.canPlaceAt(world, pos))
		{
			setToPeat(state, world, pos);
		}
	}

	@Override
	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
	{
		int i = state.get(MOISTURE);
		if(!isWaterNearby(world, pos) && !world.hasRain(pos.up()))
		{
			if(i > 0)
			{
				world.setBlockState(pos, state.with(MOISTURE, i - 1), 2);
			}else if(!hasCrop(world, pos))
			{
				setToPeat(state, world, pos);
			}
		}else if(i < 7)
		{
			world.setBlockState(pos, state.with(MOISTURE, 7), 2);
		}

		world.getBlockState(pos.up()).randomTick(world, pos.up(), random);
	}

	@Override
	public void onLandedUpon(World world, BlockPos pos, Entity entity, float distance)
	{
		if(!world.isClient && world.random.nextFloat() < distance - 0.5F && entity instanceof LivingEntity && (entity instanceof PlayerEntity || world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) && entity.getWidth() * entity.getWidth() * entity.getHeight() > 0.512F)
		{
			setToPeat(world.getBlockState(pos), world, pos);
		}
		entity.handleFallDamage(distance, 1.0F);
	}

	public static void setToPeat(BlockState state, World world, BlockPos pos)
	{
		world.setBlockState(pos, pushEntitiesUpBeforeBlockChange(state, BMBlocks.PEAT.getDefaultState(), world, pos));
	}

	private static boolean isWaterNearby(WorldView world, BlockPos pos) {
		Iterator var2 = BlockPos.iterate(pos.add(-4, 0, -4), pos.add(4, 1, 4)).iterator();

		BlockPos blockPos;
		do {
			if (!var2.hasNext()) {
				return false;
			}

			blockPos = (BlockPos)var2.next();
		} while(!world.getFluidState(blockPos).isIn(FluidTags.WATER));

		return true;
	}

	private static boolean hasCrop(BlockView world, BlockPos pos) {
		Block block = world.getBlockState(pos.up()).getBlock();
		return block instanceof CropBlock || block instanceof StemBlock || block instanceof AttachedStemBlock;
	}

	@Override
	public boolean is(Block block)
	{
		return super.is(block) || block == Blocks.FARMLAND;
	}
}
