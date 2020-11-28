package party.lemons.biomemakeover.world.feature.config;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.collection.WeightedList;
import net.minecraft.world.gen.UniformIntDistribution;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.stateprovider.WeightedBlockStateProvider;

public class FallenLogFeatureConfig implements FeatureConfig
{
	public static WeightedBlockStateProvider defaultMushrooms()
	{
		WeightedBlockStateProvider states = new WeightedBlockStateProvider();
		states.addState(Blocks.RED_MUSHROOM.getDefaultState(), 10);
		states.addState(Blocks.BROWN_MUSHROOM.getDefaultState(), 10);

		return states;
	}

	public static final Codec<FallenLogFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(
		BlockStateProvider.TYPE_CODEC.fieldOf("log").forGetter((c) -> {
			return c.log;
		}),
		BlockStateProvider.TYPE_CODEC.fieldOf("mushroom").forGetter((c) -> {
			return c.mushroom;
		}),
		UniformIntDistribution.createValidatedCodec(4, 8, 4).fieldOf("length").forGetter((c) -> {
			return c.length;}),
		Codec.INT.fieldOf("mushroomChance").orElse(35).forGetter((x) -> {
			return x.mushroomChance;
		})
		).apply(instance, FallenLogFeatureConfig::new);
	});

	public BlockStateProvider log, mushroom;
	public UniformIntDistribution length;
	public int mushroomChance;

	public FallenLogFeatureConfig(BlockStateProvider log, BlockStateProvider mushrooms, UniformIntDistribution length, int mushroomChance)
	{
		this.length = length;
		this.log = log;
		this.mushroom = mushrooms;
		this.mushroomChance = mushroomChance;
	}
}
