package party.lemons.biomemakeover.block;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.util.BMUtil;
import party.lemons.taniwha.block.types.TBlock;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

public class IvyShapedBlock extends TBlock
{
    private static final VoxelShape UP_SHAPE = Block.box(0.0D, 15.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape DOWN_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 1.0D, 16.0D);
    private static final VoxelShape EAST_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 1.0D, 16.0D, 16.0D);
    private static final VoxelShape WEST_SHAPE = Block.box(15.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D);
    private static final VoxelShape SOUTH_SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 1.0D);
    private static final VoxelShape NORTH_SHAPE = Block.box(0.0D, 0.0D, 15.0D, 16.0D, 16.0D, 16.0D);
    private static final Map<Direction, BooleanProperty> FACING_PROPERTIES = PipeBlock.PROPERTY_BY_DIRECTION;
    private static final Map<Direction, VoxelShape> DIRECTION_TO_SHAPE = Util.make(Maps.newEnumMap(Direction.class), (enumMap)->
    {
        enumMap.put(Direction.NORTH, SOUTH_SHAPE);
        enumMap.put(Direction.EAST, WEST_SHAPE);
        enumMap.put(Direction.SOUTH, NORTH_SHAPE);
        enumMap.put(Direction.WEST, EAST_SHAPE);
        enumMap.put(Direction.UP, UP_SHAPE);
        enumMap.put(Direction.DOWN, DOWN_SHAPE);
    });
    protected final ImmutableMap<BlockState, VoxelShape> shapes;

    public IvyShapedBlock(BlockBehaviour.Properties settings)
    {
        super(settings);
        this.registerDefaultState(createDefaultState(this.getStateDefinition()));
        this.shapes = collectVoxelShapes(getStateDefinition());
    }

    public static BooleanProperty getPropertyForDirection(Direction direction)
    {
        return FACING_PROPERTIES.get(direction);
    }

    protected static BlockState createDefaultState(StateDefinition<Block, BlockState> stateManager)
    {
        BlockState blockState = stateManager.any();

        for(BooleanProperty property : FACING_PROPERTIES.values())
        {
            if(blockState.hasProperty(property))
            {
                blockState = blockState.setValue(property, false);
            }
        }
        return blockState;
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState newState, LevelAccessor level, BlockPos pos, BlockPos posFrom) {
        BlockState returnState;
        if(hasDirection(state, direction) && !isValidPlaceFace(level, direction, posFrom, newState))
        {
            returnState = getStateWithoutDirection(state, getPropertyForDirection(direction));
            if(!level.isClientSide())
            {
                level.levelEvent(2001, pos, Block.getId(defaultBlockState().setValue(getPropertyForDirection(direction), true)));
            }
        }
        else
        {
            returnState = state;
        }

        if(!hasAnySide(returnState))
        {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 3);
            return state;
        }

        return returnState;
    }

    private boolean hasAnySide(BlockState state)
    {
        for(Direction d : Direction.values())
        {
            if(hasDirection(state, d))
                return true;
        }
        return false;
    }

    public boolean hasAdjacentSide(Direction direction, BlockState state)
    {
        for(Direction d : Direction.values())
        {
            if(BMUtil.isAdjacentDirection(d, direction) && hasDirection(state, d)) return true;
        }

        return false;
    }

    public BlockState getStateWithoutDirection(BlockState state, BooleanProperty directionProperty)
    {
        return state.setValue(directionProperty, false);
    }

    @Override
    public VoxelShape getVisualShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return this.shapes.get(blockState);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return this.shapes.get(blockState);
    }

    public static VoxelShape getDownShape()
    {
        return DOWN_SHAPE;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos)
    {
        boolean canPlace = false;

        for(Direction direction : Direction.values())
        {
            if(hasDirection(state, direction))
            {
                BlockPos blockPos = pos.relative(direction);
                if(!isValidPlaceFace(level, direction, blockPos, level.getBlockState(blockPos)))
                {
                    return true;
                }
                canPlace = true;
            }
        }
        return canPlace;
    }

    protected ImmutableMap<BlockState, VoxelShape> collectVoxelShapes(StateDefinition<Block, BlockState> stateDefinition)
    {
        return stateDefinition.getPossibleStates().stream().collect(ImmutableMap.toImmutableMap(Function.identity(), (s)->IvyShapedBlock.calculateShape(s, this)));
    }

    protected Direction getRandomStateSide(BlockState state, RandomSource random)
    {
        List<Direction> dirs = Arrays.stream(Direction.values()).filter(d -> state.getValue(getPropertyForDirection(d))).toList();
        if(dirs.isEmpty()) return null;

        return dirs.get(random.nextInt(dirs.size()));
    }

    protected static VoxelShape mergeVoxelShapes(BlockState blockState)
    {
        VoxelShape shape = Shapes.empty();
        for(Direction direction : Direction.values())
        {
            if(isValidDirectionForState(blockState, direction))
            {
                shape = Shapes.or(shape, DIRECTION_TO_SHAPE.get(direction));
            }
        }
        return shape;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        for(Direction direction : Direction.values())
        {
            builder.add(getPropertyForDirection(direction));
        }
    }

    @Nullable
    public BlockState getPlacementState(BlockState blockState, Level worldAccess, BlockPos blockPos, Direction direction)
    {
        BlockState placeState;
        if(blockState.is(this))
        {
            if(hasDirection(blockState, direction))
            {
                return null;
            }
            placeState = blockState;
        }else
        {
            placeState = this.defaultBlockState();
        }
        BlockPos placeSide = blockPos.relative(direction);
        return isValidPlaceFace(worldAccess, direction, placeSide, worldAccess.getBlockState(placeSide)) ? placeState.setValue(getPropertyForDirection(direction), true) : null;
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation)
    {
        return this.getStateWithDirections(state, rotation::rotate);
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror)
    {
        return this.getStateWithDirections(state, mirror::mirror);
    }

    private BlockState getStateWithDirections(BlockState blockState, Function<Direction, Direction> function)
    {
        BlockState state = blockState;
        for(Direction direction : Direction.values())
        {
            state = state.setValue(getPropertyForDirection(function.apply(direction)), blockState.getValue(getPropertyForDirection(direction)));
        }
        return state;
    }

    public boolean hasDirection(BlockState blockState, Direction direction)
    {
        BooleanProperty booleanProperty = getPropertyForDirection(direction);
        return blockState.getValue(booleanProperty);
    }

    public static boolean isValidPlaceFace(LevelReader blockView, Direction direction, BlockPos blockPos, BlockState blockState)
    {
        return Block.isFaceFull(blockState.getCollisionShape(blockView, blockPos), direction.getOpposite());
    }

    private static boolean isValidDirectionForState(BlockState blockState, Direction direction)
    {
        BooleanProperty booleanProperty = getPropertyForDirection(direction);
        return blockState.hasProperty(booleanProperty) && blockState.getValue(booleanProperty);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        Level world = ctx.getLevel();
        BlockPos blockPos = ctx.getClickedPos();
        BlockState blockState = world.getBlockState(blockPos);
        return Arrays.stream(ctx.getNearestLookingDirections())
                .map((direction)->this.getPlacementState(blockState, world, blockPos, direction))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);    }


    private static VoxelShape calculateShape(BlockState blockState, IvyShapedBlock block) {
        VoxelShape voxelShape = Shapes.empty();
        for (Direction direction : Direction.values()) {
            if (!block.hasDirection(blockState, direction)) continue;
            voxelShape = Shapes.or(voxelShape, DIRECTION_TO_SHAPE.get(direction));
        }
        return voxelShape.isEmpty() ? Shapes.block() : voxelShape;
    }
}