package party.lemons.biomemakeover.world.feature.mansion.processor;

import net.minecraft.util.math.Direction;
import party.lemons.biomemakeover.util.Grid;
import party.lemons.biomemakeover.util.MathUtils;
import party.lemons.biomemakeover.world.feature.mansion.LayoutType;
import party.lemons.biomemakeover.world.feature.mansion.RoomType;
import party.lemons.biomemakeover.world.feature.mansion.room.MansionRoom;
import party.lemons.biomemakeover.world.feature.mansion.room.NonRoofedMansionRoom;

import java.util.Random;

public class GardenRoomReplaceProcessor extends FloorRoomReplaceProcessor
{

	@Override
	public boolean isValid(Random random, int floor, Grid<MansionRoom> layout, MansionRoom currentRoom)
	{
		if(floor == 0 && random.nextInt(3) == 0)
		{
			boolean isSurrounded = true;
			for(Direction dir : MathUtils.HORIZONTALS)
			{
				if(!layout.contains(currentRoom.getPosition().offset(dir)))
				{
					isSurrounded = false;
					break;
				}
			}
			return isSurrounded;
		}

		return false;
	}

	@Override
	public MansionRoom getReplaceRoom(MansionRoom currentRoom)
	{
		NonRoofedMansionRoom gardenRoom = new NonRoofedMansionRoom(currentRoom.getPosition(), RoomType.GARDEN);
		gardenRoom.setLayoutType(LayoutType.REQUIRED);
		return gardenRoom;
	}
}
