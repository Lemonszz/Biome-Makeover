package party.lemons.biomemakeover.block.modifier;

import dev.architectury.hooks.item.tool.AxeItemHooks;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public record StrippableModifier(
        Supplier<Block> strippedBlock) implements BlockModifier {

    @Override
    public void accept(Block block) {
        AxeItemHooks.addStrippable(block, strippedBlock.get());
    }
}
