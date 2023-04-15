package party.lemons.biomemakeover.block;


import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.BlockHitResult;
import party.lemons.taniwha.block.types.TTallFlowerBlock;

public class BMTallMushroomBlock extends TTallFlowerBlock
{
	public BMTallMushroomBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
		return floor.isSolidRender(world, pos);
	}

	@Override
	public boolean isValidBonemealTarget(LevelReader arg, BlockPos arg2, BlockState arg3, boolean bl) {
		return false;
	}

	@Override
	public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
		if (blockState.getValue(HALF) != DoubleBlockHalf.UPPER) {
			return Blocks.RED_MUSHROOM.canSurvive(blockState, levelReader, blockPos);
		} else {
			BlockState belowState = levelReader.getBlockState(blockPos.below());
			return belowState.is(this) && belowState.getValue(HALF) == DoubleBlockHalf.LOWER;
		}
	}
}