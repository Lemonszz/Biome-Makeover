package party.lemons.biomemakeover.crafting.witch;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Rarity;
import party.lemons.taniwha.util.collections.WeightedList;

import java.util.Locale;

public enum QuestRarity
{
    COMMON(Rarity.COMMON, 0, new int[]{110, 150, 10, 1}), UNCOMMON(Rarity.UNCOMMON, 8, new int[]{90, 120, 75, 10}), RARE(Rarity.RARE, 15, new int[]{0, 0, 100, 50}), EPIC(Rarity.EPIC, 30, new int[]{0, 0, 0, 100});

    private final int requiredPoints;
    private final Rarity vanillaRarity;
    public final WeightedList<QuestRewardTable> rewards;

    QuestRarity(Rarity rarity, int requiredPoints, int[] rewardWeights)
    {
        this.requiredPoints = requiredPoints;
        this.vanillaRarity = rarity;

        rewards = new WeightedList<>();
        rewards.add(QuestRewardTable.ITEMS, rewardWeights[0]);
        rewards.add(QuestRewardTable.POTION_INGREDIENTS, rewardWeights[1]);
        rewards.add(QuestRewardTable.POTION, rewardWeights[2]);
        rewards.add(QuestRewardTable.COMBO_POTION, rewardWeights[3]);
    }

    public Component getTooltipText()
    {
        return Component.translatable("tooltip." + name().toLowerCase(Locale.ROOT)).withStyle(vanillaRarity.color);
    }

    public static QuestRarity getRarityFromPoints(float points)
    {
        for(int i = values().length - 1; i >= 0; i--)
        {
            if(points >= values()[i].requiredPoints) return values()[i];
        }
        return COMMON;
    }

    public static QuestRarity getRarityFromQuest(WitchQuest quest)
    {
        return getRarityFromPoints(quest.getPoints());
    }
}