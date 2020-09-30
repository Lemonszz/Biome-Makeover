package party.lemons.biomemakeover.util;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import party.lemons.biomemakeover.BiomeMakeover;

public interface BlockWithItem
{
    default boolean hasItem()
    {
        return true;

    }
    default Item.Settings makeItemSettings()
    {
        return new Item.Settings().group(BiomeMakeover.GROUP);
    }

    default Item makeItem()
    {
        return new BlockItem((Block)this, makeItemSettings());
    }
}
