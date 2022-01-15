package party.lemons.biomemakeover.util;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public record BlockItemPair(Block block, Item item) {
    public static BlockItemPair of(Block block, Item item) {
        return new BlockItemPair(block, item);
    }

    public static BlockItemPair of(Item item, Block block) {
        return of(block, item);
    }

    public Block getBlock() {
        return block;
    }

    public Item getItem() {
        return item;
    }
}