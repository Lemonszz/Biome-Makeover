package party.lemons.biomemakeover.init;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.registry.Registry;
import party.lemons.biomemakeover.block.BMMushroomPlantBlock;
import party.lemons.biomemakeover.util.BlockWithItem;
import party.lemons.biomemakeover.util.RegistryHelper;

public class BMBlocks
{
    public static final BMMushroomPlantBlock PURPLE_GLOWSHROOM = new BMMushroomPlantBlock(null, settings(Material.PLANT, 0F).lightLevel(13).noCollision().nonOpaque().sounds(BlockSoundGroup.FUNGUS));


    public static void init()
    {
        SnowballEntity
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
