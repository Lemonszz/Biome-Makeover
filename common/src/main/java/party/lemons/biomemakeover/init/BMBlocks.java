package party.lemons.biomemakeover.init;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.Tag;
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
import org.apache.commons.compress.utils.Lists;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.Constants;
import party.lemons.biomemakeover.block.*;
import party.lemons.biomemakeover.block.modifier.BlockModifier;
import party.lemons.biomemakeover.block.modifier.CompostModifier;
import party.lemons.biomemakeover.block.modifier.FlammableModifier;
import party.lemons.biomemakeover.block.modifier.RTypeModifier;
import party.lemons.biomemakeover.level.generate.foliage.AncientOakSaplingGenerator;
import party.lemons.biomemakeover.level.generate.foliage.BalsaSaplingGenerator;
import party.lemons.biomemakeover.level.generate.foliage.SwampCypressSaplingGenerator;
import party.lemons.biomemakeover.level.generate.foliage.WillowSaplingGenerator;
import party.lemons.biomemakeover.mixin.BlockTagsInvoker;
import party.lemons.biomemakeover.util.BlockItemPair;
import party.lemons.biomemakeover.util.FieldConsumer;
import party.lemons.biomemakeover.util.registry.DecorationBlockInfo;
import party.lemons.biomemakeover.util.registry.RType;
import party.lemons.biomemakeover.util.registry.RegistryHelper;
import party.lemons.biomemakeover.util.registry.WoodTypeInfo;
import party.lemons.biomemakeover.util.registry.boat.BoatTypes;

