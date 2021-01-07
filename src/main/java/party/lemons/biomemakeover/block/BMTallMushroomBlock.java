package party.lemons.biomemakeover.block;

import net.minecraft.block.*;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class BMTallMushroomBlock extends BMTallFlowerBlock
{
	private final Block dropBlock;

	public BMTallMushroomBlock(Block dropBlock, Settings settings)
	{
		super(settings);
		this.dropBlock = dropBlock;
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
	{
		if(dropBlock != null && !world.isClient() && !player.getStackInHand(hand).isEmpty() && player.getStackInHand(hand).getItem() == Items.SHEARS)
		{
			BlockPos dropPos = pos;
			player.playSound(SoundEvents.BLOCK_BEEHIVE_SHEAR, 1F, 1F);
			if(state.get(HALF) == DoubleBlockHalf.LOWER && world.getBlockState(pos.up()).getBlock() == this)
			{
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
				world.setBlockState(pos.up(), Blocks.AIR.getDefaultState(), 35);
				world.syncWorldEvent(player, 2001, pos, Block.getRawIdFromState(state));
				world.syncWorldEvent(player, 2001, pos.up(), Block.getRawIdFromState(state));

				dropPos = pos.up();
			}
			else if(state.get(HALF) == DoubleBlockHalf.UPPER && world.getBlockState(pos.down()).getBlock() == this)
			{
				world.setBlockState(pos.down(), Blocks.AIR.getDefaultState(), 35);
				world.setBlockState(pos, Blocks.AIR.getDefaultState());
				world.syncWorldEvent(player, 2001, pos, Block.getRawIdFromState(state));
				world.syncWorldEvent(player, 2001, pos.down(), Block.getRawIdFromState(state));
			}
			dropStack(world, dropPos, new ItemStack(dropBlock, 1 + world.random.nextInt(2)));
			player.getStackInHand(hand).damage(1, world.random, (ServerPlayerEntity) player);
			return ActionResult.SUCCESS;
		}

		return super.onUse(state, world, pos, player, hand, hit);
	}

	protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
		return floor.isOpaqueFullCube(world, pos);
	}
}
