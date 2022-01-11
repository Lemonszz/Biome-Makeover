package party.lemons.biomemakeover.item.modifier;

import net.minecraft.world.item.Item;
import party.lemons.biomemakeover.init.BMItems;

import java.util.Arrays;

public interface ItemWithModifiers<T extends Item>
{
    static <A extends Item> void init(ItemWithModifiers<A> item, ItemModifier... modifiers)
    {
        item.registerModifiers((A)item, modifiers);
    }

    T modifiers(ItemModifier... modifiers);

    default void registerModifiers(T item, ItemModifier... modifiers)
    {
        BMItems.MODIFIERS.putAll(item, Arrays.stream(modifiers).toList());
    }
}
