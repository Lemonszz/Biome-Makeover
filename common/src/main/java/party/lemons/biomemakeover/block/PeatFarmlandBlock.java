package party.lemons.biomemakeover.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.taniwha.block.types.TFarmBlock;

import java.util.Iterator;

public class PeatFarmlandBlock extends TFarmBlock
{
    public  PeatFarmlandBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void tick(BlockState blockState, ServerLevel serverLevel, BlockPos blockPos, RandomSource random) {
        if(!blockState.canSurvive(serverLevel, blockPos))
        {
            setToPeat(blockState, serverLevel, blockPos);
        }
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int i = state.getValue(MOISTURE);
        if(!isWaterNearby(level, pos) && !level.isRainingAt(pos.above()))
        {
            if(i > 0)
            {
                level.setBlock(pos, state.setValue(MOISTURE, i - 1), 2);
            }else if(!hasCrop(level, pos))
            {
                setToPeat(state, level, pos);
            }
        }else if(i < 7)
        {
            level.setBlock(pos, state.setValue(MOISTURE, 7), 2);
        }

        BlockState aboveState = level.getBlockState(pos.above());
        if(aboveState.isRandomlyTicking())
        {
            aboveState.randomTick(level, pos.above(), random);
        }
    }

    @Override
    public void fallOn(Level level, BlockState blockState, BlockPos pos, Entity entity, float distance) {
        if(!level.isClientSide() && level.random.nextFloat() < distance - 0.5F && entity instanceof LivingEntity && (entity instanceof Player || level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) && entity.getBbWidth() * entity.getBbWidth() * entity.getBbHeight() > 0.512F)
        {
            setToPeat(level.getBlockState(pos), level, pos);
        }
        entity.causeFallDamage(distance, 1.0F, DamageSource.FALL);
    }

    public static void setToPeat(BlockState state, Level world, BlockPos pos)
    {
        world.setBlock(pos, pushEntitiesUp(state, BMBlocks.PEAT.get().defaultBlockState(), world, pos), 3);
    }

    private static boolean isWaterNearby(Level world, BlockPos pos) {
        Iterator<BlockPos> var2 = BlockPos.betweenClosed(pos.offset(-4, 0, -4), pos.offset(4, 1, 4)).iterator();

        BlockPos blockPos;
        do {
            if (!var2.hasNext()) {
                return false;
            }

            blockPos = var2.next();
        } while(!world.getFluidState(blockPos).is(FluidTags.WATER));

        return true;
    }

    private static boolean hasCrop(Level world, BlockPos pos) {
        Block block = world.getBlockState(pos.above()).getBlock();
        return block instanceof CropBlock || block instanceof StemBlock || block instanceof AttachedStemBlock;
    }
}
