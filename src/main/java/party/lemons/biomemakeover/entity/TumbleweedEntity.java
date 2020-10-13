package party.lemons.biomemakeover.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
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

	public TumbleweedEntity(World world)
	{
		super(BMEntities.TUMBLEWEED, world);
		windOffset = 1F - (world.getRandom().nextFloat() / 3);
	}

	@Override
	protected void initDataTracker()
	{

	}

	@Override
	public void tick()
	{
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
			vX = WindSystem.windX * windOffset;
			vY = getVelocity().getY();
			vZ = WindSystem.windZ * windOffset;

			if(onGround)
			{
				vY = Math.abs(prevVelocity.y) * 0.75D;
				if(vY < 0.1F) vY = 0.4F;
			}
		}
		else
		{
			if(onGround)
			{
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
	protected void readCustomDataFromTag(CompoundTag tag)
	{

	}

	@Override
	protected void writeCustomDataToTag(CompoundTag tag)
	{

	}

	@Override
	public boolean isPushable()
	{
		return true;
	}

	public boolean collides() {
		return !this.removed;
	}

	public boolean method_30949(Entity entity) {
		return true;
	}


	@Override
	public Packet<?> createSpawnPacket()
	{
		return EntityUtil.createCustomSpawnPacket(this);
	}
}
