package party.lemons.biomemakeover.world.feature.foliage;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
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

public class AncientOakTrunkPlacer extends TrunkPlacer
{
	public static final Codec<AncientOakTrunkPlacer> CODEC = RecordCodecBuilder.create((instance) ->method_28904(instance).apply(instance, AncientOakTrunkPlacer::new));

	public AncientOakTrunkPlacer(int i, int j, int k) {
		super(i, j, k);
	}

	protected TrunkPlacerType<?> getType() {
		return BMWorldGen.ANCIENT_OAK_TRUNK;
	}

	public List<FoliagePlacer.TreeNode> generate(ModifiableTestableWorld world, Random random, int trunkHeight, BlockPos pos, Set<BlockPos> placedStates, BlockBox box, TreeFeatureConfig config) {
		List<FoliagePlacer.TreeNode> list = Lists.newArrayList();
		BlockPos blockPos = pos.down();
		setToDirt(world, blockPos);
		setToDirt(world, blockPos.east());
		setToDirt(world, blockPos.south());
		setToDirt(world, blockPos.south().east());

		int knobSpot = trunkHeight - 3 - random.nextInt(4);
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

			if(genY >= knobSpot && knobCount > 0)
			{
				--knobCount;
				HorizontalDirection lastDirection = null;
				for(int i = 0; i < 4 + random.nextInt(3); i++)
				{
					HorizontalDirection direction;
					if(lastDirection == null)
					{
						direction = HorizontalDirection.random(random);
						lastDirection = direction;
					}
					else
					{
						direction = lastDirection.opposite();
						lastDirection = null;
					}
					int genX = foliageX + direction.x;
					int genZ = foliageZ + direction.z;
					int yy;
					for(yy = genY; yy < trunkHeight - 2 +  random.nextInt(4); ++yy)
					{
						int placeGenY = y + yy;
						BlockPos placePos2 = new BlockPos(genX, placeGenY, genZ);
						if(TreeFeature.isAirOrLeaves(world, placePos2))
						{
							getAndSetState(world, random, placePos2, placedStates, box, config);
							getAndSetState(world, random, placePos2.east(), placedStates, box, config);
							getAndSetState(world, random, placePos2.south(), placedStates, box, config);
							getAndSetState(world, random, placePos2.east().south(), placedStates, box, config);
						}

						if(random.nextBoolean())
						{
							genX += direction.x;
							genZ += direction.z;
							if(random.nextInt(4) == 0) yy--;
						}
					}

					list.add(new FoliagePlacer.TreeNode(new BlockPos(genX, y + yy + 1, genZ), 0, false));
				}
			}

		}

		list.add(new FoliagePlacer.TreeNode(new BlockPos(foliageX, foliageY, foliageZ), 0, true));

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

		return list;
	}
}
