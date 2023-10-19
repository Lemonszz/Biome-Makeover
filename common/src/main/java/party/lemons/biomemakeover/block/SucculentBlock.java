package party.lemons.biomemakeover.block;

import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.item.SucculentItem;
import party.lemons.biomemakeover.util.effect.BiomeMakeoverEffect;
import party.lemons.biomemakeover.util.effect.EffectHelper;
import party.lemons.taniwha.block.types.TBushBlock;

import java.util.ArrayList;
import java.util.List;

public class SucculentBlock extends TBushBlock {
    public SucculentBlock(Properties properties) {
        super(properties);
        registerDefaultState(getStateDefinition().any().setValue(NORTH_EAST, SucculentType.NONE).setValue(SOUTH_EAST, SucculentType.NONE).setValue(SOUTH_WEST, SucculentType.NONE).setValue(NORTH_WEST, SucculentType.NONE));
        this.shapesCache = this.getShapeForEachState(SucculentBlock::calculateShapes);
    }
    
    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {

        BlockState currentState = ctx.getLevel().getBlockState(ctx.getClickedPos());
        EnumProperty<SucculentType> targetProperty = getPropertyFromPlaceContext(ctx);

        SucculentType type = SucculentType.NONE;
        if(!ctx.getItemInHand().isEmpty() && ctx.getItemInHand().getItem() instanceof SucculentItem succulentItem)
        {
            type = succulentItem.type;
        }

        if(currentState.is(this))
        {
            return currentState.setValue(targetProperty, type);
        }

        return defaultBlockState().setValue(targetProperty, type);
    }

    @Override
    public boolean canBeReplaced(BlockState blockState, BlockPlaceContext ctx)
    {
        ItemStack stack = ctx.getItemInHand();
        if (!(stack.getItem() instanceof SucculentItem)) {
            return false;
        }

        if (ctx.replacingClickedOnBlock())
        {
            BlockState currentState = ctx.getLevel().getBlockState(ctx.getClickedPos());
            EnumProperty<SucculentType> targetProperty = getPropertyFromPlaceContext(ctx);
            return currentState.getValue(targetProperty) == SucculentType.NONE;
        }
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return this.shapesCache.get(blockState);
    }

    private EnumProperty<SucculentType> getPropertyFromPlaceContext(BlockPlaceContext ctx)
    {
        BlockPos clickPos = ctx.getClickedPos();
        BlockState currentState = ctx.getLevel().getBlockState(clickPos);
        Vec3 click = ctx.getClickLocation().subtract(clickPos.getX(), clickPos.getY(), clickPos.getZ());
        boolean isNorth = click.z < 0.5F;
        boolean isEast = click.x > 0.5F;

        EnumProperty<SucculentType> targetType;
        if(isNorth && isEast)
            targetType = NORTH_EAST;
        else if(isNorth)
            targetType =  NORTH_WEST;
        else if(isEast)
            targetType = SOUTH_EAST;
        else targetType = SOUTH_WEST;

        if(currentState.is(this)) {
            if(isActive(currentState, targetType)) {
                if (ctx.getClickedFace() == Direction.EAST) {
                    if (targetType == NORTH_WEST)
                        targetType = NORTH_EAST;
                    else if (targetType == SOUTH_WEST)
                        targetType = SOUTH_EAST;
                }
                else if(ctx.getClickedFace() == Direction.NORTH) {
                    if (targetType == SOUTH_WEST)
                        targetType = NORTH_WEST;
                    else if(targetType == SOUTH_EAST)
                        targetType = NORTH_EAST;
                }
            }
        }

        return targetType;
    }

    @Override
    public List<ItemStack> getDrops(BlockState blockState, LootParams.Builder builder)
    {
        LootParams lootParams = builder.withParameter(LootContextParams.BLOCK_STATE, blockState).create(LootContextParamSets.BLOCK);
        ServerLevel serverLevel = lootParams.getLevel();
        List<ItemStack> items = new ArrayList<>();

        for(EnumProperty<SucculentType> type : TYPES)
        {
            if(isActive(blockState, type))
            {
                LootTable lootTable = serverLevel.getServer().getLootData().getLootTable(blockState.getValue(type).loottable);
                items.addAll(lootTable.getRandomItems(lootParams));
            }
        }

        return items;
    }

    @Override
    protected void spawnDestroyParticles(Level level, Player player, BlockPos blockPos, BlockState blockState) {
        if(!level.isClientSide())
            EffectHelper.doEffect(level, BiomeMakeoverEffect.DESTROY_SUCCULENT, blockPos, blockState);

        level.gameEvent(GameEvent.BLOCK_DESTROY, blockPos, GameEvent.Context.of(player, blockState));
    }

    @Override
    protected boolean mayPlaceOn(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return blockState.is(BMBlocks.SUCCULENT_PLANTABLE);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(NORTH_EAST, SOUTH_EAST, SOUTH_WEST, NORTH_WEST);
    }

    private static VoxelShape calculateShapes(BlockState blockState)        //TODO: bitmask this?
    {
        VoxelShape shape = Shapes.empty();
        for(int i = 0; i < TYPES.size(); i++)
        {
            EnumProperty<SucculentType> type = TYPES.get(i);
            if(isActive(blockState, type))
                shape = Shapes.or(shape, SHAPES.get(i));
        }

        return shape.isEmpty() ? Shapes.block() : shape;
    }

    public static boolean isActive(BlockState state, EnumProperty<SucculentType> property)
    {
        return state.getValue(property) != SucculentType.NONE;
    }

    private final ImmutableMap<BlockState, VoxelShape> shapesCache;

    public static final EnumProperty<SucculentType> NORTH_EAST = EnumProperty.create("north_east", SucculentType.class);
    private static final VoxelShape NORTH_EAST_SHAPE = box(8, 0, 0, 16, 5, 8);

    private static final EnumProperty<SucculentType> SOUTH_EAST = EnumProperty.create("south_east", SucculentType.class);
    private static final VoxelShape SOUTH_EAST_SHAPE = box(8, 0, 8, 16, 5, 16);

    private static final EnumProperty<SucculentType> SOUTH_WEST = EnumProperty.create("south_west", SucculentType.class);
    private static final VoxelShape SOUTH_WEST_SHAPE = box(0, 0, 8, 8, 5, 16);

    private static final EnumProperty<SucculentType> NORTH_WEST = EnumProperty.create("north_west", SucculentType.class);
    private static final VoxelShape NORTH_WEST_SHAPE = box(0, 0, 0, 8, 5, 8);
    public static final List<EnumProperty<SucculentType>> TYPES = List.of(NORTH_EAST, SOUTH_EAST, SOUTH_WEST, NORTH_WEST);
    public static final List<VoxelShape> SHAPES = List.of(NORTH_EAST_SHAPE, SOUTH_EAST_SHAPE, SOUTH_WEST_SHAPE, NORTH_WEST_SHAPE);

}
