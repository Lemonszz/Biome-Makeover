package party.lemons.biomemakeover.world.feature.mansion.processor;

import party.lemons.biomemakeover.util.Grid;
import party.lemons.biomemakeover.world.feature.mansion.RoomType;
import party.lemons.biomemakeover.world.feature.mansion.room.MansionRoom;

import java.util.Random;

public class CorridorReplaceProcessor extends FloorRoomReplaceProcessor
{
	@Override
	public boolean isValid(Random random, int floor, Grid<MansionRoom> grid, MansionRoom currentRoom)
	{
		return currentRoom.getRoomType() == RoomType.CORRIDOR && random.nextInt(4) == 0;
	}

	@Override
	public MansionRoom getReplaceRoom(MansionRoom currentRoom)
	{
		currentRoom.setRoomType(RoomType.ROOM);
		return currentRoom;
	}
}
