package party.lemons.biomemakeover.crafting.witch;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Rarity;
import party.lemons.taniwha.util.collections.WeightedList;

import java.util.Locale;

public enum QuestRarity
{
    COMMON(Rarity.COMMON, 0), UNCOMMON(Rarity.UNCOMMON, 8), RARE(Rarity.RARE, 15), EPIC(Rarity.EPIC, 30);

    private final int requiredPoints;
    private final Rarity vanillaRarity;

    QuestRarity(Rarity rarity, int requiredPoints)
    {
        this.requiredPoints = requiredPoints;
        this.vanillaRarity = rarity;
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