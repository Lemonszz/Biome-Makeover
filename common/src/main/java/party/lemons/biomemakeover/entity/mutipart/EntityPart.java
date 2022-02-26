package party.lemons.biomemakeover.entity.mutipart;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.util.extension.MultipartHolder;

public class EntityPart<T extends LivingEntity & MultiPartEntity> extends Entity
{
    public final T owner;
    private final EntityDimensions partDimensions;
    private final double offsetX;
    private final double offsetY;
    private final double offsetZ;
    private boolean collides = false;

    public EntityPart(T owner, float width, float height, double offsetX, double offsetY, double offsetZ) {
        super(owner.getType(), owner.getLevel());

        this.partDimensions = EntityDimensions.scalable(width, height);
        this.refreshDimensions();
        this.owner = owner;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
    }

    @Override
    public void tick() {
        super.tick();
        if(!owner.isAlive() && !isRemoved())
        {
            remove(Entity.RemovalReason.DISCARDED);
            if(level instanceof MultipartHolder && ((MultipartHolder)level).getPartMap().containsKey(getId()))
                ((MultipartHolder)level).getPartMap().remove(getId(), this);
        }
    }

    public EntityPart<T> collides()
    {
        this.collides = true;
        return this;
    }

    @Override
    public boolean canBeCollidedWith() {
        return collides;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void setCustomName(@Nullable Component component) {
        owner.setCustomName(component);
    }

    @Override
    public boolean hurt(DamageSource damageSource, float amount) {
        return !this.isInvulnerableTo(damageSource) && this.owner.damagePart(this, damageSource, amount);
    }

    @Override
    public void setItemSlot(EquipmentSlot equipmentSlot, ItemStack itemStack) {
        owner.setItemSlot(equipmentSlot, itemStack);
    }

    @Override
    public InteractionResult interact(Player player, InteractionHand interactionHand) {
        return owner.interact(player, interactionHand);
    }

    @Override
    public boolean is(Entity entity) {
        return this == entity || this.owner == entity;
    }


    @Override
    public EntityDimensions getDimensions(Pose pose) {
        return this.partDimensions;
    }

    @Override
    public boolean shouldBeSaved() {
        return false;
    }

    public void updatePartPosition()
    {
        absMoveTo(owner.getX() + offsetX, owner.getY() + offsetY, owner.getZ() + offsetZ);
    }

    public boolean isPickable() {
        return true;
    }

    @Override
    protected void defineSynchedData() {

    }


    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    public Packet<?> getAddEntityPacket() {
        throw new UnsupportedOperationException();
    }
}
