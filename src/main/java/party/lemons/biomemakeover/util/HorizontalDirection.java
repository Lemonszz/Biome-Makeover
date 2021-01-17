package party.lemons.biomemakeover.util;

import net.minecraft.util.math.BlockPos;

import java.util.Random;

public enum HorizontalDirection
{
	NORTH(0, -1, 4),
	NORTH_EAST(1, -1, 5),
	EAST(1, 0, 6),
	SOUTH_EAST(1, 1, 7),
	SOUTH(0, 1, 0),
	SOUTH_WEST(-1, 1, 1),
	WEST(-1, 0, 2),
	NORTH_WEST(-1, -1, 3);

	public int x;
	public int z;
	private final int opposite;
	HorizontalDirection(int x, int z, int opposite)
	{
		this.x = x;
		this.z = z;
		this.opposite = opposite;
	}

	public HorizontalDirection opposite()
	{
		return values()[opposite];
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
