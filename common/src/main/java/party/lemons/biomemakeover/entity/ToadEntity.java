package party.lemons.biomemakeover.entity;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.init.BMItems;
import party.lemons.biomemakeover.util.MathUtils;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.EnumSet;
import java.util.List;
import java.util.UUID;

public class ToadEntity extends Animal {

    private static final UUID JUMP_SPEED_BOOST = UUID.fromString("0fa7caca-4f09-11eb-ae93-0242ac130002");
    private static final AttributeModifier JUMP_SPEED_BOOST_MOD = new AttributeModifier(JUMP_SPEED_BOOST, "Jump Speed Boost", 0.6F, AttributeModifier.Operation.ADDITION);

    private static final EntityDataAccessor<Integer> TONGUE_ENTITY = SynchedEntityData.defineId(ToadEntity.class, EntityDataSerializers.INT);

    private boolean onGroundPrev;
    private int ticksUntilJump;
    public float tongueDistance;
    public float targetTongueDistance;
    public float mouthDistance = 0;
    public int eatCooldown = 0;
    public boolean hasBaby;

    public ToadEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);

        setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(TONGUE_ENTITY, -1);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new LookAtTongueTarget(this));
        this.goalSelector.addGoal(0, new MakeTadpoleGoal(this, 1.0D, 10));
        this.goalSelector.addGoal(2, new BreedGoal(this, 0.8D));
        this.goalSelector.addGoal(3, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.6D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0D, Ingredient.of(BMItems.DRAGONFLY_WINGS, Items.SPIDER_EYE), false));
        this.goalSelector.addGoal(11, new LookAtPlayerGoal(this, Player.class, 10.0F));
        this.goalSelector.addGoal(12, new RandomLookAroundGoal(this));
    }

    public void setTongueEntity(ToadTargetEntity e)
    {
        getEntityData().set(TONGUE_ENTITY, e.getId());
        e.setEatenBy(this);
        if(!level.isClientSide())
        {
            this.playSound(BMEffects.TOAD_MOUTH, 1F, 1F + ((float) random.nextGaussian() / 5F));
        }
    }

    public boolean hasTongueEntity()
    {
        return getEntityData().get(TONGUE_ENTITY) != -1;
    }

    public int getTongueEntityID()
    {
        return getEntityData().get(TONGUE_ENTITY);
    }

    public void clearTongueEntity()
    {
        if(level.getEntity(getTongueEntityID()) != null)
            ((ToadTargetEntity) level.getEntity(getTongueEntityID())).setEatenBy(null);

        getEntityData().set(TONGUE_ENTITY, -1);
    }

    @Override
    public float getWalkTargetValue(BlockPos blockPos, LevelReader levelReader) {
        if(levelReader.getBlockState(blockPos).is(BMBlocks.LILY_PADS)) return 100;

        return super.getWalkTargetValue(blockPos, levelReader);
    }

    @Override
    public void tick() {
        super.tick();

        if(hasBaby && getAge() == 0) hasBaby = false;

        if(hasTongueEntity())
        {
            Entity e = level.getEntity(getTongueEntityID());
            if(e != null && !e.isPassenger())
            {
                getLookControl().setLookAt(e.getX(), (e.getBoundingBox().minY + 0.25F), e.getZ(), 100, 100);
                yBodyRot = getTargetYaw();
                yHeadRot = getTargetYaw();
                setXRot(getTargetPitch());

                float speed = 10;
                targetTongueDistance = (this.distanceTo(e) * 16) - ((float) (e.getBoundingBox().maxX - e.getBoundingBox().minX) * 16F);
                if(tongueDistance > targetTongueDistance) speed *= 2;

                tongueDistance = MathUtils.approachValue(tongueDistance, targetTongueDistance, speed);
            }else//TODO: clean this
            {
                targetTongueDistance = 0;
                tongueDistance = MathUtils.approachValue(tongueDistance, 0, 20);
            }
        }else
        {
            targetTongueDistance = 0;
            tongueDistance = MathUtils.approachValue(tongueDistance, 0, 20);
        }
    }
    public boolean isTongueReady()
    {
        float yaw = Math.abs(((yBodyRot + 1) % 360) - getTargetYaw());
        boolean dis = Math.abs(tongueDistance - targetTongueDistance) < 5;
        return dis && (yaw < 4F || yaw >= 360);
    }

    public float getTargetYaw()
    {
        double xx = lookControl.getWantedX() - getX();
        double zz = lookControl.getWantedZ() - getZ();
        return (float) (Mth.atan2(zz, xx) * 57.2957763671875D) - 90.0F;
    }

    public float getTargetPitch()
    {
        double xx = lookControl.getWantedX() - getX();
        double yy = lookControl.getWantedY() - getEyeY();
        double zz = lookControl.getWantedZ() - getZ();
        double sqrt = Mth.sqrt((float)(xx * xx + zz * zz));
        return (float) (-(Mth.atan2(yy, sqrt) * 57.2957763671875D));
    }

    public boolean canUseTongue()
    {
        return !this.isPassenger();
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return stack.getItem() == BMItems.DRAGONFLY_WINGS || stack.getItem() == Items.SPIDER_EYE;
    }

    private final TargetingConditions predicate = TargetingConditions.forNonCombat().selector((e)->e.distanceTo(e) < 10);


    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        eatCooldown--;
        if(eatCooldown <= 0 && !hasTongueEntity())
        {
            List<ToadTargetEntity> targets = level.getEntitiesOfClass(ToadTargetEntity.class, getBoundingBox().inflate(3, 3, 3), (e)->hasLineOfSight(e) && !e.isBeingEaten());
            ToadTargetEntity closest = level.getNearestEntity(targets, predicate, this, getX(), getY(), getZ());

            if(!canUseTongue() || closest == null || closest.isPassenger() || targets.isEmpty()) clearTongueEntity();
            else
            {
                eatCooldown = 350;
                setTongueEntity(closest);
            }
        }else
        {
            Entity e = level.getEntity(getTongueEntityID());
            if(!canUseTongue() || e == null || !e.isAlive()) clearTongueEntity();
        }

        if(this.ticksUntilJump > 0)
        {
            --this.ticksUntilJump;
        }

        if(ticksUntilJump <= 0 && moveControl.hasWanted())
        {
            ticksUntilJump = RandomUtil.randomRange(20, 100);
            jumpFromGround();
            AttributeInstance entityAttributeInstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
            entityAttributeInstance.removeModifier(JUMP_SPEED_BOOST);
            entityAttributeInstance.addTransientModifier(JUMP_SPEED_BOOST_MOD);

            this.playSound(this.getJumpSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 0.8F);
        }

        if(onGround && !onGroundPrev)
        {
            this.getAttribute(Attributes.MOVEMENT_SPEED).removeModifier(JUMP_SPEED_BOOST_MOD);
        }

        onGroundPrev = onGround;
    }

    protected SoundEvent getJumpSound()
    {
        return BMEffects.TOAD_JUMP;
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putBoolean("HasBaby", hasBaby);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        hasBaby = compoundTag.getBoolean("HasBaby");
    }

    public void setHasBaby(boolean hasBaby)
    {
        this.hasBaby = hasBaby;
    }

    @Override
    public void spawnChildFromBreeding(ServerLevel serverLevel, Animal other) {
        ServerPlayer player = this.getLoveCause();
        if(player == null && other.getLoveCause() != null)
        {
            player = other.getLoveCause();
        }

        if(player != null)
        {
            player.awardStat(Stats.ANIMALS_BRED);
            CriteriaTriggers.BRED_ANIMALS.trigger(player, this, other, null);
        }

        setHasBaby(true);
        this.setAge(6000);
        other.setAge(6000);
        this.resetLove();
        other.resetLove();
        serverLevel.broadcastEntityEvent(this, (byte) 18);
        if(serverLevel.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT))
        {
            serverLevel.addFreshEntity(new ExperienceOrb(serverLevel, this.getX(), this.getY(), this.getZ(), this.getRandom().nextInt(7) + 1));
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        //Unused
        return null;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return BMEffects.TOAD_CROAK;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return BMEffects.TOAD_DEATH;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return BMEffects.TOAD_HURT;
    }

    private static class LookAtTongueTarget extends Goal
    {
        private final ToadEntity toad;

        public LookAtTongueTarget(ToadEntity entity)
        {
            super();
            this.toad = entity;
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            return toad.hasTongueEntity();
        }

        @Override
        public boolean canContinueToUse() {
            return toad.hasTongueEntity();
        }
    }

    public class MakeTadpoleGoal extends MoveToBlockGoal
    {
        private final ToadEntity toad;

        public MakeTadpoleGoal(ToadEntity toad, double speed, int range)
        {
            super(toad, speed, range, 5);
            this.toad = toad;
        }

        @Override
        public boolean canUse() {
            return this.toad.hasBaby && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && this.toad.hasBaby;
        }

        @Override
        public void tick()
        {
            super.tick();

            BlockPos blockPos = this.mob.getOnPos();
            if(getMoveToTarget().closerThan(toad.getOnPos(), 2F))
            {
                getNavigation().moveTo(getMoveToTarget().getX() + 0.5F, getMoveToTarget().getY(), getMoveToTarget().getZ() + 0.5F, 1F);
            }

            if(toad.isInWater())
            {
                ServerLevel world = (ServerLevel) this.toad.level;
                this.toad.setHasBaby(false);

                TadpoleEntity tadpole = BMEntities.TADPOLE.create(world);
                if(tadpole != null)
                {
                    world.playSound(null, blockPos, BMEffects.TOAD_HAVE_BABY, SoundSource.BLOCKS, 0.3F, 0.9F + world.random.nextFloat() * 0.2F);
                    tadpole.setBaby(true);
                    tadpole.moveTo(toad.getX(), toad.getY(), toad.getZ(), 0.0F, 0.0F);
                    world.addFreshEntityWithPassengers(tadpole);
                }
            }
        }

        @Override
        public double acceptedDistance()
        {
            return 0.0D;
        }

        @Override
        protected boolean isValidTarget(LevelReader levelReader, BlockPos blockPos) {
            FluidState state = levelReader.getFluidState(blockPos);
            return state.isSource() && state.is(FluidTags.WATER) && levelReader.getBlockState(blockPos.above()).isAir();
        }
    }
}
