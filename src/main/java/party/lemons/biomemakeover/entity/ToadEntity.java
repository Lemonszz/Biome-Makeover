package party.lemons.biomemakeover.entity;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.control.JumpControl;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.util.MathUtils;

import java.util.EnumSet;
import java.util.List;

public class ToadEntity extends AnimalEntity
{
	private static final TrackedData<Integer> TONGUE_ENTITY = DataTracker.registerData(ToadEntity.class, TrackedDataHandlerRegistry.INTEGER);

	private int jumpTicks;
	private int jumpDuration;
	private boolean lastOnGround;
	private int ticksUntilJump;
	public float toungeDistance;
	public float targetToungeDistance;

	private AttributeContainer attributeContainer;

	public ToadEntity(World world)
	{
		super(BMEntities.TOAD, world);
		this.jumpControl = new ToadJumpControl(this);
		this.moveControl = new ToadMoveControl(this);
	}

	@Override
	protected void initDataTracker()
	{
		super.initDataTracker();
		this.dataTracker.startTracking(TONGUE_ENTITY, -1);
	}


	@Override
	protected void initGoals()
	{
		this.goalSelector.add(0, new LookAtTongueTarget(this));
		this.goalSelector.add(1, new SwimGoal(this));
		//this.goalSelector.add(1, new RabbitEntity.EscapeDangerGoal(this, 2.2D));
		this.goalSelector.add(2, new AnimalMateGoal(this, 0.8D));
		//this.goalSelector.add(3, new TemptGoal(this, 1.0D, Ingredient.ofItems(Items.CARROT, Items.GOLDEN_CARROT, Blocks.DANDELION), false));
		this.goalSelector.add(6, new WanderAroundFarGoal(this, 0.6D));
		this.goalSelector.add(11, new LookAtEntityGoal(this, PlayerEntity.class, 10.0F));
		this.goalSelector.add(12, new LookAroundGoal(this));
	}

	public void setTongueEntity(ToadTargetEntity e)
	{
		dataTracker.set(TONGUE_ENTITY, e.getEntityId());
		e.setEatenBy(this);
	}

	public boolean hasTongueEntity()
	{
		return dataTracker.get(TONGUE_ENTITY) != -1;
	}

	public int getTongueEntityID()
	{
		return dataTracker.get(TONGUE_ENTITY);
	}

	public void clearTongueEntity()
	{
		if(world.getEntityById(getTongueEntityID()) != null)
			((ToadTargetEntity)world.getEntityById(getTongueEntityID())).setEatenBy(null);

		dataTracker.set(TONGUE_ENTITY, -1);
	}


	private TargetPredicate predicate = new TargetPredicate().setPredicate((e)->e.distanceTo(e) < 10);

	@Override
	public void tick()
	{
		super.tick();


		if(hasTongueEntity())
		{
			Entity e = world.getEntityById(getTongueEntityID());
			if(e != null)
			{
				getLookControl().lookAt(e.getX(), (e.getBoundingBox().minY + e.getBoundingBox().maxY) / 2.0D - 0.5F, e.getZ(), 100, 100);
				headYaw = getTargetYaw();
				pitch = getTargetPitch();

				float speed = 10;
				targetToungeDistance = (this.distanceTo(e) * 16) - ((float)(e.getBoundingBox().maxX - e.getBoundingBox().minX) * 16F);
				if(toungeDistance > targetToungeDistance)
					speed *= 2;

				toungeDistance = MathUtils.approachValue(toungeDistance, targetToungeDistance, speed);
			}
			else//TODO: clean this
			{
				targetToungeDistance = 0;
				toungeDistance = MathUtils.approachValue(toungeDistance, 0, 20);
			}
		}
		else
		{
			targetToungeDistance = 0;
			toungeDistance = MathUtils.approachValue(toungeDistance, 0, 20);
		}

	}

