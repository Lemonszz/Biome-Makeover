package party.lemons.biomemakeover.init;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.ComposterBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.tag.Tag;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.item.BMItem;
import party.lemons.biomemakeover.item.BMMusicDiskItem;
import party.lemons.biomemakeover.item.FakeItem;
import party.lemons.biomemakeover.util.RegistryHelper;

public class BMItems
{
	public static final FoodComponent GLOWSHROOM_SOUP_FOOD = new FoodComponent.Builder().alwaysEdible().saturationModifier(0.6F).hunger(5).statusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 1200, 0), 1).statusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 1200, 0), 1).build();
	public static final FoodComponent GLOWFISH_FOOD = new FoodComponent.Builder().alwaysEdible().saturationModifier(0.1F).hunger(1).statusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 200, 0), 0.5F).statusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 200, 0), 0.5F).build();
	public static final FoodComponent COOKED_GLOWFISH_FOOD = new FoodComponent.Builder().alwaysEdible().saturationModifier(0.6F).hunger(5).statusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 200, 0), 0.5F).statusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 200, 0), 0.5F).build();

	public static final BMMusicDiskItem BUTTON_MUSHROOMS_MUSIC_DISK = new BMMusicDiskItem(14, BMEffects.BUTTON_MUSHROOMS, settings().rarity(Rarity.RARE));
	public static final MushroomStewItem GLOWSHROOM_STEW = new MushroomStewItem(settings().maxCount(1).recipeRemainder(Items.BOWL).food(GLOWSHROOM_SOUP_FOOD));
	public static final Item GLOWFISH_BUCKET = new FishBucketItem(BMEntities.GLOWFISH, Fluids.WATER, settings().maxCount(1));
	public static final Item GLOWFISH = new Item(settings().food(GLOWFISH_FOOD));
	public static final Item COOKED_GLOWFISH = new Item(settings().food(COOKED_GLOWFISH_FOOD));

	public static final Item GLOWFISH_SPAWN_EGG = new SpawnEggItem(BMEntities.GLOWFISH, 0xff7b00, 0xffd7b3, settings());
	public static final Item MUSHROOM_TRADER_SPAWN_EGG = new SpawnEggItem(BMEntities.MUSHROOM_TRADER, 0x37ff00, 0xb1ff9c, settings());
	public static final Item BLIGHTBAT_SPAWN_EGG = new SpawnEggItem(BMEntities.BLIGHTBAT, 0xae00ff, 0xdf9ffc, settings());
	public static final Item ICON_ITEM = new FakeItem();

	public static void init()
	{
		RegistryHelper.register(Registry.ITEM, Item.class, BMItems.class);

		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.PURPLE_GLOWSHROOM.asItem(), 0.70F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.ORANGE_GLOWSHROOM.asItem(), 0.70F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.GREEN_GLOWSHROOM.asItem(), 0.70F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.BLIGHTED_BALSA_SAPLING.asItem(), 0.4F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.PURPLE_GLOWSHROOM_BLOCK.asItem(), 0.9F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.GREEN_GLOWSHROOM_BLOCK.asItem(), 0.9F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.ORANGE_GLOWSHROOM_BLOCK.asItem(), 0.9F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.GLOWSHROOM_STEM.asItem(), 0.7F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.MYCELIUM_ROOTS.asItem(), 0.7F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.MYCELIUM_SPROUTS.asItem(), 0.7F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.TALL_BROWN_MUSHROOM.asItem(), 0.7F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.TALL_RED_MUSHROOM.asItem(), 0.7F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.BLIGHTED_BALSA_LEAVES.asItem(), 0.3F);
	}

	public static Item.Settings settings()
	{
		return new Item.Settings().group(BiomeMakeover.GROUP);
	}

	public static final Tag<Item> MUSHROOM_FIELDS = TagRegistry.item(BiomeMakeover.ID("mushroom_fields"));
}
