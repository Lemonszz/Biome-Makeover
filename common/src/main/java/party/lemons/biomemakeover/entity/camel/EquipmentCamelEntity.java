package party.lemons.biomemakeover.entity.camel;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class EquipmentCamelEntity extends Camel
{
    private static final EntityDataAccessor<ItemStack> DATA_EQUIPMENT_ITEM = SynchedEntityData.defineId(EquipmentCamelEntity.class, EntityDataSerializers.ITEM_STACK);

    public EquipmentCamelEntity(EntityType<? extends Camel> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_EQUIPMENT_ITEM, ItemStack.EMPTY);
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        if (!this.level().isClientSide)
        {
            this.spawnAtLocation(getEquipmentItem());
        }
    }

    @Override
    public void tick() {
        super.tick();

        if(!isSaddled()) {
            dropEquipment();

            CompoundTag tag = saveWithoutId(new CompoundTag());
            tag.remove("UUID");
            Camel regularCamel = EntityType.CAMEL.create(level());
            regularCamel.load(tag);
            level().addFreshEntity(regularCamel);
            playSound(SoundEvents.DONKEY_CHEST, 1.0F, (getRandom().nextFloat() - getRandom().nextFloat()) * 0.2F + 1.0F);

            for(Entity passenger : getPassengers())
            {
                passenger.stopRiding();
                passenger.startRiding(regularCamel);
            }

            setRemoved(RemovalReason.DISCARDED);
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.put("Equipment", getEquipmentItem().save(new CompoundTag()));
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        setEquipmentItem(ItemStack.of(compoundTag.getCompound("Equipment")));

        this.updateContainerEquipment();
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand interactionHand) {
        ItemStack itemStack = player.getItemInHand(interactionHand);
        if (player.isSecondaryUseActive() && !this.isBaby()) {
            this.openCustomInventoryScreen(player);
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else {
            InteractionResult interactionResult = itemStack.interactLivingEntity(player, this, interactionHand);
            if (interactionResult.consumesAction()) {
                return interactionResult;
            } else if (this.isFood(itemStack)) {
                return this.fedFood(player, itemStack);
            } else {
                if (this.getPassengers().size() < 1 && !this.isBaby()) {
                    this.doPlayerRide(player);
                }

                return InteractionResult.sidedSuccess(this.level().isClientSide);
            }
        }
    }

    public void setEquipmentItem(ItemStack itemStack) {
        getEntityData().set(DATA_EQUIPMENT_ITEM, itemStack);
    }

    public ItemStack getEquipmentItem()
    {
        return getEntityData().get(DATA_EQUIPMENT_ITEM);
    }

    @Nullable
    @Override
    public ItemStack getPickResult() {
        return new ItemStack(Items.CAMEL_SPAWN_EGG);
    }
}
