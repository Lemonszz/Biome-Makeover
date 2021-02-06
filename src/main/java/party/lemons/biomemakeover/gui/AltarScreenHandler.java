package party.lemons.biomemakeover.gui;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ArrayPropertyDelegate;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.Slot;
import party.lemons.biomemakeover.block.blockentity.AltarBlockEntity;
import party.lemons.biomemakeover.init.BMItems;
import party.lemons.biomemakeover.init.BMScreens;

public class AltarScreenHandler extends ScreenHandler
{
	private final Inventory inventory;
	private final PropertyDelegate properties;

	public AltarScreenHandler(int syncId, PlayerInventory playerInventory)
	{
		this(syncId, playerInventory, new SimpleInventory(2), new ArrayPropertyDelegate(1));
	}

	public AltarScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate properties)
	{
		super(BMScreens.ALTAR, syncId);
		checkSize(inventory, 2);
		checkDataCount(properties, 1);
		this.addProperties(properties);
		this.properties = properties;

		this.inventory = inventory;
		inventory.onOpen(playerInventory.player);


		this.addSlot(new CurseToolSlot(inventory, 0, 80, 15));
		this.addSlot(new CurseFuelSlot(inventory, 1, 80, 54));

		//Player Inventory
		int x, y;
		for(y = 0; y < 3; ++y)
		{
			for(x = 0; x < 9; ++x)
			{
				this.addSlot(new Slot(playerInventory, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
			}
		}
		for(y = 0; y < 9; ++y)
		{
			this.addSlot(new Slot(playerInventory, y, 8 + y * 18, 142));
		}
	}

	@Override
	public boolean canUse(PlayerEntity player)
	{
		return this.inventory.canPlayerUse(player);
	}

	@Override
	public ItemStack transferSlot(PlayerEntity player, int index)
	{
		ItemStack stackCopy = ItemStack.EMPTY;
		Slot slot = this.slots.get(index);
		if(slot != null && slot.hasStack())
		{
			ItemStack moveStack = slot.getStack();
			stackCopy = moveStack.copy();
			if(index == 0)
			{
				if(!this.insertItem(moveStack, 2, 38, true)) return ItemStack.EMPTY;
			}else if(index == 1)
			{
				if(!this.insertItem(moveStack, 2, 38, true)) return ItemStack.EMPTY;
			}else if(moveStack.getItem().isIn(BMItems.CURSE_FUEL))
			{
				if(!this.insertItem(moveStack, 1, 2, true)) return ItemStack.EMPTY;
			}else
			{
				if(this.slots.get(0).hasStack() || !this.slots.get(0).canInsert(moveStack)) return ItemStack.EMPTY;

				ItemStack finalStack = moveStack.copy();
				finalStack.setCount(1);
				moveStack.decrement(1);
				this.slots.get(0).setStack(finalStack);
			}

			if(moveStack.isEmpty()) slot.setStack(ItemStack.EMPTY);
			else slot.markDirty();

			if(moveStack.getCount() == stackCopy.getCount())
			{
				return ItemStack.EMPTY;
			}
			slot.onTakeItem(player, moveStack);
		}

		return stackCopy;
	}

	public int getProgress()
	{
		return properties.get(0);
	}

	private static class CurseToolSlot extends Slot
	{
		public CurseToolSlot(Inventory inventory, int index, int x, int y)
		{
			super(inventory, index, x, y);
		}

		@Override
		public boolean canInsert(ItemStack stack)
		{
			return AltarBlockEntity.isValidForCurse(stack);
		}

		@Override
		public int getMaxItemCount()
		{
			return 1;
		}
	}

	private static class CurseFuelSlot extends Slot
	{
		public CurseFuelSlot(Inventory inventory, int index, int x, int y)
		{
			super(inventory, index, x, y);
		}

		@Override
		public boolean canInsert(ItemStack stack)
		{
			return stack.getItem().isIn(BMItems.CURSE_FUEL);
		}
	}
}
