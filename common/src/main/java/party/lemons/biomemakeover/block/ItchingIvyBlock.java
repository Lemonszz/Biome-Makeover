package party.lemons.biomemakeover.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import party.lemons.biomemakeover.init.BMBlocks;

import java.util.Random;

public class ItchingIvyBlock extends IvyBlock implements BonemealableBlock {

    public ItchingIvyBlock(Properties settings) {
        super(settings);
    }

    @Override
    protected boolean doesScheduleAfterSet()
    {
        return true;
    }

    @Override
    public boolean isValidBonemealTarget(BlockGetter blockGetter, BlockPos blockPos, BlockState state, boolean bl) {
        return state.getValue(IvyShapedBlock.getPropertyForDirection(Direction.DOWN));
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource random, BlockPos blockPos, BlockState blockState) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource random, BlockPos pos, BlockState blockState) {
        level.setBlock(pos, ((MothBlossomBlock)BMBlocks.MOTH_BLOSSOM).getGrowState(level, pos), 3);
    }
}
