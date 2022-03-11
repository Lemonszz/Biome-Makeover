package party.lemons.biomemakeover.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public record BlockItemPair(Supplier<Block> block, Supplier<Item> item)
{
    public static BlockItemPair of(Supplier<Block> block, Supplier<Item> item) {
        return new BlockItemPair(block, item);
    }

    public Supplier<Block> getBlock() {
        return block;
    }

    public Supplier<Item> getItem() {
        return item;
    }
}