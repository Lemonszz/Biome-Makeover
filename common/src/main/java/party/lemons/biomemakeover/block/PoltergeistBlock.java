package party.lemons.biomemakeover.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.block.blockentity.PoltergeistBlockEntity;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.level.PoltergeistHandler;
import party.lemons.taniwha.block.types.TBlock;

import java.util.Random;

public class PoltergeistBlock extends TBlock implements EntityBlock
{
    public static BooleanProperty ENABLED = BlockStateProperties.ENABLED;

    public PoltergeistBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(ENABLED, true));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new PoltergeistBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return PoltergeistBlockEntity.getTicker(level, blockState);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(ENABLED, !ctx.getLevel().hasNeighborSignal(ctx.getClickedPos()));
    }

    @Override
    public void neighborChanged(BlockState blockState, Level level, BlockPos blockPos, Block block, BlockPos blockPos2, boolean bl) {
        if(!level.isClientSide())
        {
            boolean currentlyEnabled = blockState.getValue(ENABLED);
            if(currentlyEnabled == level.hasNeighborSignal(blockPos))
            {
                if(currentlyEnabled)
                {
                    level.scheduleTick(blockPos, this, 4);
                }
                else
                {
                    level.setBlock(blockPos, blockState.cycle(ENABLED), 2);
                    doToggleEffects(level, blockPos);
                }
            }
        }
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, Random random) {
        if(blockState.getValue(ENABLED) && serverLevel.hasNeighborSignal(blockPos))
        {
            serverLevel.setBlock(blockPos, blockState.cycle(ENABLED), 2);
            doToggleEffects(serverLevel, blockPos);
        }
    }

    private void doToggleEffects(Level world, BlockPos pos)
    {
        world.playSound(null, pos, BMEffects.POLTERGEIST_TOGGLE, SoundSource.BLOCKS, 1F, 1F);
        PoltergeistHandler.doParticles(world, pos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ENABLED);
    }
}
