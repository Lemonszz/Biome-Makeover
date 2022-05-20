package party.lemons.biomemakeover.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.animal.Turtle;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.monster.ZombifiedPiglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShieldItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.init.BMEnchantments;

import java.util.Random;

public class DecayedEntity extends Zombie
{
    private static final EntityDataAccessor<Boolean> SHIELD_DOWN = SynchedEntityData.defineId(DecayedEntity.class, EntityDataSerializers.BOOLEAN);

    private boolean targetingUnderwater;
    protected final WaterBoundPathNavigation waterNavigation;
    protected final GroundPathNavigation landNavigation;

    private int shieldDisableTime = 0;
    private int shieldHealth = 30;
    private boolean isDummy = false;

    public DecayedEntity(EntityType<? extends Zombie> entityType, Level level) {
        super(entityType, level);

        this.moveControl = new DecayedMoveControl(this);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.waterNavigation = new WaterBoundPathNavigation(this, level);
        this.landNavigation = new GroundPathNavigation(this, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
    }

    @Override
    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(1, new GoToWaterGoal(this, 1.0));
        this.goalSelector.addGoal(2, new ZombieAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(5, new LeaveWaterGoal(this, 1.0));
        this.goalSelector.addGoal(6, new SwimUpGoal(this, 1.0, this.level.getSeaLevel()));
        this.goalSelector.addGoal(7, new RandomStrollGoal(this, 1.0));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this, Drowned.class).setAlertOthers(ZombifiedPiglin.class));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (e) -> true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, AbstractVillager.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Axolotl.class, true, false));
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, Turtle.class, 10, true, false, Turtle.BABY_ON_LAND_SELECTOR));
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return Monster.createMonsterAttributes()
                .add(Attributes.FOLLOW_RANGE, 16D)
                .add(Attributes.MOVEMENT_SPEED, 0.23f)
                .add(Attributes.ATTACK_DAMAGE, 3.0)
                .add(Attributes.ARMOR, 2.0)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(SHIELD_DOWN, false);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();

        if(isAggressive() && !getEntityData().get(SHIELD_DOWN)) {
            startUsingItem(InteractionHand.OFF_HAND);
        }
        else stopUsingItem();

        if(!level.isClientSide())
        {
            if(shieldDisableTime > 0)
            {
                if(!getEntityData().get(SHIELD_DOWN))
                    getEntityData().set(SHIELD_DOWN, true);

                shieldDisableTime--;
                if(shieldDisableTime <= 0)
                {
                    getEntityData().set(SHIELD_DOWN, false);
                }
            }
        }
    }

    @Override
    protected void hurtCurrentlyUsedShield(float amount) {
        if(level.isClientSide() || !(this.useItem.getItem() instanceof ShieldItem)) return;

        shieldHealth -= amount;
        if(shieldHealth <= 0)
        {
            stopUsingItem();
            setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
            playSound(SoundEvents.SHIELD_BREAK, 0.8F, 0.8F + this.level.random.nextFloat() * 0.4F);
        }else
        {
            this.playSound(SoundEvents.SHIELD_BLOCK, 1.0F, 0.8F + this.level.random.nextFloat() * 0.4F);
            shieldDisableTime = Math.max((int) (13F + (amount * 3F)), shieldDisableTime);
        }
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType spawnReason, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        spawnGroupData = super.finalizeSpawn(level, difficulty, spawnReason, spawnGroupData, compoundTag);

        setLeftHanded(random.nextBoolean());
        if(this.random.nextFloat() < 0.15F * (difficulty.getSpecialMultiplier() + 1))
        {
            int armourLevel = 0;
            float stopChance = this.level.getDifficulty() == Difficulty.HARD ? 0.05F : 0.1F;
            if(this.random.nextFloat() < 0.2F)
            {
                ++armourLevel;
            }

            if(this.random.nextFloat() < 0.2F)
            {
                ++armourLevel;
            }

            if(this.random.nextFloat() < 0.2F)
            {
                ++armourLevel;
            }

            boolean stop = true;
            EquipmentSlot[] slots = EquipmentSlot.values();
            int length = slots.length;

            for(int j = 0; j < length; ++j)
            {
                EquipmentSlot equipmentSlot = slots[j];
                if(equipmentSlot.getType() == EquipmentSlot.Type.ARMOR)
                {
                    ItemStack itemStack = this.getItemBySlot(equipmentSlot);
                    if(!stop && this.random.nextFloat() < stopChance)
                    {
                        break;
                    }

                    stop = false;
                    if(itemStack.isEmpty())
                    {
                        Item item = getEquipmentForSlot(equipmentSlot, armourLevel);
                        if(item != null)
                        {
                            ItemStack stack = new ItemStack(item);

                            stack.enchant(BMEnchantments.DECAY_CURSE.get(), 1 + random.nextInt(4));
                            this.setItemSlot(equipmentSlot, stack);
                        }
                    }
                }
            }
        }

        ItemStack shield = new ItemStack(Items.SHIELD);
        shield.enchant(BMEnchantments.DECAY_CURSE.get(), 1 + random.nextInt(4));
        this.setItemSlot(EquipmentSlot.OFFHAND, shield);
        return spawnGroupData;
    }

    public boolean hasShield()
    {
        for(InteractionHand hand : InteractionHand.values())
        {
            ItemStack st = getItemInHand(hand);
            if(!st.isEmpty() && st.getItem() == Items.SHIELD) return true;
        }

        return false;
    }

    public ItemStack getShieldStack()
    {
        for(InteractionHand hand : InteractionHand.values())
        {
            ItemStack st = getItemInHand(hand);
            if(!st.isEmpty() && st.getItem() == Items.SHIELD) return st;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canBreakDoors() {
        return false;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return this.isInWater() ? BMEffects.DECAYED_AMBIENT_WATER.get() : BMEffects.DECAYED_AMBIENT.get();
    }

    protected SoundEvent getHurtSound(DamageSource source)
    {
        return this.isInWater() ? BMEffects.DECAYED_HURT_WATER.get() : BMEffects.DECAYED_HURT.get();
    }

    protected SoundEvent getDeathSound()
    {
        return this.isInWater() ? BMEffects.DECAYED_DEATH_WATER.get() : BMEffects.DECAYED_DEATH.get();
    }

    protected SoundEvent getStepSound()
    {
        return BMEffects.DECAYED_STEP.get();
    }

    protected SoundEvent getSwimSound()
    {
        return BMEffects.DECAYED_SWIM.get();
    }

    protected ItemStack getSkull()
    {
        return ItemStack.EMPTY;
    }

    @Override
    protected boolean convertsInWater() {
        return false;
    }

    @Override
    public boolean isPushedByFluid() {
        return !this.isSwimming();
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader levelReader) {
        return levelReader.isUnobstructed(this);
    }

    boolean wantsToSwim() {
        if (this.targetingUnderwater) {
            return true;
        }
        LivingEntity livingEntity = this.getTarget();
        return livingEntity != null && livingEntity.isInWater();
    }

    @Override
    public void travel(Vec3 vec3) {
        if (this.isEffectiveAi() && this.isInWater() && this.wantsToSwim()) {
            this.moveRelative(0.01f, vec3);
            this.move(MoverType.SELF, this.getDeltaMovement());
            this.setDeltaMovement(this.getDeltaMovement().scale(0.9));
        } else {
            super.travel(vec3);
        }
    }

    @Override
    public void updateSwimming() {
        if (!this.level.isClientSide)
        {
            if(isInWater())
                maxUpStep = 1;
            else
                maxUpStep = 0.5F;

            if (this.isEffectiveAi() && this.isInWater() && this.wantsToSwim()) {
                this.navigation = this.waterNavigation;
                this.setSwimming(true);
            } else {
                this.navigation = this.landNavigation;
                this.setSwimming(false);
            }
        }
    }

    public void setDummy()
    {
        this.isDummy = true;
    }

    public boolean isDummy()
    {
        return isDummy;
    }

    public void setTargetingUnderwater(boolean targetingUnderwater)
    {
        this.targetingUnderwater = targetingUnderwater;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("ShieldHealth", shieldHealth);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        if(compoundTag.contains("ShieldHealth"))
            shieldHealth = compoundTag.getInt("ShieldHealth");
    }

    protected boolean closeToNextPos() {
        double d;
        BlockPos blockPos;
        Path path = this.getNavigation().getPath();
        return path != null && (blockPos = path.getTarget()) != null && (d = this.distanceToSqr(blockPos.getX(), blockPos.getY(), blockPos.getZ())) < 4.0;
    }

    @Override
    public float getSwimAmount(float f) {
        if(isDummy()) return 3F;

        return super.getSwimAmount(f);
    }

    public static boolean checkSpawnRules(EntityType<DecayedEntity> entityType, ServerLevelAccessor serverLevelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource random) {
        boolean validReasons = serverLevelAccessor.getDifficulty() != Difficulty.PEACEFUL && Drowned.isDarkEnoughToSpawn(serverLevelAccessor, blockPos, random) && (mobSpawnType == MobSpawnType.SPAWNER || serverLevelAccessor.getFluidState(blockPos).is(FluidTags.WATER));

        return random.nextInt(3) == 0 && validReasons && blockPos.getY() <= serverLevelAccessor.getSeaLevel();
    }

    static class DecayedMoveControl extends MoveControl {
        private final DecayedEntity drowned;

        public DecayedMoveControl(DecayedEntity drowned) {
            super(drowned);
            this.drowned = drowned;
        }

        @Override
        public void tick() {
            LivingEntity livingEntity = this.drowned.getTarget();
            if (this.drowned.wantsToSwim() && this.drowned.isInWater()) {
                if (livingEntity != null && livingEntity.getY() > this.drowned.getY() || this.drowned.targetingUnderwater) {
                    this.drowned.setDeltaMovement(this.drowned.getDeltaMovement().add(0.0, 0.002, 0.0));
                }
                if (this.operation != MoveControl.Operation.MOVE_TO || this.drowned.getNavigation().isDone()) {
                    this.drowned.setSpeed(0.0f);
                    return;
                }
                double d = this.wantedX - this.drowned.getX();
                double e = this.wantedY - this.drowned.getY();
                double f = this.wantedZ - this.drowned.getZ();
                double g = Math.sqrt(d * d + e * e + f * f);
                e /= g;
                float h = (float)(Mth.atan2(f, d) * 57.2957763671875) - 90.0f;
                this.drowned.setYRot(this.rotlerp(this.drowned.getYRot(), h, 90.0f));
                this.drowned.yBodyRot = this.drowned.getYRot();
                float i = (float)(this.speedModifier * this.drowned.getAttributeValue(Attributes.MOVEMENT_SPEED));
                float j = Mth.lerp(0.125f, this.drowned.getSpeed(), i);
                this.drowned.setSpeed(j);
                this.drowned.setDeltaMovement(this.drowned.getDeltaMovement().add((double)j * d * 0.005, (double)j * e * 0.1, (double)j * f * 0.005));
            } else {
                if (!this.drowned.onGround) {
                    this.drowned.setDeltaMovement(this.drowned.getDeltaMovement().add(0.0, -0.008, 0.0));
                }
                super.tick();
            }
        }
    }

    static class GoToWaterGoal extends RandomStrollGoal
    {
        public GoToWaterGoal(PathfinderMob mob, double speed)
        {
            super(mob, speed);
        }

        @Override
        public boolean canUse()
        {
            return !mob.isInWater() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return !mob.isInWater() && super.canContinueToUse();
        }
    }

    static class LeaveWaterGoal extends MoveToBlockGoal
    {
        private final DecayedEntity decayed;

        public LeaveWaterGoal(DecayedEntity decayed, double speed)
        {
            super(decayed, speed, 8, 2);
            this.decayed = decayed;
        }

        @Override
        public boolean canUse() {
            return super.canUse() && !this.decayed.level.isDay() && this.decayed.isInWater() && this.decayed.getY() >= (double) (this.decayed.level.getSeaLevel() - 3);
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse();
        }

        @Override
        protected boolean isValidTarget(LevelReader levelReader, BlockPos pos) {
            BlockPos blockPos = pos.above();
            return levelReader.isEmptyBlock(blockPos) && levelReader.isEmptyBlock(blockPos.above()) && levelReader.getBlockState(pos).entityCanStandOn(levelReader, pos, this.decayed);
        }

        public void start()
        {
            this.decayed.setTargetingUnderwater(false);
            this.decayed.navigation = this.decayed.landNavigation;
            super.start();
        }

        public void stop()
        {
            super.stop();
        }
    }

    static class SwimUpGoal extends Goal {
        private final DecayedEntity drowned;
        private final double speedModifier;
        private final int seaLevel;
        private boolean stuck;

        public SwimUpGoal(DecayedEntity drowned, double d, int i) {
            this.drowned = drowned;
            this.speedModifier = d;
            this.seaLevel = i;
        }

        @Override
        public boolean canUse() {
            return !this.drowned.level.isDay() && this.drowned.isInWater() && this.drowned.getY() < (double)(this.seaLevel - 2);
        }

        @Override
        public boolean canContinueToUse() {
            return this.canUse() && !this.stuck;
        }

        @Override
        public void tick() {
            if (this.drowned.getY() < (double)(this.seaLevel - 1) && (this.drowned.getNavigation().isDone() || this.drowned.closeToNextPos())) {
                Vec3 vec3 = DefaultRandomPos.getPosTowards(this.drowned, 4, 8, new Vec3(this.drowned.getX(), this.seaLevel - 1, this.drowned.getZ()), 1.5707963705062866);
                if (vec3 == null) {
                    this.stuck = true;
                    return;
                }
                this.drowned.getNavigation().moveTo(vec3.x, vec3.y, vec3.z, this.speedModifier);
            }
        }

        @Override
        public void start() {
            this.drowned.setTargetingUnderwater(true);
            this.stuck = false;
        }

        @Override
        public void stop() {
            this.drowned.setTargetingUnderwater(false);
        }
    }
}
