package party.lemons.biomemakeover.level.feature.mansion.room;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import party.lemons.biomemakeover.level.feature.mansion.MansionFeature;
import party.lemons.biomemakeover.level.feature.mansion.MansionTemplateType;
import party.lemons.biomemakeover.level.feature.mansion.MansionTemplates;
import party.lemons.biomemakeover.level.feature.mansion.RoomType;
import party.lemons.taniwha.util.collections.Grid;

public class DungeonRoom extends MansionRoom
{
    public DungeonRoom(BlockPos position, RoomType type)
    {
        super(position, type);
    }

    @Override
    public String getInnerWall(MansionTemplates templates, RandomSource random)
    {
        return MansionTemplateType.DUNGEON_DOOR.getRandomTemplate(templates, random).toString();
    }

    @Override
    public String getFlatWall(MansionTemplates templates, RandomSource random)
    {
        return MansionTemplateType.DUNGEON_WAll.getRandomTemplate(templates, random).toString();
    }

    @Override
    public String getOuterWall(MansionTemplates templates, Direction dir, Grid<MansionRoom> roomGrid, RandomSource random)
    {
        return MansionTemplateType.DUNGEON_WAll.getRandomTemplate(templates, random).toString();
    }

    @Override
    public boolean hasGroundModifications() {
        return false;
    }
}