	public boolean isToungeReady()
	{
		float yaw = Math.abs(((headYaw + 1) % 360) - getTargetYaw());
		boolean dis = Math.abs(toungeDistance - targetToungeDistance) < 5;
		return dis && Math.abs(pitch - getTargetPitch()) < 4F && (yaw < 4F || yaw >= 360);
	}

	protected float getTargetPitch() {
		double d = lookControl.getLookX() - getX();
		double e = lookControl.getLookY() - getEyeY();
		double f = lookControl.getLookZ() - getZ();
		double g = MathHelper.sqrt(d * d + f * f);
		return (float)(-(MathHelper.atan2(e, g) * 57.2957763671875D));
	}

	protected float getTargetYaw() {
		double d = lookControl.getLookX() - getX();
		double e = lookControl.getLookZ() - getZ();
		return (float)(MathHelper.atan2(e, d) * 57.2957763671875D) - 90.0F;
	}

	public void mobTick() {

		if(!hasTongueEntity())
		{
			List<ToadTargetEntity> targets = world.getEntitiesByClass(ToadTargetEntity.class, getBoundingBox().expand(3, 3, 3), (e)->canSee(e) && !e.isBeingEaten());
			ToadTargetEntity closest = world.getClosestEntity(targets, predicate, this, getX(), getY(), getZ());

			if(closest == null || targets.isEmpty())
				clearTongueEntity();
			else setTongueEntity(closest);
		}
		else
		{
			Entity e = world.getEntityById(getTongueEntityID());
			if(e == null || !e.isAlive())
				clearTongueEntity();
		}

		if (this.ticksUntilJump > 0) {
			--this.ticksUntilJump;
		}

		if (this.onGround) {
			if (!this.lastOnGround)
			{
				this.setJumping(false);
				this.scheduleJump();
			}

			ToadEntity.ToadJumpControl rabbitJumpControl = (ToadEntity.ToadJumpControl)this.jumpControl;
			if (!rabbitJumpControl.isActive()) {
				if (this.moveControl.isMoving() && this.ticksUntilJump == 0) {
					Path path = this.navigation.getCurrentPath();
					Vec3d vec3d = new Vec3d(this.moveControl.getTargetX(), this.moveControl.getTargetY(), this.moveControl.getTargetZ());
					if (path != null && !path.isFinished()) {
						vec3d = path.getNodePosition(this);
					}

					this.lookTowards(vec3d.x, vec3d.z);
					this.startJump();
				}
			} else if (!rabbitJumpControl.method_27313()) { //TODO: What are these
				this.method_6611();
			}
		}

		this.lastOnGround = this.onGround;
	}

	private void lookTowards(double x, double z) {
		this.yaw = (float)(MathHelper.atan2(z - this.getZ(), x - this.getX()) * 57.2957763671875D) - 90.0F;
	}

	public boolean shouldSpawnSprintingParticles() {
		return false;
	}

	protected float getJumpVelocity() {
		/*if (!this.horizontalCollision && (!this.moveControl.isMoving() || this.moveControl.getTargetY() <= this.getY() + 0.5D)) {
			Path path = this.navigation.getCurrentPath();
			if (path != null && !path.isFinished()) {
				Vec3d vec3d = path.getNodePosition(this);
				if (vec3d.y > this.getY() + 0.5D) {
					return 0.5F;
				}
			}

			return this.moveControl.getSpeed() <= 0.6D ? 0.2F : 0.3F;
		} else {
			return 0.5F;
		}*/
		return super.getJumpVelocity();
	}

	protected void jump() {
		super.jump();
		double d = this.moveControl.getSpeed();
		if (d > 0.0D) {
			double e = squaredHorizontalLength(this.getVelocity());
			if (e < 0.01D) {
				this.updateVelocity(0.1F, new Vec3d(0.0D, 0.0D, 1.0D));
			}
		}

		if (!this.world.isClient) {
			this.world.sendEntityStatus(this, (byte)1);
		}

	}

