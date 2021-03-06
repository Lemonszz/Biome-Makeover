package party.lemons.biomemakeover.init;

import net.fabricmc.fabric.api.tag.TagRegistry;
import net.minecraft.block.ComposterBlock;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.*;
import net.minecraft.tag.Tag;
import net.minecraft.util.Rarity;
import net.minecraft.util.registry.Registry;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.item.*;
import party.lemons.biomemakeover.util.RegistryHelper;

public class BMItems
{
	public static final FoodComponent GLOWSHROOM_SOUP_FOOD = new FoodComponent.Builder().alwaysEdible().saturationModifier(0.6F).hunger(5).statusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 1200, 0), 1).statusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 1200, 0), 1).build();
	public static final FoodComponent GLOWFISH_FOOD = new FoodComponent.Builder().alwaysEdible().saturationModifier(0.1F).hunger(1).statusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 200, 0), 0.5F).statusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 200, 0), 0.5F).build();
	public static final FoodComponent COOKED_GLOWFISH_FOOD = new FoodComponent.Builder().alwaysEdible().saturationModifier(0.6F).hunger(5).statusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 200, 0), 0.5F).statusEffect(new StatusEffectInstance(StatusEffects.GLOWING, 200, 0), 0.5F).build();
	public static final FoodComponent COOKED_TOAD_FOOD = new FoodComponent.Builder().hunger(6).saturationModifier(0.6F).meat().build();
	public static final FoodComponent RAW_TOAD_FOOD = new FoodComponent.Builder().hunger(3).saturationModifier(0.3F).meat().build();
	public static final FoodComponent RAW_BULBUS_ROOT_FOOD = new FoodComponent.Builder().hunger(2).saturationModifier(0.6F).build();
	public static final FoodComponent BULBUS_ROOT_FOOD = new FoodComponent.Builder().hunger(5).saturationModifier(0.8F).build();

	public static final MushroomStewItem GLOWSHROOM_STEW = new MushroomStewItem(settings().maxCount(1).recipeRemainder(Items.BOWL).food(GLOWSHROOM_SOUP_FOOD));
	public static final Item GLOWFISH = new Item(settings().food(GLOWFISH_FOOD));
	public static final Item COOKED_GLOWFISH = new Item(settings().food(COOKED_GLOWFISH_FOOD));
	public static final Item RAW_TOAD = new Item(settings().food(RAW_TOAD_FOOD));
	public static final Item COOKED_TOAD = new Item(settings().food(COOKED_TOAD_FOOD));
	public static final Item BULBUS_ROOT = new Item(settings().food(RAW_BULBUS_ROOT_FOOD));
	public static final Item ROASTED_BULBUS_ROOT = new Item(settings().food(BULBUS_ROOT_FOOD));

	public static final Item COWBOY_HAT = new HatItem(BiomeMakeover.ID("textures/misc/cowboy_hat.png"), settings());
	public static final Item WITCH_HAT = new HatItem(BiomeMakeover.ID("textures/misc/witch_hat.png"), settings());

	public static final Item MAGENTA_PETALS = new BMItem(settings());
	public static final Item LIGHT_BLUE_PETALS = new BMItem(settings());
	public static final Item PINK_PETALS = new BMItem(settings());
	public static final Item GRAY_PETALS = new BMItem(settings());
	public static final Item CYAN_PETALS = new BMItem(settings());
	public static final Item PURPLE_PETALS = new BMItem(settings());
	public static final Item BLUE_PETALS = new BMItem(settings());
	public static final Item BROWN_PETALS = new BMItem(settings());

	public static final Item SCUTTLER_TAIL = new BMItem(settings());
	public static final Item ECTOPLASM = new BMItem(settings());

	public static final Item LIGHTNING_BOTTLE = new LightningBottleItem(settings());
	public static final Item DRAGONFLY_WINGS = new Item(settings());
	public static final Item BAT_WING = new Item(settings());
	public static final Item BLIGHTBAT_WING = new Item(settings());
	public static final Item WART = new Item(settings());
	public static final Item SOUL_EMBERS = new Item(settings());

	public static final Item ILLUNITE_SHARD = new Item(settings());
	public static final Item ROOTLING_SEEDS = new AliasedBlockItem(BMBlocks.ROOTLING_CROP, settings());
	public static final Item MOTH_SCALES = new Item(settings());
	public static final Item STUNT_POWDER = new StuntPowderItem(settings());

	public static final BMMusicDiskItem BUTTON_MUSHROOMS_MUSIC_DISK = new BMMusicDiskItem(14, BMEffects.BUTTON_MUSHROOMS, settings().maxCount(1).rarity(Rarity.RARE));
	public static final BMMusicDiskItem GHOST_TOWN_MUSIC_DISK = new BMMusicDiskItem(15, BMEffects.GHOST_TOWN, settings().maxCount(1).rarity(Rarity.RARE));
	public static final BMMusicDiskItem SWAMP_JIVES_MUSIC_DISK = new BMMusicDiskItem(1, BMEffects.SWAMP_JIVES, settings().maxCount(1).rarity(Rarity.RARE));

	public static final Item GLOWFISH_BUCKET = new GlowfishBucketItem(BMEntities.GLOWFISH, Fluids.WATER, settings().maxCount(1));
	public static final Item TADPOLE_BUCKET = new FishBucketItem(BMEntities.TADPOLE, Fluids.WATER, settings().maxCount(1));

	public static final Item GLOWFISH_SPAWN_EGG = new SpawnEggItem(BMEntities.GLOWFISH, 0xff7b00, 0xffd7b3, settings());
	public static final Item MUSHROOM_TRADER_SPAWN_EGG = new SpawnEggItem(BMEntities.MUSHROOM_TRADER, 0x37ff00, 0xb1ff9c, settings());
	public static final Item BLIGHTBAT_SPAWN_EGG = new SpawnEggItem(BMEntities.BLIGHTBAT, 0xae00ff, 0xdf9ffc, settings());
	public static final Item GHOST_SPAWN_EGG = new SpawnEggItem(BMEntities.GHOST, 0x566b6b, 0xb5fffe, settings());
	public static final Item SCUTTLER_SPAWN_EGG = new SpawnEggItem(BMEntities.SCUTTLER, 0x473427, 0x806553, settings());
	public static final Item COWBOY_SPAWN_EGG = new SpawnEggItem(BMEntities.COWBOY, 0x9bc2c2, 0x6b3f39, settings());
	public static final Item TOAD_SPAWN_EGG = new SpawnEggItem(BMEntities.TOAD, 0x4b8252, 0x614d33, settings());
	public static final Item TADPOLE_SPAWN_EGG = new SpawnEggItem(BMEntities.TADPOLE, 0x67824b, 0x614d33, settings());
	public static final Item DRAGONFLY_SPAWN_EGG = new SpawnEggItem(BMEntities.DRAGONFLY, 0xc7b634, 0xf2ebb6, settings());
	public static final Item LIGHTNING_BUG_SPAWN_EGG = new SpawnEggItem(BMEntities.LIGHTNING_BUG_ALTERNATE, 0x62c961, 0x96ebe1, settings());
	public static final Item DECAYED_SPAWN_EGG = new SpawnEggItem(BMEntities.DECAYED, 0x2e7068, 0x4a4034, settings());
	public static final Item GIANT_SLIME_SPAWN_EGG = new SpawnEggItem(BMEntities.GIANT_SLIME, 0x3ea05f, 0x7EBF6E, settings());
	public static final Item OWL_SPAWN_EGG = new SpawnEggItem(BMEntities.OWL, 0x302e27, 0x635c49, settings());
	public static final Item ROOTLING_SPAWN_EGG = new SpawnEggItem(BMEntities.ROOTLING, 0x2b2924, 0xa17b1f, settings());
	public static final Item MOTH_SPAWN_EGG = new SpawnEggItem(BMEntities.MOTH, 0x7d5699, 0x968e9c, settings());

	public static final Item ICON_ITEM = new FakeItem();

	public static void init()
	{
		RegistryHelper.register(Registry.ITEM, Item.class, BMItems.class);

		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.PURPLE_GLOWSHROOM.asItem(), 0.70F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.ORANGE_GLOWSHROOM.asItem(), 0.70F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.GREEN_GLOWSHROOM.asItem(), 0.70F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.BLIGHTED_BALSA_SAPLING.asItem(), 0.4F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.SWAMP_CYPRESS_SAPLING.asItem(), 0.4F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.WILLOW_SAPLING.asItem(), 0.4F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.ANCIENT_OAK_SAPLING.asItem(), 0.4F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.PURPLE_GLOWSHROOM_BLOCK.asItem(), 0.9F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.GREEN_GLOWSHROOM_BLOCK.asItem(), 0.9F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.ORANGE_GLOWSHROOM_BLOCK.asItem(), 0.9F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.GLOWSHROOM_STEM.asItem(), 0.7F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.MYCELIUM_ROOTS.asItem(), 0.7F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.MYCELIUM_SPROUTS.asItem(), 0.7F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.TALL_BROWN_MUSHROOM.asItem(), 0.7F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.TALL_RED_MUSHROOM.asItem(), 0.7F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.BLIGHTED_BALSA_LEAVES.asItem(), 0.3F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.WILLOW_LEAVES.asItem(), 0.3F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.SWAMP_CYPRESS_LEAVES.asItem(), 0.3F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.ANCIENT_OAK_LEAVES.asItem(), 0.3F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.CATTAIL.asItem(), 0.5F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.REED.asItem(), 0.2F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.SMALL_LILY_PAD.asItem(), 0.3F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.SAGUARO_CACTUS.asItem(), 0.15F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.BARREL_CACTUS.asItem(), 0.3F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMItems.PINK_PETALS.asItem(), 0.3F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMItems.MAGENTA_PETALS.asItem(), 0.3F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.WILLOWING_BRANCHES.asItem(), 0.3F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.MARIGOLD.asItem(), 0.65F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.SWAMP_AZALEA.asItem(), 0.65F);
		ComposterBlock.ITEM_TO_LEVEL_INCREASE_CHANCE.put(BMBlocks.WATER_LILY.asItem(), 0.8F);
	}

	public static Item.Settings settings()
	{
		return new Item.Settings().group(BiomeMakeover.GROUP);
	}

	public static final Tag<Item> CURSE_FUEL = TagRegistry.item(BiomeMakeover.ID("curse_fuel"));

	public static final Tag<Item> MUSHROOM_FIELDS = TagRegistry.item(BiomeMakeover.ID("mushroom_fields"));
	public static final Tag<Item> BADLANDS = TagRegistry.item(BiomeMakeover.ID("badlands"));
	public static final Tag<Item> SWAMP = TagRegistry.item(BiomeMakeover.ID("swamp"));
	public static final Tag<Item> DARK_FOREST = TagRegistry.item(BiomeMakeover.ID("dark_forest"));
}
