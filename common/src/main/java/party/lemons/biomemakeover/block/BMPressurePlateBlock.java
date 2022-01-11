package party.lemons.biomemakeover.block;

import net.minecraft.world.level.block.PressurePlateBlock;
import party.lemons.biomemakeover.util.registry.BlockWithItem;

public class BMPressurePlateBlock extends PressurePlateBlock implements BlockWithItem
{
    public BMPressurePlateBlock(Sensitivity type, Properties settings)
    {
        super(type, settings);
    }
}