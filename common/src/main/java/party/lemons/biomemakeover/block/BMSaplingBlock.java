package party.lemons.biomemakeover.block;

import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import party.lemons.biomemakeover.block.modifier.BlockModifier;
import party.lemons.biomemakeover.block.modifier.BlockWithModifiers;
import party.lemons.biomemakeover.util.registry.BlockWithItem;

public class BMSaplingBlock extends SaplingBlock implements BlockWithItem, BlockWithModifiers<BMSaplingBlock> {
    public BMSaplingBlock(AbstractTreeGrower abstractTreeGrower, Properties settings) {
        super(abstractTreeGrower, settings);
    }

    @Override
    public BMSaplingBlock modifiers(BlockModifier... modifiers) {
        BlockWithModifiers.init(this, modifiers);

        return this;
    }
}