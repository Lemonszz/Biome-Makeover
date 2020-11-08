package party.lemons.biomemakeover.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ComposterBlock;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class EctoplasmComposterBlock extends ComposterBlock
{
	public EctoplasmComposterBlock(Settings settings)
	{
		super(settings);
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		int currentLevel = state.get(LEVEL);
		ItemStack itemStack = player.getStackInHand(hand);

		if (currentLevel < 8 && ITEM_TO_LEVEL_INCREASE_CHANCE.containsKey(itemStack.getItem()))
		{
			if (currentLevel < 7 && !world.isClient) {
				BlockState blockState = addToComposter(state, world, pos, itemStack);
				world.syncWorldEvent(1500, pos, state != blockState ? 1 : 0);
				if (!player.abilities.creativeMode) {
					itemStack.decrement(1);
				}
			}
			return ActionResult.success(world.isClient);
		} else if (currentLevel == 8) {
			emptyFullComposter(world, pos);
			return ActionResult.success(world.isClient);
		} else {
			return ActionResult.PASS;
		}
	}

	@Override
	public SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
		int currentLevel = state.get(LEVEL);
		if (currentLevel == 8) {
			return new FullComposterInventory(world, pos, new ItemStack(Blocks.SOUL_SOIL));
		} else {
			return super.getInventory(state, world, pos);
		}
	}

	public static void emptyFullComposter(World world, BlockPos pos) {
		if (!world.isClient) {
			float offset = 0.7F;
			double offsetX = (double)(world.random.nextFloat() * offset) + 0.15D;
			double offsetY = (double)(world.random.nextFloat() * offset) + 0.06D + 0.6D;
			double offsetZ = (double)(world.random.nextFloat() * offset) + 0.15D;
			ItemEntity itemEntity = new ItemEntity(world, (double)pos.getX() + offsetX, (double)pos.getY() + offsetY, (double)pos.getZ() + offsetZ, new ItemStack(Blocks.SOUL_SOIL));
			itemEntity.setToDefaultPickupDelay();
			world.spawnEntity(itemEntity);
		}

		BlockState originalState = Blocks.COMPOSTER.getDefaultState();
		world.setBlockState(pos, originalState);
		world.playSound(null, pos, SoundEvents.BLOCK_COMPOSTER_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
	}

	private static BlockState addToComposter(BlockState state, WorldAccess world, BlockPos pos, ItemStack item)
	{
		int currentLevel = state.get(LEVEL);
		float increaseChance = ITEM_TO_LEVEL_INCREASE_CHANCE.getFloat(item.getItem());
		if ((currentLevel != 0 || increaseChance <= 0.0F) && world.getRandom().nextDouble() >= (double)increaseChance)
		{
			return state;
		}
		else {
			int nextLevel = currentLevel + 1;
			BlockState blockState = state.with(LEVEL, nextLevel);
			world.setBlockState(pos, blockState, 3);
			if (nextLevel == 7)
				world.getBlockTickScheduler().schedule(pos, state.getBlock(), 20);

			return blockState;
		}
	}

	static class FullComposterInventory extends SimpleInventory implements SidedInventory {
		private final WorldAccess world;
		private final BlockPos pos;
		private boolean dirty;

		public FullComposterInventory(WorldAccess world, BlockPos pos, ItemStack outputItem) {
			super(outputItem);
			this.world = world;
			this.pos = pos;
		}

		@Override
		public int getMaxCountPerStack() {
			return 1;
		}

		public int[] getAvailableSlots(Direction side) {
			return side == Direction.DOWN ? new int[]{0} : new int[0];
		}

		public boolean canInsert(int slot, ItemStack stack, Direction dir) {
			return false;
		}

		public boolean canExtract(int slot, ItemStack stack, Direction dir) {
			return !this.dirty && dir == Direction.DOWN && stack.getItem() == Items.SOUL_SOIL;
		}

		public void markDirty() {
			BlockState blockState = Blocks.COMPOSTER.getDefaultState();
			world.setBlockState(pos, blockState, 3);
			this.dirty = true;
		}
	}
}
