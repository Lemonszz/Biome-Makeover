package party.lemons.biomemakeover.level.feature.mansion.processor;

import net.minecraft.util.RandomSource;
import party.lemons.biomemakeover.level.feature.mansion.RoomType;
import party.lemons.biomemakeover.level.feature.mansion.room.MansionRoom;
import party.lemons.biomemakeover.util.Grid;

import java.util.Random;

public class CorridorReplaceProcessor extends FloorRoomReplaceProcessor
{
    @Override
    public boolean isValid(RandomSource random, int floor, Grid<MansionRoom> grid, MansionRoom currentRoom)
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