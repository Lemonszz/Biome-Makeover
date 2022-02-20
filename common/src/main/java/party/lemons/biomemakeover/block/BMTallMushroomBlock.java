package party.lemons.biomemakeover.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
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

public class BMTallMushroomBlock extends BMTallFlowerBlock{
    private Block dropBlock;

    public BMTallMushroomBlock(Block dropBlock, Properties properties) {
        super(properties);

        this.dropBlock = dropBlock;
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if(dropBlock != null && !level.isClientSide() && !player.getItemInHand(interactionHand).isEmpty() && player.getItemInHand(interactionHand).getItem() == Items.SHEARS)
        {
            BlockPos dropPos = blockPos;
            player.playSound(SoundEvents.BEEHIVE_SHEAR, 1F, 1F);
            if(blockState.getValue(HALF) == DoubleBlockHalf.LOWER && level.getBlockState(blockPos.above()).getBlock() == this)
            {
                level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 35);
                level.setBlock(blockPos.above(), Blocks.AIR.defaultBlockState(), 35);
                level.levelEvent(player, 2001, blockPos, Block.getId(blockState));
                level.levelEvent(player, 2001, blockPos.above(), Block.getId(blockState));

                dropPos = blockPos.above();
            }else if(blockState.getValue(HALF) == DoubleBlockHalf.UPPER && level.getBlockState(blockPos.below()).getBlock() == this)
            {
                level.setBlock(blockPos.below(), Blocks.AIR.defaultBlockState(), 35);
                level.setBlock(blockPos, Blocks.AIR.defaultBlockState() , 3);
                level.levelEvent(player, 2001, blockPos, Block.getId(blockState));
                level.levelEvent(player, 2001, blockPos.below(), Block.getId(blockState));
            }
            popResourceFromFace(level, dropPos, blockHitResult.getDirection(), new ItemStack(dropBlock, 1 + level.random.nextInt(2)));
            player.getItemInHand(interactionHand).hurt(1, level.random, (ServerPlayer) player);
            return InteractionResult.SUCCESS;
        }

        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    @Override
    protected boolean mayPlaceOn(BlockState floor, BlockGetter world, BlockPos pos) {
        return floor.isSolidRender(world, pos);
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
