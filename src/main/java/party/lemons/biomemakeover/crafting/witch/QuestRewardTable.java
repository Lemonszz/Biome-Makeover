package party.lemons.biomemakeover.crafting.witch;

import com.google.common.collect.Lists;
import net.minecraft.block.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.util.Lazy;
import party.lemons.biomemakeover.init.BMPotions;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.List;
import java.util.Random;

public enum QuestRewardTable
{
	ITEMS(new Lazy<>(QuestRewardTable::createItemTable)),
	POTION_INGREDIENTS(new Lazy<>(QuestRewardTable::createPotionIngredientTable)),
	POTION(new Lazy<>(QuestRewardTable::createPotionTable)),
	COMBO_POTION(new Lazy<>(QuestRewardTable::createMultiPotionTable));

	private final Lazy<List<RewardItem>> itemTable;
	QuestRewardTable(Lazy<List<RewardItem>> itemTable)
	{
		this.itemTable = itemTable;
	}

	public ItemStack pickRandom(Random random)
	{
		List<RewardItem> rewards = itemTable.get();
		return rewards.get(random.nextInt(rewards.size())).getRewardStack().copy();
	}

	private static List<RewardItem> createItemTable()
	{
		return Lists.newArrayList(
			new ItemStackRewardItem(new ItemStack(Items.GLASS_BOTTLE),1, 10),
			new ItemStackRewardItem(new ItemStack(Items.GLOWSTONE_DUST),4, 8),
			new ItemStackRewardItem(new ItemStack(Items.GUNPOWDER),2, 10),
			new ItemStackRewardItem(new ItemStack(Items.REDSTONE),7, 15),
			new ItemStackRewardItem(new ItemStack(Items.SPIDER_EYE),5, 10),
			new ItemStackRewardItem(new ItemStack(Items.SUGAR),8, 10),
			new ItemStackRewardItem(new ItemStack(Items.STICK),10, 20),
			new ItemStackRewardItem(new ItemStack(Items.EMERALD),1, 4),
			new ItemStackRewardItem(new ItemStack(Items.LAPIS_LAZULI),1, 10),
			new ItemStackRewardItem(new ItemStack(Blocks.BROWN_MUSHROOM),3, 5),
			new ItemStackRewardItem(new ItemStack(Blocks.RED_MUSHROOM),3, 5),
			new ItemStackRewardItem(new ItemStack(Blocks.CAULDRON),1, 5)
		);
	}

	private static List<RewardItem> createPotionIngredientTable()
	{
		return Lists.newArrayList(
				new ItemStackRewardItem(new ItemStack(Items.FERMENTED_SPIDER_EYE), 6, 20),
				new ItemStackRewardItem(new ItemStack(Items.REDSTONE), 12, 32),
				new ItemStackRewardItem(new ItemStack(Items.GLOWSTONE_DUST), 5, 25),
				new ItemStackRewardItem(new ItemStack(Items.GUNPOWDER), 5, 25),
				new ItemStackRewardItem(new ItemStack(Items.SPIDER_EYE), 5, 32),
				new ItemStackRewardItem(new ItemStack(Items.GLISTERING_MELON_SLICE), 5, 15),
				new ItemStackRewardItem(new ItemStack(Items.BLAZE_POWDER), 3, 25),
				new ItemStackRewardItem(new ItemStack(Items.GHAST_TEAR), 1, 10),
				new ItemStackRewardItem(new ItemStack(Items.RABBIT_FOOT), 1, 20)
		);
	}

