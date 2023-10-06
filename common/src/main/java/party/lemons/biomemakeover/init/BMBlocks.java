package party.lemons.biomemakeover.init;

import com.google.common.collect.ImmutableMap;
import dev.architectury.hooks.item.tool.HoeItemHooks;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
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
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
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
import party.lemons.taniwha.hooks.block.entity.BlockEntityHooks;
import party.lemons.taniwha.item.ItemHelper;
import party.lemons.taniwha.util.BlockUtil;

import java.util.*;
import java.util.function.Supplier;

public class BMBlocks
{
    public static final TagKey<Block> LILY_PADS = blockTag("lily_pads");
    public static final TagKey<Block> MOTH_ATTRACTIVE = blockTag("moth_attractive");
    public static final TagKey<Block> IVY_TAG = blockTag("ivy");
    public static final TagKey<Block> ITCHING_IVY_TAG = blockTag("itching_ivy");
    public static final TagKey<Block> ORE_REPLACEABLE = blockTag("ore_replaceable");
    public static final TagKey<Block> CRAB_SPAWNABLE_ON = blockTag("crab_spawnable_on");
    public static final TagKey<Block> FISSURE_NO_REPLACE = blockTag("fissure_no_replace");
    public static final TagKey<Block> BARREL_CACTUS_PLANTABLE = blockTag("barrel_cactus_plantable_on");
    public static final TagKey<Block> SAGUARO_CACTUS_PLANTABLE = blockTag("saguaro_cactus_plantable_on");

    public static final Map<RegistrySupplier<Block>, RegistrySupplier<Item>> BLOCK_ITEMS = new HashMap<>();

    private static final BlockBehaviour.Properties LEAF_PROPERTIES = properties(0.2F)
        .mapColor(MapColor.PLANT)
				.randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(BMBlocks::canSpawnOnLeaves).isSuffocating(BMBlocks::never).isViewBlocking(BMBlocks::never).ignitedByLava().pushReaction(PushReaction.DESTROY).isRedstoneConductor(BMBlocks::never);

