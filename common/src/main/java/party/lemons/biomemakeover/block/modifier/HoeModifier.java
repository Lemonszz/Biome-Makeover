package party.lemons.biomemakeover.block.modifier;

import dev.architectury.hooks.item.tool.HoeItemHooks;
import net.minecraft.world.item.HoeItem;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class HoeModifier implements BlockModifier{

    private final Supplier<Block> tilledBlock;

    public HoeModifier(Supplier<Block> tilledBlock)
    {
        this.tilledBlock = tilledBlock;
    }

    @Override
    public void accept(Block block) {
        HoeItemHooks.addTillable(block, HoeItem::onlyIfAirAbove, HoeItem.changeIntoState(tilledBlock.get().defaultBlockState()), (ctx)->tilledBlock.get().defaultBlockState());
    }
}
