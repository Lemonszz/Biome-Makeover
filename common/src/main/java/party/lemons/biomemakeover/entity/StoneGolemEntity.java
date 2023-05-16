package party.lemons.biomemakeover.entity;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.AbstractIllager;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ProjectileWeaponItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.entity.ai.BetterCrossbowAttackGoal;
import party.lemons.biomemakeover.entity.ai.EmptyMobNavigation;
import party.lemons.biomemakeover.entity.mutipart.EntityPart;
import party.lemons.biomemakeover.entity.mutipart.MultiPartEntity;
import party.lemons.biomemakeover.init.BMAdvancements;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.init.BMItems;
import party.lemons.biomemakeover.util.sound.StoneGolemTurnSoundInstance;
import party.lemons.taniwha.entity.golem.PlayerCreatable;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class StoneGolemEntity extends AbstractGolem implements CrossbowAttackMob, NeutralMob, MultiPartEntity<EntityPart<StoneGolemEntity>>, PlayerCreatable
{
    private static final EntityDataAccessor<Boolean> CHARGING = SynchedEntityData.defineId(StoneGolemEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> PLAYER_CREATED = SynchedEntityData.defineId(StoneGolemEntity.class, EntityDataSerializers.BOOLEAN);
    private static final UniformInt ANGER_TIME_RANGE = TimeUtil.rangeOfSeconds(20, 39);;

    private int angerTime;
    private int holdCooldown = 0;
    private UUID angryAt;
    private EntityPart<StoneGolemEntity> BODY = new EntityPart<>(this, 1.6F, 2F, 0, 0.5F, 0);
    private EntityPart<StoneGolemEntity> BASE = new EntityPart<>(this, 0.875F, 0.5F, 0, 0.0F, 0).collides();
    private List<EntityPart<StoneGolemEntity>> parts = Lists.newArrayList(BODY, BASE);

    public StoneGolemEntity(EntityType<? extends AbstractGolem> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.goalSelector.addGoal(1, new BetterCrossbowAttackGoal<>(this, 1.0D, 24.0F));
        this.goalSelector.addGoal(2, new LookAtPlayerGoal(this, Player.class, 5.0F, 1.0F));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Mob.class, 5.0F));
        this.goalSelector.addGoal(4, new LookAtPlayerGoal(this, StoneGolemEntity.class, 10.0F));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, AbstractGolem.class, 5.0F));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, StoneGolemEntity.class, 10, true, false, living -> {
            if (living instanceof StoneGolemEntity other) {
                return isPlayerCreated() != other.isPlayerCreated();
            }
            return false;
        }));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, IronGolem.class, 10, true, false, (g) -> !isPlayerCreated()));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(6, new NearestAttackableTargetGoal<>(this, Mob.class, 5, false, false, (livingEntity) -> {
            return livingEntity instanceof Monster && !(livingEntity instanceof Creeper);
        }));
        this.targetSelector.addGoal(4, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(CHARGING, false);
        getEntityData().define(PLAYER_CREATED, false);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if(isPlayerCreated())
        {
            ItemStack playerStack = player.getItemInHand(hand);

            if(!playerStack.isEmpty() && playerStack.is(BMItems.HEALS_STONE_GOLEM))
            {
                float currentHealth = this.getHealth();
                this.heal(15.0F);
                if(this.getHealth() == currentHealth)
                {
                    return InteractionResult.sidedSuccess(this.level().isClientSide());
                }
                else
                {
                    float g = 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F;
                    this.playSound(SoundEvents.IRON_GOLEM_REPAIR, 1.0F, g);
                    if(!player.isCreative())
                    {
                        playerStack.shrink(1);
                    }

                    return InteractionResult.sidedSuccess(this.level().isClientSide());
                }
            }
            else if(isHolding() && holdCooldown <= 0)
            {
                if(!level().isClientSide()) {
                    player.getInventory().placeItemBackInInventory(getItemBySlot(EquipmentSlot.MAINHAND).copy());
                    setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                }
                return InteractionResult.SUCCESS;
            }
            else
            {
                if(!playerStack.isEmpty())
                {
                    if(playerStack.getItem() == Items.CROSSBOW)
                    {
                        if(!level().isClientSide())
                        {
                            ItemStack newStack = playerStack.copy();
                            newStack.setCount(1);
                            setItemSlot(EquipmentSlot.MAINHAND, newStack);
                            playerStack.shrink(1);
                            holdCooldown++;

                            BMAdvancements.ARM_GOLEM.trigger((ServerPlayer) player);
                        }
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void tick() {
        super.tick();
        holdCooldown = 0;

        if(yBodyRot != yBodyRotO && level().isClientSide())
        {
            playRotateSound();
        }
    }

    private boolean isHolding()
    {
        return !getItemBySlot(EquipmentSlot.MAINHAND).isEmpty();
    }

    @Override
    protected void populateDefaultEquipmentSlots(RandomSource randomSource, DifficultyInstance difficultyInstance)
    {
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {

        RandomSource randomSource = serverLevelAccessor.getRandom();

        this.populateDefaultEquipmentSlots(randomSource, difficultyInstance);
        this.populateDefaultEquipmentEnchantments(randomSource, difficultyInstance);
        return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
    }


    @Override
    public boolean isAngryAt(LivingEntity livingEntity) {
        if(EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingEntity) && !isPlayerCreated())
            return true;

        return NeutralMob.super.isAngryAt(livingEntity);
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        if(isPlayerCreated())
        {
            if(target instanceof Player || target instanceof AbstractVillager)
                return false;
        }
        else
        {
            if(target instanceof Monster)
                return false;
        }

        return super.canAttack(target);
    }

    public boolean isPlayerCreated()
    {
        return getEntityData().get(PLAYER_CREATED);
    }

    public void setPlayerCreated(boolean playerCreated)
    {
        getEntityData().set(PLAYER_CREATED, playerCreated);
    }

    @Override
    protected int decreaseAirSupply(int i) {
        return i;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("PlayerCreated", this.isPlayerCreated());
        this.addPersistentAngerSaveData(tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.setPlayerCreated(tag.getBoolean("PlayerCreated"));
        this.readPersistentAngerSaveData(this.level(), tag);
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return BMEffects.STONE_GOLEM_DEATH.get();
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source)
    {
        return BMEffects.STONE_GOLEM_HURT.get();
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new EmptyMobNavigation(this, level);
    }

    @Override
    protected BodyRotationControl createBodyControl() {
        return new StoneGolemBodyControl(this);
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return angerTime;
    }

    @Override
    public void setRemainingPersistentAngerTime(int i) {
        this.angerTime = i;

    }

    public boolean canCollideWith(Entity entity)
    {
        return false;
    }

    @Override
    public void push(double d, double e, double f) {

    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return angryAt;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID uUID) {
        this.angryAt = uUID;
    }

    @Override
    public void startPersistentAngerTimer() {
        setRemainingPersistentAngerTime(ANGER_TIME_RANGE.sample(random));
    }

    @Override
    public void setChargingCrossbow(boolean bl) {
        getEntityData().set(CHARGING, bl);
    }
    public boolean isChargingCrossbow() {
        return getEntityData().get(CHARGING);
    }

    @Override
    public void shootCrossbowProjectile(LivingEntity livingEntity, ItemStack itemStack, Projectile projectile, float f) {
        this.shootCrossbowProjectile(this, livingEntity, projectile, f,1.6F);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        updateParts();
    }

    @Override
    public void onClientRemoval()
    {
        removeParts();
    }

    @Override
    public void knockback(double d, double e, double f) {

    }

    @Override
    public int getMaxHeadYRot() {
        return 20;
    }

    @Override
    public int getMaxHeadXRot() {
        return 2;
    }

    @Override
    public boolean canFireProjectileWeapon(ProjectileWeaponItem projectileWeaponItem) {
        return projectileWeaponItem == Items.CROSSBOW;
    }

    @Override
    public void onCrossbowAttackPerformed() {

    }

    @Override
    public void performRangedAttack(LivingEntity livingEntity, float f) {
        this.performCrossbowAttack(this, f);
    }

    @Override
    public ItemStack getProjectile(ItemStack stack)
    {
        if (stack.getItem() instanceof ProjectileWeaponItem) {
            Predicate<ItemStack> predicate = ((ProjectileWeaponItem)stack.getItem()).getSupportedHeldProjectiles();
            ItemStack itemStack = ProjectileWeaponItem.getHeldProjectile(this, predicate);
            return itemStack.isEmpty() ? new ItemStack(Items.ARROW) : itemStack;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions entityDimensions) {
        return 2F;
    }

    @Override
    public boolean damagePart(EntityPart<StoneGolemEntity> part, DamageSource source, float amount) {
        return hurt(source, amount);
    }

    @Environment(EnvType.CLIENT)
    public AbstractIllager.IllagerArmPose getState() {
        if (this.isChargingCrossbow()) {
            return AbstractIllager.IllagerArmPose.CROSSBOW_CHARGE;
        } else if (this.isHolding(Items.CROSSBOW)) {
            return AbstractIllager.IllagerArmPose.CROSSBOW_HOLD;
        } else {
            return this.isAggressive() ?AbstractIllager.IllagerArmPose.ATTACKING : AbstractIllager.IllagerArmPose.NEUTRAL;
        }
    }

    @Environment(EnvType.CLIENT)
    private void playRotateSound()
    {
        boolean nulled = turnSound == null;
        if(nulled || ((StoneGolemTurnSoundInstance)turnSound).isStopped())
        {
            if(!nulled)
                Minecraft.getInstance().getSoundManager().stop(((StoneGolemTurnSoundInstance)turnSound));

            turnSound = new StoneGolemTurnSoundInstance(this, getRandom());
            Minecraft.getInstance().getSoundManager().play(((StoneGolemTurnSoundInstance)turnSound));
        }
        else if(!nulled)
            ((StoneGolemTurnSoundInstance)turnSound).tick();
    }

    private Object turnSound = null;

    public IronGolem.Crackiness getCrack() {
        return IronGolem.Crackiness.byFraction(this.getHealth() / this.getMaxHealth());
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        IronGolem.Crackiness crack = this.getCrack();
        boolean bl = super.hurt(source, amount);
        if (bl && this.getCrack() != crack) {
            this.playSound(SoundEvents.IRON_GOLEM_DAMAGE, 1.0F, 1.0F);
        }
        return bl;
    }

    @Override
    public double getPassengersRidingOffset() {
        return 2F;
    }

    @Override
    public List<EntityPart<StoneGolemEntity>> getParts() {
        return parts;
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 60);
    }

    private AABB cullBox = EntityDimensions.fixed(1.6F, 2.5F).makeBoundingBox(0,0,0);
    @Override
    public AABB getBoundingBoxForCulling() {
        return cullBox.move(position());
    }

    private static class StoneGolemBodyControl extends BodyRotationControl {
        private final Mob entity;
        private int activeTicks;

        public StoneGolemBodyControl(Mob mob) {
            super(mob);
            this.entity = mob;
        }

        @Override
        public void clientTick() {
            ++this.activeTicks;
            this.rotateBody();
            this.rotateLook();
        }

        private void rotateLook() {
            this.entity.setYBodyRot(Mth.rotateIfNecessary(this.entity.getYRot(), this.entity.getYHeadRot(), this.entity.getMaxHeadYRot()));
        }

        private void rotateBody() {
            int i = this.activeTicks - 10;
            float f = Mth.clamp((float)i / 10.0F, 0.0F, 1.0F);
            float g = (float)this.entity.getMaxHeadYRot() * (1.0F - f);
            this.entity.setYBodyRot(Mth.rotateIfNecessary(this.entity.getYRot(), this.entity.getYHeadRot(), g));
        }
    }
}
