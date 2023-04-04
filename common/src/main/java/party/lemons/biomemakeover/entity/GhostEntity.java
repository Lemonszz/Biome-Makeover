package party.lemons.biomemakeover.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.level.PoltergeistHandler;

import java.util.EnumSet;
import java.util.UUID;

public class GhostEntity extends Monster implements NeutralMob
{
    private static final EntityDataAccessor<Boolean> IsCharging = SynchedEntityData.defineId(GhostEntity.class, EntityDataSerializers.BOOLEAN);
    private static final UniformInt WANDER_RANGE_HORIZONTAL = UniformInt.of(-7, 7);
    private static final UniformInt WANDER_RANGE_VERTICAL = UniformInt.of(-5, 5);
    private static final double CHARGE_MIN_DISTANCE = 2;
    private static final double CHARGE_MAX_DISTANCE = 10;
    private static final int POLTERGEIST_RANGE = 10;

    private static final UniformInt ANGER_TIME_RANGE = TimeUtil.rangeOfSeconds(20, 39);
    private int angerTime;
    private UUID targetUuid;
    private static final UniformInt REINFORCE_TIME_RANGE = TimeUtil.rangeOfSeconds(4, 6);
    private int reinforceTime;
    private static final UniformInt ANGER_SOUND_TIME_RANGE = TimeUtil.rangeOfSeconds(0, 1);
    private int angrySoundDelay;
    private BlockPos homePosition;

