package party.lemons.biomemakeover.crafting;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import party.lemons.biomemakeover.block.blockentity.AltarBlockEntity;
import party.lemons.biomemakeover.init.BMItems;
import party.lemons.biomemakeover.init.BMScreens;

public class AltarMenu extends AbstractContainerMenu
{
    private final Container inventory;
    private final ContainerData containerData;

    public AltarMenu(int containerID, Inventory playerInventory) {
        this(containerID, playerInventory, new SimpleContainer(2), new SimpleContainerData(1));
    }

    public AltarMenu(int containerID, Inventory playerInventory, Container inventory, ContainerData data) {
        super(BMScreens.ALTAR, containerID);

        checkContainerSize(inventory, 2);
        checkContainerDataCount(data, 1);
        this.addDataSlots(data);
        this.containerData = data;

        this.inventory = inventory;
        inventory.startOpen(playerInventory.player);


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

    public int getProgress()
    {
        return containerData.get(0);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack stackCopy = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if(slot != null && slot.hasItem())
        {
            ItemStack moveStack = slot.getItem();
            stackCopy = moveStack.copy();
            if(index == 0)
            {
                if(!this.moveItemStackTo(moveStack, 2, 38, true)) return ItemStack.EMPTY;
            }else if(index == 1)
            {
                if(!this.moveItemStackTo(moveStack, 2, 38, true)) return ItemStack.EMPTY;
            }else if(moveStack.is(BMItems.CURSE_FUEL))
            {
                if(!this.moveItemStackTo(moveStack, 1, 2, true)) return ItemStack.EMPTY;
            }else
            {
                if(this.slots.get(0).hasItem() || !this.slots.get(0).mayPlace(moveStack)) return ItemStack.EMPTY;

                ItemStack finalStack = moveStack.copy();
                finalStack.setCount(1);
                moveStack.shrink(1);
                this.slots.get(0).set(finalStack);
            }

            if(moveStack.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();

            if(moveStack.getCount() == stackCopy.getCount())
            {
                return ItemStack.EMPTY;
            }
            slot.onTake(player, moveStack);
        }

        return stackCopy;
    }

    @Override
    public boolean stillValid(Player player) {
        return this.inventory.stillValid(player);
    }

    private static class CurseToolSlot extends Slot
    {
        public CurseToolSlot(Container inventory, int index, int x, int y)
        {
            super(inventory, index, x, y);
        }

        @Override
        public boolean mayPlace(ItemStack stack)
        {
            return AltarBlockEntity.isValidForCurse(stack);
        }

        @Override
        public int getMaxStackSize() {
            return 1;
        }
    }

    private static class CurseFuelSlot extends Slot {
        public CurseFuelSlot(Container container, int i, int j, int k) {
            super(container, i, j, k);
        }

        @Override
        public boolean mayPlace(ItemStack itemStack) {
            return itemStack.is(BMItems.CURSE_FUEL);
        }
    }
}
