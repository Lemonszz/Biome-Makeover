package party.lemons.biomemakeover.entity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
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
import net.minecraft.entity.mob.VexEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import party.lemons.biomemakeover.entity.GhostEntity.ChargeTargetGoal;
import party.lemons.biomemakeover.entity.GhostEntity.GhostMoveControl;
import party.lemons.biomemakeover.entity.GhostEntity.LookAtTargetGoal;
import party.lemons.biomemakeover.init.BMEntities;

import java.util.EnumSet;
import java.util.UUID;

public class GhostEntity extends HostileEntity implements Angerable
{
	protected static final TrackedData<Byte> GHOST_FLAGS = DataTracker.registerData(GhostEntity.class, TrackedDataHandlerRegistry.BYTE);

	private static final IntRange ANGER_TIME_RANGE = Durations.betweenSeconds(20, 39);
	private int angerTime;
	private UUID targetUuid;
	private static final IntRange REENFORCE_TIME_RANGE = Durations.betweenSeconds(4, 6);
	private int refenforceTime;
	private static final IntRange ANGER_SOUND_TIME_RANGE = Durations.betweenSeconds(0, 1);
	private int angrySoundDelay;
	private BlockPos bounds;

	public AttributeContainer attributeContainer;

	public GhostEntity(World world)
	{
		super(BMEntities.GHOST, world);

		this.moveControl = new GhostMoveControl(this);
	}

