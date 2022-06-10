package party.lemons.biomemakeover.crafting.witch;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.RandomSource;
import party.lemons.biomemakeover.crafting.witch.data.QuestCategories;

import java.util.ArrayList;

public class WitchQuestList extends ArrayList<WitchQuest>
{
    public WitchQuestList()
    {
    }

    public WitchQuestList(CompoundTag tags)
    {
        ListTag questsTag = tags.getList("Quests", Tag.TAG_COMPOUND);
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

    public void populate(RandomSource random)
    {
        clear();
        if(!QuestCategories.hasQuests())
            return;

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