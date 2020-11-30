package party.lemons.biomemakeover.world.feature.config;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.Heightmap;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.size.FeatureSize;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import net.minecraft.world.gen.tree.TreeDecorator;
import net.minecraft.world.gen.trunk.TrunkPlacer;

import java.util.List;

public class WaterTreeFeatureConfig extends TreeFeatureConfig
{
	public int maxDepth;

	protected WaterTreeFeatureConfig(BlockStateProvider trunkProvider, BlockStateProvider leavesProvider, FoliagePlacer foliagePlacer, TrunkPlacer trunkPlacer, FeatureSize minimumSize, List<TreeDecorator> decorators, int maxWaterDepth, boolean ignoreVines, Heightmap.Type heightmap, int maxDepth)
	{
		super(trunkProvider, leavesProvider, foliagePlacer, trunkPlacer, minimumSize, decorators, maxWaterDepth, ignoreVines, heightmap);
		this.maxDepth = maxDepth;
	}
	

	public static final Codec<WaterTreeFeatureConfig> CODEC = RecordCodecBuilder.create((instance) -> {
		return instance.group(BlockStateProvider.TYPE_CODEC.fieldOf("trunk_provider").forGetter((treeFeatureConfig) -> {
			return treeFeatureConfig.trunkProvider;
		}), BlockStateProvider.TYPE_CODEC.fieldOf("leaves_provider").forGetter((treeFeatureConfig) -> {
			return treeFeatureConfig.leavesProvider;
		}), FoliagePlacer.TYPE_CODEC.fieldOf("foliage_placer").forGetter((treeFeatureConfig) -> {
			return treeFeatureConfig.foliagePlacer;
		}), TrunkPlacer.CODEC.fieldOf("trunk_placer").forGetter((treeFeatureConfig) -> {
			return treeFeatureConfig.trunkPlacer;
		}), FeatureSize.TYPE_CODEC.fieldOf("minimum_size").forGetter((treeFeatureConfig) -> {
			return treeFeatureConfig.minimumSize;
		}), TreeDecorator.TYPE_CODEC.listOf().fieldOf("decorators").forGetter((treeFeatureConfig) -> {
			return treeFeatureConfig.decorators;
		}), Codec.INT.fieldOf("max_water_depth").orElse(0).forGetter((treeFeatureConfig) -> {
			return treeFeatureConfig.maxWaterDepth;
		}), Codec.BOOL.fieldOf("ignore_vines").orElse(false).forGetter((treeFeatureConfig) -> {
			return treeFeatureConfig.ignoreVines;
		}), Heightmap.Type.CODEC.fieldOf("heightmap").forGetter((treeFeatureConfig) -> {
			return treeFeatureConfig.heightmap;
		}), Codec.INT.fieldOf("max_depth").orElse(0).forGetter((treeFeatureConfig)->{
			return treeFeatureConfig.maxDepth;
		})
		).apply(instance, WaterTreeFeatureConfig::new);
	});
}
