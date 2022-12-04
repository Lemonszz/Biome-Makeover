package party.lemons.biomemakeover.block;

import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.block.blockentity.DirectionalDataBlockEntity;

public class DirectionalDataBlock extends DirectionalBlock implements EntityBlock
{
    public DirectionalDataBlock(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        if(!level.isClientSide() && player.isCreative())
        {
            MenuProvider screenHandlerFactory = blockState.getMenuProvider(level, blockPos);
            if(screenHandlerFactory != null)
            {
                BlockEntity entity = level.getBlockEntity(blockPos);
                if(entity instanceof DirectionalDataBlockEntity data) {

                    MenuRegistry.openExtendedMenu((ServerPlayer) player, screenHandlerFactory, (buf) -> {
                        buf.writeBlockPos(data.getBlockPos());
                        buf.writeUtf(data.getMetadata());
                    });
                }
            }
        }
        return InteractionResult.SUCCESS;
    }

    @Override
    public boolean triggerEvent(BlockState state, Level level, BlockPos pos, int type, int data) {
        super.triggerEvent(state, level, pos, type, data);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        return blockEntity != null && blockEntity.triggerEvent(type, data);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
        return defaultBlockState().setValue(FACING, blockPlaceContext.getClickedFace().getOpposite());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new DirectionalDataBlockEntity(blockPos, blockState);
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.rotate(mirror.getRotation(state.getValue(FACING)));
    }

    @Override
    @Nullable
    public MenuProvider getMenuProvider(BlockState blockState, Level level, BlockPos blockPos) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        return blockEntity instanceof MenuProvider ? (MenuProvider) blockEntity : null;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }
}
