package party.lemons.biomemakeover.crafting.witch;

import com.google.common.collect.Lists;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.block.Blocks;
import party.lemons.biomemakeover.init.BMItems;
import party.lemons.biomemakeover.init.BMPotions;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.List;
import java.util.Random;

public enum QuestRewardTable
{
    ITEMS(new LazyLoadedValue<>(QuestRewardTable::createItemTable)),
    POTION_INGREDIENTS(new LazyLoadedValue<>(QuestRewardTable::createPotionIngredientTable)),
    POTION(new LazyLoadedValue<>(QuestRewardTable::createPotionTable)),
    COMBO_POTION(new LazyLoadedValue<>(QuestRewardTable::createMultiPotionTable));

    private final LazyLoadedValue<List<RewardItem>> itemTable;

    QuestRewardTable(LazyLoadedValue<List<RewardItem>> itemTable)
    {
        this.itemTable = itemTable;
    }

    public ItemStack pickRandom(RandomSource random)
    {
        List<RewardItem> rewards = itemTable.get();
        return rewards.get(random.nextInt(rewards.size())).getRewardStack().copy();
    }

    private static List<RewardItem> createItemTable()
    {
        return Lists.newArrayList(
                new ItemStackRewardItem(new ItemStack(Items.GLASS_BOTTLE), 1, 10),
                new ItemStackRewardItem(new ItemStack(Items.GLOWSTONE_DUST), 4, 8),
                new ItemStackRewardItem(new ItemStack(Items.GUNPOWDER), 2, 10),
                new ItemStackRewardItem(new ItemStack(Items.REDSTONE), 7, 15),
                new ItemStackRewardItem(new ItemStack(Items.SPIDER_EYE), 5, 10),
                new ItemStackRewardItem(new ItemStack(Items.SUGAR), 8, 10),
                new ItemStackRewardItem(new ItemStack(Items.STICK), 10, 20),
                new ItemStackRewardItem(new ItemStack(Items.EMERALD), 1, 4),
                new ItemStackRewardItem(new ItemStack(Items.LAPIS_LAZULI), 1, 10),
                new ItemStackRewardItem(new ItemStack(Blocks.BROWN_MUSHROOM), 3, 5),
                new ItemStackRewardItem(new ItemStack(Blocks.RED_MUSHROOM), 3, 5),
                new ItemStackRewardItem(new ItemStack(Blocks.CAULDRON), 1, 5),
                new ItemStackRewardItem(new ItemStack(BMItems.ILLUNITE_SHARD.get()), 1, 10)
        );
    }


    private static List<RewardItem> createPotionIngredientTable()
    {
        return Lists.newArrayList(new ItemStackRewardItem(new ItemStack(Items.FERMENTED_SPIDER_EYE), 6, 20), new ItemStackRewardItem(new ItemStack(Items.REDSTONE), 12, 32), new ItemStackRewardItem(new ItemStack(Items.GLOWSTONE_DUST), 5, 25), new ItemStackRewardItem(new ItemStack(Items.GUNPOWDER), 5, 25), new ItemStackRewardItem(new ItemStack(Items.SPIDER_EYE), 5, 32), new ItemStackRewardItem(new ItemStack(Items.GLISTERING_MELON_SLICE), 5, 15), new ItemStackRewardItem(new ItemStack(BMItems.SOUL_EMBERS.get()), 3, 5), new ItemStackRewardItem(new ItemStack(Items.GHAST_TEAR), 1, 10), new ItemStackRewardItem(new ItemStack(Items.RABBIT_FOOT), 1, 20));
    }

