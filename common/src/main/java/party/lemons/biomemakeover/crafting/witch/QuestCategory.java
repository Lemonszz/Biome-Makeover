package party.lemons.biomemakeover.crafting.witch;

import net.minecraft.world.entity.ai.behavior.ShufflingList;

import java.util.Random;
import java.util.stream.Collectors;

public enum QuestCategory
{
    COMMON(10), RARE(1), MUSHROOM(2), FLOWER(5), MESA(4), SWAMP(7), OCEAN(9), JUNGLE(4), NETHER(5), DARK_FOREST(7);

    public final int weight;

    QuestCategory(int weight)
    {
        this.weight = weight;
    }

    private static final ShufflingList<QuestCategory> CATEGORIES = new ShufflingList<>();

    static
    {
        for(QuestCategory category : QuestCategory.values())
        {
            CATEGORIES.add(category, category.weight);
        }
    }

    public static QuestCategory choose(Random random)
    {
        return CATEGORIES.shuffle().stream().toList().get(random.nextInt(values().length));
    }
}