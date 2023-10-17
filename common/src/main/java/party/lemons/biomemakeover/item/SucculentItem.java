package party.lemons.biomemakeover.item;

import net.minecraft.world.item.ItemNameBlockItem;
import party.lemons.biomemakeover.block.SucculentType;
import party.lemons.biomemakeover.init.BMBlocks;

public class SucculentItem extends ItemNameBlockItem {

    public final SucculentType type;

    public SucculentItem(SucculentType type, Properties properties)
    {
        super(BMBlocks.SUCCULENT.get(), properties);
        this.type = type;
    }
}
