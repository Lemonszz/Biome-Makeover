package party.lemons.biomemakeover.util;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import party.lemons.biomemakeover.init.BMEntities;

public final class EntityUtil
{

    private EntityUtil()
    {
    }

    public static boolean attemptProjectileResistanceBlock(LivingEntity entity, DamageSource source)
    {
        if (source.is(DamageTypeTags.IS_PROJECTILE)) {
            double protection = entity.getAttribute(BMEntities.ATT_PROJECTILE_RESISTANCE).getValue();
            if (protection > 0D && (entity.getRandom().nextDouble() * 30D) < protection) {
                entity.playSound(SoundEvents.SHIELD_BLOCK, 1F, 0.8F + entity.getRandom().nextFloat() * 0.4F);
                return true;
            }
        }

        return false;
    }
}
