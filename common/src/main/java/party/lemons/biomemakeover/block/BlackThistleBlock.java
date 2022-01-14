package party.lemons.biomemakeover.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import party.lemons.biomemakeover.init.BMEntities;

public class BlackThistleBlock extends BMTallFlowerBlock{
    public BlackThistleBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos blockPos, Entity entity) {
        if (entity instanceof LivingEntity && entity.getType() != BMEntities.ROOTLING && entity.getType() != BMEntities.OWL && entity.getType() != EntityType.BEE) {
            if (!level.isClientSide() && state.getValue(HALF) == DoubleBlockHalf.UPPER  && !entity.isInvulnerableTo(DamageSource.WITHER)) {
                ((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 110, 0));
            }
        }
    }
}
