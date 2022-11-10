package party.lemons.biomemakeover.crafting.witch.data;

import net.minecraft.world.item.Items;
import party.lemons.biomemakeover.crafting.witch.QuestCategory;
import party.lemons.biomemakeover.crafting.witch.QuestItem;
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

	public static void addSafety()
	{
		if(CATEGORIES.isEmpty()) {
			QuestCategory category = new QuestCategory(1);
			category.addItem(QuestItem.of(Items.DIAMOND, 10, 10));
			addCategory(category);
		}
	}
}
