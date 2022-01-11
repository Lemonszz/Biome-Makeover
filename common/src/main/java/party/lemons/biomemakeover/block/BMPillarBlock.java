package party.lemons.biomemakeover.block;

import net.minecraft.world.level.block.RotatedPillarBlock;
import party.lemons.biomemakeover.block.modifier.BlockModifier;
import party.lemons.biomemakeover.block.modifier.BlockWithModifiers;
import party.lemons.biomemakeover.util.registry.BlockWithItem;

public class BMPillarBlock extends RotatedPillarBlock implements BlockWithItem, BlockWithModifiers<BMPillarBlock>
{
    public BMPillarBlock(Properties settings)
    {
        super(settings);
    }

    @Override
    public BMPillarBlock modifiers(BlockModifier... modifiers) {
        BlockWithModifiers.init(this, modifiers);
        return this;
    }
}