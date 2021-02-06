package party.lemons.biomemakeover.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LilyPadBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.LilyPadItem;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.util.BlockWithItem;

public class SmallLilyPadBlock extends LilyPadBlock implements BlockWithItem
{
	public static IntProperty PADS = IntProperty.of("pads", 0, 3);

	public SmallLilyPadBlock(Settings settings)
	{
		super(settings);
		setDefaultState(getStateManager().getDefaultState().with(PADS, 0));
	}

	@Override
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit)
	{
		ItemStack stack = player.getStackInHand(hand);

		if(world.canPlayerModifyAt(player, pos) && state.get(PADS) < 3 && !stack.isEmpty() && stack.getItem() == BMBlocks.SMALL_LILY_PAD.asItem())
		{
			world.setBlockState(pos, state.with(PADS, state.get(PADS) + 1));
			if(!player.isCreative()) stack.decrement(1);

			BlockSoundGroup blockSoundGroup = state.getSoundGroup();
			world.playSound(player, pos, state.getSoundGroup().getPlaceSound(), SoundCategory.BLOCKS, (blockSoundGroup.getVolume() + 1.0F) / 2.0F, blockSoundGroup.getPitch() * 0.8F);

			return ActionResult.SUCCESS;
		}

		return super.onUse(state, world, pos, player, hand, hit);
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder)
	{
		super.appendProperties(builder);
		builder.add(PADS);
	}

	@Override
	public Item makeItem()
	{
		return new LilyPadItem(this, makeItemSettings());
	}
}
