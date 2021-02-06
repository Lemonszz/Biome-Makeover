package party.lemons.biomemakeover.crafting.witch;

import net.minecraft.util.collection.WeightedList;

import java.util.Random;

public enum QuestCategory
{
	COMMON(10), RARE(1), MUSHROOM(2), FLOWER(5), MESA(4), SWAMP(7), OCEAN(9), JUNGLE(4), NETHER(5);

	public final int weight;

	QuestCategory(int weight)
	{
		this.weight = weight;
	}

	private static final WeightedList<QuestCategory> CATEGORIES = new WeightedList<>();

	static
	{
		for(QuestCategory category : QuestCategory.values())
		{
			CATEGORIES.add(category, category.weight);
		}
	}

	public static QuestCategory choose(Random random)
	{
		return CATEGORIES.pickRandom(random);
	}
}
