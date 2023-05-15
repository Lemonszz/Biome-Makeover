package party.lemons.biomemakeover.block;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.util.RandomUtil;
import party.lemons.taniwha.util.MathUtils;

import java.util.List;

public class MothBlossomBlock extends IvyShapedBlock{

    public static final DirectionProperty BLOSSOM_DIRECTION = DirectionProperty.create("blossom", Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST, Direction.UP, Direction.DOWN);

    public MothBlossomBlock(Properties settings) {
        super(settings);

        registerDefaultState(getStateDefinition().any().setValue(BLOSSOM_DIRECTION, Direction.DOWN));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BLOSSOM_DIRECTION);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor level, BlockPos pos, BlockPos posFrom) {
        if(!isValidPlaceFace(level, direction.getOpposite(), posFrom, newState))
        {
            if(direction == state.getValue(BLOSSOM_DIRECTION))
            {
                level.destroyBlock(pos, false);
                return state;
            }

            if(hasDirection(state, direction))
            {
                return getStateWithoutDirection(state, getPropertyForDirection(direction));
            }
        }
        else
        {
            return state.setValue(getPropertyForDirection(direction), true);
        }
        return super.updateShape(state, direction, newState, level, pos, posFrom);
    }

    @Override
    public boolean hasDirection(BlockState blockState, Direction direction)
    {
        if(blockState.getValue(BLOSSOM_DIRECTION) == direction)
            return true;

        return super.hasDirection(blockState, direction);
    }


    @Override
    public void tick(BlockState blockState, ServerLevel level, BlockPos pos, RandomSource random) {
        for(int i = 0; i < 3; i++)
        {
            if(attemptSpread(level, pos.offset(0, yOffset[i], 0), random))
                return;
        }
    }
    private final int[] yOffset = {0, 1, -1};

    private boolean attemptSpread(Level level, BlockPos pos, RandomSource random)
    {
        for(Direction direction : MathUtils.randomOrderedHorizontals())
        {
            BlockPos offsetPos = pos.relative(direction);
            BlockState placePosState = level.getBlockState(offsetPos);
            if((placePosState.isAir() || (placePosState.getMaterial().isReplaceable() && !placePosState.is(BMBlocks.ITCHING_IVY.get()))) && IvyBlock.isValidPlaceFace(level, Direction.DOWN, pos.relative(direction), level.getBlockState(pos.relative(direction).below())))
            {
                level.setBlock(offsetPos, BMBlocks.ITCHING_IVY.get().defaultBlockState().setValue(IvyBlock.getPropertyForDirection(Direction.DOWN), true), 3);
                level.scheduleTick(offsetPos, BMBlocks.ITCHING_IVY.get(), 4);
                if(random.nextBoolean())
                    return true;
            }
        }
        return false;
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource random) {
        tick(blockState, serverLevel, blockPos, random);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if(random.nextInt(3) == 0)
        {
            double xx = 0, yy = 0, zz = 0, vx = 0, vy = 0, vz = 0;

            //TODO: could probably use a cleanup but honestly who cares.
            switch (state.getValue(BLOSSOM_DIRECTION)) {
                case DOWN -> {
                    xx = (pos.getX() + 0.5F) + RandomUtil.randomDirection(random.nextFloat() / 4F);
                    zz = pos.getZ() + 0.5F + RandomUtil.randomDirection(random.nextFloat() / 4F);
                    yy = pos.getY() + 0.1F;
                    vx = RandomUtil.randomDirection(random.nextFloat() / 20F);
                    vz = RandomUtil.randomDirection(random.nextFloat() / 20F);
                    vy = random.nextFloat() / 10F;
                }
                case UP -> {
                    xx = (pos.getX() + 0.5F) + RandomUtil.randomDirection(random.nextFloat() / 4F);
                    zz = pos.getZ() + 0.5F + RandomUtil.randomDirection(random.nextFloat() / 4F);
                    yy = pos.getY() + 0.9F;
                    vx = RandomUtil.randomDirection(random.nextFloat() / 20F);
                    vz = RandomUtil.randomDirection(random.nextFloat() / 20F);
                    vy = (random.nextFloat() / 10F) * -1;
                }
                case NORTH -> {
                    xx = pos.getX() + 0.5F + RandomUtil.randomDirection(random.nextFloat() / 4F);
                    zz = pos.getZ() + 0.1F;
                    yy = pos.getY() + 0.5F + RandomUtil.randomDirection(random.nextFloat() / 4F);
                    vx = RandomUtil.randomDirection(random.nextFloat() / 20F);
                    vz = random.nextFloat() / 10F;
                    vy = random.nextFloat() / 20F;
                }
                case SOUTH -> {
                    xx = pos.getX() + 0.5F + RandomUtil.randomDirection(random.nextFloat() / 4F);
                    zz = pos.getZ() + 0.9F;
                    yy = pos.getY() + 0.5F + RandomUtil.randomDirection(random.nextFloat() / 4F);
                    vx = RandomUtil.randomDirection(random.nextFloat() / 20F);
                    vz = (random.nextFloat() / 10F) * -1F;
                    vy = random.nextFloat() / 20F;
                }
                case WEST -> {
                    xx = (pos.getX() + 0.1);
                    zz = pos.getZ() + 0.5F + RandomUtil.randomDirection(random.nextFloat() / 4F);
                    yy = pos.getY() + 0.5F + RandomUtil.randomDirection(random.nextFloat() / 4F);
                    vx = random.nextFloat() / 10F;
                    vz = RandomUtil.randomDirection(random.nextFloat() / 20F);
                    vy = random.nextFloat() / 20F;
                }
                case EAST -> {
                    xx = (pos.getX() + 0.9);
                    zz = pos.getZ() + 0.5F + RandomUtil.randomDirection(random.nextFloat() / 4F);
                    yy = pos.getY() + 0.5F + RandomUtil.randomDirection(random.nextFloat() / 4F);
                    vx = (random.nextFloat() / 10F) * -1;
                    vz = RandomUtil.randomDirection(random.nextFloat() / 20F);
                    vy = random.nextFloat() / 20F;
                }
            }

            level.addParticle(BMEffects.BLOSSOM.get(), xx, yy, zz, vx, vy, vz);
        }
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockPos placeOffset = ctx.getClickedPos().relative(ctx.getClickedFace().getOpposite());
        BlockState offsetState = ctx.getLevel().getBlockState(placeOffset);
        if(isValidPlaceFace(ctx.getLevel(), ctx.getClickedFace().getOpposite(), placeOffset, offsetState))
        {
            BlockState state = defaultBlockState().setValue(BLOSSOM_DIRECTION, ctx.getClickedFace().getOpposite());
            for(Direction dir : Direction.values())
            {
                if(dir == ctx.getClickedFace().getOpposite())
                    continue;

                placeOffset = ctx.getClickedPos().relative(dir);
                offsetState = ctx.getLevel().getBlockState(placeOffset);
                boolean validFace = isValidPlaceFace(ctx.getLevel(), dir, placeOffset, offsetState);
                state = state.setValue(getPropertyForDirection(dir), validFace);
            }
            return state;
        }
        return null;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction dir = state.getValue(BLOSSOM_DIRECTION);
        BlockPos offsetPos = pos.relative(dir);
        BlockState offsetState = level.getBlockState(offsetPos);
        return isValidPlaceFace(level, dir.getOpposite(), offsetPos, offsetState) || super.canSurvive(state, level, pos);
    }

    public BlockState getGrowState(LevelReader world, BlockPos pos)
    {
        BlockState placeState = defaultBlockState();
        List<Direction> validDirections = Lists.newArrayList();

        for(Direction dir : Direction.values())
        {
            BlockPos offsetPos = pos.relative(dir);
            BlockState offsetState = world.getBlockState(offsetPos);
            boolean validFace = isValidPlaceFace(world, dir, offsetPos, offsetState);
            placeState = placeState.setValue(getPropertyForDirection(dir), validFace);

            if(validFace)
                validDirections.add(dir);
        }

        if(validDirections.isEmpty())
            return Blocks.AIR.defaultBlockState();

        Direction blossomDir = RandomUtil.choose(validDirections);
        placeState = placeState.setValue(BLOSSOM_DIRECTION, blossomDir);

        return placeState;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        BlockState st = state.setValue(getPropertyForDirection(state.getValue(BLOSSOM_DIRECTION)), true);
        return this.shapes.get(st);
    }
}
