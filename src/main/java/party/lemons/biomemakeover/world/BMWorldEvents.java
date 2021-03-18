package party.lemons.biomemakeover.world;

import net.minecraft.block.*;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.function.MaterialPredicate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.placer.BlockPlacer;
import net.minecraft.world.gen.placer.DoublePlantPlacer;
import net.minecraft.world.gen.placer.SimpleBlockPlacer;
import party.lemons.biomemakeover.block.SmallLilyPadBlock;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.util.access.CarvedPumpkinBlockAccess;

import java.util.Random;

public final class BMWorldEvents
{
	private static BlockPattern stoneGolemDispenserPattern;
	private static BlockPattern stoneGolemPattern;

	public static BlockPattern getStoneGolemDispenserPattern() {
		if (stoneGolemDispenserPattern == null) {
			stoneGolemDispenserPattern = BlockPatternBuilder.start().aisle("~ ~", "###", "~#~").where('#', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(BMBlocks.CLADDED_STONE))).where('~', CachedBlockPosition.matchesBlockState(MaterialPredicate.create(Material.AIR))).build();
		}

		return stoneGolemDispenserPattern;
	}

	public static BlockPattern getStoneGolemPattern() {
		if (stoneGolemPattern == null) {
			stoneGolemPattern = BlockPatternBuilder.start().aisle("~^~", "###", "~#~").where('^', CachedBlockPosition.matchesBlockState(((CarvedPumpkinBlockAccess)Blocks.CARVED_PUMPKIN).isGolemHeadBlock())).where('#', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(BMBlocks.CLADDED_STONE))).where('~', CachedBlockPosition.matchesBlockState(MaterialPredicate.create(Material.AIR))).build();
		}

		return stoneGolemPattern;
	}


	public static void handleSwampBoneMeal(World world, BlockPos pos, Random random)
	{
		start:
		for(int i = 0; i < 128; ++i)
		{
			BlockPos placePos = pos;
			BlockState placeState = Blocks.SEAGRASS.getDefaultState();
			BlockPlacer placer = SimpleBlockPlacer.INSTANCE;
			boolean requireWater = true;

			for(int j = 0; j < i / 16; ++j)
			{
				placePos = placePos.add(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
				if(world.getBlockState(placePos).isFullCube(world, placePos))
				{
					continue start;
				}
			}

			if(world.getBlockState(placePos.up()).isAir() && random.nextInt(4) == 0)
			{
				if(random.nextInt(5) > 0)
				{
					placer = DoublePlantPlacer.INSTANCE;
					placeState = random.nextInt(3) == 0 ? BMBlocks.CATTAIL.getDefaultState() : BMBlocks.REED.getDefaultState();
				}else
				{
					placePos = placePos.up();
					requireWater = false;
					if(random.nextBoolean())
					{
						placeState = BMBlocks.SMALL_LILY_PAD.getDefaultState().with(SmallLilyPadBlock.PADS, random.nextInt(4));
					}else
					{
						if(random.nextInt(4) == 0) placeState = BMBlocks.WATER_LILY.getDefaultState();
						else placeState = Blocks.LILY_PAD.getDefaultState();
					}
				}
			}

			if(placeState.canPlaceAt(world, placePos))
			{
				BlockState currentState = world.getBlockState(placePos);
				if((!requireWater && currentState.isAir()) || (requireWater && currentState.isOf(Blocks.WATER) && world.getFluidState(placePos).getLevel() == 8))
				{
					placer.generate(world, placePos, placeState, random);
				}else if(currentState.isOf(Blocks.SEAGRASS) && random.nextInt(10) == 0)
				{
					((Fertilizable) Blocks.SEAGRASS).grow((ServerWorld) world, random, placePos, currentState);
				}
			}
		}
	}
}
