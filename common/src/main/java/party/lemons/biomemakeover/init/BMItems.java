package party.lemons.biomemakeover.init;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.Registry;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.Tag;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluids;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.Constants;
import party.lemons.biomemakeover.item.*;
import party.lemons.biomemakeover.mixin.ItemTagsInvoker;
import party.lemons.taniwha.item.modifier.CompostItemModifier;
import party.lemons.taniwha.item.types.FakeItem;
import party.lemons.taniwha.item.types.TItem;
import party.lemons.taniwha.item.types.TItemNameBlockItem;
import party.lemons.taniwha.registry.RegistryHelper;

public class BMItems
{

    public static final FoodProperties GLOWSHROOM_SOUP_FOOD = new FoodProperties.Builder().alwaysEat().saturationMod(0.6F).nutrition(5).effect(new MobEffectInstance(MobEffects.NIGHT_VISION, 1200, 0), 1).effect(new MobEffectInstance(MobEffects.GLOWING, 1200, 0), 1).build();
    public static final FoodProperties GLOWFISH_FOOD = new FoodProperties.Builder().alwaysEat().saturationMod(0.1F).nutrition(1).effect(new MobEffectInstance(MobEffects.NIGHT_VISION, 200, 0), 0.5F).effect(new MobEffectInstance(MobEffects.GLOWING, 200, 0), 0.5F).build();
    public static final FoodProperties COOKED_GLOWFISH_FOOD = new FoodProperties.Builder().alwaysEat().saturationMod(0.6F).nutrition(5).effect(new MobEffectInstance(MobEffects.NIGHT_VISION, 200, 0), 0.5F).effect(new MobEffectInstance(MobEffects.GLOWING, 200, 0), 0.5F).build();
    public static final FoodProperties COOKED_TOAD_FOOD = new FoodProperties.Builder().nutrition(6).saturationMod(0.6F).meat().build();
    public static final FoodProperties RAW_TOAD_FOOD = new FoodProperties.Builder().nutrition(3).saturationMod(0.3F).meat().build();
    public static final FoodProperties RAW_BULBUS_ROOT_FOOD = new FoodProperties.Builder().nutrition(2).saturationMod(0.6F).build();
    public static final FoodProperties BULBUS_ROOT_FOOD = new FoodProperties.Builder().nutrition(5).saturationMod(0.8F).build();

    public static final ArmorMaterial CLADDED_MATERIAL = new CladdedArmorMaterial();
    public static final BMArmorItem.Builder CLADDED_ARMOR = BMArmorItem.Builder.create(CLADDED_MATERIAL).attribute("Armor Proj Res", BMEntities.ATT_PROJECTILE_RESISTANCE, 1, AttributeModifier.Operation.ADDITION);

    public static final Item GLOWSHROOM_STEW = new SuspiciousStewItem(properties().stacksTo(1).craftRemainder(Items.BOWL).food(GLOWSHROOM_SOUP_FOOD));
    public static final Item GLOWFISH = new Item(properties().food(GLOWFISH_FOOD));
    public static final Item COOKED_GLOWFISH = new Item(properties().food(COOKED_GLOWFISH_FOOD));
    public static final Item RAW_TOAD = new Item(properties().food(RAW_TOAD_FOOD));
    public static final Item COOKED_TOAD = new Item(properties().food(COOKED_TOAD_FOOD));
    public static final Item BULBUS_ROOT = new TItem(properties().food(RAW_BULBUS_ROOT_FOOD)).modifiers(CompostItemModifier.create(0.4F));
    public static final Item ROASTED_BULBUS_ROOT = new Item(properties().food(BULBUS_ROOT_FOOD));

    public static final Item COWBOY_HAT = new HatItem(BiomeMakeover.ID("textures/misc/cowboy_hat.png"), properties());
    public static final Item WITCH_HAT = new HatItem(BiomeMakeover.ID("textures/misc/witch_hat.png"), properties());

