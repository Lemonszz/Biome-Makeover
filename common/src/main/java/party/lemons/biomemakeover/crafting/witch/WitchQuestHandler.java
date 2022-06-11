package party.lemons.biomemakeover.crafting.witch;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import dev.architectury.networking.NetworkManager;
import io.netty.buffer.Unpooled;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ai.behavior.ShufflingList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import party.lemons.biomemakeover.crafting.witch.data.QuestCategories;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMItems;
import party.lemons.biomemakeover.network.S2C_HandleWitchQuests;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class WitchQuestHandler
{
    public static ItemStack getRewardFor(WitchQuest quest, RandomSource random)
    {
        QuestRarity rarity = QuestRarity.getRarityFromQuest(quest);

        return rarity.rewards.sample().pickRandom(random);
    }

    public static WitchQuest createQuest(RandomSource random)
    {
        List<Integer> counts = ITEM_COUNT_SELECTOR.shuffle().stream().toList();

        int count = counts.get(random.nextInt(counts.size()));
        List<QuestItem> questItems = Lists.newArrayList();

        int safetyCount = count * 2;    //If there's not enough items to select, it infinite loops

        while(questItems.size() < count && safetyCount > 0)
        {
            safetyCount--;
            QuestCategory category = QuestCategories.choose();
            List<QuestItem> itemPool = category.getRequestedItemPool();

            QuestItem item = itemPool.get(random.nextInt(itemPool.size()));
            if(!questItems.contains(item)) questItems.add(item);
        }

        WitchQuest quest = new WitchQuest(random, questItems);

        return quest;
    }

    public static void sendQuests(Player player, int index, WitchQuestList quests)
    {
        if(player.level.isClientSide()) return;

        new S2C_HandleWitchQuests(index, quests).sendTo((ServerPlayer) player);
    }

    private static final ShufflingList<Integer> ITEM_COUNT_SELECTOR = new ShufflingList<>();

    static
    {
        ITEM_COUNT_SELECTOR.add(1, 5);
        ITEM_COUNT_SELECTOR.add(2, 8);
        ITEM_COUNT_SELECTOR.add(3, 4);
        ITEM_COUNT_SELECTOR.add(4, 3);
        ITEM_COUNT_SELECTOR.add(5, 1);
    }
}