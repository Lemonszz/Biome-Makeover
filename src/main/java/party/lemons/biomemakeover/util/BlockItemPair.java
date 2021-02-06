package party.lemons.biomemakeover.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class BlockItemPair
{
	private final Block block;
	private final Item item;

	public static BlockItemPair of(Block block, Item item)
	{
		return new BlockItemPair(block, item);
	}

	public static BlockItemPair of(Item item, Block block)
	{
		return of(block, item);
	}

	public BlockItemPair(Block block, Item item)
	{
		this.block = block;
		this.item = item;
	}

	public Block getBlock()
	{
		return block;
	}

	public Item getItem()
	{
		return item;
	}
}
