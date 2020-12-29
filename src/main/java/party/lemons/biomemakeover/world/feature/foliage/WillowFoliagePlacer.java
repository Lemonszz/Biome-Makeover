package party.lemons.biomemakeover.world.feature.foliage;

import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.ModifiableTestableWorld;
import net.minecraft.world.gen.UniformIntDistribution;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.foliage.FoliagePlacer;
import net.minecraft.world.gen.foliage.FoliagePlacerType;
import party.lemons.biomemakeover.block.WillowingBranchesBlock;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMWorldGen;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.Random;
import java.util.Set;

public class WillowFoliagePlacer extends FoliagePlacer
{
	protected final int height;
	protected final boolean doVines;

	public WillowFoliagePlacer(UniformIntDistribution radius, UniformIntDistribution offset, int height, boolean doVines) {
		super(radius, offset);
		this.height = height;
		this.doVines = doVines;
	}

	protected void generate(ModifiableTestableWorld world, Random random, TreeFeatureConfig config, int trunkHeight, FoliagePlacer.TreeNode treeNode, int foliageHeight, int radius, Set<BlockPos> leaves, int offset, BlockBox box) {

		for(int placeOffset = offset; placeOffset >= offset - foliageHeight; --placeOffset)
		{
			int baseHeight;
			if(treeNode.getFoliageRadius() > 0)
				baseHeight =  Math.max(radius + treeNode.getFoliageRadius() - 1 - placeOffset / 2, 0);
			else
				baseHeight = Math.max(radius + treeNode.getFoliageRadius() - placeOffset / 2, 0);

			this.generateSquare(world, random, config, treeNode.getCenter(), baseHeight, leaves, placeOffset, treeNode.isGiantTrunk(), box);
		}

		BlockBox leafBox = BlockBox.empty();
		for(BlockPos leafPos : leaves)
		{
			leafBox.encompass(new BlockBox(leafPos, leafPos));
		}

		for(int i = 0; i < 4 + random.nextInt(8); i++)
		{
			BlockPos.Mutable downPos = new BlockPos.Mutable(RandomUtil.randomRange(box.minX, box.maxX), leafBox.minY - 1, RandomUtil.randomRange(box.minZ, box.maxZ));
			if(TreeFeature.canReplace(world, downPos) && world.testBlockState(downPos.up(), (state)->state.isIn(BlockTags.LEAVES)))
			{
				world.setBlockState(downPos, config.leavesProvider.getBlockState(random, downPos), 19);
				box.encompass(new BlockBox(downPos, downPos));
				leaves.add(downPos.toImmutable());
			}
		}

		if(doVines)
			for(int i = 0; i < 10; i++)
			{
				BlockPos.Mutable pos = new BlockPos.Mutable(RandomUtil.randomRange(box.minX, box.maxX), leafBox.minY, RandomUtil.randomRange(box.minZ, box.maxZ));
				for(int j = 0; j < 3; j++)
				{
					if((world.testBlockState(pos, (s)->s.isAir()) || world.testBlockState(pos, (s)->s == Blocks.WATER.getDefaultState())) && world.testBlockState(pos.up(), (s)->s.isIn(BlockTags.LEAVES) || (s.isOf(BMBlocks.WILLOWING_BRANCHES) && s.get(WillowingBranchesBlock.STAGE) < 2)))
					{
						boolean water = world.testBlockState(pos, (s)->s == Blocks.WATER.getDefaultState());
						if(water || world.testBlockState(pos, (s)->s.isAir()))
						{
							world.setBlockState(pos, BMBlocks.WILLOWING_BRANCHES.getDefaultState().with(WillowingBranchesBlock.STAGE, j).with(Properties.WATERLOGGED, water), 19);
							pos.move(Direction.DOWN);
						}
						else
							break;
					}
					else
					{
						break;
					}
				}
			}

	}

	public int getRandomHeight(Random random, int trunkHeight, TreeFeatureConfig config) {
		return this.height;
	}

	protected boolean isInvalidForLeaves(Random random, int baseHeight, int dx, int dy, int dz, boolean giantTrunk)
	{
		if (baseHeight + dy >= 4) {
			return true;
		} else {
			return baseHeight * baseHeight + dy * dy > dz * dz;
		}
	}

	protected FoliagePlacerType<?> getType() {
		return BMWorldGen.WILLOW_FOLIAGE;
	}

	protected static <P extends WillowFoliagePlacer> Products.P4<RecordCodecBuilder.Mu<P>, UniformIntDistribution, UniformIntDistribution, Integer, Boolean> buildCodec(RecordCodecBuilder.Instance<P> instance)
	{
		return fillFoliagePlacerFields(instance).and(Codec.intRange(0, 16).fieldOf("height").forGetter((cdc) ->cdc.height)).and(Codec.BOOL.fieldOf("doVines").forGetter((cdc)->cdc.doVines));
	}
	public static final Codec<WillowFoliagePlacer> CODEC = RecordCodecBuilder.create((instance) ->buildCodec(instance).apply(instance, WillowFoliagePlacer::new));
}
