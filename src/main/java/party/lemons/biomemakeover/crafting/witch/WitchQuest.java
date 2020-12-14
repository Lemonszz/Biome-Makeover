package party.lemons.biomemakeover.crafting.witch;

import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.village.TradeOffer;

import java.util.List;
import java.util.Random;

import static party.lemons.biomemakeover.crafting.witch.WitchQuestHandler.*;

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
			requiredItems[i] = ItemStack.fromTag(items.getCompound(i));
	}

	public WitchQuest(PacketByteBuf buffer)
	{
		rarityPoints = buffer.readFloat();
		int length = buffer.readInt();
		requiredItems = new ItemStack[length];

		for(int i = 0; i < length; i++)
		{
			requiredItems[i] = buffer.readItemStack();
		}
	}

	public ItemStack[] getRequiredItems()
	{
		return requiredItems;
	}

	public boolean hasItems(Inventory inventory)
	{
		int size = getRequiredItems().length;
		ItemStack[] stacks = new ItemStack[size];
		for(int i = 0; i < size; i++)
		{
			ItemStack st = getRequiredItems()[i];
			int count = inventory.count(st.getItem());

			if(count < st.getCooldown())
				return false;
		}
		return true;
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
			items.add(requiredItems[i].toTag(new CompoundTag()));
		}
		return tag;
	}

	public void toPacket(PacketByteBuf buffer) {
		buffer.writeFloat(rarityPoints);

		int length = this.requiredItems.length;
		buffer.writeByte((byte)(length & 255));

		for(int i = 0; i < length; i++)
		{
			buffer.writeItemStack(requiredItems[i]);
		}
	}
}
