package party.lemons.biomemakeover.mixin.client;

import net.minecraft.client.particle.Particle;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.particle.ParticleEffect;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import party.lemons.biomemakeover.util.access.WorldRendererAccess;

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin implements WorldRendererAccess
{
	@Shadow
	private int cameraChunkX;

	@Shadow
	protected abstract Particle spawnParticle(ParticleEffect parameters, boolean alwaysSpawn, boolean canSpawnOnMinimal, double x, double y, double z, double velocityX, double velocityY, double velocityZ);

	@Override
	public Particle bm_createParticle(ParticleEffect parameters, boolean alwaysSpawn, boolean canSpawnOnMinimal, double x, double y, double z, double velocityX, double velocityY, double velocityZ)
	{
		return spawnParticle(parameters, alwaysSpawn, canSpawnOnMinimal, x, y, z, velocityX, velocityY, velocityZ);
	}
}
