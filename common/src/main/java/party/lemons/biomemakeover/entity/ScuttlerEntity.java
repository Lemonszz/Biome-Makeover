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
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.init.BMItems;
import party.lemons.taniwha.entity.ai.TagTemptGoal;
import party.lemons.taniwha.util.ItemUtil;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class ScuttlerEntity extends Animal {

    private static final EntityDataAccessor<Boolean> RATTLING = SynchedEntityData.defineId(ScuttlerEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> EATING = SynchedEntityData.defineId(ScuttlerEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> PASSIVE = SynchedEntityData.defineId(ScuttlerEntity.class, EntityDataSerializers.BOOLEAN);

    public static final ResourceLocation EAT_LOOT_TABLE = BiomeMakeover.ID("gameplay/scuttler_eating");

    private static final int FIND_EAT_TARGET_COOLDOWN_MAX = 300;

    public float rattleTime = 0;
    private int eatCooldown = 100;
    public int eatTime = 0;
    private BlockPos eatTarget = null;
    private int findEatTargetCooldown = -69;

    public ScuttlerEntity(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);

        if(findEatTargetCooldown == -69)
            findEatTargetCooldown = this.getRandom().nextInt(0, FIND_EAT_TARGET_COOLDOWN_MAX);
    }

    @Override
    protected void registerGoals() {

        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new TagTemptGoal(this, BMItems.SCUTTLER_FOOD, 0.7D, false));
        this.goalSelector.addGoal(2, new RattleGoal<>(this, 20.0F, Player.class));
        this.goalSelector.addGoal(3, new PanicGoal(this, 1.25D));
        this.goalSelector.addGoal(4, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(5, new AvoidEntityGoal<>(this, Player.class, 16.0F, 1.6D, 1.4D, (livingEntity)->!isPassive()));
        this.goalSelector.addGoal(6, new EatFlowerGoal());
        this.goalSelector.addGoal(7, new FollowParentGoal(this, 1.1D));
        this.goalSelector.addGoal(8, new FindFlowerGoal());
        this.goalSelector.addGoal(9, new AvoidDaylightGoal(1.0D));
        this.goalSelector.addGoal(10, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(11, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(12, new RandomLookAroundGoal(this));
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return createMobAttributes().add(Attributes.MAX_HEALTH, 10).add(Attributes.MOVEMENT_SPEED, 0.25F);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(RATTLING, false);
        entityData.define(EATING, false);
        entityData.define(PASSIVE, false);
    }

    @Override
    public void tick() {
        super.tick();

        if(findEatTargetCooldown > 0)
            findEatTargetCooldown--;

        eatCooldown--;
        if(getEntityData().get(EATING))
            eatTime--;

        if(entityData.get(RATTLING))
        {
            double dir = Math.signum(Math.sin(rattleTime));
            rattleTime++;

            if(dir != Math.signum(Math.sin(rattleTime)))
            {
                playSound(BMEffects.SCUTTLER_RATTLE.get(), 0.25F, 0.75F + random.nextFloat());
            }
        }else
        {
            rattleTime = 0;
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        Item item = itemStack.getItem();
        if(this.level.isClientSide())
        {
            if(this.isFood(itemStack))
            {
                return InteractionResult.SUCCESS;
            }
        }else
        {
            if(entityData.get(PASSIVE))
            {
                if(item.getFoodProperties() != null && this.isFood(itemStack) && this.getHealth() < this.getMaxHealth())
                {
                    this.eat(level, itemStack);
                    this.heal((float) item.getFoodProperties().getNutrition());
                    return InteractionResult.CONSUME;
                }
            }else if(this.isFood(itemStack))
            {
                this.eat(level, itemStack);
                if(this.random.nextInt(3) == 0)
                {
                    entityData.set(PASSIVE, true);
                    this.level.broadcastEntityEvent(this, (byte) 7);
                }else
                {
                    this.level.broadcastEntityEvent(this, (byte) 6);
                }
                this.setPersistenceRequired();
                return InteractionResult.CONSUME;
            }
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        if(damageSource.is(BMEntities.SCUTTLER_IMMUNE_DAMAGE))
            return true;

        return super.isInvulnerableTo(damageSource);
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(BMItems.SCUTTLER_FOOD);
    }

    @Override
    public boolean canBeLeashed(Player player) {
        return super.canBeLeashed(player) && isPassive();
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> entityDataAccessor) {
        super.onSyncedDataUpdated(entityDataAccessor);
        if(entityDataAccessor == RATTLING) rattleTime = 0;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        ScuttlerEntity baby = BMEntities.SCUTTLER.get().create(serverLevel);
        baby.setPassive(true);
        return baby;
    }

    public boolean isPassive()
    {
        return entityData.get(PASSIVE);
    }

    public void setPassive(boolean passive)
    {
        entityData.set(PASSIVE, passive);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.putBoolean("Passive", isPassive());
        tag.putInt("EatCooldown", eatCooldown);
        tag.putInt("EatTargetCooldown", findEatTargetCooldown);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        setPassive(tag.getBoolean("Passive"));
        eatCooldown = tag.getInt("EatCooldown");
        findEatTargetCooldown = tag.getInt("EatTargetCooldown");
    }

    @Override
    protected void playStepSound(BlockPos blockPos, BlockState state) {
        if(!state.getMaterial().isLiquid())
        {
            playSound(BMEffects.SCUTTLER_STEP.get(), 0.10F, 1.25F + random.nextFloat());
            spawnSprintParticle();
        }
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return BMEffects.SCUTTLER_DEATH.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return BMEffects.SCUTTLER_RATTLE.get();
    }

    @Override
    public int getAmbientSoundInterval() {
        return super.getAmbientSoundInterval() * 3;
    }

    @Override
    protected float getStandingEyeHeight(Pose pose, EntityDimensions entityDimensions) {
        return 0.2F;
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 1;
    }

    public static boolean checkSpawnRules(EntityType<ScuttlerEntity> type, ServerLevelAccessor level, MobSpawnType mobSpawnType, BlockPos pos, RandomSource random)
    {
        return random.nextBoolean() && level.getEntitiesOfClass(ScuttlerEntity.class, new AABB(pos).inflate(50), (e)->true).isEmpty() && isBrightEnoughToSpawn(level, pos);
    }

    private static class RattleGoal<T extends LivingEntity> extends Goal
    {
        private final ScuttlerEntity scuttler;
        private final float distance;
        private final Class<T> targetClass;
        T targetEntity;
        private final TargetingConditions withinRangePredicate;

        public RattleGoal(ScuttlerEntity scuttlerEntity, float distance, Class<T> target)
        {
            this.setFlags(EnumSet.of(Goal.Flag.LOOK, Goal.Flag.MOVE));
            this.scuttler = scuttlerEntity;
            this.distance = distance;
            this.targetClass = target;

            Predicate<LivingEntity> s = (l)->true;
            this.withinRangePredicate = TargetingConditions.forNonCombat().range(distance).selector(s.and(EntitySelector.NO_CREATIVE_OR_SPECTATOR));
        }

        @Override
        public boolean canUse()
        {
            if(scuttler.isInWater() || scuttler.isPassive()) return false;

            this.targetEntity = scuttler.level.
                    getNearestEntity(targetClass, this.withinRangePredicate, this.scuttler, this.scuttler.getX(), this.scuttler.getY(), this.scuttler.getZ(), this.scuttler.getBoundingBox().inflate(this.distance, 3.0D, this.distance));
            if(this.targetEntity == null || !scuttler.hasLineOfSight(targetEntity) || !targetEntity.hasLineOfSight(scuttler))
            {

                return false;
            }else
            {
                return !targetEntity.isHolding(i->i.is(BMItems.SCUTTLER_FOOD)) && scuttler.distanceTo(targetEntity) >= distance / 2;
            }
        }

        @Override
        public boolean canContinueToUse()
        {
            if(targetEntity.isHolding(i->i.is(BMItems.SCUTTLER_FOOD))) return false;

            double d = scuttler.distanceTo(targetEntity);
            return d > distance / 2 && d < distance && scuttler.hasLineOfSight(targetEntity) && targetEntity.hasLineOfSight(scuttler);
        }

        @Override
        public void stop()
        {
            scuttler.getEntityData().set(ScuttlerEntity.RATTLING, false);
            super.stop();
        }

        @Override
        public void start()
        {
            scuttler.getEntityData().set(ScuttlerEntity.RATTLING, true);
            scuttler.getNavigation().stop();
            super.start();
        }

        @Override
        public void tick()
        {
            scuttler.lookControl.setLookAt(targetEntity, 30, 30);
        }
    }

    class AvoidDaylightGoal extends FleeSunGoal
    {
        private int timer = 100;

        AvoidDaylightGoal(double speed)
        {
            super(ScuttlerEntity.this, speed);
        }

        @Override
        public boolean canUse()
        {
            if(this.mob.getTarget() == null)
            {
                if(this.timer > 0)
                {
                    --this.timer;
                    return false;
                }else
                {
                    this.timer = 100;
                    BlockPos pos = this.mob.blockPosition();
                    return ScuttlerEntity.this.level.isDay() && ScuttlerEntity.this.level.canSeeSky(pos) && this.setWantedPos();
                }
            }else
            {
                return false;
            }
        }
    }

    public class EatFlowerGoal extends Goal
    {
        public EatFlowerGoal()
        {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse()
        {
            return eatTarget != null;
        }

        @Override
        public void tick()
        {
            if(eatTarget == null)
                return;

            Vec3 targetPosition = eatTarget.getCenter();

            double dist = distanceToSqr(targetPosition);
            if(!blockPosition().equals(eatTarget) && dist > 0.25F) {
                getNavigation().moveTo(targetPosition.x, targetPosition.y, targetPosition.z, 0.6F);
            }
            else
                getNavigation().stop();

            getLookControl().setLookAt(targetPosition.x, targetPosition.y - 0.25F, targetPosition.z);

            if(eatTime <= 1)
            {
                BlockState st = level.getBlockState(eatTarget);
                if(st.is(BMBlocks.BARREL_CACTUS_FLOWERED.get()))
                {
                    BlockState setState = BMBlocks.BARREL_CACTUS.get().defaultBlockState();
                    level.blockEvent(eatTarget, BMBlocks.BARREL_CACTUS.get(), 1, 0);
                    level.setBlock(eatTarget, setState, Block.UPDATE_CLIENTS);
                    level.gameEvent(GameEvent.BLOCK_CHANGE, eatTarget, GameEvent.Context.of(setState));
                    ItemUtil.dropLootTable(level, eatTarget.getX(), eatTarget.getY(), eatTarget.getZ(), EAT_LOOT_TABLE);
                }
                eatTarget = null;
            }
        }

        @Override
        public boolean canContinueToUse()
        {
            if(!canUse())
                return false;

            BlockState st = level.getBlockState(eatTarget);
            return st.is(BMBlocks.BARREL_CACTUS_FLOWERED.get());
        }

        @Override
        public void start()
        {
            getEntityData().set(EATING, true);
            eatTime = 40;
        }

        @Override
        public void stop()
        {
            getNavigation().stop();
            getEntityData().set(EATING, false);
        }
    }

    private class FindFlowerGoal extends Goal
    {
        @Override
        public boolean canUse()
        {
            return findEatTargetCooldown <= 0;
        }

        @Override
        public boolean canContinueToUse()
        {
            return false;
        }

        @Override
        public void start()
        {
            super.start();
            eatTarget = findCactus();
            findEatTargetCooldown = FIND_EAT_TARGET_COOLDOWN_MAX;
        }

        private BlockPos findCactus()
        {
            BlockPos startPos = blockPosition();
            List<BlockPos> spots = Lists.newArrayList();
            final int range = 4;

            BlockPos.MutableBlockPos m = new BlockPos.MutableBlockPos(startPos.getX(), startPos.getY(), startPos.getZ());
            for(int x = startPos.getX() - range; x < startPos.getX() + range; x++)
            {
                for(int z = startPos.getZ() - range; z < startPos.getZ() + range; z++)
                {
                    m.set(x, startPos.getY(), z);
                    BlockState checkState = level.getBlockState(m);
                    if(checkState.is(BMBlocks.BARREL_CACTUS_FLOWERED.get()))
                    {
                        spots.add(new BlockPos(m.getX(), m.getY(), m.getZ()));
                    }
                }
            }
            if(spots.isEmpty()) return null;
            return spots.get(random.nextInt(spots.size()));
        }
    }
}
