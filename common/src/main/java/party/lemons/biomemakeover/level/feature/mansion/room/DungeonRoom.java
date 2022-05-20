package party.lemons.biomemakeover.level.feature.mansion.room;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import party.lemons.biomemakeover.level.feature.mansion.MansionFeature;
import party.lemons.biomemakeover.level.feature.mansion.RoomType;
import party.lemons.biomemakeover.util.Grid;

public class DungeonRoom extends MansionRoom
{
    public DungeonRoom(BlockPos position, RoomType type)
    {
        super(position, type);
    }

    @Override
    public String getInnerWall(RandomSource random)
    {
        return MansionFeature.DUNGEON_DOOR.get(random.nextInt(MansionFeature.DUNGEON_DOOR.size())).toString();
    }

    @Override
    public String getFlatWall(RandomSource random)
    {
        return MansionFeature.DUNGEON_WALL.get(random.nextInt(MansionFeature.DUNGEON_WALL.size())).toString();
    }

    @Override
    public String getOuterWall(Direction dir, Grid<MansionRoom> roomGrid, RandomSource random)
    {
        return MansionFeature.DUNGEON_WALL.get(random.nextInt(MansionFeature.DUNGEON_WALL.size())).toString();
    }

    @Override
    public boolean hasGroundModifications() {
        return true;
    }
}