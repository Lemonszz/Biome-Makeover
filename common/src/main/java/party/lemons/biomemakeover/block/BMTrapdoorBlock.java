package party.lemons.biomemakeover.block;

import net.minecraft.world.level.block.TrapDoorBlock;
import party.lemons.biomemakeover.block.modifier.BlockModifier;
import party.lemons.biomemakeover.block.modifier.BlockWithModifiers;
import party.lemons.biomemakeover.util.registry.BlockWithItem;

public class BMTrapdoorBlock extends TrapDoorBlock implements BlockWithItem, BlockWithModifiers<BMTrapdoorBlock>
{
    public BMTrapdoorBlock(Properties settings)
    {
        super(settings);
    }

    @Override
    public BMTrapdoorBlock modifiers(BlockModifier... modifiers) {
        BlockWithModifiers.init(this, modifiers);

        return this;
    }
}