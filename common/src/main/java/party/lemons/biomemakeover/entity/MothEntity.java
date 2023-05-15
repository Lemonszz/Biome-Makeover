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
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.entity.ai.FlyWanderGoal;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMEffects;

import java.util.EnumSet;
import java.util.Optional;
import java.util.function.Predicate;

public class MothEntity extends Monster
{
    private static final EntityDataAccessor<Boolean> TARGETING = SynchedEntityData.defineId(MothEntity.class, EntityDataSerializers.BOOLEAN);

    public boolean hasPlayedLoop = false;
    private float currentPitch;
    private float lastPitch;

    private int findAttractionCooldown = Mth.nextInt(this.random, 20, 60);
    private BlockPos savedAttractionPos;
    private HugAttractionGoal hugAttractionGoal;

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

        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.hugAttractionGoal = new HugAttractionGoal();
        this.goalSelector.addGoal(1, this.hugAttractionGoal);
        this.goalSelector.addGoal(3, new MeleeAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, OwlEntity.class, 6.0F, 1.0D, 1.2D));
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
        this.currentPitch = Math.max((float)Math.sin(tickCount / 10F) / 10F, this.currentPitch - 0.24F);

    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide) {
            if (this.findAttractionCooldown > 0) {
                --this.findAttractionCooldown;
            }
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

            @Override
            public void tick() {
                if (!MothEntity.this.hugAttractionGoal.isHugging()) {
                    super.tick();
                }
            }
        };
        birdNavigation.setCanPassDoors(false);
        birdNavigation.setCanFloat(false);
        birdNavigation.setCanOpenDoors(false);
        return birdNavigation;
    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        if (this.isInvulnerableTo(damageSource)) {
            return false;
        } else {
            if (!this.level.isClientSide) {
                this.hugAttractionGoal.stopHugging();
            }

            return super.hurt(damageSource, f);
        }
    }

    private boolean isAttractive(BlockState state) {
        return state.getLightEmission() > 10 || state.is(BMBlocks.MOTH_ATTRACTIVE);
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

    public boolean isTargeting()
    {
        return getEntityData().get(TARGETING);
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

    public boolean hasSavedAttractionPos() {
        return this.savedAttractionPos != null;
    }

    public boolean isValidAttraction(BlockPos pos)
    {
        return this.level.isLoaded(pos) && isAttractive(level.getBlockState(pos));
    }

    abstract class BaseAttractionGoal extends Goal {
        public abstract boolean canMothUse();

        public abstract boolean canMothContinueToUse();

        @Override
        public boolean canUse() {
            return this.canMothUse() && MothEntity.this.getLastAttacker() != null;
        }

        @Override
        public boolean canContinueToUse() {
            return this.canMothContinueToUse() && MothEntity.this.getLastAttacker() != null;
        }
    }


    private class HugAttractionGoal extends BaseAttractionGoal {
        private int successfulPollinatingTicks;
        private boolean hugging;
        @Nullable
        private Vec3 hoverPos;
        private int huggingTicks;

        private HugAttractionGoal() {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        @Override
        public boolean canMothUse() {
            if (MothEntity.this.findAttractionCooldown > 0) {
                return false;
            }
            else
            {
                Optional<BlockPos> nearbyAttraction = this.findNearbyAttraction();
                if (nearbyAttraction.isPresent()) {
                    MothEntity.this.savedAttractionPos = nearbyAttraction.get();
                    Vec3 targetPosition = MothEntity.this.savedAttractionPos.getCenter();
                    MothEntity.this.navigation.moveTo(targetPosition.x, targetPosition.y, targetPosition.z, 1.2F);
                    return true;
                } else {
                    MothEntity.this.findAttractionCooldown = Mth.nextInt(MothEntity.this.random, 20, 60);
                    return false;
                }
            }
        }

        @Override
        public boolean canMothContinueToUse() {
            if (!this.hugging) {
                return false;
            }
            else if (!MothEntity.this.hasSavedAttractionPos()) {
                return false;
            }
            else if (this.hasHuggedLongEnough())
            {
                return MothEntity.this.random.nextFloat() < 0.2F;
            }
            else if (MothEntity.this.tickCount % 20 == 0 && !MothEntity.this.isValidAttraction(MothEntity.this.savedAttractionPos)) {
                MothEntity.this.savedAttractionPos = null;
                return false;
            } else {
                return true;
            }
        }

        private boolean hasHuggedLongEnough() {
            return this.successfulPollinatingTicks > 400;
        }

        boolean isHugging() {
            return this.hugging;
        }

        void stopHugging() {
            this.hugging = false;
        }

        @Override
        public void start() {
            this.successfulPollinatingTicks = 0;
            this.huggingTicks = 0;
            this.hugging = true;
        }

        @Override
        public void stop() {
            this.hugging = false;
            MothEntity.this.navigation.stop();
            MothEntity.this.findAttractionCooldown = 200;
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            ++this.huggingTicks;
            if (this.huggingTicks > 600) {
                MothEntity.this.savedAttractionPos = null;
            } else {
                Vec3 vec3 = Vec3.atBottomCenterOf(MothEntity.this.savedAttractionPos).add(0.0, 0.6F, 0.0);
                if (vec3.distanceTo(MothEntity.this.position()) > 1.0) {
                    this.hoverPos = vec3;
                    this.setWantedPos();
                } else {
                    if (this.hoverPos == null) {
                        this.hoverPos = vec3;
                    }

                    boolean bl = MothEntity.this.position().distanceTo(this.hoverPos) <= 0.1;
                    boolean bl2 = true;
                    if (!bl && this.huggingTicks > 600) {
                        MothEntity.this.savedAttractionPos = null;
                    } else {
                        if (bl) {
                            boolean bl3 = MothEntity.this.random.nextInt(25) == 0;
                            if (bl3) {
                                this.hoverPos = new Vec3(vec3.x() + (double)this.getOffset(), vec3.y(), vec3.z() + (double)this.getOffset());
                                MothEntity.this.navigation.stop();
                            } else {
                                bl2 = false;
                            }

                            MothEntity.this.getLookControl().setLookAt(vec3.x(), vec3.y(), vec3.z());
                        }

                        if (bl2) {
                            this.setWantedPos();
                        }

                        ++this.successfulPollinatingTicks;
                    }
                }
            }
        }

        private void setWantedPos() {
            MothEntity.this.getMoveControl().setWantedPosition(this.hoverPos.x(), this.hoverPos.y(), this.hoverPos.z(), 0.35F);
        }

        private float getOffset() {
            return (MothEntity.this.random.nextFloat() * 2.0F - 1.0F) * 0.33F;
        }

        private Optional<BlockPos> findNearbyAttraction()
        {
            return this.findNearestBlock(MothEntity.this::isAttractive, 5.0);
        }

        private Optional<BlockPos> findNearestBlock(Predicate<BlockState> predicate, double d) {
            BlockPos mothPos = MothEntity.this.blockPosition();
            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

            for(int y = 0; (double)y <= d; y = y > 0 ? -y : 1 - y) {
                for(int j = 0; (double)j < d; ++j) {
                    for(int x = 0; x <= j; x = x > 0 ? -x : 1 - x) {
                        for(int z = x < j && x > -j ? j : 0; z <= j; z = z > 0 ? -z : 1 - z) {
                            pos.setWithOffset(mothPos, x, y - 1, z);
                            if (mothPos.closerThan(pos, d) && predicate.test(MothEntity.this.level.getBlockState(pos))) {
                                return Optional.of(pos);
                            }
                        }
                    }
                }
            }

            return Optional.empty();
        }
    }
}