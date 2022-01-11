package party.lemons.biomemakeover.block.modifier;

import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.minecraft.world.level.block.Block;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.util.registry.RType;

import java.util.Arrays;

public interface BlockWithModifiers<T extends Block>
{
    static <A extends Block> void init(BlockWithModifiers<A> block, BlockModifier... modifiers)
    {
        block.registerModifiers((A)block, modifiers);
    }

    T modifiers(BlockModifier... modifiers);

    default void registerModifiers(T block, BlockModifier... modifiers)
    {
        BMBlocks.MODIFIERS.putAll(block, Arrays.stream(modifiers).toList());
    }
}
