package party.lemons.biomemakeover.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Lazy;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.chunk.light.ChunkLightProvider;

import java.util.Random;

public class BMSpreadableBlock extends BMBlock
{
	private final Lazy<Block> dirtBlock;

	public BMSpreadableBlock(Settings settings, Lazy<Block> dirtBlock)
	{
		super(settings);

		this.dirtBlock = dirtBlock;
	}

	private static boolean canSurvive(BlockState state, WorldView worldView, BlockPos pos)
	{
		BlockPos blockPos = pos.up();
		BlockState blockState = worldView.getBlockState(blockPos);
		int i = ChunkLightProvider.getRealisticOpacity(worldView, state, pos, blockState, blockPos, Direction.UP, blockState.getOpacity(worldView, blockPos));
		return i < worldView.getMaxLightLevel();
	}

	private static boolean canSpread(BlockState state, WorldView worldView, BlockPos pos)
	{
		return canSurvive(state, worldView, pos);
	}

	protected BlockState getStateForPlacement(World world, BlockPos pos)
	{
		return getDefaultPlaceState();
	}

	protected BlockState getDefaultPlaceState()
	{
		return getDefaultState();
	}


	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random)
	{
		if(!canSurvive(state, world, pos))
		{
			world.setBlockState(pos, dirtBlock.get().getDefaultState());
		}else
		{
			if(world.getLightLevel(pos.up()) >= 9)
			{
				for(int i = 0; i < 4; ++i)
				{
					BlockPos blockPos = pos.add(random.nextInt(3) - 1, random.nextInt(5) - 3, random.nextInt(3) - 1);
					if(world.getBlockState(blockPos).isOf(dirtBlock.get()) && canSpread(getDefaultPlaceState(), world, blockPos))
					{
						world.setBlockState(blockPos, getStateForPlacement(world, blockPos));
					}
				}
			}

		}
	}
}
