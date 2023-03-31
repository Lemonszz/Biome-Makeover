package party.lemons.biomemakeover.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.FlyingMoveControl;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.entity.ai.FlyWanderGoal;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.init.BMItems;
import party.lemons.biomemakeover.util.NetworkUtil;

import java.util.List;
import java.util.Random;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class LightningBugEntity extends ToadTargetEntity implements FlyingAnimal
{
    private LightningBugEntity leader;
    private int groupSize = 1;

    public float scale = random.nextFloat();
    public float prevRed = -1;
    public float prevGreen = -1;
    public float prevBlue = -1;
    private boolean isAlternate = false;

    public LightningBugEntity(EntityType<? extends LightningBugEntity> entityType, Level level) {
        super(entityType, level);

        this.moveControl = new FlyingMoveControl(this, 20, true);
        this.setPathfindingMalus(BlockPathTypes.DANGER_FIRE, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.WATER_BORDER, 16.0F);
        this.setPathfindingMalus(BlockPathTypes.COCOA, -1.0F);
        this.setPathfindingMalus(BlockPathTypes.FENCE, -1.0F);
        tickCount += getRandom().nextInt(10000);
    }

    public LightningBugEntity(Level level, boolean isAlternate)
    {
        this(BMEntities.LIGHTNING_BUG_ALTERNATE.get(), level);
        this.isAlternate = true;
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return createMobAttributes()
                .add(Attributes.FLYING_SPEED, 0.6D)
                .add(Attributes.MAX_HEALTH, 3)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {

        ItemStack heldStack = player.getItemInHand(hand);

        if(!heldStack.isEmpty() && (heldStack.getItem() == Items.GLASS_BOTTLE || heldStack.getItem() == Items.EXPERIENCE_BOTTLE))
        {
            if(!level.isClientSide())
            {
                ItemStack result = ItemUtils.createFilledResult(heldStack, player, new ItemStack(heldStack.getItem() == Items.GLASS_BOTTLE ? BMBlocks.LIGHTNING_BUG_BOTTLE.get() : BMItems.LIGHTNING_BOTTLE.get()));
                player.setItemInHand(hand, result);
                remove(RemovalReason.DISCARDED);
                player.playSound(SoundEvents.BOTTLE_FILL, 1F, 1F);
            }
            return InteractionResult.SUCCESS;
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void baseTick() {
        if(firstTick && !isAlternate)
        {
            for(int i = 0; i < random.nextInt(5); i++)
            {
                LightningBugEntity alternate = BMEntities.LIGHTNING_BUG_ALTERNATE.get().create(level);
                alternate.isAlternate = true;
                alternate.moveTo(getX(), getY(), getZ());
                level.addFreshEntity(alternate);
            }
        }

        super.baseTick();
    }

    @Override
    public float getWalkTargetValue(BlockPos blockPos, LevelReader levelReader) {
        return levelReader.getBlockState(blockPos).isAir() ? 10.0F : 0.0F;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(8, new FlyWanderGoal(this));
        this.goalSelector.addGoal(9, new FloatGoal(this));
        this.goalSelector.addGoal(5, new FollowGroupLeaderGoal(this));
    }

    @Override
    public boolean causeFallDamage(float f, float g, DamageSource damageSource) {
        return false;
    }

    @Override
    protected void checkFallDamage(double d, boolean bl, BlockState blockState, BlockPos blockPos) {
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        FlyingPathNavigation birdNavigation = new FlyingPathNavigation(this, level)
        {
            @Override
            public boolean isStableDestination(BlockPos blockPos) {
                return !this.level.getBlockState(blockPos.below()).isAir();
            }
        };
        birdNavigation.setCanPassDoors(true);
        birdNavigation.setCanFloat(false);
        birdNavigation.setCanOpenDoors(false);
        return birdNavigation;
    }

    public boolean hasLeader()
    {
        return this.leader != null && this.leader.isAlive();
    }

    @Override
    protected void playStepSound(BlockPos blockPos, BlockState blockState) {

    }

    public LightningBugEntity joinGroupOf(LightningBugEntity groupLeader)
    {
        this.leader = groupLeader;
        groupLeader.increaseGroupSize();
        return groupLeader;
    }

    public void leaveGroup()
    {
        this.leader.decreaseGroupSize();
        this.leader = null;
    }

    private void increaseGroupSize()
    {
        ++this.groupSize;
    }

    private void decreaseGroupSize()
    {
        --this.groupSize;
    }

    public boolean canHaveMoreInGroup()
    {
        return this.hasOthersInGroup() && this.groupSize < this.getMaxGroupSize();
    }


    public boolean hasOthersInGroup()
    {
        return this.groupSize > 1;
    }

    public boolean isCloseEnoughToLeader()
    {
        return this.distanceToSqr(this.leader) <= 121.0D;
    }

    public void tick()
    {
        super.tick();
        if(this.hasOthersInGroup() && this.random.nextInt(200) == 1)
        {
            List<LightningBugEntity> list = this.level.getEntitiesOfClass(LightningBugEntity.class, this.getBoundingBox().inflate(8.0D, 8.0D, 8.0D));
            if(list.size() <= 1)
            {
                this.groupSize = 1;
            }
        }
    }
    public int getMaxGroupSize()
    {
        return super.getMaxSpawnClusterSize();
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return getMaxGroupSize();
    }

    @Override
    public void aiStep() {
        super.aiStep();

        if(random.nextInt(200) == 0) NetworkUtil.doLightningEntity(level, this, 2);
    }

    public void moveTowardLeader()
    {
        if(this.hasLeader())
        {
            this.getNavigation().moveTo(this.leader, 1.0D);
        }

    }

    public void pullInOthers(Stream<LightningBugEntity> fish)
    {
        fish.limit(this.getMaxGroupSize() - this.groupSize).filter((e)->e != this).forEach((e)->
        {
            e.joinGroupOf(this);
        });
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        spawnGroupData = super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
        if(spawnGroupData == null)
        {
            spawnGroupData = new LightningBugEntity.GroupInfo(this);
        }else
        {
            this.joinGroupOf(((LightningBugEntity.GroupInfo) spawnGroupData).leader);
        }

        return spawnGroupData;
    }

    @Override
    public boolean isFlying() {
        return true;
    }


	public record GroupInfo(LightningBugEntity leader) implements SpawnGroupData {
    }

    private static class FollowGroupLeaderGoal extends Goal
    {
        private final LightningBugEntity entity;
        private int moveDelay;
        private int checkSurroundingDelay;

        public FollowGroupLeaderGoal(LightningBugEntity entity)
        {
            this.entity = entity;
            this.checkSurroundingDelay = this.getSurroundingSearchDelay(entity);
        }

        protected int getSurroundingSearchDelay(LightningBugEntity entity)
        {
            return 200 + entity.getRandom().nextInt(200) % 20;
        }


        @Override
        public boolean canUse() {
            if(this.entity.hasOthersInGroup())
            {
                return false;
            }else if(this.entity.hasLeader())
            {
                return true;
            }else if(this.checkSurroundingDelay > 0)
            {
                --this.checkSurroundingDelay;
                return false;
            }else
            {
                this.checkSurroundingDelay = this.getSurroundingSearchDelay(this.entity);
                Predicate<LightningBugEntity> predicate = (e)->e.canHaveMoreInGroup() || !e.hasLeader();
                List<LightningBugEntity> list = this.entity.level.getEntitiesOfClass(LightningBugEntity.class, this.entity.getBoundingBox().inflate(8.0D, 8.0D, 8.0D), predicate);
                LightningBugEntity e = list.stream().filter(LightningBugEntity::canHaveMoreInGroup).findAny().orElse(this.entity);
                e.pullInOthers(list.stream().filter((lnb)->!lnb.hasLeader()));
                return this.entity.hasLeader();
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.entity.hasLeader() && this.entity.isCloseEnoughToLeader();
        }

        public void start()
        {
            this.moveDelay = 0;
        }

        public void stop()
        {
            this.entity.leaveGroup();
        }

        public void tick()
        {
            if(--this.moveDelay <= 0)
            {
                this.moveDelay = 10;
                this.entity.moveTowardLeader();
            }
        }
    }

    public static boolean checkSpawnRules(EntityType<LightningBugEntity> lightningBugEntityEntityType, ServerLevelAccessor level, MobSpawnType mobSpawnType, BlockPos pos, RandomSource randomSource)
    {
        return level.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) && level.getRawBrightness(pos, 0) > 8;
    }
}
