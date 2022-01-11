package party.lemons.biomemakeover.block;

import net.minecraft.world.level.block.FenceGateBlock;
import party.lemons.biomemakeover.block.modifier.BlockModifier;
import party.lemons.biomemakeover.block.modifier.BlockWithModifiers;
import party.lemons.biomemakeover.util.registry.BlockWithItem;

public class BMFenceGateBlock extends FenceGateBlock implements BlockWithItem, BlockWithModifiers<BMFenceGateBlock>
{
    public BMFenceGateBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public BMFenceGateBlock modifiers(BlockModifier... modifiers) {
        BlockWithModifiers.init(this, modifiers);
        return this;
    }
}