package party.lemons.biomemakeover.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetFinder;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.TameableShoulderEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import party.lemons.biomemakeover.entity.ai.PredicateTemptGoal;
import party.lemons.biomemakeover.init.BMEntities;

import java.util.Iterator;
import java.util.function.Predicate;

public class OwlEntity extends TameableShoulderEntity
{
	private static final TrackedData<Integer> STANDING_STATE = DataTracker.registerData(OwlEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Integer> OWL_STATE = DataTracker.registerData(OwlEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final EntityDimensions FLYING_DIMENSION = new EntityDimensions(0.7F, 1.4F, false);
	private static final Predicate<LivingEntity> IS_OWL_TARGET = e->e.getType().isIn(BMEntities.OWL_TARGETS);

	private AttributeContainer attributeContainer;
	private float leaningPitch;
	private float lastLeaningPitch;

	public OwlEntity(World world)
	{
		super(BMEntities.OWL, world);
		this.moveControl = new FlightMoveControl(this, 0,false);
		this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
		this.setPathfindingPenalty(PathNodeType.DAMAGE_FIRE, -1.0F);
	}

	protected void initGoals() {
		super.initGoals();
		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(1, new SitGoal(this));
		this.goalSelector.add(2, new MeleeAttackGoal(this, 1.0D, true));
		this.goalSelector.add(3, new FollowOwnerGoal(this, 1.0D, 10.0F, 2.0F, true));
		this.goalSelector.add(4, new PredicateTemptGoal(this, 1.2D, false, this::isBreedingItem));
		this.goalSelector.add(4, new AnimalMateGoal(this, 1.0D));
		this.goalSelector.add(5, new EscapeDangerGoal(this, 1.25D));
		this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.add(7, new ExtendedFlyOntoTree(this, 1D, 0.5F));
		this.goalSelector.add(8, new WanderAroundGoal(this, 1D));
		this.goalSelector.add(9, new LookAroundGoal(this));
		this.targetSelector.add(1, new TrackOwnerAttackerGoal(this));
		this.targetSelector.add(2, new AttackWithOwnerGoal(this));
		this.targetSelector.add(3, new FollowTargetIfTamedGoal(this, LivingEntity.class, false, IS_OWL_TARGET));
	}

	protected EntityNavigation createNavigation(World world) {
		BirdNavigation birdNavigation = new BirdNavigation(this, world);
		birdNavigation.setCanPathThroughDoors(false);
		birdNavigation.setCanSwim(true);
		birdNavigation.setCanEnterOpenDoors(true);
		return birdNavigation;
	}


	@Override
	protected void initDataTracker()
	{
		super.initDataTracker();
		dataTracker.startTracking(STANDING_STATE, 0);
		dataTracker.startTracking(OWL_STATE, 0);
	}

	@Environment(EnvType.CLIENT)
	@Override
	public float getLeaningPitch(float tickDelta)
	{
		return MathHelper.lerpAngleDegrees(tickDelta, lastLeaningPitch, leaningPitch);
	}

	@Override
	public void tick()
	{
		super.tick();
		setStandingState(onGround || touchingWater || isInSittingPose() ? StandingState.STANDING : StandingState.FLYING);

		/*PlayerEntity playerEntity = world.getClosestPlayer(this, 100F);
		if(playerEntity != null && !world.isClient())
		{
			if(playerEntity.isSneaking())
			{
				setStandingState(StandingState.FLYING);
			}
			else
			{
				setStandingState(StandingState.STANDING);
			}

			if(playerEntity.inventory.selectedSlot == 0)
				setOwlState(OwlState.IDLE);
			else if(playerEntity.inventory.selectedSlot == 1)
				setOwlState(OwlState.ATTACKING);
			else
				setOwlState(OwlState.SLEEPING);
		}*/

		lastLeaningPitch = leaningPitch;
		switch(getStandingState())
		{
			case STANDING:
				this.leaningPitch = Math.max(0.0F, this.leaningPitch - 2F);
				break;
			case FLYING:
				this.leaningPitch = Math.min(7F, this.leaningPitch + 1.5F);
				break;
		}
	}

	public void setTamed(boolean tamed) {
		super.setTamed(tamed);
		if (tamed) {
			this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(20.0D);
			this.setHealth(20.0F);
		} else {
			this.getAttributeInstance(EntityAttributes.GENERIC_MAX_HEALTH).setBaseValue(8.0D);
		}

		this.getAttributeInstance(EntityAttributes.GENERIC_ATTACK_DAMAGE).setBaseValue(4.0D);
	}

	public ActionResult interactMob(PlayerEntity player, Hand hand)
	{
		ItemStack stack = player.getStackInHand(hand);
		Item item = stack.getItem();
		if (this.world.isClient)
		{
			boolean canTame = (this.isOwner(player) || this.isTamed() || isBreedingItem(stack)) && (!this.isTamed() && this.getTarget() != null);
			return canTame ? ActionResult.CONSUME : ActionResult.PASS;
		}
		else
		{
			if (this.isTamed())
			{
				if (this.isBreedingItem(stack) && this.getHealth() < this.getMaxHealth()) {
					if (!player.abilities.creativeMode) {
						stack.decrement(1);
					}

					this.heal((float)item.getFoodComponent().getHunger());
					return ActionResult.SUCCESS;
				}

				ActionResult actionResult = super.interactMob(player, hand);
				if ((!actionResult.isAccepted() || this.isBaby()) && this.isOwner(player))
				{
					this.setSitting(!this.isSitting());
					this.jumping = false;
					this.navigation.stop();
					this.setTarget(null);
					return ActionResult.SUCCESS;
				}
				return actionResult;
			}
			else if (isBreedingItem(stack) && this.getTarget() == null)
			{
				if (!player.abilities.creativeMode) {
					stack.decrement(1);
				}

				if (this.random.nextInt(3) == 0)
				{
					this.setOwner(player);
					this.navigation.stop();
					this.setTarget(null);
					this.setSitting(true);
					this.world.sendEntityStatus(this, (byte)7);
				}
				else
				{
					this.world.sendEntityStatus(this, (byte)6);
				}
				return ActionResult.SUCCESS;
			}
			return super.interactMob(player, hand);
		}
	}

	public boolean isBreedingItem(ItemStack stack)
	{
		Item item = stack.getItem();
		return item.isFood() && item.getFoodComponent().isMeat();
	}

	@Override
	public void writeCustomDataToTag(CompoundTag tag)
	{
		super.writeCustomDataToTag(tag);
		tag.putInt("OwlState", dataTracker.get(OWL_STATE));
		tag.putInt("StandingState", dataTracker.get(STANDING_STATE));
	}

	@Override
	public void readCustomDataFromTag(CompoundTag tag)
	{
		super.readCustomDataFromTag(tag);
		setOwlState(OwlState.values()[tag.getInt("OwlState")]);
		setStandingState(StandingState.values()[tag.getInt("StandingState")]);
	}

	@Override
	public PassiveEntity createChild(ServerWorld world, PassiveEntity entity)
	{
		return null;
	}

	@Override
	public AttributeContainer getAttributes()
	{
		if(attributeContainer == null)
			attributeContainer = new AttributeContainer(AnimalEntity.createMobAttributes()
					.add(EntityAttributes.GENERIC_MAX_HEALTH, 6.0D)
					.add(EntityAttributes.GENERIC_FLYING_SPEED, 0.8D)
					.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.4D)
					.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 2D)
					.build());

		return attributeContainer;
	}

