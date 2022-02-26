package party.lemons.biomemakeover.level.feature.mansion.processor;

import net.minecraft.core.Direction;
import party.lemons.biomemakeover.level.feature.mansion.LayoutType;
import party.lemons.biomemakeover.level.feature.mansion.RoomType;
import party.lemons.biomemakeover.level.feature.mansion.room.MansionRoom;
import party.lemons.biomemakeover.level.feature.mansion.room.NonRoofedMansionRoom;
import party.lemons.taniwha.util.MathUtils;
import party.lemons.taniwha.util.collections.Grid;

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
                if(!layout.contains(currentRoom.getPosition().relative(dir)))
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