package party.lemons.biomemakeover.crafting.witch;

import com.google.common.collect.Lists;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import party.lemons.biomemakeover.crafting.witch.data.QuestCategories;
import party.lemons.biomemakeover.network.S2C_HandleWitchQuests;
import party.lemons.taniwha.util.collections.WeightedList;

import java.util.List;

public class WitchQuestHandler
{
    public static ItemStack getRewardFor(WitchQuest quest, RandomSource random)
    {
        QuestRarity rarity = QuestRarity.getRarityFromQuest(quest);

        return rarity.rewards.sample(random).pickRandom(random);
    }

    public static WitchQuest createQuest(RandomSource random)
    {
        int count = ITEM_COUNT_SELECTOR.sample(random);
        List<QuestItem> questItems = Lists.newArrayList();

        int safetyCount = count * 2;    //If there's not enough items to select, it infinite loops

        while(questItems.size() < count && safetyCount > 0)
        {
            safetyCount--;
            QuestCategory category = QuestCategories.choose(random);
            List<QuestItem> itemPool = category.getRequestedItemPool();

            QuestItem item = itemPool.get(random.nextInt(itemPool.size()));
            if(!questItems.contains(item)) questItems.add(item);
        }

        return new WitchQuest(random, questItems);
    }

    public static void sendQuests(Player player, int index, WitchQuestList quests)
    {
        if(player.level.isClientSide()) return;

        new S2C_HandleWitchQuests(index, quests).sendTo((ServerPlayer) player);
    }

    private static final WeightedList<Integer> ITEM_COUNT_SELECTOR = new WeightedList<>();

    static
    {
        ITEM_COUNT_SELECTOR.add(1, 5);
        ITEM_COUNT_SELECTOR.add(2, 8);
        ITEM_COUNT_SELECTOR.add(3, 4);
        ITEM_COUNT_SELECTOR.add(4, 3);
        ITEM_COUNT_SELECTOR.add(5, 1);
    }
}