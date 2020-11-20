package party.lemons.biomemakeover.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.Item;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.init.BMNetwork;
import party.lemons.biomemakeover.util.EntityUtil;
import party.lemons.biomemakeover.util.boat.BoatType;
import party.lemons.biomemakeover.util.boat.BoatTypes;

public class BMBoatEntity extends BoatEntity
{
	private static final TrackedData<String> BOAT_TYPE = DataTracker.registerData(BMBoatEntity.class, TrackedDataHandlerRegistry.STRING);
	private static final String TAG_TYPE = "NewType";

	public BMBoatEntity(EntityType<? extends BoatEntity> entityType, World world)
	{
		super(entityType, world);
	}

	public BMBoatEntity(World world, double x, double y, double z)
	{
		this(BMEntities.BM_BOAT, world);
		this.updatePosition(x, y, z);
		this.setVelocity(Vec3d.ZERO);
		this.prevX = x;
		this.prevY = y;
		this.prevZ = z;
	}

	public BMBoatEntity(BoatEntity boatEntity, BoatType type)
	{
		this(BMEntities.BM_BOAT, boatEntity.world);

		this.copyPositionAndRotation(boatEntity);
		setBoatType(type);
	}

	@Override
	protected void initDataTracker()
	{
		super.initDataTracker();
		this.dataTracker.startTracking(BOAT_TYPE, BiomeMakeover.ID("blighted_balsa").toString());
	}

	public void setBoatType(BoatType type)
	{
		this.getDataTracker().set(BOAT_TYPE, type.id.toString());
	}

	public BoatType getNewBoatType()
	{
		return BoatTypes.REGISTRY.get(new Identifier(this.getDataTracker().get(BOAT_TYPE)));
	}

	@Override
	public Item asItem()
	{
		return getNewBoatType().item.get().asItem();
	}

	@Override
	protected void writeCustomDataToTag(CompoundTag tag)
	{
		tag.putString(TAG_TYPE, this.getNewBoatType().id.toString());
	}

	@Override
	protected void readCustomDataFromTag(CompoundTag tag)
	{
		if (tag.contains(TAG_TYPE, 8))
		{
			this.setBoatType(BoatTypes.REGISTRY.get(new Identifier(tag.getString(TAG_TYPE))));
		}
	}

	/*
	@Override
	public ActionResult interact(PlayerEntity player, Hand hand)
	{
		if(player.shouldCancelInteraction()) return ActionResult.PASS;
		return super.interact(player, hand);
	}*/

	/*
	@Override
	protected boolean canAddPassenger(Entity passenger)
	{
		return this.getPassengerList().size() < 2 && !this.isSubmergedIn(FluidTags.WATER);
	}

	@Override
	public void updatePassengerPosition(Entity passenger)
	{
		if(this.hasPassenger(passenger))
		{
			float offset = 0.0F;
			float yOffset = (float)((this.removed ? 0.01F : this.getMountedHeightOffset()) + passenger.getHeightOffset());

			if(this.getPassengerList().size() > 1)
			{
				int i = this.getPassengerList().indexOf(passenger);
				offset = i == 0 ? 0.2F : -0.6F;

				if(passenger instanceof AnimalEntity)
				{
					offset += 0.2F;
				}
			}

			Vec3d offsetDir = (new Vec3d(offset, 0.0D, 0.0D)).rotateY(-this.yaw * 0.017453292F - 1.5707964F);
			passenger.updatePosition(this.getX() + offsetDir.x, this.getY() + (double)yOffset, this.getZ() + offsetDir.z);
			passenger.yaw += ((BoatAccessor)this).getYawVelocity();
			passenger.setHeadYaw(passenger.getHeadYaw() + ((BoatAccessor)this).getYawVelocity());
			this.copyEntityData(passenger);

			if (passenger instanceof AnimalEntity && this.getPassengerList().size() > 1) {
				int animalLook = passenger.getEntityId() % 2 == 0 ? 90 : 270;
				passenger.setYaw(((AnimalEntity)passenger).bodyYaw + (float)animalLook);
				passenger.setHeadYaw(passenger.getHeadYaw() + (float)animalLook);
			}
		}
	}
*/
	@Override
	public Packet<?> createSpawnPacket()
	{
		return new CustomPayloadS2CPacket(BMNetwork.SPAWN_ENTITY, EntityUtil.WriteEntitySpawn(this));
	}
}