package party.lemons.biomemakeover.world.particle;

import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleTextureSheet;
import net.minecraft.client.particle.SpriteBillboardParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.DefaultParticleType;

public class TeleportParticle extends SpriteBillboardParticle
{
	private final double startX;
	private final double startY;
	private final double startZ;
	protected double angle;
	protected double rotateSpeed;

	protected TeleportParticle(ClientWorld clientWorld, double x, double y, double z, double velX, double velY, double velZ) {
		super(clientWorld, x, y, z);
		this.velocityX = velX;
		this.velocityY = velY;
		this.velocityZ = velZ;
		this.x = x;
		this.y = y;
		this.z = z;
		this.startX = this.x;
		this.startY = this.y;
		this.startZ = this.z;
		this.scale = 0.1F * (this.random.nextFloat() * 0.2F + 0.5F);
		float colorChange = this.random.nextFloat() * 0.6F + 0.4F;
		this.colorRed = colorChange * 0.9F;
		this.colorGreen = colorChange * 0.3F;
		this.colorBlue = colorChange;
		this.maxAge = 30 + ((int)(Math.random() * 10D));
		this.angle = Math.random() * 360D;
		this.rotateSpeed = Math.random() / 2;
	}

	public void tick()
	{
		this.prevPosX = this.x;
		this.prevPosY = this.y;
		this.prevPosZ = this.z;
		if (this.age++ >= this.maxAge)
		{
			this.markDead();
		}
		else
		{
			this.angle += rotateSpeed;
			double offset = age / 30F;
			x = (Math.cos(angle) / offset) + startX;
			z = (Math.sin(angle) / offset) + startZ;
			y = startY + Math.sin((age + rotateSpeed) / 10F) / 3F;
		}
	}

	@Override
	public ParticleTextureSheet getType()
	{
		return ParticleTextureSheet.PARTICLE_SHEET_OPAQUE;
	}

	public static class Factory implements ParticleFactory<DefaultParticleType>
	{
		private final SpriteProvider sprite;

		public Factory(SpriteProvider spriteProvider) {
			this.sprite = spriteProvider;
		}
		public TeleportParticle createParticle(DefaultParticleType defaultParticleType, ClientWorld clientWorld, double x, double y, double z, double vX, double vY, double vZ) {
			TeleportParticle p =  new TeleportParticle(clientWorld, x, y, z, vX, vY, vZ);
			p.setSprite(sprite);
			return p;
		}
	}
}
