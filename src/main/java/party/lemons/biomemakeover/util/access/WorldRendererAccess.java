package party.lemons.biomemakeover.util.access;

import net.minecraft.client.particle.Particle;
import net.minecraft.particle.ParticleEffect;

public interface WorldRendererAccess
{
	Particle createParticle(ParticleEffect parameters, boolean alwaysSpawn, boolean canSpawnOnMinimal, double x, double y, double z, double velocityX, double velocityY, double velocityZ);
}