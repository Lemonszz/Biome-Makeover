package party.lemons.biomemakeover.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Packet;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import org.jetbrains.annotations.Nullable;

public class EntityPart<T extends LivingEntity & MultiPartEntity> extends Entity
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
	public boolean collides() {
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
	public void setCustomName(@Nullable Text name)
	{
		owner.setCustomName(name);
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		return !this.isInvulnerableTo(source) && this.owner.damagePart(this, source, amount);
	}

	@Override
	public void equipStack(EquipmentSlot slot, ItemStack stack)
	{
		owner.equipStack(slot, stack);
	}

	@Override
	public ActionResult interact(PlayerEntity player, Hand hand)
	{
		return owner.interact(player, hand);
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
