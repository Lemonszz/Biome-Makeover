package party.lemons.biomemakeover.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;

public class EntityPart<T extends Entity & MultiPartEntity> extends Entity
{
	public final T owner;
	private final EntityDimensions partDimensions;
	private double offsetX, offsetY, offsetZ;

	public EntityPart(T owner, float width, float height, double offsetX, double offsetY, double offsetZ) {
		super(owner.getType(), owner.world);
		this.partDimensions = EntityDimensions.changing(width, height);
		this.calculateDimensions();
		this.owner = owner;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
	}

	@Override
	protected void initDataTracker() {
	}

	@Override
	protected void readCustomDataFromTag(CompoundTag tag) {
	}

	@Override
	protected void writeCustomDataToTag(CompoundTag tag) {
	}

	@Override
	public boolean collides() {
		return true;
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		return !this.isInvulnerableTo(source) && this.owner.damagePart(this, source, amount);
	}

	@Override
	public boolean isPartOf(Entity entity) {
		return this == entity || this.owner == entity;
	}

	@Override
	public Packet<?> createSpawnPacket() {
		throw new UnsupportedOperationException();
	}

	@Override
	public EntityDimensions getDimensions(EntityPose pose) {
		return this.partDimensions;
	}

	public void updatePartPosition()
	{
		updatePosition(owner.getX() + offsetX, owner.getY() + offsetY, owner.getZ() + offsetZ);
	}
}
