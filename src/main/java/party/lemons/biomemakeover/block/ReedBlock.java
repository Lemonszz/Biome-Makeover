package party.lemons.biomemakeover.block;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Random;

public class ReedBlock extends WaterTallFlowerBlock
{
	public ReedBlock(Settings settings)
	{
		super(settings);
	}

	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state)
	{
		return false;
	}
}