	public EntityDimensions getDimensions(EntityPose pose) {
		return getStandingState() == StandingState.STANDING ? super.getDimensions(pose) : FLYING_DIMENSION;
	}

	public boolean handleFallDamage(float fallDistance, float damageMultiplier) {
		return false;
	}

	protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
	}

	protected boolean hasWings() {
		return true;
	}


	public void setOwlState(OwlState owlState)
	{
		dataTracker.set(OWL_STATE, owlState.ordinal());
	}

	public void setStandingState(StandingState standingState)
	{
		dataTracker.set(STANDING_STATE, standingState.ordinal());
	}

	public StandingState getStandingState()
	{
		return StandingState.values()[dataTracker.get(STANDING_STATE)];
	}

	public OwlState getOwlState()
	{
		return OwlState.values()[dataTracker.get(OWL_STATE)];
	}

	private static class ExtendedFlyOntoTree extends WanderAroundFarGoal
	{
		public ExtendedFlyOntoTree(PathAwareEntity pathAwareEntity, double speed, float probability) {
			super(pathAwareEntity, speed, probability);
		}

		protected Vec3d getWanderTarget() {
			Vec3d vec3d = null;
			if (this.mob.isTouchingWater()) {
				vec3d = TargetFinder.findGroundTarget(this.mob, 15, 15);
			}

			if (this.mob.getRandom().nextFloat() >= this.probability) {
				vec3d = this.getTreeTarget();
			}

			return vec3d == null ? super.getWanderTarget() : vec3d;
		}

		private Vec3d getTreeTarget() {
			BlockPos blockPos = this.mob.getBlockPos();
			BlockPos.Mutable mutable = new BlockPos.Mutable();
			BlockPos.Mutable mutable2 = new BlockPos.Mutable();
			Iterable<BlockPos> iterable = BlockPos.iterate(MathHelper.floor(this.mob.getX() - 3.0D), MathHelper.floor(this.mob.getY() - 6.0D), MathHelper.floor(this.mob.getZ() - 3.0D), MathHelper.floor(this.mob.getX() + 3.0D), MathHelper.floor(this.mob.getY() + 6.0D), MathHelper.floor(this.mob.getZ() + 3.0D));
			Iterator var5 = iterable.iterator();

			BlockPos blockPos2;
			boolean bl;
			do {
				do {
					if (!var5.hasNext()) {
						return null;
					}

					blockPos2 = (BlockPos)var5.next();
				} while(blockPos.equals(blockPos2));

				Block block = this.mob.world.getBlockState(mutable2.set(blockPos2, Direction.DOWN)).getBlock();
				bl = block instanceof LeavesBlock || block.isIn(BlockTags.LOGS);
			} while(!bl || !this.mob.world.isAir(blockPos2) || !this.mob.world.isAir(mutable.set(blockPos2, Direction.UP)));

			return Vec3d.ofBottomCenter(blockPos2);
		}
	}

	public enum StandingState
	{
		STANDING,
		FLYING
	}

	public enum OwlState
	{
		IDLE,
		ATTACKING,
		SLEEPING
	}
}
