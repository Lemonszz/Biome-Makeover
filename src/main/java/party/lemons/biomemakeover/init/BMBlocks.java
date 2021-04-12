package party.lemons.biomemakeover.init;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.fabricmc.fabric.api.tag.TagRegistry;
import net.fabricmc.fabric.api.tool.attribute.v1.FabricToolTags;
import net.minecraft.block.*;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.WallStandingBlockItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.Tag;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.block.*;
import party.lemons.biomemakeover.util.*;
import party.lemons.biomemakeover.util.access.FireBlockAccessor;
import party.lemons.biomemakeover.util.access.SignTypeHelper;
import party.lemons.biomemakeover.util.boat.BoatTypes;
import party.lemons.biomemakeover.world.feature.foliage.AncientOakSaplingGenerator;
import party.lemons.biomemakeover.world.feature.foliage.BalsaSaplingGenerator;
import party.lemons.biomemakeover.world.feature.foliage.SwampCypressSaplingGenerator;
import party.lemons.biomemakeover.world.feature.foliage.WillowSaplingGenerator;

import java.util.List;
import java.util.Map;

public class BMBlocks
{
	public static final Material POLTERGEISTER_MATERIAL = new Material(MaterialColor.WHITE, false, true, true, false, true, false, PistonBehavior.BLOCK);
	public static final BlockSoundGroup BM_LILY_PAD_SOUNDS = new BlockSoundGroup(1.0F, 1.0F, SoundEvents.BLOCK_WET_GRASS_BREAK, SoundEvents.BLOCK_WET_GRASS_STEP, SoundEvents.BLOCK_LILY_PAD_PLACE, SoundEvents.BLOCK_WET_GRASS_HIT, SoundEvents.BLOCK_WET_GRASS_FALL);
	public static final BlockSoundGroup ILLUNITE_SOUNDS = new BlockSoundGroup(1.0F, 1.0F, BMEffects.ILLUNITE_BREAK, BMEffects.ILLUNITE_STEP, BMEffects.ILLUNITE_PLACE, BMEffects.ILLUNITE_HIT, SoundEvents.BLOCK_STONE_FALL);

	public static final BMMushroomPlantBlock PURPLE_GLOWSHROOM = new GlowshroomPlantBlock(()->BMWorldGen.UNDERGROUND_HUGE_PURPLE_GLOWSHROOM_FEATURE_CONFIGURED, settings(Material.PLANT, RLayer.CUTOUT, 0F).lightLevel(13).noCollision().nonOpaque().sounds(BlockSoundGroup.FUNGUS));
	public static final BMMushroomPlantBlock GREEN_GLOWSHROOM = new GlowshroomPlantBlock(()->BMWorldGen.UNDERGROUND_HUGE_GREEN_GLOWSHROOM_FEATURE_CONFIGURED, settings(Material.PLANT, RLayer.CUTOUT, 0F).lightLevel(13).noCollision().nonOpaque().sounds(BlockSoundGroup.FUNGUS));
	public static final UnderwaterMushroomPlantBlock ORANGE_GLOWSHROOM = new UnderwaterMushroomPlantBlock(()->BMWorldGen.HUGE_ORANGE_GLOWSHROOM_FEATURE_CONFIGURED, settings(Material.PLANT, RLayer.CUTOUT, 0F).lightLevel(13).noCollision().nonOpaque().sounds(BlockSoundGroup.FUNGUS));

	public static final BMMushroomBlock PURPLE_GLOWSHROOM_BLOCK = new BMMushroomBlock(settings(Material.SOLID_ORGANIC, 0.2F).lightLevel(15).sounds(BlockSoundGroup.FUNGUS));
	public static final BMMushroomBlock GREEN_GLOWSHROOM_BLOCK = new BMMushroomBlock(settings(Material.SOLID_ORGANIC, 0.2F).lightLevel(15).sounds(BlockSoundGroup.FUNGUS));
	public static final BMMushroomBlock ORANGE_GLOWSHROOM_BLOCK = new BMMushroomBlock(settings(Material.SOLID_ORGANIC, 0.2F).lightLevel(15).sounds(BlockSoundGroup.FUNGUS));

	public static final MushroomSproutsBlock MYCELIUM_SPROUTS = new MushroomSproutsBlock(settings(Material.NETHER_SHOOTS, RLayer.CUTOUT, 0).noCollision().nonOpaque().breakInstantly().sounds(BlockSoundGroup.NETHER_SPROUTS));
	public static final MushroomRootsBlock MYCELIUM_ROOTS = new MushroomRootsBlock(settings(Material.NETHER_SHOOTS, RLayer.CUTOUT, 0).noCollision().nonOpaque().breakInstantly().sounds(BlockSoundGroup.ROOTS));

