package party.lemons.biomemakeover.block;

import net.minecraft.world.level.block.LeavesBlock;
import party.lemons.biomemakeover.block.modifier.BlockModifier;
import party.lemons.biomemakeover.block.modifier.BlockWithModifiers;
import party.lemons.biomemakeover.util.registry.BlockWithItem;

public class BMLeavesBlock extends LeavesBlock implements BlockWithItem, BlockWithModifiers<BMLeavesBlock>
{
    public BMLeavesBlock(Properties settings)
    {
        super(settings);
    }

    @Override
    public BMLeavesBlock modifiers(BlockModifier... modifiers) {
        BlockWithModifiers.init(this, modifiers);

        return this;
    }
}