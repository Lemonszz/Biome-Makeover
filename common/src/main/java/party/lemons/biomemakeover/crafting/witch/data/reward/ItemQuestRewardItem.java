package party.lemons.biomemakeover.crafting.witch.data.reward;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import party.lemons.biomemakeover.util.RandomUtil;

public class ItemQuestRewardItem extends QuestRewardItem
{
	private final Item item;
	private final CompoundTag tag;
	private final int min;
	private final int max;

	public ItemQuestRewardItem(Item item, CompoundTag tag, int min, int max)
	{
		this.item = item;
		this.min = min;
		this.max = max;
		this.tag = tag;
	}

	@Override
	RewardItemType<?> type()
	{
		return QuestRewardItem.ITEM.get();
	}

	@Override
	public ItemStack getReward(RandomSource randomSource)
	{
		ItemStack stack = new ItemStack(item);
		if(max > 1)
			stack.setCount(RandomUtil.randomRange(min, max));
		if(!tag.isEmpty())
			stack.setTag(tag.copy());
		return stack;
	}

	public static final Codec<ItemQuestRewardItem> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(i->i.item),
					CompoundTag.CODEC.optionalFieldOf("tag", new CompoundTag()).forGetter(i->i.tag),
					Codec.INT.fieldOf("min").forGetter(i->i.min),
					Codec.INT.fieldOf("max").forGetter(i->i.max)
			)
			.apply(instance, ItemQuestRewardItem::new));
}
