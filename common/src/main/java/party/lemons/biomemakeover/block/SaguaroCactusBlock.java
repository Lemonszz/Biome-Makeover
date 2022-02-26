package party.lemons.biomemakeover.block;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.util.RandomUtil;
import party.lemons.taniwha.block.types.TBlock;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class SaguaroCactusBlock extends TBlock implements BonemealableBlock
{
    public static final BooleanProperty HORIZONTAL = BooleanProperty.create("horizontal");
    public static final DirectionProperty HORIZONTAL_DIRECTION = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty NORTH = BooleanProperty.create("north");
    public static final BooleanProperty SOUTH = BooleanProperty.create("south");
    public static final BooleanProperty EAST = BooleanProperty.create("east");
    public static final BooleanProperty WEST = BooleanProperty.create("west");

    public SaguaroCactusBlock(Properties properties)
    {
        super(properties);
        this.registerDefaultState(getStateDefinition().any()
                .setValue(HORIZONTAL, false)
                .setValue(HORIZONTAL_DIRECTION, Direction.NORTH)
                .setValue(NORTH, false)
                .setValue(SOUTH, false)
                .setValue(EAST, false)
                .setValue(WEST, false));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState st = defaultBlockState();
        if(ctx.getClickedFace().get2DDataValue() >= 0)
            st = st.setValue(HORIZONTAL, true).setValue(HORIZONTAL_DIRECTION, ctx.getClickedFace().getOpposite()).setValue(FACING_PROPERTIES.get(ctx.getClickedFace().getOpposite()), true);

        return st;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor level, BlockPos pos, BlockPos posFrom) {
        if(!canSurvive(state, level, pos))
        {
            level.scheduleTick(pos, this, 1);
            return super.updateShape(state, direction, newState, level, pos, posFrom);
        }

        if(direction.getAxis().isVertical())
            return super.updateShape(state, direction, newState, level, pos, posFrom);

        if(newState.getBlock() == this)
        {
            if(newState.getValue(HORIZONTAL) && newState.getValue(FACING_PROPERTIES.get(direction.getOpposite())))
                return state.setValue(FACING_PROPERTIES.get(direction), true);
        }else
        {
            return state.setValue(FACING_PROPERTIES.get(direction), false);
        }
        return super.updateShape(state, direction, newState, level, pos, posFrom);
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, Random random) {
        if(!blockState.canSurvive(serverLevel, blockPos))
        {
            serverLevel.destroyBlock(blockPos, true);
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader levelReader, BlockPos blockPos) {
        if(state.getValue(HORIZONTAL))
        {
            Direction offset = state.getValue(HORIZONTAL_DIRECTION);
            if(!levelReader.getBlockState(blockPos.relative(offset)).is(this)) return false;
        }else
        {
            BlockState checkState = levelReader.getBlockState(blockPos.below());
            return (checkState.is(this) || checkState.is(Blocks.SAND) || checkState.is(Blocks.RED_SAND)) && !levelReader.getBlockState(blockPos.above()).getMaterial().isLiquid();
        }

        return super.canSurvive(state, levelReader, blockPos);
    }

    public boolean isGrowBlock(BlockState state)
    {
        return state.is(Blocks.SAND) || state.is(Blocks.RED_SAND);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return getShape(blockState);
    }

    public VoxelShape getShape(BlockState state)
    {
        return shapes.get(state);
    }

    private final HashMap<BlockState, VoxelShape> shapes = Util.make(Maps.newHashMap(), m->getStateDefinition().getPossibleStates().forEach(st->m.put(st, getShapeForState(st))));

    private VoxelShape getShapeForState(BlockState state)
    {
        double size = 4;
        VoxelShape base;

        if(state.getValue(HORIZONTAL)) base = Block.box(size, size * 2, size, 16 - size, 15.98, 16 - size);
        else base = Block.box(size, 0, size, 16 - size, 15.98, 16 - size);

        List<VoxelShape> connections = Lists.newArrayList();
        for(Direction dir : Direction.values())
        {
           if(dir == Direction.DOWN || dir == Direction.UP) continue;

            if(state.getValue(FACING_PROPERTIES.get(dir)))
            {
                double x = dir == Direction.WEST ? 0 : dir == Direction.EAST ? 16D : size;
                double z = dir == Direction.NORTH ? 0 : dir == Direction.SOUTH ? 16D : size;

                double upper = 16D - size;

                double minX = Math.min(x, upper) / 16.0;
                double maxX = Math.max(x, upper) / 16.0;
                double minZ = Math.min(z, upper) / 16.0;
                double maxZ = Math.max(z, upper) / 16.0;

                VoxelShape sh = Shapes.box(minX, 8 / 16.0, minZ, maxX, 15.98 / 16.0, maxZ);
                connections.add(sh);
            }
        }

        return Shapes.or(base, connections.toArray(new VoxelShape[]{}));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(EAST, WEST, NORTH, SOUTH, HORIZONTAL, HORIZONTAL_DIRECTION);
    }

    public static final Map<Direction, BooleanProperty> FACING_PROPERTIES = Util.make(Maps.newEnumMap(Direction.class), (enumMap)->
    {
        enumMap.put(Direction.NORTH, NORTH);
        enumMap.put(Direction.EAST, EAST);
        enumMap.put(Direction.SOUTH, SOUTH);
        enumMap.put(Direction.WEST, WEST);
    });


    @Override
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, Random random) {
        if(isValidBonemealTarget(serverLevel, blockPos, blockState, serverLevel.isClientSide) && random.nextInt(10) == 0)
            performBonemeal(serverLevel, random, blockPos, blockState);
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        entity.hurt(DamageSource.CACTUS, 1.0f);
    }

    @Override
    public boolean isPathfindable(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, PathComputationType pathComputationType) {
        return false;
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, boolean isClient) {
        return blockState.equals(defaultBlockState()) && isGrowBlock(blockGetter.getBlockState(blockPos.below())) && blockGetter.getBlockState(blockPos.above()).isAir();
    }

    @Override
    public boolean isBonemealSuccess(Level level, Random random, BlockPos blockPos, BlockState blockState) {
        return (double) level.random.nextFloat() < 0.45D;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel, Random random, BlockPos blockPos, BlockState blockState) {
        generateCactus(this, serverLevel, random.nextBoolean(), blockPos, random, false);
    }


    private final static Direction[] NORTH_SOUTH = {Direction.NORTH, Direction.SOUTH};
    private final static Direction[] EAST_WEST = {Direction.EAST, Direction.WEST};
    public static boolean generateCactus(Block block, WorldGenLevel world, boolean northSouth, BlockPos pos, Random random, boolean isBig)
    {
        if(!block.defaultBlockState().canSurvive(world, pos)) return false;

        boolean hasArms = random.nextInt(10) > 1;
        boolean has2Arms = random.nextInt(5) != 0;

        int centerHeight = RandomUtil.randomRange(4, 8);

        BlockPos.MutableBlockPos p = new BlockPos.MutableBlockPos(pos.getX(), pos.getY(), pos.getZ());
        for(int yy = 0; yy < centerHeight; yy++)
        {
            if(yy > 0)
            {
                if(!world.getBlockState(p).isAir()) break;
            }

            world.setBlock(p, block.defaultBlockState(), 2);
            p.move(Direction.UP);
        }

        if(!hasArms) return true;

        int centerEndY = p.getY();
        int armStart = RandomUtil.randomRange(1, centerHeight - 2);
        Direction[] directions = northSouth ? NORTH_SOUTH : EAST_WEST;

        if(has2Arms)
        {
            for(Direction d : directions)
            {
                generateArm(block, world, d, p.getX(), pos.getY() + armStart, p.getZ(), centerEndY);
                armStart = RandomUtil.randomRange(1, centerHeight - 2);
            }
        }else
        {
            generateArm(block, world, directions[random.nextInt(directions.length)], p.getX(), pos.getY() + armStart, p.getZ(), centerEndY);
        }

        if((!isBig && random.nextInt(10) == 0) || (isBig && random.nextInt(50) == 0))
        {
            BlockPos nextPos = new BlockPos(pos.getX(), centerEndY, pos.getZ());
            if(world.getBlockState(nextPos).isAir()) generateCactus(block, world, random.nextBoolean(), nextPos, random, true);
        }
        return true;
    }

    private static void generateArm(Block block, WorldGenLevel world, Direction direction, int centerX, int armY, int centerZ, int centerHeight)
    {
        BlockPos.MutableBlockPos p = new BlockPos.MutableBlockPos(centerX + direction.getStepX(), armY, centerZ + direction.getStepZ());

        if(!world.getBlockState(p).isAir()) return;

        BlockPos centerPos = p.relative(direction.getOpposite());
        BlockState centerState = world.getBlockState(centerPos);
        if(!centerState.is(block)) return;

        world.setBlock(centerPos, centerState.setValue(SaguaroCactusBlock.FACING_PROPERTIES.get(direction), true), 2);
        world.setBlock(p, block.defaultBlockState().setValue(SaguaroCactusBlock.HORIZONTAL, true).setValue(SaguaroCactusBlock.HORIZONTAL_DIRECTION, direction.getOpposite()).setValue(SaguaroCactusBlock.FACING_PROPERTIES.get(direction.getOpposite()), true), 2);

        p.move(Direction.UP);
        int amt = Math.max(1, (centerHeight - p.getY()) + RandomUtil.randomRange(-3, -1));
        for(int i = 0; i < amt; i++)
        {
            if(!world.getBlockState(p).isAir()) return;
            world.setBlock(p, block.defaultBlockState(), 2);
            p.move(Direction.UP);
        }
    }
}