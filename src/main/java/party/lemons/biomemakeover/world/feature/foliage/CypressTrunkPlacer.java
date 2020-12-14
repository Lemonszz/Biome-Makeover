package party.lemons.biomemakeover.world.feature.foliage;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.ModifiableTestableWorld;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;
import party.lemons.biomemakeover.init.BMWorldGen;
import party.lemons.biomemakeover.util.HorizontalDirection;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class CypressTrunkPlacer extends TrunkPlacer
{
	public CypressTrunkPlacer(int baseHeight, int firstRandomHeight, int secondRandomHeight)
	{
		super(baseHeight, firstRandomHeight, secondRandomHeight);
	}

	@Override
	protected TrunkPlacerType<?> getType()
	{
		return BMWorldGen.CYPRESS_TRUNK;
	}

	@Override
	public List<FoliagePlacer.TreeNode> generate(ModifiableTestableWorld world, Random random, int trunkHeight, BlockPos pos, Set<BlockPos> set, BlockBox blockBox, TreeFeatureConfig treeFeatureConfig)
	{
		//Change ground under to dirt
		method_27400(world, pos.down());

		List<FoliagePlacer.TreeNode> nodes = Lists.newArrayList();
		for(int i = 0; i < trunkHeight; ++i)
		{
			//Place trunk
			method_27402(world, random, pos.up(i), set, blockBox, treeFeatureConfig);
		}
		nodes.add(new FoliagePlacer.TreeNode(pos.up(trunkHeight), 1, true));

		//Nub
		for(int i = 0; i < 2 + random.nextInt(6); i++)
		{
			HorizontalDirection dir = HorizontalDirection.random(random);
			BlockPos offsetStart = dir.offset(pos);
			for(int j = 0; j < 1 + random.nextInt(8); j++)
			{
				method_27402(world, random, offsetStart.up(j), set, blockBox, treeFeatureConfig);
			}
		}

		//Branches
		for(int i = 0; i < 1 + random.nextInt(4); i++)
		{
			int offset = 7 + random.nextInt(trunkHeight - 4);
			Direction dir = Direction.random(random);
			BlockPos trunkPos = pos.offset(dir).up(offset);

			setBranch(world, random, trunkPos, set, blockBox, treeFeatureConfig.trunkProvider.getBlockState(random, trunkPos).with(PillarBlock.AXIS, dir.getAxis()));
			nodes.add(new FoliagePlacer.TreeNode(trunkPos.offset(dir), 1, false));

		}

		return nodes;
	}

	protected static boolean setBranch(ModifiableTestableWorld modifiableTestableWorld, Random random, BlockPos blockPos, Set<BlockPos> set, BlockBox blockBox, BlockState st) {
		if (TreeFeature.canReplace(modifiableTestableWorld, blockPos)) {
			method_27404(modifiableTestableWorld, blockPos,st, blockBox);
			set.add(blockPos.toImmutable());
			return true;
		} else {
			return false;
		}
	}

	public static final Codec<CypressTrunkPlacer> CODEC = RecordCodecBuilder.create((instance) ->method_28904(instance).apply(instance, CypressTrunkPlacer::new));

}
