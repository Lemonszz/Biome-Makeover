package party.lemons.biomemakeover.crafting.witch;

import com.google.common.collect.Lists;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.behavior.ShufflingList;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public class QuestCategory
{
    private final int weight;
    private List<QuestItem> requestedItems = Lists.newArrayList();

    public QuestCategory(int weight)
    {
        this.weight = weight;
    }

    public int getWeight()
    {
        return weight;
    }

    public void addItem(QuestItem item)
    {
        this.requestedItems.add(item);
    }

    public List<QuestItem> getRequestedItemPool()
    {
        return requestedItems;
    }

    public boolean isEmpty()
    {
        return requestedItems.isEmpty();
    }
}