package party.lemons.biomemakeover.level.feature.mansion;

import net.minecraft.core.Direction;

import java.util.HashMap;

public class RoomLayout extends HashMap<Direction, Boolean>
{
    public RoomLayout()
    {
        for(int i = 0; i < 4; i++)
        {
            put(Direction.from2DDataValue(i), false);
        }
    }

    public int getCount()
    {
        return entrySet().size();
    }

    public int doorCount()
    {
        int count = 0;
        for(int i = 0; i < 4; i++)
        {
            if(get(Direction.from2DDataValue(i))) count++;
        }
        return count;
    }
}