	@Override
	protected void initGoals() {
		super.initGoals();

		this.goalSelector.add(0, new SwimGoal(this));
		this.targetSelector.add(1, (new RevengeGoal(this)).setGroupRevenge());
		this.goalSelector.add(4, new ChargeTargetGoal());
		this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0D));
		this.goalSelector.add(8, new LookAtTargetGoal());
		this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 3.0F, 1.0F));
		this.goalSelector.add(10, new LookAtEntityGoal(this, MobEntity.class, 8.0F));
		this.targetSelector.add(1, (new RevengeGoal(this, new Class[]{RaiderEntity.class})).setGroupRevenge());
		this.targetSelector.add(2, new FollowTargetGoal(this, PlayerEntity.class, 10, true, false, (e)->this.shouldAngerAt((LivingEntity) e)));
		this.targetSelector.add(3, new UniversalAngerGoal(this, true));
	}

	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(GHOST_FLAGS, (byte)0);
	}

	@Override
	public AttributeContainer getAttributes()
	{
		if(attributeContainer == null)
			attributeContainer =  new AttributeContainer(
					HostileEntity.createHostileAttributes().build());
		return attributeContainer;
	}

	public void move(MovementType type, Vec3d movement) {
		super.move(type, movement);
		this.checkBlockCollision();
	}

	public void tick() {
		this.noClip = true;
		super.tick();
		this.noClip = false;
		this.setNoGravity(true);
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
		if (this.refenforceTime > 0) {
			--this.refenforceTime;
		} else {
			if (this.getVisibilityCache().canSee(this.getTarget())) {
				this.alertNearby();
			}

			this.refenforceTime = REENFORCE_TIME_RANGE.choose(this.random);
		}
	}

	private void alertNearby() {
		double d = this.getAttributeValue(EntityAttributes.GENERIC_FOLLOW_RANGE);
		Box box = Box.method_29968(this.getPos()).expand(d, 10.0D, d);
		this.world.getEntitiesIncludingUngeneratedChunks(GhostEntity.class, box).stream()
				.filter((e) ->e != this)
				.filter((e) ->e.getTarget() == null)
				.filter((e) ->!e.isTeammate(this.getTarget()))
				.forEach((e) ->e.setTarget(this.getTarget()));
	}

	public void setTarget(LivingEntity target) {
		if (this.getTarget() == null && target != null) {
			this.angrySoundDelay = ANGER_SOUND_TIME_RANGE.choose(this.random);
			this.refenforceTime = REENFORCE_TIME_RANGE.choose(this.random);
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
		this.playSound(SoundEvents.ENTITY_ZOMBIFIED_PIGLIN_ANGRY, this.getSoundVolume() * 2.0F, this.getSoundPitch() * 1.8F);
	}

	public void chooseRandomAngerTime() {
		this.setAngerTime(ANGER_TIME_RANGE.choose(this.random));
	}

	@Override
	public void setAngryAt(UUID uuid)
	{
		this.targetUuid = uuid;
	}

	public void writeCustomDataToTag(CompoundTag tag) {
		super.writeCustomDataToTag(tag);
		this.angerToTag(tag);

		if (this.bounds != null) {
			tag.putInt("BoundX", this.bounds.getX());
			tag.putInt("BoundY", this.bounds.getY());
			tag.putInt("BoundZ", this.bounds.getZ());
		}
	}

	public void readCustomDataFromTag(CompoundTag tag) {
		super.readCustomDataFromTag(tag);
		this.angerFromTag((ServerWorld)this.world, tag);

		if (tag.contains("BoundX")) {
			this.bounds = new BlockPos(tag.getInt("BoundX"), tag.getInt("BoundY"), tag.getInt("BoundZ"));
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
		return this.hasAngerTime() ? SoundEvents.ENTITY_ZOMBIFIED_PIGLIN_ANGRY : SoundEvents.ENTITY_ZOMBIFIED_PIGLIN_AMBIENT;
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
		return SoundEvents.ENTITY_ZOMBIFIED_PIGLIN_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_ZOMBIFIED_PIGLIN_DEATH;
	}

	private boolean areFlagsSet(int mask) {
		int i = (Byte)this.dataTracker.get(GHOST_FLAGS);
		return (i & mask) != 0;
	}

	private void setGhostFlag(int mask, boolean value) {
		int i = (Byte)this.dataTracker.get(GHOST_FLAGS);
		if (value) {
			i = i | mask;
		} else {
			i = i & ~mask;
		}

		this.dataTracker.set(GHOST_FLAGS, (byte)(i & 255));
	}

	public boolean isCharging() {
		return this.areFlagsSet(1);
	}

	public void setCharging(boolean charging) {
		this.setGhostFlag(1, charging);
	}

	public BlockPos getBounds() {
		return this.bounds;
	}

	public void setBounds(BlockPos pos) {
		this.bounds = pos;
	}

	class GhostMoveControl extends MoveControl {
		public GhostMoveControl(GhostEntity owner) {
			super(owner);
		}

		public void tick() {
			if (this.state == MoveControl.State.MOVE_TO) {
				Vec3d targetDistance = new Vec3d(this.targetX - GhostEntity.this.getX(), this.targetY - GhostEntity.this.getY(), this.targetZ - GhostEntity.this.getZ());
				double length = targetDistance.length();
				if (length < GhostEntity.this.getBoundingBox().getAverageSideLength()) {
					this.state = MoveControl.State.WAIT;
					GhostEntity.this.setVelocity(GhostEntity.this.getVelocity().multiply(0.5D));
				} else {
					GhostEntity.this.setVelocity(GhostEntity.this.getVelocity().add(targetDistance.multiply(this.speed * 0.05D / length)));
					if (GhostEntity.this.getTarget() == null) {
						Vec3d vec3d2 = GhostEntity.this.getVelocity();
						GhostEntity.this.yaw = -((float)MathHelper.atan2(vec3d2.x, vec3d2.z)) * 57.295776F;
						GhostEntity.this.bodyYaw = GhostEntity.this.yaw;
					} else {
						double e = GhostEntity.this.getTarget().getX() - GhostEntity.this.getX();
						double f = GhostEntity.this.getTarget().getZ() - GhostEntity.this.getZ();
						GhostEntity.this.yaw = -((float)MathHelper.atan2(e, f)) * 57.295776F;
						GhostEntity.this.bodyYaw = GhostEntity.this.yaw;
					}
				}

			}
		}
	}

	class LookAtTargetGoal extends Goal {
		public LookAtTargetGoal() {
			this.setControls(EnumSet.of(Goal.Control.MOVE));
		}

		public boolean canStart() {
			return !GhostEntity.this.getMoveControl().isMoving() && GhostEntity.this.random.nextInt(2) == 0;
		}

		public boolean shouldContinue() {
			return false;
		}

		public void tick() {
			BlockPos blockPos = GhostEntity.this.getBounds();
			if (blockPos == null) {
				blockPos = GhostEntity.this.getBlockPos();
			}

			for(int i = 0; i < 3; ++i) {
				BlockPos blockPos2 = blockPos.add(GhostEntity.this.random.nextInt(15) - 7, GhostEntity.this.random.nextInt(6) - 5, GhostEntity.this.random.nextInt(15) - 7);
				if (GhostEntity.this.world.isAir(blockPos2)) {
					GhostEntity.this.moveControl.moveTo((double)blockPos2.getX() + 0.5D, (double)blockPos2.getY() + 0.5D, (double)blockPos2.getZ() + 0.5D, 0.25D);
					if (GhostEntity.this.getTarget() == null) {
						GhostEntity.this.getLookControl().lookAt((double)blockPos2.getX() + 0.5D, (double)blockPos2.getY() + 0.5D, (double)blockPos2.getZ() + 0.5D, 180.0F, 20.0F);
					}
					break;
				}
			}

		}
	}

	class ChargeTargetGoal extends Goal {
		public ChargeTargetGoal() {
			this.setControls(EnumSet.of(Goal.Control.MOVE));
		}

		public boolean canStart() {
			if (GhostEntity.this.getTarget() != null && !GhostEntity.this.getMoveControl().isMoving() && GhostEntity.this.random.nextInt(2) == 0) {
				return GhostEntity.this.squaredDistanceTo(GhostEntity.this.getTarget()) > 2.0D;
			} else {
				return false;
			}
		}

		public boolean shouldContinue() {
			return GhostEntity.this.getMoveControl().isMoving() && GhostEntity.this.isCharging() && GhostEntity.this.getTarget() != null && GhostEntity.this.getTarget().isAlive();
		}

		public void start() {
			LivingEntity livingEntity = GhostEntity.this.getTarget();
			Vec3d vec3d = livingEntity.getCameraPosVec(1.0F);
			GhostEntity.this.moveControl.moveTo(vec3d.x, vec3d.y, vec3d.z, 1.0D);
			GhostEntity.this.setCharging(true);
			GhostEntity.this.playSound(SoundEvents.ENTITY_VEX_CHARGE, 1.0F, 1.0F);
		}

		public void stop() {
			GhostEntity.this.setCharging(false);
		}

		public void tick() {
			LivingEntity livingEntity = GhostEntity.this.getTarget();
			if (GhostEntity.this.getBoundingBox().intersects(livingEntity.getBoundingBox())) {
				GhostEntity.this.tryAttack(livingEntity);
				GhostEntity.this.setCharging(false);
			} else {
				double d = GhostEntity.this.squaredDistanceTo(livingEntity);
				if (d < 9.0D) {
					Vec3d vec3d = livingEntity.getCameraPosVec(1.0F);
					GhostEntity.this.moveControl.moveTo(vec3d.x, vec3d.y, vec3d.z, 1.0D);
				}
			}

		}
	}
}
