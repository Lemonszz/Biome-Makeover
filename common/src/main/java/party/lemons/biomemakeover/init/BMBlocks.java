package party.lemons.biomemakeover.init;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.registry.block.BlockProperties;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.fuel.FuelRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.*;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.level.material.PushReaction;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.Constants;
import party.lemons.biomemakeover.block.*;
import party.lemons.biomemakeover.block.modifier.*;
import party.lemons.biomemakeover.level.generate.foliage.AncientOakSaplingGenerator;
import party.lemons.biomemakeover.level.generate.foliage.BalsaSaplingGenerator;
import party.lemons.biomemakeover.level.generate.foliage.SwampCypressSaplingGenerator;
import party.lemons.biomemakeover.level.generate.foliage.WillowSaplingGenerator;
import party.lemons.biomemakeover.util.BMSoundType;
import party.lemons.biomemakeover.util.FieldConsumer;
import party.lemons.biomemakeover.util.registry.DecorationBlockInfo;
import party.lemons.biomemakeover.util.registry.RType;
import party.lemons.biomemakeover.util.registry.WoodTypeInfo;
import party.lemons.biomemakeover.util.registry.boat.BoatTypes;

import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class BMBlocks
{
    public static final List<Block> blocks = Lists.newArrayList();
    public static final Multimap<Block, BlockModifier> MODIFIERS = ArrayListMultimap.create();
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(Constants.MOD_ID, Registry.BLOCK_REGISTRY);

    public static final Material POLTERGEIST_MATERIAL = new Material(MaterialColor.COLOR_GRAY, false, true, true, false, true, false, PushReaction.BLOCK);
    public static final SoundType BM_LILY_PAD_SOUND = new SoundType(1.0F, 1.0F, SoundEvents.WET_GRASS_BREAK, SoundEvents.WET_GRASS_STEP, SoundEvents.LILY_PAD_PLACE, SoundEvents.WET_GRASS_HIT, SoundEvents.WET_GRASS_FALL);
    public static final SoundType ILLUNITE_SOUND = new BMSoundType(1.0F, 1.0F, BMEffects.ILLUNITE_BREAK, BMEffects.ILLUNITE_STEP, BMEffects.ILLUNITE_PLACE, BMEffects.ILLUNITE_HIT, ()->SoundEvents.STONE_FALL);

    public static final Supplier<Block> PURPLE_GLOWSHROOM = registerBlockItem("purple_glowshroom", ()->new GlowshroomPlantBlock(()->BMWorldGen.MushroomFields.HUGE_PURPLE_GLOWSHROOM, properties(Material.PLANT, 0F).lightLevel((s)->13).noCollission().noCollission().sound(SoundType.FUNGUS)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.7F)));
    public static final Supplier<Block> GREEN_GLOWSHROOM = registerBlockItem("green_glowshroom", ()->new GlowshroomPlantBlock(()->BMWorldGen.MushroomFields.HUGE_GREEN_GLOWSHROOM, properties(Material.PLANT, 0F).lightLevel((s)->13).noCollission().noOcclusion().sound(SoundType.FUNGUS)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.7F)));
    public static final Supplier<Block> ORANGE_GLOWSHROOM = registerBlockItem("orange_glowshroom", ()->new UnderwaterMushroomPlantBlock(()->BMWorldGen.MushroomFields.HUGE_ORANGE_GLOWSHROOM, properties(Material.PLANT, 0F).lightLevel((s)->13).noCollission().noOcclusion().sound(SoundType.FUNGUS)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.7F)));

    public static final Supplier<Block> PURPLE_GLOWSHROOM_BLOCK = registerBlockItem("purple_glowshroom_block", ()->new BMMushroomBlock(properties(Material.PLANT, 0.2F).lightLevel((s)->15).sound(SoundType.FUNGUS)).modifiers(CompostModifier.create(0.9F)));
    public static final Supplier<Block> GREEN_GLOWSHROOM_BLOCK = registerBlockItem("green_glowshroom_block", ()->new BMMushroomBlock(properties(Material.PLANT, 0.2F).lightLevel((s)->15).sound(SoundType.FUNGUS)).modifiers(CompostModifier.create(0.9F)));
    public static final Supplier<Block> ORANGE_GLOWSHROOM_BLOCK = registerBlockItem("orange_glowshroom_block", ()->new BMMushroomBlock(properties(Material.PLANT, 0.2F).lightLevel((s)->15).sound(SoundType.FUNGUS)).modifiers(CompostModifier.create(0.9F)));

    public static final Supplier<Block> MYCELIUM_SPROUTS = registerBlockItem("mycelium_sprouts", ()->new MushroomSproutsBlock(properties(Material.REPLACEABLE_FIREPROOF_PLANT, 0).noCollission().noCollission().instabreak().sound(SoundType.NETHER_SPROUTS)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.7F)));
    public static final Supplier<Block> MYCELIUM_ROOTS = registerBlockItem("mycelium_roots", ()->new MushroomRootsBlock(properties(Material.REPLACEABLE_FIREPROOF_PLANT, 0).noCollission().noCollission().instabreak().sound(SoundType.ROOTS)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.7F)));

    public static final Supplier<Block> TALL_BROWN_MUSHROOM = registerBlockItem("tall_brown_mushroom", ()->new BMTallMushroomBlock(Blocks.BROWN_MUSHROOM, properties(Material.PLANT, 0).instabreak().noCollission().sound(SoundType.FUNGUS)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.7F)));
    public static final Supplier<Block> TALL_RED_MUSHROOM = registerBlockItem("tall_red_mushroom", ()->new BMTallMushroomBlock(Blocks.RED_MUSHROOM, properties(Material.PLANT, 0).instabreak().noCollission().sound(SoundType.FUNGUS)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.9F)));

    public static WoodTypeInfo BLIGHTED_BALSA_WOOD_INFO = new WoodTypeInfo(Constants.MOD_ID, BiomeMakeover.TAB, "blighted_balsa").all(()-> BoatTypes.BLIGHTED_BALSA);
    public static final Supplier<Block> BLIGHTED_BALSA_LEAVES = registerBlockItem("blighted_balsa_leaves", ()->new BMLeavesBlock(properties(Material.LEAVES, 0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(BMBlocks::canSpawnOnLeaves).isSuffocating((a, b, c)->false).isViewBlocking((a, b, c)->false)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.3F), FlammableModifier.LEAVES));
    public static final Supplier<Block> BLIGHTED_BALSA_SAPLING = registerBlockItem("blighted_balsa_sapling", ()->new BMSaplingBlock(new BalsaSaplingGenerator(), properties(Material.PLANT, 0).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.4F)));

    public static final Supplier<Block> GLOWSHROOM_STEM = registerBlockItem("glowshroom_stem", ()->new BMMushroomBlock(properties(Material.GRASS, 0.2F).lightLevel((s)->7).sound(SoundType.FUNGUS)).modifiers(CompostModifier.create(0.9F)));
    public static final Supplier<Block> RED_MUSHROOM_BRICK = registerBlockItem("red_mushroom_brick", ()->new BMBlock(properties(Material.GRASS, 0.8F).sound(SoundType.FUNGUS)));
    public static final DecorationBlockInfo RED_MUSHROOM_BRICK_DECORATION = new DecorationBlockInfo(Constants.MOD_ID, BiomeMakeover.TAB, "red_mushroom_brick", RED_MUSHROOM_BRICK, properties(Material.GRASS, 0.8F).sound(SoundType.FUNGUS)).all();
    public static final Supplier<Block> BROWN_MUSHROOM_BRICK = registerBlockItem("brown_mushroom_brick", ()->new BMBlock(properties(Material.GRASS, 0.8F).sound(SoundType.FUNGUS)));
    public static final DecorationBlockInfo BROWN_MUSHROOM_BRICK_DECORATION = new DecorationBlockInfo(Constants.MOD_ID, BiomeMakeover.TAB, "brown_mushroom_brick", BROWN_MUSHROOM_BRICK, properties(Material.GRASS, 0.8F).sound(SoundType.FUNGUS)).all();
    public static final Supplier<Block> PURPLE_GLOWSHROOM_BRICK = registerBlockItem("purple_glowshroom_brick", ()->new BMBlock(properties(Material.GRASS, 0.8F).lightLevel((s)->13).sound(SoundType.FUNGUS)));
    public static final DecorationBlockInfo PURPLE_GLOWSROOM_BRICK_DECORATION = new DecorationBlockInfo(Constants.MOD_ID, BiomeMakeover.TAB, "purple_glowshroom_brick", PURPLE_GLOWSHROOM_BRICK, properties(Material.GRASS, 0.8F).lightLevel((s)->13).sound(SoundType.FUNGUS)).all();
    public static final Supplier<Block> GREEN_GLOWSHROOM_BRICK = registerBlockItem("green_glowshroom_brick", ()->new BMBlock(properties(Material.GRASS, 0.8F).lightLevel((s)->13).sound(SoundType.FUNGUS)));
    public static final DecorationBlockInfo GREEN_GLOWSROOM_BRICK_DECORATION = new DecorationBlockInfo(Constants.MOD_ID, BiomeMakeover.TAB, "green_glowshroom_brick", GREEN_GLOWSHROOM_BRICK, properties(Material.GRASS, 0.8F).lightLevel((s)->13).sound(SoundType.FUNGUS)).all();
    public static final Supplier<Block> ORANGE_GLOWSHROOM_BRICK = registerBlockItem("orange_glowshroom_brick", ()->new BMBlock(properties(Material.GRASS, 0.8F).lightLevel((s)->13).sound(SoundType.FUNGUS)));
    public static final DecorationBlockInfo ORANGE_GLOWSROOM_BRICK_DECORATION = new DecorationBlockInfo(Constants.MOD_ID, BiomeMakeover.TAB, "orange_glowshroom_brick", ORANGE_GLOWSHROOM_BRICK, properties(Material.GRASS, 0.8F).lightLevel((s)->13).sound(SoundType.FUNGUS)).all();
    public static final Supplier<Block> GLOWSHROOM_STEM_BRICK = registerBlockItem("glowshroom_stem_brick", ()->new BMBlock(properties(Material.GRASS, 0.8F).lightLevel((s)->7).sound(SoundType.FUNGUS)));
    public static final DecorationBlockInfo GLOWSHROOM_STEM_BRICK_DECORATION = new DecorationBlockInfo(Constants.MOD_ID, BiomeMakeover.TAB, "glowshroom_stem_brick", GLOWSHROOM_STEM_BRICK, properties(Material.GRASS, 0.8F).lightLevel((s)->7).sound(SoundType.FUNGUS)).all();
    public static final Supplier<Block> MUSHROOM_STEM_BRICK = registerBlockItem("mushroom_stem_brick", ()->new BMBlock(properties(Material.GRASS, 0.8F).sound(SoundType.FUNGUS)));
    public static final DecorationBlockInfo MUSHROOM_STEM_BRICK_DECORATION = new DecorationBlockInfo(Constants.MOD_ID, BiomeMakeover.TAB, "mushroom_stem_brick", MUSHROOM_STEM_BRICK, properties(Material.GRASS, 0.8F).sound(SoundType.FUNGUS)).all();
    public static final Supplier<Block> BLIGHTED_COBBLESTONE = registerBlockItem("blighted_cobblestone", ()->new BMBlock(properties(Material.STONE, 2).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DecorationBlockInfo BLIGHTED_COBBLESTONE_DECORATION = new DecorationBlockInfo(Constants.MOD_ID, BiomeMakeover.TAB, "blighted_cobblestone", BLIGHTED_COBBLESTONE, properties(Material.STONE, 2F).requiresCorrectToolForDrops().sound(SoundType.STONE)).all();
    public static final Supplier<Block> BLIGHTED_STONE_BRICKS = registerBlockItem("blighted_stone_bricks", ()->new BMBlock(properties(Material.STONE, 2).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DecorationBlockInfo BLIGHTED_STONE_BRICKS_DECORATION = new DecorationBlockInfo(Constants.MOD_ID, BiomeMakeover.TAB, "blighted_stone_bricks", BLIGHTED_STONE_BRICKS, properties(Material.STONE, 2F).requiresCorrectToolForDrops().sound(SoundType.STONE)).all();
    public static final Supplier<Block> PAYDIRT = registerBlockItem("paydirt", ()->new BMBlock(properties(Material.DIRT, 1.4F).requiresCorrectToolForDrops().sound(SoundType.GRAVEL)));

    public static final Supplier<Block> TUMBLEWEED = BLOCKS.register(BiomeMakeover.ID("tumbleweed"), ()->new Block(properties(Material.PLANT, 0)));

    public static final Supplier<Block> SAGUARO_CACTUS = registerBlockItem("saguaro_cactus", ()->new SaguaroCactusBlock(properties(Material.CACTUS, 0.4F).sound(SoundType.WOOL).randomTicks()).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.15F)));
    public static final Supplier<Block> BARREL_CACTUS = registerBlockItem("barrel_cactus", ()->new BarrelCactusBlock(false, properties(Material.CACTUS, 0).randomTicks().sound(SoundType.WOOL).noOcclusion().instabreak().noCollission()).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.3F)));
    public static final Supplier<Block> BARREL_CACTUS_FLOWERED = registerBlockItem("barrel_cactus_flowered", ()->new BarrelCactusBlock(true, properties(Material.CACTUS, 0).sound(SoundType.WOOL).noOcclusion().instabreak().noCollission()).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.3F)));

    public static final Supplier<Block> POLTERGEIST = registerBlockItem("poltergeist", ()->new PoltergeistBlock(properties(POLTERGEIST_MATERIAL, 1.0F).lightLevel((bs)->bs.getValue(PoltergeistBlock.ENABLED) ? 7 : 0).sound(SoundType.LODESTONE)).modifiers(RTypeModifier.create(RType.CUTOUT)));
    public static final Supplier<Block> ECTOPLASM_COMPOSTER = BLOCKS.register(BiomeMakeover.ID("ectoplasm_composter"), ()->new EctoplasmComposterBlock(properties(Material.WOOD, 0.6F).sound(SoundType.WOOD)));

    public static WoodTypeInfo WILLOW_WOOD_INFO = new WoodTypeInfo(Constants.MOD_ID, BiomeMakeover.TAB, "willow").all(()->BoatTypes.WILLOW);
    public static WoodTypeInfo SWAMP_CYPRESS_WOOD_INFO = new WoodTypeInfo(Constants.MOD_ID, BiomeMakeover.TAB, "swamp_cypress").all(()->BoatTypes.SWAMP_CYPRESS);

    public static final Supplier<Block> WILLOWING_BRANCHES = registerBlockItem("willowing_branches", ()->new WillowingBranchesBlock(properties(Material.PLANT, 0.1F).randomTicks().sound(SoundType.VINE).noCollission().noOcclusion()).modifiers(RTypeModifier.create(RType.CUTOUT_MIPPED), CompostModifier.create(0.3F), new FlammableModifier(15, 100)));
    public static final Supplier<Block> WILLOW_SAPLING = registerBlockItem("willow_sapling", ()->new WaterSaplingBlock(new WillowSaplingGenerator(), 1, properties(Material.PLANT, 0).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.4F)));
    public static final Supplier<Block> SWAMP_CYPRESS_SAPLING = registerBlockItem("swamp_cypress_sapling", ()->new WaterSaplingBlock(new SwampCypressSaplingGenerator(), 3, properties(Material.PLANT, 0).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.4F)));
    public static final Supplier<Block> PEAT = registerBlockItem("peat", ()->new BMBlock(properties(Material.DIRT, 0.5F).sound(SoundType.WET_GRASS)).modifiers(new HoeModifier(BMBlocks.PEAT_FARMLAND)));
    public static final Supplier<Block> DRIED_PEAT = registerBlockItem("dried_peat", ()->new BMBlock(properties(Material.DIRT, 1F).sound(SoundType.NETHERRACK)));
    public static final Supplier<Block> MOSSY_PEAT = registerBlockItem("mossy_peat", ()->new BMSpreadableBlock(properties(Material.DIRT, 0.5F).randomTicks().sound(SoundType.WET_GRASS), PEAT).modifiers(new HoeModifier(BMBlocks.PEAT_FARMLAND)));
    public static final Supplier<Block> PEAT_FARMLAND = registerBlockItem("peat_farmland", ()->new PeatFarmlandBlock(properties(Material.DIRT, 0.5F).sound(SoundType.WET_GRASS).randomTicks().noOcclusion()));
    public static final Supplier<Block> DRIED_PEAT_BRICKS = registerBlockItem("dried_peat_bricks", ()->new BMBlock(properties(Material.STONE, 2).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DecorationBlockInfo DRIED_PEAT_BRICKS_DECORATION = new DecorationBlockInfo(Constants.MOD_ID, BiomeMakeover.TAB, "dried_peat_bricks", DRIED_PEAT_BRICKS, properties(Material.STONE, 2F).sound(SoundType.NETHERRACK).requiresCorrectToolForDrops().sound(SoundType.STONE)).all();
    public static final Supplier<Block> MOSSY_DRIED_PEAT_BRICKS = registerBlockItem("mossy_dried_peat_bricks", ()->new BMBlock(properties(Material.STONE, 2).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DecorationBlockInfo MOSSY_DRIED_PEAT_BRICKS_DECORATION = new DecorationBlockInfo(Constants.MOD_ID, BiomeMakeover.TAB, "mossy_dried_peat_brick", MOSSY_DRIED_PEAT_BRICKS, properties(Material.STONE, 2F).sound(SoundType.NETHERRACK).requiresCorrectToolForDrops().sound(SoundType.STONE)).all();
    public static final Supplier<Block> CRACKED_DRIED_PEAT_BRICKS = registerBlockItem("cracked_dried_peat_bricks", ()->new BMBlock(properties(Material.STONE, 2).sound(SoundType.STONE).requiresCorrectToolForDrops()));
    public static final DecorationBlockInfo CRACKED_DRIED_PEAT_BRICKS_DECORATION = new DecorationBlockInfo(Constants.MOD_ID, BiomeMakeover.TAB, "cracked_dried_peat_brick", CRACKED_DRIED_PEAT_BRICKS, properties(Material.STONE, 2F).sound(SoundType.NETHERRACK).requiresCorrectToolForDrops().sound(SoundType.STONE)).all();
    public static final Supplier<Block> SWAMP_AZALEA = registerBlockItem("swamp_azalea", ()->new BMTallFlowerBlock(properties(Material.PLANT, 0).noCollission().instabreak().sound(SoundType.GRASS)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.65F), FlammableModifier.TALL_FLOWER));
    public static final Supplier<Block> MARIGOLD = registerBlockItem("marigold", ()->new BMTallFlowerBlock(properties(Material.PLANT, 0).noCollission().instabreak().sound(SoundType.GRASS)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.65F), FlammableModifier.TALL_FLOWER));
    public static final Supplier<Block> BLACK_THISTLE = registerBlockItem("black_thistle", ()->new BlackThistleBlock(properties(Material.PLANT, 0).noCollission().instabreak().sound(SoundType.GRASS)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.65F), FlammableModifier.TALL_FLOWER));
    public static final Supplier<Block> FOXGLOVE = registerBlockItem("foxglove", ()->new BMTallFlowerBlock(properties(Material.PLANT, 0).noCollission().instabreak().sound(SoundType.GRASS)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.65F), FlammableModifier.TALL_FLOWER));

    public static final Supplier<Block> CATTAIL = registerBlockItem("cattail", ()->new ReedBlock(properties(Material.PLANT, 0).instabreak().noCollission().sound(SoundType.GRASS)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.5F)));
    public static final Supplier<Block> REED = registerBlockItem("reed", ()->new ReedBlock(properties(Material.PLANT, 0).instabreak().noCollission().sound(SoundType.GRASS)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.2F)));
    public static final Supplier<Block> SMALL_LILY_PAD = registerLilyPad("small_lily_pad", ()->new SmallLilyPadBlock(properties(Material.PLANT, 0).instabreak().sound(BM_LILY_PAD_SOUND)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.3F)));
    public static final Supplier<Block> WATER_LILY = registerLilyPad("water_lily", ()->new FloweredWaterlilyPadBlock(properties(Material.PLANT, 0).instabreak().sound(BM_LILY_PAD_SOUND)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.8F)));
    public static final Supplier<Block> WILLOW_LEAVES = registerBlockItem("willow_leaves", ()->new BMLeavesBlock(properties(Material.LEAVES, 0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(BMBlocks::canSpawnOnLeaves).isSuffocating((a, b, c)->false).isViewBlocking((a, b, c)->false)).modifiers(RTypeModifier.create(RType.CUTOUT_MIPPED), CompostModifier.create(0.3F), FlammableModifier.LEAVES));
    public static final Supplier<Block> SWAMP_CYPRESS_LEAVES = registerBlockItem("swamp_cypress_leaves", ()->new BMLeavesBlock(properties(Material.LEAVES, 0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(BMBlocks::canSpawnOnLeaves).isSuffocating((a, b, c)->false).isViewBlocking((a, b, c)->false)).modifiers(RTypeModifier.create(RType.CUTOUT_MIPPED), CompostModifier.create(0.3F), FlammableModifier.LEAVES));
    public static final Supplier<Block> LIGHTNING_BUG_BOTTLE = registerBlockItem("lightning_bug_bottle", ()->new LightningBugBottleBlock(properties(Material.STONE, 0.5F).lightLevel((b)->15).noOcclusion()).modifiers(RTypeModifier.create(RType.CUTOUT)));

    public static final Supplier<Block> ILLUNITE_CLUSTER = registerBlockItem("illunite_cluster", ()->new IlluniteClusterBlock(properties(Material.STONE, 0.5F).sound(ILLUNITE_SOUND).noOcclusion().hasPostProcess(BMBlocks::always).emissiveRendering(BMBlocks::always)).modifiers(RTypeModifier.create(RType.CUTOUT)));
    public static final Supplier<Block> ILLUNITE_BLOCK = registerBlockItem("illunite_block", ()->new BMBlock(properties(Material.STONE, 1.5F).requiresCorrectToolForDrops().sound(ILLUNITE_SOUND)));
    public static final Supplier<Block> MESMERITE = registerBlockItem("mesmerite", ()->new BMBlock(properties(Material.STONE, 1.5F)));
    public static final DecorationBlockInfo MESMERITE_DECORATION = new DecorationBlockInfo(Constants.MOD_ID, BiomeMakeover.TAB,  "mesmerite", MESMERITE, properties(Material.STONE, 1.5F)).all();
    public static final Supplier<Block> POLISHED_MESMERITE = registerBlockItem("polished_mesmerite", ()->new BMBlock(properties(Material.STONE, 1.5F)));
    public static final DecorationBlockInfo POLISHED_MESMERITE_DECORATION = new DecorationBlockInfo(Constants.MOD_ID, BiomeMakeover.TAB, "polished_mesmerite", POLISHED_MESMERITE, properties(Material.STONE, 1.5F)).all();
    public static WoodTypeInfo ANCIENT_OAK_WOOD_INFO = new WoodTypeInfo(Constants.MOD_ID, BiomeMakeover.TAB, "ancient_oak").all(()->BoatTypes.ANCIENT_OAK);

    public static final Supplier<Block> ANCIENT_OAK_SAPLING = registerBlockItem("ancient_oak_sapling", ()->new BMSaplingBlock(new AncientOakSaplingGenerator(), properties(Material.PLANT, 0).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)).modifiers(RTypeModifier.create(RType.CUTOUT)));
    public static final Supplier<Block> ANCIENT_OAK_LEAVES = registerBlockItem("ancient_oak_leaves", ()->new BMLeavesBlock(properties(Material.LEAVES, 0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(BMBlocks::canSpawnOnLeaves).isSuffocating((a, b, c)->false).isViewBlocking((a, b, c)->false)).modifiers(RTypeModifier.create(RType.CUTOUT_MIPPED), CompostModifier.create(0.3F), FlammableModifier.LEAVES));
    public static final Supplier<Block> ALTAR = registerBlockItem("altar", ()->new AltarBlock(properties(Material.STONE, 5F).lightLevel((st)->st.getValue(AltarBlock.ACTIVE) ? 5 : 1).requiresCorrectToolForDrops().noOcclusion()).modifiers(RTypeModifier.create(RType.CUTOUT)));
    public static final Supplier<Block> CLADDED_STONE = registerBlockItem("cladded_stone", ()->new BMBlock(properties(Material.STONE, 1.5F).requiresCorrectToolForDrops()));

    public static final Supplier<Block> ROOTLING_CROP = BLOCKS.register(BiomeMakeover.ID("rootling_crop"), ()->new RootlingCropBlock(properties(Material.PLANT, 0).noCollission().randomTicks().instabreak().sound(SoundType.CROP)).modifiers(RTypeModifier.create(RType.CUTOUT)));
    public static final Supplier<Block> IVY = registerBlockItem("ivy", ()->new IvyBlock(properties(Material.REPLACEABLE_PLANT, 0.15F).noCollission().randomTicks().sound(SoundType.VINE)).modifiers(RTypeModifier.create(RType.CUTOUT), FlammableModifier.IVY));
    public static final Supplier<Block> ITCHING_IVY = registerBlockItem("itching_ivy", ()->new ItchingIvyBlock(properties(Material.REPLACEABLE_PLANT, 0.15F).speedFactor(0.5F).noCollission().randomTicks().sound(SoundType.VINE)).modifiers(RTypeModifier.create(RType.CUTOUT), FlammableModifier.IVY));
    public static final Supplier<Block> MOTH_BLOSSOM = registerBlockItem("moth_blossom", ()->new MothBlossomBlock(properties(Material.PLANT, 0.25F).speedFactor(0.5F).noCollission().randomTicks().sound(SoundType.VINE)).modifiers(RTypeModifier.create(RType.CUTOUT), FlammableModifier.IVY));
    public static final Supplier<Block> WILD_MUSHROOMS = registerBlockItem("wild_mushrooms", ()->new WildMushroomBlock(properties(Material.PLANT, 0F).noCollission().randomTicks().noOcclusion().sound(SoundType.FUNGUS)).modifiers(RTypeModifier.create(RType.CUTOUT), FlammableModifier.IVY));

    public static final Supplier<Block> POTTED_MYCELIUM_ROOTS = BLOCKS.register(BiomeMakeover.ID("potted_mycelium_roots"), ()->new BMFlowerPotBlock(MYCELIUM_ROOTS.get(), properties(Material.DECORATION, 0).instabreak().noOcclusion().sound(SoundType.NETHER_SPROUTS)).modifiers(RTypeModifier.create(RType.CUTOUT)));
    public static final Supplier<Block> POTTED_PURPLE_GLOWSHROOM =  BLOCKS.register(BiomeMakeover.ID("potted_purple_glowshroom"), ()->new BMFlowerPotBlock(PURPLE_GLOWSHROOM.get(), properties(Material.DECORATION, 0).lightLevel((s)->13).instabreak().noOcclusion().sound(SoundType.NETHER_SPROUTS)).modifiers(RTypeModifier.create(RType.CUTOUT)));
    public static final Supplier<Block> POTTED_GREEN_GLOWSHROOM =  BLOCKS.register(BiomeMakeover.ID("potted_green_glowshroom"), ()->new BMFlowerPotBlock(GREEN_GLOWSHROOM.get(), properties(Material.DECORATION, 0).lightLevel((s)->13).instabreak().noOcclusion().sound(SoundType.NETHER_SPROUTS)).modifiers(RTypeModifier.create(RType.CUTOUT)));
    public static final Supplier<Block> POTTED_ORANGE_GLOWSHROOM =  BLOCKS.register(BiomeMakeover.ID("potted_orange_glowshroom"), ()->new BMFlowerPotBlock(ORANGE_GLOWSHROOM.get(), properties(Material.DECORATION,0).lightLevel((s)->13).instabreak().noOcclusion().sound(SoundType.NETHER_SPROUTS)).modifiers(RTypeModifier.create(RType.CUTOUT)));
    public static final Supplier<Block> POTTED_BLIGHTED_BALSA_SAPLING =  BLOCKS.register(BiomeMakeover.ID("potted_blighted_balsa_sapling"), ()->new BMFlowerPotBlock(BLIGHTED_BALSA_SAPLING.get(), properties(Material.DECORATION,0).instabreak().noOcclusion().sound(SoundType.NETHER_SPROUTS)).modifiers(RTypeModifier.create(RType.CUTOUT)));
    public static final Supplier<Block> POTTED_SAGUARO_CACTUS =  BLOCKS.register(BiomeMakeover.ID("potted_saguaro_cactus"), ()->new BMFlowerPotBlock(SAGUARO_CACTUS.get(), properties(Material.DECORATION, 0).instabreak().noOcclusion().sound(SoundType.WOOL)).modifiers(RTypeModifier.create(RType.CUTOUT)));
    public static final Supplier<Block> POTTED_BARREL_CACTUS =  BLOCKS.register(BiomeMakeover.ID("potted_barrel_cactus"), ()->new BMFlowerPotBlock(BARREL_CACTUS.get(), properties(Material.DECORATION, 0).instabreak().noOcclusion().sound(SoundType.WOOL)).modifiers(RTypeModifier.create(RType.CUTOUT)));
    public static final Supplier<Block> POTTED_FLOWERED_BARREL_CACTUS =  BLOCKS.register(BiomeMakeover.ID("potted_flowered_barrel_cactus"), ()->new BMFlowerPotBlock(BARREL_CACTUS_FLOWERED.get(), properties(Material.DECORATION, 0).instabreak().noOcclusion().sound(SoundType.WOOL)).modifiers(RTypeModifier.create(RType.CUTOUT)));
    public static final Supplier<Block> POTTED_WILLOW_SAPLING =  BLOCKS.register(BiomeMakeover.ID("potted_willow_sapling"), ()->new BMFlowerPotBlock(WILLOW_SAPLING.get(), properties(Material.DECORATION, 0).instabreak().noOcclusion().sound(SoundType.WOOL)).modifiers(RTypeModifier.create(RType.CUTOUT)));
    public static final Supplier<Block> POTTED_SWAMP_CYPRESS_SAPLING =  BLOCKS.register(BiomeMakeover.ID("potted_swamp_cypress_sapling"), ()->new BMFlowerPotBlock(SWAMP_CYPRESS_SAPLING.get(), properties(Material.DECORATION, 0).instabreak().noOcclusion().sound(SoundType.WOOL)).modifiers(RTypeModifier.create(RType.CUTOUT)));
    public static final Supplier<Block> POTTED_ANCIENT_OAK_SAPLING =  BLOCKS.register(BiomeMakeover.ID("potted_ancient_oak_sapling"), ()->new BMFlowerPotBlock(ANCIENT_OAK_SAPLING.get(), properties(Material.DECORATION, 0).instabreak().noOcclusion().sound(SoundType.WOOL)).modifiers(RTypeModifier.create(RType.CUTOUT)));
    public static final Supplier<Block> POTTED_WILD_MUSHROOMS =  BLOCKS.register(BiomeMakeover.ID("potted_wild_mushrooms"), ()->new BMFlowerPotBlock(WILD_MUSHROOMS.get(), properties(Material.DECORATION, 0).instabreak().noOcclusion().sound(SoundType.WOOL)).modifiers(RTypeModifier.create(RType.CUTOUT)));

    public static final Supplier<Block> DIRECTIONAL_DATA =  registerBlockItem("directional_data", ()->new DirectionalDataBlock(BlockBehaviour.Properties.of(Material.STONE).strength(-1).noDrops()));

    public static final Map<DyeColor, Supplier<Block>> DYE_TO_TAPESTRY = Maps.newHashMap();
    public static final List<Supplier<Block>> TAPESTRY_BLOCKS = Lists.newArrayList();
    public static final List<Supplier<Block>> TAPESTRY_WALL_BLOCKS = Lists.newArrayList();
    public static final List<Supplier<Block>> TAPESTRY_FLOOR_BLOCKS = Lists.newArrayList();
    public static Supplier<Block> ADJUDICATOR_TAPESTRY;
    static
    {
        for(DyeColor dyeColor : DyeColor.values())
        {
            Supplier<Block> tap = ()->new ColorTapestryBlock(dyeColor, properties(Material.WOOD, 1F).noCollission().sound(SoundType.WOOD));
            DYE_TO_TAPESTRY.put(dyeColor, tap);
        }
    }

    public static final List<CompostModifier.CompostValue> COMPOSTABLES = Lists.newArrayList();

    public static void init() {
        FieldConsumer.run(BMBlocks.class, WoodTypeInfo.class, w->w.register(BLOCKS, BMItems.ITEMS));
        FieldConsumer.run(BMBlocks.class, DecorationBlockInfo.class, d->d.register(BLOCKS, BMItems.ITEMS));

        createTerracottaBricks();
        createTapestries();

        BLOCKS.register();

        FuelRegistry.register(10000, DRIED_PEAT.get());

        postRegister();
    }

    @ExpectPlatform
    private static void postRegister() {
    }

    public static final Map<Block, RType> RTYPES = Maps.newHashMap();
    public static void initClient()
    {
        EnvExecutor.runInEnv(Env.CLIENT, () -> () ->
                RTYPES.forEach((block, type) -> RenderTypeRegistry.register(type.getAsRenderType(), block)));

    }

    public static BlockBehaviour.Properties properties(Material material, float hardness)
    {
        return BlockProperties.of(material).strength(hardness, hardness);
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

        RegistrySupplier<Block> tBlock = BLOCKS.register(BiomeMakeover.ID("terracotta_bricks"), ()->new BMBlock(properties(Material.STONE, 2F).requiresCorrectToolForDrops().sound(SoundType.STONE)));
        BMItems.ITEMS.register(BiomeMakeover.ID("terracotta_bricks"), ()->new BlockItem(tBlock.get(), new Item.Properties().tab(BiomeMakeover.TAB)));
        DecorationBlockInfo terracottaBrick = new DecorationBlockInfo(Constants.MOD_ID, BiomeMakeover.TAB, "terracotta_brick", tBlock, properties(Material.STONE, 2F).requiresCorrectToolForDrops().sound(SoundType.STONE)).all();
        terracottaBrick.register(BLOCKS, BMItems.ITEMS);

        for(DyeColor dye : DyeColor.values())
        {
            ResourceLocation id = BiomeMakeover.ID(dye.getName() + "_terracotta_bricks");
            RegistrySupplier<Block> bl = BLOCKS.register(id, ()->new BMBlock(properties(Material.STONE, 2F).color(dye.getMaterialColor()).requiresCorrectToolForDrops().sound(SoundType.STONE)));
            BMItems.ITEMS.register(id, ()->new BlockItem(bl.get(), new Item.Properties().tab(BiomeMakeover.TAB)));

            DecorationBlockInfo dec = new DecorationBlockInfo(Constants.MOD_ID, BiomeMakeover.TAB, dye.getName() + "_terracotta_brick", bl, properties(Material.STONE, 2F).color(dye.getMaterialColor()).requiresCorrectToolForDrops().sound(SoundType.STONE)).all();
            dec.register(BLOCKS, BMItems.ITEMS);

            BRICK_TO_TERRACOTTA.put(bl, vanillaTerracotta.get(dye));
        }
    }

    private static void createTapestries()
    {
        for(DyeColor dye : DyeColor.values())
        {
            Supplier<Block> tap = BLOCKS.register(BiomeMakeover.ID(dye.getName() + "_tapestry"), DYE_TO_TAPESTRY.get(dye));

            Supplier<Block> wallBlock = BLOCKS.register(BiomeMakeover.ID(dye.getName() + "_wall_tapestry"), ()->new ColorTapestryWallBlock(dye, properties(Material.WOOD, 1F).noCollission().sound(SoundType.WOOD).dropsLike(tap.get())));
            BMItems.ITEMS.register(BiomeMakeover.ID(dye.getName() + "_tapestry"), ()->new StandingAndWallBlockItem(tap.get(), wallBlock.get(), BMItems.properties().stacksTo(16).rarity(Rarity.UNCOMMON)));

            TAPESTRY_BLOCKS.add(tap);
            TAPESTRY_BLOCKS.add(wallBlock);
            TAPESTRY_FLOOR_BLOCKS.add(tap);
            TAPESTRY_WALL_BLOCKS.add(wallBlock);
        }
        ADJUDICATOR_TAPESTRY = BLOCKS.register(BiomeMakeover.ID("adjudicator_tapestry"), ()->new AdjudicatorTapestryBlock(properties(Material.WOOD, 1F).noCollission().sound(SoundType.WOOD)));

        Supplier<Block> adjWall = BLOCKS.register(BiomeMakeover.ID("adjudicator_wall_tapestry"), ()->new AdjudicatorTapestryWallBlock(properties(Material.WOOD, 1F).noCollission().sound(SoundType.WOOD).dropsLike(ADJUDICATOR_TAPESTRY.get())));

        BMItems.ITEMS.register(BiomeMakeover.ID("adjudicator_tapestry"), ()->new StandingAndWallBlockItem(ADJUDICATOR_TAPESTRY.get(), adjWall.get(),  BMItems.properties().stacksTo(16).rarity(Rarity.EPIC)));

        TAPESTRY_BLOCKS.add(ADJUDICATOR_TAPESTRY);
        TAPESTRY_BLOCKS.add(adjWall);
    }

    public static void initModifiers()
    {
        MODIFIERS.forEach((b, a)->a.accept(b));
    }

    public static void initCompost()
    {
        COMPOSTABLES.forEach((p)-> ComposterBlock.COMPOSTABLES.put(p.block().asItem(), p.chance()));
    }

    private static boolean always(BlockState state, BlockGetter world, BlockPos pos)
    {
        return true;
    }

    public static Supplier<Block> registerBlockItem(String id, Supplier<Block> block)
    {
        RegistrySupplier<Block> bl = BLOCKS.register(BiomeMakeover.ID(id), block);
        BMItems.ITEMS.register(BiomeMakeover.ID(id), ()->new BlockItem(bl.get(), new Item.Properties().tab(BiomeMakeover.TAB)));

        return bl;
    }

    public static Supplier<Block> registerLilyPad(String id, Supplier<Block> block)
    {
        RegistrySupplier<Block> bl = BLOCKS.register(BiomeMakeover.ID(id), block);
        BMItems.ITEMS.register(BiomeMakeover.ID(id), ()->new WaterLilyBlockItem(bl.get(), new Item.Properties().tab(BiomeMakeover.TAB)));

        return bl;
    }

    public static final TagKey<Block> LILY_PADS = TagKey.create(Registry.BLOCK_REGISTRY, BiomeMakeover.ID("lily_pads"));
    public static final TagKey<Block> MOTH_ATTRACTIVE = TagKey.create(Registry.BLOCK_REGISTRY, BiomeMakeover.ID("moth_attractive"));
    public static final TagKey<Block> IVY_TAG = TagKey.create(Registry.BLOCK_REGISTRY, BiomeMakeover.ID("ivy"));
    public static final TagKey<Block> ITCHING_IVY_TAG = TagKey.create(Registry.BLOCK_REGISTRY, BiomeMakeover.ID("itching_ivy"));
    public static final TagKey<Block> ORE_REPLACEABLE = TagKey.create(Registry.BLOCK_REGISTRY, BiomeMakeover.ID("ore_replaceable"));
}
