package party.lemons.biomemakeover.block;

import net.minecraft.world.level.block.FenceBlock;
import party.lemons.biomemakeover.block.modifier.BlockModifier;
import party.lemons.biomemakeover.block.modifier.BlockWithModifiers;
import party.lemons.biomemakeover.util.registry.BlockWithItem;

public class BMFenceBlock extends FenceBlock implements BlockWithItem, BlockWithModifiers<BMFenceBlock> {
    public BMFenceBlock(Properties settings) {
        super(settings);
    }

    @Override
    public BMFenceBlock modifiers(BlockModifier... modifiers) {
        BlockWithModifiers.init(this, modifiers);
        return this;
    }
}