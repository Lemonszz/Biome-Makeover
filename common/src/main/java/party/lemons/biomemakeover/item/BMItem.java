package party.lemons.biomemakeover.item;

import net.minecraft.world.item.Item;
import party.lemons.biomemakeover.item.modifier.ItemModifier;
import party.lemons.biomemakeover.item.modifier.ItemWithModifiers;

public class BMItem extends Item implements ItemWithModifiers<BMItem>
{
    public BMItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public BMItem modifiers(ItemModifier... modifiers) {
        ItemWithModifiers.init(this, modifiers);
        return this;
    }
}