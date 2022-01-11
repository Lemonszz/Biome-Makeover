package party.lemons.biomemakeover.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class FakeItem extends Item
{
    public FakeItem()
    {
        super(new Properties().stacksTo(0));
    }

    @Override
    public void inventoryTick(ItemStack stack, Level world, Entity entity, int slot, boolean selected)
    {
        stack.setCount(0);
    }
}