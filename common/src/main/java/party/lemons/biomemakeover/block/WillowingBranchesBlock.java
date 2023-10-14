package party.lemons.biomemakeover.block;

import net.minecraft.BlockUtil;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import party.lemons.taniwha.block.types.TBlock;

import java.util.Optional;

public class WillowingBranchesBlock extends TBlock implements SimpleWaterloggedBlock, BonemealableBlock
{

    public static final int MAX_GROWTH_STAGES = 2;
    public static final int MAX_STAGES = 3;

    public static final IntegerProperty STAGE = IntegerProperty.create("stage", 0, MAX_STAGES);
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private VoxelShape[] SHAPES = new VoxelShape[]{
            Shapes.block(),
            Shapes.box(0.1, 0, 0.1, 0.9, 1, 0.9),
            Shapes.box(0.15, 0.4, 0.15, 0.85, 1, 0.85),
            Shapes.box(0.15, 0.4, 0.15, 0.85, 1, 0.85),
    };

    public WillowingBranchesBlock(Properties properties)
    {
        super(properties);

        registerDefaultState(getStateDefinition().any().setValue(STAGE, MAX_GROWTH_STAGES).setValue(WATERLOGGED, false));
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState newState, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos posFrom) {

        if(direction == Direction.DOWN)
        {
            int currentStage = blockState.getValue(STAGE);
            if(newState.is(this)) {
                int targetStage;
                if(newState.getValue(STAGE) == MAX_STAGES)
                    targetStage = 1;
                else
                    targetStage = Mth.clamp(newState.getValue(STAGE) - 1, 0, MAX_GROWTH_STAGES);

                if (currentStage != targetStage)
                    blockState = blockState.setValue(STAGE, targetStage);
            }
            else if(currentStage != MAX_GROWTH_STAGES)
                    blockState = blockState.setValue(STAGE, MAX_GROWTH_STAGES);
        }

        if(!canSurvive(blockState, levelAccessor, blockPos))
        {
            levelAccessor.scheduleTick(blockPos, this, 1);
        }

        return super.updateShape(blockState, direction, newState, levelAccessor, blockPos, posFrom);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {

        if(random.nextInt(7) == 0)
        {
            grow(state, level, pos, random, false);
        }

        super.randomTick(state, level, pos, random);
    }

    public void grow(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, boolean forced)
    {
        BlockState aboveState = level.getBlockState(pos.above());
        boolean isWaterloggedBranchAbove = aboveState.is(this) && aboveState.getValue(WATERLOGGED);

        if((state.getValue(STAGE) == MAX_GROWTH_STAGES || forced) && !isWaterloggedBranchAbove)
        {
            BlockState placeState = defaultBlockState().setValue(STAGE, random.nextInt(4) == 0 ? MAX_STAGES : MAX_GROWTH_STAGES);
            BlockPos below = pos.below();
            if((level.isEmptyBlock(below) || level.getBlockState(below).is(Blocks.WATER)) && canSurvive(placeState, level, below))
            {
                FluidState st = level.getFluidState(below);
                level.setBlock(below, placeState.setValue(WATERLOGGED, st.getType() == Fluids.WATER), 3);
            }
        }
    }

    public boolean canGrow(BlockState state, LevelReader level, BlockPos pos)
    {
        BlockPos below = pos.below();
        return (level.isEmptyBlock(below) || level.getBlockState(below).is(Blocks.WATER)) && canSurvive(state, level, below);
    }

    public Optional<BlockPos> findHead(BlockState state, BlockPos pos, BlockGetter level)
    {
        if(state.getValue(STAGE) >= MAX_GROWTH_STAGES)
            return Optional.of(pos);

        BlockPos.MutableBlockPos checkPos = pos.mutable();

        do {
            checkPos.move(Direction.DOWN);
        } while(level.getBlockState(checkPos).is(this));

        return Optional.of(checkPos.above());
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource random) {
        if(!blockState.canSurvive(serverLevel, blockPos))
            serverLevel.destroyBlock(blockPos, true);
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader level, BlockPos pos) {
        BlockPos blockPos = pos.above();
        BlockState upState = level.getBlockState(blockPos);
        return upState.is(this) || upState.is(BlockTags.LEAVES);
    }

    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
        return pathComputationType == PathComputationType.AIR && !this.hasCollision || super.isPathfindable(blockState, blockGetter, blockPos, pathComputationType);
    }

    @Override
    public boolean triggerEvent(BlockState blockState, Level level, BlockPos blockPos, int i, int j)
    {
        for(int m = 0; m < 15; m++)
        {
            int dirX = level.getRandom().nextInt(2) * 2 - 1;
            int dirY = level.getRandom().nextInt(2) * 2 - 1;
            int dirZ = level.getRandom().nextInt(2) * 2 - 1;

            Vec3 pos = blockPos.getCenter();
            level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockState), pos.x + (dirX * 0.3F), (pos.y + 0.5) + (dirY * 0.3F), pos.z + (dirZ * 0.3F), 0.0D, 0.0D, 0.0D);
        }

        return true;
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult)
    {
        ItemStack stack = player.getItemInHand(interactionHand);
        if(stack.is(Items.SHEARS) && blockState.getValue(STAGE) == MAX_GROWTH_STAGES)  //TOOD: look into tool tags/interactions
        {
            level.playSound(player, blockPos, SoundEvents.GROWING_PLANT_CROP, SoundSource.BLOCKS, 1.0F, 1.0F);

            if (player instanceof ServerPlayer) {
                CriteriaTriggers.ITEM_USED_ON_BLOCK.trigger((ServerPlayer)player, blockPos, stack);
                BlockState newState = blockState.setValue(STAGE, MAX_STAGES);
                level.setBlockAndUpdate(blockPos, newState);
                level.gameEvent(GameEvent.BLOCK_CHANGE, blockPos, GameEvent.Context.of(player, newState));
                stack.hurtAndBreak(1, player, playerx -> playerx.broadcastBreakEvent(interactionHand));

                level.blockEvent(blockPos, this, 0, 0);
            }
            return InteractionResult.SUCCESS;
        }
        return super.use(blockState, level, blockPos, player, interactionHand, blockHitResult);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx)
    {
        BlockPos pos = ctx.getClickedPos();
        FluidState fluidState = ctx.getLevel().getFluidState(pos);
        boolean waterlog = fluidState.getType() == Fluids.WATER;
        return this.defaultBlockState().setValue(STAGE, MAX_GROWTH_STAGES).setValue(WATERLOGGED, waterlog);
    }

    @Override
    public FluidState getFluidState(BlockState blockState) {
        return blockState.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(blockState);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext)
    {
        Vec3 offset = blockState.getOffset(blockGetter, blockPos);
        return SHAPES[blockState.getValue(STAGE)].move(offset.x, offset.y, offset.z);
    }

    @Override
    public float getMaxHorizontalOffset() {
        return 0.10F;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STAGE, WATERLOGGED);
        super.createBlockStateDefinition(builder);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState blockState, boolean bl)
    {
        Optional<BlockPos> headPos = findHead(blockState, blockPos, levelReader);
        if(headPos.isEmpty())
            return false;

        return canGrow(levelReader.getBlockState(headPos.get()), levelReader, headPos.get());
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState blockState)
    {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel, RandomSource randomSource, BlockPos blockPos, BlockState blockState)
    {
        Optional<BlockPos> headPos = findHead(blockState, blockPos, serverLevel);
        if(headPos.isEmpty())
            return;

        grow(serverLevel.getBlockState(headPos.get()), serverLevel, headPos.get(), randomSource, true);
    }
}
