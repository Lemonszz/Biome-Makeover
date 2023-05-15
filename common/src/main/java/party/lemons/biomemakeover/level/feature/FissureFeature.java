package party.lemons.biomemakeover.level.feature;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BuddingAmethystBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.Column;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import party.lemons.biomemakeover.init.BMBlocks;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public class FissureFeature extends Feature<FissureFeature.FissureConfig>
{

	public FissureFeature(Codec<FissureConfig> codec)
	{
		super(codec);
	}

	@Override
	public boolean place(FeaturePlaceContext<FissureConfig> ctx)
	{
		RandomSource random = ctx.random();
		WorldGenLevel level = ctx.level();
		BlockPos origin = ctx.origin();
		FissureConfig cfg = ctx.config();


		List<DirPos> offsets = Lists.newArrayList();
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		pos.set(origin);

		DirPos originStart = new DirPos(Direction.from2DDataValue(random.nextInt(4)), pos.immutable(), cfg.baseHeight.sample(random));
		offsets.add(originStart);
		createOffsets(originStart.movedPosition, cfg.count.sample(random), offsets, originStart, random, cfg.heightOffset);
		createOffsets(originStart.movedPosition.getOpposite(), cfg.count.sample(random), offsets, originStart, random, cfg.heightOffset);

		List<DirPos> spreadOffsets = Lists.newArrayList();
		for(int i = 0; i < cfg.spreadOffset.sample(random); i++)
			spreadOffset(offsets, spreadOffsets, random);
		offsets.addAll(spreadOffsets);

		if(offsets.isEmpty())
			return false;

		boolean generated = false;
		Set<BlockPos> alternatePositions = Sets.newHashSet();

		for(DirPos genPos : offsets)
		{
			pos.set(genPos.pos);

			Optional<Column> scan = Column.scan(level, pos, 5, (b)->b.isAir() || b.is(Blocks.WATER), (b)->!b.isAir() && !b.is(Blocks.WATER));
			if(scan.isEmpty() || scan.get().getFloor().isEmpty())
				continue;

			generated = true;
			pos.setY(scan.get().getFloor().getAsInt());

			int height = genPos.height;
			for(int i = 0; i < height; i++)
			{
				BlockState fillState = cfg.fillBlock.getState(random, pos);
				BlockState currentState = level.getBlockState(pos);
				boolean isSource = currentState.getFluidState().isSource();
				if (isSource && fillState.hasProperty(BlockStateProperties.WATERLOGGED)) {
					fillState = fillState.setValue(BlockStateProperties.WATERLOGGED, isSource);
				}
				else if(isSource && fillState.isAir())
					fillState = Blocks.WATER.defaultBlockState();

				level.setBlock(pos, fillState, Block.UPDATE_CLIENTS);
				level.scheduleTick(pos, fillState.getFluidState().getType(), 0);

				float chance = 0.1F + (i / 5F);
				if(chance > 1 || random.nextFloat() < chance)
					setAround(cfg.coreBlock, cfg.depthBlock, cfg.alternateCoreBlock, cfg.alternateChance, alternatePositions, fillState, pos, level, random);
				pos.move(Direction.DOWN);
			}
		}

		List<BlockState> innerStates = cfg.innerPlacements;
		for(BlockPos alternatePos : alternatePositions)
		{
			if(cfg.innerPlacementChance > random.nextFloat())
				continue;
			if(!cfg.target.test(level, alternatePos))
				continue;

			BlockState placeState = Util.getRandom(innerStates, random);


			for(Direction direction2 : Direction.values()) {
				if (placeState.hasProperty(BlockStateProperties.FACING)) {
					placeState = placeState.setValue(BlockStateProperties.FACING, direction2);
				}

				BlockPos offsetPos = alternatePos.relative(direction2);
				BlockState currentState = level.getBlockState(offsetPos);
				if (placeState.hasProperty(BlockStateProperties.WATERLOGGED)) {
					placeState = placeState.setValue(BlockStateProperties.WATERLOGGED, currentState.getFluidState().isSource());
				}

				if (placeState.canSurvive(level, offsetPos) && BuddingAmethystBlock.canClusterGrowAtState(currentState)) {
					level.setBlock(offsetPos, placeState, Block.UPDATE_CLIENTS);
					break;
				}
			}
		}

		return generated;
	}

	public void setAround(BlockStateProvider coreState, BlockStateProvider depthState, BlockStateProvider alternateState, float alternateChance, Set<BlockPos> alternatePositions, BlockState fillState, BlockPos pos, WorldGenLevel level, RandomSource randomSource)
	{
		BlockPos.MutableBlockPos mutPos = new BlockPos.MutableBlockPos();
		for(Direction direction : Direction.values())
		{
			if(direction == Direction.UP)
				continue;

			mutPos.setWithOffset(pos, direction);
			BlockState existingState = level.getBlockState(mutPos);
			if(!existingState.isAir() && !existingState.is(fillState.getBlock()))
			{
				BlockStateProvider stateProvider;
				if(randomSource.nextFloat() < alternateChance) {
					stateProvider = alternateState;
					alternatePositions.add(mutPos.immutable());
				}
				else
					stateProvider = coreState;


				level.setBlock(mutPos, stateProvider.getState(randomSource, mutPos), Block.UPDATE_CLIENTS);

				mutPos.move(direction);
				BlockState current = level.getBlockState(mutPos);
				if(!current.is(BMBlocks.FISSURE_NO_REPLACE))
					level.setBlock(mutPos, depthState.getState(randomSource, mutPos), Block.UPDATE_CLIENTS);
			}
		}
	}

	private void spreadOffset(List<DirPos> offsets, List<DirPos> spreadOffsets, RandomSource random)
	{
		List<DirPos> targetList = spreadOffsets.isEmpty() ? offsets : spreadOffsets;
		List<DirPos> newPostions = Lists.newArrayList();

		for(DirPos pos : targetList)
		{
			//TODO: probably lots of optimisation here, but i wanna get it working first lmao
			DirPos offset1 = createSpreadOffset(pos, pos.movedPosition.getClockWise(), random);
			if(!offsets.contains(offset1) && !spreadOffsets.contains(offset1) && !newPostions.contains(offset1))
				newPostions.add(offset1);

			DirPos offset2 = createSpreadOffset(pos, pos.movedPosition.getCounterClockWise(), random);
			if(!offsets.contains(offset2) && !spreadOffsets.contains(offset2) && !newPostions.contains(offset2))
				newPostions.add(offset2);
		}

		spreadOffsets.addAll(newPostions);
	}

	private DirPos createSpreadOffset(DirPos dirPos, Direction direction, RandomSource randomSource)
	{
		return new DirPos(dirPos.movedPosition, dirPos.pos().relative(direction), Math.max(1, dirPos.height - randomSource.nextInt(2, 5)));
	}

	private void createOffsets(Direction direction, int count, List<DirPos> offsets, DirPos start, RandomSource random, IntProvider heightOffset)
	{
		BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
		pos.set(start.pos.relative(direction));

		DirPos last = start;
		for(int i = 0; i < count; i++)
		{
			DirPos newPos = new DirPos(direction, pos.immutable(), last.height + heightOffset.sample(random));
			last = newPos;
			if(!offsets.contains(newPos))
				offsets.add(newPos);

			if(random.nextInt(5) == 0)
				direction = random.nextBoolean() ? direction.getCounterClockWise() : direction.getClockWise();
			pos.move(direction);
		}
	}

	private record DirPos(Direction movedPosition, BlockPos pos, int height)
	{
		@Override
		public boolean equals(Object o)
		{
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			DirPos dirPos = (DirPos) o;

			return pos.equals(dirPos.pos);
		}

		@Override
		public int hashCode()
		{
			return pos.hashCode();
		}
	}

	public record FissureConfig(IntProvider baseHeight, IntProvider heightOffset, IntProvider spreadOffset, IntProvider count, BlockStateProvider coreBlock, BlockStateProvider depthBlock, BlockStateProvider alternateCoreBlock, float alternateChance, List<BlockState> innerPlacements, float innerPlacementChance, BlockStateProvider fillBlock, BlockPredicate target) implements FeatureConfiguration
	{
		public static final Codec<FissureConfig> CODEC = RecordCodecBuilder.create(instance ->
				instance.group(
								IntProvider.CODEC.fieldOf("height").forGetter(c -> c.baseHeight),
								IntProvider.CODEC.fieldOf("height_offset").forGetter(c -> c.heightOffset),
								IntProvider.CODEC.fieldOf("spread_offset").forGetter(c -> c.spreadOffset),
								IntProvider.CODEC.fieldOf("count").forGetter(c -> c.count),
								BlockStateProvider.CODEC.fieldOf("base_block").forGetter(c -> c.coreBlock),
								BlockStateProvider.CODEC.fieldOf("depth_block").forGetter(c -> c.depthBlock),
								BlockStateProvider.CODEC.fieldOf("alternate_base_block").forGetter(c -> c.alternateCoreBlock),
								Codec.FLOAT.fieldOf("alternate_chance").forGetter(c->c.alternateChance),
								ExtraCodecs.nonEmptyList(BlockState.CODEC.listOf()).fieldOf("inner_placements").forGetter(c -> c.innerPlacements),
								Codec.FLOAT.fieldOf("inner_placement_chance").forGetter(c->c.innerPlacementChance),
								BlockStateProvider.CODEC.fieldOf("fill_block").forGetter(c -> c.fillBlock),
								BlockPredicate.CODEC.fieldOf("inner_target").forGetter(c->c.target)
						)
						.apply(instance, FissureConfig::new));
	}
}