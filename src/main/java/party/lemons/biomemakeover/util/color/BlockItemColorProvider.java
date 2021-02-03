package party.lemons.biomemakeover.util.color;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.client.color.item.ItemColorProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

public class BlockItemColorProvider implements ItemColorProvider
{
	@Override
	public int getColor(ItemStack stack, int tintIndex)
	{
		BlockState blockState = ((BlockItem)stack.getItem()).getBlock().getDefaultState();
		return ColorProviderRegistry.BLOCK.get(blockState.getBlock()).getColor(blockState, null, null, tintIndex);
	}
}
