package party.lemons.biomemakeover.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.ParticleOptions;
import party.lemons.biomemakeover.util.access.LevelRendererAccess;

public class ClientUtil
{
    public static Particle spawnParticle(ParticleOptions parameters, boolean alwaysSpawn, boolean canSpawnOnMinimal, double x, double y, double z, double velocityX, double velocityY, double velocityZ)
    {
        return ((LevelRendererAccess) Minecraft.getInstance().levelRenderer).bm_createParticle(parameters, alwaysSpawn, canSpawnOnMinimal, x, y, z, velocityX, velocityY, velocityZ);
    }
}
