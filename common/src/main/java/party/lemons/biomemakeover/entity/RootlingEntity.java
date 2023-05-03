package party.lemons.biomemakeover.entity;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.event.EntityEvent;
import party.lemons.biomemakeover.entity.event.EntityEventBroadcaster;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.init.BMItems;
import party.lemons.biomemakeover.util.EntityUtil;
import party.lemons.biomemakeover.util.RandomUtil;
import party.lemons.taniwha.item.types.TItem;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class RootlingEntity extends Animal implements Shearable, EntityEventBroadcaster {

    public static final EntityDataAccessor<Boolean> HAS_FLOWER = SynchedEntityData.defineId(RootlingEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Integer> FLOWER_TYPE = SynchedEntityData.defineId(RootlingEntity.class, EntityDataSerializers.INT);
    private static final ResourceLocation[] PETAL_LOOT_TABLES = new ResourceLocation[]{
            BiomeMakeover.ID("gameplay/rootling/blue"),
            BiomeMakeover.ID("gameplay/rootling/brown"),
            BiomeMakeover.ID("gameplay/rootling/cyan"),
            BiomeMakeover.ID("gameplay/rootling/gray"),
            BiomeMakeover.ID("gameplay/rootling/light_blue"),
            BiomeMakeover.ID("gameplay/rootling/purple"),
    };

    private boolean hasAction = false;
    public RootlingEntity forcedDancePartner = null;
    private int actionCooldown;
    private int growTime = 0;

    public RootlingEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);

        actionCooldown = RandomUtil.randomRange(0, 500);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(2, new GetInRainGoal());
        this.goalSelector.addGoal(4, new RootlingFleeGoal(this, LivingEntity.class, 8F, 1.6D, 1.4D, (e)->
                e.getItemBySlot(EquipmentSlot.MAINHAND).getItem() == Items.SHEARS || e.getItemBySlot(EquipmentSlot.OFFHAND).getItem() == Items.SHEARS));
        this.goalSelector.addGoal(5, new TemptGoal(this, 1D, Ingredient.of(Items.BONE_MEAL), false));

        this.goalSelector.addGoal(6, new DanceGoal());
        this.goalSelector.addGoal(7, new FollowEntityGoal());
        this.goalSelector.addGoal(8, new InspectFlowerGoal());
        this.goalSelector.addGoal(9, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(11, new RandomLookAroundGoal(this));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(HAS_FLOWER, true);
        getEntityData().define(FLOWER_TYPE, 0);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();

        if(!level.isClientSide())
        {
            if(!hasAction()) actionCooldown--;
            if(growTime > 0)
            {
                growTime--;
                if(isInWaterOrRain() && random.nextInt(5) == 0)
                    growTime--;
                if(growTime <= 0 && !hasFlower())
                    setFlowered(true);
            }
        }
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {

        ItemStack itemStack = player.getItemInHand(hand);
        if(itemStack.getItem() == Items.SHEARS)
        {
            if(!this.level.isClientSide() && this.readyForShearing())
            {
                this.shear(SoundSource.PLAYERS);
                itemStack.hurtAndBreak(1, player, (p)->p.broadcastBreakEvent(hand));
                return InteractionResult.SUCCESS;
            }else
            {
                return InteractionResult.CONSUME;
            }
        }else if(itemStack.getItem() == Items.BONE_MEAL)
        {
            if(!hasFlower())
            {
                if(level.isClientSide())
                {
                    return InteractionResult.CONSUME;
                }else
                {
                    broadcastEvent(this, EntityEvent.BONEMEAL_PARTICLE);

                    if(random.nextInt(3) == 0)
                    {
                        setFlowered(true);
                    }
                    if(!player.isCreative()) itemStack.shrink(1);
                    return InteractionResult.SUCCESS;
                }
            }
        }

        return super.mobInteract(player, hand);
    }

    public int getActionCooldown()
    {
        return actionCooldown;
    }

    public void setActionCoolDown(int cooldown)
    {
        this.actionCooldown = cooldown;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

        tag.putInt("ActionCooldown", actionCooldown);
        tag.putInt("GrowTime", growTime);
        tag.putBoolean("HasFlower", getEntityData().get(HAS_FLOWER));
        tag.putInt("FlowerType", getFlowerIndex());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        actionCooldown = tag.getInt("ActionCooldown");
        growTime = tag.getInt("GrowTime");
        getEntityData().set(HAS_FLOWER, tag.getBoolean("HasFlower"));
        getEntityData().set(FLOWER_TYPE, tag.getInt("FlowerType"));
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions entityDimensions) {
        return getDimensions(getPose()).height * 0.6F;
    }

    public boolean hasFlower()
    {
        return getEntityData().get(HAS_FLOWER);
    }

    public void setFlowered(boolean hasFlower)
    {
        boolean current = getEntityData().get(HAS_FLOWER);
        if(current != hasFlower)
        {
            getEntityData().set(HAS_FLOWER, hasFlower);
            if(hasFlower)
            {
                randomizeFlower();
            }else
            {
                EntityUtil.dropFromLootTable(this, PETAL_LOOT_TABLES[getEntityData().get(FLOWER_TYPE)]);
            }
        }
    }

    public boolean hasAction()
    {
        return hasAction;
    }

    public void setHasAction(boolean dancing)
    {
        this.hasAction = dancing;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    @Override
    public void shear(SoundSource soundSource) {
        this.level.playSound(null, this, SoundEvents.SHEEP_SHEAR, soundSource, 1.0F, 1.0F);
        this.setFlowered(false);
        growTime = RandomUtil.randomRange(600, 1200);
    }

    @Override
    public boolean readyForShearing() {
        return hasFlower();
    }

    public int getFlowerIndex()
    {
        return getEntityData().get(FLOWER_TYPE);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        SpawnGroupData data =  super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
        randomizeFlower();
        return data;
    }

    public void randomizeFlower()
    {
        getEntityData().set(FLOWER_TYPE, random.nextInt(BMItems.ROOTLING_PETALS.size()));
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource source)
    {
        return BMEffects.ROOTLING_HURT.get();
    }

    @Override
    protected @Nullable SoundEvent getDeathSound()
    {
        return BMEffects.ROOTLING_DEATH.get();
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound()
    {
        return BMEffects.ROOTLING_IDLE.get();
    }

    private static final TargetingConditions VALID_ROOTLING_PARTNER = (TargetingConditions.forNonCombat()).range(8.0D);
    public static int MAX_DANCE_TIME = 60;
    public static int MAX_FOLLOW_TIME = 120;
    public static int MAX_INSPECT_TIME = 200;
    public static int MAX_ACTION_COOLDOWN = 500;

    public class InspectFlowerGoal extends Goal
    {
        private BlockPos targetPos;
        private BlockState targetState;
        private int timer;

        public InspectFlowerGoal()
        {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse()
        {
            if(random.nextInt(10) == 0 || actionCooldown > 0 || hasAction()) return false;

            targetPos = findFlower();
            return targetPos != null;
        }

        @Override
        public void tick()
        {
            timer++;
            if(distanceToSqr(targetPos.getX() + 0.5F, targetPos.getY() + 0.5F, targetPos.getZ() + 0.5F) > 2F)
            {
                getMoveControl().setWantedPosition(targetPos.getX() + 0.5F, targetPos.getY() + 0.5F, targetPos.getZ() + 0.5F, 0.6F);
            }
            float targetY = targetPos.getY();
            if(targetState.is(BlockTags.TALL_FLOWERS)) targetY += 1;

            getLookControl().setLookAt(targetPos.getX() + 0.5F, targetY, targetPos.getZ() + 0.5F);
        }

        @Override
        public boolean canContinueToUse() {
            BlockState st = level.getBlockState(targetPos);
            if(st != targetState) return false;
            return timer <= MAX_INSPECT_TIME;
        }

        @Override
        public void stop()
        {
            this.timer = 0;
            setHasAction(false);
        }

        @Override
        public void start()
        {
            setHasAction(true);
            setActionCoolDown(MAX_ACTION_COOLDOWN);
        }

        private BlockPos findFlower()
        {
            BlockPos startPos = getOnPos();
            List<BlockPos> spots = Lists.newArrayList();

            BlockPos.MutableBlockPos m = new BlockPos.MutableBlockPos(startPos.getX(), startPos.getY(), startPos.getZ());
            for(int x = startPos.getX() - 2; x < startPos.getX() + 2; x++)
            {
                for(int z = startPos.getZ() - 2; z < startPos.getZ() + 2; z++)
                {
                    m.set(x, startPos.getY(), z);
                    BlockState checkState = level.getBlockState(m);
                    if(checkState.is(BlockTags.FLOWERS))
                    {
                        spots.add(new BlockPos(m.getX(), m.getY(), m.getZ()));
                    }
                }
            }
            if(spots.isEmpty()) return null;

            BlockPos pos = spots.get(random.nextInt(spots.size()));
            if(pos != null) targetState = level.getBlockState(pos);

            return pos;
        }
    }

    public class FollowEntityGoal extends Goal
    {
        public LivingEntity followPartner;
        private int timer = 0;

        public FollowEntityGoal()
        {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse()
        {
            if(hasAction() || getActionCooldown() >= 0 || random.nextInt(10) != 0) return false;

            followPartner = findFollowPartner();

            return followPartner != null;
        }

        @Override
        public boolean canContinueToUse()
        {
            return this.followPartner.isAlive() && this.timer < MAX_FOLLOW_TIME;
        }


        @Override
        public void stop()
        {
            this.followPartner = null;
            this.timer = 0;
            setHasAction(false);
        }

        @Override
        public void start()
        {
            setHasAction(true);
            setActionCoolDown(MAX_ACTION_COOLDOWN);
        }

        @Override
        public void tick()
        {
            getLookControl().setLookAt(this.followPartner, 10.0F, (float) getMaxHeadXRot());
            getNavigation().moveTo(this.followPartner, 1D);
            if(distanceToSqr(followPartner) <= 4D) getNavigation().stop();

            ++this.timer;
        }

        private LivingEntity findFollowPartner()
        {
            List<Animal> list = level.getNearbyEntities(Animal.class, VALID_ROOTLING_PARTNER, RootlingEntity.this, getBoundingBox().inflate(8.0D));
            double minDistance = Double.MAX_VALUE;
            LivingEntity closestPossible = null;

            for(Animal e : list)
            {
                if(distanceToSqr(e) < minDistance)
                {
                    if(e instanceof RootlingEntity) continue;

                    closestPossible = e;
                    minDistance = distanceToSqr(e);
                }
            }
            return closestPossible;
        }
    }

    public class DanceGoal extends Goal
    {
        private RootlingEntity partner = null;
        private int timer;

        public DanceGoal()
        {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Flag.JUMP));
        }

        public boolean canUse()
        {

            if(forcedDancePartner != null)
            {
                partner = forcedDancePartner;
                forcedDancePartner = null;
                return true;
            }

            if(hasAction() || getActionCooldown() >= 0) return false;

            this.partner = this.findPartner();
            return this.partner != null;
        }

        @Override
        public boolean canContinueToUse()
        {
            return this.partner.isAlive() && this.partner.hasAction() && this.timer < MAX_DANCE_TIME;
        }

        @Override
        public void stop()
        {
            this.partner = null;
            this.timer = 0;

            setHasAction(false);
            if(partner != null) partner.setHasAction(false);
        }

        @Override
        public void start()
        {
            setHasAction(true);
            partner.setHasAction(true);
            setActionCoolDown(MAX_ACTION_COOLDOWN);
        }

        private RootlingEntity findPartner()
        {
            List<RootlingEntity> list = level.getNearbyEntities(RootlingEntity.class, VALID_ROOTLING_PARTNER, RootlingEntity.this, getBoundingBox().inflate(8.0D));
            double minDistance = Double.MAX_VALUE;
            RootlingEntity closestPossible = null;

            for(RootlingEntity rootlingEntity : list)
            {
                if(!rootlingEntity.hasAction() && rootlingEntity.getActionCooldown() <= 0 && distanceToSqr(rootlingEntity) < minDistance)
                {
                    closestPossible = rootlingEntity;
                    minDistance = distanceToSqr(rootlingEntity);
                }
            }

            if(closestPossible != null) closestPossible.forcedDancePartner = RootlingEntity.this;
            return closestPossible;
        }

        public void tick()
        {
            getLookControl().setLookAt(this.partner, 10.0F, (float) getMaxHeadXRot());
            getNavigation().moveTo(this.partner, 1D);
            ++this.timer;
            if(this.timer < MAX_DANCE_TIME && distanceToSqr(this.partner) < 9.0D)
            {
                getJumpControl().jump();
            }
        }
    }

    public class GetInRainGoal extends Goal
    {
        private double targetX;
        private double targetY;
        private double targetZ;

        public GetInRainGoal()
        {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE));
        }

        public boolean canUse()
        {
            if(level.isRaining() && !level.canSeeSky(getOnPos())) return true;

            return this.targetSkyPos();
        }

        protected boolean targetSkyPos()
        {
            Vec3 vec3d = this.locateSkyPos();
            if(vec3d == null)
            {
                return false;
            }else
            {
                this.targetX = vec3d.x;
                this.targetY = vec3d.y;
                this.targetZ = vec3d.z;
                return true;
            }
        }

        public boolean canContinueToUse()
        {
            return !getNavigation().isDone();
        }

        public void start()
        {
            getNavigation().moveTo(this.targetX, this.targetY, this.targetZ, 1);
        }

        protected Vec3 locateSkyPos()
        {
            RandomSource random = getRandom();
            BlockPos blockPos = getOnPos();

            for(int i = 0; i < 10; ++i)
            {
                BlockPos blockPos2 = blockPos.offset(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);
                if(level.canSeeSky(blockPos2) && getWalkTargetValue(blockPos2) < 0.0F)
                {
                    return Vec3.atBottomCenterOf(blockPos2);
                }
            }

            return null;
        }
    }

    private class RootlingFleeGoal extends AvoidEntityGoal<LivingEntity>
    {
        private int soundTime = 0;
        private final TargetingConditions withinRangePredicate;

        public RootlingFleeGoal(PathfinderMob fleeingEntity, Class<LivingEntity> classToFleeFrom, float fleeDistance, double fleeSlowSpeed, double fleeFastSpeed, Predicate<LivingEntity> inclusionSelector)
        {
            super(fleeingEntity, classToFleeFrom, fleeDistance, fleeSlowSpeed, fleeFastSpeed, inclusionSelector);
            this.withinRangePredicate =TargetingConditions.forNonCombat().range(fleeDistance).selector(inclusionSelector.and(inclusionSelector));
        }

        @Override
        public boolean canUse() {
            this.toAvoid = this.mob.level.getNearestEntity(this.avoidClass, this.withinRangePredicate, this.mob, this.mob.getX(), this.mob.getY(), this.mob.getZ(), this.mob.getBoundingBox().inflate(this.maxDist, 3.0D, this.maxDist));
            if (this.toAvoid == null) {
                return false;
            } else {
                Vec3 vec3 = DefaultRandomPos.getPosAway(this.mob, 16, 7, this.toAvoid.position());
                if (vec3 == null) {
                    return false;
                }
                if (this.toAvoid.distanceToSqr(vec3.x, vec3.y, vec3.z) < this.toAvoid.distanceToSqr(this.mob)) {
                    return false;
                }
                this.path = this.pathNav.createPath(vec3.x, vec3.y, vec3.z, 0);
                return this.path != null;
            }
        }

        @Override
        public void start()
        {
            super.start();
            if(soundTime == 0)
                this.mob.playSound(BMEffects.ROOTLING_AFRAID.get(), 1F, 1F + (random.nextFloat() / 10F));
        }

        @Override
        public void tick()
        {
            super.tick();

            soundTime++;
            if(soundTime > 100)
            {
                this.mob.playSound(BMEffects.ROOTLING_AFRAID.get(), 1F, 1F + (random.nextFloat() / 10F));
                soundTime = 0;
            }
        }
    }
}
