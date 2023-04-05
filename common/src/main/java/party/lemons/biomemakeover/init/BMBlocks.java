package party.lemons.biomemakeover.init;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import dev.architectury.hooks.item.tool.HoeItemHooks;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.PushReaction;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.Constants;
import party.lemons.biomemakeover.block.*;
import party.lemons.biomemakeover.level.generate.foliage.AncientOakSaplingGenerator;
import party.lemons.biomemakeover.level.generate.foliage.BalsaSaplingGenerator;
import party.lemons.biomemakeover.level.generate.foliage.SwampCypressSaplingGenerator;
import party.lemons.biomemakeover.level.generate.foliage.WillowSaplingGenerator;
import party.lemons.biomemakeover.util.BMSoundType;
import party.lemons.taniwha.block.BlockHelper;
import party.lemons.taniwha.block.DecorationBlockFactory;
import party.lemons.taniwha.block.WoodBlockFactory;
import party.lemons.taniwha.block.modifier.BlockModifier;
import party.lemons.taniwha.block.modifier.FlammableModifier;
import party.lemons.taniwha.block.modifier.RTypeModifier;
import party.lemons.taniwha.block.rtype.RType;
import party.lemons.taniwha.block.types.*;
import party.lemons.taniwha.item.ItemHelper;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class BMBlocks
{
    public static Map<RegistrySupplier<Block>, RegistrySupplier<Item>> BLOCK_ITEMS = Maps.newHashMap();

    private static final BlockBehaviour.Properties LEAF_PROPERTIES = properties(Material.LEAVES, 0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(BMBlocks::canSpawnOnLeaves).isSuffocating((a, b, c)->false).isViewBlocking((a, b, c)->false);
    private static final BlockModifier[] LEAF_MODIFIERS = new party.lemons.taniwha.block.modifier.BlockModifier[]{party.lemons.taniwha.block.modifier.RTypeModifier.create(party.lemons.taniwha.block.rtype.RType.CUTOUT), party.lemons.taniwha.block.modifier.FlammableModifier.LEAVES};

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Constants.MOD_ID, Registries.BLOCK);

    public static final Material POLTERGEIST_MATERIAL = new Material(MaterialColor.COLOR_GRAY, false, true, true, false, true, false, PushReaction.BLOCK);
    public static final SoundType BM_LILY_PAD_SOUND = new SoundType(1.0F, 1.0F, SoundEvents.WET_GRASS_BREAK, SoundEvents.WET_GRASS_STEP, SoundEvents.LILY_PAD_PLACE, SoundEvents.WET_GRASS_HIT, SoundEvents.WET_GRASS_FALL);
    public static final SoundType ILLUNITE_SOUND = new BMSoundType(1.0F, 1.0F, BMEffects.ILLUNITE_BREAK, BMEffects.ILLUNITE_STEP, BMEffects.ILLUNITE_PLACE, BMEffects.ILLUNITE_HIT, ()->SoundEvents.STONE_FALL);

    public static final RegistrySupplier<Block> PURPLE_GLOWSHROOM = registerBlockItem("purple_glowshroom", ()->new GlowshroomPlantBlock(ResourceKey.create(Registries.CONFIGURED_FEATURE, BiomeMakeover.ID("mushroom_fields/huge_purple_glowshroom")), properties(Material.PLANT, 0F).lightLevel((s)->13).noCollission().noCollission().sound(SoundType.FUNGUS)).modifiers(RTypeModifier.CUTOUT));
    public static final RegistrySupplier<Block> GREEN_GLOWSHROOM = registerBlockItem("green_glowshroom", ()->new GlowshroomPlantBlock(ResourceKey.create(Registries.CONFIGURED_FEATURE, BiomeMakeover.ID("mushroom_fields/huge_green_glowshroom")), properties(Material.PLANT, 0F).lightLevel((s)->13).noCollission().noOcclusion().sound(SoundType.FUNGUS)).modifiers(RTypeModifier.CUTOUT));
    public static final RegistrySupplier<Block> ORANGE_GLOWSHROOM = registerBlockItem("orange_glowshroom", ()->new UnderwaterMushroomPlantBlock(ResourceKey.create(Registries.CONFIGURED_FEATURE, BiomeMakeover.ID("mushroom_fields/huge_orange_glowshroom")), properties(Material.PLANT, 0F).lightLevel((s)->13).noCollission().noOcclusion().sound(SoundType.FUNGUS)).modifiers(RTypeModifier.CUTOUT));

    public static final RegistrySupplier<Block> PURPLE_GLOWSHROOM_BLOCK = registerBlockItem("purple_glowshroom_block", ()->new BMMushroomBlock(properties(Material.PLANT, 0.2F).lightLevel((s)->15).sound(SoundType.FUNGUS)));
    public static final RegistrySupplier<Block> GREEN_GLOWSHROOM_BLOCK = registerBlockItem("green_glowshroom_block", ()->new BMMushroomBlock(properties(Material.PLANT, 0.2F).lightLevel((s)->15).sound(SoundType.FUNGUS)));
    public static final RegistrySupplier<Block> ORANGE_GLOWSHROOM_BLOCK = registerBlockItem("orange_glowshroom_block", ()->new BMMushroomBlock(properties(Material.PLANT, 0.2F).lightLevel((s)->15).sound(SoundType.FUNGUS)));

    public static final RegistrySupplier<Block> MYCELIUM_SPROUTS = registerBlockItem("mycelium_sprouts", ()->new MushroomSproutsBlock(properties(Material.REPLACEABLE_FIREPROOF_PLANT, 0).noCollission().noCollission().instabreak().sound(SoundType.NETHER_SPROUTS)).modifiers(RTypeModifier.CUTOUT));
    public static final RegistrySupplier<Block> MYCELIUM_ROOTS = registerBlockItem("mycelium_roots", ()->new MushroomRootsBlock(properties(Material.REPLACEABLE_FIREPROOF_PLANT, 0).noCollission().noCollission().instabreak().sound(SoundType.ROOTS)).modifiers(RTypeModifier.CUTOUT));

    public static final RegistrySupplier<Block> TALL_BROWN_MUSHROOM = registerBlockItem("tall_brown_mushroom", ()->new BMTallMushroomBlock(Blocks.BROWN_MUSHROOM, properties(Material.PLANT, 0).instabreak().noCollission().sound(SoundType.FUNGUS)).modifiers(RTypeModifier.CUTOUT));
    public static final RegistrySupplier<Block> TALL_RED_MUSHROOM = registerBlockItem("tall_red_mushroom", ()->new BMTallMushroomBlock(Blocks.RED_MUSHROOM, properties(Material.PLANT, 0).instabreak().noCollission().sound(SoundType.FUNGUS)).modifiers(RTypeModifier.CUTOUT));

    public static WoodBlockFactory BLIGHTED_BALSA_WOOD_INFO = new WoodBlockFactory(Constants.MOD_ID, "blighted_balsa", BiomeMakeover.TAB).all(()-> BMBoats.BLIGHTED_BALSA).register(BLOCKS, BMItems.ITEMS);
    public static final RegistrySupplier<Block> BLIGHTED_BALSA_LEAVES = registerBlockItem("blighted_balsa_leaves", ()->new TLeavesBlock(properties(Material.LEAVES, 0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(BMBlocks::canSpawnOnLeaves).isSuffocating((a, b, c)->false).isViewBlocking((a, b, c)->false)).modifiers(RTypeModifier.CUTOUT, FlammableModifier.LEAVES));
    public static final RegistrySupplier<Block> BLIGHTED_BALSA_SAPLING = sapling("blighted_balsa_sapling", new BalsaSaplingGenerator());

    public static final RegistrySupplier<Block> GLOWSHROOM_STEM = registerBlockItem("glowshroom_stem", ()->new BMMushroomBlock(properties(Material.GRASS, 0.2F).lightLevel((s)->7).sound(SoundType.FUNGUS)));
    public static final RegistrySupplier<Block> RED_MUSHROOM_BRICK = registerBlockItem("red_mushroom_brick", ()->new TBlock(properties(Material.GRASS, 0.8F).sound(SoundType.FUNGUS)));
    public static final DecorationBlockFactory RED_MUSHROOM_BRICK_DECORATION = new DecorationBlockFactory(Constants.MOD_ID, "red_mushroom_brick", RED_MUSHROOM_BRICK, properties(Material.GRASS, 0.8F).sound(SoundType.FUNGUS), BiomeMakeover.TAB).all().register(BLOCKS, BMItems.ITEMS);;
    public static final RegistrySupplier<Block> BROWN_MUSHROOM_BRICK = registerBlockItem("brown_mushroom_brick", ()->new TBlock(properties(Material.GRASS, 0.8F).sound(SoundType.FUNGUS)));
    public static final DecorationBlockFactory BROWN_MUSHROOM_BRICK_DECORATION = new DecorationBlockFactory(Constants.MOD_ID, "brown_mushroom_brick", BROWN_MUSHROOM_BRICK, properties(Material.GRASS, 0.8F).sound(SoundType.FUNGUS), BiomeMakeover.TAB).all().register(BLOCKS, BMItems.ITEMS);;
    public static final RegistrySupplier<Block> PURPLE_GLOWSHROOM_BRICK = registerBlockItem("purple_glowshroom_brick", ()->new TBlock(properties(Material.GRASS, 0.8F).lightLevel((s)->13).sound(SoundType.FUNGUS)));
    public static final DecorationBlockFactory PURPLE_GLOWSROOM_BRICK_DECORATION = new DecorationBlockFactory(Constants.MOD_ID, "purple_glowshroom_brick", PURPLE_GLOWSHROOM_BRICK, properties(Material.GRASS, 0.8F).lightLevel((s)->13).sound(SoundType.FUNGUS), BiomeMakeover.TAB).all().register(BLOCKS, BMItems.ITEMS);;
    public static final RegistrySupplier<Block> GREEN_GLOWSHROOM_BRICK = registerBlockItem("green_glowshroom_brick", ()->new TBlock(properties(Material.GRASS, 0.8F).lightLevel((s)->13).sound(SoundType.FUNGUS)));
    public static final DecorationBlockFactory GREEN_GLOWSROOM_BRICK_DECORATION = new DecorationBlockFactory(Constants.MOD_ID, "green_glowshroom_brick", GREEN_GLOWSHROOM_BRICK, properties(Material.GRASS, 0.8F).lightLevel((s)->13).sound(SoundType.FUNGUS), BiomeMakeover.TAB).all().register(BLOCKS, BMItems.ITEMS);;
    public static final RegistrySupplier<Block> ORANGE_GLOWSHROOM_BRICK = registerBlockItem("orange_glowshroom_brick", ()->new TBlock(properties(Material.GRASS, 0.8F).lightLevel((s)->13).sound(SoundType.FUNGUS)));
    public static final DecorationBlockFactory ORANGE_GLOWSROOM_BRICK_DECORATION = new DecorationBlockFactory(Constants.MOD_ID, "orange_glowshroom_brick", ORANGE_GLOWSHROOM_BRICK, properties(Material.GRASS, 0.8F).lightLevel((s)->13).sound(SoundType.FUNGUS), BiomeMakeover.TAB).all().register(BLOCKS, BMItems.ITEMS);;
    public static final RegistrySupplier<Block> GLOWSHROOM_STEM_BRICK = registerBlockItem("glowshroom_stem_brick", ()->new TBlock(properties(Material.GRASS, 0.8F).lightLevel((s)->7).sound(SoundType.FUNGUS)));
    public static final DecorationBlockFactory GLOWSHROOM_STEM_BRICK_DECORATION = new DecorationBlockFactory(Constants.MOD_ID, "glowshroom_stem_brick", GLOWSHROOM_STEM_BRICK, properties(Material.GRASS, 0.8F).lightLevel((s)->7).sound(SoundType.FUNGUS), BiomeMakeover.TAB).all().register(BLOCKS, BMItems.ITEMS);;
    public static final RegistrySupplier<Block> MUSHROOM_STEM_BRICK = registerBlockItem("mushroom_stem_brick", ()->new TBlock(properties(Material.GRASS, 0.8F).sound(SoundType.FUNGUS)));
    public static final DecorationBlockFactory MUSHROOM_STEM_BRICK_DECORATION = new DecorationBlockFactory(Constants.MOD_ID, "mushroom_stem_brick", MUSHROOM_STEM_BRICK, properties(Material.GRASS, 0.8F).sound(SoundType.FUNGUS), BiomeMakeover.TAB).all().register(BLOCKS, BMItems.ITEMS);;
    public static final RegistrySupplier<Block> BLIGHTED_COBBLESTONE = registerBlockItem("blighted_cobblestone", ()->new TBlock(properties(Material.STONE, 2).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DecorationBlockFactory BLIGHTED_COBBLESTONE_DECORATION = new DecorationBlockFactory(Constants.MOD_ID, "blighted_cobblestone", BLIGHTED_COBBLESTONE, properties(Material.STONE, 2F).requiresCorrectToolForDrops().sound(SoundType.STONE), BiomeMakeover.TAB).all().register(BLOCKS, BMItems.ITEMS);;
    public static final RegistrySupplier<Block> BLIGHTED_STONE_BRICKS = registerBlockItem("blighted_stone_bricks", ()->new TBlock(properties(Material.STONE, 2).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DecorationBlockFactory BLIGHTED_STONE_BRICKS_DECORATION = new DecorationBlockFactory(Constants.MOD_ID, "blighted_stone_bricks", BLIGHTED_STONE_BRICKS, properties(Material.STONE, 2F).requiresCorrectToolForDrops().sound(SoundType.STONE), BiomeMakeover.TAB).all().register(BLOCKS, BMItems.ITEMS);;
    public static final RegistrySupplier<Block> PAYDIRT = registerBlockItem("paydirt", ()->new TBlock(properties(Material.DIRT, 1.4F).requiresCorrectToolForDrops().sound(SoundType.GRAVEL)));

    public static final RegistrySupplier<Block> TUMBLEWEED = BLOCKS.register(BiomeMakeover.ID("tumbleweed"), ()->new Block(properties(Material.PLANT, 0)));

    public static final RegistrySupplier<Block> SAGUARO_CACTUS = registerBlockItem("saguaro_cactus", ()->new SaguaroCactusBlock(properties(Material.CACTUS, 0.4F).sound(SoundType.WOOL).randomTicks()).modifiers(RTypeModifier.CUTOUT));
    public static final RegistrySupplier<Block> BARREL_CACTUS = registerBlockItem("barrel_cactus", ()->new BarrelCactusBlock(false, properties(Material.CACTUS, 0).randomTicks().sound(SoundType.WOOL).noOcclusion().instabreak().noCollission()).modifiers(RTypeModifier.CUTOUT));
    public static final RegistrySupplier<Block> BARREL_CACTUS_FLOWERED = registerBlockItem("barrel_cactus_flowered", ()->new BarrelCactusBlock(true, properties(Material.CACTUS, 0).sound(SoundType.WOOL).noOcclusion().instabreak().noCollission()).modifiers(RTypeModifier.CUTOUT));

    public static final RegistrySupplier<Block> POLTERGEIST = registerBlockItem("poltergeist", ()->new PoltergeistBlock(properties(POLTERGEIST_MATERIAL, 1.0F).lightLevel((bs)->bs.getValue(PoltergeistBlock.ENABLED) ? 7 : 0).sound(SoundType.LODESTONE)).modifiers(RTypeModifier.CUTOUT));
    public static final RegistrySupplier<Block> ECTOPLASM_COMPOSTER = BLOCKS.register(BiomeMakeover.ID("ectoplasm_composter"), ()->new EctoplasmComposterBlock(properties(Material.WOOD, 0.6F).sound(SoundType.WOOD)));

    public static WoodBlockFactory WILLOW_WOOD_INFO = new WoodBlockFactory(Constants.MOD_ID, "willow", BiomeMakeover.TAB).all(()->BMBoats.WILLOW).register(BLOCKS, BMItems.ITEMS);;
    public static WoodBlockFactory SWAMP_CYPRESS_WOOD_INFO = new WoodBlockFactory(Constants.MOD_ID, "swamp_cypress", BiomeMakeover.TAB).all(()->BMBoats.SWAMP_CYPRESS).register(BLOCKS, BMItems.ITEMS);;

    public static final RegistrySupplier<Block> WILLOWING_BRANCHES = registerBlockItem("willowing_branches", ()->new WillowingBranchesBlock(properties(Material.PLANT, 0.1F).randomTicks().sound(SoundType.VINE).noCollission().noOcclusion()).modifiers(RTypeModifier.create(RType.CUTOUT_MIPPED), new FlammableModifier(15, 100)));
    public static final RegistrySupplier<Block> WILLOW_SAPLING = registerBlockItem("willow_sapling", ()->new WaterSaplingBlock(new WillowSaplingGenerator(), 1, properties(Material.PLANT, 0).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)).modifiers(RTypeModifier.CUTOUT));
    public static final RegistrySupplier<Block> SWAMP_CYPRESS_SAPLING = registerBlockItem("swamp_cypress_sapling", ()->new WaterSaplingBlock(new SwampCypressSaplingGenerator(), 3, properties(Material.PLANT, 0).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)).modifiers(RTypeModifier.CUTOUT));
    public static final RegistrySupplier<Block> PEAT = registerBlockItem("peat", ()->new TBlock(properties(Material.DIRT, 0.5F).sound(SoundType.WET_GRASS)));
    public static final RegistrySupplier<Block> DRIED_PEAT = registerBlockItem("dried_peat", ()->new TBlock(properties(Material.DIRT, 1F).sound(SoundType.NETHERRACK)));
    public static final RegistrySupplier<Block> MOSSY_PEAT = registerBlockItem("mossy_peat", ()->new TSpreadableBlock(properties(Material.DIRT, 0.5F).randomTicks().sound(SoundType.WET_GRASS), PEAT));
    public static final RegistrySupplier<Block> PEAT_FARMLAND = registerBlockItem("peat_farmland", ()->new PeatFarmlandBlock(properties(Material.DIRT, 0.5F).sound(SoundType.WET_GRASS).randomTicks().noOcclusion()));
    public static final RegistrySupplier<Block> DRIED_PEAT_BRICKS = registerBlockItem("dried_peat_bricks", ()->new TBlock(properties(Material.STONE, 2).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DecorationBlockFactory DRIED_PEAT_BRICKS_DECORATION = new DecorationBlockFactory(Constants.MOD_ID, "dried_peat_bricks", DRIED_PEAT_BRICKS, properties(Material.STONE, 2F).sound(SoundType.NETHERRACK).requiresCorrectToolForDrops().sound(SoundType.STONE), BiomeMakeover.TAB).all().register(BLOCKS, BMItems.ITEMS);;
    public static final RegistrySupplier<Block> MOSSY_DRIED_PEAT_BRICKS = registerBlockItem("mossy_dried_peat_bricks", ()->new TBlock(properties(Material.STONE, 2).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DecorationBlockFactory MOSSY_DRIED_PEAT_BRICKS_DECORATION = new DecorationBlockFactory(Constants.MOD_ID, "mossy_dried_peat_brick", MOSSY_DRIED_PEAT_BRICKS, properties(Material.STONE, 2F).sound(SoundType.NETHERRACK).requiresCorrectToolForDrops().sound(SoundType.STONE), BiomeMakeover.TAB).all().register(BLOCKS, BMItems.ITEMS);;
    public static final RegistrySupplier<Block> CRACKED_DRIED_PEAT_BRICKS = registerBlockItem("cracked_dried_peat_bricks", ()->new TBlock(properties(Material.STONE, 2).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DecorationBlockFactory CRACKED_DRIED_PEAT_BRICKS_DECORATION = new DecorationBlockFactory(Constants.MOD_ID, "cracked_dried_peat_brick", CRACKED_DRIED_PEAT_BRICKS, properties(Material.STONE, 2F).sound(SoundType.NETHERRACK).requiresCorrectToolForDrops().sound(SoundType.STONE), BiomeMakeover.TAB).all().register(BLOCKS, BMItems.ITEMS);;
    public static final RegistrySupplier<Block> SWAMP_AZALEA = registerBlockItem("buttonbush", ()->new TTallFlowerBlock(properties(Material.PLANT, 0).noCollission().instabreak().sound(SoundType.GRASS)).modifiers(RTypeModifier.CUTOUT, FlammableModifier.TALL_FLOWER));
    public static final RegistrySupplier<Block> MARIGOLD = registerBlockItem("marigold", ()->new TTallFlowerBlock(properties(Material.PLANT, 0).noCollission().instabreak().sound(SoundType.GRASS)).modifiers(RTypeModifier.CUTOUT, FlammableModifier.TALL_FLOWER));
    public static final RegistrySupplier<Block> BLACK_THISTLE = registerBlockItem("black_thistle", ()->new BlackThistleBlock(properties(Material.PLANT, 0).noCollission().instabreak().sound(SoundType.GRASS)).modifiers(RTypeModifier.CUTOUT, FlammableModifier.TALL_FLOWER));
    public static final RegistrySupplier<Block> FOXGLOVE = registerBlockItem("foxglove", ()->new TTallFlowerBlock(properties(Material.PLANT, 0).noCollission().instabreak().sound(SoundType.GRASS)).modifiers(RTypeModifier.CUTOUT, FlammableModifier.TALL_FLOWER));

    public static final RegistrySupplier<Block> CATTAIL = registerBlockItem("cattail", ()->new ReedBlock(properties(Material.PLANT, 0).instabreak().noCollission().sound(SoundType.GRASS)).modifiers(RTypeModifier.CUTOUT));
    public static final RegistrySupplier<Block> REED = registerBlockItem("reed", ()->new ReedBlock(properties(Material.PLANT, 0).instabreak().noCollission().sound(SoundType.GRASS)).modifiers(RTypeModifier.CUTOUT));
    public static final RegistrySupplier<Block> SMALL_LILY_PAD = registerLilyPad("small_lily_pad", ()->new SmallLilyPadBlock(properties(Material.PLANT, 0).instabreak().sound(BM_LILY_PAD_SOUND)).modifiers(RTypeModifier.CUTOUT));
    public static final RegistrySupplier<Block> WATER_LILY = registerLilyPad("water_lily", ()->new FloweredWaterlilyPadBlock(properties(Material.PLANT, 0).instabreak().sound(BM_LILY_PAD_SOUND)).modifiers(RTypeModifier.CUTOUT));
    public static final RegistrySupplier<Block> WILLOW_LEAVES = registerBlockItem("willow_leaves", ()->new TLeavesBlock(properties(Material.LEAVES, 0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(BMBlocks::canSpawnOnLeaves).isSuffocating((a, b, c)->false).isViewBlocking((a, b, c)->false)).modifiers(RTypeModifier.create(RType.CUTOUT_MIPPED), FlammableModifier.LEAVES));
    public static final RegistrySupplier<Block> SWAMP_CYPRESS_LEAVES = registerBlockItem("swamp_cypress_leaves", ()->new TLeavesBlock(properties(Material.LEAVES, 0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(BMBlocks::canSpawnOnLeaves).isSuffocating((a, b, c)->false).isViewBlocking((a, b, c)->false)).modifiers(RTypeModifier.create(RType.CUTOUT_MIPPED), FlammableModifier.LEAVES));
    public static final RegistrySupplier<Block> LIGHTNING_BUG_BOTTLE = registerBlockItem("lightning_bug_bottle", ()->new LightningBugBottleBlock(properties(Material.STONE, 0.5F).lightLevel((b)->15).noOcclusion()).modifiers(RTypeModifier.CUTOUT));

    public static final RegistrySupplier<Block> ILLUNITE_CLUSTER = registerBlockItem("illunite_cluster", ()->new IlluniteClusterBlock(7, 3, properties(Material.STONE, 0.5F).sound(ILLUNITE_SOUND).noOcclusion().hasPostProcess(BMBlocks::always).emissiveRendering(BMBlocks::always)).modifiers(RTypeModifier.CUTOUT));
    public static final RegistrySupplier<Block> LARGE_ILLUNITE_BUD = registerBlockItem("large_illunite_bud", ()->new IlluniteClusterBlock(5, 3, properties(Material.STONE, 0.5F).sound(ILLUNITE_SOUND).noOcclusion().hasPostProcess(BMBlocks::always).emissiveRendering(BMBlocks::always)).modifiers(RTypeModifier.CUTOUT));
    public static final RegistrySupplier<Block> MEDIUM_ILLUNITE_BUD = registerBlockItem("medium_illunite_bud", ()->new IlluniteClusterBlock(4, 3, properties(Material.STONE, 0.5F).sound(ILLUNITE_SOUND).noOcclusion().hasPostProcess(BMBlocks::always).emissiveRendering(BMBlocks::always)).modifiers(RTypeModifier.CUTOUT));
    public static final RegistrySupplier<Block> SMALL_ILLUNITE_BUD = registerBlockItem("small_illunite_bud", ()->new IlluniteClusterBlock(3, 4, properties(Material.STONE, 0.5F).sound(ILLUNITE_SOUND).noOcclusion().hasPostProcess(BMBlocks::always).emissiveRendering(BMBlocks::always)).modifiers(RTypeModifier.CUTOUT));

    public static final RegistrySupplier<Block> ILLUNITE_BLOCK = registerBlockItem("illunite_block", ()->new TBlock(properties(Material.STONE, 1.5F).requiresCorrectToolForDrops().sound(ILLUNITE_SOUND)));
    public static final RegistrySupplier<Block> BUDDING_ILLUNITE = registerBlockItem("budding_illunite", ()->new BuddingIlluniteBlock(properties(Material.STONE, 1.5F).noLootTable().randomTicks().sound(ILLUNITE_SOUND)));
    public static final RegistrySupplier<Block> MESMERITE = registerBlockItem("mesmerite", ()->new TBlock(properties(Material.STONE, 1.5F)));
    public static final DecorationBlockFactory MESMERITE_DECORATION = new DecorationBlockFactory(Constants.MOD_ID,  "mesmerite", MESMERITE, properties(Material.STONE, 1.5F), BiomeMakeover.TAB).all().register(BLOCKS, BMItems.ITEMS);;
    public static final RegistrySupplier<Block> POLISHED_MESMERITE = registerBlockItem("polished_mesmerite", ()->new TBlock(properties(Material.STONE, 1.5F)));
    public static final DecorationBlockFactory POLISHED_MESMERITE_DECORATION = new DecorationBlockFactory(Constants.MOD_ID, "polished_mesmerite", POLISHED_MESMERITE, properties(Material.STONE, 1.5F), BiomeMakeover.TAB).all().register(BLOCKS, BMItems.ITEMS);;
    public static WoodBlockFactory ANCIENT_OAK_WOOD_INFO = new WoodBlockFactory(Constants.MOD_ID, "ancient_oak", BiomeMakeover.TAB).all(()->BMBoats.ANCIENT_OAK).register(BLOCKS, BMItems.ITEMS);;

    public static final RegistrySupplier<Block> ANCIENT_OAK_SAPLING = sapling("ancient_oak_sapling", new AncientOakSaplingGenerator());
    public static final RegistrySupplier<Block> ANCIENT_OAK_LEAVES = registerBlockItem("ancient_oak_leaves", ()->new TLeavesBlock(properties(Material.LEAVES, 0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(BMBlocks::canSpawnOnLeaves).isSuffocating((a, b, c)->false).isViewBlocking((a, b, c)->false)).modifiers(RTypeModifier.create(RType.CUTOUT_MIPPED), FlammableModifier.LEAVES));
    public static final RegistrySupplier<Block> ALTAR = registerBlockItem("altar", ()->new AltarBlock(properties(Material.STONE, 5F).lightLevel((st)->st.getValue(AltarBlock.ACTIVE) ? 5 : 1).requiresCorrectToolForDrops().noOcclusion()).modifiers(RTypeModifier.CUTOUT));
    public static final RegistrySupplier<Block> CLADDED_STONE = registerBlockItem("cladded_stone", ()->new TBlock(properties(Material.STONE, 1.5F).requiresCorrectToolForDrops()));

    public static final RegistrySupplier<Block> ROOTLING_CROP = register("rootling_crop", ()->new RootlingCropBlock(properties(Material.PLANT, 0).noCollission().randomTicks().instabreak().sound(SoundType.CROP)).modifiers(RTypeModifier.CUTOUT));
    public static final RegistrySupplier<Block> IVY = registerBlockItem("ivy", ()->new IvyBlock(properties(Material.REPLACEABLE_PLANT, 0.15F).noCollission().randomTicks().sound(SoundType.VINE)).modifiers(RTypeModifier.CUTOUT, FlammableModifier.IVY));
    public static final RegistrySupplier<Block> ITCHING_IVY = registerBlockItem("itching_ivy", ()->new ItchingIvyBlock(properties(Material.REPLACEABLE_PLANT, 0.15F).speedFactor(0.5F).noCollission().randomTicks().sound(SoundType.VINE)).modifiers(RTypeModifier.CUTOUT, FlammableModifier.IVY));
    public static final RegistrySupplier<Block> MOTH_BLOSSOM = registerBlockItem("moth_blossom", ()->new MothBlossomBlock(properties(Material.PLANT, 0.25F).speedFactor(0.5F).noCollission().randomTicks().sound(SoundType.VINE)).modifiers(RTypeModifier.CUTOUT, FlammableModifier.IVY));
    public static final RegistrySupplier<Block> WILD_MUSHROOMS = registerBlockItem("wild_mushrooms", ()->new WildMushroomBlock(properties(Material.PLANT, 0F).noCollission().randomTicks().noOcclusion().sound(SoundType.FUNGUS)).modifiers(RTypeModifier.CUTOUT, FlammableModifier.IVY));

    public static final RegistrySupplier<Block> POTTED_MYCELIUM_ROOTS = pottedPlant("mycelium_roots",MYCELIUM_ROOTS);
    public static final RegistrySupplier<Block> POTTED_PURPLE_GLOWSHROOM =  pottedPlant("purple_glowshroom", PURPLE_GLOWSHROOM);
    public static final RegistrySupplier<Block> POTTED_GREEN_GLOWSHROOM =  pottedPlant("green_glowshroom", GREEN_GLOWSHROOM);
    public static final RegistrySupplier<Block> POTTED_ORANGE_GLOWSHROOM =  pottedPlant("orange_glowshroom", ORANGE_GLOWSHROOM);
    public static final RegistrySupplier<Block> POTTED_SAGUARO_CACTUS =  pottedPlant("saguaro_cactus", SAGUARO_CACTUS);
    public static final RegistrySupplier<Block> POTTED_BARREL_CACTUS =  pottedPlant("barrel_cactus", BARREL_CACTUS);
    public static final RegistrySupplier<Block> POTTED_FLOWERED_BARREL_CACTUS =  pottedPlant("flowered_barrel_cactus", BARREL_CACTUS_FLOWERED);
    public static final RegistrySupplier<Block> POTTED_WILLOW_SAPLING =  pottedPlant("willow_sapling", WILLOW_SAPLING);
    public static final RegistrySupplier<Block> POTTED_SWAMP_CYPRESS_SAPLING =  pottedPlant("swamp_cypress_sapling", SWAMP_CYPRESS_SAPLING);
    public static final RegistrySupplier<Block> POTTED_WILD_MUSHROOMS =  pottedPlant("wild_mushrooms", WILD_MUSHROOMS);

    public static final RegistrySupplier<Block> DIRECTIONAL_DATA =  registerBlockItem("directional_data", ()->new DirectionalDataBlock(BlockBehaviour.Properties.of(Material.STONE).strength(-1).noLootTable()));

    public static final Map<DyeColor, Supplier<Block>> DYE_TO_TAPESTRY = Maps.newHashMap();
    public static final List<Supplier<Block>> TAPESTRY_BLOCKS = Lists.newArrayList();
    public static final List<Supplier<Block>> TAPESTRY_WALL_BLOCKS = Lists.newArrayList();
    public static final List<Supplier<Block>> TAPESTRY_FLOOR_BLOCKS = Lists.newArrayList();
    public static RegistrySupplier<Block> ADJUDICATOR_TAPESTRY;
    static
    {
        for(DyeColor dyeColor : DyeColor.values())
        {
            Supplier<Block> tap = ()->new ColorTapestryBlock(dyeColor, properties(Material.WOOD, 1F).noCollission().sound(SoundType.WOOD));
            DYE_TO_TAPESTRY.put(dyeColor, tap);
        }
    }


    public static void init() {

        createTerracottaBricks();
        createTapestries();

        BLOCKS.register();

        DIRECTIONAL_DATA.listen((b)->{
            HoeItemHooks.addTillable(PEAT.get(), HoeItem::onlyIfAirAbove, HoeItem.changeIntoState(PEAT_FARMLAND.get().defaultBlockState()), (c)->PEAT_FARMLAND.get().defaultBlockState());
            HoeItemHooks.addTillable(MOSSY_PEAT.get(), HoeItem::onlyIfAirAbove, HoeItem.changeIntoState(PEAT_FARMLAND.get().defaultBlockState()), (c)->PEAT_FARMLAND.get().defaultBlockState());
        });
    }

    public static final Map<Block, RType> RTYPES = Maps.newHashMap();
    public static void initClient()
    {
        EnvExecutor.runInEnv(Env.CLIENT, () -> () ->
                RTYPES.forEach((block, type) -> RenderTypeRegistry.register(type.getAsRenderType(), block)));

    }

    public static Boolean canSpawnOnLeaves(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> type)
    {
        return type == EntityType.OCELOT || type == EntityType.PARROT;
    }

    public static final Map<Supplier<Block>, Block> BRICK_TO_TERRACOTTA = Maps.newHashMap();
    public static void createTerracottaBricks()
    {
        Map<DyeColor, Block> vanillaTerracotta = Maps.newHashMap();
        vanillaTerracotta.put(DyeColor.BLACK, Blocks.BLACK_TERRACOTTA);
        vanillaTerracotta.put(DyeColor.RED, Blocks.RED_TERRACOTTA);
        vanillaTerracotta.put(DyeColor.BLUE, Blocks.BLUE_TERRACOTTA);
        vanillaTerracotta.put(DyeColor.BROWN, Blocks.BROWN_TERRACOTTA);
        vanillaTerracotta.put(DyeColor.CYAN, Blocks.CYAN_TERRACOTTA);
        vanillaTerracotta.put(DyeColor.GRAY, Blocks.GRAY_TERRACOTTA);
        vanillaTerracotta.put(DyeColor.GREEN, Blocks.GREEN_TERRACOTTA);
        vanillaTerracotta.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_TERRACOTTA);
        vanillaTerracotta.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_TERRACOTTA);
        vanillaTerracotta.put(DyeColor.LIME, Blocks.LIME_TERRACOTTA);
        vanillaTerracotta.put(DyeColor.MAGENTA, Blocks.MAGENTA_TERRACOTTA);
        vanillaTerracotta.put(DyeColor.ORANGE, Blocks.ORANGE_TERRACOTTA);
        vanillaTerracotta.put(DyeColor.PINK, Blocks.PINK_TERRACOTTA);
        vanillaTerracotta.put(DyeColor.PURPLE, Blocks.PURPLE_TERRACOTTA);
        vanillaTerracotta.put(DyeColor.WHITE, Blocks.WHITE_TERRACOTTA);
        vanillaTerracotta.put(DyeColor.YELLOW, Blocks.YELLOW_TERRACOTTA);


        RegistrySupplier<Block> tBlock = registerBlockItem("terracotta_bricks", ()->new TBlock(properties(Material.STONE, 2F).requiresCorrectToolForDrops().sound(SoundType.STONE)));
        DecorationBlockFactory terracottaBrick = new DecorationBlockFactory(Constants.MOD_ID, "terracotta_brick", tBlock, properties(Material.STONE, 2F).requiresCorrectToolForDrops().sound(SoundType.STONE), BiomeMakeover.TAB).all().register(BLOCKS, BMItems.ITEMS);;

        for(DyeColor dye : DyeColor.values())
        {
            RegistrySupplier<Block> bl = registerBlockItem(dye.getName() + "_terracotta_bricks", ()->new TBlock(properties(Material.STONE, 2F).color(dye.getMaterialColor()).requiresCorrectToolForDrops().sound(SoundType.STONE)));

            DecorationBlockFactory dec = new DecorationBlockFactory(Constants.MOD_ID, dye.getName() + "_terracotta_brick", bl, properties(Material.STONE, 2F).color(dye.getMaterialColor()).requiresCorrectToolForDrops().sound(SoundType.STONE), BiomeMakeover.TAB).all().register(BLOCKS, BMItems.ITEMS);;
            BRICK_TO_TERRACOTTA.put(bl, vanillaTerracotta.get(dye));
        }
    }

    private static void createTapestries()
    {
        for(DyeColor dye : DyeColor.values())
        {
            RegistrySupplier<Block> tap = BLOCKS.register(BiomeMakeover.ID(dye.getName() + "_tapestry"), DYE_TO_TAPESTRY.get(dye));

            RegistrySupplier<Block> wallBlock = BLOCKS.register(BiomeMakeover.ID(dye.getName() + "_wall_tapestry"), ()->new ColorTapestryWallBlock(dye, properties(Material.WOOD, 1F).noCollission().sound(SoundType.WOOD).dropsLike(tap.get())));
            RegistrySupplier<Item> it = ItemHelper.registerItem(BMItems.ITEMS, BiomeMakeover.ID(dye.getName() + "_tapestry"), ()->new StandingAndWallBlockItem(tap.get(), wallBlock.get(), new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON), Direction.DOWN));

            initBlockItem(tap, it);
            initBlockItem(wallBlock, it);


            TAPESTRY_BLOCKS.add(tap);
            TAPESTRY_BLOCKS.add(wallBlock);
            TAPESTRY_FLOOR_BLOCKS.add(tap);
            TAPESTRY_WALL_BLOCKS.add(wallBlock);
        }
        ADJUDICATOR_TAPESTRY = BLOCKS.register(BiomeMakeover.ID("adjudicator_tapestry"), ()->new AdjudicatorTapestryBlock(properties(Material.WOOD, 1F).noCollission().sound(SoundType.WOOD)));

        RegistrySupplier<Block> adjWall = BLOCKS.register(BiomeMakeover.ID("adjudicator_wall_tapestry"), ()->new AdjudicatorTapestryWallBlock(properties(Material.WOOD, 1F).noCollission().sound(SoundType.WOOD).dropsLike(ADJUDICATOR_TAPESTRY.get())));

        RegistrySupplier<Item> it = ItemHelper.registerItem(BMItems.ITEMS, BiomeMakeover.ID("adjudicator_tapestry"), ()->new StandingAndWallBlockItem(ADJUDICATOR_TAPESTRY.get(), adjWall.get(), new Item.Properties().stacksTo(16).rarity(Rarity.EPIC), Direction.DOWN));
        initBlockItem(adjWall, it);
        initBlockItem(ADJUDICATOR_TAPESTRY, it);

        TAPESTRY_BLOCKS.add(ADJUDICATOR_TAPESTRY);
        TAPESTRY_BLOCKS.add(adjWall);
    }

    public static RegistrySupplier<Block> registerBlockItemPot(String id, Supplier<Block> block)
    {
        RegistrySupplier<Block> baseBlock = registerBlockItem(id, block);
        pottedPlant(id, baseBlock);

        return baseBlock;
    }

    public static RegistrySupplier<Block> registerBlockItem(String id, Supplier<Block> block)
    {
        RegistrySupplier<Block> bl = registerBlock(BLOCKS, BiomeMakeover.ID(id), block);
        RegistrySupplier<Item> it = ItemHelper.registerItem(BMItems.ITEMS, BiomeMakeover.ID(id), ()->new BlockItem(bl.get(), new Item.Properties()));
        initBlockItem(bl, it);

        return bl;
    }

    public static RegistrySupplier<Block> registerBlock(DeferredRegister<Block> register, ResourceLocation location, Supplier<Block> blockSupplier)
    {
        RegistrySupplier<Block> registered = register.register(location, blockSupplier);
        register.getRegistrar().listen(location, BlockHelper.MODIFIER_CONSUMER);

        return registered;
    }

    public static RegistrySupplier<Block> register(String id, Supplier<Block> block)
    {
        RegistrySupplier<Block> bl = registerBlock(BLOCKS, BiomeMakeover.ID(id), block);
        return bl;
    }

    private static RegistrySupplier<Block> pottedPlant(String base_name, RegistrySupplier<Block> baseBlock)
    {
        RegistrySupplier<Block> pottedPlant = register("potted_" + base_name, ()->new TFlowerPotBlock(baseBlock.get(), properties(Material.DECORATION, 0).instabreak().noCollission().sound(SoundType.NETHER_SPROUTS)).modifiers(party.lemons.taniwha.block.modifier.RTypeModifier.CUTOUT));
        return pottedPlant;
    }

    private static RegistrySupplier<Block> sapling(String name, AbstractTreeGrower grower)
    {
        RegistrySupplier<Block> sapling = registerBlockItem(name, ()->new TSaplingBlock(grower, BlockBehaviour.Properties.of(Material.PLANT).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)).modifiers(party.lemons.taniwha.block.modifier.RTypeModifier.CUTOUT));
        pottedPlant(name, sapling);
        return sapling;
    }

    private static RegistrySupplier<Block> leaves(String name)
    {
        return registerBlockItem(name, ()->new TLeavesBlock(LEAF_PROPERTIES).modifiers(LEAF_MODIFIERS));
    }

    public static BlockBehaviour.Properties properties(Material material, float strength)
    {
        return properties(material, strength, strength);
    }

    public static BlockBehaviour.Properties properties(Material material, float breakSpeed, float explosionResist)
    {
        return BlockBehaviour.Properties.of(material).strength(breakSpeed, explosionResist);
    }

    private static boolean always(BlockState state, BlockGetter world, BlockPos pos)
    {
        return true;
    }

    public static RegistrySupplier<Block> registerLilyPad(String id, Supplier<Block> block)
    {
        RegistrySupplier<Block> bl = register(id, block);
        RegistrySupplier<Item> it = ItemHelper.registerItem(BMItems.ITEMS, BiomeMakeover.ID(id), ()->new PlaceOnWaterBlockItem(bl.get(), new Item.Properties()));
        initBlockItem(bl, it);
        return bl;
    }

    public static void initBlockItem(RegistrySupplier<Block> block, RegistrySupplier<Item> item)
    {
        BLOCK_ITEMS.put(block, item);
    }


    public static final TagKey<Block> LILY_PADS = TagKey.create(Registries.BLOCK, BiomeMakeover.ID("lily_pads"));
    public static final TagKey<Block> MOTH_ATTRACTIVE = TagKey.create(Registries.BLOCK, BiomeMakeover.ID("moth_attractive"));
    public static final TagKey<Block> IVY_TAG = TagKey.create(Registries.BLOCK, BiomeMakeover.ID("ivy"));
    public static final TagKey<Block> ITCHING_IVY_TAG = TagKey.create(Registries.BLOCK, BiomeMakeover.ID("itching_ivy"));
    public static final TagKey<Block> ORE_REPLACEABLE = TagKey.create(Registries.BLOCK, BiomeMakeover.ID("ore_replaceable"));
    public static final TagKey<Block> CRAB_SPAWNABLE_ON = TagKey.create(Registries.BLOCK, BiomeMakeover.ID("crab_spawnable_on"));
    public static final TagKey<Block> FISSURE_NO_REPLACE = TagKey.create(Registries.BLOCK, BiomeMakeover.ID("fissure_no_replace"));
}
