package party.lemons.biomemakeover.entity;

import com.mojang.serialization.Dynamic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
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
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.entity.behavior.DustDevilAI;
import party.lemons.biomemakeover.init.BMEffects;

public class DustDevilEntity extends Monster implements InventoryCarrier
{
    private static final EntityDataAccessor<Boolean> IS_TORNADO = SynchedEntityData.defineId(DustDevilEntity.class, EntityDataSerializers.BOOLEAN);
    private static final int ANIM_CHARGE_TIME = 78;

    public boolean hasPlayedLoop = false;
    private final SimpleContainer inventory = new SimpleContainer(1);
    public final AnimationState idleAnimation = new AnimationState();
    public final AnimationState tornadoStartAnimation = new AnimationState();
    private int tornadoStartTime = 0;
    public final AnimationState tornadoAnimation = new AnimationState();


    public DustDevilEntity(EntityType<? extends Monster> entityType, Level level)
    {
        super(entityType, level);
        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setCanPickUpLoot(this.canPickUpLoot());
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(IS_TORNADO, false);
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
            spawnTornadoParticles();
        }
        super.tick();
    }

    private void setupAnimations() {
        boolean isTornado = isTornado();

        if(!isTornado && !idleAnimation.isStarted())
            idleAnimation.start(tickCount);

        if(isTornado())
        {
            if(tornadoStartAnimation.isStarted()) {
                tornadoStartTime++;
                if (tornadoStartTime >= ANIM_CHARGE_TIME) {
                    tornadoStartTime = 0;
                    tornadoStartAnimation.stop();
                    tornadoAnimation.startIfStopped(tickCount);
                }
            }
        }
    }

    public void spawnTornadoParticles()
    {
        final int checkDepth = 3;

        BlockState blockState = Blocks.AIR.defaultBlockState();
        for(int i = 0; i < checkDepth; i++)
        {
            BlockPos belowPosition = this.getOnPos().below(i);
            blockState = this.level().getBlockState(belowPosition);
            if(blockState.getRenderShape() != RenderShape.INVISIBLE)
                break;
        }

        if (blockState.getRenderShape() != RenderShape.INVISIBLE)
        {
            boolean isTornado = isTornado();
            AABB bounds = isTornado ? this.getBoundingBox().inflate(0.25F) : this.getBoundingBox();
            int count = isTornado ? 5 : 1;

            Vec3 velocity = this.getDeltaMovement();
            BlockPos position = this.blockPosition();

            for(int i = 0; i < count; i++) {
                double xPosition = this.getX() + (this.random.nextDouble() - 0.5) * bounds.getXsize();
                double zPosition = this.getZ() + (this.random.nextDouble() - 0.5) * bounds.getZsize();
                double yPosition = this.getY() + (this.random.nextDouble() - 0.5) * bounds.getYsize();

            /*
            if (position.getX() != belowPosition.getX()) {
                xPosition = Mth.clamp(xPosition, belowPosition.getX(), (double)belowPosition.getX() + 1.0);
            }

            if (position.getZ() != belowPosition.getZ()) {
                zPosition = Mth.clamp(zPosition, (double)belowPosition.getZ(), (double)belowPosition.getZ() + 1.0);
            }
            */
                // +
                //   +
                final float angleSpeed = (random.nextFloat() * 360F) * 0.0125F;

                this.level().addParticle(
                        new BlockParticleOption(BMEffects.SIMPLE_BLOCK.get(), blockState),
                        xPosition, yPosition, zPosition,
                        -Mth.sin(angleSpeed) + (velocity.x * -4.0), 0.6F, Mth.cos(angleSpeed) +  (velocity.z * -4.0)
                );
            }
        }
    }

    @Override
    protected void customServerAiStep() {
        this.getBrain().tick((ServerLevel) level(), this);
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
    public Brain<DustDevilEntity> getBrain() {
        return (Brain<DustDevilEntity>) super.getBrain();
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
    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor) {
        super.onSyncedDataUpdated(entityDataAccessor);

        if(level().isClientSide() && IS_TORNADO.equals(entityDataAccessor))
        {
            if(getEntityData().get(IS_TORNADO))
            {
                tornadoStartTime = 0;
                tornadoStartAnimation.startIfStopped(tickCount);
                idleAnimation.stop();
            }
            else {
                tornadoStartAnimation.stop();
                tornadoAnimation.stop();
            }
        }
    }

    public boolean isTornado()
    {
        return getEntityData().get(IS_TORNADO);
    }

    public void setTornado(boolean tornado)
    {
        getEntityData().set(IS_TORNADO, tornado);
    }

    @Override
    public SimpleContainer getInventory() {
        return this.inventory;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return BMEffects.DUST_DEVIL_IDLE.get();
    }

    @Override
    protected SoundEvent getDeathSound() {
        return BMEffects.DUST_DEVIL_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return BMEffects.DUST_DEVIL_HURT.get();
    }

    protected float getSoundVolume() {
        return 0.4F;
    }
}
