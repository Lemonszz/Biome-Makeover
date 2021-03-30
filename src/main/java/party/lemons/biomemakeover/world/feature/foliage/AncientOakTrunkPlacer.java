package party.lemons.biomemakeover.world.feature.foliage;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ModifiableTestableWorld;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;
import party.lemons.biomemakeover.init.BMWorldGen;
import party.lemons.biomemakeover.util.HorizontalDirection;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class AncientOakTrunkPlacer extends TrunkPlacer
{
	public static final Codec<AncientOakTrunkPlacer> CODEC = RecordCodecBuilder.create((instance)->method_28904(instance).apply(instance, AncientOakTrunkPlacer::new));

	public AncientOakTrunkPlacer(int i, int j, int k)
	{
		super(i, j, k);
	}

	protected TrunkPlacerType<?> getType()
	{
		return BMWorldGen.ANCIENT_OAK_TRUNK;
	}

	public List<FoliagePlacer.TreeNode> generate(ModifiableTestableWorld world, Random random, int trunkHeight, BlockPos pos, Set<BlockPos> placedStates, BlockBox box, TreeFeatureConfig config)
	{
		List<FoliagePlacer.TreeNode> list = Lists.newArrayList();
		BlockPos blockPos = pos.down();
		setToDirt(world, blockPos);
		setToDirt(world, blockPos.east());
		setToDirt(world, blockPos.south());
		setToDirt(world, blockPos.south().east());

		int knobSpot = Math.max(5, (trunkHeight / 2) - random.nextInt(4));
		//	int knobCount = 2 - random.nextInt(3);
		int knobCount = 1;
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		int foliageX = x;
		int foliageZ = z;
		int foliageY = y + trunkHeight - 2;

		for(int genY = 0; genY < trunkHeight; ++genY)
		{
			int placeY = y + genY;
			BlockPos placePos = new BlockPos(foliageX, placeY, foliageZ);
			if(TreeFeature.isAirOrLeaves(world, placePos))
			{
				getAndSetState(world, random, placePos, placedStates, box, config);
				getAndSetState(world, random, placePos.east(), placedStates, box, config);
				getAndSetState(world, random, placePos.south(), placedStates, box, config);
				getAndSetState(world, random, placePos.east().south(), placedStates, box, config);
			}
		}

		list.add(new FoliagePlacer.TreeNode(new BlockPos(foliageX, foliageY + 3, foliageZ), 2, true));

		for(int offsetX = -1; offsetX <= 2; ++offsetX)
		{
			for(int offsetZ = -1; offsetZ <= 2; ++offsetZ)
			{
				if((offsetX < 0 || offsetX > 1 || offsetZ < 0 || offsetZ > 1) && random.nextInt(3) <= 0)
				{
					int u = random.nextInt(3) + 2;

					for(int v = 0; v < u; ++v)
					{
						getAndSetState(world, random, new BlockPos(x + offsetX, foliageY - v - 1, z + offsetZ), placedStates, box, config);
					}

					list.add(new FoliagePlacer.TreeNode(new BlockPos(foliageX + offsetX, foliageY, foliageZ + offsetZ), 0, false));
				}
			}
		}

		List<HorizontalDirection> directions = Lists.newArrayList();
		for(int i = 0; i < 1 + random.nextInt(4); i++)
		{
			makeBranch(world, random, list, directions, placedStates, foliageX, y, foliageZ, RandomUtil.randomRange(trunkHeight - 13, trunkHeight - 7), trunkHeight, box, config);
		}

		return list;
	}

	public void makeBranch(ModifiableTestableWorld world, Random random, List<FoliagePlacer.TreeNode> foliage, List<HorizontalDirection> directions, Set<BlockPos> placedStates, int foliageX, int genY, int foliageZ, int branchY, int trunkHeight, BlockBox box, TreeFeatureConfig config)
	{
		HorizontalDirection lastDirection = null;
		for(int i = 0; i < 2; i++)
		{
			HorizontalDirection direction;
			if(lastDirection == null)
			{
				direction = HorizontalDirection.random(random);
				while(directions.contains(direction)) direction = HorizontalDirection.random(random);

				directions.add(direction);
				directions.add(direction.opposite());
				lastDirection = direction;
			}else
			{
				direction = lastDirection.opposite();
				lastDirection = null;
			}
			int genX = foliageX + direction.x;
			int genZ = foliageZ + direction.z;
			int yy;
			int offsetCount = 0;
			for(yy = branchY; yy < trunkHeight - 5 + random.nextInt(4); ++yy)
			{
				int placeGenY = genY + yy;
				BlockPos placePos2 = new BlockPos(genX, placeGenY, genZ);
				if(TreeFeature.isAirOrLeaves(world, placePos2))
				{
					getAndSetState(world, random, placePos2, placedStates, box, config);
					getAndSetState(world, random, placePos2.east(), placedStates, box, config);
					getAndSetState(world, random, placePos2.south(), placedStates, box, config);
					getAndSetState(world, random, placePos2.east().south(), placedStates, box, config);
				}

				if(random.nextBoolean() && offsetCount <= 5)
				{
					offsetCount++;
					if(direction.isStraight)
					{
						genX += direction.x;
						genZ += direction.z;
					}else
					{
						if(yy % 2 == 0)
						{
							genX += direction.x;
						}else
						{
							genZ += direction.z;
						}
					}
				}
			}
			foliage.add(new FoliagePlacer.TreeNode(new BlockPos(genX, genY + yy + 1, genZ), 1, false));
		}
	}
}
