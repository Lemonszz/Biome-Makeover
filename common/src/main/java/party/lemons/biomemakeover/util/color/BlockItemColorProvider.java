package party.lemons.biomemakeover.util.color;

import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class BlockItemColorProvider implements ItemColor {
    @Override
    public int getColor(ItemStack stack, int tintIndex) {
        BlockState blockState = ((BlockItem) stack.getItem()).getBlock().defaultBlockState();
        return Minecraft.getInstance().getBlockColors().getColor(blockState.getBlock().defaultBlockState(), null, null, tintIndex);
    }
}
