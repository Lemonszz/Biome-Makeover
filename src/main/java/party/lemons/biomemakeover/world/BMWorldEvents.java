package party.lemons.biomemakeover.world;

import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.placer.BlockPlacer;
import net.minecraft.world.gen.placer.DoublePlantPlacer;
import net.minecraft.world.gen.placer.SimpleBlockPlacer;
import party.lemons.biomemakeover.block.SmallLilyPadBlock;
import party.lemons.biomemakeover.init.BMBlocks;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public final class BMWorldEvents
{
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
				if (world.getBlockState(placePos).isFullCube(world, placePos)) {
					continue start;
				}
			}

			if(world.getBlockState(placePos.up()).isAir() && random.nextInt(4) == 0)
			{
				if(random.nextInt(5) > 0)
				{
					placer = DoublePlantPlacer.INSTANCE;
					placeState = random.nextInt(3) == 0  ? BMBlocks.CATTAIL.getDefaultState() : BMBlocks.REED.getDefaultState();
				}
				else
				{
					placePos = placePos.up();
					requireWater = false;
					if(random.nextBoolean())
					{
						placeState = BMBlocks.SMALL_LILY_PAD.getDefaultState().with(SmallLilyPadBlock.PADS, random.nextInt(4));
					}
					else
					{
						if(random.nextInt(4) == 0)
							placeState = BMBlocks.WATER_LILY.getDefaultState();
						else
							placeState = Blocks.LILY_PAD.getDefaultState();
					}
				}
			}

			if (placeState.canPlaceAt(world, placePos))
			{
				BlockState currentState = world.getBlockState(placePos);
				if ((!requireWater && currentState.isAir()) || (requireWater && currentState.isOf(Blocks.WATER) && world.getFluidState(placePos).getLevel() == 8))
				{
					placer.generate(world, placePos, placeState, random);
				}
				else if (currentState.isOf(Blocks.SEAGRASS) && random.nextInt(10) == 0)
				{
					((Fertilizable)Blocks.SEAGRASS).grow((ServerWorld)world, random, placePos, currentState);
				}
			}
		}
	}
}
