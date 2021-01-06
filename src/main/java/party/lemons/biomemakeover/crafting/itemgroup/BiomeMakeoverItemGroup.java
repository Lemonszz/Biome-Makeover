package party.lemons.biomemakeover.crafting.itemgroup;

import net.minecraft.block.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMItems;

import java.util.List;

public class BiomeMakeoverItemGroup extends TabbedItemGroup
{
	public BiomeMakeoverItemGroup(Identifier id)
	{
		super(id);
	}

	@Override
	public void initTabs(List<ItemTab> tabs)
	{
		tabs.add(new ItemTab(new ItemStack(BMBlocks.TALL_RED_MUSHROOM), "mushroom_fields", BMItems.MUSHROOM_FIELDS));
		tabs.add(new ItemTab(new ItemStack(BMBlocks.SAGUARO_CACTUS), "badlands", BMItems.BADLANDS));
		tabs.add(new ItemTab(new ItemStack(BMBlocks.WATER_LILY), "swamp", BMItems.SWAMP));
	}

	@Override
	public ItemStack createIcon()
	{
		return new ItemStack(BMItems.ICON_ITEM);
	}
}