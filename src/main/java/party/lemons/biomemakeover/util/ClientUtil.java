package party.lemons.biomemakeover.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.particle.ParticleEffect;
import party.lemons.biomemakeover.util.access.WorldRendererAccess;

public class ClientUtil
{
	public static Particle spawnParticle(ParticleEffect parameters, boolean alwaysSpawn, boolean canSpawnOnMinimal, double x, double y, double z, double velocityX, double velocityY, double velocityZ)
	{
		return ((WorldRendererAccess) MinecraftClient.getInstance().worldRenderer).bm_createParticle(parameters, alwaysSpawn, canSpawnOnMinimal, x, y, z, velocityX, velocityY, velocityZ);
	}

	private ClientUtil()
	{
	}
}