	private static List<RewardItem> createPotionTable()
	{
		List<RewardItem> rewards = Lists.newArrayList(
			new PotionRewardItem(Potions.NIGHT_VISION),
			new PotionRewardItem(Potions.LONG_NIGHT_VISION),
			new PotionRewardItem(Potions.INVISIBILITY),
			new PotionRewardItem(Potions.LONG_INVISIBILITY),
			new PotionRewardItem(Potions.LEAPING),
			new PotionRewardItem(Potions.LONG_LEAPING),
			new PotionRewardItem(Potions.STRONG_LEAPING),
			new PotionRewardItem(Potions.FIRE_RESISTANCE),
			new PotionRewardItem(Potions.LONG_FIRE_RESISTANCE),
			new PotionRewardItem(Potions.SWIFTNESS),
			new PotionRewardItem(Potions.LONG_SWIFTNESS),
			new PotionRewardItem(Potions.STRONG_SWIFTNESS),
			new PotionRewardItem(Potions.SLOWNESS),
			new PotionRewardItem(Potions.LONG_SLOWNESS),
			new PotionRewardItem(Potions.STRONG_SLOWNESS),
			new PotionRewardItem(Potions.TURTLE_MASTER),
			new PotionRewardItem(Potions.LONG_TURTLE_MASTER),
			new PotionRewardItem(Potions.STRONG_TURTLE_MASTER),
			new PotionRewardItem(Potions.WATER_BREATHING),
			new PotionRewardItem(Potions.LONG_WATER_BREATHING),
			new PotionRewardItem(Potions.HEALING),
			new PotionRewardItem(Potions.STRONG_HEALING),
			new PotionRewardItem(Potions.HARMING),
			new PotionRewardItem(Potions.STRONG_HARMING),
			new PotionRewardItem(Potions.POISON),
			new PotionRewardItem(Potions.LONG_POISON),
			new PotionRewardItem(Potions.STRONG_POISON),
			new PotionRewardItem(Potions.REGENERATION),
			new PotionRewardItem(Potions.LONG_REGENERATION),
			new PotionRewardItem(Potions.STRONG_REGENERATION),
			new PotionRewardItem(Potions.STRENGTH),
			new PotionRewardItem(Potions.LONG_STRENGTH),
			new PotionRewardItem(Potions.STRONG_STRENGTH),
			new PotionRewardItem(Potions.WEAKNESS),
			new PotionRewardItem(Potions.LONG_WEAKNESS),
			new PotionRewardItem(Potions.LUCK),
			new PotionRewardItem(Potions.SLOW_FALLING),
			new PotionRewardItem(Potions.LONG_SLOW_FALLING)
		);
		return rewards;
	}

	private static List<RewardItem> createMultiPotionTable()
	{
		return Lists.newArrayList(
				new PotionRewardItem(BMPotions.ADRENALINE),
				new PotionRewardItem(BMPotions.ASSASSIN),
				new PotionRewardItem(BMPotions.DARKNESS),
				new PotionRewardItem(BMPotions.DOLPHIN_MASTER),
				new PotionRewardItem(BMPotions.LIQUID_BREAD),
				new PotionRewardItem(BMPotions.PHANTOM_SPIRIT),
				new PotionRewardItem(BMPotions.LIGHT_FOOTED),
				new PotionRewardItem(BMPotions.MINER)
		);
	}

	private static class ItemStackRewardItem implements RewardItem
	{
		private final ItemStack stack;
		private final int quantityMin, quantityMax;

		public ItemStackRewardItem(ItemStack stack, int quantityMin, int quantityMax)
		{
			this.stack = stack;
			this.quantityMin = quantityMin;
			this.quantityMax = quantityMax;
		}

		public ItemStack getRewardStack()
		{
			ItemStack reward = stack.copy();
			reward.setCount(RandomUtil.randomRange(quantityMin, quantityMax));

			return reward;
		}
	}

	private static class PotionRewardItem implements RewardItem
	{
		private final Potion potion;

		public PotionRewardItem(Potion potion)
		{
			this.potion = potion;
		}

		@Override
		public ItemStack getRewardStack()
		{
			Item it = Items.POTION;
			if(RandomUtil.RANDOM.nextInt(4) == 0)
				if(RandomUtil.RANDOM.nextInt(3) == 0)
				{
					it = Items.LINGERING_POTION;
				}
				else
				{
					it = Items.SPLASH_POTION;
				}
			return PotionUtil.setPotion(new ItemStack(it), potion);
		}
	}

	private interface RewardItem
	{
		ItemStack getRewardStack();
	}
}
