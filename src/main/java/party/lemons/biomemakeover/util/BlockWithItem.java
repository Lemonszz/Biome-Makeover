package party.lemons.biomemakeover.util;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;

public interface BlockWithItem
{
    default boolean hasItem()
    {
        return true;

    }
    default Item.Settings makeItemSettings()
    {
        return new Item.Settings();
    }

    default Item makeItem()
    {
        return new BlockItem((Block)this, makeItemSettings());
    }
}
