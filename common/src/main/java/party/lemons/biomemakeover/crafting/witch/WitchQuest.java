package party.lemons.biomemakeover.crafting.witch;

import dev.architectury.utils.NbtType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import java.util.List;
import java.util.Random;

public class WitchQuest
{
    private final ItemStack[] requiredItems;
    private float rarityPoints;

    public WitchQuest(Random random, List<QuestItem> items)
    {
        requiredItems = new ItemStack[items.size()];
        for(int i = 0; i < items.size(); i++)
        {
            QuestItem qi = items.get(i);
            rarityPoints += qi.getPoints();
            requiredItems[i] = qi.createStack(random);
        }
    }

    public WitchQuest(CompoundTag tag)
    {
        rarityPoints = tag.getFloat("Points");
        ListTag items = tag.getList("Items", NbtType.COMPOUND);

        requiredItems = new ItemStack[items.size()];
        for(int i = 0; i < items.size(); i++)
            requiredItems[i] = ItemStack.of(items.getCompound(i));
    }

    public WitchQuest(FriendlyByteBuf buffer)
    {
        rarityPoints = buffer.readFloat();
        int length = buffer.readByte() & 255;
        requiredItems = new ItemStack[length];

        for(int i = 0; i < length; i++)
        {
            requiredItems[i] = buffer.readItem();
        }
    }

    public ItemStack[] getRequiredItems()
    {
        return requiredItems;
    }

    public boolean hasItems(Container inventory)
    {
        int size = getRequiredItems().length;
        for(int i = 0; i < size; i++)
        {
            ItemStack st = getRequiredItems()[i];
            int count = inventory.countItem(st.getItem());

            if(count < st.getCount()) return false;
        }
        return true;
    }

    //Assumes inventory has items!!
    public void consumeItems(Inventory inventory)
    {
        for(int i = 0; i < inventory.getContainerSize(); i++)
        {
            ItemStack invStack = inventory.getItem(i);
            if(invStack.isEmpty()) continue;

            for(ItemStack stack : getRequiredItems())
            {
                if(stack.isEmpty()) continue;

                if(stack.getItem() == invStack.getItem())
                {
                    if(stack.getCount() <= invStack.getCount())
                    {
                        invStack.shrink(stack.getCount());
                        stack.setCount(0);
                    }else
                    {
                        stack.shrink(invStack.getCount());
                        invStack.setCount(0);
                    }
                }
            }
        }
    }

    public float getPoints()
    {
        return rarityPoints;
    }

    @Override
    public String toString()
    {
        String s = "WQ | ";
        for(ItemStack st : requiredItems)
        {
            s += st.toString();
        }
        s += " | " + QuestRarity.getRarityFromPoints(rarityPoints);
        return s;
    }

    public CompoundTag toTag()
    {
        CompoundTag tag = new CompoundTag();
        tag.putFloat("Points", rarityPoints);

        ListTag items = new ListTag();
        for(int i = 0; i < requiredItems.length; i++)
        {
            items.add(requiredItems[i].save(new CompoundTag()));
        }
        tag.put("Items", items);
        return tag;
    }

    public void toPacket(FriendlyByteBuf buffer)
    {
        buffer.writeFloat(rarityPoints);

        int length = this.requiredItems.length;
        buffer.writeByte((byte) (length & 255));

        for(int i = 0; i < length; i++)
        {
            buffer.writeItem(requiredItems[i]);
        }
    }

}