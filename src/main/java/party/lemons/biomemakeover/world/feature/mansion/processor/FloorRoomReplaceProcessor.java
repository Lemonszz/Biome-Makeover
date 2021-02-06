package party.lemons.biomemakeover.world.feature.mansion.processor;

import party.lemons.biomemakeover.util.Grid;
import party.lemons.biomemakeover.world.feature.mansion.room.MansionRoom;

import java.util.Random;

public abstract class FloorRoomReplaceProcessor
{
	public abstract boolean isValid(Random random, int floor, Grid<MansionRoom> grid, MansionRoom currentRoom);
	public abstract MansionRoom getReplaceRoom(MansionRoom currentRoom);
}
