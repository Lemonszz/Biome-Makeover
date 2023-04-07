package party.lemons.biomemakeover.crafting.witch.data.reward;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;

import java.util.List;

public record RewardTable(RewardTable.Weights weights, List<QuestRewardItem> rewards)
{
	public QuestRewardItem getReward(RandomSource random)
	{
		return rewards.get(random.nextInt(rewards.size()));
	}

	public static final Codec<RewardTable> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					Weights.CODEC.fieldOf("weights").forGetter(i -> i.weights),
					ExtraCodecs.nonEmptyList(QuestRewardItem.CODEC.listOf()).fieldOf("rewards").forGetter(c -> c.rewards)
			).apply(instance, RewardTable::new));

	public record Weights(int common, int uncommon, int rare, int epic)
	{
		public static final Codec<Weights> CODEC = RecordCodecBuilder.create(instance ->
				instance.group(
								Codec.INT.fieldOf("common").forGetter(i -> i.common),
								Codec.INT.fieldOf("uncommon").forGetter(i -> i.uncommon),
								Codec.INT.fieldOf("rare").forGetter(i -> i.rare),
								Codec.INT.fieldOf("epic").forGetter(i -> i.epic)
						)
						.apply(instance, Weights::new));
	}
}
