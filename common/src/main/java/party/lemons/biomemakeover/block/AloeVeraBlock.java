package party.lemons.biomemakeover.block;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.taniwha.block.types.TBushBlock;

import static net.minecraft.world.level.block.state.properties.BlockStateProperties.AGE_4;

public class AloeVeraBlock extends TBushBlock implements BonemealableBlock
{
    private static final IntegerProperty AGE = AGE_4;
    private static final int MAX_AGE = 4;
    private static final float GROW_SPEED = 2.0F;

    public AloeVeraBlock(Properties properties) {
        super(properties);

        registerDefaultState(getStateDefinition().any().setValue(AGE, 0));
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.getRawBrightness(pos, 0) >= 9) {
            int i = this.getAge(state);
            if (i < this.getMaxAge()) {
                if(onPreGrow(level, pos, state, random.nextInt((int)(25.0F / GROW_SPEED) + 1) == 0))
                {
                    grow(level, pos, state, 1);
                    onPostGrow(level, pos, state);
                }
            }
        }
    }
    @Override
    public void performBonemeal(ServerLevel serverLevel, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        grow(serverLevel, blockPos, blockState, Mth.nextInt(randomSource, 1, 2));
    }

    private void grow(ServerLevel level, BlockPos pos, BlockState state, int growAmount)
    {
        int newAge = Math.min(getMaxAge(), getAge(state) + growAmount);
        level.setBlock(pos, state.setValue(AGE, newAge), Block.UPDATE_CLIENTS);
    }

    @ExpectPlatform
    private static boolean onPreGrow(ServerLevel level, BlockPos pos, BlockState state, boolean isGrowing)
    {
        throw new AssertionError();
    }

    @ExpectPlatform
    private static void onPostGrow(ServerLevel level, BlockPos pos, BlockState state) {
        throw new AssertionError();
    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState) {
        return blockState.getValue(AGE) != MAX_AGE;
    }

    public int getMaxAge()
    {
        return MAX_AGE;
    }

    public int getAge(BlockState state)
    {
        return state.getValue(AGE_4);
    }

    public boolean isMaxAge(BlockState state)
    {
        return getAge(state) == getMaxAge();
    }

    @Override
    protected boolean mayPlaceOn(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return blockState.is(BMBlocks.ALOE_VERA_PLANTABLE);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader levelReader, BlockPos blockPos, BlockState blockState, boolean bl) {
        return !isMaxAge(blockState);
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState blockState) {
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(AGE);
    }
}
