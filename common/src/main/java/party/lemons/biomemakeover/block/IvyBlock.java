package party.lemons.biomemakeover.block;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import party.lemons.biomemakeover.init.BMBlocks;

import java.util.Random;

public class IvyBlock extends IvyShapedBlock
{
    public IvyBlock(Properties settings) {
        super(settings);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        Pair<Integer, Integer> nearbyCount = getNearbyIvyCount(level, pos);
        if(nearbyCount.getFirst() >= 6 && nearbyCount.getSecond() >= 1) return;

        Direction checkDirection = getRandomStateSide(state, random);
        if(checkDirection == null) return;

        Direction direction = Direction.getRandom(random);
        while(direction == checkDirection.getOpposite()) direction = Direction.getRandom(random);

        BlockPos offsetPos = pos.relative(direction);
        BlockState offsetState = level.getBlockState(offsetPos);
        BooleanProperty dirProperty = getPropertyForDirection(direction);

        if(!state.getValue(dirProperty) && isValidPlaceFace(level, direction, offsetPos, offsetState))
        {
            if(hasAdjacentSide(direction, state))
                level.setBlock(pos, state.setValue(dirProperty, true), 3);
        }else if(hasAdjacentSide(direction, state) && canReplace(offsetState))
        {
            BlockPos hitPos = offsetPos.relative(checkDirection);
            BlockState hitState = level.getBlockState(hitPos);
            if(isValidPlaceFace(level, direction, hitPos, hitState))
            {
                placeOrAddTo(level, offsetPos, checkDirection);
                return;
            }

            BlockPos creepPos = pos.relative(direction).relative(checkDirection);
            BlockState creepState = level.getBlockState(creepPos);
            if(canReplace(creepState))
            {
                hitPos = pos.relative(checkDirection);
                hitState = level.getBlockState(hitPos);
                if(isValidPlaceFace(level, direction, hitPos, hitState))
                {
                    placeOrAddTo(level, creepPos, direction.getOpposite());
                }
            }
        }
    }

    private void placeOrAddTo(Level world, BlockPos pos, Direction direction)
    {
        BlockState state = world.getBlockState(pos);

        if(state.is(this))
        {
            world.setBlock(pos, state.setValue(getPropertyForDirection(direction), true), 1);
        }
        else if(canReplace(state))
        {
            world.setBlock(pos, defaultBlockState().setValue(getPropertyForDirection(direction), true), 3);
            if(doesScheduleAfterSet())
                world.scheduleTick(pos, this, getScheduleDelay());
        }
    }
    protected boolean doesScheduleAfterSet()
    {
        return false;
    }

    protected int getScheduleDelay()
    {
        return 4;
    }

    private boolean canReplace(BlockState state)
    {
        return !state.is(BMBlocks.IVY_TAG) && (state.isAir() || state.is(this) || (state.canBeReplaced() && !state.liquid()));
    }

    private Pair<Integer, Integer> getNearbyIvyCount(Level world, BlockPos pos)
    {
        int distance = 3;
        int count = -1; //-1 because it counts itself and this was easier lol
        for(int x = -distance; x < distance; x++)
        {
            for(int z = -distance; z < distance; z++)
            {
                for(int y = -distance; y < distance; y++)
                {
                    if(world.getBlockState(pos.offset(x, y, z)).is(this)) count++;
                }
            }
        }
        int adjacent = 0;
        for(Direction dir : Direction.values())
            if(world.getBlockState(pos.relative(dir)).is(this)) adjacent++;


        return new Pair<>(count, adjacent);
    }

    @Override
    public boolean canBeReplaced(BlockState blockState, BlockPlaceContext blockPlaceContext) {
        return true;
    }
}
