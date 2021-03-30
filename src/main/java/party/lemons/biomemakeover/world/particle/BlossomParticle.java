package party.lemons.biomemakeover.world.particle;

import net.minecraft.client.particle.AbstractSlowingParticle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class BlossomParticle extends AbstractSlowingParticle
{
	private final SpriteProvider spriteProvider;

	private BlossomParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider)
	{
		super(world, x, y, z, velocityX, velocityY, velocityZ);
		this.spriteProvider = spriteProvider;
		this.scale(1);
		this.setColorAlpha(0.8F);
		this.setSpriteForAge(spriteProvider);
	}

	public ParticleTextureSheet getType()
	{
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	public void tick()
	{
		super.tick();
		if(!this.dead)
		{
			this.setSpriteForAge(this.spriteProvider);
		}
	}

	public static class Factory implements ParticleFactory<DefaultParticleType> {
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}

		public BlossomParticle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i) {
			BlossomParticle soulParticle = new BlossomParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
			soulParticle.setColorAlpha(1.0F);
			return soulParticle;
		}
	}
}