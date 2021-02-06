package party.lemons.biomemakeover.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.*;
import net.minecraft.util.collection.DefaultedList;

import java.util.Map;

public class BlockStateItem extends BlockItem
{
	private final BlockState state;
	private final String translationPostfix;

	public BlockStateItem(BlockState block, String translationPostfix, Settings settings)
	{
		super(block.getBlock(), settings);

		this.state = block;
		this.translationPostfix = translationPostfix;
	}

	@Override
	protected BlockState getPlacementState(ItemPlacementContext context)
	{
		BlockState blockState = state;
		return this.canPlace(context, blockState) ? blockState : null;
	}

	@Override
	public String getTranslationKey()
	{
		return super.getTranslationKey() + "_" + translationPostfix;
	}

	@Override
	public void appendStacks(ItemGroup group, DefaultedList<ItemStack> stacks)
	{
		if(this.isIn(group))
		{
			stacks.add(new ItemStack(this));
		}
	}

	@Override
	public void appendBlocks(Map<Block, Item> map, Item item)
	{

	}
}
