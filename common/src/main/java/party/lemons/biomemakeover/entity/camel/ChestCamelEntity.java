package party.lemons.biomemakeover.entity.camel;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ChestCamelEntity extends EquipmentCamelEntity implements InventoryMount
{
    public static final int INV_CHEST_COUNT = 15;
    public ChestCamelEntity(EntityType<? extends Camel> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected int getInventorySize() {
        return super.getInventorySize() + INV_CHEST_COUNT;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        ListTag listTag = new ListTag();
        for(int i = 2; i < this.inventory.getContainerSize(); ++i) {
            ItemStack itemStack = this.inventory.getItem(i);
            if (!itemStack.isEmpty()) {
                CompoundTag compoundTag2 = new CompoundTag();
                compoundTag2.putByte("Slot", (byte)i);
                itemStack.save(compoundTag2);
                listTag.add(compoundTag2);
            }
        }

        compoundTag.put("Items", listTag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.createInventory();
        ListTag listTag = compoundTag.getList("Items", 10);

        for(int i = 0; i < listTag.size(); ++i) {
            CompoundTag compoundTag2 = listTag.getCompound(i);
            int j = compoundTag2.getByte("Slot") & 255;
            if (j >= 2 && j < this.inventory.getContainerSize()) {
                this.inventory.setItem(j, ItemStack.of(compoundTag2));
            }
        }
        this.updateContainerEquipment();
    }
}
