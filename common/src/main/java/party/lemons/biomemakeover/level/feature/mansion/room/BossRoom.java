package party.lemons.biomemakeover.level.feature.mansion.room;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;
import party.lemons.biomemakeover.level.feature.mansion.RoomType;

import java.util.Random;

public class BossRoom extends MansionRoom
{
    public BossRoom(BlockPos position)
    {
        super(position, RoomType.BOSS);
    }

    public BlockPos getOffsetForRotation(BlockPos offsetPos, Rotation rotation)
    {
        if(layout.get(Direction.SOUTH)) return offsetPos.south(23).west(9);
        else if(layout.get(Direction.NORTH)) return offsetPos.north(14).east(19);
        else if(layout.get(Direction.EAST)) return offsetPos.east(23).south(19);
        else if(layout.get(Direction.WEST)) return offsetPos.west(14).north(9);

        return offsetPos;
    }

    public Rotation getRotation(Random random)
    {
        if(layout.get(Direction.SOUTH)) return Rotation.COUNTERCLOCKWISE_90;
        else if(layout.get(Direction.NORTH)) return Rotation.CLOCKWISE_90;
        else if(layout.get(Direction.EAST)) return Rotation.CLOCKWISE_180;
        else if(layout.get(Direction.WEST)) return Rotation.NONE;

        return Rotation.NONE;
    }
}