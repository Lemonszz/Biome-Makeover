package party.lemons.biomemakeover.item;

import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class FakeItem extends Item
{
	public FakeItem()
	{
		super(new Settings().maxCount(0));
	}

	@Override
	public void inventoryTick(ItemStack stack, World world, Entity entity, int slot, boolean selected)
	{
		stack.setCount(0);
	}
}
