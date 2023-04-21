package party.lemons.biomemakeover.level.feature.mansion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public record MansionTemplates(BaseTemplates baseTemplates, TowerRoofTemplates towerRoofTemplates, DungeonTemplates dungeonTemplates, OtherTemplates otherTemplates)
{
	public static final Codec<MansionTemplates> CODEC = RecordCodecBuilder.create(i->
			i.group(
				BaseTemplates.CODEC.fieldOf("base").forGetter(MansionTemplates::baseTemplates),
				TowerRoofTemplates.CODEC.fieldOf("tower_roof").forGetter(MansionTemplates::towerRoofTemplates),
					DungeonTemplates.CODEC.fieldOf("dungeon").forGetter(MansionTemplates::dungeonTemplates),
					OtherTemplates.CODEC.fieldOf("other").forGetter(MansionTemplates::otherTemplates)
			).apply(i, MansionTemplates::new));

	public record BaseTemplates(List<ResourceLocation> CORRIDOR_STRAIGHT, List<ResourceLocation> CORRIDOR_CORNER, List<ResourceLocation> CORRIDOR_T, List<ResourceLocation> CORRIDOR_CROSS, List<ResourceLocation> ROOMS, List<ResourceLocation> ROOMS_BIG, List<ResourceLocation> STAIRS_UP, List<ResourceLocation> STAIRS_DOWN, List<ResourceLocation> INNER_WALL, List<ResourceLocation> FLAT_WALL,  List<ResourceLocation> OUTER_WALL_BASE, List<ResourceLocation> OUTER_WALL, List<ResourceLocation> OUTER_WINDOW, List<ResourceLocation> GARDEN, List<ResourceLocation> ENTRANCE)
	{
		public static final Codec<BaseTemplates> CODEC = RecordCodecBuilder.create(i->
				i.group(
						ResourceLocation.CODEC.listOf().fieldOf("corridor_straight").forGetter(s->s.CORRIDOR_STRAIGHT),
						ResourceLocation.CODEC.listOf().fieldOf("corridor_corner").forGetter(s->s.CORRIDOR_CORNER),
						ResourceLocation.CODEC.listOf().fieldOf("corridor_t").forGetter(s->s.CORRIDOR_T),
						ResourceLocation.CODEC.listOf().fieldOf("corridor_cross").forGetter(s->s.CORRIDOR_CROSS),
						ResourceLocation.CODEC.listOf().fieldOf("rooms").forGetter(s->s.ROOMS),
						ResourceLocation.CODEC.listOf().fieldOf("rooms_big").forGetter(s->s.ROOMS_BIG),
						ResourceLocation.CODEC.listOf().fieldOf("stair_up").forGetter(s->s.STAIRS_UP),
						ResourceLocation.CODEC.listOf().fieldOf("stair_down").forGetter(s->s.STAIRS_DOWN),
						ResourceLocation.CODEC.listOf().fieldOf("inner_wall").forGetter(s->s.INNER_WALL),
						ResourceLocation.CODEC.listOf().fieldOf("flat_wall").forGetter(s->s.FLAT_WALL),
						ResourceLocation.CODEC.listOf().fieldOf("outer_wall_base").forGetter(s->s.OUTER_WALL_BASE),
						ResourceLocation.CODEC.listOf().fieldOf("outer_wall").forGetter(s->s.OUTER_WALL),
						ResourceLocation.CODEC.listOf().fieldOf("outer_window").forGetter(s->s.OUTER_WINDOW),
						ResourceLocation.CODEC.listOf().fieldOf("garden").forGetter(s->s.GARDEN),
						ResourceLocation.CODEC.listOf().fieldOf("entrance").forGetter(s->s.ENTRANCE)
				).apply(i, BaseTemplates::new));
	}

	public record TowerRoofTemplates(List<ResourceLocation> TOWER_BASE, List<ResourceLocation> TOWER_MID, List<ResourceLocation> TOWER_TOP, List<ResourceLocation> ROOF_0, List<ResourceLocation> ROOF_1, List<ResourceLocation> ROOF_2, List<ResourceLocation> ROOF_2_STRAIGHT, List<ResourceLocation> ROOF_3, List<ResourceLocation> ROOF_4, List<ResourceLocation> ROOF_SPLIT)
	{
		public static final Codec<TowerRoofTemplates> CODEC = RecordCodecBuilder.create(i->
				i.group(
					ResourceLocation.CODEC.listOf().fieldOf("tower_base").forGetter(s->s.TOWER_BASE),
					ResourceLocation.CODEC.listOf().fieldOf("tower_mid").forGetter(s->s.TOWER_MID),
					ResourceLocation.CODEC.listOf().fieldOf("tower_top").forGetter(s->s.TOWER_TOP),
					ResourceLocation.CODEC.listOf().fieldOf("roof_0").forGetter(s->s.ROOF_0),
					ResourceLocation.CODEC.listOf().fieldOf("roof_1").forGetter(s->s.ROOF_1),
					ResourceLocation.CODEC.listOf().fieldOf("roof_2").forGetter(s->s.ROOF_2),
					ResourceLocation.CODEC.listOf().fieldOf("roof_2_straight").forGetter(s->s.ROOF_2_STRAIGHT),
					ResourceLocation.CODEC.listOf().fieldOf("roof_3").forGetter(s->s.ROOF_3),
					ResourceLocation.CODEC.listOf().fieldOf("roof_4").forGetter(s->s.ROOF_4),
					ResourceLocation.CODEC.listOf().fieldOf("roof_split").forGetter(s->s.ROOF_SPLIT)
			).apply(i, TowerRoofTemplates::new));
	}

	public record DungeonTemplates(List<ResourceLocation> DUNGEON_DOOR, List<ResourceLocation> DUNGEON_WALL, List<ResourceLocation> DUNGEON_ROOM, List<ResourceLocation> DUNGEON_STAIRS_BOTTOM, List<ResourceLocation> DUNGEON_STAIRS_MID, List<ResourceLocation> DUNGEON_STAIR_TOP, List<ResourceLocation> BOSS_ROOM)
	{
		public static final Codec<DungeonTemplates> CODEC = RecordCodecBuilder.create(i->
				i.group(
						ResourceLocation.CODEC.listOf().fieldOf("dungeon_door").forGetter(s->s.DUNGEON_DOOR),
						ResourceLocation.CODEC.listOf().fieldOf("dungeon_wall").forGetter(s->s.DUNGEON_WALL),
						ResourceLocation.CODEC.listOf().fieldOf("dungeon_room").forGetter(s->s.DUNGEON_ROOM),
						ResourceLocation.CODEC.listOf().fieldOf("dungeon_stair_bottom").forGetter(s->s.DUNGEON_STAIRS_BOTTOM),
						ResourceLocation.CODEC.listOf().fieldOf("dungeon_stair_mid").forGetter(s->s.DUNGEON_STAIRS_MID),
						ResourceLocation.CODEC.listOf().fieldOf("dungeon_stair_top").forGetter(s->s.DUNGEON_STAIR_TOP),
						ResourceLocation.CODEC.listOf().fieldOf("boss_room").forGetter(s->s.BOSS_ROOM)
				).apply(i, DungeonTemplates::new));
	}

	public record OtherTemplates(List<ResourceLocation> CORNER_FILLERS, List<ResourceLocation> EMPTIES)
	{
		public static final Codec<OtherTemplates> CODEC = RecordCodecBuilder.create(i->
				i.group(
						ResourceLocation.CODEC.listOf().fieldOf("corner_fillers").forGetter(s->s.CORNER_FILLERS),
						ResourceLocation.CODEC.listOf().fieldOf("empties").forGetter(s->s.EMPTIES)
				).apply(i, OtherTemplates::new));
	}
}