import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class BMBlocks
{
    public static final List<Block> blocks = Lists.newArrayList();
    public static final Multimap<Block, BlockModifier> MODIFIERS = ArrayListMultimap.create();


    public static final Material POLTERGEIST_MATERIAL = new Material(MaterialColor.COLOR_GRAY, false, true, true, false, true, false, PushReaction.BLOCK);
    public static final SoundType BM_LILY_PAD_SOUND = new SoundType(1.0F, 1.0F, SoundEvents.WET_GRASS_BREAK, SoundEvents.WET_GRASS_STEP, SoundEvents.LILY_PAD_PLACE, SoundEvents.WET_GRASS_HIT, SoundEvents.WET_GRASS_FALL);
    public static final SoundType ILLUNITE_SOUND = new SoundType(1.0F, 1.0F, BMEffects.ILLUNITE_BREAK, BMEffects.ILLUNITE_STEP, BMEffects.ILLUNITE_PLACE, BMEffects.ILLUNITE_HIT, SoundEvents.STONE_FALL);

    public static final BMMushroomPlantBlock PURPLE_GLOWSHROOM = new GlowshroomPlantBlock(()->BMWorldGen.MushroomFields.HUGE_PURPLE_GLOWSHROOM, properties(Material.PLANT, 0F).lightLevel((s)->13).noCollission().noCollission().sound(SoundType.FUNGUS)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.7F));
    public static final BMMushroomPlantBlock GREEN_GLOWSHROOM = new GlowshroomPlantBlock(()->BMWorldGen.MushroomFields.HUGE_GREEN_GLOWSHROOM, properties(Material.PLANT, 0F).lightLevel((s)->13).noCollission().noOcclusion().sound(SoundType.FUNGUS)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.7F));
    public static final BMMushroomPlantBlock ORANGE_GLOWSHROOM = new UnderwaterMushroomPlantBlock(()->BMWorldGen.MushroomFields.HUGE_ORANGE_GLOWSHROOM, properties(Material.PLANT, 0F).lightLevel((s)->13).noCollission().noOcclusion().sound(SoundType.FUNGUS)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.7F));

    public static final BMMushroomBlock PURPLE_GLOWSHROOM_BLOCK = new BMMushroomBlock(properties(Material.PLANT, 0.2F).lightLevel((s)->15).sound(SoundType.FUNGUS)).modifiers(CompostModifier.create(0.9F));
    public static final BMMushroomBlock GREEN_GLOWSHROOM_BLOCK = new BMMushroomBlock(properties(Material.PLANT, 0.2F).lightLevel((s)->15).sound(SoundType.FUNGUS)).modifiers(CompostModifier.create(0.9F));
    public static final BMMushroomBlock ORANGE_GLOWSHROOM_BLOCK = new BMMushroomBlock(properties(Material.PLANT, 0.2F).lightLevel((s)->15).sound(SoundType.FUNGUS)).modifiers(CompostModifier.create(0.9F));

    public static final MushroomSproutsBlock MYCELIUM_SPROUTS = new MushroomSproutsBlock(properties(Material.REPLACEABLE_FIREPROOF_PLANT, 0).noCollission().noCollission().instabreak().sound(SoundType.NETHER_SPROUTS)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.7F));
    public static final MushroomRootsBlock MYCELIUM_ROOTS = new MushroomRootsBlock(properties(Material.REPLACEABLE_FIREPROOF_PLANT, 0).noCollission().noCollission().instabreak().sound(SoundType.ROOTS)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.7F));

    public static final BMTallMushroomBlock TALL_BROWN_MUSHROOM = (BMTallMushroomBlock) new BMTallMushroomBlock(Blocks.BROWN_MUSHROOM, properties(Material.PLANT, 0).instabreak().noCollission().sound(SoundType.FUNGUS)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.7F));
    public static final BMTallMushroomBlock TALL_RED_MUSHROOM = (BMTallMushroomBlock) new BMTallMushroomBlock(Blocks.RED_MUSHROOM, properties(Material.PLANT, 0).instabreak().noCollission().sound(SoundType.FUNGUS)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.9F));

    public static WoodTypeInfo BLIGHTED_BALSA_WOOD_INFO = new WoodTypeInfo(Constants.MOD_ID, BiomeMakeover.TAB, "blighted_balsa", properties(Material.WOOD, 1.5F).sound(SoundType.WOOD)).all().boat(()-> BoatTypes.BLIGHTED_BALSA);
    public static final BMLeavesBlock BLIGHTED_BALSA_LEAVES = new BMLeavesBlock(properties(Material.LEAVES, 0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(BMBlocks::canSpawnOnLeaves).isSuffocating((a, b, c)->false).isViewBlocking((a, b, c)->false)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.3F), FlammableModifier.LEAVES);
    public static final BMSaplingBlock BLIGHTED_BALSA_SAPLING = new BMSaplingBlock(new BalsaSaplingGenerator(), properties(Material.PLANT, 0).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.4F));

    public static final BMMushroomBlock GLOWSHROOM_STEM = new BMMushroomBlock(properties(Material.GRASS, 0.2F).lightLevel((s)->7).sound(SoundType.FUNGUS)).modifiers(CompostModifier.create(0.9F));
    public static final BMBlock RED_MUSHROOM_BRICK = new BMBlock(properties(Material.GRASS, 0.8F).sound(SoundType.FUNGUS));
    public static final DecorationBlockInfo RED_MUSHROOM_BRICK_DECORATION = new DecorationBlockInfo(Constants.MOD_ID, "red_mushroom_brick", RED_MUSHROOM_BRICK, properties(Material.GRASS, 0.8F).sound(SoundType.FUNGUS)).all();
    public static final BMBlock BROWN_MUSHROOM_BRICK = new BMBlock(properties(Material.GRASS, 0.8F).sound(SoundType.FUNGUS));
    public static final DecorationBlockInfo BROWN_MUSHROOM_BRICK_DECORATION = new DecorationBlockInfo(Constants.MOD_ID, "brown_mushroom_brick", BROWN_MUSHROOM_BRICK, properties(Material.GRASS, 0.8F).sound(SoundType.FUNGUS)).all();
    public static final BMBlock PURPLE_GLOWSHROOM_BRICK = new BMBlock(properties(Material.GRASS, 0.8F).lightLevel((s)->13).sound(SoundType.FUNGUS));
    public static final DecorationBlockInfo PURPLE_GLOWSROOM_BRICK_DECORATION = new DecorationBlockInfo(Constants.MOD_ID, "purple_glowshroom_brick", PURPLE_GLOWSHROOM_BRICK, properties(Material.GRASS, 0.8F).lightLevel((s)->13).sound(SoundType.FUNGUS)).all();
    public static final BMBlock GREEN_GLOWSHROOM_BRICK = new BMBlock(properties(Material.GRASS, 0.8F).lightLevel((s)->13).sound(SoundType.FUNGUS));
    public static final DecorationBlockInfo GREEN_GLOWSROOM_BRICK_DECORATION = new DecorationBlockInfo(Constants.MOD_ID, "green_glowshroom_brick", GREEN_GLOWSHROOM_BRICK, properties(Material.GRASS, 0.8F).lightLevel((s)->13).sound(SoundType.FUNGUS)).all();
    public static final BMBlock ORANGE_GLOWSHROOM_BRICK = new BMBlock(properties(Material.GRASS, 0.8F).lightLevel((s)->13).sound(SoundType.FUNGUS));
    public static final DecorationBlockInfo ORANGE_GLOWSROOM_BRICK_DECORATION = new DecorationBlockInfo(Constants.MOD_ID, "orange_glowshroom_brick", ORANGE_GLOWSHROOM_BRICK, properties(Material.GRASS, 0.8F).lightLevel((s)->13).sound(SoundType.FUNGUS)).all();
    public static final BMBlock GLOWSHROOM_STEM_BRICK = new BMBlock(properties(Material.GRASS, 0.8F).lightLevel((s)->7).sound(SoundType.FUNGUS));
    public static final DecorationBlockInfo GLOWSHROOM_STEM_BRICK_DECORATION = new DecorationBlockInfo(Constants.MOD_ID, "glowshroom_stem_brick", GLOWSHROOM_STEM_BRICK, properties(Material.GRASS, 0.8F).lightLevel((s)->7).sound(SoundType.FUNGUS)).all();
    public static final BMBlock MUSHROOM_STEM_BRICK = new BMBlock(properties(Material.GRASS, 0.8F).sound(SoundType.FUNGUS));
    public static final DecorationBlockInfo MUSHROOM_STEM_BRICK_DECORATION = new DecorationBlockInfo(Constants.MOD_ID, "mushroom_stem_brick", MUSHROOM_STEM_BRICK, properties(Material.GRASS, 0.8F).sound(SoundType.FUNGUS)).all();
    public static final BMBlock BLIGHTED_COBBLESTONE = new BMBlock(properties(Material.STONE, 2).sound(SoundType.STONE).requiresCorrectToolForDrops());
    public static final DecorationBlockInfo BLIGHTED_COBBLESTONE_DECORATION = new DecorationBlockInfo(Constants.MOD_ID,"blighted_cobblestone", BLIGHTED_COBBLESTONE, properties(Material.STONE, 2F).requiresCorrectToolForDrops().sound(SoundType.STONE)).all();
    public static final BMBlock BLIGHTED_STONE_BRICKS = new BMBlock(properties(Material.STONE, 2).sound(SoundType.STONE).requiresCorrectToolForDrops());
    public static final DecorationBlockInfo BLIGHTED_STONE_BRICKS_DECORATION = new DecorationBlockInfo(Constants.MOD_ID,"blighted_stone_bricks", BLIGHTED_STONE_BRICKS, properties(Material.STONE, 2F).requiresCorrectToolForDrops().sound(SoundType.STONE)).all();
    public static final BMBlock PAYDIRT = new BMBlock(properties(Material.DIRT, 1.4F).requiresCorrectToolForDrops().sound(SoundType.GRAVEL));

    public static final Block TUMBLEWEED = new Block(properties(Material.PLANT, 0));

    public static final BMBlock SAGUARO_CACTUS = new SaguaroCactusBlock(properties(Material.CACTUS, 0.4F).sound(SoundType.WOOL).randomTicks()).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.15F));
    public static final BMBlock BARREL_CACTUS = new BarrelCactusBlock(false, properties(Material.CACTUS, 0).randomTicks().sound(SoundType.WOOL).noOcclusion().instabreak().noCollission()).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.3F));
    public static final BMBlock BARREL_CACTUS_FLOWERED = new BarrelCactusBlock(true, properties(Material.CACTUS, 0).sound(SoundType.WOOL).noOcclusion().instabreak().noCollission()).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.3F));

    public static final BMBlock POLTERGEIST = new PoltergeistBlock(properties(POLTERGEIST_MATERIAL, 1.0F).lightLevel((bs)->bs.getValue(PoltergeistBlock.ENABLED) ? 7 : 0).sound(SoundType.LODESTONE)).modifiers(RTypeModifier.create(RType.CUTOUT));
    public static final Block ECTOPLASM_COMPOSTER = new EctoplasmComposterBlock(properties(Material.WOOD, 0.6F).sound(SoundType.WOOD));

    public static WoodTypeInfo WILLOW_WOOD_INFO = new WoodTypeInfo(Constants.MOD_ID, BiomeMakeover.TAB, "willow", properties(Material.WOOD, 1.5F).sound(SoundType.WOOD)).all().boat(()->BoatTypes.WILLOW);;
    public static WoodTypeInfo SWAMP_CYPRESS_WOOD_INFO = new WoodTypeInfo(Constants.MOD_ID, BiomeMakeover.TAB, "swamp_cypress", properties(Material.WOOD, 1.5F).sound(SoundType.WOOD)).all().boat(()->BoatTypes.SWAMP_CYPRESS);;

    public static final BMBlock WILLOWING_BRANCHES = new WillowingBranchesBlock(properties(Material.PLANT, 0.1F).randomTicks().sound(SoundType.VINE).noCollission().noOcclusion()).modifiers(RTypeModifier.create(RType.CUTOUT_MIPPED), CompostModifier.create(0.3F), new FlammableModifier(15, 100));
    public static final BMSaplingBlock WILLOW_SAPLING = new WaterSaplingBlock(new WillowSaplingGenerator(), 1, properties(Material.PLANT, 0).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.4F));
    public static final BMSaplingBlock SWAMP_CYPRESS_SAPLING = new WaterSaplingBlock(new SwampCypressSaplingGenerator(), 3, properties(Material.PLANT, 0).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.4F));
    public static final BMBlock PEAT = new BMBlock(properties(Material.DIRT, 0.5F).sound(SoundType.WET_GRASS));
    public static final BMBlock DRIED_PEAT = new BMBlock(properties(Material.DIRT, 1F).sound(SoundType.NETHERRACK));
    public static final BMSpreadableBlock MOSSY_PEAT = new BMSpreadableBlock(properties(Material.DIRT, 0.5F).randomTicks().sound(SoundType.WET_GRASS), ()->PEAT);
    public static final PeatFarmlandBlock PEAT_FARMLAND = new PeatFarmlandBlock(properties(Material.DIRT, 0.5F).sound(SoundType.WET_GRASS).randomTicks().noOcclusion());
    public static final BMBlock DRIED_PEAT_BRICKS = new BMBlock(properties(Material.STONE, 2).sound(SoundType.STONE).requiresCorrectToolForDrops());
    public static final DecorationBlockInfo DRIED_PEAT_BRICKS_DECORATION = new DecorationBlockInfo(Constants.MOD_ID, "dried_peat_bricks", DRIED_PEAT_BRICKS, properties(Material.STONE, 2F).sound(SoundType.NETHERRACK).requiresCorrectToolForDrops().sound(SoundType.STONE)).all();
    public static final BMBlock MOSSY_DRIED_PEAT_BRICKS = new BMBlock(properties(Material.STONE, 2).sound(SoundType.STONE).requiresCorrectToolForDrops());
    public static final DecorationBlockInfo MOSSY_DRIED_PEAT_BRICKS_DECORATION = new DecorationBlockInfo(Constants.MOD_ID, "mossy_dried_peat_brick", MOSSY_DRIED_PEAT_BRICKS, properties(Material.STONE, 2F).sound(SoundType.NETHERRACK).requiresCorrectToolForDrops().sound(SoundType.STONE)).all();
    public static final BMBlock CRACKED_DRIED_PEAT_BRICKS = new BMBlock(properties(Material.STONE, 2).sound(SoundType.STONE).requiresCorrectToolForDrops());
    public static final DecorationBlockInfo CRACKED_DRIED_PEAT_BRICKS_DECORATION = new DecorationBlockInfo(Constants.MOD_ID, "cracked_dried_peat_brick", CRACKED_DRIED_PEAT_BRICKS, properties(Material.STONE, 2F).sound(SoundType.NETHERRACK).requiresCorrectToolForDrops().sound(SoundType.STONE)).all();
    public static final BMTallFlowerBlock SWAMP_AZALEA = new BMTallFlowerBlock(properties(Material.PLANT, 0).noCollission().instabreak().sound(SoundType.GRASS)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.65F), FlammableModifier.TALL_FLOWER);
    public static final BMTallFlowerBlock MARIGOLD = new BMTallFlowerBlock(properties(Material.PLANT, 0).noCollission().instabreak().sound(SoundType.GRASS)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.65F), FlammableModifier.TALL_FLOWER);
    public static final BMTallFlowerBlock BLACK_THISTLE = new BMTallFlowerBlock(properties(Material.PLANT, 0).noCollission().instabreak().sound(SoundType.GRASS)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.65F), FlammableModifier.TALL_FLOWER);
    public static final BMTallFlowerBlock FOXGLOVE = new BMTallFlowerBlock(properties(Material.PLANT, 0).noCollission().instabreak().sound(SoundType.GRASS)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.65F), FlammableModifier.TALL_FLOWER);

    public static final BMTallFlowerBlock CATTAIL = new ReedBlock(properties(Material.PLANT, 0).instabreak().noCollission().sound(SoundType.GRASS)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.5F));
    public static final BMTallFlowerBlock REED = new ReedBlock(properties(Material.PLANT, 0).instabreak().noCollission().sound(SoundType.GRASS)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.2F));
    public static final SmallLilyPadBlock SMALL_LILY_PAD = new SmallLilyPadBlock(properties(Material.PLANT, 0).instabreak().sound(BM_LILY_PAD_SOUND)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.3F));
    public static final FloweredWaterlilyPadBlock WATER_LILY = new FloweredWaterlilyPadBlock(properties(Material.PLANT, 0).instabreak().sound(BM_LILY_PAD_SOUND)).modifiers(RTypeModifier.create(RType.CUTOUT), CompostModifier.create(0.8F));
    public static final BMLeavesBlock WILLOW_LEAVES = new BMLeavesBlock(properties(Material.LEAVES, 0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(BMBlocks::canSpawnOnLeaves).isSuffocating((a, b, c)->false).isViewBlocking((a, b, c)->false)).modifiers(RTypeModifier.create(RType.CUTOUT_MIPPED), CompostModifier.create(0.3F), FlammableModifier.LEAVES);
    public static final BMLeavesBlock SWAMP_CYPRESS_LEAVES = new BMLeavesBlock(properties(Material.LEAVES, 0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(BMBlocks::canSpawnOnLeaves).isSuffocating((a, b, c)->false).isViewBlocking((a, b, c)->false)).modifiers(RTypeModifier.create(RType.CUTOUT_MIPPED), CompostModifier.create(0.3F), FlammableModifier.LEAVES);
    public static final BMBlock LIGHTNING_BUG_BOTTLE = new LightningBugBottleBlock(properties(Material.STONE, 0.5F).lightLevel((b)->15).noOcclusion()).modifiers(RTypeModifier.create(RType.CUTOUT));

    public static final IlluniteClusterBlock ILLUNITE_CLUSTER = new IlluniteClusterBlock(properties(Material.STONE, 0.5F).sound(ILLUNITE_SOUND).noOcclusion().hasPostProcess(BMBlocks::always).emissiveRendering(BMBlocks::always)).modifiers(RTypeModifier.create(RType.CUTOUT));;
    public static final Block ILLUNITE_BLOCK = new BMBlock(properties(Material.STONE, 1.5F).requiresCorrectToolForDrops().sound(ILLUNITE_SOUND));
    public static final Block MESMERITE = new BMBlock(properties(Material.STONE, 1.5F));
    public static final DecorationBlockInfo MESMERITE_DECORATION = new DecorationBlockInfo(Constants.MOD_ID, "mesmerite", MESMERITE, properties(Material.STONE, 1.5F)).all();
    public static final Block POLISHED_MESMERITE = new BMBlock(properties(Material.STONE, 1.5F));
    public static final DecorationBlockInfo POLISHED_MESMERITE_DECORATION = new DecorationBlockInfo(Constants.MOD_ID, "polished_mesmerite", POLISHED_MESMERITE, properties(Material.STONE, 1.5F)).all();
    public static WoodTypeInfo ANCIENT_OAK_WOOD_INFO = new WoodTypeInfo(Constants.MOD_ID, BiomeMakeover.TAB, "ancient_oak", properties(Material.WOOD, 1.5F).sound(SoundType.WOOD)).all().boat(()->BoatTypes.ANCIENT_OAK);;

    public static final BMSaplingBlock ANCIENT_OAK_SAPLING = new BMSaplingBlock(new AncientOakSaplingGenerator(), properties(Material.PLANT, 0).noCollission().randomTicks().instabreak().sound(SoundType.GRASS)).modifiers(RTypeModifier.create(RType.CUTOUT));
    public static final BMLeavesBlock ANCIENT_OAK_LEAVES = new BMLeavesBlock(properties(Material.LEAVES, 0.2F).randomTicks().sound(SoundType.GRASS).noOcclusion().isValidSpawn(BMBlocks::canSpawnOnLeaves).isSuffocating((a, b, c)->false).isViewBlocking((a, b, c)->false)).modifiers(RTypeModifier.create(RType.CUTOUT_MIPPED), CompostModifier.create(0.3F), FlammableModifier.LEAVES);
    public static final BMBlock ALTAR = new AltarBlock(properties(Material.STONE, 1.5F).lightLevel((st)->st.getValue(AltarBlock.ACTIVE) ? 5 : 1).requiresCorrectToolForDrops().noOcclusion()).modifiers(RTypeModifier.create(RType.CUTOUT));
    public static final Block CLADDED_STONE = new BMBlock(properties(Material.STONE, 1.5F).requiresCorrectToolForDrops());

    public static final RootlingCropBlock ROOTLING_CROP = new RootlingCropBlock(properties(Material.PLANT, 0).noCollission().randomTicks().instabreak().sound(SoundType.CROP)).modifiers(RTypeModifier.create(RType.CUTOUT));
    public static final IvyShapedBlock IVY = new IvyBlock(properties(Material.REPLACEABLE_PLANT, 0.15F).noCollission().randomTicks().sound(SoundType.VINE)).modifiers(RTypeModifier.create(RType.CUTOUT), FlammableModifier.IVY);
    public static final IvyShapedBlock ITCHING_IVY = new ItchingIvyBlock(properties(Material.REPLACEABLE_PLANT, 0.15F).speedFactor(0.5F).noCollission().randomTicks().sound(SoundType.VINE)).modifiers(RTypeModifier.create(RType.CUTOUT), FlammableModifier.IVY);
    public static final IvyShapedBlock MOTH_BLOSSOM = new MothBlossomBlock(properties(Material.PLANT, 0.25F).speedFactor(0.5F).noCollission().randomTicks().sound(SoundType.VINE)).modifiers(RTypeModifier.create(RType.CUTOUT), FlammableModifier.IVY);
    public static final BMMushroomPlantBlock WILD_MUSHROOMS = new WildMushroomBlock(properties(Material.PLANT, 0F).noCollission().randomTicks().noOcclusion().sound(SoundType.FUNGUS)).modifiers(RTypeModifier.create(RType.CUTOUT), FlammableModifier.IVY);

    public static final BMFlowerPotBlock POTTED_MYCELIUM_ROOTS = new BMFlowerPotBlock(MYCELIUM_ROOTS, properties(Material.DECORATION, 0).instabreak().noOcclusion().sound(SoundType.NETHER_SPROUTS)).modifiers(RTypeModifier.create(RType.CUTOUT));
    public static final BMFlowerPotBlock POTTED_PURPLE_GLOWSHROOM = new BMFlowerPotBlock(PURPLE_GLOWSHROOM, properties(Material.DECORATION, 0).lightLevel((s)->13).instabreak().noOcclusion().sound(SoundType.NETHER_SPROUTS)).modifiers(RTypeModifier.create(RType.CUTOUT));
    public static final BMFlowerPotBlock POTTED_GREEN_GLOWSHROOM = new BMFlowerPotBlock(GREEN_GLOWSHROOM, properties(Material.DECORATION, 0).lightLevel((s)->13).instabreak().noOcclusion().sound(SoundType.NETHER_SPROUTS)).modifiers(RTypeModifier.create(RType.CUTOUT));
    public static final BMFlowerPotBlock POTTED_ORANGE_GLOWSHROOM = new BMFlowerPotBlock(ORANGE_GLOWSHROOM, properties(Material.DECORATION,0).lightLevel((s)->13).instabreak().noOcclusion().sound(SoundType.NETHER_SPROUTS)).modifiers(RTypeModifier.create(RType.CUTOUT));
    public static final BMFlowerPotBlock POTTED_BLIGHTED_BALSA_SAPLING = new BMFlowerPotBlock(BLIGHTED_BALSA_SAPLING, properties(Material.DECORATION,0).instabreak().noOcclusion().sound(SoundType.NETHER_SPROUTS)).modifiers(RTypeModifier.create(RType.CUTOUT));
    public static final BMFlowerPotBlock POTTED_SAGUARO_CACTUS = new BMFlowerPotBlock(SAGUARO_CACTUS, properties(Material.DECORATION, 0).instabreak().noOcclusion().sound(SoundType.WOOL)).modifiers(RTypeModifier.create(RType.CUTOUT));
    public static final BMFlowerPotBlock POTTED_BARREL_CACTUS = new BMFlowerPotBlock(BARREL_CACTUS, properties(Material.DECORATION, 0).instabreak().noOcclusion().sound(SoundType.WOOL)).modifiers(RTypeModifier.create(RType.CUTOUT));
    public static final BMFlowerPotBlock POTTED_FLOWERED_BARREL_CACTUS = new BMFlowerPotBlock(BARREL_CACTUS_FLOWERED, properties(Material.DECORATION, 0).instabreak().noOcclusion().sound(SoundType.WOOL)).modifiers(RTypeModifier.create(RType.CUTOUT));
    public static final BMFlowerPotBlock POTTED_WILLOW_SAPLING = new BMFlowerPotBlock(WILLOW_SAPLING, properties(Material.DECORATION, 0).instabreak().noOcclusion().sound(SoundType.WOOL)).modifiers(RTypeModifier.create(RType.CUTOUT));
    public static final BMFlowerPotBlock POTTED_SWAMP_CYPRESS_SAPLING = new BMFlowerPotBlock(SWAMP_CYPRESS_SAPLING, properties(Material.DECORATION, 0).instabreak().noOcclusion().sound(SoundType.WOOL)).modifiers(RTypeModifier.create(RType.CUTOUT));
    public static final BMFlowerPotBlock POTTED_ANCIENT_OAK_SAPLING = new BMFlowerPotBlock(ANCIENT_OAK_SAPLING, properties(Material.DECORATION, 0).instabreak().noOcclusion().sound(SoundType.WOOL)).modifiers(RTypeModifier.create(RType.CUTOUT));
    public static final BMFlowerPotBlock POTTED_WILD_MUSHROOMS = new BMFlowerPotBlock(WILD_MUSHROOMS, properties(Material.DECORATION, 0).instabreak().noOcclusion().sound(SoundType.WOOL)).modifiers(RTypeModifier.create(RType.CUTOUT));

    public static final DirectionalDataBlock DIRECTIONAL_DATA = new DirectionalDataBlock(properties(Material.STONE, -1).noDrops());

    public static final Map<DyeColor, Block> DYE_TO_TAPESTRY = Maps.newHashMap();
    public static final List<Block> TAPESTRY_BLOCKS = Lists.newArrayList();
    public static final List<Block> TAPESTRY_WALL_BLOCKS = Lists.newArrayList();
    public static final List<Block> TAPESTRY_FLOOR_BLOCKS = Lists.newArrayList();
    public static Block ADJUDICATOR_TAPESTRY;
    static
    {
        for(DyeColor dyeColor : DyeColor.values())
        {
            ColorTapestryBlock tap = new ColorTapestryBlock(dyeColor, properties(Material.WOOD, 1F).noCollission().sound(SoundType.WOOD));
            DYE_TO_TAPESTRY.put(dyeColor, tap);
            TAPESTRY_BLOCKS.add(tap);
            TAPESTRY_FLOOR_BLOCKS.add(tap);
        }
    }

    public static final List<CompostModifier.CompostValue> COMPOSTABLES = Lists.newArrayList();

    public static void init() {
        RegistryHelper.register(Constants.MOD_ID, Registry.BLOCK, Block.class, BMBlocks.class, new RegistryHelper.BlockWithItemCallback(BiomeMakeover.TAB), (registry, registryObject, identifier) -> blocks.add(registryObject));

        FieldConsumer.run(BMBlocks.class, WoodTypeInfo.class, WoodTypeInfo::register);
        FieldConsumer.run(BMBlocks.class, DecorationBlockInfo.class, DecorationBlockInfo::register);

        createTerracottaBricks();
        createTapestries();

        MODIFIERS.forEach((b, a)->a.accept(b));
    }

    public static final Map<Block, RType> RTYPES = Maps.newHashMap();
    public static void initClient()
    {
        EnvExecutor.runInEnv(Env.CLIENT, () -> () ->
                RTYPES.forEach((block, type) -> RenderTypeRegistry.register(type.getAsRenderType(), block)));

    }

    public static BlockItemPair registerBlockAndItem(Block block, ResourceLocation id)
    {
        BlockItem bi = new BlockItem(block, BMItems.properties());
        RegistryHelper.registerObject(Registry.BLOCK, id, block);
        RegistryHelper.registerObject(Registry.ITEM, id, bi);

        return BlockItemPair.of(block, bi);
    }

    public static BlockBehaviour.Properties properties(Material material, float hardness)
    {
        return  BlockBehaviour.Properties.of(material).strength(hardness, hardness);
    }

    public static Boolean canSpawnOnLeaves(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, EntityType<?> type)
    {
        return type == EntityType.OCELOT || type == EntityType.PARROT;
    }

    public static final Map<Block, Block> BRICK_TO_TERRACOTTA = Maps.newHashMap();
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

        BlockItemPair terracottaBricks = registerBlockAndItem(new BMBlock(properties(Material.STONE, 2F).requiresCorrectToolForDrops().sound(SoundType.STONE)), BiomeMakeover.ID("terracotta_bricks"));
        DecorationBlockInfo terracottaBrick = new DecorationBlockInfo(Constants.MOD_ID, "terracotta_brick", terracottaBricks.getBlock(), properties(Material.STONE, 2F).requiresCorrectToolForDrops().sound(SoundType.STONE)).all();
        terracottaBrick.register();

        for(DyeColor dye : DyeColor.values())
        {
            BlockItemPair brick = registerBlockAndItem(new BMBlock(properties(Material.STONE, 2F).color(dye.getMaterialColor()).requiresCorrectToolForDrops().sound(SoundType.STONE)), BiomeMakeover.ID(dye.getName() + "_terracotta_bricks"));
            DecorationBlockInfo dec = new DecorationBlockInfo(Constants.MOD_ID, dye.getName() + "_terracotta_brick", brick.getBlock(), properties(Material.STONE, 2F).color(dye.getMaterialColor()).requiresCorrectToolForDrops().sound(SoundType.STONE)).all();
            dec.register();

            BRICK_TO_TERRACOTTA.put(brick.getBlock(), vanillaTerracotta.get(dye));
        }
    }

    private static void createTapestries()
    {
        for(DyeColor dye : DyeColor.values())
        {
            Block tap = DYE_TO_TAPESTRY.get(dye);
            RegistryHelper.registerObject(Registry.BLOCK, BiomeMakeover.ID(dye.getName() + "_tapestry"), tap);

            ColorTapestryWallBlock wallBlock = new ColorTapestryWallBlock(dye, properties(Material.WOOD, 1F).noCollission().sound(SoundType.WOOD).dropsLike(tap));
            RegistryHelper.registerObject(Registry.BLOCK, BiomeMakeover.ID(dye.getName() + "_wall_tapestry"), wallBlock);

            StandingAndWallBlockItem blItem = new StandingAndWallBlockItem(tap, wallBlock, BMItems.properties().stacksTo(16).rarity(Rarity.UNCOMMON));
            RegistryHelper.registerObject(Registry.ITEM, BiomeMakeover.ID(dye.getName() + "_tapestry"), blItem);

            TAPESTRY_BLOCKS.add(wallBlock);
            TAPESTRY_WALL_BLOCKS.add(wallBlock);
        }
        ADJUDICATOR_TAPESTRY = new AdjudicatorTapestryBlock(properties(Material.WOOD, 1F).noCollission().sound(SoundType.WOOD));
        RegistryHelper.registerObject(Registry.BLOCK, BiomeMakeover.ID("adjudicator_tapestry"), ADJUDICATOR_TAPESTRY);
        AdjudicatorTapestryWallBlock adjWall = new AdjudicatorTapestryWallBlock(properties(Material.WOOD, 1F).noCollission().sound(SoundType.WOOD).dropsLike(ADJUDICATOR_TAPESTRY));
        RegistryHelper.registerObject(Registry.BLOCK, BiomeMakeover.ID("adjudicator_wall_tapestry"), adjWall);
        StandingAndWallBlockItem adjBLItem = new StandingAndWallBlockItem(ADJUDICATOR_TAPESTRY, adjWall,  BMItems.properties().stacksTo(16).rarity(Rarity.EPIC));
        RegistryHelper.registerObject(Registry.ITEM, BiomeMakeover.ID("adjudicator_tapestry"), adjBLItem);

        TAPESTRY_BLOCKS.add(ADJUDICATOR_TAPESTRY);
        TAPESTRY_BLOCKS.add(adjWall);
    }

    public static void initCompostables()
    {
        COMPOSTABLES.forEach((p)->ComposterBlock.COMPOSTABLES.put(p.block(), p.chance()));
    }

    private static boolean always(BlockState state, BlockGetter world, BlockPos pos)
    {
        return true;
    }

    public static final Tag<Block> LILY_PADS = BlockTagsInvoker.callBind(BiomeMakeover.ID("lily_pads").toString());
    public static final Tag<Block> MOTH_ATTRACTIVE = BlockTagsInvoker.callBind(BiomeMakeover.ID("moth_attractive").toString());
    public static final Tag<Block> IVY_TAG = BlockTagsInvoker.callBind(BiomeMakeover.ID("ivy").toString());
    public static final Tag<Block> ITCHING_IVY_TAG = BlockTagsInvoker.callBind(BiomeMakeover.ID("itching_ivy").toString());

}
