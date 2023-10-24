package party.lemons.biomemakeover.entity;

import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.InventoryCarrier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import party.lemons.biomemakeover.entity.behavior.DustDevilAI;

public class DustDevilEntity extends Monster implements InventoryCarrier
{
    private final SimpleContainer inventory = new SimpleContainer(1);
    public final AnimationState idleAnimation = new AnimationState();

    public DustDevilEntity(EntityType<? extends Monster> entityType, Level level)
    {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setCanPickUpLoot(this.canPickUpLoot());
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation flyingPathNavigation = new FlyingPathNavigation(this, level);
        flyingPathNavigation.setCanOpenDoors(false);
        flyingPathNavigation.setCanFloat(true);
        flyingPathNavigation.setCanPassDoors(true);
        return flyingPathNavigation;
    }

    @Override
    public void tick() {

        if(level().isClientSide()) {
            setupAnimations();
            spawnSprintParticle();
        }
        super.tick();
    }

    private void setupAnimations() {
        if(!idleAnimation.isStarted())
            idleAnimation.start(tickCount);
    }

    @Override
    protected void customServerAiStep() {
        ((Brain<DustDevilEntity>)this.getBrain()).tick((ServerLevel) level(), this);
        DustDevilAI.updateActivity(this);
        super.customServerAiStep();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10.0)
                .add(Attributes.FLYING_SPEED, 0.3F)
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .add(Attributes.ATTACK_DAMAGE, 2.0);
    }

    @Override
    protected Brain.Provider<DustDevilEntity> brainProvider() {
        return Brain.provider(DustDevilAI.MEMORY_TYPES, DustDevilAI.SENSOR_TYPES);
    }

    @Override
    protected Brain<DustDevilEntity> makeBrain(Dynamic<?> dynamic) {
        return DustDevilAI.makeBrain(this.brainProvider().makeBrain(dynamic));
    }

    @Override
    public boolean canPickUpLoot() {
        return super.canPickUpLoot(); //TODO: cooldowns n shit
    }
    @Override
    protected void playStepSound(BlockPos blockPos, BlockState blockState) {
    }

    @Override
    protected void checkFallDamage(double d, boolean bl, BlockState blockState, BlockPos blockPos) {
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        this.inventory.removeAllItems().forEach(this::spawnAtLocation);
        ItemStack itemStack = this.getItemBySlot(EquipmentSlot.MAINHAND);
        if (!itemStack.isEmpty() && !EnchantmentHelper.hasVanishingCurse(itemStack)) {
            this.spawnAtLocation(itemStack);
            this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean removeWhenFarAway(double d) {
        return inventory.isEmpty();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        this.writeInventoryToTag(compoundTag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.readInventoryFromTag(compoundTag);
    }

    @Override
    public SimpleContainer getInventory() {
        return this.inventory;
    }
}
