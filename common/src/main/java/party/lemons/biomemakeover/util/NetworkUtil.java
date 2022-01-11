package party.lemons.biomemakeover.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import party.lemons.biomemakeover.network.S2C_DoEntityParticle;
import party.lemons.biomemakeover.network.S2C_DoLightningEntity;
import party.lemons.biomemakeover.network.S2C_DoLightningSplash;

public class NetworkUtil
{
    public static void doEntityParticle(Level world, ParticleOptions effect, Entity e, int count, float offset)
    {
        if(world.isClientSide()) return;

        new S2C_DoEntityParticle(e, effect, count, offset).sendToChunkListeners(e.getLevel().getChunkAt(e.getOnPos()));
    }

    public static void doLightningSplash(Level level, boolean doBottle, BlockPos pos)
    {
        if(level.isClientSide()) return;

        new S2C_DoLightningSplash(doBottle, pos).sendToChunkListeners(level.getChunkAt(pos));
    }

    public static void doLightningEntity(Level level, LivingEntity entity, int count)
    {
        if(level.isClientSide()) return;

        new S2C_DoLightningEntity(entity.getId(), count).sendToChunkListeners(level.getChunkAt(entity.getOnPos()));
    }
}