    private static List<RewardItem> createPotionTable()
    {
        return Lists.newArrayList(new PotionRewardItem(Potions.NIGHT_VISION), new PotionRewardItem(Potions.LONG_NIGHT_VISION), new PotionRewardItem(Potions.INVISIBILITY), new PotionRewardItem(Potions.LONG_INVISIBILITY), new PotionRewardItem(Potions.LEAPING), new PotionRewardItem(Potions.LONG_LEAPING), new PotionRewardItem(Potions.STRONG_LEAPING), new PotionRewardItem(Potions.FIRE_RESISTANCE), new PotionRewardItem(Potions.LONG_FIRE_RESISTANCE), new PotionRewardItem(Potions.SWIFTNESS), new PotionRewardItem(Potions.LONG_SWIFTNESS), new PotionRewardItem(Potions.STRONG_SWIFTNESS), new PotionRewardItem(Potions.SLOWNESS), new PotionRewardItem(Potions.LONG_SLOWNESS), new PotionRewardItem(Potions.STRONG_SLOWNESS), new PotionRewardItem(Potions.TURTLE_MASTER), new PotionRewardItem(Potions.LONG_TURTLE_MASTER), new PotionRewardItem(Potions.STRONG_TURTLE_MASTER), new PotionRewardItem(Potions.WATER_BREATHING), new PotionRewardItem(Potions.LONG_WATER_BREATHING), new PotionRewardItem(Potions.HEALING), new PotionRewardItem(Potions.STRONG_HEALING), new PotionRewardItem(Potions.HARMING), new PotionRewardItem(Potions.STRONG_HARMING), new PotionRewardItem(Potions.POISON), new PotionRewardItem(Potions.LONG_POISON), new PotionRewardItem(Potions.STRONG_POISON), new PotionRewardItem(Potions.REGENERATION), new PotionRewardItem(Potions.LONG_REGENERATION), new PotionRewardItem(Potions.STRONG_REGENERATION), new PotionRewardItem(Potions.STRENGTH), new PotionRewardItem(Potions.LONG_STRENGTH), new PotionRewardItem(Potions.STRONG_STRENGTH), new PotionRewardItem(Potions.WEAKNESS), new PotionRewardItem(Potions.LONG_WEAKNESS), new PotionRewardItem(Potions.LUCK), new PotionRewardItem(Potions.SLOW_FALLING), new PotionRewardItem(Potions.LONG_SLOW_FALLING), new PotionRewardItem(BMPotions.ANTIDOTE_POT.get()), new ItemStackRewardItem(new ItemStack(BMItems.SWAMP_JIVES_MUSIC_DISK.get()), 1, 1));
    }

    private static List<RewardItem> createMultiPotionTable()
    {
        return Lists.newArrayList(new PotionRewardItem(BMPotions.ADRENALINE.get()), new PotionRewardItem(BMPotions.ASSASSIN.get()), new PotionRewardItem(BMPotions.DARKNESS.get()), new PotionRewardItem(BMPotions.DOLPHIN_MASTER.get()), new PotionRewardItem(BMPotions.LIQUID_BREAD.get()), new PotionRewardItem(BMPotions.PHANTOM_SPIRIT.get()), new PotionRewardItem(BMPotions.LIGHT_FOOTED.get()), new PotionRewardItem(BMPotions.MINER.get()));
    }

    private record ItemStackRewardItem(ItemStack stack, int quantityMin, int quantityMax) implements RewardItem {

        public ItemStack getRewardStack() {
            ItemStack reward = stack.copy();
            reward.setCount(RandomUtil.randomRange(quantityMin, quantityMax));

            return reward;
        }
    }

    private record PotionRewardItem(Potion potion) implements RewardItem
    {

        @Override
        public ItemStack getRewardStack() {
            Item it = Items.POTION;
            if (RandomUtil.RANDOM.nextInt(4) == 0) if (RandomUtil.RANDOM.nextInt(3) == 0) {
                it = Items.LINGERING_POTION;
            } else {
                it = Items.SPLASH_POTION;
            }
            return PotionUtils.setPotion(new ItemStack(it), potion);
        }
    }

    private interface RewardItem
    {
        ItemStack getRewardStack();
    }
}