package party.lemons.biomemakeover.init;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.BlockView;
import org.lwjgl.system.CallbackI;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.block.*;
import party.lemons.biomemakeover.util.*;
import party.lemons.biomemakeover.util.access.FireBlockAccessor;
import party.lemons.biomemakeover.world.feature.foliage.BalsaSaplingGenerator;

public class BMBlocks
{
    public static final BMMushroomPlantBlock PURPLE_GLOWSHROOM = new GlowshroomPlantBlock(()->BMWorldGen.UNDERGROUND_HUGE_PURPLE_GLOWSHROOM_FEATURE_CONFIGURED, settings(Material.PLANT, 0F).lightLevel(13).noCollision().nonOpaque().sounds(BlockSoundGroup.FUNGUS));
    public static final BMMushroomPlantBlock GREEN_GLOWSHROOM = new GlowshroomPlantBlock(()->BMWorldGen.UNDERGROUND_HUGE_GREEN_GLOWSHROOM_FEATURE_CONFIGURED, settings(Material.PLANT, 0F).lightLevel(13).noCollision().nonOpaque().sounds(BlockSoundGroup.FUNGUS));
    public static final UnderwaterMushroomPlantBlock ORANGE_GLOWSHROOM = new UnderwaterMushroomPlantBlock(()->BMWorldGen.HUGE_ORANGE_GLOWSHROOM_FEATURE_CONFIGURED, settings(Material.PLANT, 0F).lightLevel(13).noCollision().nonOpaque().sounds(BlockSoundGroup.FUNGUS));

    public static final BMMushroomBlock PURPLE_GLOWSHROOM_BLOCK = new BMMushroomBlock(settings(Material.PLANT, 0.2F).lightLevel(15).sounds(BlockSoundGroup.FUNGUS));
    public static final BMMushroomBlock GREEN_GLOWSHROOM_BLOCK = new BMMushroomBlock(settings(Material.PLANT, 0.2F).lightLevel(15).sounds(BlockSoundGroup.FUNGUS));
    public static final BMMushroomBlock ORANGE_GLOWSHROOM_BLOCK = new BMMushroomBlock(settings(Material.PLANT, 0.2F).lightLevel(15).sounds(BlockSoundGroup.FUNGUS));

    public static final MushroomSproutsBlock MYCELIUM_SPROUTS = new MushroomSproutsBlock(settings(Material.field_26708, 0).noCollision().nonOpaque().breakInstantly().sounds(BlockSoundGroup.NETHER_SPROUTS));
	public static final MushroomRootsBlock MYCELIUM_ROOTS = new MushroomRootsBlock(settings(Material.field_26708, 0).noCollision().nonOpaque().breakInstantly().sounds(BlockSoundGroup.ROOTS));

	public static final BMTallMushroomBlock TALL_BROWN_MUSHROOM = new BMTallMushroomBlock(Blocks.BROWN_MUSHROOM, settings(Material.PLANT, 0).breakInstantly().noCollision().sounds(BlockSoundGroup.FUNGUS));
    public static final BMTallMushroomBlock TALL_RED_MUSHROOM = new BMTallMushroomBlock(Blocks.RED_MUSHROOM, settings(Material.PLANT, 0).breakInstantly().noCollision().sounds(BlockSoundGroup.FUNGUS));

    public static final WoodTypeInfo BLIGHTED_BALSA_WOOD_INFO = new WoodTypeInfo("blighted_balsa", settings(Material.WOOD, 1.5F).sounds(BlockSoundGroup.WOOD)).all();
	public static final BMLeavesBlock BLIGHTED_BALSA_LEAVES = new BMLeavesBlock(settings(Material.LEAVES, 0.2F).ticksRandomly().sounds(BlockSoundGroup.GRASS).nonOpaque().allowsSpawning(BMBlocks::canSpawnOnLeaves).suffocates((a,b,c)->false).blockVision((a,b,c)->false));
	public static final BMSaplingBlock BLIGHTED_BALSA_SAPLING = new BMSaplingBlock(new BalsaSaplingGenerator(), settings(Material.PLANT, 0).noCollision().ticksRandomly().breakInstantly().sounds(BlockSoundGroup.GRASS));

