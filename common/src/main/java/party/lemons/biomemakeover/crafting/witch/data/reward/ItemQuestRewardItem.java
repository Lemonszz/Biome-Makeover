package party.lemons.biomemakeover.crafting.witch.data.reward;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import party.lemons.biomemakeover.util.RandomUtil;

public class ItemQuestRewardItem extends QuestRewardItem
{
	private final Item item;
	private final int min;
	private final int max;

	public ItemQuestRewardItem(Item item, int min, int max)
	{
		this.item = item;
		this.min = min;
		this.max = max;
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
		stack.setCount(RandomUtil.randomRange(min, max));
		return stack;
	}

	public static final Codec<ItemQuestRewardItem> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(i->i.item),
					Codec.INT.fieldOf("min").forGetter(i->i.min),
					Codec.INT.fieldOf("max").forGetter(i->i.max)
			)
			.apply(instance, ItemQuestRewardItem::new));
}
