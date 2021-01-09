package party.lemons.biomemakeover.world.feature.mansion;

import net.minecraft.util.math.BlockPos;

public enum WallDirection
{
	NORTH(0, -1),
	EAST(1, 0),
	SOUTH(0, 1),
	WEST(-1, 0);

	public int x;
	public int z;
	WallDirection(int x, int z)
	{
		this.x = x;
		this.z = z;
	}

	public WallDirection opposite()
	{
		switch(this)
		{
			case NORTH:
				return SOUTH;
			case EAST:
				return WEST;
			case SOUTH:
				return NORTH;
			case WEST:
				return EAST;
		}
		throw new RuntimeException();
	}
}
