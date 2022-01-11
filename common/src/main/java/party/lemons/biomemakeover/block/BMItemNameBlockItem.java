package party.lemons.biomemakeover.block;

import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.level.block.Block;
import party.lemons.biomemakeover.item.modifier.ItemModifier;
import party.lemons.biomemakeover.item.modifier.ItemWithModifiers;

public class BMItemNameBlockItem extends ItemNameBlockItem implements ItemWithModifiers<BMItemNameBlockItem> {
    public BMItemNameBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public Block getBlock() {
        return super.getBlock();
    }

    @Override
    public BMItemNameBlockItem modifiers(ItemModifier... modifiers) {
        ItemWithModifiers.init(this, modifiers);
        return this;
    }
}
