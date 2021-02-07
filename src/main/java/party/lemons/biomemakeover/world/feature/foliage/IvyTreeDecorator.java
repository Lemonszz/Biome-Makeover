package party.lemons.biomemakeover.world.feature.foliage;

import com.mojang.serialization.Codec;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.ModifiableWorld;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.tree.TreeDecorator;
import net.minecraft.world.gen.tree.TreeDecoratorType;
import party.lemons.biomemakeover.block.IvyBlock;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMWorldGen;

import java.util.List;
import java.util.Random;
import java.util.Set;

public class IvyTreeDecorator extends TreeDecorator
{
	public static final IvyTreeDecorator INSTANCE = new IvyTreeDecorator();
	public static final Codec<IvyTreeDecorator> CODEC = Codec.unit(() ->INSTANCE);

	@Override
	protected TreeDecoratorType<?> getType()
	{
		return BMWorldGen.IVY_DECORATOR;
	}

	public void generate(StructureWorldAccess world, Random random, List<BlockPos> logPositions, List<BlockPos> leavesPositions, Set<BlockPos> placedStates, BlockBox box) {
		logPositions.forEach((pos) ->
		{
			BlockPos blockPos4;
			if (random.nextInt(8) == 0) {
				blockPos4 = pos.west();
				if (Feature.isAir(world, blockPos4)) {
					this.placeIvy(world, blockPos4, Direction.EAST, placedStates, box);
				}
			}

			if (random.nextInt(8) == 0) {
				blockPos4 = pos.east();
				if (Feature.isAir(world, blockPos4)) {
					this.placeIvy(world, blockPos4, Direction.WEST, placedStates, box);
				}
			}

			if (random.nextInt(8) == 0) {
				blockPos4 = pos.north();
				if (Feature.isAir(world, blockPos4)) {
					this.placeIvy(world, blockPos4, Direction.SOUTH, placedStates, box);
				}
			}

			if (random.nextInt(8) == 0) {
				blockPos4 = pos.south();
				if (Feature.isAir(world, blockPos4)) {
					this.placeIvy(world, blockPos4, Direction.NORTH, placedStates, box);
				}
			}

		});
	}

	protected void placeIvy(ModifiableWorld world, BlockPos pos, Direction dir, Set<BlockPos> placedStates, BlockBox box)
	{
		this.setBlockStateAndEncompassPosition(world, pos, BMBlocks.IVY.getDefaultState().with(IvyBlock.getPropertyForDirection(dir), true), placedStates, box);
	}
}