    private static final BlockModifier[] LEAF_MODIFIERS = new BlockModifier[]{RTypeModifier.create(RType.CUTOUT),FlammableModifier.LEAVES};

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Constants.MOD_ID, Registries.BLOCK);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Constants.MOD_ID, Registries.ITEM);

    public static final SoundType BM_LILY_PAD_SOUND = new SoundType(1.0F, 1.0F, SoundEvents.WET_GRASS_BREAK, SoundEvents.WET_GRASS_STEP, SoundEvents.LILY_PAD_PLACE, SoundEvents.WET_GRASS_HIT, SoundEvents.WET_GRASS_FALL);
    public static final SoundType ILLUNITE_SOUND = new BMSoundType(1.0F, 1.0F, BMEffects.ILLUNITE_BREAK, BMEffects.ILLUNITE_STEP, BMEffects.ILLUNITE_PLACE, BMEffects.ILLUNITE_HIT, ()->SoundEvents.STONE_FALL);

    public static final RegistrySupplier<Block> PURPLE_GLOWSHROOM = registerBlockItem("purple_glowshroom", ()->new GlowshroomPlantBlock(ResourceKey.create(Registries.CONFIGURED_FEATURE, BiomeMakeover.ID("mushroom_fields/huge_purple_glowshroom")), properties(0F).pushReaction(PushReaction.DESTROY).pushReaction(PushReaction.DESTROY).mapColor(MapColor.COLOR_PINK).lightLevel((s)->13).noCollission().noCollission().sound(SoundType.FUNGUS)).modifiers(RTypeModifier.CUTOUT));
    public static final RegistrySupplier<Block> GREEN_GLOWSHROOM = registerBlockItem("green_glowshroom", ()->new GlowshroomPlantBlock(ResourceKey.create(Registries.CONFIGURED_FEATURE, BiomeMakeover.ID("mushroom_fields/huge_green_glowshroom")), properties(0F).pushReaction(PushReaction.DESTROY).pushReaction(PushReaction.DESTROY).mapColor(MapColor.GLOW_LICHEN).lightLevel((s)->13).noCollission().noOcclusion().sound(SoundType.FUNGUS)).modifiers(RTypeModifier.CUTOUT));
    public static final RegistrySupplier<Block> ORANGE_GLOWSHROOM = registerBlockItem("orange_glowshroom", ()->new UnderwaterMushroomPlantBlock(ResourceKey.create(Registries.CONFIGURED_FEATURE, BiomeMakeover.ID("mushroom_fields/huge_orange_glowshroom")), properties(0F).pushReaction(PushReaction.DESTROY).pushReaction(PushReaction.DESTROY).mapColor(MapColor.SAND).lightLevel((s)->13).noCollission().noOcclusion().sound(SoundType.FUNGUS)).modifiers(RTypeModifier.CUTOUT));

    public static final RegistrySupplier<Block> PURPLE_GLOWSHROOM_BLOCK = registerBlockItem("purple_glowshroom_block", ()->new BMMushroomBlock(properties(0.2F).lightLevel((s)->15).sound(SoundType.FUNGUS).instrument(NoteBlockInstrument.BASS).mapColor(MapColor.COLOR_PINK)));
    public static final RegistrySupplier<Block> GREEN_GLOWSHROOM_BLOCK = registerBlockItem("green_glowshroom_block", ()->new BMMushroomBlock(properties(0.2F).lightLevel((s)->15).sound(SoundType.FUNGUS).instrument(NoteBlockInstrument.BASS).mapColor(MapColor.GLOW_LICHEN)));
    public static final RegistrySupplier<Block> ORANGE_GLOWSHROOM_BLOCK = registerBlockItem("orange_glowshroom_block", ()->new BMMushroomBlock(properties(0.2F).lightLevel((s)->15).sound(SoundType.FUNGUS).instrument(NoteBlockInstrument.BASS).mapColor(MapColor.SAND)));

    public static final RegistrySupplier<Block> MYCELIUM_SPROUTS = registerBlockItem("mycelium_sprouts", ()->new MushroomSproutsBlock(properties(0).replaceable().pushReaction(PushReaction.DESTROY).noCollission().noCollission().instabreak().sound(SoundType.NETHER_SPROUTS).mapColor(MapColor.TERRACOTTA_PURPLE)).modifiers(RTypeModifier.CUTOUT));
    public static final RegistrySupplier<Block> MYCELIUM_ROOTS = registerBlockItem("mycelium_roots", ()->new MushroomRootsBlock(properties(0).replaceable().pushReaction(PushReaction.DESTROY).noCollission().noCollission().instabreak().sound(SoundType.ROOTS).mapColor(MapColor.TERRACOTTA_PURPLE)).modifiers(RTypeModifier.CUTOUT));

    public static final RegistrySupplier<Block> TALL_BROWN_MUSHROOM = registerBlockItem("tall_brown_mushroom", ()->new BMTallMushroomBlock(properties(0).pushReaction(PushReaction.DESTROY).instabreak().noCollission().sound(SoundType.FUNGUS).mapColor(MapColor.DIRT)).modifiers(RTypeModifier.CUTOUT));
    public static final RegistrySupplier<Block> TALL_RED_MUSHROOM = registerBlockItem("tall_red_mushroom", ()->new BMTallMushroomBlock(properties(0).pushReaction(PushReaction.DESTROY).instabreak().noCollission().sound(SoundType.FUNGUS).mapColor(MapColor.COLOR_RED)).modifiers(RTypeModifier.CUTOUT));

    public static final WoodBlockFactory BLIGHTED_BALSA_WOOD_INFO = new WoodBlockFactory(Constants.MOD_ID, "blighted_balsa", BMTab.TAB).color(MapColor.TERRACOTTA_GREEN, MapColor.WOOL).all(()-> BMBoats.BLIGHTED_BALSA).register(BLOCKS, ITEMS);
    public static final RegistrySupplier<Block> BLIGHTED_BALSA_LEAVES = registerBlockItem("blighted_balsa_leaves", ()->new TLeavesBlock(BlockUtil.copyProperties(LEAF_PROPERTIES).mapColor(MapColor.ICE)).modifiers(RTypeModifier.CUTOUT, FlammableModifier.LEAVES));
    public static final RegistrySupplier<Block> BLIGHTED_BALSA_SAPLING = sapling("blighted_balsa_sapling", new BalsaSaplingGenerator(), MapColor.WOOL);

    public static final RegistrySupplier<Block> GLOWSHROOM_STEM = registerBlockItem("glowshroom_stem", ()->new BMMushroomBlock(properties(0.2F).mapColor(MapColor.CLAY).instrument(NoteBlockInstrument.BASS).lightLevel((s)->7).sound(SoundType.FUNGUS)));
    public static final RegistrySupplier<Block> RED_MUSHROOM_BRICK = registerBlockItem("red_mushroom_brick", ()->new TBlock(properties(0.8F).sound(SoundType.FUNGUS).instrument(NoteBlockInstrument.BASS).mapColor(MapColor.COLOR_RED)));
    public static final DecorationBlockFactory RED_MUSHROOM_BRICK_DECORATION = new DecorationBlockFactory(Constants.MOD_ID, "red_mushroom_brick", RED_MUSHROOM_BRICK, properties(0.8F).instrument(NoteBlockInstrument.BASS).mapColor(MapColor.COLOR_RED).sound(SoundType.FUNGUS), BMTab.TAB).all().register(BLOCKS, ITEMS);
    public static final RegistrySupplier<Block> BROWN_MUSHROOM_BRICK = registerBlockItem("brown_mushroom_brick", ()->new TBlock(properties(0.8F).sound(SoundType.FUNGUS).instrument(NoteBlockInstrument.BASS).mapColor(MapColor.COLOR_BROWN)));
    public static final DecorationBlockFactory BROWN_MUSHROOM_BRICK_DECORATION = new DecorationBlockFactory(Constants.MOD_ID, "brown_mushroom_brick", BROWN_MUSHROOM_BRICK, properties(0.8F).instrument(NoteBlockInstrument.BASS).instrument(NoteBlockInstrument.BASS).mapColor(MapColor.COLOR_BROWN).sound(SoundType.FUNGUS), BMTab.TAB).all().register(BLOCKS, ITEMS);
    public static final RegistrySupplier<Block> PURPLE_GLOWSHROOM_BRICK = registerBlockItem("purple_glowshroom_brick", ()->new TBlock(properties(0.8F).lightLevel((s)->13).mapColor(MapColor.COLOR_PURPLE).instrument(NoteBlockInstrument.BASS).sound(SoundType.FUNGUS)));
    public static final DecorationBlockFactory PURPLE_GLOWSROOM_BRICK_DECORATION = new DecorationBlockFactory(Constants.MOD_ID, "purple_glowshroom_brick", PURPLE_GLOWSHROOM_BRICK, properties(0.8F).instrument(NoteBlockInstrument.BASS).mapColor(MapColor.COLOR_PURPLE).lightLevel((s)->13).sound(SoundType.FUNGUS), BMTab.TAB).all().register(BLOCKS, ITEMS);
    public static final RegistrySupplier<Block> GREEN_GLOWSHROOM_BRICK = registerBlockItem("green_glowshroom_brick", ()->new TBlock(properties(0.8F).lightLevel((s)->13).sound(SoundType.FUNGUS).instrument(NoteBlockInstrument.BASS).mapColor(MapColor.GLOW_LICHEN)));
    public static final DecorationBlockFactory GREEN_GLOWSROOM_BRICK_DECORATION = new DecorationBlockFactory(Constants.MOD_ID, "green_glowshroom_brick", GREEN_GLOWSHROOM_BRICK, properties(0.8F).instrument(NoteBlockInstrument.BASS).mapColor(MapColor.GLOW_LICHEN).lightLevel((s)->13).sound(SoundType.FUNGUS), BMTab.TAB).all().register(BLOCKS, ITEMS);
    public static final RegistrySupplier<Block> ORANGE_GLOWSHROOM_BRICK = registerBlockItem("orange_glowshroom_brick", ()->new TBlock(properties(0.8F).instrument(NoteBlockInstrument.BASS).lightLevel((s)->13).sound(SoundType.FUNGUS).mapColor(MapColor.COLOR_ORANGE)));
    public static final DecorationBlockFactory ORANGE_GLOWSROOM_BRICK_DECORATION = new DecorationBlockFactory(Constants.MOD_ID, "orange_glowshroom_brick", ORANGE_GLOWSHROOM_BRICK, properties(0.8F).instrument(NoteBlockInstrument.BASS).mapColor(MapColor.COLOR_ORANGE).lightLevel((s)->13).sound(SoundType.FUNGUS), BMTab.TAB).all().register(BLOCKS, ITEMS);
    public static final RegistrySupplier<Block> GLOWSHROOM_STEM_BRICK = registerBlockItem("glowshroom_stem_brick", ()->new TBlock(properties(0.8F).lightLevel((s)->7).instrument(NoteBlockInstrument.BASS).sound(SoundType.FUNGUS).mapColor(MapColor.CLAY)));
    public static final DecorationBlockFactory GLOWSHROOM_STEM_BRICK_DECORATION = new DecorationBlockFactory(Constants.MOD_ID, "glowshroom_stem_brick", GLOWSHROOM_STEM_BRICK, properties(0.8F).instrument(NoteBlockInstrument.BASS).lightLevel((s)->7).mapColor(MapColor.CLAY).sound(SoundType.FUNGUS), BMTab.TAB).all().register(BLOCKS, ITEMS);
    public static final RegistrySupplier<Block> MUSHROOM_STEM_BRICK = registerBlockItem("mushroom_stem_brick", ()->new TBlock(properties(0.8F).instrument(NoteBlockInstrument.BASS).sound(SoundType.FUNGUS).mapColor(MapColor.WOOL)));
    public static final DecorationBlockFactory MUSHROOM_STEM_BRICK_DECORATION = new DecorationBlockFactory(Constants.MOD_ID, "mushroom_stem_brick", MUSHROOM_STEM_BRICK, properties(0.8F).instrument(NoteBlockInstrument.BASS).mapColor(MapColor.WOOL).sound(SoundType.FUNGUS), BMTab.TAB).all().register(BLOCKS, ITEMS);
    public static final RegistrySupplier<Block> BLIGHTED_COBBLESTONE = registerBlockItem("blighted_cobblestone", ()->new TBlock(properties(2).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.STONE).mapColor(MapColor.STONE).requiresCorrectToolForDrops()));
    public static final DecorationBlockFactory BLIGHTED_COBBLESTONE_DECORATION = new DecorationBlockFactory(Constants.MOD_ID, "blighted_cobblestone", BLIGHTED_COBBLESTONE, properties(2F).instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE), BMTab.TAB).all().register(BLOCKS, ITEMS);
    public static final RegistrySupplier<Block> BLIGHTED_STONE_BRICKS = registerBlockItem("blighted_stone_bricks", ()->new TBlock(properties(2).sound(SoundType.STONE).instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops()));
    public static final DecorationBlockFactory BLIGHTED_STONE_BRICKS_DECORATION = new DecorationBlockFactory(Constants.MOD_ID, "blighted_stone_bricks", BLIGHTED_STONE_BRICKS, properties(2F).instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE).requiresCorrectToolForDrops().sound(SoundType.STONE), BMTab.TAB).all().register(BLOCKS, ITEMS);
    public static final RegistrySupplier<Block> PAYDIRT = registerBlockItem("paydirt", ()->new TBlock(properties(1.4F).requiresCorrectToolForDrops().sound(SoundType.GRAVEL).mapColor(MapColor.TERRACOTTA_GRAY)));

    public static final RegistrySupplier<Block> TUMBLEWEED = BLOCKS.register(BiomeMakeover.ID("tumbleweed"), ()->new Block(properties(0).mapColor(MapColor.COLOR_YELLOW)));

    public static final RegistrySupplier<Block> SAGUARO_CACTUS = registerBlockItem("saguaro_cactus", ()->new SaguaroCactusBlock(properties(0.4F).mapColor(MapColor.COLOR_GREEN).pushReaction(PushReaction.DESTROY).sound(SoundType.WOOL).randomTicks()).modifiers(RTypeModifier.CUTOUT));
    public static final RegistrySupplier<Block> BARREL_CACTUS = registerBlockItem("barrel_cactus", ()->new BarrelCactusBlock(false, properties(0).mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).randomTicks().sound(SoundType.WOOL).noOcclusion().instabreak().noCollission()).modifiers(RTypeModifier.CUTOUT));
    public static final RegistrySupplier<Block> BARREL_CACTUS_FLOWERED = registerBlockItem("barrel_cactus_flowered", ()->new BarrelCactusBlock(true, properties(0).pushReaction(PushReaction.DESTROY).mapColor(MapColor.COLOR_PINK).sound(SoundType.WOOL).noOcclusion().instabreak().noCollission()).modifiers(RTypeModifier.CUTOUT));

    public static final RegistrySupplier<Block> POLTERGEIST = registerBlockItem("poltergeist", ()->new PoltergeistBlock(properties(1.0F).pushReaction(PushReaction.BLOCK).lightLevel((bs)->bs.getValue(PoltergeistBlock.ENABLED) ? 7 : 0).mapColor(MapColor.WARPED_WART_BLOCK).sound(SoundType.LODESTONE)).modifiers(RTypeModifier.CUTOUT));
    public static final RegistrySupplier<Block> ECTOPLASM_COMPOSTER = BLOCKS.register(BiomeMakeover.ID("ectoplasm_composter"), ()->new EctoplasmComposterBlock(properties(0.6F).ignitedByLava().sound(SoundType.WOOD).mapColor(MapColor.WOOD)));
    public static final RegistrySupplier<Block> PEAT_COMPOSTER = BLOCKS.register(BiomeMakeover.ID("peat_composter"), ()->new PeatComposterBlock(properties(0.6F).instrument(NoteBlockInstrument.BASS).sound(SoundType.WOOD).ignitedByLava().mapColor(MapColor.WOOD)));

    public static final WoodBlockFactory WILLOW_WOOD_INFO = new WoodBlockFactory(Constants.MOD_ID, "willow", BMTab.TAB).color(MapColor.TERRACOTTA_GRAY, MapColor.SAND).all(()->BMBoats.WILLOW).register(BLOCKS, ITEMS);
    public static final WoodBlockFactory SWAMP_CYPRESS_WOOD_INFO = new WoodBlockFactory(Constants.MOD_ID, "swamp_cypress", BMTab.TAB).color(MapColor.TERRACOTTA_BROWN, MapColor.TERRACOTTA_ORANGE).all(()->BMBoats.SWAMP_CYPRESS).register(BLOCKS, ITEMS);

    public static final RegistrySupplier<Block> WILLOWING_BRANCHES = registerBlockItem("willowing_branches", ()->new WillowingBranchesBlock(properties(0.1F).pushReaction(PushReaction.DESTROY).randomTicks().sound(SoundType.VINE).noCollission().noOcclusion().ignitedByLava().mapColor(MapColor.PLANT)).modifiers(RTypeModifier.create(RType.CUTOUT_MIPPED), new FlammableModifier(15, 100)));
    public static final RegistrySupplier<Block> WILLOW_SAPLING = registerBlockItem("willow_sapling", ()->new WaterSaplingBlock(new WillowSaplingGenerator(), 1, properties(0).noCollission().randomTicks().pushReaction(PushReaction.DESTROY).instabreak().mapColor(MapColor.PLANT).sound(SoundType.GRASS)).modifiers(RTypeModifier.CUTOUT));
    public static final RegistrySupplier<Block> SWAMP_CYPRESS_SAPLING = registerBlockItem("swamp_cypress_sapling", ()->new WaterSaplingBlock(new SwampCypressSaplingGenerator(), 3, properties(0).pushReaction(PushReaction.DESTROY).noCollission().randomTicks().mapColor(MapColor.PLANT).instabreak().sound(SoundType.GRASS)).modifiers(RTypeModifier.CUTOUT));
    public static final RegistrySupplier<Block> PEAT = registerBlockItem("peat", ()->new TBlock(properties(0.5F).sound(SoundType.WET_GRASS).mapColor(MapColor.TERRACOTTA_GRAY)));
    public static final RegistrySupplier<Block> DRIED_PEAT = registerBlockItem("dried_peat", ()->new TBlock(properties(1F).sound(SoundType.NETHERRACK).mapColor(MapColor.TERRACOTTA_BROWN)));
    public static final RegistrySupplier<Block> MOSSY_PEAT = registerBlockItem("mossy_peat", ()->new TSpreadableBlock(properties(0.5F).randomTicks().sound(SoundType.WET_GRASS), PEAT));
    public static final RegistrySupplier<Block> PEAT_FARMLAND = registerBlockItem("peat_farmland", ()->new PeatFarmlandBlock(properties(0.5F).sound(SoundType.WET_GRASS).mapColor(MapColor.COLOR_GREEN).randomTicks().noOcclusion()));
    public static final RegistrySupplier<Block> DRIED_PEAT_BRICKS = registerBlockItem("dried_peat_bricks", ()->new TBlock(properties(2).sound(SoundType.STONE).mapColor(MapColor.TERRACOTTA_BROWN).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops()));
    public static final DecorationBlockFactory DRIED_PEAT_BRICKS_DECORATION = new DecorationBlockFactory(Constants.MOD_ID, "dried_peat_bricks", DRIED_PEAT_BRICKS, properties(2F).instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.TERRACOTTA_BROWN).sound(SoundType.NETHERRACK).requiresCorrectToolForDrops().sound(SoundType.STONE), BMTab.TAB).all().register(BLOCKS, ITEMS);
    public static final RegistrySupplier<Block> MOSSY_DRIED_PEAT_BRICKS = registerBlockItem("mossy_dried_peat_bricks", ()->new TBlock(properties(2).sound(SoundType.STONE).instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.TERRACOTTA_BROWN).requiresCorrectToolForDrops()));
    public static final DecorationBlockFactory MOSSY_DRIED_PEAT_BRICKS_DECORATION = new DecorationBlockFactory(Constants.MOD_ID, "mossy_dried_peat_brick", MOSSY_DRIED_PEAT_BRICKS, properties(2F).instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.TERRACOTTA_BROWN).sound(SoundType.NETHERRACK).requiresCorrectToolForDrops().sound(SoundType.STONE), BMTab.TAB).all().register(BLOCKS, ITEMS);
    public static final RegistrySupplier<Block> CRACKED_DRIED_PEAT_BRICKS = registerBlockItem("cracked_dried_peat_bricks", ()->new TBlock(properties(2).mapColor(MapColor.TERRACOTTA_BROWN).instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DecorationBlockFactory CRACKED_DRIED_PEAT_BRICKS_DECORATION = new DecorationBlockFactory(Constants.MOD_ID, "cracked_dried_peat_brick", CRACKED_DRIED_PEAT_BRICKS, properties(2F).instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.TERRACOTTA_BROWN).sound(SoundType.NETHERRACK).requiresCorrectToolForDrops().sound(SoundType.STONE), BMTab.TAB).all().register(BLOCKS, ITEMS);
    public static final RegistrySupplier<Block> SWAMP_AZALEA = registerBlockItem("buttonbush", ()->new TTallFlowerBlock(properties(0).pushReaction(PushReaction.DESTROY).noCollission().instabreak().sound(SoundType.GRASS).mapColor(MapColor.WOOL)).modifiers(RTypeModifier.CUTOUT, FlammableModifier.TALL_FLOWER));
    public static final RegistrySupplier<Block> MARIGOLD = registerBlockItem("marigold", ()->new TTallFlowerBlock(properties(0).pushReaction(PushReaction.DESTROY).noCollission().instabreak().sound(SoundType.GRASS).mapColor(MapColor.COLOR_ORANGE)).modifiers(RTypeModifier.CUTOUT, FlammableModifier.TALL_FLOWER));
    public static final RegistrySupplier<Block> BLACK_THISTLE = registerBlockItem("black_thistle", ()->new BlackThistleBlock(properties(0).pushReaction(PushReaction.DESTROY).noCollission().instabreak().sound(SoundType.GRASS).mapColor(MapColor.TERRACOTTA_BLACK)).modifiers(RTypeModifier.CUTOUT, FlammableModifier.TALL_FLOWER));
    public static final RegistrySupplier<Block> FOXGLOVE = registerBlockItem("foxglove", ()->new TTallFlowerBlock(properties(0).pushReaction(PushReaction.DESTROY).noCollission().instabreak().sound(SoundType.GRASS).mapColor(MapColor.COLOR_PINK)).modifiers(RTypeModifier.CUTOUT, FlammableModifier.TALL_FLOWER));

    public static final RegistrySupplier<Block> CATTAIL = registerBlockItem("cattail", ()->new ReedBlock(properties(0).pushReaction(PushReaction.DESTROY).instabreak().noCollission().sound(SoundType.GRASS).mapColor(MapColor.GLOW_LICHEN)).modifiers(RTypeModifier.CUTOUT));
    public static final RegistrySupplier<Block> REED = registerBlockItem("reed", ()->new ReedBlock(properties(0).pushReaction(PushReaction.DESTROY).instabreak().noCollission().sound(SoundType.GRASS).mapColor(MapColor.GLOW_LICHEN)).modifiers(RTypeModifier.CUTOUT));
    public static final RegistrySupplier<Block> SMALL_LILY_PAD = registerLilyPad("small_lily_pad", ()->new SmallLilyPadBlock(properties(0).pushReaction(PushReaction.DESTROY).instabreak().sound(BM_LILY_PAD_SOUND).mapColor(MapColor.PLANT)).modifiers(RTypeModifier.CUTOUT), true);
    public static final RegistrySupplier<Block> WATER_LILY = registerLilyPad("water_lily", ()->new FloweredWaterlilyPadBlock(properties(0).pushReaction(PushReaction.DESTROY).instabreak().sound(BM_LILY_PAD_SOUND).mapColor(MapColor.COLOR_PINK)).modifiers(RTypeModifier.CUTOUT), true);
    public static final RegistrySupplier<Block> WILLOW_LEAVES = registerBlockItem("willow_leaves", ()->new TLeavesBlock(LEAF_PROPERTIES.mapColor(MapColor.TERRACOTTA_LIGHT_GREEN)).modifiers(RTypeModifier.create(RType.CUTOUT_MIPPED), FlammableModifier.LEAVES));
    public static final RegistrySupplier<Block> SWAMP_CYPRESS_LEAVES = registerBlockItem("swamp_cypress_leaves", ()->new TLeavesBlock(BlockUtil.copyProperties(LEAF_PROPERTIES).mapColor(MapColor.TERRACOTTA_GREEN)).modifiers(RTypeModifier.create(RType.CUTOUT_MIPPED), FlammableModifier.LEAVES));
    public static final RegistrySupplier<Block> LIGHTNING_BUG_BOTTLE = registerBlockItem("lightning_bug_bottle", ()->new LightningBugBottleBlock(properties(0.5F).pushReaction(PushReaction.DESTROY).lightLevel((b)->15).noOcclusion().mapColor(MapColor.NONE)).modifiers(RTypeModifier.CUTOUT));

    public static final RegistrySupplier<Block> ILLUNITE_CLUSTER = registerBlockItem("illunite_cluster", ()->new IlluniteClusterBlock(7, 3, 15, properties(0.5F).sound(ILLUNITE_SOUND).pushReaction(PushReaction.DESTROY).mapColor(MapColor.LAPIS).noOcclusion().hasPostProcess(BMBlocks::always).emissiveRendering(BMBlocks::always)).modifiers(RTypeModifier.CUTOUT));
    public static final RegistrySupplier<Block> LARGE_ILLUNITE_BUD = registerBlockItem("large_illunite_bud", ()->new IlluniteClusterBlock(5, 3, 13, properties(0.5F).sound(ILLUNITE_SOUND).pushReaction(PushReaction.DESTROY).mapColor(MapColor.LAPIS).noOcclusion().hasPostProcess(BMBlocks::always).emissiveRendering(BMBlocks::always)).modifiers(RTypeModifier.CUTOUT));
    public static final RegistrySupplier<Block> MEDIUM_ILLUNITE_BUD = registerBlockItem("medium_illunite_bud", ()->new IlluniteClusterBlock(4, 3, 7, properties(0.5F).sound(ILLUNITE_SOUND).pushReaction(PushReaction.DESTROY).mapColor(MapColor.LAPIS).noOcclusion().hasPostProcess(BMBlocks::always).emissiveRendering(BMBlocks::always)).modifiers(RTypeModifier.CUTOUT));
    public static final RegistrySupplier<Block> SMALL_ILLUNITE_BUD = registerBlockItem("small_illunite_bud", ()->new IlluniteClusterBlock(3, 4, 5, properties(0.5F).sound(ILLUNITE_SOUND).pushReaction(PushReaction.DESTROY).mapColor(MapColor.LAPIS).noOcclusion().hasPostProcess(BMBlocks::always).emissiveRendering(BMBlocks::always)).modifiers(RTypeModifier.CUTOUT));

    public static final RegistrySupplier<Block> ILLUNITE_BLOCK = registerBlockItem("illunite_block", ()->new TBlock(properties(1.5F).mapColor(MapColor.LAPIS).requiresCorrectToolForDrops().sound(ILLUNITE_SOUND)));
    public static final RegistrySupplier<Block> BUDDING_ILLUNITE = registerBlockItem("budding_illunite", ()->new BuddingIlluniteBlock(properties(1.5F).mapColor(MapColor.LAPIS).noLootTable().randomTicks().sound(ILLUNITE_SOUND)));
    public static final RegistrySupplier<Block> MESMERITE = registerBlockItem("mesmerite", ()->new TBlock(properties(1.5F).instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.ICE)));
    public static final DecorationBlockFactory MESMERITE_DECORATION = new DecorationBlockFactory(Constants.MOD_ID,  "mesmerite", MESMERITE, properties(1.5F).instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.ICE), BMTab.TAB).all().register(BLOCKS, ITEMS);
    public static final RegistrySupplier<Block> POLISHED_MESMERITE = registerBlockItem("polished_mesmerite", ()->new TBlock(properties(1.5F).instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.ICE)));
    public static final DecorationBlockFactory POLISHED_MESMERITE_DECORATION = new DecorationBlockFactory(Constants.MOD_ID, "polished_mesmerite", POLISHED_MESMERITE, properties(1.5F).instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.ICE), BMTab.TAB).all().register(BLOCKS, ITEMS);
    public static final WoodBlockFactory ANCIENT_OAK_WOOD_INFO = new WoodBlockFactory(Constants.MOD_ID, "ancient_oak", BMTab.TAB).color(MapColor.TERRACOTTA_BLACK, MapColor.TERRACOTTA_BROWN).all(()->BMBoats.ANCIENT_OAK).register(BLOCKS, ITEMS);

    public static final RegistrySupplier<Block> ANCIENT_OAK_SAPLING = sapling("ancient_oak_sapling", new AncientOakSaplingGenerator(), MapColor.PLANT);
    public static final RegistrySupplier<Block> ANCIENT_OAK_LEAVES = registerBlockItem("ancient_oak_leaves", ()->new TLeavesBlock(LEAF_PROPERTIES).modifiers(RTypeModifier.create(RType.CUTOUT_MIPPED), FlammableModifier.LEAVES));
    public static final RegistrySupplier<Block> ALTAR = registerBlockItem("altar", ()->new AltarBlock(properties(5F).lightLevel((st)->st.getValue(AltarBlock.ACTIVE) ? 5 : 1).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().mapColor(MapColor.COLOR_BLACK).noOcclusion()).modifiers(RTypeModifier.CUTOUT));
    public static final RegistrySupplier<Block> CLADDED_STONE = registerBlockItem("cladded_stone", ()->new TBlock(properties(1.5F, 6.0F).requiresCorrectToolForDrops().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.STONE)));
    public static final RegistrySupplier<Block> CRUDE_CLADDING_BLOCK = registerBlockItem("crude_cladding_block", ()->new TBlock(properties(5F, 7F).instrument(NoteBlockInstrument.IRON_XYLOPHONE).sound(SoundType.NETHERITE_BLOCK).requiresCorrectToolForDrops().mapColor(MapColor.WARPED_STEM)));

    public static final RegistrySupplier<Block> ROOTLING_CROP = register("rootling_crop", ()->new RootlingCropBlock(properties(0).noCollission().randomTicks().instabreak().sound(SoundType.CROP).mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY)).modifiers(RTypeModifier.CUTOUT));
    public static final RegistrySupplier<Block> IVY = registerBlockItem("ivy", ()->new IvyBlock(properties(0.15F).noCollission().randomTicks().replaceable().sound(SoundType.VINE).pushReaction(PushReaction.DESTROY).mapColor(MapColor.PLANT)).modifiers(RTypeModifier.CUTOUT, FlammableModifier.IVY));
    public static final RegistrySupplier<Block> ITCHING_IVY = registerBlockItem("itching_ivy", ()->new ItchingIvyBlock(properties(0.15F).replaceable().speedFactor(0.5F).noCollission().mapColor(MapColor.PLANT).pushReaction(PushReaction.DESTROY).randomTicks().sound(SoundType.VINE)).modifiers(RTypeModifier.CUTOUT, FlammableModifier.IVY));
    public static final RegistrySupplier<Block> MOTH_BLOSSOM = registerBlockItem("moth_blossom", ()->new MothBlossomBlock(properties(0.25F).speedFactor(0.5F).noCollission().randomTicks().sound(SoundType.VINE).mapColor(MapColor.COLOR_ORANGE).pushReaction(PushReaction.DESTROY)).modifiers(RTypeModifier.CUTOUT, FlammableModifier.IVY));
    public static final RegistrySupplier<Block> WILD_MUSHROOMS = registerBlockItem("wild_mushrooms", ()->new WildMushroomBlock(properties(0F).noCollission().instabreak().randomTicks().noOcclusion().sound(SoundType.FUNGUS).mapColor(MapColor.WOOL).pushReaction(PushReaction.DESTROY)).modifiers(RTypeModifier.CUTOUT, FlammableModifier.IVY));

    public static final RegistrySupplier<Block> SUSPICIOUS_RED_SAND = registerBlockItem("suspicious_red_sand", ()->new BrushableBlock(Blocks.RED_SAND, BlockBehaviour.Properties.of().mapColor(MapColor.SAND).instrument(NoteBlockInstrument.SNARE).strength(0.25F).sound(SoundType.SUSPICIOUS_SAND).pushReaction(PushReaction.DESTROY), SoundEvents.BRUSH_SAND, SoundEvents.BRUSH_SAND_COMPLETED));

    public static final TapestryInfo TAPESTRIES = TapestryInfo.create();
    public static final TerracottaBrickInfo TERRACOTTA_BRICKS = TerracottaBrickInfo.create();
    public static final RegistrySupplier<Block> CRACKED_BRICKS = registerBlockItem("cracked_bricks", ()->new TBlock(properties(2F, 6F).requiresCorrectToolForDrops().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.COLOR_RED)));
    public static final DecorationBlockFactory CRACKED_BRICKS_DECORATION = new DecorationBlockFactory(Constants.MOD_ID, "cracked_brick", CRACKED_BRICKS, properties(2F, 6F).requiresCorrectToolForDrops().instrument(NoteBlockInstrument.BASEDRUM).mapColor(MapColor.COLOR_RED), BMTab.TAB).all().register(BLOCKS, ITEMS);

    public static final RegistrySupplier<Block> POTTED_MYCELIUM_ROOTS = pottedPlant("mycelium_roots",MYCELIUM_ROOTS);
    public static final RegistrySupplier<Block> POTTED_PURPLE_GLOWSHROOM =  litPottedPlant("purple_glowshroom", PURPLE_GLOWSHROOM, 13);
    public static final RegistrySupplier<Block> POTTED_GREEN_GLOWSHROOM =  litPottedPlant("green_glowshroom", GREEN_GLOWSHROOM, 13);
    public static final RegistrySupplier<Block> POTTED_ORANGE_GLOWSHROOM =  litPottedPlant("orange_glowshroom", ORANGE_GLOWSHROOM, 13);
    public static final RegistrySupplier<Block> POTTED_SAGUARO_CACTUS =  pottedPlant("saguaro_cactus", SAGUARO_CACTUS);
    public static final RegistrySupplier<Block> POTTED_BARREL_CACTUS =  pottedPlant("barrel_cactus", BARREL_CACTUS);
    public static final RegistrySupplier<Block> POTTED_FLOWERED_BARREL_CACTUS =  pottedPlant("flowered_barrel_cactus", BARREL_CACTUS_FLOWERED);
    public static final RegistrySupplier<Block> POTTED_WILLOW_SAPLING =  pottedPlant("willow_sapling", WILLOW_SAPLING);
    public static final RegistrySupplier<Block> POTTED_SWAMP_CYPRESS_SAPLING =  pottedPlant("swamp_cypress_sapling", SWAMP_CYPRESS_SAPLING);
    public static final RegistrySupplier<Block> POTTED_WILD_MUSHROOMS =  pottedPlant("wild_mushrooms", WILD_MUSHROOMS);

    public static final RegistrySupplier<Block> DIRECTIONAL_DATA =  registerBlockItem("directional_data", ()->new DirectionalDataBlock(BlockBehaviour.Properties.of().strength(-1).noLootTable()), false);

    public static void init() {
        BLOCKS.register();
        ITEMS.register();

        DIRECTIONAL_DATA.listen((b)->{
            HoeItemHooks.addTillable(PEAT.get(), HoeItem::onlyIfAirAbove, HoeItem.changeIntoState(PEAT_FARMLAND.get().defaultBlockState()), (c)->PEAT_FARMLAND.get().defaultBlockState());
            HoeItemHooks.addTillable(MOSSY_PEAT.get(), HoeItem::onlyIfAirAbove, HoeItem.changeIntoState(PEAT_FARMLAND.get().defaultBlockState()), (c)->PEAT_FARMLAND.get().defaultBlockState());
        });

        SUSPICIOUS_RED_SAND.listen((b)->{
            BlockEntityHooks.addAdditionalBlock(BlockEntityType.BRUSHABLE_BLOCK, b);
        });
    }

    public static Boolean canSpawnOnLeaves(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> type)
    {
        return type == EntityType.OCELOT || type == EntityType.PARROT;
    }

    public static RegistrySupplier<Block> registerBlockItemPot(String id, Supplier<Block> block)
    {
        RegistrySupplier<Block> baseBlock = registerBlockItem(id, block);
        pottedPlant(id, baseBlock);

        return baseBlock;
    }

    public static RegistrySupplier<Block> registerBlockItem(String id, Supplier<Block> block)
    {
        return registerBlockItem(id, block, true);
    }

    public static RegistrySupplier<Block> registerBlockItem(String id, Supplier<Block> block, boolean tab)
    {
        RegistrySupplier<Block> bl = registerBlock(BLOCKS, BiomeMakeover.ID(id), block);
        RegistrySupplier<Item> it = ItemHelper.registerItem(ITEMS, BiomeMakeover.ID(id), ()->new BlockItem(bl.get(), new Item.Properties()));
        initBlockItem(bl, it);

        if(tab)
            CreativeTabRegistry.append(BMTab.TAB, it);

        return bl;
    }

    public static RegistrySupplier<Block> registerBlock(DeferredRegister<Block> register, ResourceLocation location, Supplier<Block> blockSupplier)
    {
        return BlockHelper.registerBlock(register, location, blockSupplier);
    }

    public static RegistrySupplier<Block> register(String id, Supplier<Block> block)
    {
        return registerBlock(BLOCKS, BiomeMakeover.ID(id), block);
    }

    private static RegistrySupplier<Block> pottedPlant(String base_name, RegistrySupplier<Block> baseBlock)
    {
        return register("potted_" + base_name, ()->new TFlowerPotBlock(baseBlock.get(), properties(0).mapColor(MapColor.NONE).instabreak().noOcclusion().pushReaction(PushReaction.DESTROY)).modifiers(RTypeModifier.CUTOUT));
    }

    private static RegistrySupplier<Block> litPottedPlant(String base_name, RegistrySupplier<Block> baseBlock, int light)
    {
        return register("potted_" + base_name, ()->new TFlowerPotBlock(baseBlock.get(), properties(0).mapColor(MapColor.NONE).lightLevel(s->light).instabreak().noOcclusion().pushReaction(PushReaction.DESTROY)).modifiers(RTypeModifier.CUTOUT));
    }

    private static RegistrySupplier<Block> sapling(String name, AbstractTreeGrower grower, MapColor color)
    {
        RegistrySupplier<Block> sapling = registerBlockItem(name, ()->new TSaplingBlock(grower, BlockBehaviour.Properties.of().noCollission().mapColor(color).randomTicks().instabreak().sound(SoundType.GRASS).pushReaction(PushReaction.DESTROY)).modifiers(RTypeModifier.CUTOUT));
        pottedPlant(name, sapling);
        return sapling;
    }

    private static RegistrySupplier<Block> leaves(String name)
    {
        return registerBlockItem(name, ()->new TLeavesBlock(LEAF_PROPERTIES).modifiers(LEAF_MODIFIERS));
    }

    public static BlockBehaviour.Properties properties(float strength)
    {
        return properties(strength, strength);
    }

    public static BlockBehaviour.Properties properties(float breakSpeed, float explosionResist)
    {
        return BlockBehaviour.Properties.of().strength(breakSpeed, explosionResist);
    }

    private static boolean always(BlockState state, BlockGetter world, BlockPos pos)
    {
        return true;
    }

    public static RegistrySupplier<Block> registerLilyPad(String id, Supplier<Block> block, boolean tab)
    {
        RegistrySupplier<Block> bl = register(id, block);
        RegistrySupplier<Item> it = ItemHelper.registerItem(ITEMS, BiomeMakeover.ID(id), ()->new PlaceOnWaterBlockItem(bl.get(), new Item.Properties()));
        initBlockItem(bl, it);
        if(tab)
            CreativeTabRegistry.append(BMTab.TAB, it);

        return bl;
    }

    private static void initBlockItem(RegistrySupplier<Block> block, RegistrySupplier<Item> item)
    {
        BLOCK_ITEMS.put(block, item);
    }

    private static TagKey<Block> blockTag(String path)
    {
        return blockTag(BiomeMakeover.ID(path));
    }

    private static TagKey<Block> blockTag(ResourceLocation id)
    {
        return TagKey.create(Registries.BLOCK, id);
    }

    public static final class TapestryInfo
    {
        private final List<RegistrySupplier<Block>> allBlocks;
        private final List<RegistrySupplier<Block>> wallBlocks;
        private final List<RegistrySupplier<Block>> floorBlocks;
        private final RegistrySupplier<Block> adjudicator;

        static TapestryInfo create()
        {
            return new TapestryInfo();
        }

        private TapestryInfo()
        {
            List<RegistrySupplier<Block>> allBlocks = new ArrayList<>();
            List<RegistrySupplier<Block>> wallBlocks = new ArrayList<>();
            List<RegistrySupplier<Block>> floorBlocks = new ArrayList<>();
            Set<RegistrySupplier<Block>> allBlocksSet = new HashSet<>();
            Set<RegistrySupplier<Block>> wallBlocksSet = new HashSet<>();
            Set<RegistrySupplier<Block>> floorBlocksSet = new HashSet<>();

            for(DyeColor dye : DyeColor.values())
            {
                RegistrySupplier<Block> tapestryBlock = BLOCKS.register(BiomeMakeover.ID(dye.getName() + "_tapestry"), ()->new ColorTapestryBlock(dye, properties(1F).instrument(NoteBlockInstrument.BASS).noCollission().sound(SoundType.WOOD)));

                RegistrySupplier<Block> wallBlock = BLOCKS.register(BiomeMakeover.ID(dye.getName() + "_wall_tapestry"), ()->new ColorTapestryWallBlock(dye, properties(1F).instrument(NoteBlockInstrument.BASS).noCollission().sound(SoundType.WOOD).dropsLike(tapestryBlock.get())));
                RegistrySupplier<Item> it = ItemHelper.registerItem(ITEMS, BiomeMakeover.ID(dye.getName() + "_tapestry"), ()->new StandingAndWallBlockItem(tapestryBlock.get(), wallBlock.get(), new Item.Properties().stacksTo(16).rarity(Rarity.UNCOMMON), Direction.DOWN));
                CreativeTabRegistry.append(BMTab.TAB, it);

                initBlockItem(tapestryBlock, it);
                initBlockItem(wallBlock, it);

                addIfNew(allBlocks, allBlocksSet, tapestryBlock);
                addIfNew(allBlocks, allBlocksSet, wallBlock);
                addIfNew(floorBlocks, floorBlocksSet, tapestryBlock);
                addIfNew(wallBlocks, wallBlocksSet, wallBlock);
            }
            adjudicator = BLOCKS.register(BiomeMakeover.ID("adjudicator_tapestry"), ()->new AdjudicatorTapestryBlock(properties(1F).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().sound(SoundType.WOOD)));

            RegistrySupplier<Block> adjWall = BLOCKS.register(BiomeMakeover.ID("adjudicator_wall_tapestry"), ()->new AdjudicatorTapestryWallBlock(properties(1F).forceSolidOn().instrument(NoteBlockInstrument.BASS).noCollission().sound(SoundType.WOOD).dropsLike(adjudicator.get())));

            RegistrySupplier<Item> it = ItemHelper.registerItem(ITEMS, BiomeMakeover.ID("adjudicator_tapestry"), ()->new StandingAndWallBlockItem(adjudicator.get(), adjWall.get(), new Item.Properties().stacksTo(16).rarity(Rarity.EPIC), Direction.DOWN));
            initBlockItem(adjWall, it);
            initBlockItem(adjudicator, it);

            allBlocks.add(adjudicator);
            allBlocks.add(adjWall);

            this.allBlocks = Collections.unmodifiableList(allBlocks);
            this.wallBlocks = Collections.unmodifiableList(wallBlocks);
            this.floorBlocks = Collections.unmodifiableList(floorBlocks);
        }

        private static void addIfNew(List<RegistrySupplier<Block>> list, Set<RegistrySupplier<Block>> set, RegistrySupplier<Block> block) {
            if (set.add(block)) {
                list.add(block);
            }
        }

        public List<RegistrySupplier<Block>> getWallBlocks() {
            return wallBlocks;
        }

        public List<RegistrySupplier<Block>> getFloorBlocks() {
            return floorBlocks;
        }

        public List<RegistrySupplier<Block>> getBlocks() {
            return allBlocks;
        }

        public RegistrySupplier<Block> getAdjudicatorTapestry() {
            return adjudicator;
        }
    }

    public static final class TerracottaBrickInfo
    {
        private TerracottaBrickInfo()
        {
        }

        public static TerracottaBrickInfo create()
        {
            return new TerracottaBrickInfo().init();
        }

        public TerracottaBrickInfo init()
        {
            RegistrySupplier<Block> tBlock = registerBlockItem("terracotta_bricks", ()->new TBlock(properties(2F).mapColor(MapColor.COLOR_ORANGE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().sound(SoundType.STONE)));
            DecorationBlockFactory terracottaBrick = new DecorationBlockFactory(Constants.MOD_ID, "terracotta_brick", tBlock, properties(2F).mapColor(MapColor.COLOR_ORANGE).instrument(NoteBlockInstrument.BASEDRUM).requiresCorrectToolForDrops().sound(SoundType.STONE), BMTab.TAB).all().register(BLOCKS, ITEMS);

            Map<DyeColor, MapColor> colors = ImmutableMap.<DyeColor, MapColor>builder()
                    .put(DyeColor.WHITE, MapColor.TERRACOTTA_WHITE)
                    .put(DyeColor.ORANGE, MapColor.TERRACOTTA_ORANGE)
                    .put(DyeColor.MAGENTA, MapColor.TERRACOTTA_MAGENTA)
                    .put(DyeColor.LIGHT_BLUE, MapColor.TERRACOTTA_LIGHT_BLUE)
                    .put(DyeColor.YELLOW, MapColor.TERRACOTTA_YELLOW)
                    .put(DyeColor.LIME, MapColor.TERRACOTTA_LIGHT_GREEN)
                    .put(DyeColor.PINK, MapColor.TERRACOTTA_PINK)
                    .put(DyeColor.GRAY, MapColor.TERRACOTTA_GRAY)
                    .put(DyeColor.LIGHT_GRAY, MapColor.TERRACOTTA_LIGHT_GRAY)
                    .put(DyeColor.CYAN, MapColor.TERRACOTTA_CYAN)
                    .put(DyeColor.PURPLE, MapColor.TERRACOTTA_PURPLE)
                    .put(DyeColor.BLUE, MapColor.TERRACOTTA_BLUE)
                    .put(DyeColor.BROWN, MapColor.TERRACOTTA_BROWN)
                    .put(DyeColor.GREEN, MapColor.TERRACOTTA_GREEN)
                    .put(DyeColor.RED, MapColor.TERRACOTTA_RED)
                    .put(DyeColor.BLACK, MapColor.TERRACOTTA_BLACK)
                    .build();

            for(DyeColor dye : DyeColor.values())
            {
                RegistrySupplier<Block> bl = registerBlockItem(dye.getName() + "_terracotta_bricks", ()->new TBlock(properties(2F).mapColor(colors.get(dye)).requiresCorrectToolForDrops().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.STONE)));
                DecorationBlockFactory dec = new DecorationBlockFactory(Constants.MOD_ID, dye.getName() + "_terracotta_brick", bl, properties(2F).mapColor(colors.get(dye)).requiresCorrectToolForDrops().instrument(NoteBlockInstrument.BASEDRUM).sound(SoundType.STONE), BMTab.TAB).all().register(BLOCKS, ITEMS);
            }
            return this;
        }

    }

    private static boolean never(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> entityType) {
        return false;
    }

    private static boolean never(BlockState state, BlockGetter blockGetter, BlockPos pos)
    {
        return false;
    }
}
