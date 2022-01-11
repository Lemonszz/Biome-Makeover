package party.lemons.biomemakeover.block;

import net.minecraft.world.level.block.SlabBlock;
import party.lemons.biomemakeover.block.modifier.BlockModifier;
import party.lemons.biomemakeover.block.modifier.BlockWithModifiers;
import party.lemons.biomemakeover.util.registry.BlockWithItem;

public class BMSlabBlock extends SlabBlock implements BlockWithItem, BlockWithModifiers<BMSlabBlock> {
    public BMSlabBlock(Properties settings) {
        super(settings);
    }

    @Override
    public BMSlabBlock modifiers(BlockModifier... modifiers) {
        BlockWithModifiers.init(this, modifiers);
        return this;
    }
}
