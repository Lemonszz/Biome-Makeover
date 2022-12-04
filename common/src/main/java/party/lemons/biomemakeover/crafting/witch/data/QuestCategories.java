package party.lemons.biomemakeover.crafting.witch.data;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Items;
import party.lemons.biomemakeover.crafting.witch.QuestCategory;
import party.lemons.biomemakeover.crafting.witch.QuestItem;
import party.lemons.taniwha.util.collections.WeightedList;

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

	public static QuestCategory choose(RandomSource randomSource)
	{
		return CATEGORIES.sample(randomSource);
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
