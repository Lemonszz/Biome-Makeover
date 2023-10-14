package party.lemons.biomemakeover.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.init.BMAdvancements;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.taniwha.block.types.TBlock;

public class PeatComposterBlock extends TBlock implements WorldlyContainerHolder
{
    //TODO: more generic class?

    public PeatComposterBlock(Properties properties) {
        super(properties);
    }

    @Override
    public WorldlyContainer getContainer(BlockState blockState, LevelAccessor levelAccessor, BlockPos blockPos) {
        return new EctoplasmComposterBlock.FullComposterContainer(levelAccessor, blockPos, new ItemStack(BMBlocks.PEAT.get()));
    }

    @Override
    public boolean hasAnalogOutputSignal(BlockState blockState) {
        return true;
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState, Level level, BlockPos blockPos) {
        return 9;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult)
    {
        EctoplasmComposterBlock.emptyFullComposter(level, pos,  new ItemStack(BMBlocks.PEAT.get()));
        if(!level.isClientSide())
            BMAdvancements.PEAT_COMPOST.trigger((ServerPlayer) player);

        return InteractionResult.sidedSuccess(level.isClientSide());
    }
}
