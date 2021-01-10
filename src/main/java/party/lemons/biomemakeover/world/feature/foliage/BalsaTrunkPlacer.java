package party.lemons.biomemakeover.world.feature.foliage;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ModifiableTestableWorld;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.trunk.TrunkPlacer;
import net.minecraft.world.gen.trunk.TrunkPlacerType;
import party.lemons.biomemakeover.init.BMWorldGen;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class BalsaTrunkPlacer extends TrunkPlacer
{
	public BalsaTrunkPlacer(int baseHeight, int firstRandomHeight, int secondRandomHeight)
	{
		super(baseHeight, firstRandomHeight, secondRandomHeight);
	}

	@Override
	protected TrunkPlacerType<?> getType()
	{
		return BMWorldGen.BLIGHTED_BALSA_TRUNK;
	}

	@Override
	public List<FoliagePlacer.TreeNode> generate(ModifiableTestableWorld world, Random random, int trunkHeight, BlockPos pos, Set<BlockPos> set, BlockBox blockBox, TreeFeatureConfig treeFeatureConfig)
	{
		setToDirt(world, pos.down());    //Set dirt

		List<FoliagePlacer.TreeNode> list = Lists.newArrayList();
		Direction direction = Direction.Type.HORIZONTAL.random(random);

		int treeHeight = trunkHeight - random.nextInt(4) - 1;
		int j = 3 - random.nextInt(3);
		BlockPos.Mutable mpos = new BlockPos.Mutable();
		int xx = pos.getX();
		int zz = pos.getZ();
		int yy = 0;

		int yPosition;
		for(int n = 0; n < trunkHeight; ++n)
		{
			yPosition = pos.getY() + n;
			if (n >= treeHeight && j > 0) {
				xx += direction.getOffsetX();
				zz += direction.getOffsetZ();
				--j;
			}

			//Set Tree block?
			if (getAndSetState(world, random, mpos.set(xx, yPosition, zz), set, blockBox, treeFeatureConfig)) {
				yy = yPosition + 1;
			}
		}

		list.add(new FoliagePlacer.TreeNode(new BlockPos(xx, yy, zz), 1, false));

		for(int i =  0; i < 1 + random.nextInt(4); i++)
		{
			xx = pos.getX();
			zz = pos.getZ();
			Direction branchDirection = Direction.Type.HORIZONTAL.random(random);
			if(branchDirection != direction)
			{
				yPosition = Math.max(treeHeight, treeHeight - random.nextInt(1 + Math.abs(treeHeight - 1)));
				int branchSize = 4 + random.nextInt(10);
				yy = 0;

				int s = 0;
				for(int r = yPosition; r < trunkHeight && branchSize > 0; --branchSize)
				{
					if(r >= 1)
					{
						s = pos.getY() + r;
						xx += branchDirection.getOffsetX();
						zz += branchDirection.getOffsetZ();
						if(getAndSetState(world, random, mpos.set(xx, s, zz), set, blockBox, treeFeatureConfig) && random.nextInt(3) == 0)
						{
							yy = s + 1;
						}
					}
					++r;
				}
				list.add(new FoliagePlacer.TreeNode(new BlockPos(xx, s + 1, zz), 0, false));
			}
		}

		return list;
	}

	public static final Codec<BalsaTrunkPlacer> CODEC = RecordCodecBuilder.create((instance) ->method_28904(instance).apply(instance, BalsaTrunkPlacer::new));
}