	@Environment(EnvType.CLIENT)
	public float getJumpProgress(float delta) {
		return this.jumpDuration == 0 ? 0.0F : ((float)this.jumpTicks + delta) / (float)this.jumpDuration;
	}

	public void setSpeed(double speed) {
		this.getNavigation().setSpeed(speed);
		this.moveControl.moveTo(this.moveControl.getTargetX(), this.moveControl.getTargetY(), this.moveControl.getTargetZ(), speed);
	}

	public void setJumping(boolean jumping) {
		super.setJumping(jumping);
		if (jumping) {
			this.playSound(this.getJumpSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 0.8F);
		}
	}

	public void startJump() {
		this.setJumping(true);
		this.jumpDuration = 10;
		this.jumpTicks = 0;
	}

	private void method_6611() {
		((ToadJumpControl)this.jumpControl).method_27311(true);
	}

	private void method_6621() {
		((ToadJumpControl)this.jumpControl).method_27311(false);
	}

	private void doScheduleJump() {
		if (this.moveControl.getSpeed() < 2.2D) {
			this.ticksUntilJump = 10;
		} else {
			this.ticksUntilJump = 1;
		}

	}

	private void scheduleJump() {
		this.doScheduleJump();
		this.method_6621();
	}

	public void tickMovement() {
		super.tickMovement();
		if (this.jumpTicks != this.jumpDuration) {
			++this.jumpTicks;
		} else if (this.jumpDuration != 0) {
			this.jumpTicks = 0;
			this.jumpDuration = 0;
			this.setJumping(false);
		}

	}

	protected SoundEvent getJumpSound() {
		return SoundEvents.ENTITY_RABBIT_JUMP;
	}


	@Override
	public AttributeContainer getAttributes()
	{
		if(attributeContainer == null)
			attributeContainer =  new AttributeContainer(
					MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D).build());
		return attributeContainer;
	}

	@Override
	public PassiveEntity createChild(ServerWorld world, PassiveEntity entity)
	{
		return null;
	}

	static class ToadMoveControl extends MoveControl
	{
		private final ToadEntity toad;
		private double rabbitSpeed;

		public ToadMoveControl(ToadEntity owner) {
			super(owner);
			this.toad = owner;
		}

		public void tick() {
			if (this.toad.onGround && !this.toad.jumping && !((ToadEntity.ToadJumpControl)this.toad.jumpControl).isActive()) {
				this.toad.setSpeed(0.0D);
			} else if (this.isMoving()) {
				this.toad.setSpeed(this.rabbitSpeed);
			}

			super.tick();
		}

		public void moveTo(double x, double y, double z, double speed) {
			if (this.toad.isTouchingWater()) {
				speed = 1.5D;
			}

			super.moveTo(x, y, z, speed);
			if (speed > 0.0D) {
				this.rabbitSpeed = speed;
			}

		}
	}

	public class ToadJumpControl extends JumpControl
	{
		private final ToadEntity toad;
		private boolean field_24091;

		public ToadJumpControl(ToadEntity toad) {
			super(toad);
			this.toad = toad;
		}

		public boolean isActive() {
			return this.active;
		}

		public boolean method_27313() {
			return this.field_24091;
		}

		public void method_27311(boolean bl) {
			this.field_24091 = bl;
		}

		public void tick() {
			if (this.active) {
				this.toad.startJump();
				this.active = false;
			}
		}
	}

	private class LookAtTongueTarget extends Goal
	{
		private ToadEntity toad;

		public LookAtTongueTarget(ToadEntity entity)
		{
			super();
			this.toad = entity;
			this.setControls(EnumSet.of(Goal.Control.MOVE, Control.LOOK));
		}

		@Override
		public boolean canStart()
		{
			return toad.hasTongueEntity();
		}

		@Override
		public boolean shouldContinue()
		{
			return toad.hasTongueEntity();
		}
	}
}
