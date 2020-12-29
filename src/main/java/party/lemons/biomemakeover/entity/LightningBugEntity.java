package party.lemons.biomemakeover.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import party.lemons.biomemakeover.entity.ai.FlyWanderAroundGoal;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.util.NetworkUtil;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class LightningBugEntity extends ToadTargetEntity
{
	private LightningBugEntity leader;
	private int groupSize = 1;

	private AttributeContainer attributeContainer;
	public float scale = random.nextFloat();
	public float prevRed = -1;
	public float prevGreen = -1;
	public float prevBlue = -1;
	private boolean isAlternate = false;

	public LightningBugEntity(World world)
	{
		super(BMEntities.LIGHTNING_BUG, world);
		this.moveControl = new FlightMoveControl(this, 20, true);
		this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
		this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
		this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 16.0F);
		this.setPathfindingPenalty(PathNodeType.COCOA, -1.0F);
		this.setPathfindingPenalty(PathNodeType.FENCE, -1.0F);
		age += world.random.nextInt(10000);
	}

	public LightningBugEntity(EntityType<LightningBugEntity> type, World world)
	{
		super(type, world);
		this.moveControl = new FlightMoveControl(this, 20, true);
		this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
		this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
		this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 16.0F);
		this.setPathfindingPenalty(PathNodeType.COCOA, -1.0F);
		this.setPathfindingPenalty(PathNodeType.FENCE, -1.0F);
		age += world.random.nextInt(10000);
	}

	public LightningBugEntity(World world, boolean isAlternate)
	{
		this(BMEntities.LIGHTNING_BUG_ALTERNATE, world);
		this.isAlternate = true;
	}

	@Override
	public void baseTick()
	{
		if(firstUpdate && !isAlternate)
		{
			for(int i = 0; i < random.nextInt(3); i++)
			{
				LightningBugEntity alternate =  BMEntities.LIGHTNING_BUG_ALTERNATE.create(world);
				alternate.isAlternate = true;
				alternate.updatePosition(getX(), getY(), getZ());
				world.spawnEntity(alternate);
			}
		}

		super.baseTick();
	}

	public float getPathfindingFavor(BlockPos pos, WorldView world) {
		return world.getBlockState(pos).isAir() ? 10.0F : 0.0F;
	}

	protected void initGoals() {
		this.goalSelector.add(8, new FlyWanderAroundGoal(this));
		this.goalSelector.add(9, new SwimGoal(this));
		this.goalSelector.add(5, new FollowGroupLeaderGoal(this));
	}

	public boolean handleFallDamage(float fallDistance, float damageMultiplier) {
		return false;
	}

	protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
	}

	protected boolean hasWings() {
		return true;
	}

	protected EntityNavigation createNavigation(World world) {
		BirdNavigation birdNavigation = new BirdNavigation(this, world) {
			public boolean isValidPosition(BlockPos pos) {
				return !this.world.getBlockState(pos.down()).isAir();
			}
		};
		birdNavigation.setCanPathThroughDoors(false);
		birdNavigation.setCanSwim(false);
		birdNavigation.setCanEnterOpenDoors(true);
		return birdNavigation;
	}

	public int getLimitPerChunk() {
		return this.getMaxGroupSize();
	}

	public int getMaxGroupSize() {
		return super.getLimitPerChunk();
	}

	protected boolean hasSelfControl() {
		return !this.hasLeader();
	}

	public boolean hasLeader() {
		return this.leader != null && this.leader.isAlive();
	}

	public LightningBugEntity joinGroupOf(LightningBugEntity groupLeader) {
		this.leader = groupLeader;
		groupLeader.increaseGroupSize();
		return groupLeader;
	}

	public void leaveGroup() {
		this.leader.decreaseGroupSize();
		this.leader = null;
	}

	private void increaseGroupSize() {
		++this.groupSize;
	}

	private void decreaseGroupSize() {
		--this.groupSize;
	}

	public boolean canHaveMoreInGroup() {
		return this.hasOthersInGroup() && this.groupSize < this.getMaxGroupSize();
	}

	public void tick() {
		super.tick();
		if (this.hasOthersInGroup() && this.world.random.nextInt(200) == 1) {
			List<LightningBugEntity> list = this.world.getNonSpectatingEntities(this.getClass(), this.getBoundingBox().expand(8.0D, 8.0D, 8.0D));
			if (list.size() <= 1) {
				this.groupSize = 1;
			}
		}
	}

	@Override
	public void tickMovement()
	{
		super.tickMovement();

		if(random.nextInt(200) == 0) NetworkUtil.doLightningEntity(world, this, 2);
	}

	public boolean hasOthersInGroup() {
		return this.groupSize > 1;
	}

	public boolean isCloseEnoughToLeader() {
		return this.squaredDistanceTo(this.leader) <= 121.0D;
	}

	public void moveTowardLeader() {
		if (this.hasLeader()) {
			this.getNavigation().startMovingTo(this.leader, 1.0D);
		}

	}

	public void pullInOthers(Stream<LightningBugEntity> fish) {
		fish.limit(this.getMaxGroupSize() - this.groupSize).filter((e) ->e != this).forEach((e) -> {
			e.joinGroupOf(this);
		});
	}

	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CompoundTag entityTag) {
		super.initialize(world, difficulty, spawnReason, entityData, entityTag);
		if (entityData == null) {
			entityData = new LightningBugEntity.GroupInfo(this);
		} else {
			this.joinGroupOf(((LightningBugEntity.GroupInfo)entityData).leader);
		}

		return entityData;
	}

	public static class GroupInfo implements EntityData {
		public final LightningBugEntity leader;

		public GroupInfo(LightningBugEntity leader) {
			this.leader = leader;
		}
	}


	@Override
	public AttributeContainer getAttributes()
	{
		if(attributeContainer == null)
			attributeContainer =  new AttributeContainer(
					MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_FLYING_SPEED, 0.6F).add(EntityAttributes.GENERIC_MAX_HEALTH, 3).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D).build());
		return attributeContainer;
	}

	private static class FollowGroupLeaderGoal extends Goal
	{
		private final LightningBugEntity entity;
		private int moveDelay;
		private int checkSurroundingDelay;

		public FollowGroupLeaderGoal(LightningBugEntity entity) {
			this.entity = entity;
			this.checkSurroundingDelay = this.getSurroundingSearchDelay(entity);
		}

		protected int getSurroundingSearchDelay(LightningBugEntity entity) {
			return 200 + entity.getRandom().nextInt(200) % 20;
		}

		public boolean canStart() {
			if (this.entity.hasOthersInGroup()) {
				return false;
			} else if (this.entity.hasLeader()) {
				return true;
			} else if (this.checkSurroundingDelay > 0) {
				--this.checkSurroundingDelay;
				return false;
			} else {
				this.checkSurroundingDelay = this.getSurroundingSearchDelay(this.entity);
				Predicate<LightningBugEntity> predicate = (e) ->e.canHaveMoreInGroup() || !e.hasLeader();
				List<LightningBugEntity> list = this.entity.world.getEntitiesByClass(this.entity.getClass(), this.entity.getBoundingBox().expand(8.0D, 8.0D, 8.0D), predicate);
				LightningBugEntity e = list.stream().filter(LightningBugEntity::canHaveMoreInGroup).findAny().orElse(this.entity);
				e.pullInOthers(list.stream().filter((lnb) ->!lnb.hasLeader()));
				return this.entity.hasLeader();
			}
		}

		public boolean shouldContinue() {
			return this.entity.hasLeader() && this.entity.isCloseEnoughToLeader();
		}

		public void start() {
			this.moveDelay = 0;
		}

		public void stop() {
			this.entity.leaveGroup();
		}

		public void tick() {
			if (--this.moveDelay <= 0) {
				this.moveDelay = 10;
				this.entity.moveTowardLeader();
			}
		}
	}
}