    public static final BMMushroomBlock GLOWSHROOM_STEM = new BMMushroomBlock(settings(Material.PLANT, 0.2F).lightLevel(7).sounds(BlockSoundGroup.FUNGUS));
    public static final BMBlock RED_MUSHROOM_BRICK = new BMBlock(settings(Material.PLANT, 0.8F).sounds(BlockSoundGroup.FUNGUS));
	public static final DecorationBlockInfo RED_MUSHROOM_BRICK_DECORATION = new DecorationBlockInfo("red_mushroom_brick", RED_MUSHROOM_BRICK, settings(Material.PLANT, 0.8F).sounds(BlockSoundGroup.FUNGUS)).all();
	public static final BMBlock BROWN_MUSHROOM_BRICK = new BMBlock(settings(Material.PLANT, 0.8F).sounds(BlockSoundGroup.FUNGUS));
	public static final DecorationBlockInfo BROWN_MUSHROOM_BRICK_DECORATION = new DecorationBlockInfo("brown_mushroom_brick", BROWN_MUSHROOM_BRICK, settings(Material.PLANT, 0.8F).sounds(BlockSoundGroup.FUNGUS)).all();
	public static final BMBlock PURPLE_GLOWSHROOM_BRICK = new BMBlock(settings(Material.PLANT, 0.8F).lightLevel(13).sounds(BlockSoundGroup.FUNGUS));
	public static final DecorationBlockInfo PURPLE_GLOWSROOM_BRICK_DECORATION = new DecorationBlockInfo("purple_glowshroom_brick", PURPLE_GLOWSHROOM_BRICK, settings(Material.PLANT, 0.8F).lightLevel(13).sounds(BlockSoundGroup.FUNGUS)).all();
	public static final BMBlock GREEN_GLOWSHROOM_BRICK = new BMBlock(settings(Material.PLANT, 0.8F).lightLevel(13).sounds(BlockSoundGroup.FUNGUS));
	public static final DecorationBlockInfo GREEN_GLOWSROOM_BRICK_DECORATION = new DecorationBlockInfo("green_glowshroom_brick", GREEN_GLOWSHROOM_BRICK, settings(Material.PLANT, 0.8F).lightLevel(13).sounds(BlockSoundGroup.FUNGUS)).all();
	public static final BMBlock ORANGE_GLOWSHROOM_BRICK = new BMBlock(settings(Material.PLANT, 0.8F).lightLevel(13).sounds(BlockSoundGroup.FUNGUS));
	public static final DecorationBlockInfo ORANGE_GLOWSROOM_BRICK_DECORATION = new DecorationBlockInfo("orange_glowshroom_brick", ORANGE_GLOWSHROOM_BRICK, settings(Material.PLANT, 0.8F).lightLevel(13).sounds(BlockSoundGroup.FUNGUS)).all();
	public static final BMBlock GLOWSHROOM_STEM_BRICK = new BMBlock(settings(Material.PLANT, 0.8F).lightLevel(7).sounds(BlockSoundGroup.FUNGUS));
	public static final DecorationBlockInfo GLOWSHROOM_STEM_BRICK_DECORATION = new DecorationBlockInfo("glowshroom_stem_brick", GLOWSHROOM_STEM_BRICK, settings(Material.PLANT, 0.8F).lightLevel(7).sounds(BlockSoundGroup.FUNGUS)).all();
	public static final BMBlock MUSHROOM_STEM_BRICK = new BMBlock(settings(Material.PLANT, 0.8F).sounds(BlockSoundGroup.FUNGUS));
	public static final DecorationBlockInfo MUSHROOM_STEM_BRICK_DECORATION = new DecorationBlockInfo("mushroom_stem_brick", MUSHROOM_STEM_BRICK, settings(Material.PLANT, 0.8F).sounds(BlockSoundGroup.FUNGUS)).all();
	public static final BMBlock BLIGHTED_COBBLESTONE = new BMBlock(settings(Material.STONE, 2).sounds(BlockSoundGroup.STONE).requiresTool());
	public static final DecorationBlockInfo BLIGHTED_COBBLESTONE_DECORATION = new DecorationBlockInfo("blighted_cobblestone", BLIGHTED_COBBLESTONE, settings(Material.STONE, 2F).requiresTool().sounds(BlockSoundGroup.STONE)).all();
	public static final BMBlock BLIGHTED_STONE_BRICKS = new BMBlock(settings(Material.STONE, 2).sounds(BlockSoundGroup.STONE).requiresTool());
	public static final DecorationBlockInfo BLIGHTED_STONE_BRICKS_DECORATION = new DecorationBlockInfo("blighted_stone_bricks", BLIGHTED_STONE_BRICKS, settings(Material.STONE, 2F).requiresTool().sounds(BlockSoundGroup.STONE)).all();

	public static final BMBlock TUMBLEWEED = new BMBlock(settings(Material.PLANT, 0));

	public static final SaguaroCactusBlock SAGUARO_CACTUS = new SaguaroCactusBlock(settings(Material.CACTUS, 0.4F).sounds(BlockSoundGroup.WOOL).ticksRandomly());
	public static final FlowerPotBlock POTTED_MYCELIUM_ROOTS = new FlowerPotBlock(MYCELIUM_ROOTS, settings(Material.SUPPORTED, 0).breakInstantly().nonOpaque().sounds(BlockSoundGroup.NETHER_SPROUTS));
	public static final FlowerPotBlock POTTED_PURPLE_GLOWSHROOM = new FlowerPotBlock(PURPLE_GLOWSHROOM, settings(Material.SUPPORTED, 0).lightLevel(13).breakInstantly().nonOpaque().sounds(BlockSoundGroup.NETHER_SPROUTS));
	public static final FlowerPotBlock POTTED_GREEN_GLOWSHROOM = new FlowerPotBlock(GREEN_GLOWSHROOM, settings(Material.SUPPORTED, 0).lightLevel(13).breakInstantly().nonOpaque().sounds(BlockSoundGroup.NETHER_SPROUTS));
	public static final FlowerPotBlock POTTED_ORANGE_GLOWSHROOM = new FlowerPotBlock(ORANGE_GLOWSHROOM, settings(Material.SUPPORTED, 0).lightLevel(13).breakInstantly().nonOpaque().sounds(BlockSoundGroup.NETHER_SPROUTS));
	public static final FlowerPotBlock POTTED_BLIGHTED_BALSA_SAPLING = new FlowerPotBlock(BLIGHTED_BALSA_SAPLING, settings(Material.SUPPORTED, 0).breakInstantly().nonOpaque().sounds(BlockSoundGroup.NETHER_SPROUTS));


