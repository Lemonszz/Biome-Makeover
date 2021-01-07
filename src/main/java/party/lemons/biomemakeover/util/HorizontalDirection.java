package party.lemons.biomemakeover.util;

import net.minecraft.util.math.BlockPos;

import java.util.Random;

public enum HorizontalDirection
{
	NORTH(0, -1),
	NORTH_EAST(1, -1),
	EAST(1, 0),
	SOUTH_EAST(1, 1),
	SOUTH(0, 1),
	SOUTH_WEST(-1, 1),
	WEST(-1, 0),
	NORTH_WEST(-1, -1);

	public int x;
	public int z;
	HorizontalDirection(int x, int z)
	{
		this.x = x;
		this.z = z;
	}

	public BlockPos offset(BlockPos pos)
	{
		return pos.add(x, 0, z);
	}

	public static HorizontalDirection random(Random random)
	{
		return values()[random.nextInt(values().length)];
	}
}
