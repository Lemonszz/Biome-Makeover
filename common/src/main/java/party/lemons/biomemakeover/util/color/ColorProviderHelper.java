package party.lemons.biomemakeover.util.color;

import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.world.level.block.Block;

public final class ColorProviderHelper
{
    public static void registerSimpleBlockWithItem(BlockColor colorProvider, Block... blocks)
    {
        ColorHandlerRegistry.registerBlockColors(colorProvider, blocks);
        ColorHandlerRegistry.registerItemColors(new BlockItemColorProvider(), blocks);
    }

    private ColorProviderHelper()
    {
    }
}
