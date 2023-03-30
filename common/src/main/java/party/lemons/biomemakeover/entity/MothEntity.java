package party.lemons.biomemakeover.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.util.AirRandomPos;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import party.lemons.biomemakeover.entity.ai.FlyWanderGoal;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMEffects;

import java.util.EnumSet;
import java.util.Optional;

public class MothEntity extends Monster
{
    private static final EntityDataAccessor<Boolean> TARGETING = SynchedEntityData.defineId(MothEntity.class, EntityDataSerializers.BOOLEAN);

    public boolean hasPlayedLoop = false;
    private float currentPitch;
    private float lastPitch;
    private AttractLightGoal attractLightGoal;
    private MoveToLightGoal moveToLightGoal;
    private BlockPos attactPos;

    public MothEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);

        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(BlockPathTypes.COCOA, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.FENCE, -1.0F);
    }

    @Override
    protected void registerGoals() {
        this.attractLightGoal = new AttractLightGoal();
        this.moveToLightGoal = new MoveToLightGoal();

        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, OwlEntity.class, 6.0F, 1.0D, 1.2D));
        this.goalSelector.addGoal(3, this.attractLightGoal);
        this.goalSelector.addGoal(4, this.moveToLightGoal);
        this.goalSelector.addGoal(5, new FlyWanderGoal(this));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new HurtByTargetGoal(this));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();

        getEntityData().define(TARGETING, false);
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return createMonsterAttributes()
                .add(Attributes.FLYING_SPEED, 0.6D)
                .add(Attributes.MAX_HEALTH, 10D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    public float getWalkTargetValue(BlockPos blockPos, LevelReader levelReader) {
        if(levelReader.getBlockState(blockPos).isAir())
        {
            return 10F + levelReader.getLightEmission(blockPos);
        }

        return super.getWalkTargetValue(blockPos, levelReader);
    }

    @Override
    public float getSwimAmount(float delta) {
        return Mth.lerp(delta, this.lastPitch, this.currentPitch);
    }

    private void updateBodyPitch() {
        this.lastPitch = this.currentPitch;
        if (this.isNearTarget())
        {
            this.currentPitch = Math.min(1.0F, this.currentPitch + 0.2F);
        }
        else
        {
            this.currentPitch = Math.max((float)Math.sin(tickCount / 10F) / 10F, this.currentPitch - 0.24F);
        }
    }

    public static boolean checkSpawnRules(EntityType<? extends MothEntity> entityType, ServerLevelAccessor levelAccessor, MobSpawnType mobSpawnType, BlockPos pos, RandomSource random) {

        return levelAccessor.getDifficulty() != Difficulty.PEACEFUL && Monster.isDarkEnoughToSpawn(levelAccessor, pos, random) && (mobSpawnType == MobSpawnType.SPAWNER || levelAccessor.getBlockState(pos.below()).is(BlockTags.LEAVES));
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation birdNavigation = new FlyingPathNavigation(this, level) {
            @Override
            public boolean isStableDestination(BlockPos blockPos) {
                return !this.level.getBlockState(blockPos.below()).isAir();
            }

            public void tick() {
                if (!MothEntity.this.attractLightGoal.isRunning()) {
                    super.tick();
                }
            }
        };
        birdNavigation.setCanPassDoors(false);
        birdNavigation.setCanFloat(false);
        birdNavigation.setCanOpenDoors(false);
        return birdNavigation;
    }

    private boolean isAttractive(BlockPos pos) {
        return this.level.isLoaded(pos) && ((level.getBlockState(pos).getLightEmission()) > 10) || this.level.getBlockState(pos).is(BMBlocks.MOTH_ATTRACTIVE);
    }

    @Override
    protected float getBlockSpeedFactor() {
        BlockState state = getBlockStateOn();
        Block block = state.getBlock();
        float mult;
        if(state.is(BMBlocks.ITCHING_IVY_TAG))
            mult = 1F;
        else
            mult = block.getSpeedFactor();

        if (block != Blocks.WATER && block != Blocks.BUBBLE_COLUMN)
        {
            if(mult == 1F)
            {
                BlockState velBlock = this.level.getBlockState(this.getBlockPosBelowThatAffectsMyMovement());
                if(!velBlock.is(BMBlocks.ITCHING_IVY_TAG))
                {
                    return  velBlock.getBlock().getSpeedFactor();
                }
                else
                {
                    return mult;
                }
            }
            else
            {
                return mult;
            }
        }
        else {
            return mult;
        }
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return BMEffects.MOTH_IDLE.get();
    }
    @Override
    public boolean causeFallDamage(float f, float g, DamageSource damageSource) {
        return false;
    }

    @Override
    protected void checkFallDamage(double d, boolean bl, BlockState blockState, BlockPos blockPos) {
    }

    @Override
    protected boolean isFlapping() {
        return true;
    }

    @Override
    public MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    public void tick() {
        super.tick();
        this.updateBodyPitch();
    }

    private boolean isNearTarget()
    {
        //TODO: work out wtf this was?
        return false;
    }

    public boolean isTargeting()
    {
        return getEntityData().get(TARGETING);
    }

    private boolean closerThan(BlockPos pos, int distance) {
        return pos.closerThan(this.getOnPos(), distance);
    }

    private boolean isTooFar(BlockPos pos) {
        return !this.closerThan(pos, 32);
    }

    private void startMovingTo(BlockPos pos) {
        Vec3 vec32;
        Vec3 vec3 = Vec3.atBottomCenterOf(getOnPos());
        int i = 0;
        BlockPos blockPos2 = this.blockPosition();
        int j = (int)vec3.y - blockPos2.getY();
        if (j > 2) {
            i = 4;
        } else if (j < -2) {
            i = -4;
        }
        int k = 6;
        int l = 8;
        int m = blockPos2.distManhattan(getOnPos());
        if (m < 15) {
            k = m / 2;
            l = m / 2;
        }
        if ((vec32 = AirRandomPos.getPosTowards(this, k, l, i, vec3, 0.3141592741012573)) == null) {
            return;
        }
        this.navigation.setMaxVisitedNodesMultiplier(0.5f);
        this.navigation.moveTo(vec32.x, vec32.y, vec32.z, 1.0);
    }

    @Override
    protected SoundEvent getDeathSound()
    {
        return BMEffects.MOTH_DEATH.get();
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source)
    {
        return BMEffects.MOTH_HURT.get();
    }

    @Override
    public boolean doHurtTarget(Entity entity) {
        boolean attacked = super.doHurtTarget(getTarget());
        if(attacked)
        {
            this.playSound(BMEffects.MOTH_BITE.get(), 1.0F, 1.0F);
        }
        return attacked;
    }

    public class MoveToLightGoal extends NotAttackingGoal {
        private int ticks;

        MoveToLightGoal() {
            super();
            this.ticks = MothEntity.this.random.nextInt(10);
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canMothContinue() {
            return MothEntity.this.attactPos != null && !MothEntity.this.hasRestriction() && MothEntity.this.isAttractive(MothEntity.this.attactPos) && !MothEntity.this.closerThan(MothEntity.this.attactPos, 2);
        }

        public boolean canMothStart() {
            return this.canMothContinue();
        }

        public void start() {
            this.ticks = 0;
            super.start();
        }

        public void stop() {
            this.ticks = 0;
            MothEntity.this.navigation.stop();
            MothEntity.this.navigation.resetMaxVisitedNodesMultiplier();
        }

        public void tick() {
            if (MothEntity.this.attactPos != null) {
                ++this.ticks;
                if (this.ticks > 600) {
                    MothEntity.this.attactPos = null;
                } else if (!MothEntity.this.navigation.isInProgress()) {
                    if (MothEntity.this.isTooFar(MothEntity.this.attactPos)) {
                        MothEntity.this.attactPos = null;
                    } else {
                        MothEntity.this.startMovingTo(MothEntity.this.attactPos);
                    }
                }
            }
        }
    }

    private abstract class NotAttackingGoal extends Goal
    {
        private NotAttackingGoal() {
        }

        public abstract boolean canMothContinue();

        public abstract boolean canMothStart();

        public boolean canUse() {
            return this.canMothStart() && !MothEntity.this.isTargeting();
        }

        @Override
        public boolean canContinueToUse() {
            return this.canMothContinue() && !MothEntity.this.isTargeting();
        }
    }

    private class AttractLightGoal extends NotAttackingGoal
    {
        private int attractTicks = 0;
        private int lastAttractTick = 0;
        private boolean running;
        private Vec3 nextTarget;
        private int ticks = 0;

        AttractLightGoal() {
            super();
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canMothStart() {
            if (MothEntity.this.level.isRaining()) {
                return false;
            } else if (MothEntity.this.random.nextFloat() < 0.7F) {
                return false;
            } else {
                Optional<BlockPos> optional = this.findLight();
                if (optional.isPresent())
                {
                    MothEntity.this.attactPos = optional.get();
                    MothEntity.this.navigation.moveTo((double)MothEntity.this.attactPos.getX() + 0.5D, (double)MothEntity.this.attactPos
                            .getY() + 1.25F, (double)MothEntity.this.attactPos.getZ() + 0.5D, 1.2F);
                    return true;
                } else {
                    return false;
                }
            }
        }

        public boolean canMothContinue() {
            if (!this.running) {
                return false;
            } else if (MothEntity.this.attactPos == null) {
                return false;
            } else if (MothEntity.this.level.isRaining()) {
                return false;
            } else if (this.completedAttract()) {
                return MothEntity.this.random.nextFloat() < 0.2F;
            } else if (MothEntity.this.tickCount % 20 == 0 && !MothEntity.this.isAttractive(MothEntity.this.attactPos)) {
                MothEntity.this.attactPos = null;
                return false;
            }
            else if(attactPos != null && attactPos.getY() < getY() && !MothEntity.this.closerThan(attactPos, 1) && level.getBlockState(MothEntity.this.getOnPos().below()).canOcclude()){
                MothEntity.this.attactPos = null;

                return false;
            }
            else {
                return true;
            }
        }

        private boolean completedAttract() {
            return this.attractTicks > 400;
        }

        private boolean isRunning() {
            return this.running;
        }

        private void cancel() {
            this.running = false;
        }

        public void start() {
            this.attractTicks = 0;
            this.ticks = 0;
            this.lastAttractTick = 0;
            this.running = true;
        }

        public void stop() {
            this.running = false;
            MothEntity.this.navigation.stop();
        }

        public void tick()
        {
            ++this.ticks;
            if (this.ticks > 600) {
                MothEntity.this.attactPos = null;
            } else {

                Vec3 centerTarget = Vec3.atBottomCenterOf(attactPos);
                if(MothEntity.this.getY() < centerTarget.y)
                    centerTarget = centerTarget.add(0, -1.25F, 0);
                else centerTarget = centerTarget.add(0, 1.25F, 0);

                BlockPos pos = MothEntity.this.getOnPos();
                Vec3 onVec = new Vec3(pos.getX(), pos.getY(), pos.getZ());
                if (centerTarget.distanceTo(onVec) > 1.0D) {
                    this.nextTarget = centerTarget;
                    this.moveToNextTarget();
                } else {
                    if (this.nextTarget == null) {
                        this.nextTarget = centerTarget;
                    }

                    boolean isClose = onVec.distanceTo(this.nextTarget) <= 0.1D;
                    boolean hasFinished = true;
                    if (!isClose && this.ticks > 600) {
                        MothEntity.this.attactPos = null;
                    } else {
                        if (isClose) {
                            boolean getNewTarget = MothEntity.this.random.nextInt(25) == 0;
                            if (getNewTarget) {
                                this.nextTarget = new Vec3(centerTarget.x() + (double)this.getRandomOffset(), centerTarget.y(), centerTarget.z() + (double)this.getRandomOffset());
                                MothEntity.this.navigation.stop();
                            } else {
                                hasFinished = false;
                            }

                            MothEntity.this.getLookControl().setLookAt(centerTarget.x(), centerTarget.y(), centerTarget.z());
                        }

                        if (hasFinished) {
                            this.moveToNextTarget();
                        }

                        ++this.attractTicks;
                        if (MothEntity.this.random.nextFloat() < 0.05F && this.attractTicks > this.lastAttractTick + 60) {
                            this.lastAttractTick = this.attractTicks;
                        }

                    }
                }
            }
        }

        private void moveToNextTarget() {
            MothEntity.this.getMoveControl().setWantedPosition(this.nextTarget.x(), this.nextTarget.y(), this.nextTarget.z(), 0.35F);
        }

        private float getRandomOffset() {
            return (MothEntity.this.random.nextFloat() * 2.0F - 1.0F) * 0.33333334F;
        }

        private Optional<BlockPos> findLight() {
            return this.findLight(5.0D);
        }

        private Optional<BlockPos> findLight(double searchDistance) {
            BlockPos blockPos = MothEntity.this.getOnPos();
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

            for(int y = 0; (double)y <= searchDistance; y = y > 0 ? -y : 1 - y) {
                for(int j = 0; (double)j < searchDistance; ++j) {
                    for(int x = 0; x <= j; x = x > 0 ? -x : 1 - x) {
                        for(int z = x < j && x > -j ? j : 0; z <= j; z = z > 0 ? -z : 1 - z) {
                            mutable.setWithOffset(blockPos, x, y - 1, z);
                            if (blockPos.closerThan(mutable, searchDistance) && MothEntity.this.isAttractive(mutable))
                            {
                                return Optional.of(mutable);
                            }
                        }
                    }
                }
            }

            return Optional.empty();
        }
    }
}