    public static final Item MAGENTA_PETALS = new TItem(properties()).modifiers(CompostItemModifier.create(0.3F));
    public static final Item LIGHT_BLUE_PETALS = new TItem(properties()).modifiers(CompostItemModifier.create(0.3F));
    public static final Item PINK_PETALS = new TItem(properties()).modifiers(CompostItemModifier.create(0.3F));
    public static final Item GRAY_PETALS = new TItem(properties()).modifiers(CompostItemModifier.create(0.3F));
    public static final Item CYAN_PETALS = new TItem(properties()).modifiers(CompostItemModifier.create(0.3F));
    public static final Item PURPLE_PETALS = new TItem(properties()).modifiers(CompostItemModifier.create(0.3F));
    public static final Item BLUE_PETALS = new TItem(properties()).modifiers(CompostItemModifier.create(0.3F));
    public static final Item BROWN_PETALS = new TItem(properties()).modifiers(CompostItemModifier.create(0.3F));

    public static final Item SCUTTLER_TAIL = new TItem(properties());
    public static final Item ECTOPLASM = new EctoplasmItem(properties());

    public static final Item LIGHTNING_BOTTLE = new LightningBottleItem(properties());
    public static final Item DRAGONFLY_WINGS = new Item(properties());
    public static final Item BAT_WING = new Item(properties());
    public static final Item BLIGHTBAT_WING = new Item(properties());
    public static final Item WART = new Item(properties());
    public static final Item SOUL_EMBERS = new Item(properties());

    public static final Item ILLUNITE_SHARD = new Item(properties());
    public static final Item ROOTLING_SEEDS = new TItemNameBlockItem(BMBlocks.ROOTLING_CROP, properties()).modifiers(CompostItemModifier.create(0.4F));;
    public static final Item MOTH_SCALES = new Item(properties());
    public static final Item STUNT_POWDER = new StuntPowderItem(properties());
    public static final Item CRUDE_CLADDING = new Item(properties());
    public static final Item CLADDED_HELMET = CLADDED_ARMOR.build(EquipmentSlot.HEAD, properties());
    public static final Item CLADDED_CHESTPLATE = CLADDED_ARMOR.build(EquipmentSlot.CHEST, properties());
    public static final Item CLADDED_LEGGINGS = CLADDED_ARMOR.build(EquipmentSlot.LEGS, properties());
    public static final Item CLADDED_BOOTS = CLADDED_ARMOR.build(EquipmentSlot.FEET, properties());
    public static final Item ENCHANTED_TOTEM = new EnchantedTotemItem(properties().rarity(Rarity.EPIC).stacksTo(1));

    public static final BMRecordItem BUTTON_MUSHROOMS_MUSIC_DISK = new BMRecordItem(14, BMEffects.BUTTON_MUSHROOMS, properties().stacksTo(1).rarity(Rarity.RARE));
    public static final BMRecordItem GHOST_TOWN_MUSIC_DISK = new BMRecordItem(15, BMEffects.GHOST_TOWN, properties().stacksTo(1).rarity(Rarity.RARE));
    public static final BMRecordItem SWAMP_JIVES_MUSIC_DISK = new BMRecordItem(1, BMEffects.SWAMP_JIVES, properties().stacksTo(1).rarity(Rarity.RARE));
    public static final BMRecordItem RED_ROSE_MUSIC_DISK = new BMRecordItem(2, BMEffects.RED_ROSE, properties().stacksTo(1).rarity(Rarity.RARE));

    public static final Item GLOWFISH_BUCKET = new GlowfishBucketItem(BMEntities.GLOWFISH, Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, properties().stacksTo(1));
    public static final Item TADPOLE_BUCKET = new MobBucketItem(BMEntities.TADPOLE, Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, properties().stacksTo(1));

