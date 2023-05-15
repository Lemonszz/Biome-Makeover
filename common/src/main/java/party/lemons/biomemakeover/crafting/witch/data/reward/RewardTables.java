package party.lemons.biomemakeover.crafting.witch.data.reward;

import com.google.common.collect.Maps;
import net.minecraft.util.RandomSource;
import party.lemons.biomemakeover.crafting.witch.QuestRarity;
import party.lemons.taniwha.util.collections.WeightedList;

import java.util.EnumMap;

public class RewardTables
{
	public static final EnumMap<QuestRarity, WeightedList<RewardTable>> tables = Maps.newEnumMap(QuestRarity.class);

	public static RewardTable getTable(QuestRarity rarity, RandomSource randomSource)
	{
		return tables.get(rarity).sample(randomSource);
	}

	public static void addTable(RewardTable table)
	{
		WeightedList<RewardTable> common = tables.getOrDefault(QuestRarity.COMMON, new WeightedList<>());
		WeightedList<RewardTable> uncommon = tables.getOrDefault(QuestRarity.UNCOMMON, new WeightedList<>());
		WeightedList<RewardTable> rare = tables.getOrDefault(QuestRarity.RARE, new WeightedList<>());
		WeightedList<RewardTable> epic = tables.getOrDefault(QuestRarity.EPIC, new WeightedList<>());

		common.add(table, table.weights().common());
		uncommon.add(table, table.weights().uncommon());
		rare.add(table, table.weights().rare());
		epic.add(table, table.weights().epic());

		tables.put(QuestRarity.COMMON, common);
		tables.put(QuestRarity.UNCOMMON, uncommon);
		tables.put(QuestRarity.RARE, rare);
		tables.put(QuestRarity.EPIC, epic);

	}

	public static void clear()
	{
		tables.clear();
	}
}
