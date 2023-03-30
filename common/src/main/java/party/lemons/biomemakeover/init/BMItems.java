package party.lemons.biomemakeover.init;

import dev.architectury.core.item.ArchitecturyMobBucketItem;
import dev.architectury.core.item.ArchitecturyRecordItem;
import dev.architectury.core.item.ArchitecturySpawnEggItem;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.fuel.FuelRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
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
import party.lemons.taniwha.item.ArmorBuilder;
import party.lemons.taniwha.item.ItemHelper;
import party.lemons.taniwha.item.types.FakeItem;
import party.lemons.taniwha.item.types.TItem;
import party.lemons.taniwha.item.types.TItemNameBlockItem;

import java.util.function.Supplier;

public class BMItems
{
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Constants.MOD_ID, Registries.ITEM);
    public static final TagKey<Item> CURSE_FUEL = TagKey.create(Registries.ITEM, BiomeMakeover.ID("curse_fuel"));
    public static final TagKey<Item> ADDITIONAL_CAMEL_FOOD = TagKey.create(Registries.ITEM, BiomeMakeover.ID("additional_camel_food"));

    public static final FoodProperties GLOWSHROOM_SOUP_FOOD = new FoodProperties.Builder().alwaysEat().saturationMod(0.6F).nutrition(5).effect(new MobEffectInstance(MobEffects.NIGHT_VISION, 1200, 0), 1).effect(new MobEffectInstance(MobEffects.GLOWING, 1200, 0), 1).build();
    public static final FoodProperties GLOWFISH_FOOD = new FoodProperties.Builder().alwaysEat().saturationMod(0.1F).nutrition(1).effect(new MobEffectInstance(MobEffects.NIGHT_VISION, 200, 0), 0.5F).effect(new MobEffectInstance(MobEffects.GLOWING, 200, 0), 0.5F).build();
    public static final FoodProperties COOKED_GLOWFISH_FOOD = new FoodProperties.Builder().alwaysEat().saturationMod(0.6F).nutrition(5).effect(new MobEffectInstance(MobEffects.NIGHT_VISION, 200, 0), 0.5F).effect(new MobEffectInstance(MobEffects.GLOWING, 200, 0), 0.5F).build();
    public static final FoodProperties COOKED_TOAD_FOOD = new FoodProperties.Builder().nutrition(6).saturationMod(0.6F).meat().build();
    public static final FoodProperties RAW_TOAD_FOOD = new FoodProperties.Builder().nutrition(3).saturationMod(0.3F).meat().build();
    public static final FoodProperties RAW_BULBUS_ROOT_FOOD = new FoodProperties.Builder().nutrition(2).saturationMod(0.6F).build();
    public static final FoodProperties BULBUS_ROOT_FOOD = new FoodProperties.Builder().nutrition(5).saturationMod(0.8F).build();
    public static final FoodProperties RAW_CRAB_FOOD = new FoodProperties.Builder().nutrition(2).saturationMod(0.1F).build();
    public static final FoodProperties COOKED_CRAB_FOOD = new FoodProperties.Builder().nutrition(5).saturationMod(0.6F).build();
    public static final FoodProperties CRAB_CHOWDER_FOOD = new FoodProperties.Builder().nutrition(11).saturationMod(0.9F).build();


    public static final ArmorMaterial CLADDED_MATERIAL = new CladdedArmorMaterial();

    public static final Supplier<Item> GLOWSHROOM_STEW = registerItem("glowshroom_stew", ()->new SuspiciousStewItem(properties().stacksTo(1).craftRemainder(Items.BOWL).food(GLOWSHROOM_SOUP_FOOD)));
    public static final Supplier<Item> GLOWFISH = registerItem("glowfish", ()->new Item(properties().food(GLOWFISH_FOOD)));
    public static final Supplier<Item> COOKED_GLOWFISH = registerItem("cooked_glowfish", ()->new Item(properties().food(COOKED_GLOWFISH_FOOD)));
    public static final Supplier<Item> RAW_TOAD = registerItem("raw_toad", ()->new Item(properties().food(RAW_TOAD_FOOD)));
    public static final Supplier<Item> COOKED_TOAD = registerItem("cooked_toad", ()->new Item(properties().food(COOKED_TOAD_FOOD)));
    public static final Supplier<Item> BULBUS_ROOT = registerItem("bulbus_root", ()->new TItem(properties().food(RAW_BULBUS_ROOT_FOOD)));
    public static final Supplier<Item> ROASTED_BULBUS_ROOT = registerItem("roasted_bulbus_root", ()->new Item(properties().food(BULBUS_ROOT_FOOD)));
    public static final Supplier<Item> RAW_CRAB = registerItem("raw_crab", ()->new TItem(properties().food(RAW_CRAB_FOOD)));
    public static final Supplier<Item> COOKED_CRAB = registerItem("cooked_crab", ()->new TItem(properties().food(COOKED_CRAB_FOOD)));
    public static final Supplier<Item> CRAB_CHOWDER = registerItem("crab_chowder", ()->new BowlFoodItem(properties().food(COOKED_CRAB_FOOD).stacksTo(1)));

    public static final Supplier<Item> COWBOY_HAT = registerItem("cowboy_hat", ()->new HatItem(BiomeMakeover.ID("textures/misc/cowboy_hat.png"), properties()));
    public static final Supplier<Item> WITCH_HAT = registerItem("witch_hat", ()->new HatItem(BiomeMakeover.ID("textures/misc/witch_hat.png"), properties()));

    public static final Supplier<Item> MAGENTA_PETALS = registerItem("magenta_petals", ()->new TItem(properties()));
    public static final Supplier<Item> LIGHT_BLUE_PETALS = registerItem("light_blue_petals", ()->new TItem(properties()));
    public static final Supplier<Item> PINK_PETALS = registerItem("pink_petals", ()->new TItem(properties()));
    public static final Supplier<Item> GRAY_PETALS = registerItem("gray_petals", ()->new TItem(properties()));
    public static final Supplier<Item> CYAN_PETALS = registerItem("cyan_petals", ()->new TItem(properties()));
    public static final Supplier<Item> PURPLE_PETALS = registerItem("purple_petals", ()->new TItem(properties()));
    public static final Supplier<Item> BLUE_PETALS = registerItem("blue_petals", ()->new TItem(properties()));
    public static final Supplier<Item> BROWN_PETALS = registerItem("brown_petals", ()->new TItem(properties()));

    public static final Supplier<Item> SCUTTLER_TAIL = registerItem("scuttler_tail", ()->new TItem(properties()));
    public static final Supplier<Item> ECTOPLASM = registerItem("ectoplasm", ()->new EctoplasmItem(properties()));

    public static final Supplier<Item> LIGHTNING_BOTTLE = registerItem("lightning_bottle", ()->new LightningBottleItem(properties()));
    public static final Supplier<Item> DRAGONFLY_WINGS = registerItem("dragonfly_wings", ()->new Item(properties()));
    public static final Supplier<Item> BAT_WING = registerItem("bat_wing", ()->new Item(properties()));
    public static final Supplier<Item> BLIGHTBAT_WING = registerItem("blightbat_wing", ()->new Item(properties()));
    public static final Supplier<Item> WART = registerItem("wart", ()->new Item(properties()));
    public static final Supplier<Item> SOUL_EMBERS = registerItem("soul_embers", ()->new Item(properties()));

    public static final Supplier<Item> ILLUNITE_SHARD = registerItem("illunite_shard", ()->new Item(properties()));
    public static final Supplier<Item> ROOTLING_SEEDS = registerItem("rootling_seeds", ()->new TItemNameBlockItem(BMBlocks.ROOTLING_CROP.get(), properties()));
    public static final Supplier<Item> MOTH_SCALES = registerItem("moth_scales", ()->new Item(properties()));
    public static final Supplier<Item> STUNT_POWDER = registerItem("stunt_powder", ()->new StuntPowderItem(properties()));
    public static final Supplier<Item> CRUDE_CLADDING = registerItem("crude_cladding", ()->new Item(properties()));

    public static final Supplier<Item> ENCHANTED_TOTEM = registerItem("enchanted_totem", ()->new EnchantedTotemItem(properties().rarity(Rarity.EPIC).stacksTo(1)));

    public static final Supplier<Item> BUTTON_MUSHROOMS_MUSIC_DISK = registerItem("button_mushrooms_music_disk", ()->new ArchitecturyRecordItem(14, BMEffects.BUTTON_MUSHROOMS, properties().stacksTo(1).rarity(Rarity.RARE), 115));
    public static final Supplier<Item> GHOST_TOWN_MUSIC_DISK = registerItem("ghost_town_music_disk", ()->new ArchitecturyRecordItem(15, BMEffects.GHOST_TOWN, properties().stacksTo(1).rarity(Rarity.RARE), 270));
    public static final Supplier<Item> SWAMP_JIVES_MUSIC_DISK = registerItem("swamp_jives_music_disk", ()->new ArchitecturyRecordItem(1, BMEffects.SWAMP_JIVES, properties().stacksTo(1).rarity(Rarity.RARE), 277));
    public static final Supplier<Item> RED_ROSE_MUSIC_DISK = registerItem("red_rose_music_disk", ()->new ArchitecturyRecordItem(2, BMEffects.RED_ROSE, properties().stacksTo(1).rarity(Rarity.RARE), 135));

    public static final Supplier<Item> GLOWFISH_BUCKET = registerItem("glowfish_bucket", ()->new GlowfishBucketItem(BMEntities.GLOWFISH, ()->Fluids.WATER, ()->SoundEvents.BUCKET_EMPTY_FISH, properties().stacksTo(1)));
    public static final Supplier<Item> TADPOLE_BUCKET = registerItem("tadpole_bucket", ()-> new ArchitecturyMobBucketItem(BMEntities.TADPOLE, ()->Fluids.WATER, ()->SoundEvents.BUCKET_EMPTY_FISH, properties().stacksTo(1)));

    public static final Supplier<Item> GLOWFISH_SPAWN_EGG = registerItem("glowfish_spawn_egg", ()->new ArchitecturySpawnEggItem(BMEntities.GLOWFISH, 0xff7b00, 0xffd7b3, properties()));
    public static final Supplier<Item> MUSHROOM_TRADER_SPAWN_EGG = registerItem("mushroom_trader_spawn_egg", ()->new ArchitecturySpawnEggItem(BMEntities.MUSHROOM_TRADER, 0x37ff00, 0xb1ff9c, properties()));
    public static final Supplier<Item> BLIGHTBAT_SPAWN_EGG = registerItem("blightbat_spawn_egg", ()->new ArchitecturySpawnEggItem(BMEntities.BLIGHTBAT, 0xae00ff, 0xdf9ffc, properties()));
    public static final Supplier<Item> GHOST_SPAWN_EGG =registerItem("ghost_spawn_egg", ()-> new ArchitecturySpawnEggItem(BMEntities.GHOST, 0x566b6b, 0xb5fffe, properties()));
    public static final Supplier<Item> SCUTTLER_SPAWN_EGG = registerItem("scuttler_spawn_egg", ()->new ArchitecturySpawnEggItem(BMEntities.SCUTTLER, 0x473427, 0x806553, properties()));
    public static final Supplier<Item> COWBOY_SPAWN_EGG = registerItem("cowboy_spawn_egg", ()->new ArchitecturySpawnEggItem(BMEntities.COWBOY, 0x9bc2c2, 0x6b3f39, properties()));
    public static final Supplier<Item> TOAD_SPAWN_EGG = registerItem("toad_spawn_egg", ()->new DisabledSpawnEggItem(BMEntities.TOAD, 0x4b8252, 0x614d33, properties()));
    public static final Supplier<Item> TADPOLE_SPAWN_EGG = registerItem("tadpole_spawn_egg", ()->new DisabledSpawnEggItem(BMEntities.TADPOLE, 0x67824b, 0x614d33, properties()));
    public static final Supplier<Item> DRAGONFLY_SPAWN_EGG = registerItem("dragonfly_spawn_egg", ()->new ArchitecturySpawnEggItem(BMEntities.DRAGONFLY, 0xc7b634, 0xf2ebb6, properties()));
    public static final Supplier<Item> LIGHTNING_BUG_SPAWN_EGG = registerItem("lightning_bug_spawn_egg", ()->new ArchitecturySpawnEggItem(BMEntities.LIGHTNING_BUG_ALTERNATE, 0x62c961, 0x96ebe1, properties()));
    public static final Supplier<Item> DECAYED_SPAWN_EGG = registerItem("decayed_spawn_egg", ()->new ArchitecturySpawnEggItem(BMEntities.DECAYED, 0x2e7068, 0x4a4034, properties()));
    public static final Supplier<Item> OWL_SPAWN_EGG = registerItem("owl_spawn_egg", ()->new ArchitecturySpawnEggItem(BMEntities.OWL, 0x302e27, 0x635c49, properties()));
    public static final Supplier<Item> ROOTLING_SPAWN_EGG = registerItem("rootling_spawn_egg", ()->new ArchitecturySpawnEggItem(BMEntities.ROOTLING, 0x2b2924, 0xa17b1f, properties()));
    public static final RegistrySupplier<Item> MOTH_SPAWN_EGG = registerItem("moth_spawn_egg", ()->new ArchitecturySpawnEggItem(BMEntities.MOTH, 0x7d5699, 0x968e9c, properties()));
    public static final RegistrySupplier<Item> HELMIT_CRAB_SPAWN_EGG = registerItem("helmit_crab_spawn_egg", ()->new ArchitecturySpawnEggItem(BMEntities.HELMIT_CRAB, 0xbd2520, 0xeb7b65, properties()));

    public static final Supplier<Item> ICON_ITEM = registerItem("icon_item", FakeItem::new);

    public static void init() {

        BMEntities.ATT_PROJECTILE_RESISTANCE.listen(a->{
            ArmorBuilder CLADDED_ARMOR_BUILDER = ArmorBuilder.create(CLADDED_MATERIAL).attribute("Armor Proj Res", BMEntities.ATT_PROJECTILE_RESISTANCE.get(), 1, AttributeModifier.Operation.ADDITION);
            Supplier<Item> CLADDED_HELMET = registerItem("cladded_helmet", CLADDED_ARMOR_BUILDER.build(ArmorItem.Type.HELMET, properties()));
            Supplier<Item> CLADDED_CHESTPLATE = registerItem("cladded_chestplate", CLADDED_ARMOR_BUILDER.build(ArmorItem.Type.CHESTPLATE, properties()));
            Supplier<Item> CLADDED_LEGGINGS = registerItem("cladded_leggings",CLADDED_ARMOR_BUILDER.build(ArmorItem.Type.LEGGINGS, properties()));
            Supplier<Item> CLADDED_BOOTS = registerItem("cladded_boots", CLADDED_ARMOR_BUILDER.build(ArmorItem.Type.BOOTS, properties()));
        });

        HELMIT_CRAB_SPAWN_EGG.listen((i)->{
            FuelRegistry.register(10000, BMBlocks.DRIED_PEAT.get().asItem());
        });

        ITEMS.register();
    }

    private static RegistrySupplier<Item> registerItem(String id, Supplier<Item> item) {
        return ItemHelper.registerItem(ITEMS, BiomeMakeover.ID(id), item);
    }


    public static Item.Properties properties()
    {
        return new Item.Properties();
    }


    private static class CladdedArmorMaterial implements ArmorMaterial
    {

        @Override
        public int getDurabilityForType(ArmorItem.Type type)
        {
            return ArmorMaterials.IRON.getDurabilityForType(type);
        }

        @Override
        public int getDefenseForType(ArmorItem.Type type)
        {
            return ArmorMaterials.CHAIN.getDefenseForType(type);
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
