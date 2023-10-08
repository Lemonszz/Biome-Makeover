package party.lemons.biomemakeover.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
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
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.block.blockentity.PoltergeistBlockEntity;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.init.BMPotions;
import party.lemons.biomemakeover.level.PoltergeistHandler;
import party.lemons.taniwha.block.types.TBlock;

import java.util.Random;

public class PoltergeistBlock extends TBlock implements EntityBlock
{
    //Cauldron shapes
    private static final VoxelShape INSIDE = box(2.0, 4.0, 2.0, 14.0, 16.0, 14.0);
    //private static final VoxelShape SUPPORT_SHAPE = Shapes.join(box(0.0, 0.0, 0.0, 16.0, 16.0, 16.0), INSIDE, BooleanOp.ONLY_FIRST);   //Can't place face attachements on top
    private static final VoxelShape SUPPORT_SHAPE = Shapes.join(Shapes.block(), Block.box(2.0, 0, 2.0, 14.0, 16.0, 14.0), BooleanOp.ONLY_FIRST);
    protected static final VoxelShape SHAPE = Shapes.join(
            Shapes.block(),
            Shapes.or(box(0.0, 0.0, 4.0, 16.0, 3.0, 12.0), box(4.0, 0.0, 0.0, 12.0, 3.0, 16.0), box(2.0, 0.0, 2.0, 14.0, 3.0, 14.0), INSIDE),
            BooleanOp.ONLY_FIRST
    );

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

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public VoxelShape getInteractionShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return INSIDE;
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return SUPPORT_SHAPE;
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
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource random) {
        if(blockState.getValue(ENABLED) && serverLevel.hasNeighborSignal(blockPos))
        {
            serverLevel.setBlock(blockPos, blockState.cycle(ENABLED), 2);
            doToggleEffects(serverLevel, blockPos);
        }
    }

    private void doToggleEffects(Level world, BlockPos pos)
    {
        world.playSound(null, pos, BMEffects.POLTERGEIST_TOGGLE.get(), SoundSource.BLOCKS, 1F, 1F);
        PoltergeistHandler.doParticles(world, pos);
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        final int maxTime = 600;
        final int maxLevel = 4;

        if (blockState.getValue(ENABLED) && entity.getY() < (double)blockPos.getY() + 0.9375 && entity.getBoundingBox().maxY > (double)blockPos.getY() + 0.25) {
            if(entity instanceof LivingEntity living)
            {
                MobEffectInstance instance = living.getEffect(BMPotions.POSSESSED.get());
                if(instance == null)
                    living.addEffect(new MobEffectInstance(BMPotions.POSSESSED.get(), 200, 0));
                else if(instance.getDuration() < maxTime || instance.getAmplifier() < maxLevel){
                    int nextLevel = instance.getAmplifier();
                    if(nextLevel <= maxLevel && living.getRandom().nextInt(100) == 0)
                        nextLevel += 1;

                    MobEffectInstance newEffect = new MobEffectInstance(BMPotions.POSSESSED.get(), instance.getDuration() + 2, nextLevel);
                    instance.update(newEffect);
                }
            }
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(ENABLED);
    }
}
