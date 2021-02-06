package party.lemons.biomemakeover.world.feature.foliage;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.PillarBlock;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ModifiableTestableWorld;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer.TreeNode;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;
import party.lemons.biomemakeover.init.BMWorldGen;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class WillowTrunkPlacer extends TrunkPlacer
{
	private static final Direction[] dirs = new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST,};

	public WillowTrunkPlacer(int base, int first, int second)
	{
		super(base, first, second);
	}

	@Override
	protected TrunkPlacerType<?> getType()
	{
		return BMWorldGen.WILLOW_TRUNK;
	}

	@Override
	public List<TreeNode> generate(ModifiableTestableWorld world, Random random, int trunkHeight, BlockPos pos, Set<BlockPos> set, BlockBox blockBox, TreeFeatureConfig treeFeatureConfig)
	{
		//Change ground under to dirt
		setToDirt(world, pos.down());

		List<TreeNode> nodes = Lists.newArrayList();
		for(int i = 0; i < trunkHeight; ++i)
		{
			//Place trunk
			getAndSetState(world, random, pos.up(i), set, blockBox, treeFeatureConfig);
		}
		nodes.add(new TreeNode(pos.up(trunkHeight), 1, false));

		BlockPos start = pos.up(RandomUtil.randomRange(trunkHeight / 2, Math.min(trunkHeight, (trunkHeight / 2) + 2)));
		for(Direction dir : dirs)
		{
			int length = 1 + random.nextInt(4);
			BlockPos.Mutable lP = new BlockPos.Mutable(start.offset(dir).getX(), start.getY(), start.offset(dir).getZ());

			for(int i = 0; i < length; i++)
			{
				BlockState st = treeFeatureConfig.trunkProvider.getBlockState(random, lP).with(PillarBlock.AXIS, dir.getAxis());
				setBranch(world, random, lP, set, blockBox, st);

				if(i != length - 1)
				{
					nodes.add(new TreeNode(lP, -1, false));
					lP.move(dir);
					if(random.nextBoolean())
						lP.move(random.nextBoolean() ? dir.rotateYClockwise() : dir.rotateYCounterclockwise());

					if(random.nextInt(2) == 0) lP.move(Direction.UP);
				}else nodes.add(new TreeNode(lP, 0, false));

			}
		}
		return nodes;
	}

	protected static boolean setBranch(ModifiableTestableWorld modifiableTestableWorld, Random random, BlockPos blockPos, Set<BlockPos> set, BlockBox blockBox, BlockState st)
	{
		if(TreeFeature.canReplace(modifiableTestableWorld, blockPos))
		{
			setBlockState(modifiableTestableWorld, blockPos, st, blockBox);
			set.add(blockPos.toImmutable());
			return true;
		}else
		{
			return false;
		}
	}

	public static final Codec<WillowTrunkPlacer> CODEC = RecordCodecBuilder.create((instance)->method_28904(instance).apply(instance, WillowTrunkPlacer::new));

}