	public static void init()
    {
        RegistryHelper.register(Registry.BLOCK, Block.class, BMBlocks.class, (reg, bl, id)->{

            if(!(bl instanceof BlockWithItem))
                return;

            BlockWithItem info = (BlockWithItem)bl;
            if(!info.hasItem())
                return;

            Registry.register(Registry.ITEM, id, info.makeItem());
        });

        /* Simple decoration registers */
        BLIGHTED_BALSA_WOOD_INFO.register();
        RED_MUSHROOM_BRICK_DECORATION.register();
        BROWN_MUSHROOM_BRICK_DECORATION.register();
        PURPLE_GLOWSROOM_BRICK_DECORATION.register();
        GREEN_GLOWSROOM_BRICK_DECORATION.register();
        ORANGE_GLOWSROOM_BRICK_DECORATION.register();
        GLOWSHROOM_STEM_BRICK_DECORATION.register();
        MUSHROOM_STEM_BRICK_DECORATION.register();
        BLIGHTED_COBBLESTONE_DECORATION.register();
        BLIGHTED_STONE_BRICKS_DECORATION.register();

        /* Terracotta Bricks */
        BlockItemPair terracottaBricks = registerBlockAndItem(new BMBlock(settings(Material.STONE, 2F).requiresTool().sounds(BlockSoundGroup.STONE)), BiomeMakeover.ID("terracotta_bricks"));
	    DecorationBlockInfo terracottaBrick = new DecorationBlockInfo("terracotta_brick", terracottaBricks.getBlock(), settings(Material.STONE, 2F).requiresTool().sounds(BlockSoundGroup.STONE)).all();
	    terracottaBrick.register();

        for(DyeColor dye : DyeColor.values())
        {
        	BlockItemPair brick = registerBlockAndItem(new BMBlock(settings(Material.STONE, 2F).materialColor(dye).requiresTool().sounds(BlockSoundGroup.STONE)), BiomeMakeover.ID(dye.getName() + "_terracotta_bricks"));
	        DecorationBlockInfo dec = new DecorationBlockInfo(dye.getName() + "_terracotta_brick", brick.getBlock(), settings(Material.STONE, 2F).materialColor(dye).requiresTool().sounds(BlockSoundGroup.STONE)).all();
            dec.register();
        }

        /* Flammables */
	    registerFlammable(BLIGHTED_BALSA_WOOD_INFO.get(WoodTypeInfo.Type.PLANK), 5, 20);
	    registerFlammable(BLIGHTED_BALSA_WOOD_INFO.get(WoodTypeInfo.Type.LOG), 5, 5);
	    registerFlammable(BLIGHTED_BALSA_WOOD_INFO.get(WoodTypeInfo.Type.WOOD), 5, 5);
	    registerFlammable(BLIGHTED_BALSA_WOOD_INFO.get(WoodTypeInfo.Type.STRIPPED_LOG), 5, 5);
	    registerFlammable(BLIGHTED_BALSA_WOOD_INFO.get(WoodTypeInfo.Type.STRIPPED_WOOD), 5, 5);
	    registerFlammable(BLIGHTED_BALSA_WOOD_INFO.get(WoodTypeInfo.Type.FENCE), 5, 20);
	    registerFlammable(BLIGHTED_BALSA_WOOD_INFO.get(WoodTypeInfo.Type.FENCE_GATE), 5, 20);
	    registerFlammable(BLIGHTED_BALSA_WOOD_INFO.get(WoodTypeInfo.Type.STAIR), 5, 20);
	    registerFlammable(BLIGHTED_BALSA_WOOD_INFO.get(WoodTypeInfo.Type.SLAB), 5, 20);
        registerFlammable(BLIGHTED_BALSA_LEAVES, 5, 60);
    }

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

	public static Boolean canSpawnOnLeaves(BlockState state, BlockView world, BlockPos pos, EntityType<?> type) {
		return type == EntityType.OCELOT || type == EntityType.PARROT;
	}

	public static void registerFlammable(Block block, int burnChance, int spreadChance)
	{
		((FireBlockAccessor)Blocks.FIRE).registerFlammable(block, burnChance, spreadChance);
	}
}
