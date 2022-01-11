package party.lemons.biomemakeover.block;

import net.minecraft.world.level.block.TallFlowerBlock;
import party.lemons.biomemakeover.block.modifier.BlockModifier;
import party.lemons.biomemakeover.block.modifier.BlockWithModifiers;
import party.lemons.biomemakeover.util.registry.BlockWithItem;

public class BMTallFlowerBlock extends TallFlowerBlock implements BlockWithItem, BlockWithModifiers<BMTallFlowerBlock>
{
    public BMTallFlowerBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public BMTallFlowerBlock modifiers(BlockModifier... modifiers) {
        BlockWithModifiers.init(this, modifiers);

        return this;
    }
}