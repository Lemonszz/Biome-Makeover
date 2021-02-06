package party.lemons.biomemakeover.world.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.*;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

import static party.lemons.biomemakeover.util.RandomUtil.RANDOM;

public class LightningSparkParticle extends SpriteBillboardParticle
{
	private final SpriteProvider spriteProvider;

	private LightningSparkParticle(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider)
	{
		super(world, x, y, z, 0.5D - RANDOM.nextDouble(), velocityY, 0.5D - RANDOM.nextDouble());
		this.spriteProvider = spriteProvider;
		this.velocityY *= 0.20000000298023224D;
		if(velocityX == 0.0D && velocityZ == 0.0D)
		{
			this.velocityX *= 0.10000000149011612D;
			this.velocityZ *= 0.10000000149011612D;
		}

		this.scale *= 0.75F;
		this.maxAge = (int) (8.0D / (Math.random() * 0.8D + 0.2D));
		this.collidesWithWorld = false;
		this.setSpriteForAge(spriteProvider);
	}

	public ParticleTextureSheet getType()
	{
		return ParticleTextureSheet.PARTICLE_SHEET_TRANSLUCENT;
	}

	public void tick()
	{
		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;
		if(this.age++ >= this.maxAge)
		{
			this.markDead();
		}else
		{
			this.setSpriteForAge(this.spriteProvider);
			this.velocityY += 0.004D;
			this.move(this.velocityX, this.velocityY, this.velocityZ);
			if(this.y == this.prevPosY)
			{
				this.velocityX *= 1.1D;
				this.velocityZ *= 1.1D;
			}

			this.velocityX *= 0.9599999785423279D;
			this.velocityY *= 0.9599999785423279D;
			this.velocityZ *= 0.9599999785423279D;
			if(this.onGround)
			{
				this.velocityX *= 0.699999988079071D;
				this.velocityZ *= 0.699999988079071D;
			}

		}
	}

	@Environment(EnvType.CLIENT)
	public static class Factory implements ParticleFactory<DefaultParticleType>
	{
		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider)
		{
			this.spriteProvider = spriteProvider;
		}

		public Particle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double d, double e, double f, double g, double h, double i)
		{
			LightningSparkParticle soulParticle = new LightningSparkParticle(clientWorld, d, e, f, g, h, i, this.spriteProvider);
			soulParticle.setColorAlpha(1.0F);
			return soulParticle;
		}
	}
}
