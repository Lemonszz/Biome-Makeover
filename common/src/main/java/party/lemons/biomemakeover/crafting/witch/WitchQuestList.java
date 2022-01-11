package party.lemons.biomemakeover.crafting.witch;

import dev.architectury.utils.NbtType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.ArrayList;
import java.util.Random;

public class WitchQuestList extends ArrayList<WitchQuest>
{
    public WitchQuestList()
    {
    }

    public WitchQuestList(CompoundTag tags)
    {
        ListTag questsTag = tags.getList("Quests", NbtType.COMPOUND);
        for(int i = 0; i < questsTag.size(); i++)
            add(new WitchQuest(questsTag.getCompound(i)));
    }

    public WitchQuestList(FriendlyByteBuf buffer)
    {
        int size = buffer.readByte() & 255;
        for(int i = 0; i < size; i++)
        {
            add(new WitchQuest(buffer));
        }
    }

    public void populate(Random random)
    {
        clear();
        for(int i = 0; i < 3; i++)
        {
            add(WitchQuestHandler.createQuest(random));
        }
    }

    public void toPacket(FriendlyByteBuf buffer)
    {
        buffer.writeByte((byte) (this.size() & 255));
        for(int i = 0; i < this.size(); i++)
        {
            get(i).toPacket(buffer);
        }
    }

    public CompoundTag toTag()
    {
        CompoundTag compoundTag = new CompoundTag();
        ListTag listTag = new ListTag();

        for(int i = 0; i < this.size(); i++)
        {
            WitchQuest quest = get(i);
            listTag.add(quest.toTag());
        }

        compoundTag.put("Quests", listTag);
        return compoundTag;
    }
}