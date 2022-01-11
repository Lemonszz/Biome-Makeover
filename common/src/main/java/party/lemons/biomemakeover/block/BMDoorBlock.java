package party.lemons.biomemakeover.block;

import net.minecraft.world.level.block.DoorBlock;
import party.lemons.biomemakeover.block.modifier.BlockModifier;
import party.lemons.biomemakeover.block.modifier.BlockWithModifiers;
import party.lemons.biomemakeover.util.registry.BlockWithItem;

public class BMDoorBlock extends DoorBlock implements BlockWithItem, BlockWithModifiers<BMDoorBlock>
{
    public BMDoorBlock(Properties settings)
    {
        super(settings);
    }

    @Override
    public BMDoorBlock modifiers(BlockModifier... modifiers) {
        BlockWithModifiers.init(this, modifiers);

        return this;
    }
}