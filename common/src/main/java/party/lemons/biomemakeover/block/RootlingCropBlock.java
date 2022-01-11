package party.lemons.biomemakeover.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import party.lemons.biomemakeover.block.modifier.BlockModifier;
import party.lemons.biomemakeover.block.modifier.BlockWithModifiers;
import party.lemons.biomemakeover.entity.RootlingEntity;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.init.BMItems;

import java.util.Random;

public class RootlingCropBlock extends BushBlock implements BonemealableBlock, BlockWithModifiers<RootlingCropBlock> {
    public static final IntegerProperty AGE_4 = IntegerProperty.create("age", 0, 4);
    private static final VoxelShape[] AGE_TO_SHAPE = new VoxelShape[]{Block.box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D), Block.box(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)};


    public RootlingCropBlock(BlockBehaviour.Properties properties) {
        super(properties);

        registerDefaultState(getStateDefinition().any().setValue(AGE_4, 0));
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return AGE_TO_SHAPE[blockState.getValue(AGE_4)];
    }

    @Override
    protected boolean mayPlaceOn(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos) {
        return blockState.is(Blocks.FARMLAND) || blockState.is(BMBlocks.PEAT_FARMLAND);
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newState, boolean bl) {
        super.onRemove(blockState, level, blockPos, newState, bl);

        if(!level.isClientSide() && newState.getBlock() == this && isMature(newState))
        {
            level.destroyBlock(blockPos, false);

            RootlingEntity rootling = BMEntities.ROOTLING.create(level);
            rootling.moveTo(blockPos.getX() + 0.5F, blockPos.getY(), blockPos.getZ() + 0.5F, level.random.nextFloat(), level.random.nextFloat());
            rootling.setDeltaMovement(0, 0.25F, 0);
            rootling.randomizeFlower();
            level.addFreshEntity(rootling);
        }
    }

    public boolean isMature(BlockState state)
    {
        return state.getValue(AGE_4) >= getMaxAge();
    }

    public int getMaxAge()
    {
        return 4;
    }

    @Override
    public boolean isRandomlyTicking(BlockState blockState) {
        return !this.isMature(blockState);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        if(level.getRawBrightness(pos, 0) >= 9)
        {
            int i = this.getAge(state);
            if(i < this.getMaxAge())
            {
                float f = getAvailableMoisture(this, level, pos);
                if(random.nextInt((int) (25.0F / f) + 1) == 0)
                {
                    level.setBlock(pos, this.withAge(i + 1), 2);
                }
            }
        }
    }

    protected int getAge(BlockState state)
    {
        return state.getValue(AGE_4);
    }

    public BlockState withAge(int age)
    {
        return this.defaultBlockState().setValue(AGE_4, age);
    }

    public void applyGrowth(Level world, BlockPos pos, BlockState state)
    {
        int i = this.getAge(state) + this.getGrowthAmount(world);
        int j = this.getMaxAge();
        if(i > j)
        {
            i = j;
        }

        world.setBlock(pos, this.withAge(i), 2);
    }

    protected int getGrowthAmount(Level world)
    {
        return Mth.nextInt(world.random, 1, 2);
    }

    protected static float getAvailableMoisture(Block block, Level world, BlockPos pos)
    {
        float f = 1.0F;
        BlockPos blockPos = pos.below();

        for(int i = -1; i <= 1; ++i)
        {
            for(int j = -1; j <= 1; ++j)
            {
                float g = 0.0F;
                BlockState blockState = world.getBlockState(blockPos.offset(i, 0, j));
                if(blockState.is(Blocks.FARMLAND) || blockState.is(BMBlocks.PEAT_FARMLAND))
                {
                    g = 1.0F;
                    if(blockState.getValue(FarmBlock.MOISTURE) > 0)
                    {
                        g = 3.0F;
                    }
                }

                if(i != 0 || j != 0)
                {
                    g /= 4.0F;
                }

                f += g;
            }
        }

        BlockPos blockPos2 = pos.north();
        BlockPos blockPos3 = pos.south();
        BlockPos blockPos4 = pos.west();
        BlockPos blockPos5 = pos.east();
        boolean bl = block == world.getBlockState(blockPos4).getBlock() || block == world.getBlockState(blockPos5).getBlock();
        boolean bl2 = block == world.getBlockState(blockPos2).getBlock() || block == world.getBlockState(blockPos3).getBlock();
        if(bl && bl2)
        {
            f /= 2.0F;
        }else
        {
            boolean bl3 = block == world.getBlockState(blockPos4.north()).getBlock() || block == world.getBlockState(blockPos5.north()).getBlock() || block == world.getBlockState(blockPos5.south()).getBlock() || block == world.getBlockState(blockPos4.south()).getBlock();
            if(bl3)
            {
                f /= 2.0F;
            }
        }

        return f;
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return (level.getRawBrightness(pos, 0) >= 8 || level.canSeeSky(pos)) && super.canSurvive(state, level, pos);
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if(entity instanceof Ravager && level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING))
        {
            level.destroyBlock(blockPos, true, entity);
        }

        super.entityInside(blockState, level, blockPos, entity);
    }

    /*
    protected ItemLike getSeedsItem()
    {return BMItems.ROOTLING_SEEDS;
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState) {
        return new ItemStack(this.getSeedsItem());
    }
*/
    @Override
    public boolean isValidBonemealTarget(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, boolean bl) {
        return !this.isMature(blockState);
    }

    @Override
    public boolean isBonemealSuccess(Level level, Random random, BlockPos blockPos, BlockState blockState) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel serverLevel, Random random, BlockPos blockPos, BlockState blockState) {
        this.applyGrowth(serverLevel, blockPos, blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE_4);
    }

    @Override
    public RootlingCropBlock modifiers(BlockModifier... modifiers) {
        BlockWithModifiers.init(this, modifiers);
        return this;
    }
}
