package party.lemons.biomemakeover.level.feature.mansion;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

import java.util.List;
import java.util.Random;

public enum RoomType
{
    CORRIDOR(true, true, true, false, null),
    ROOM(false, true, true, false, MansionTemplateType.ROOMS),
    ROOM_BIG(false, false, true, false, MansionTemplateType.ROOMS_BIG),
    ROOM_BIG_DUMMY(false, false, true, false,  MansionTemplateType.ROOMS_BIG),
    STAIRS_UP(false, false, true, true,  MansionTemplateType.STAIR_UP),
    STAIRS_DOWN(false, false, true, true,  MansionTemplateType.STAIR_DOWN),
    ROOF(false, false, false, false, null),
    GARDEN(true, true, true, false,  MansionTemplateType.GARDEN),
    TOWER_BASE(false, false, true, true,  MansionTemplateType.TOWER_BASE),
    TOWER_MID(false, false, false, true,  MansionTemplateType.TOWER_MID),
    TOWER_TOP(false, false, false, true,  MansionTemplateType.TOWER_TOP),
    DUNGEON_STAIRS_TOP(false, false, true, true,  MansionTemplateType.DUNGEON_STAIR_TOP),
    DUNGEON_STAIRS_MID(false, false, false, true,  MansionTemplateType.DUNGEON_STAIR_MID),
    DUNGEON_STAIRS_BOTTOM(false, false, true, true,  MansionTemplateType.DUNGEON_STAIR_BOTTOM),
    DUNGEON_ROOM(false, true, true, true,  MansionTemplateType.DUNGEON_ROOM),
    BOSS(true, false, false, false,  MansionTemplateType.BOSS_ROOM),
    ENTRANCE(true, false, true, false,  MansionTemplateType.ENTRANCE);

    public final boolean doorRequired;
    private final boolean isReplaceable, hasWalls, columnRotation;
    private final MansionTemplateType templateType;

    RoomType(boolean doorRequired, boolean isReplaceable, boolean hasWalls, boolean columnRotation, MansionTemplateType templateType)
    {
        this.doorRequired = doorRequired;
        this.isReplaceable = isReplaceable;
        this.hasWalls = hasWalls;
        this.columnRotation = columnRotation;
        this.templateType = templateType;
    }

    public ResourceLocation getRandomTemplate(MansionTemplates templates, BlockPos pos, RandomSource random)
    {
        if(columnRotation)
        {
            List<ResourceLocation> templateList = templateType.getTemplates(templates);
            int index = Math.abs((pos.getX() + pos.getZ()) % templateList.size());
            return templateList.get(index);
        }

        return templateType.getRandomTemplate(templates, random);
    }

    public boolean isReplaceable()
    {
        return isReplaceable;
    }

    public boolean hasWalls()
    {
        return hasWalls;
    }

    public boolean hasWindows()
    {
        //TODO: better
        return this == RoomType.ROOM || this == RoomType.ROOM_BIG || this == RoomType.ROOM_BIG_DUMMY || this == STAIRS_DOWN || this == STAIRS_UP;
    }

    public boolean hasColumnRotation()
    {
        return columnRotation;
    }

    public boolean isDungeon()
    {
        //TODO: better
        //also this is unused?
        return this == DUNGEON_ROOM || this == DUNGEON_STAIRS_MID || this == DUNGEON_STAIRS_BOTTOM;
    }
}