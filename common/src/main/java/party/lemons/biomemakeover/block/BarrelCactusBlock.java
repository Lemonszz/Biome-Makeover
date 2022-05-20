package party.lemons.biomemakeover.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import party.lemons.biomemakeover.init.BMBlocks;

import java.util.Random;

public class BarrelCactusBlock extends BMBlock
{
    protected static final VoxelShape SHAPE = Block.box(4.0D, 0.0D, 4.0D, 12.0D, 7.0D, 12.0D);
    private final boolean flowered;

    public BarrelCactusBlock(boolean flowered, BlockBehaviour.Properties properties)
    {
        super(properties);
        this.flowered = flowered;
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        BlockState checkState = levelReader.getBlockState(blockPos.below());
        return checkState.is(Blocks.SAND) || checkState.is(Blocks.RED_SAND) || checkState.is(Blocks.CACTUS) || checkState.is(BMBlocks.SAGUARO_CACTUS.get());
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor level, BlockPos pos, BlockPos posFrom) {
        if(!state.canSurvive(level, pos))
            level.scheduleTick(pos, this, 1);

        return state;
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource random) {
        if(!blockState.canSurvive(serverLevel, blockPos))
            serverLevel.destroyBlock(blockPos, true);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return SHAPE;
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if(!entity.isSteppingCarefully())
        {
            if(entity instanceof ItemEntity && entity.tickCount < 30) return;

            entity.hurt(DamageSource.CACTUS, 1.0F);
        }
        super.entityInside(blockState, level, blockPos, entity);
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource random) {
        if(!flowered && serverLevel.getRawBrightness(blockPos, 0) >= 9 && random.nextInt(7) == 0)
            serverLevel.setBlock(blockPos, BMBlocks.BARREL_CACTUS_FLOWERED.get().defaultBlockState(), 3);
    }

    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
        return false;
    }
}
