package party.lemons.biomemakeover.block;

import net.minecraft.world.level.block.Block;
import party.lemons.biomemakeover.block.modifier.BlockModifier;
import party.lemons.biomemakeover.block.modifier.BlockWithModifiers;
import party.lemons.biomemakeover.util.registry.BlockWithItem;

public class BMBlock extends Block implements BlockWithItem, BlockWithModifiers<BMBlock>
{
    public BMBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public BMBlock modifiers(BlockModifier... modifiers) {
        BlockWithModifiers.init(this, modifiers);

        return this;
    }
}