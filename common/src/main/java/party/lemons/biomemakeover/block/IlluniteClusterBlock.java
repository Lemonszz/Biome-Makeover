package party.lemons.biomemakeover.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.AmethystClusterBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.taniwha.block.modifier.BlockModifier;
import party.lemons.taniwha.block.modifier.BlockWithModifiers;
import party.lemons.taniwha.registry.ModifierContainer;

import java.util.Locale;

public class IlluniteClusterBlock extends AmethystClusterBlock implements BlockWithModifiers<IlluniteClusterBlock>
{

    public static final EnumProperty<Type> TYPE = EnumProperty.create("type", Type.class);

    public IlluniteClusterBlock(Properties properties)
    {
        super(7, 3, properties.randomTicks().lightLevel((v)->{
            switch(v.getValue(TYPE))
            {
                case DAY:
                    return 2;
                case NIGHT:
                    return 15;
                case UNKNOWN:
                    return 2;
            }
            return 0;
        }));

        this.registerDefaultState(getStateDefinition().any().setValue(FACING, Direction.UP).setValue(TYPE, Type.DAY).setValue(WATERLOGGED, false));
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        Type expectedType = getType(level);
        if(state.getValue(TYPE) != expectedType)
        {
            level.setBlock(pos, state.setValue(TYPE, expectedType), 3);
        }
        level.scheduleTick(new BlockPos(pos), this, 20 + random.nextInt(150));
    }

    private Type getType(Level world)
    {
        if(world.dimensionType().hasFixedTime())
        {
            return Type.UNKNOWN;
        }
        else if(world.isNight())
        {
            return Type.NIGHT;
        }else
        {
            return Type.DAY;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(TYPE);
    }

    @Override
    public void randomTick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource random) {
        scheduleUpdates(serverLevel, blockPos, random);
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState blockState2, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos2) {

        scheduleUpdates(levelAccessor, blockPos, levelAccessor.getRandom());

        return super.updateShape(blockState, direction, blockState2, levelAccessor, blockPos, blockPos2);

    }

    public void scheduleUpdates(LevelAccessor world, BlockPos pos, RandomSource random)
    {
        if(!world.getBlockTicks().hasScheduledTick(pos, this))
            world.scheduleTick(new BlockPos(pos), this, 20 + random.nextInt(150));
    }

    @Override
    public void updateIndirectNeighbourShapes(BlockState blockState, LevelAccessor levelAccessor, BlockPos blockPos, int i, int j) {
        super.updateIndirectNeighbourShapes(blockState, levelAccessor, blockPos, i, j);
        scheduleUpdates(levelAccessor, blockPos, levelAccessor.getRandom());
    }

    @Override
    public void onProjectileHit(Level level, BlockState blockState, BlockHitResult blockHitResult, Projectile projectile) {
        if (!level.isClientSide) {
            BlockPos blockPos = blockHitResult.getBlockPos();
            level.playSound(null, blockPos, BMEffects.ILLUNITE_HIT.get(), SoundSource.BLOCKS, 1.0f, 0.5f + level.random.nextFloat() * 1.2f);
            level.playSound(null, blockPos, BMEffects.ILLUNITE_STEP.get(), SoundSource.BLOCKS, 1.0f, 0.5f + level.random.nextFloat() * 1.2f);
        }
    }

    private ModifierContainer<Block> modifierContainer;
    @Override
    public ModifierContainer<Block> getModifierContainer() {
        return modifierContainer;
    }

    @Override
    public IlluniteClusterBlock modifiers(BlockModifier... modifiers)
    {
        modifierContainer = new ModifierContainer<>(this, modifiers);
        return this;
    }

    public enum Type implements StringRepresentable
    {
        DAY, NIGHT, UNKNOWN;

        @Override
        public String getSerializedName() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }
}
