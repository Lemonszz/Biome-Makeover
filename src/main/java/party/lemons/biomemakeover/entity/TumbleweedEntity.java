package party.lemons.biomemakeover.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.util.EntityUtil;
import party.lemons.biomemakeover.world.WindSystem;

public class TumbleweedEntity extends Entity
{
	public Quaternion quaternion = new Quaternion(0,0,0,1);
	public Quaternion prevQuaternion = new Quaternion(0,0,0,1);
	private Vec3d prevVelocity;
	private float xRot = 0;
	private float zRot = 0;
	private double disX = 0;
	private double disZ = 0;
	private float windOffset = 0;
	private float acceleration = 0.0025F;
	private float age = 0;

	public TumbleweedEntity(World world)
	{
		super(BMEntities.TUMBLEWEED, world);
		windOffset = 1F - (world.getRandom().nextFloat() / 3);
	}

	@Override
	public void tick()
	{
		age++;
		if(age > 1500)
			kill();

		if(!isTouchingWater())
			this.setVelocity(this.getVelocity().add(0.0D, -0.04D, 0.0D));

		super.tick();

		prevQuaternion = new Quaternion(quaternion);
		prevVelocity = new Vec3d(getVelocity().x, getVelocity().y, getVelocity().z);

		double pX = getX();
		double pZ = getZ();

		this.move(MovementType.SELF, getVelocity());

		double fX = getX();
		double fZ = getZ();
		disX = fX - pX;
		disZ = fZ - pZ;

		double vX = 0, vY = 0, vZ = 0;

		if(!world.isClient())
		{
			vX = step(getVelocity().getX(), (WindSystem.windX * windOffset), acceleration);
			vZ = step(getVelocity().getZ(), (WindSystem.windZ * windOffset), acceleration);
			vY = getVelocity().getY();

			if(onGround)
			{
				vY = MathHelper.clamp(Math.abs(prevVelocity.y) * 0.75D, 0.31F, 2);
				this.playSound(BMEffects.TUMBLEWEED_TUMBLE, 0.25F, 1.0F);
			}

			if(isTouchingWater())
			{
				vX *= 0.75F;
				vZ *= 0.75F;
				vY = 0.1F;
			}
		}
		else
		{
			if(onGround)
			{
				makeParticles(15);
				xRot = (float) -(disX / 0.25D);
				zRot = (float) (disZ / 0.25D);
			}
			else
			{
				xRot = (float) -(disX / 0.6D);
				zRot = (float) (disZ / 0.6D);
			}

			Quaternion rot = new Quaternion(zRot,0F, xRot, false);
			rot.hamiltonProduct(quaternion);
			quaternion = rot;
		}

		if(!world.isClient)
		{
			setVelocity(vX, vY, vZ);
			velocityDirty = true;
			velocityModified = true;
		}
	}

	@Override
	public void kill()
	{
		makeParticles(30);
		super.kill();
	}

	@Override
	protected void initDataTracker()
	{

	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		if(source != DamageSource.CACTUS)
		{
			this.playSound(BMEffects.TUMBLEWEED_BREAK, 0.25F, 1.0F);
			kill();
		}

		return true;
	}

	private void makeParticles(int count)
	{
		for(int i = 0; i < count; i++)
		{
			world.addParticle(new BlockStateParticleEffect(ParticleTypes.BLOCK, BMBlocks.TUMBLEWEED.getDefaultState()), -0.5D + (getX() + random.nextDouble()), getY() + random.nextDouble(), -0.5D + (getZ() + random.nextDouble()), 0.0D, 0.0D, 0.0D);
		}
	}

	@Override
	public boolean isPushable()
	{
		return true;
	}

	@Override
	protected void readCustomDataFromTag(CompoundTag tag)
	{

	}

	@Override
	protected void writeCustomDataToTag(CompoundTag tag)
	{

	}

	@Override
	public boolean collides() {
		return !this.removed;
	}

	@Override
	public boolean collidesWith(Entity other)
	{
		return true;
	}

	private double step(double val, double target, double step)
	{
		if(val < target)
			return Math.min(val + step, target);
		else
			return Math.max(val - step, target);
	}

	@Override
	public Packet<?> createSpawnPacket()
	{
		return EntityUtil.createCustomSpawnPacket(this);
	}
}
