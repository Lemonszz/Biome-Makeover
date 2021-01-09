package party.lemons.biomemakeover.world.feature.mansion;

import net.minecraft.util.math.Direction;

import java.util.HashMap;

public class RoomLayout extends HashMap<Direction, Boolean>
{
	public RoomLayout()
	{
		for(int i = 0; i < 4; i++)
		{
			put(Direction.fromHorizontal(i), false);
		}
	}

	public int getCount()
	{
		return entrySet().size();
	}
}
