package party.lemons.biomemakeover.level.feature.mansion;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.init.BMEntities;

import java.util.List;

public record MansionDetails(Loot loot, Mobs mobs)
{
	public static final Codec<MansionDetails> CODEC = RecordCodecBuilder.create(i->
			i.group(
					Loot.CODEC.fieldOf("loot").forGetter(MansionDetails::loot),
					Mobs.CODEC.fieldOf("mobs").forGetter(MansionDetails::mobs)
			).apply(i, MansionDetails::new));

	public record Loot(ResourceLocation arrow, ResourceLocation dungeon_junk, ResourceLocation dungeon_standard, ResourceLocation dungeonGood, ResourceLocation junk, ResourceLocation standard, ResourceLocation good)
	{
		public static final Codec<Loot> CODEC = RecordCodecBuilder.create(i->
				i.group(
						ResourceLocation.CODEC.optionalFieldOf("arrow", BiomeMakeover.ID("mansion/arrows")).forGetter(s->s.arrow),
						ResourceLocation.CODEC.optionalFieldOf("dungeon_junk", BiomeMakeover.ID("mansion/dungeon_junk")).forGetter(s->s.dungeon_junk),
						ResourceLocation.CODEC.optionalFieldOf("dungeon_standard", BiomeMakeover.ID("mansion/dungeon")).forGetter(s->s.dungeon_standard),
						ResourceLocation.CODEC.optionalFieldOf("dungeon_good", BiomeMakeover.ID("mansion/dungeon_good")).forGetter(s->s.dungeonGood),
						ResourceLocation.CODEC.optionalFieldOf("junk", BiomeMakeover.ID("mansion/junk")).forGetter(s->s.junk),
						ResourceLocation.CODEC.optionalFieldOf("standard", BiomeMakeover.ID("mansion/standard")).forGetter(s->s.standard),
						ResourceLocation.CODEC.optionalFieldOf("good",  BiomeMakeover.ID("mansion/good")).forGetter(s->s.good)
				).apply(i, Loot::new));
	}

	public record Mobs(List<EntityType<?>> enemies, List<EntityType<?>> ranged_enemies, List<EntityType<?>> golem_enemies, List<EntityType<?>> ravagers, List<EntityType<?>> cow, List<EntityType<?>> allays)
	{
		public static final Codec<Mobs> CODEC = RecordCodecBuilder.create(i->
				i.group(
						BuiltInRegistries.ENTITY_TYPE.byNameCodec().listOf().optionalFieldOf("enemies", Lists.newArrayList(EntityType.VINDICATOR, EntityType.EVOKER, EntityType.PILLAGER)).forGetter(e->e.enemies),
						BuiltInRegistries.ENTITY_TYPE.byNameCodec().listOf().optionalFieldOf("ranged_enemies", Lists.newArrayList(EntityType.PILLAGER)).forGetter(e->e.ranged_enemies),
						BuiltInRegistries.ENTITY_TYPE.byNameCodec().listOf().optionalFieldOf("golem_enemies", Lists.newArrayList(BMEntities.STONE_GOLEM.get())).forGetter(e->e.golem_enemies),
						BuiltInRegistries.ENTITY_TYPE.byNameCodec().listOf().optionalFieldOf("ravagers", Lists.newArrayList(EntityType.RAVAGER)).forGetter(e->e.ravagers),
						BuiltInRegistries.ENTITY_TYPE.byNameCodec().listOf().optionalFieldOf("cow", Lists.newArrayList(EntityType.COW)).forGetter(e->e.cow),
						BuiltInRegistries.ENTITY_TYPE.byNameCodec().listOf().optionalFieldOf("allays", Lists.newArrayList(EntityType.ALLAY)).forGetter(e->e.allays)
				).apply(i, Mobs::new));
	}
}
