package party.lemons.biomemakeover.mixin;

import net.minecraft.block.*;
import net.minecraft.fluid.FluidState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import party.lemons.biomemakeover.init.BMBlocks;

import java.util.Random;

@Mixin(MyceliumBlock.class)
public abstract class MyceliumBlockMixin extends Block implements Fertilizable
{
	private MyceliumBlockMixin(Settings settings)
	{
		super(settings);
	}

	@Override
	public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient)
	{
		return true;
	}

	@Override
	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state)
	{
		FluidState fs = world.getFluidState(pos.up());
		boolean isWater = fs.isIn(FluidTags.WATER) && fs.getLevel() == 8;

		return world.getBlockState(pos.up()).isAir() || isWater;
	}

	@Override
	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state)
	{
		BlockPos startPos = pos.up();
		BlockState sprouts = BMBlocks.MYCELIUM_SPROUTS.getDefaultState();
		BlockState roots = BMBlocks.MYCELIUM_ROOTS.getDefaultState();
		BlockState purple_shroom = BMBlocks.PURPLE_GLOWSHROOM.getDefaultState();
		BlockState green_shroom = BMBlocks.GREEN_GLOWSHROOM.getDefaultState();
		BlockState orange_shroom = BMBlocks.ORANGE_GLOWSHROOM.getDefaultState().with(Properties.WATERLOGGED, true);
		BlockState red_shroom = Blocks.RED_MUSHROOM.getDefaultState();
		BlockState brown_shroom = Blocks.BROWN_MUSHROOM.getDefaultState();

		next:
		//From vanilla, 128 / 16 = 8 block range
		for(int range = 0; range < 128; ++range)
		{
			BlockPos checkPos = startPos;

			for(int attempts = 0; attempts < range / 16; ++attempts)
			{
				checkPos = checkPos.add(random.nextInt(3) - 1, (random.nextInt(3) - 1) * random.nextInt(3) / 2, random.nextInt(3) - 1);
				if(!world.getBlockState(checkPos.down()).isOf(this) || world.getBlockState(checkPos).isFullCube(world, checkPos))
				{
					continue next;
				}
			}

			boolean placeInWater = false;
			BlockState currentState = world.getBlockState(checkPos);

			FluidState fs = world.getFluidState(checkPos);
			boolean isWater = fs.isIn(FluidTags.WATER) && fs.getLevel() == 8;

			if(currentState.isAir() || isWater)
			{
				BlockState placeState;
				if(random.nextInt(8) == 0)
				{
					if(random.nextInt(20) == 0)
					{
						if(!isWater) placeState = random.nextBoolean() ? purple_shroom : green_shroom;
						else
						{
							placeState = orange_shroom;
							placeInWater = true;
						}
					}else
					{
						placeState = random.nextBoolean() ? red_shroom : brown_shroom;
					}
				}else
				{
					if(random.nextInt(5) == 0)
					{
						placeState = roots;
					}else
					{
						placeState = sprouts;
					}
				}

				if(placeState.canPlaceAt(world, checkPos))
				{
					if(isWater && !placeInWater) continue;

					world.setBlockState(checkPos, placeState, 3);
				}
			}
		}

	}
}