    public static final Item GLOWFISH_SPAWN_EGG = new SpawnEggItem(BMEntities.GLOWFISH, 0xff7b00, 0xffd7b3, properties());
    public static final Item MUSHROOM_TRADER_SPAWN_EGG = new SpawnEggItem(BMEntities.MUSHROOM_TRADER, 0x37ff00, 0xb1ff9c, properties());
    public static final Item BLIGHTBAT_SPAWN_EGG = new SpawnEggItem(BMEntities.BLIGHTBAT, 0xae00ff, 0xdf9ffc, properties());
    public static final Item GHOST_SPAWN_EGG = new SpawnEggItem(BMEntities.GHOST, 0x566b6b, 0xb5fffe, properties());
    public static final Item SCUTTLER_SPAWN_EGG = new SpawnEggItem(BMEntities.SCUTTLER, 0x473427, 0x806553, properties());
    public static final Item COWBOY_SPAWN_EGG = new SpawnEggItem(BMEntities.COWBOY, 0x9bc2c2, 0x6b3f39, properties());
    public static final Item TOAD_SPAWN_EGG = new SpawnEggItem(BMEntities.TOAD, 0x4b8252, 0x614d33, properties());
    public static final Item TADPOLE_SPAWN_EGG = new SpawnEggItem(BMEntities.TADPOLE, 0x67824b, 0x614d33, properties());
    public static final Item DRAGONFLY_SPAWN_EGG = new SpawnEggItem(BMEntities.DRAGONFLY, 0xc7b634, 0xf2ebb6, properties());
    public static final Item LIGHTNING_BUG_SPAWN_EGG = new SpawnEggItem(BMEntities.LIGHTNING_BUG_ALTERNATE, 0x62c961, 0x96ebe1, properties());
    public static final Item DECAYED_SPAWN_EGG = new SpawnEggItem(BMEntities.DECAYED, 0x2e7068, 0x4a4034, properties());
    public static final Item OWL_SPAWN_EGG = new SpawnEggItem(BMEntities.OWL, 0x302e27, 0x635c49, properties());
    public static final Item ROOTLING_SPAWN_EGG = new SpawnEggItem(BMEntities.ROOTLING, 0x2b2924, 0xa17b1f, properties());
    public static final Item MOTH_SPAWN_EGG = new SpawnEggItem(BMEntities.MOTH, 0x7d5699, 0x968e9c, properties());

    public static final Item ICON_ITEM = new FakeItem();

    public static final Tag<Item> CURSE_FUEL = ItemTagsInvoker.callBind(BiomeMakeover.ID("curse_fuel").toString());

  //  public static final Tag<Item> MUSHROOM_FIELDS = ItemTagsInvoker.callBind(BiomeMakeover.ID("mushroom_fields").toString());
  //  public static final Tag<Item> BADLANDS = ItemTagsInvoker.callBind(BiomeMakeover.ID("badlands").toString());
  //  public static final Tag<Item> SWAMP = ItemTagsInvoker.callBind(BiomeMakeover.ID("swamp").toString());
  //  public static final Tag<Item> DARK_FOREST = ItemTagsInvoker.callBind(BiomeMakeover.ID("dark_forest").toString());


    public static void init() {
        RegistryHelper.register(Constants.MOD_ID, Registry.ITEM, Item.class, BMItems.class);
    }

    public static Item.Properties properties()
    {
        return new Item.Properties().tab(BiomeMakeover.TAB);
    }


    private static class CladdedArmorMaterial implements ArmorMaterial
    {
        @Override
        public int getDurabilityForSlot(EquipmentSlot equipmentSlot) {
            return ArmorMaterials.IRON.getDurabilityForSlot(equipmentSlot);
        }

        @Override
        public int getDefenseForSlot(EquipmentSlot equipmentSlot) {
            return ArmorMaterials.CHAIN.getDefenseForSlot(equipmentSlot);
        }

        @Override
        public int getEnchantmentValue() {
            return 15;
        }

        @Override
        public SoundEvent getEquipSound()
        {
            return ArmorMaterials.LEATHER.getEquipSound();
        }

        @Override
        public Ingredient getRepairIngredient()
        {
            return ArmorMaterials.LEATHER.getRepairIngredient();
        }

        @Override
        public String getName()
        {
            return "cladded";
        }

        @Override
        public float getToughness()
        {
            return 0;
        }

        @Override
        public float getKnockbackResistance()
        {
            return 0.07F;
        }
    }
}
