package party.lemons.biomemakeover.util.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.init.BMItems;

public interface BlockWithItem
{
    default boolean hasItem()
    {
        return true;
    }

    default Item.Properties makeItemSettings(CreativeModeTab group)
    {
        return new Item.Properties().tab(group);
    }

    default Item makeItem(CreativeModeTab group)
    {
        return new BlockItem((Block) this, makeItemSettings(group));
    }

    default Item makeItem()
    {
        return new BlockItem((Block) this, makeItemSettings(BiomeMakeover.TAB));
    }

    default void registerItem(ResourceLocation id, CreativeModeTab group)
    {
        RegistryHelper.registerObject(Registry.ITEM, id, makeItem(group));
    }
}