    public GhostEntity(EntityType<? extends Monster> entityType, Level level)
    {
        super(entityType, level);

        this.moveControl = new GhostMoveControl(this);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();

        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PoltergeistGoal());
        this.goalSelector.addGoal(4, new ChargeTargetGoal());
        this.goalSelector.addGoal(8, new FlyAroundGoal());
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 3.0F, 1.0F));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Mob.class, 8.0F));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers());
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();

        this.getEntityData().define(IsCharging, false);
    }

    @Override
    public void move(MoverType moverType, Vec3 vec3) {
        super.move(moverType, vec3);
        this.checkInsideBlocks();
    }

    @Override
    public MobType getMobType() {
        return MobType.UNDEAD;
    }

    @Override
    public void tick() {
        this.noPhysics = true;
        super.tick();
        this.noPhysics = false;
        this.setNoGravity(true);

        if(getHomePosition() == null)
        {
            homePosition = GhostEntity.this.getOnPos();
        }
    }

    private BlockPos getHomePosition()
    {
        return homePosition;
    }

    @Override
    protected void customServerAiStep() {
        if(this.isAngry())
        {
            this.maybePlayFirstAngerSound();
        }

        this.updatePersistentAnger((ServerLevel) this.level, true);
        if(this.getTarget() != null)
        {
            this.checkAlertTime();
        }
        super.customServerAiStep();
    }

    private void checkAlertTime()
    {
        if(this.reinforceTime > 0)
        {
            --this.reinforceTime;
        }else
        {
            if(this.getSensing().hasLineOfSight(this.getTarget()))
            {
                this.alertNearby();
            }

            this.reinforceTime = REINFORCE_TIME_RANGE.sample(this.random);
        }
    }

    private void maybePlayFirstAngerSound() {
        if (this.angrySoundDelay > 0) {
            --this.angrySoundDelay;
            if (this.angrySoundDelay == 0) {
                this.playAngerSound();
            }
        }
    }

    private void alertNearby()
    {
        double alertRange = this.getAttributeValue(Attributes.FOLLOW_RANGE);
        BlockPos pos = getOnPos();

        AABB alertBounds = AABB.unitCubeFromLowerCorner(new Vec3(pos.getX(), pos.getY(), pos.getZ())).inflate(alertRange, 10.0D, alertRange);
        this.level.getEntitiesOfClass(GhostEntity.class, alertBounds).stream().filter((e)->e != this).filter((e)->e.getTarget() == null).filter((e)->getTarget() != null && !e.isAlliedTo(this.getTarget())).forEach((e)->e.setTarget(this.getTarget()));
    }

    @Override
    public void setTarget(@Nullable LivingEntity target) {
        if(this.getTarget() == null && target != null)
        {
            this.angrySoundDelay = ANGER_SOUND_TIME_RANGE.sample(this.random);
            this.reinforceTime = REINFORCE_TIME_RANGE.sample(this.random);
        }

        if(target instanceof Player player)
        {
            this.setLastHurtByPlayer(player);
        }

        super.setTarget(target);
    }

    private void playAngerSound()
    {
        this.playSound(BMEffects.GHOST_ANGRY.get(), this.getSoundVolume() * 2.0F, this.getVoicePitch());
    }

    public void chooseRandomAngerTime()
    {
        this.setRemainingPersistentAngerTime(ANGER_TIME_RANGE.sample(this.random));
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return angerTime;
    }

    @Override
    public void setRemainingPersistentAngerTime(int i) {
        this.angerTime = i;
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return targetUuid;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID uUID)
    {
        this.targetUuid = uUID;
    }

    @Override
    public void startPersistentAngerTimer() {
        chooseRandomAngerTime();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        this.addPersistentAngerSaveData(compoundTag);

        if(getHomePosition() != null)
        {
            compoundTag.putInt("HomeX", this.homePosition.getX());
            compoundTag.putInt("HomeY", this.homePosition.getY());
            compoundTag.putInt("HomeZ", this.homePosition.getZ());
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        this.readPersistentAngerSaveData(this.level, tag);

        if(tag.contains("HomeX"))
        {
            this.homePosition = new BlockPos(tag.getInt("HomeX"), tag.getInt("HomeY"), tag.getInt("HomeZ"));
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource)
    {
        if(damageSource.is(BMEntities.GHOST_IMMUNE_DAMAGE))
            return true;

        return super.isInvulnerableTo(damageSource);
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 1;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isAngry() ? BMEffects.GHOST_ANGRY.get() : BMEffects.GHOST_IDLE.get();
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return BMEffects.GHOST_DEATH.get();
    }
    private void setCharging(boolean charging)
    {
        this.getEntityData().set(IsCharging, charging);
    }

    public boolean isCharging()
    {
        return getEntityData().get(IsCharging);
    }

    public static boolean checkGhostSpawnRules(EntityType<GhostEntity> entityType, ServerLevelAccessor serverLevelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
        return serverLevelAccessor.getDifficulty() != Difficulty.PEACEFUL
                && isDarkEnoughToSpawnGhost(serverLevelAccessor, blockPos, randomSource)
                && checkMobSpawnRules(entityType, serverLevelAccessor, mobSpawnType, blockPos, randomSource);
    }

    public static boolean isDarkEnoughToSpawnGhost(ServerLevelAccessor serverLevelAccessor, BlockPos blockPos, RandomSource randomSource) {
        if (serverLevelAccessor.getBrightness(LightLayer.SKY, blockPos) > randomSource.nextInt(32)) {
            return false;
        } else {
            DimensionType dimensionType = serverLevelAccessor.dimensionType();
            int i = dimensionType.monsterSpawnBlockLightLimit();
            if (i < 15 && serverLevelAccessor.getBrightness(LightLayer.BLOCK, blockPos) - 5F > i) {
                return false;
            } else {
                int j = serverLevelAccessor.getLevel().isThundering()
                        ? serverLevelAccessor.getMaxLocalRawBrightness(blockPos, 10)
                        : serverLevelAccessor.getMaxLocalRawBrightness(blockPos);
                return j <= dimensionType.monsterSpawnLightTest().sample(randomSource);
            }
        }
    }


    class GhostMoveControl extends MoveControl
    {
        private GhostMoveControl(GhostEntity owner)
        {
            super(owner);
        }

        @Override
        public void tick()
        {
            if(this.operation == MoveControl.Operation.MOVE_TO)
            {
                Vec3 targetPosition = new Vec3(this.wantedX - GhostEntity.this.getX(), this.wantedY - GhostEntity.this.getY(), this.wantedZ - GhostEntity.this.getZ());
                double length = targetPosition.length();
                if(length < GhostEntity.this.getBoundingBox().getSize())
                {
                    this.operation = MoveControl.Operation.WAIT;
                    GhostEntity.this.setDeltaMovement(GhostEntity.this.getDeltaMovement().scale(0.5D));
                }else
                {
                    GhostEntity.this.setDeltaMovement(GhostEntity.this.getDeltaMovement().add(targetPosition.scale(this.speedModifier * 0.05D / length)));
                    if(GhostEntity.this.getTarget() == null)
                    {
                        if(EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(getTarget())) setTarget(null);

                        Vec3 vec3d2 = GhostEntity.this.getDeltaMovement();
                        GhostEntity.this.setYRot(-((float) Mth.atan2(vec3d2.x, vec3d2.z)) * 57.295776F);
                        GhostEntity.this.yBodyRot = GhostEntity.this.getYRot();
                    }else
                    {
                        double e = GhostEntity.this.getTarget().getX() - GhostEntity.this.getX();
                        double f = GhostEntity.this.getTarget().getZ() - GhostEntity.this.getZ();
                        GhostEntity.this.setYRot(-((float) Mth.atan2(e, f)) * 57.295776F);
                        GhostEntity.this.yBodyRot = GhostEntity.this.getYRot();
                    }
                }
            }
        }
    }

    class FlyAroundGoal extends Goal
    {

        private FlyAroundGoal()
        {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            return !GhostEntity.this.getMoveControl().hasWanted() && GhostEntity.this.random.nextInt(2) == 0;
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }

        @Override
        public void tick()
        {
            if(getHomePosition() == null) homePosition = getOnPos();

            for(int i = 0; i < 3; ++i)
            {
                BlockPos randomTarget = homePosition.offset(WANDER_RANGE_HORIZONTAL.sample(random), WANDER_RANGE_VERTICAL.sample(random), WANDER_RANGE_HORIZONTAL.sample(random));

                if(GhostEntity.this.level.isEmptyBlock(randomTarget))
                {
                    GhostEntity.this.moveControl.setWantedPosition((double) randomTarget.getX() + 0.5D, (double) randomTarget.getY() + 0.5D, (double) randomTarget.getZ() + 0.5D, 0.25D);

                    if(GhostEntity.this.getTarget() == null)
                    {
                        GhostEntity.this.getLookControl().setLookAt((double) randomTarget.getX() + 0.5D, (double) randomTarget.getY() + 0.5D, (double) randomTarget.getZ() + 0.5D, 180.0F, 20.0F);
                    }
                    break;
                }
            }
        }
    }

    class ChargeTargetGoal extends Goal
    {
        private ChargeTargetGoal()
        {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canUse()
        {
            if(GhostEntity.this.getTarget() != null && !GhostEntity.this.getMoveControl().hasWanted() && GhostEntity.this.random.nextInt(2) == 0)
            {
                return GhostEntity.this.distanceToSqr(GhostEntity.this.getTarget()) > CHARGE_MIN_DISTANCE;
            }
            return false;
        }

        @Override
        public boolean canContinueToUse()
        {
            return GhostEntity.this.getMoveControl().hasWanted() && GhostEntity.this.isCharging() && GhostEntity.this.getTarget() != null && GhostEntity.this.getTarget().isAlive();
        }

        @Override
        public void start()
        {
            LivingEntity target = GhostEntity.this.getTarget();
            Vec3 targetPosition = target.getEyePosition(1.0F);
            GhostEntity.this.moveControl.setWantedPosition(targetPosition.x, targetPosition.y, targetPosition.z, 1.25D);
            GhostEntity.this.setCharging(true);
            GhostEntity.this.playSound(BMEffects.GHOST_CHARGE.get(), 1.0F, 1.0F);
        }

        @Override
        public void stop()
        {
            GhostEntity.this.setCharging(false);
        }

        @Override
        public void tick()
        {
            LivingEntity target = GhostEntity.this.getTarget();
            if(EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(target)) setTarget(null);

            if(GhostEntity.this.getBoundingBox().inflate(2F).intersects(target.getBoundingBox()))
            {
                GhostEntity.this.doHurtTarget(target);
                GhostEntity.this.setCharging(false);
            }else
            {
                double distance = GhostEntity.this.distanceToSqr(target);
                if(distance < CHARGE_MAX_DISTANCE)
                {
                    Vec3 vec3d = target.getEyePosition(1.0F);
                    GhostEntity.this.moveControl.setWantedPosition(vec3d.x, vec3d.y, vec3d.z, 1.0D);
                }
            }
        }
    }

    private class PoltergeistGoal extends Goal
    {
        @Override
        public void tick()
        {
            for(int i = 0; i < 4; i++)
            {
                PoltergeistHandler.doPoltergeist(level, getOnPos(), POLTERGEIST_RANGE);
            }

            super.tick();
        }

        @Override
        public boolean canUse() {
            return true;
        }

        @Override
        public boolean canContinueToUse() {
            return false;
        }
    }
}
