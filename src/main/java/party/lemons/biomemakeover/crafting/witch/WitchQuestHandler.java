package party.lemons.biomemakeover.crafting.witch;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.Blocks;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.collection.WeightedList;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMItems;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class WitchQuestHandler
{
	private static final Map<QuestCategory, List<QuestItem>> QUEST_ITEMS = Maps.newHashMap();

	public static void init()
	{
		addQuestItem(QuestCategory.MUSHROOM, QuestItem.of(BMItems.GLOWFISH, 10, 3));
		addQuestItem(QuestCategory.MUSHROOM, QuestItem.of(BMBlocks.GLOWSHROOM_STEM, 15, 3));
		addQuestItem(QuestCategory.MUSHROOM, QuestItem.of(BMBlocks.GREEN_GLOWSHROOM_BLOCK, 15, 3));
		addQuestItem(QuestCategory.MUSHROOM, QuestItem.of(BMBlocks.ORANGE_GLOWSHROOM_BLOCK, 20, 3));
		addQuestItem(QuestCategory.MUSHROOM, QuestItem.of(BMBlocks.PURPLE_GLOWSHROOM_BLOCK, 15, 3));
		addQuestItem(QuestCategory.MUSHROOM, QuestItem.of(BMBlocks.GREEN_GLOWSHROOM, 11, 1));
		addQuestItem(QuestCategory.MUSHROOM, QuestItem.of(BMBlocks.ORANGE_GLOWSHROOM, 11, 1));
		addQuestItem(QuestCategory.MUSHROOM, QuestItem.of(BMBlocks.PURPLE_GLOWSHROOM, 11, 1));
		addQuestItem(QuestCategory.MUSHROOM, QuestItem.of(BMBlocks.MYCELIUM_ROOTS, 5, 2));
		addQuestItem(QuestCategory.MUSHROOM, QuestItem.of(BMBlocks.MYCELIUM_SPROUTS, 5, 2));
		addQuestItem(QuestCategory.MUSHROOM, QuestItem.of(BMBlocks.TALL_BROWN_MUSHROOM, 5, 2));
		addQuestItem(QuestCategory.MUSHROOM, QuestItem.of(BMBlocks.TALL_RED_MUSHROOM, 5, 2));
		addQuestItem(QuestCategory.MUSHROOM, QuestItem.of(Blocks.MYCELIUM, 10, 1));

		addQuestItem(QuestCategory.MESA, QuestItem.of(BMItems.SCUTTLER_TAIL, 5, 5));
		addQuestItem(QuestCategory.MESA, QuestItem.of(BMItems.ECTOPLASM, 8, 3));
		addQuestItem(QuestCategory.MESA, QuestItem.of(BMItems.PINK_PETALS, 12, 1));
		addQuestItem(QuestCategory.MESA, QuestItem.of(BMBlocks.SAGUARO_CACTUS, 2, 10));

		addQuestItem(QuestCategory.FLOWER, QuestItem.of(Blocks.DANDELION, 1, 4));
		addQuestItem(QuestCategory.FLOWER, QuestItem.of(Blocks.POPPY, 1, 4));
		addQuestItem(QuestCategory.FLOWER, QuestItem.of(Blocks.BLUE_ORCHID, 1, 4));
		addQuestItem(QuestCategory.FLOWER, QuestItem.of(Blocks.ALLIUM, 1, 4));
		addQuestItem(QuestCategory.FLOWER, QuestItem.of(Blocks.AZURE_BLUET, 1, 4));
		addQuestItem(QuestCategory.FLOWER, QuestItem.of(Blocks.ORANGE_TULIP, 1, 4));
		addQuestItem(QuestCategory.FLOWER, QuestItem.of(Blocks.RED_TULIP, 1, 4));
		addQuestItem(QuestCategory.FLOWER, QuestItem.of(Blocks.PINK_TULIP, 1, 4));
		addQuestItem(QuestCategory.FLOWER, QuestItem.of(Blocks.WHITE_TULIP, 1, 4));
		addQuestItem(QuestCategory.FLOWER, QuestItem.of(Blocks.OXEYE_DAISY, 1, 4));
		addQuestItem(QuestCategory.FLOWER, QuestItem.of(Blocks.CORNFLOWER, 1, 4));
		addQuestItem(QuestCategory.FLOWER, QuestItem.of(Blocks.LILY_OF_THE_VALLEY, 1, 4));
		addQuestItem(QuestCategory.FLOWER, QuestItem.of(Blocks.SUNFLOWER, 4, 4));
		addQuestItem(QuestCategory.FLOWER, QuestItem.of(Blocks.LILAC, 1, 4));
		addQuestItem(QuestCategory.FLOWER, QuestItem.of(Blocks.ROSE_BUSH, 1, 4));
		addQuestItem(QuestCategory.FLOWER, QuestItem.of(Blocks.PEONY, 1, 4));

		addQuestItem(QuestCategory.SWAMP, QuestItem.of(BMBlocks.SMALL_LILY_PAD, 2, 4));
		addQuestItem(QuestCategory.SWAMP, QuestItem.of(BMBlocks.CATTAIL, 1, 4));
		addQuestItem(QuestCategory.SWAMP, QuestItem.of(BMBlocks.REED, 1, 4));
		addQuestItem(QuestCategory.SWAMP, QuestItem.of(BMBlocks.WILLOWING_BRANCHES, 1, 4));
		addQuestItem(QuestCategory.SWAMP, QuestItem.of(Blocks.LILY_PAD, 1, 4));
		addQuestItem(QuestCategory.SWAMP, QuestItem.of(Items.SLIME_BALL, 2, 4));

		addQuestItem(QuestCategory.OCEAN, QuestItem.of(Blocks.KELP, 1, 5));
		addQuestItem(QuestCategory.OCEAN, QuestItem.of(Blocks.SEAGRASS, 1, 5));
		addQuestItem(QuestCategory.OCEAN, QuestItem.of(Items.SALMON, 1, 2));
		addQuestItem(QuestCategory.OCEAN, QuestItem.of(Items.COD, 1, 2));
		addQuestItem(QuestCategory.OCEAN, QuestItem.of(Items.INK_SAC, 2, 4));
		addQuestItem(QuestCategory.OCEAN, QuestItem.of(Items.SEA_PICKLE, 4, 4));
		addQuestItem(QuestCategory.OCEAN, QuestItem.of(Items.TROPICAL_FISH, 2, 2));
		addQuestItem(QuestCategory.OCEAN, QuestItem.of(Items.PUFFERFISH, 2, 2));

		addQuestItem(QuestCategory.JUNGLE, QuestItem.of(Items.COCOA_BEANS, 0.5F, 10));
		addQuestItem(QuestCategory.JUNGLE, QuestItem.of(Blocks.VINE, 1, 4));
		addQuestItem(QuestCategory.JUNGLE, QuestItem.of(Blocks.MELON, 4, 4));
		addQuestItem(QuestCategory.JUNGLE, QuestItem.of(Items.GLISTERING_MELON_SLICE, 5, 2));
		addQuestItem(QuestCategory.JUNGLE, QuestItem.of(Items.MELON_SLICE, 1, 4));
		addQuestItem(QuestCategory.JUNGLE, QuestItem.of(Blocks.BAMBOO, 0.5F, 10));

		addQuestItem(QuestCategory.NETHER, QuestItem.of(Blocks.WARPED_FUNGUS, 3, 5));
		addQuestItem(QuestCategory.NETHER, QuestItem.of(Blocks.CRIMSON_FUNGUS, 3, 5));
		addQuestItem(QuestCategory.NETHER, QuestItem.of(Items.NETHER_WART, 1, 10));
		addQuestItem(QuestCategory.NETHER, QuestItem.of(Blocks.TWISTING_VINES, 1, 3));
		addQuestItem(QuestCategory.NETHER, QuestItem.of(Blocks.WEEPING_VINES, 1, 3));
		addQuestItem(QuestCategory.NETHER, QuestItem.of(Blocks.WARPED_ROOTS, 3, 3));
		addQuestItem(QuestCategory.NETHER, QuestItem.of(Blocks.CRIMSON_ROOTS, 3, 3));
		addQuestItem(QuestCategory.NETHER, QuestItem.of(Blocks.WARPED_WART_BLOCK, 6, 2));
		addQuestItem(QuestCategory.NETHER, QuestItem.of(Blocks.NETHER_WART_BLOCK, 4, 2));
		addQuestItem(QuestCategory.NETHER, QuestItem.of(Blocks.NETHER_SPROUTS, 2, 2));
		addQuestItem(QuestCategory.NETHER, QuestItem.of(Blocks.CRIMSON_NYLIUM, 4, 1));
		addQuestItem(QuestCategory.NETHER, QuestItem.of(Blocks.WARPED_NYLIUM, 4, 1));
		addQuestItem(QuestCategory.NETHER, QuestItem.of(Blocks.GLOWSTONE, 4, 3));
		addQuestItem(QuestCategory.NETHER, QuestItem.of(Items.BLAZE_ROD, 4, 3));
		addQuestItem(QuestCategory.NETHER, QuestItem.of(Items.BLAZE_POWDER, 3, 3));
		addQuestItem(QuestCategory.NETHER, QuestItem.of(Items.MAGMA_CREAM, 5, 3));
		addQuestItem(QuestCategory.NETHER, QuestItem.of(Items.GHAST_TEAR, 10, 2));
		addQuestItem(QuestCategory.NETHER, QuestItem.of(Blocks.SHROOMLIGHT, 5, 2));

		addQuestItem(QuestCategory.COMMON, QuestItem.of(Items.APPLE, 2, 4));
		addQuestItem(QuestCategory.COMMON, QuestItem.of(Items.WHEAT, 0.5F, 10));
		addQuestItem(QuestCategory.COMMON, QuestItem.of(Items.TURTLE_EGG, 10F, 1));
		addQuestItem(QuestCategory.COMMON, QuestItem.of(Items.TALL_GRASS, 0.5F, 4));
		addQuestItem(QuestCategory.COMMON, QuestItem.of(Items.SWEET_BERRIES, 0.5F, 4));
		addQuestItem(QuestCategory.COMMON, QuestItem.of(Items.SUGAR_CANE, 0.5F, 8));
		addQuestItem(QuestCategory.COMMON, QuestItem.of(Items.SUGAR, 0.5F, 8));
		addQuestItem(QuestCategory.COMMON, QuestItem.of(Items.PAPER, 0.5F, 8));
		addQuestItem(QuestCategory.COMMON, QuestItem.of(Items.REDSTONE, 2F, 8));
		addQuestItem(QuestCategory.COMMON, QuestItem.of(Items.GLOWSTONE, 2F, 2));
		addQuestItem(QuestCategory.COMMON, QuestItem.of(Items.SPIDER_EYE, 2F, 2));
		addQuestItem(QuestCategory.COMMON, QuestItem.of(Items.FERMENTED_SPIDER_EYE, 4F, 1));
		addQuestItem(QuestCategory.COMMON, QuestItem.of(Items.ENDER_PEARL, 2F, 5));
		addQuestItem(QuestCategory.COMMON, QuestItem.of(Items.SCUTE, 5F, 1));
		addQuestItem(QuestCategory.COMMON, QuestItem.of(Items.GUNPOWDER, 2F, 4));
		addQuestItem(QuestCategory.COMMON, QuestItem.of(Items.RABBIT_FOOT, 5F, 2));
		addQuestItem(QuestCategory.COMMON, QuestItem.of(Items.PHANTOM_MEMBRANE, 10F, 4));
		addQuestItem(QuestCategory.COMMON, QuestItem.of(Items.GOLDEN_CARROT, 5F, 4));
		addQuestItem(QuestCategory.COMMON, QuestItem.of(Items.HONEY_BOTTLE, 9F, 1));
		addQuestItem(QuestCategory.COMMON, QuestItem.of(Items.HONEYCOMB_BLOCK, 5F, 1));
		addQuestItem(QuestCategory.COMMON, QuestItem.of(Items.EGG, 0.5F, 5));
		addQuestItem(QuestCategory.COMMON, QuestItem.of(Items.POISONOUS_POTATO, 4F, 5));
		addQuestItem(QuestCategory.COMMON, QuestItem.of(Items.BONE, 0.4F, 10));
		addQuestItem(QuestCategory.COMMON, QuestItem.of(Items.LAPIS_LAZULI, 0.5F, 10));
		addQuestItem(QuestCategory.COMMON, QuestItem.of(Blocks.PUMPKIN, 4F, 4));
		addQuestItem(QuestCategory.COMMON, QuestItem.of(Blocks.LARGE_FERN, 1F, 4));
		addQuestItem(QuestCategory.COMMON, QuestItem.of(Blocks.GRASS, 1F, 4));
		addQuestItem(QuestCategory.COMMON, QuestItem.of(Blocks.CAULDRON, 4F, 1));

		addQuestItem(QuestCategory.RARE, QuestItem.of(Blocks.WITHER_ROSE, 10, 1));
		addQuestItem(QuestCategory.RARE, QuestItem.of(Items.TOTEM_OF_UNDYING, 15, 1));
		addQuestItem(QuestCategory.RARE, QuestItem.of(Blocks.WITHER_SKELETON_SKULL, 50, 1));
		addQuestItem(QuestCategory.RARE, QuestItem.of(Items.NETHER_STAR, 175, 1));
		addQuestItem(QuestCategory.RARE, QuestItem.of(Items.EXPERIENCE_BOTTLE, 10, 1));
		addQuestItem(QuestCategory.RARE, QuestItem.of(Items.ENCHANTED_GOLDEN_APPLE, 40, 1));
		addQuestItem(QuestCategory.RARE, QuestItem.of(Items.GOLDEN_APPLE, 5, 1));
		addQuestItem(QuestCategory.RARE, QuestItem.of(Items.CREEPER_HEAD, 30, 1));
		addQuestItem(QuestCategory.RARE, QuestItem.of(Items.SKELETON_SKULL, 30, 1));
		addQuestItem(QuestCategory.RARE, QuestItem.of(Items.ZOMBIE_HEAD, 30, 1));

		Random random = new Random();
		for(int i = 0; i < 100; i++)
		{
			WitchQuest quest = createQuest(random);
			System.out.println(quest);
			System.out.println("---");
		}
	}

	public static ItemStack getRewardFor(WitchQuest quest, Random random)
	{
		return ItemStack.EMPTY;
	}

	public static WitchQuest createQuest(Random random)
	{
		int count = ITEM_COUNT_SELECTOR.pickRandom(random);
		List<QuestItem> questItems = Lists.newArrayList();

		while(questItems.size() < count)
		{
			QuestCategory category = QuestCategory.choose(random);
			List<QuestItem> itemPool = QUEST_ITEMS.get(category);

			QuestItem item = itemPool.get(random.nextInt(itemPool.size()));
			if(!questItems.contains(item))
				questItems.add(item);
		}

		WitchQuest quest = new WitchQuest(random, questItems);

		return quest;
	}

	public static void addQuestItem(QuestCategory category, QuestItem questItem)
	{
		if(!QUEST_ITEMS.containsKey(category))
			QUEST_ITEMS.put(category, Lists.newArrayList());

		QUEST_ITEMS.get(category).add(questItem);
	}

	private static WeightedList<Integer> ITEM_COUNT_SELECTOR = new WeightedList<>();
	static {
		ITEM_COUNT_SELECTOR.add(1, 5);
		ITEM_COUNT_SELECTOR.add(2, 8);
		ITEM_COUNT_SELECTOR.add(3, 4);
		ITEM_COUNT_SELECTOR.add(4, 3);
		ITEM_COUNT_SELECTOR.add(5, 1);
	}
}
