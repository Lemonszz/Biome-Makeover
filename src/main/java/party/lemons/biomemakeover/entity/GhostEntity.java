package party.lemons.biomemakeover.entity;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.ai.Durations;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.world.PoltergeistHandler;

import java.util.EnumSet;
import java.util.UUID;

public class GhostEntity extends HostileEntity implements Angerable
{
	private static final TrackedData<Boolean> IsCharging = DataTracker.registerData(GhostEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private static final IntRange WANDER_RANGE_HORIZONTAL = IntRange.between(-7, 7);
	private static final IntRange WANDER_RANGE_VERTICAL = IntRange.between(-5, 5);
	private static final double CHARGE_MIN_DISTANCE = 2;
	private static final double CHARGE_MAX_DISTANCE = 10;
	private static final int POLTERGEIST_RANGE = 10;

	private static final IntRange ANGER_TIME_RANGE = Durations.betweenSeconds(20, 39);
	private int angerTime;
	private UUID targetUuid;
	private static final IntRange REINFORCE_TIME_RANGE = Durations.betweenSeconds(4, 6);
	private int reinforceTime;
	private static final IntRange ANGER_SOUND_TIME_RANGE = Durations.betweenSeconds(0, 1);
	private int angrySoundDelay;
	private BlockPos homePosition;

	private AttributeContainer attributeContainer;

	public GhostEntity(World world)
	{
		super(BMEntities.GHOST, world);
		this.moveControl = new GhostMoveControl(this);
	}

	@Override
	protected void initGoals() {
		super.initGoals();

		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(1, new PoltergeistGoal());
		this.goalSelector.add(4, new ChargeTargetGoal());
		this.goalSelector.add(8, new FlyAroundGoal());
		this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 3.0F, 1.0F));
		this.goalSelector.add(10, new LookAtEntityGoal(this, MobEntity.class, 8.0F));
		this.targetSelector.add(1, (new RevengeGoal(this)).setGroupRevenge());
		this.targetSelector.add(2, new FollowTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::shouldAngerAt));
		this.targetSelector.add(3, new UniversalAngerGoal<>(this, true));
	}

	@Override
	protected void initDataTracker()
	{
		super.initDataTracker();
		this.dataTracker.startTracking(IsCharging, false);
	}

	@Override
	public AttributeContainer getAttributes()
	{
		if(attributeContainer == null)
			attributeContainer =  new AttributeContainer(
					HostileEntity.createHostileAttributes().build());
		return attributeContainer;
	}

	@Override
	public void move(MovementType type, Vec3d movement) {
		super.move(type, movement);
		this.checkBlockCollision();
	}

	@Override
	public void tick() {
		this.noClip = true;
		super.tick();
		this.noClip = false;
		this.setNoGravity(true);

		if (getHomePosition() == null) {
			homePosition = GhostEntity.this.getBlockPos();
		}
	}

	@Override
	protected void mobTick()
	{
		if (this.hasAngerTime()) {
			this.tickAngerSounds();
		}

		this.tickAngerLogic((ServerWorld)this.world, true);
		if (this.getTarget() != null) {
			this.checkAlertTime();
		}
		super.mobTick();
	}

	private void checkAlertTime() {
		if (this.reinforceTime > 0) {
			--this.reinforceTime;
		} else {
			if (this.getVisibilityCache().canSee(this.getTarget())) {
				this.alertNearby();
			}

			this.reinforceTime = REINFORCE_TIME_RANGE.choose(this.random);
		}
	}

	private void alertNearby() {
		double alertRange = this.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE);
		Box alertBounds = Box.method_29968(this.getPos()).expand(alertRange, 10.0D, alertRange);
		this.world.getEntitiesIncludingUngeneratedChunks(GhostEntity.class, alertBounds).stream()
				.filter((e) ->e != this)
				.filter((e) ->e.getTarget() == null)
				.filter((e) ->getTarget() != null && !e.isTeammate(this.getTarget()))
				.forEach((e) ->e.setTarget(this.getTarget()));
	}

	public void setTarget(LivingEntity target) {
		if (this.getTarget() == null && target != null) {
			this.angrySoundDelay = ANGER_SOUND_TIME_RANGE.choose(this.random);
			this.reinforceTime = REINFORCE_TIME_RANGE.choose(this.random);
		}

		if (target instanceof PlayerEntity) {
			this.setAttacking((PlayerEntity)target);
		}

		super.setTarget(target);
	}

	private void tickAngerSounds() {
		if (this.angrySoundDelay > 0) {
			--this.angrySoundDelay;
			if (this.angrySoundDelay == 0) {
				this.playAngerSound();
			}
		}

	}

	private void playAngerSound() {
		this.playSound(BMEffects.GHOST_ANGRY, this.getSoundVolume() * 2.0F, this.getSoundPitch());
	}

	public void chooseRandomAngerTime() {
		this.setAngerTime(ANGER_TIME_RANGE.choose(this.random));
	}

	@Override
	public void setAngryAt(UUID uuid)
	{
		this.targetUuid = uuid;
	}

	@Override
	public void writeCustomDataToTag(CompoundTag tag) {
		super.writeCustomDataToTag(tag);
		this.angerToTag(tag);

		if (this.homePosition != null) {
			tag.putInt("HomeX", this.homePosition.getX());
			tag.putInt("HomeY", this.homePosition.getY());
			tag.putInt("HomeZ", this.homePosition.getZ());
		}
	}
	@Override
	public void readCustomDataFromTag(CompoundTag tag) {
		super.readCustomDataFromTag(tag);
		this.angerFromTag((ServerWorld)this.world, tag);

		if (tag.contains("HomeX")) {
			this.homePosition = new BlockPos(tag.getInt("HomeX"), tag.getInt("HomeY"), tag.getInt("HomeZ"));
		}
	}

	public void setAngerTime(int ticks) {
		this.angerTime = ticks;
	}

	public int getAngerTime() {
		return this.angerTime;
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		return !this.isInvulnerableTo(source) && super.damage(source, amount);
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return this.hasAngerTime() ? BMEffects.GHOST_ANGRY : BMEffects.GHOST_IDLE;
	}

	public UUID getAngryAt() {
		return this.targetUuid;
	}

	@Override
	public boolean isAngryAt(PlayerEntity player) {
		return this.shouldAngerAt(player);
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source) {
		return BMEffects.GHOST_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return BMEffects.GHOST_DEATH;
	}

	private void setCharging(boolean charging) {

		this.dataTracker.set(IsCharging, charging);
	}

	public boolean isCharging() {
		return dataTracker.get(IsCharging);
	}

	private BlockPos getHomePosition() {
		return this.homePosition;
	}

	class GhostMoveControl extends MoveControl {
		private GhostMoveControl(GhostEntity owner) {
			super(owner);
		}

		@Override
		public void tick()
		{
			if (this.state == MoveControl.State.MOVE_TO)
			{
				Vec3d targetPosition = new Vec3d(this.targetX - GhostEntity.this.getX(), this.targetY - GhostEntity.this.getY(), this.targetZ - GhostEntity.this.getZ());
				double length = targetPosition.length();
				if (length < GhostEntity.this.getBoundingBox().getAverageSideLength())
				{
					this.state = MoveControl.State.WAIT;
					GhostEntity.this.setVelocity(GhostEntity.this.getVelocity().multiply(0.5D));
				}
				else
					{
					GhostEntity.this.setVelocity(GhostEntity.this.getVelocity().add(targetPosition.multiply(this.speed * 0.05D / length)));
					if (GhostEntity.this.getTarget() == null)
					{
						if(EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(getTarget()))
							setTarget(null);

						Vec3d vec3d2 = GhostEntity.this.getVelocity();
						GhostEntity.this.yaw = -((float)MathHelper.atan2(vec3d2.x, vec3d2.z)) * 57.295776F;
						GhostEntity.this.bodyYaw = GhostEntity.this.yaw;
					}
					else
					{
						double e = GhostEntity.this.getTarget().getX() - GhostEntity.this.getX();
						double f = GhostEntity.this.getTarget().getZ() - GhostEntity.this.getZ();
						GhostEntity.this.yaw = -((float)MathHelper.atan2(e, f)) * 57.295776F;
						GhostEntity.this.bodyYaw = GhostEntity.this.yaw;
					}
				}

			}
		}
	}

	class FlyAroundGoal extends Goal {

		private FlyAroundGoal() {
			this.setControls(EnumSet.of(Goal.Control.MOVE));
		}

		public boolean canStart() {
			return !GhostEntity.this.getMoveControl().isMoving() && GhostEntity.this.random.nextInt(2) == 0;
		}

		@Override
		public boolean shouldContinue() {
			return false;
		}

		@Override
		public void tick() {
			if(getHomePosition() == null)
				homePosition = getBlockPos();

			for(int i = 0; i < 3; ++i)
			{
				BlockPos randomTarget = homePosition.add(
						WANDER_RANGE_HORIZONTAL.choose(random),
						WANDER_RANGE_VERTICAL.choose(random),
						WANDER_RANGE_HORIZONTAL.choose(random)
				);

				if (GhostEntity.this.world.isAir(randomTarget))
				{
					GhostEntity.this.moveControl.moveTo(
							(double)randomTarget.getX() + 0.5D,
							(double)randomTarget.getY() + 0.5D,
							(double)randomTarget.getZ() + 0.5D,
							0.25D
					);

					if (GhostEntity.this.getTarget() == null)
					{
						GhostEntity.this.getLookControl().lookAt(
								(double)randomTarget.getX() + 0.5D,
								(double)randomTarget.getY() + 0.5D,
								(double)randomTarget.getZ() + 0.5D,
								180.0F, 20.0F);
					}
					break;
				}
			}

		}
	}

	class ChargeTargetGoal extends Goal {
		private ChargeTargetGoal() {
			this.setControls(EnumSet.of(Goal.Control.MOVE));
		}

		public boolean canStart()
		{
			if (GhostEntity.this.getTarget() != null && !GhostEntity.this.getMoveControl().isMoving() && GhostEntity.this.random.nextInt(2) == 0)
			{
				return GhostEntity.this.squaredDistanceTo(GhostEntity.this.getTarget()) > CHARGE_MIN_DISTANCE;
			}
			return false;
		}

		@Override
		public boolean shouldContinue()
		{
			return GhostEntity.this.getMoveControl().isMoving() && GhostEntity.this.isCharging() && GhostEntity.this.getTarget() != null && GhostEntity.this.getTarget().isAlive();
		}

		@Override
		public void start()
		{
			LivingEntity target = GhostEntity.this.getTarget();
			Vec3d targetPosition = target.getCameraPosVec(1.0F);
			GhostEntity.this.moveControl.moveTo(targetPosition.x, targetPosition.y, targetPosition.z, 1.25D);
			GhostEntity.this.setCharging(true);
			GhostEntity.this.playSound(BMEffects.GHOST_CHARGE, 1.0F, 1.0F);
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
			if(EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(target))
				setTarget(null);

			if (GhostEntity.this.getBoundingBox().expand(2F).intersects(target.getBoundingBox()))
			{
				GhostEntity.this.tryAttack(target);
				GhostEntity.this.setCharging(false);
			}
			else
			{
				double distance = GhostEntity.this.squaredDistanceTo(target);
				if (distance < CHARGE_MAX_DISTANCE)
				{
					Vec3d vec3d = target.getCameraPosVec(1.0F);
					GhostEntity.this.moveControl.moveTo(vec3d.x, vec3d.y, vec3d.z, 1.0D);
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
				PoltergeistHandler.doPoltergeist(world, getBlockPos(), POLTERGEIST_RANGE);
			}

			super.tick();
		}

		@Override
		public boolean canStart()
		{
			return true;
		}

		@Override
		public boolean shouldContinue()
		{
			return false;
		}
	}
}
