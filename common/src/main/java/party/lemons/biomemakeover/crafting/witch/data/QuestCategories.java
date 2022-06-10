package party.lemons.biomemakeover.crafting.witch.data;

import party.lemons.biomemakeover.crafting.witch.QuestCategory;
import party.lemons.biomemakeover.util.WeightedList;

public class QuestCategories
{
	private static final WeightedList<QuestCategory> CATEGORIES = new WeightedList<>();

	public static void clearCategories()
	{
		CATEGORIES.clear();
	}

	public static void addCategory(QuestCategory category)
	{
		CATEGORIES.add(category, category.getWeight());
	}

	public static QuestCategory choose()
	{
		return CATEGORIES.sample();
	}

	public static boolean hasQuests()
	{
		return !CATEGORIES.isEmpty();
	}
}