	public static final BMTallMushroomBlock TALL_BROWN_MUSHROOM = new BMTallMushroomBlock(Blocks.BROWN_MUSHROOM, settings(Material.PLANT, RLayer.CUTOUT, 0).breakInstantly().noCollision().sounds(BlockSoundGroup.FUNGUS));
	public static final BMTallMushroomBlock TALL_RED_MUSHROOM = new BMTallMushroomBlock(Blocks.RED_MUSHROOM, settings(Material.PLANT, RLayer.CUTOUT, 0).breakInstantly().noCollision().sounds(BlockSoundGroup.FUNGUS));

	public static WoodTypeInfo BLIGHTED_BALSA_WOOD_INFO = new WoodTypeInfo("blighted_balsa", settings(Material.WOOD, 1.5F).sounds(BlockSoundGroup.WOOD)).all();
	public static final BMLeavesBlock BLIGHTED_BALSA_LEAVES = new BMLeavesBlock(settings(Material.LEAVES, 0.2F).ticksRandomly().sounds(BlockSoundGroup.GRASS).nonOpaque().allowsSpawning(BMBlocks::canSpawnOnLeaves).suffocates((a, b, c)->false).blockVision((a, b, c)->false));
	public static final BMSaplingBlock BLIGHTED_BALSA_SAPLING = new BMSaplingBlock(new BalsaSaplingGenerator(), settings(Material.PLANT, RLayer.CUTOUT, 0).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.GRASS));

	public static final BMMushroomBlock GLOWSHROOM_STEM = new BMMushroomBlock(settings(Material.SOLID_ORGANIC, 0.2F).lightLevel(7).sounds(BlockSoundGroup.FUNGUS));
	public static final BMBlock RED_MUSHROOM_BRICK = new BMBlock(settings(Material.SOLID_ORGANIC, 0.8F).sounds(BlockSoundGroup.FUNGUS));
	public static final DecorationBlockInfo RED_MUSHROOM_BRICK_DECORATION = new DecorationBlockInfo("red_mushroom_brick", RED_MUSHROOM_BRICK, settings(Material.SOLID_ORGANIC, 0.8F).sounds(BlockSoundGroup.FUNGUS)).all();
	public static final BMBlock BROWN_MUSHROOM_BRICK = new BMBlock(settings(Material.SOLID_ORGANIC, 0.8F).sounds(BlockSoundGroup.FUNGUS));
	public static final DecorationBlockInfo BROWN_MUSHROOM_BRICK_DECORATION = new DecorationBlockInfo("brown_mushroom_brick", BROWN_MUSHROOM_BRICK, settings(Material.SOLID_ORGANIC, 0.8F).sounds(BlockSoundGroup.FUNGUS)).all();
	public static final BMBlock PURPLE_GLOWSHROOM_BRICK = new BMBlock(settings(Material.SOLID_ORGANIC, 0.8F).lightLevel(13).sounds(BlockSoundGroup.FUNGUS));
	public static final DecorationBlockInfo PURPLE_GLOWSROOM_BRICK_DECORATION = new DecorationBlockInfo("purple_glowshroom_brick", PURPLE_GLOWSHROOM_BRICK, settings(Material.SOLID_ORGANIC, 0.8F).lightLevel(13).sounds(BlockSoundGroup.FUNGUS)).all();
	public static final BMBlock GREEN_GLOWSHROOM_BRICK = new BMBlock(settings(Material.SOLID_ORGANIC, 0.8F).lightLevel(13).sounds(BlockSoundGroup.FUNGUS));
	public static final DecorationBlockInfo GREEN_GLOWSROOM_BRICK_DECORATION = new DecorationBlockInfo("green_glowshroom_brick", GREEN_GLOWSHROOM_BRICK, settings(Material.SOLID_ORGANIC, 0.8F).lightLevel(13).sounds(BlockSoundGroup.FUNGUS)).all();
	public static final BMBlock ORANGE_GLOWSHROOM_BRICK = new BMBlock(settings(Material.SOLID_ORGANIC, 0.8F).lightLevel(13).sounds(BlockSoundGroup.FUNGUS));
	public static final DecorationBlockInfo ORANGE_GLOWSROOM_BRICK_DECORATION = new DecorationBlockInfo("orange_glowshroom_brick", ORANGE_GLOWSHROOM_BRICK, settings(Material.SOLID_ORGANIC, 0.8F).lightLevel(13).sounds(BlockSoundGroup.FUNGUS)).all();
	public static final BMBlock GLOWSHROOM_STEM_BRICK = new BMBlock(settings(Material.SOLID_ORGANIC, 0.8F).lightLevel(7).sounds(BlockSoundGroup.FUNGUS));
	public static final DecorationBlockInfo GLOWSHROOM_STEM_BRICK_DECORATION = new DecorationBlockInfo("glowshroom_stem_brick", GLOWSHROOM_STEM_BRICK, settings(Material.SOLID_ORGANIC, 0.8F).lightLevel(7).sounds(BlockSoundGroup.FUNGUS)).all();
	public static final BMBlock MUSHROOM_STEM_BRICK = new BMBlock(settings(Material.SOLID_ORGANIC, 0.8F).sounds(BlockSoundGroup.FUNGUS));
	public static final DecorationBlockInfo MUSHROOM_STEM_BRICK_DECORATION = new DecorationBlockInfo("mushroom_stem_brick", MUSHROOM_STEM_BRICK, settings(Material.SOLID_ORGANIC, 0.8F).sounds(BlockSoundGroup.FUNGUS)).all();
	public static final BMBlock BLIGHTED_COBBLESTONE = new BMBlock(settings(Material.STONE, 2).sounds(BlockSoundGroup.STONE).requiresTool());
	public static final DecorationBlockInfo BLIGHTED_COBBLESTONE_DECORATION = new DecorationBlockInfo("blighted_cobblestone", BLIGHTED_COBBLESTONE, settings(Material.STONE, 2F).requiresTool().sounds(BlockSoundGroup.STONE)).all();
	public static final BMBlock BLIGHTED_STONE_BRICKS = new BMBlock(settings(Material.STONE, 2).sounds(BlockSoundGroup.STONE).requiresTool());
	public static final DecorationBlockInfo BLIGHTED_STONE_BRICKS_DECORATION = new DecorationBlockInfo("blighted_stone_bricks", BLIGHTED_STONE_BRICKS, settings(Material.STONE, 2F).requiresTool().sounds(BlockSoundGroup.STONE)).all();

	public static final Block TUMBLEWEED = new Block(settings(Material.PLANT, RLayer.CUTOUT, 0));

	public static final SaguaroCactusBlock SAGUARO_CACTUS = new SaguaroCactusBlock(settings(Material.CACTUS, RLayer.CUTOUT, 0.4F).sounds(BlockSoundGroup.WOOL).ticksRandomly());
	public static final BMBlock BARREL_CACTUS = new BarrelCactusBlock(false, settings(Material.CACTUS, RLayer.CUTOUT, 0).ticksRandomly().sounds(BlockSoundGroup.WOOL).nonOpaque().breakInstantly().noCollision());
	public static final BMBlock BARREL_CACTUS_FLOWERED = new BarrelCactusBlock(true, settings(Material.CACTUS, RLayer.CUTOUT, 0).sounds(BlockSoundGroup.WOOL).nonOpaque().breakInstantly().noCollision());

	public static final BMBlock PAYDIRT = new BMBlock(settings(Material.SOIL, 1.4F).breakByTool(FabricToolTags.SHOVELS).sounds(BlockSoundGroup.GRAVEL));
	public static final BMBlock POLTERGEIST = new PoltergeistBlock(settings(POLTERGEISTER_MATERIAL, RLayer.CUTOUT, 1.0F).luminance((bs)->bs.get(PoltergeistBlock.ENABLED) ? 7 : 0).sounds(BlockSoundGroup.LODESTONE));
	public static final Block ECTOPLASM_COMPOSTER = new EctoplasmComposterBlock(settings(Material.WOOD, 0.6F).sounds(BlockSoundGroup.WOOD));

	public static WoodTypeInfo WILLOW_WOOD_INFO = new WoodTypeInfo("willow", settings(Material.WOOD, 1.5F).sounds(BlockSoundGroup.WOOD)).all();
	public static WoodTypeInfo SWAMP_CYPRESS_WOOD_INFO = new WoodTypeInfo("swamp_cypress", settings(Material.WOOD, 1.5F).sounds(BlockSoundGroup.WOOD)).all();

	public static final BMBlock WILLOWING_BRANCHES = new WillowingBranchesBlock(settings(Material.PLANT, RLayer.CUTOUT_MIPPED, 0.1F).ticksRandomly().sounds(BlockSoundGroup.VINE).noCollision().nonOpaque());
	public static final BMSaplingBlock WILLOW_SAPLING = new WaterSaplingBlock(new WillowSaplingGenerator(), 1, settings(Material.PLANT, RLayer.CUTOUT, 0).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.GRASS));
	public static final BMSaplingBlock SWAMP_CYPRESS_SAPLING = new WaterSaplingBlock(new SwampCypressSaplingGenerator(), 3, settings(Material.PLANT, RLayer.CUTOUT, 0).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.GRASS));
	public static final BMBlock PEAT = new BMBlock(settings(Material.SOIL, 0.5F).breakByHand(true).breakByTool(FabricToolTags.SHOVELS).sounds(BlockSoundGroup.WET_GRASS));
	public static final BMBlock DRIED_PEAT = new BMBlock(settings(Material.SOIL, 1F).breakByHand(true).breakByTool(FabricToolTags.SHOVELS).breakByTool(FabricToolTags.PICKAXES).sounds(BlockSoundGroup.NETHERRACK));
	public static final BMSpreadableBlock MOSSY_PEAT = new BMSpreadableBlock(settings(Material.SOIL, 0.5F).breakByTool(FabricToolTags.SHOVELS).ticksRandomly().sounds(BlockSoundGroup.WET_GRASS), new Lazy<>(()->PEAT));
	public static final PeatFarmlandBlock PEAT_FARMLAND = new PeatFarmlandBlock(settings(Material.SOIL, 0.5F).breakByHand(true).breakByTool(FabricToolTags.SHOVELS).sounds(BlockSoundGroup.WET_GRASS).ticksRandomly().nonOpaque());
	public static final BMBlock DRIED_PEAT_BRICKS = new BMBlock(settings(Material.STONE, 2).sounds(BlockSoundGroup.STONE).breakByHand(true).breakByTool(FabricToolTags.SHOVELS).breakByTool(FabricToolTags.PICKAXES).requiresTool());
	public static final DecorationBlockInfo DRIED_PEAT_BRICKS_DECORATION = new DecorationBlockInfo("dried_peat_bricks", DRIED_PEAT_BRICKS, settings(Material.STONE, 2F).sounds(BlockSoundGroup.NETHERRACK).breakByHand(true).breakByTool(FabricToolTags.SHOVELS).breakByTool(FabricToolTags.PICKAXES).requiresTool().sounds(BlockSoundGroup.STONE)).all();
	public static final BMBlock MOSSY_DRIED_PEAT_BRICKS = new BMBlock(settings(Material.STONE, 2).sounds(BlockSoundGroup.STONE).breakByHand(true).breakByTool(FabricToolTags.SHOVELS).breakByTool(FabricToolTags.PICKAXES).requiresTool());
	public static final DecorationBlockInfo MOSSY_DRIED_PEAT_BRICKS_DECORATION = new DecorationBlockInfo("mossy_dried_peat_brick", MOSSY_DRIED_PEAT_BRICKS, settings(Material.STONE, 2F).sounds(BlockSoundGroup.NETHERRACK).breakByHand(true).breakByTool(FabricToolTags.SHOVELS).breakByTool(FabricToolTags.PICKAXES).requiresTool().sounds(BlockSoundGroup.STONE)).all();
	public static final BMBlock CRACKED_DRIED_PEAT_BRICKS = new BMBlock(settings(Material.STONE, 2).sounds(BlockSoundGroup.STONE).breakByHand(true).breakByTool(FabricToolTags.SHOVELS).breakByTool(FabricToolTags.PICKAXES).requiresTool());
	public static final DecorationBlockInfo CRACKED_DRIED_PEAT_BRICKS_DECORATION = new DecorationBlockInfo("cracked_dried_peat_brick", CRACKED_DRIED_PEAT_BRICKS, settings(Material.STONE, 2F).sounds(BlockSoundGroup.NETHERRACK).breakByHand(true).breakByTool(FabricToolTags.SHOVELS).breakByTool(FabricToolTags.PICKAXES).requiresTool().sounds(BlockSoundGroup.STONE)).all();
	public static final BMTallFlowerBlock SWAMP_AZALEA = new BMTallFlowerBlock(settings(Material.PLANT, RLayer.CUTOUT, 0).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS));
	public static final BMTallFlowerBlock MARIGOLD = new BMTallFlowerBlock(settings(Material.PLANT, RLayer.CUTOUT, 0).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS));
	public static final BMTallFlowerBlock BLACK_THISTLE = new BlackThistleBlock(settings(Material.PLANT, RLayer.CUTOUT, 0).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS));
	public static final BMTallFlowerBlock FOXGLOVE = new BMTallFlowerBlock(settings(Material.PLANT, RLayer.CUTOUT, 0).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS));

	public static final ReedBlock CATTAIL = new ReedBlock(settings(Material.PLANT, RLayer.CUTOUT, 0).breakInstantly().noCollision().sounds(BlockSoundGroup.GRASS));
	public static final ReedBlock REED = new ReedBlock(settings(Material.PLANT, RLayer.CUTOUT, 0).breakInstantly().noCollision().sounds(BlockSoundGroup.GRASS));
	public static final SmallLilyPadBlock SMALL_LILY_PAD = new SmallLilyPadBlock(settings(Material.PLANT, RLayer.CUTOUT, 0).breakInstantly().sounds(BM_LILY_PAD_SOUNDS));
	public static final WaterLilyPadBlock WATER_LILY = new WaterLilyPadBlock(settings(Material.PLANT, RLayer.CUTOUT, 0).breakInstantly().sounds(BM_LILY_PAD_SOUNDS));
	public static final BMLeavesBlock WILLOW_LEAVES = new BMLeavesBlock(settings(Material.LEAVES, RLayer.CUTOUT_MIPPED, 0.2F).ticksRandomly().sounds(BlockSoundGroup.GRASS).nonOpaque().allowsSpawning(BMBlocks::canSpawnOnLeaves).suffocates((a, b, c)->false).blockVision((a, b, c)->false));
	public static final BMLeavesBlock SWAMP_CYPRESS_LEAVES = new BMLeavesBlock(settings(Material.LEAVES, 0.2F).ticksRandomly().sounds(BlockSoundGroup.GRASS).nonOpaque().allowsSpawning(BMBlocks::canSpawnOnLeaves).suffocates((a, b, c)->false).blockVision((a, b, c)->false));
	public static final LightningBugBottleBlock LIGHTNING_BUG_BOTTLE = new LightningBugBottleBlock(settings(Material.STONE, RLayer.CUTOUT, 0.5F).luminance(15).nonOpaque());

	public static final IlluniteClusterBlock ILLUNITE_CLUSTER = new IlluniteClusterBlock(settings(Material.STONE, RLayer.CUTOUT, 0.5F).sounds(ILLUNITE_SOUNDS).nonOpaque().noCollision().postProcess(BMBlocks::always).emissiveLighting(BMBlocks::always));
	public static final Block ILLUNITE_BLOCK = new BMBlock(settings(Material.STONE, 1.5F).requiresTool().sounds(ILLUNITE_SOUNDS));
	public static final Block MESMERITE = new BMBlock(settings(Material.STONE, 1.5F));
	public static final DecorationBlockInfo MESMERITE_DECORATION = new DecorationBlockInfo("mesmerite", MESMERITE, settings(Material.STONE, 1.5F)).all();
	public static final Block POLISHED_MESMERITE = new BMBlock(settings(Material.STONE, 1.5F));
	public static final DecorationBlockInfo POLISHED_MESMERITE_DECORATION = new DecorationBlockInfo("polished_mesmerite", POLISHED_MESMERITE, settings(Material.STONE, 1.5F)).all();
	public static WoodTypeInfo ANCIENT_OAK_WOOD_INFO = new WoodTypeInfo("ancient_oak", settings(Material.WOOD, 1.5F).sounds(BlockSoundGroup.WOOD)).all();
	public static final BMSaplingBlock ANCIENT_OAK_SAPLING = new BMSaplingBlock(new AncientOakSaplingGenerator(), settings(Material.PLANT, RLayer.CUTOUT, 0).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.GRASS));
	public static final BMLeavesBlock ANCIENT_OAK_LEAVES = new BMLeavesBlock(settings(Material.LEAVES, RLayer.CUTOUT_MIPPED, 0.2F).ticksRandomly().sounds(BlockSoundGroup.GRASS).nonOpaque().allowsSpawning(BMBlocks::canSpawnOnLeaves).suffocates((a, b, c)->false).blockVision((a, b, c)->false));
	public static final AltarBlock ALTAR = new AltarBlock(settings(Material.STONE, RLayer.CUTOUT, 0.5F).luminance((st)->st.get(AltarBlock.ACTIVE) ? 5 : 1).requiresTool().nonOpaque());
	public static final Block CLADDED_STONE = new BMBlock(settings(Material.STONE, 1.5F));

	public static final RootlingCropBlock ROOTLING_CROP = new RootlingCropBlock(settings(Material.PLANT, RLayer.CUTOUT, 0).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.CROP));
	public static final IvyBlock IVY = new IvyBlock(settings(Material.REPLACEABLE_PLANT, RLayer.CUTOUT, 0.15F).noCollision().ticksRandomly().sounds(BlockSoundGroup.VINE));
	public static final IvyBlock ITCHING_IVY = new ItchingIvyBlock(settings(Material.REPLACEABLE_PLANT, RLayer.CUTOUT, 0.15F).velocityMultiplier(0.5F).noCollision().ticksRandomly().sounds(BlockSoundGroup.VINE));
	public static final MothBlossomBlock MOTH_BLOSSOM = new MothBlossomBlock(settings(Material.PLANT, RLayer.CUTOUT, 0.25F).velocityMultiplier(0.5F).noCollision().ticksRandomly().sounds(BlockSoundGroup.VINE));
	public static final BMMushroomPlantBlock WILD_MUSHROOMS = new WildMushroomBlock(settings(Material.PLANT, RLayer.CUTOUT, 0F).noCollision().ticksRandomly().nonOpaque().sounds(BlockSoundGroup.FUNGUS));

	public static final FlowerPotBlock POTTED_MYCELIUM_ROOTS = new FlowerPotBlock(MYCELIUM_ROOTS, settings(Material.SUPPORTED, RLayer.CUTOUT, 0).breakInstantly().nonOpaque().sounds(BlockSoundGroup.NETHER_SPROUTS));
	public static final FlowerPotBlock POTTED_PURPLE_GLOWSHROOM = new FlowerPotBlock(PURPLE_GLOWSHROOM, settings(Material.SUPPORTED, RLayer.CUTOUT, 0).lightLevel(13).breakInstantly().nonOpaque().sounds(BlockSoundGroup.NETHER_SPROUTS));
	public static final FlowerPotBlock POTTED_GREEN_GLOWSHROOM = new FlowerPotBlock(GREEN_GLOWSHROOM, settings(Material.SUPPORTED, RLayer.CUTOUT, 0).lightLevel(13).breakInstantly().nonOpaque().sounds(BlockSoundGroup.NETHER_SPROUTS));
	public static final FlowerPotBlock POTTED_ORANGE_GLOWSHROOM = new FlowerPotBlock(ORANGE_GLOWSHROOM, settings(Material.SUPPORTED, RLayer.CUTOUT,0).lightLevel(13).breakInstantly().nonOpaque().sounds(BlockSoundGroup.NETHER_SPROUTS));
	public static final FlowerPotBlock POTTED_BLIGHTED_BALSA_SAPLING = new FlowerPotBlock(BLIGHTED_BALSA_SAPLING, settings(Material.SUPPORTED, RLayer.CUTOUT,0).breakInstantly().nonOpaque().sounds(BlockSoundGroup.NETHER_SPROUTS));
	public static final FlowerPotBlock POTTED_SAGUARO_CACTUS = new FlowerPotBlock(SAGUARO_CACTUS, settings(Material.SUPPORTED, RLayer.CUTOUT, 0).breakInstantly().nonOpaque().sounds(BlockSoundGroup.WOOL));
	public static final FlowerPotBlock POTTED_BARREL_CACTUS = new FlowerPotBlock(BARREL_CACTUS, settings(Material.SUPPORTED, RLayer.CUTOUT, 0).breakInstantly().nonOpaque().sounds(BlockSoundGroup.WOOL));
	public static final FlowerPotBlock POTTED_FLOWERED_BARREL_CACTUS = new FlowerPotBlock(BARREL_CACTUS_FLOWERED, settings(Material.SUPPORTED, RLayer.CUTOUT, 0).breakInstantly().nonOpaque().sounds(BlockSoundGroup.WOOL));
	public static final FlowerPotBlock POTTED_WILLOW_SAPLING = new FlowerPotBlock(WILLOW_SAPLING, settings(Material.SUPPORTED, RLayer.CUTOUT, 0).breakInstantly().nonOpaque().sounds(BlockSoundGroup.WOOL));
	public static final FlowerPotBlock POTTED_SWAMP_CYPRESS_SAPLING = new FlowerPotBlock(SWAMP_CYPRESS_SAPLING, settings(Material.SUPPORTED, RLayer.CUTOUT, 0).breakInstantly().nonOpaque().sounds(BlockSoundGroup.WOOL));
	public static final FlowerPotBlock POTTED_ANCIENT_OAK_SAPLING = new FlowerPotBlock(ANCIENT_OAK_SAPLING, settings(Material.SUPPORTED, RLayer.CUTOUT, 0).breakInstantly().nonOpaque().sounds(BlockSoundGroup.WOOL));
	public static final FlowerPotBlock POTTED_WILD_MUSHROOMS = new FlowerPotBlock(WILD_MUSHROOMS, settings(Material.SUPPORTED, RLayer.CUTOUT, 0).breakInstantly().nonOpaque().sounds(BlockSoundGroup.WOOL));

	public static final DirectionalDataBlock DIRECTIONAL_DATA = new DirectionalDataBlock(settings(Material.STONE, -1).dropsNothing());

	public static final SignType BLIGHTED_BALSA_ST = SignTypeHelper.register(new SignType("blighted_balsa"));
	public static final SignType WILLOW_ST = SignTypeHelper.register(new SignType("willow"));
	public static final SignType SWAMP_CYPRESS_ST = SignTypeHelper.register(new SignType("swamp_cypress"));
	public static final SignType ANCIENT_OAK_ST = SignTypeHelper.register(new SignType("ancient_oak"));

	public static final Map<DyeColor, Block> DYE_TO_TAPESTRY = Maps.newHashMap();
	public static final List<Block> TAPESTRY_BLOCKS = Lists.newArrayList();
	public static final List<Block> TAPESTRY_WALL_BLOCKS = Lists.newArrayList();
	public static final List<Block> TAPESTRY_FLOOR_BLOCKS = Lists.newArrayList();
	public static Block ADJUDICATOR_TAPESTRY;
	static
	{
		for(DyeColor dyeColor : DyeColor.values())
		{
			TapestryBlock tap = new TapestryBlock(dyeColor, settings(Material.WOOD, 1F).noCollision().sounds(BlockSoundGroup.WOOD));
			DYE_TO_TAPESTRY.put(dyeColor, tap);
			TAPESTRY_BLOCKS.add(tap);
			TAPESTRY_FLOOR_BLOCKS.add(tap);
		}
	}
	public static void init()
	{
		RegistryHelper.register(Registry.BLOCK, Block.class, BMBlocks.class, (reg, bl, id)->
		{

			if(!(bl instanceof BlockWithItem)) return;

			BlockWithItem info = (BlockWithItem) bl;
			if(!info.hasItem()) return;

			info.registerItem(id);
		});

		/* Simple decoration registers */
		//TODO:
		BLIGHTED_BALSA_WOOD_INFO = BLIGHTED_BALSA_WOOD_INFO.boat(()->BoatTypes.BLIGHTED_BALSA).sign(BLIGHTED_BALSA_ST);
		WILLOW_WOOD_INFO = WILLOW_WOOD_INFO.boat(()->BoatTypes.WILLOW).sign(WILLOW_ST);
		SWAMP_CYPRESS_WOOD_INFO = SWAMP_CYPRESS_WOOD_INFO.boat(()->BoatTypes.SWAMP_CYPRESS).sign(SWAMP_CYPRESS_ST);
		ANCIENT_OAK_WOOD_INFO = ANCIENT_OAK_WOOD_INFO.boat(()->BoatTypes.ANCIENT_OAK).sign(ANCIENT_OAK_ST);
		BLIGHTED_BALSA_WOOD_INFO.register();
		WILLOW_WOOD_INFO.register();
		SWAMP_CYPRESS_WOOD_INFO.register();
		ANCIENT_OAK_WOOD_INFO.register();
		RED_MUSHROOM_BRICK_DECORATION.register();
		BROWN_MUSHROOM_BRICK_DECORATION.register();
		PURPLE_GLOWSROOM_BRICK_DECORATION.register();
		GREEN_GLOWSROOM_BRICK_DECORATION.register();
		ORANGE_GLOWSROOM_BRICK_DECORATION.register();
		GLOWSHROOM_STEM_BRICK_DECORATION.register();
		MUSHROOM_STEM_BRICK_DECORATION.register();
		BLIGHTED_COBBLESTONE_DECORATION.register();
		BLIGHTED_STONE_BRICKS_DECORATION.register();
		DRIED_PEAT_BRICKS_DECORATION.register();
		MOSSY_DRIED_PEAT_BRICKS_DECORATION.register();
		CRACKED_DRIED_PEAT_BRICKS_DECORATION.register();
		MESMERITE_DECORATION.register();
		POLISHED_MESMERITE_DECORATION.register();

        /* Terracotta Bricks */
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

		BlockItemPair terracottaBricks = registerBlockAndItem(new BMBlock(settings(Material.STONE, 2F).requiresTool().sounds(BlockSoundGroup.STONE)), BiomeMakeover.ID("terracotta_bricks"));
		DecorationBlockInfo terracottaBrick = new DecorationBlockInfo("terracotta_brick", terracottaBricks.getBlock(), settings(Material.STONE, 2F).requiresTool().sounds(BlockSoundGroup.STONE)).all();
		terracottaBrick.register();

		for(DyeColor dye : DyeColor.values())
		{
			BlockItemPair brick = registerBlockAndItem(new BMBlock(settings(Material.STONE, 2F).materialColor(dye).requiresTool().sounds(BlockSoundGroup.STONE)), BiomeMakeover.ID(dye.getName() + "_terracotta_bricks"));
			DecorationBlockInfo dec = new DecorationBlockInfo(dye.getName() + "_terracotta_brick", brick.getBlock(), settings(Material.STONE, 2F).materialColor(dye).requiresTool().sounds(BlockSoundGroup.STONE)).all();
			dec.register();

			BRICK_TO_TERRACOTTA.put(brick.getBlock(), vanillaTerracotta.get(dye));
		}

		/* Tapestry */
		for(DyeColor dye : DyeColor.values())
		{
			Block tap = DYE_TO_TAPESTRY.get(dye);
			Registry.register(Registry.BLOCK, BiomeMakeover.ID(dye.getName() + "_tapestry"), tap);

			TapestryWallBlock wallBlock = new TapestryWallBlock(dye, settings(Material.WOOD, 1F).noCollision().sounds(BlockSoundGroup.WOOD).dropsLike(tap));
			Registry.register(Registry.BLOCK, BiomeMakeover.ID(dye.getName() + "_wall_tapestry"), wallBlock);

			WallStandingBlockItem blItem = new WallStandingBlockItem(tap, wallBlock, new Item.Settings().maxCount(16).group(BiomeMakeover.GROUP));
			Registry.register(Registry.ITEM, BiomeMakeover.ID(dye.getName() + "_tapestry"), blItem);

			TAPESTRY_BLOCKS.add(wallBlock);
			TAPESTRY_WALL_BLOCKS.add(wallBlock);
		}
		ADJUDICATOR_TAPESTRY = new AdjudicatorTapestryBlock(settings(Material.WOOD, 1F).noCollision().sounds(BlockSoundGroup.WOOD));
		Registry.register(Registry.BLOCK, BiomeMakeover.ID("adjudicator_tapestry"), ADJUDICATOR_TAPESTRY);
		AdjudicatorTapestryWallBlock adjWall = new AdjudicatorTapestryWallBlock(settings(Material.WOOD, 1F).noCollision().sounds(BlockSoundGroup.WOOD).dropsLike(ADJUDICATOR_TAPESTRY));
		Registry.register(Registry.BLOCK, BiomeMakeover.ID("adjudicator_wall_tapestry"), adjWall);
		WallStandingBlockItem adjBLItem = new WallStandingBlockItem(ADJUDICATOR_TAPESTRY, adjWall, new Item.Settings().rarity(Rarity.EPIC).maxCount(16).group(BiomeMakeover.GROUP));
		Registry.register(Registry.ITEM, BiomeMakeover.ID("adjudicator_tapestry"), adjBLItem);
		TAPESTRY_BLOCKS.add(ADJUDICATOR_TAPESTRY);
		TAPESTRY_BLOCKS.add(adjWall);

		/* Flammables */
		registerFlammable(BLIGHTED_BALSA_WOOD_INFO);
		registerFlammable(BLIGHTED_BALSA_LEAVES, 5, 60);
		registerFlammable(WILLOW_WOOD_INFO);
		registerFlammable(WILLOW_LEAVES, 5, 60);
		registerFlammable(SWAMP_CYPRESS_WOOD_INFO);
		registerFlammable(SWAMP_CYPRESS_LEAVES, 5, 60);
		registerFlammable(ANCIENT_OAK_WOOD_INFO);
		registerFlammable(ANCIENT_OAK_LEAVES, 5, 60);

		FuelRegistry.INSTANCE.add(DRIED_PEAT, 10000);
	}

	public static final Map<Block, Block> BRICK_TO_TERRACOTTA = Maps.newHashMap();

	public static BlockItemPair registerBlockAndItem(Block block, Identifier id)
	{
		BlockItem bi = new BlockItem(block, BMItems.settings());
		Registry.register(Registry.BLOCK, id, block);
		Registry.register(Registry.ITEM, id, bi);
		return BlockItemPair.of(block, bi);
	}

	public static FabricBlockSettings settings(Material material, float hardness)
	{
		return FabricBlockSettings.of(material).hardness(hardness).resistance(hardness);
	}

	public static FabricBlockSettings settings(Material material, RLayer layer, float hardness)
	{
		return new BMBlockSettings(material, material.getColor()).layer(layer).hardness(hardness).resistance(hardness);
	}

	public static Boolean canSpawnOnLeaves(BlockState state, BlockView world, BlockPos pos, EntityType<?> type)
	{
		return type == EntityType.OCELOT || type == EntityType.PARROT;
	}

	public static void registerFlammable(Block block, int burnChance, int spreadChance)
	{
		((FireBlockAccessor) Blocks.FIRE).bm_registerFlammable(block, burnChance, spreadChance);
	}

	public static void registerFlammable(WoodTypeInfo info)
	{
		registerFlammable(info.getBlock(WoodTypeInfo.Type.PLANK), 5, 20);
		registerFlammable(info.getBlock(WoodTypeInfo.Type.LOG), 5, 5);
		registerFlammable(info.getBlock(WoodTypeInfo.Type.WOOD), 5, 5);
		registerFlammable(info.getBlock(WoodTypeInfo.Type.STRIPPED_LOG), 5, 5);
		registerFlammable(info.getBlock(WoodTypeInfo.Type.STRIPPED_WOOD), 5, 5);
		registerFlammable(info.getBlock(WoodTypeInfo.Type.FENCE), 5, 20);
		registerFlammable(info.getBlock(WoodTypeInfo.Type.FENCE_GATE), 5, 20);
		registerFlammable(info.getBlock(WoodTypeInfo.Type.STAIR), 5, 20);
		registerFlammable(info.getBlock(WoodTypeInfo.Type.SLAB), 5, 20);
		registerFlammable(WILLOWING_BRANCHES, 15, 100);
		registerFlammable(SWAMP_AZALEA, 60, 100);
		registerFlammable(MARIGOLD, 60, 100);
	}

	public static final Tag<Block> LILY_PADS = TagRegistry.block(BiomeMakeover.ID("lily_pads"));
	public static final Tag<Block> MOTH_ATTRACTIVE = TagRegistry.block(BiomeMakeover.ID("moth_attractive"));
	public static final Tag<Block> IVY_TAG = TagRegistry.block(BiomeMakeover.ID("ivy"));
	public static final Tag<Block> ITCHING_IVY_TAG = TagRegistry.block(BiomeMakeover.ID("itching_ivy"));

	private static boolean always(BlockState state, BlockView world, BlockPos pos)
	{
		return true;
	}
}
