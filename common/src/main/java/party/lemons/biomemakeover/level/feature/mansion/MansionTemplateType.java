package party.lemons.biomemakeover.level.feature.mansion;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;

import java.util.List;

public enum MansionTemplateType
{
	CORRIDOR_STRAIGHT(m->m.baseTemplates().CORRIDOR_STRAIGHT()),
	CORRIDOR_CORNER(m->m.baseTemplates().CORRIDOR_CORNER()),
	CORRIDOR_T(m->m.baseTemplates().CORRIDOR_T()),
	CORRIDOR_CROSS(m->m.baseTemplates().CORRIDOR_CROSS()),
	ROOMS(m->m.baseTemplates().ROOMS()),
	ROOMS_BIG(m->m.baseTemplates().ROOMS_BIG()),
	STAIR_UP(m->m.baseTemplates().STAIRS_UP()),
	STAIR_DOWN(m->m.baseTemplates().STAIRS_DOWN()),
	INNER_WALL(m->m.baseTemplates().INNER_WALL()),
	FLAT_WALL(m->m.baseTemplates().FLAT_WALL()),
	OUTER_WALL_BASE(m->m.baseTemplates().OUTER_WALL_BASE()),
	OUTER_WALL(m->m.baseTemplates().OUTER_WALL()),
	OUTER_WINDOW(m->m.baseTemplates().OUTER_WINDOW()),
	GARDEN(m->m.baseTemplates().GARDEN()),
	TOWER_BASE(m->m.towerRoofTemplates().TOWER_BASE()),
	TOWER_MID(m->m.towerRoofTemplates().TOWER_MID()),
	TOWER_TOP(m->m.towerRoofTemplates().TOWER_TOP()),
	ROOF_0(m->m.towerRoofTemplates().ROOF_0()),
	ROOF_1(m->m.towerRoofTemplates().ROOF_1()),
	ROOF_2(m->m.towerRoofTemplates().ROOF_2()),
	ROOF_2_STRAIGHT(m->m.towerRoofTemplates().ROOF_2_STRAIGHT()),
	ROOF_3(m->m.towerRoofTemplates().ROOF_3()),
	ROOF_4(m->m.towerRoofTemplates().ROOF_4()),
	ROOF_SPLIT(m->m.towerRoofTemplates().ROOF_SPLIT()),
	DUNGEON_DOOR(m->m.dungeonTemplates().DUNGEON_DOOR()),
	DUNGEON_WAll(m->m.dungeonTemplates().DUNGEON_WALL()),
	DUNGEON_ROOM(m->m.dungeonTemplates().DUNGEON_ROOM()),
	DUNGEON_STAIR_BOTTOM(m->m.dungeonTemplates().DUNGEON_STAIRS_BOTTOM()),
	DUNGEON_STAIR_MID(m->m.dungeonTemplates().DUNGEON_STAIRS_MID()),
	DUNGEON_STAIR_TOP(m->m.dungeonTemplates().DUNGEON_STAIR_TOP()),
	BOSS_ROOM(m->m.dungeonTemplates().BOSS_ROOM()),
	ENTRANCE(m->m.baseTemplates().ENTRANCE()),
	CORNER_FILLERS(m->m.otherTemplates().CORNER_FILLERS()),
	EMPTIES(m->m.otherTemplates().EMPTIES());

	private final TemplateGetter templateGetter;
	private MansionTemplateType(TemplateGetter templateGetter)
	{
		this.templateGetter = templateGetter;
	}

	public List<ResourceLocation> getTemplates(MansionTemplates mansion)
	{
		return templateGetter.getTemplate(mansion);
	}

	public ResourceLocation getRandomTemplate(MansionTemplates mansionFeature, RandomSource random)
	{
		List<ResourceLocation> template = getTemplates(mansionFeature);
		return template.get(random.nextInt(template.size()));
	}
}