package party.lemons.biomemakeover.init;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import party.lemons.biomemakeover.block.*;
import party.lemons.biomemakeover.util.BlockWithItem;
import party.lemons.biomemakeover.util.RegistryHelper;

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
	public static final FlowerPotBlock POTTED_MYCELIUM_ROOTS = new FlowerPotBlock(MYCELIUM_ROOTS, settings(Material.SUPPORTED, 0).breakInstantly().nonOpaque().sounds(BlockSoundGroup.NETHER_SPROUTS));
	public static final FlowerPotBlock POTTED_PURPLE_GLOWSHROOM = new FlowerPotBlock(PURPLE_GLOWSHROOM, settings(Material.SUPPORTED, 0).breakInstantly().nonOpaque().sounds(BlockSoundGroup.NETHER_SPROUTS));
	public static final FlowerPotBlock POTTED_GREEN_GLOWSHROOM = new FlowerPotBlock(GREEN_GLOWSHROOM, settings(Material.SUPPORTED, 0).breakInstantly().nonOpaque().sounds(BlockSoundGroup.NETHER_SPROUTS));
	public static final FlowerPotBlock POTTED_ORANGE_GLOWSHROOM = new FlowerPotBlock(ORANGE_GLOWSHROOM, settings(Material.SUPPORTED, 0).breakInstantly().nonOpaque().sounds(BlockSoundGroup.NETHER_SPROUTS));
    public static final BMTallMushroomBlock TALL_BROWN_MUSHROOM = new BMTallMushroomBlock(settings(Material.PLANT, 0).breakInstantly().noCollision().sounds(BlockSoundGroup.FUNGUS));
    public static final BMTallMushroomBlock TALL_RED_MUSHROOM = new BMTallMushroomBlock(settings(Material.PLANT, 0).breakInstantly().noCollision().sounds(BlockSoundGroup.FUNGUS));
    public static final BMMushroomBlock GLOWSHROOM_STEM = new BMMushroomBlock(settings(Material.PLANT, 0.2F).lightLevel(7).sounds(BlockSoundGroup.FUNGUS));

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
    }

    public static FabricBlockSettings settings(Material material, float hardness)
    {
        return FabricBlockSettings.of(material).hardness(hardness).resistance(hardness);
    }
}
