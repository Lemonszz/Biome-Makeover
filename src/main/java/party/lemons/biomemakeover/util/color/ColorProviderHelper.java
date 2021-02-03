package party.lemons.biomemakeover.util.color;

import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.color.block.BlockColorProvider;

public final class ColorProviderHelper
{
	public static void registerSimpleBlockWithItem(BlockColorProvider colorProvider, Block... blocks)
	{
		ColorProviderRegistry.BLOCK.register(colorProvider, blocks);
		ColorProviderRegistry.ITEM.register(new BlockItemColorProvider(), blocks);
	}

	private ColorProviderHelper(){}
}
