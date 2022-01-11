package party.lemons.biomemakeover.util.access;

import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.ParticleOptions;

public interface LevelRendererAccess {
    Particle bm_createParticle(ParticleOptions parameters, boolean alwaysSpawn, boolean canSpawnOnMinimal, double x, double y, double z, double velocityX, double velocityY, double velocityZ);

}
