package party.lemons.biomemakeover.block;

import net.minecraft.world.level.block.HugeMushroomBlock;
import party.lemons.biomemakeover.block.modifier.BlockModifier;
import party.lemons.biomemakeover.block.modifier.BlockWithModifiers;
import party.lemons.biomemakeover.util.registry.BlockWithItem;

public class BMMushroomBlock extends HugeMushroomBlock implements BlockWithItem, BlockWithModifiers<BMMushroomBlock>
{
    public BMMushroomBlock(Properties properties)
    {
        super(properties);
    }

    @Override
    public BMMushroomBlock modifiers(BlockModifier... modifiers) {
        BlockWithModifiers.init(this, modifiers);
        return this;
    }
}