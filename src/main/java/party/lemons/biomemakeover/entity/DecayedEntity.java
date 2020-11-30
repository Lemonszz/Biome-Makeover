package party.lemons.biomemakeover.entity;

import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetFinder;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.ai.pathing.SwimNavigation;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.*;
import party.lemons.biomemakeover.init.BMEntities;

public class DecayedEntity extends ZombieEntity
{
	private boolean targetingUnderwater;
	protected final SwimNavigation waterNavigation;
	protected final MobNavigation landNavigation;
	private AttributeContainer attributeContainer;


	public DecayedEntity(World world)
	{
		super(BMEntities.DECAYED, world);
	//	this.stepHeight = 1.0F;
		this.moveControl = new DecayedMoveControl(this);
		this.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
		this.waterNavigation = new SwimNavigation(this, world);
		this.landNavigation = new MobNavigation(this, world);
	}

	protected void initCustomGoals() {
		this.goalSelector.add(1, new SurfaceWanderGoal(this, 1.0D));
		this.goalSelector.add(2, new ZombieAttackGoal(this, 1.0D, false));
		this.goalSelector.add(5, new LeaveWaterGoal(this, 1.0D));
		this.goalSelector.add(6, new TargetAboveWaterGoal(this, 1.0D, this.world.getSeaLevel()));
		this.goalSelector.add(7, new WanderAroundGoal(this, 1.0D));
		this.targetSelector.add(1, (new RevengeGoal(this, DrownedEntity.class)).setGroupRevenge(ZombifiedPiglinEntity.class));
		this.targetSelector.add(2, new FollowTargetGoal(this, PlayerEntity.class, 10, true, false, (e)->true));
		this.targetSelector.add(3, new FollowTargetGoal(this, MerchantEntity.class, false));
		this.targetSelector.add(3, new FollowTargetGoal(this, IronGolemEntity.class, true));
		this.targetSelector.add(5, new FollowTargetGoal(this, TurtleEntity.class, 10, true, false, TurtleEntity.BABY_TURTLE_ON_LAND_FILTER));
	}

	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CompoundTag entityTag)
	{
		entityData = super.initialize(world, difficulty, spawnReason, entityData, entityTag);

		setLeftHanded(random.nextBoolean());
		this.equipStack(EquipmentSlot.OFFHAND, new ItemStack(Items.SHIELD));
		return entityData;
	}

	@Override
	public AttributeContainer getAttributes()
	{
		if(attributeContainer == null)
			attributeContainer = new AttributeContainer(HostileEntity.createHostileAttributes()
					.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35.0D)
					.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23000000417232513D)
					.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0D)
					.add(EntityAttributes.GENERIC_ARMOR, 2.0D)
					.add(EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS).build());

		return attributeContainer;
	}


	protected boolean shouldBreakDoors() {
		return false;
	}

	protected SoundEvent getAmbientSound() {
		return this.isTouchingWater() ? SoundEvents.ENTITY_DROWNED_AMBIENT_WATER : SoundEvents.ENTITY_DROWNED_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource source) {
		return this.isTouchingWater() ? SoundEvents.ENTITY_DROWNED_HURT_WATER : SoundEvents.ENTITY_DROWNED_HURT;
	}

	protected SoundEvent getDeathSound() {
		return this.isTouchingWater() ? SoundEvents.ENTITY_DROWNED_DEATH_WATER : SoundEvents.ENTITY_DROWNED_DEATH;
	}

	protected SoundEvent getStepSound() {
		return SoundEvents.ENTITY_DROWNED_STEP;
	}

	protected SoundEvent getSwimSound() {
		return SoundEvents.ENTITY_DROWNED_SWIM;
	}

	protected ItemStack getSkull() {
		return ItemStack.EMPTY;
	}
	protected boolean canConvertInWater() {
		return false;
	}

	public boolean canSpawn(WorldView world) {
		return world.intersectsEntities(this);
	}

	public boolean canFly() {
		return !this.isSwimming();
	}

	private boolean isTargetingUnderwater() {
		if (this.targetingUnderwater) {
			return true;
		} else {
			LivingEntity livingEntity = this.getTarget();
			return livingEntity != null && livingEntity.isTouchingWater();
		}
	}

	public void travel(Vec3d movementInput) {
		if (this.canMoveVoluntarily() && this.isTouchingWater() && this.isTargetingUnderwater()) {
			this.updateVelocity(0.1F, movementInput);
			this.move(MovementType.SELF, this.getVelocity());
			this.setVelocity(this.getVelocity().multiply(0.9D));
		} else {
			super.travel(movementInput);
		}

	}

	public void updateSwimming() {
		if (!this.world.isClient) {
			if (this.canMoveVoluntarily() && this.submergedInWater && this.isTargetingUnderwater()) {
				this.navigation = this.waterNavigation;
				stepHeight = 1;
				this.setSwimming(true);
			} else {
				stepHeight = 0.5F;
				this.navigation = this.landNavigation;
				this.setSwimming(false);
			}
		}

	}

	public void setTargetingUnderwater(boolean targetingUnderwater) {
		this.targetingUnderwater = targetingUnderwater;
	}

	protected boolean hasFinishedCurrentPath() {
		Path path = this.getNavigation().getCurrentPath();
		if (path != null) {
			BlockPos blockPos = path.getTarget();
			if (blockPos != null) {
				double d = this.squaredDistanceTo((double)blockPos.getX(), (double)blockPos.getY(), (double)blockPos.getZ());
				if (d < 4.0D) {
					return true;
				}
			}
		}

		return false;
	}


	static class DecayedMoveControl extends MoveControl {
		private final DecayedEntity decayed;

		public DecayedMoveControl(DecayedEntity decayed) {
			super(decayed);
			this.decayed = decayed;
		}

		public void tick() {
			LivingEntity livingEntity = this.decayed.getTarget();
			if (this.decayed.isTargetingUnderwater() && this.decayed.isTouchingWater()) {
				if (livingEntity != null && livingEntity.getY() > this.decayed.getY() || this.decayed.targetingUnderwater) {
					this.decayed.setVelocity(this.decayed.getVelocity().add(0.0D, 0.002D, 0.0D));
				}

				if (this.state != MoveControl.State.MOVE_TO || this.decayed.getNavigation().isIdle()) {
					this.decayed.setMovementSpeed(0.0F);
					return;
				}

				double d = this.targetX - this.decayed.getX();
				double e = this.targetY - this.decayed.getY();
				double f = this.targetZ - this.decayed.getZ();
				double g = MathHelper.sqrt(d * d + e * e + f * f);
				e /= g;
				float h = (float)(MathHelper.atan2(f, d) * 57.2957763671875D) - 90.0F;
				this.decayed.yaw = this.changeAngle(this.decayed.yaw, h, 90.0F);
				this.decayed.bodyYaw = this.decayed.yaw;
				float i = (float)(this.speed * this.decayed.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
				float j = MathHelper.lerp(0.125F, this.decayed.getMovementSpeed(), i);
				this.decayed.setMovementSpeed(j);
				this.decayed.setVelocity(this.decayed.getVelocity().add((double)j * d * 0.005D, (double)j * e * 0.1D, (double)j * f * 0.005D));
			} else {
				if (!this.decayed.onGround) {
					this.decayed.setVelocity(this.decayed.getVelocity().add(0.0D, -0.008D, 0.0D));
				}

				super.tick();
			}

		}
	}


	static class SurfaceWanderGoal extends WanderAroundGoal
	{
		public SurfaceWanderGoal(PathAwareEntity mob, double speed)
		{
			super(mob, speed);
		}

		@Override
		public boolean canStop()
		{
			return !mob.isTouchingWater() && super.canStop();
		}

		@Override
		public boolean shouldContinue()
		{
			return !mob.isTouchingWater() && super.shouldContinue();
		}
	}

	static class LeaveWaterGoal extends MoveToTargetPosGoal
	{
		private final DecayedEntity decayed;

		public LeaveWaterGoal(DecayedEntity decayed, double speed) {
			super(decayed, speed, 8, 2);
			this.decayed = decayed;
		}

		public boolean canStart() {
			return super.canStart() && !this.decayed.world.isDay() && this.decayed.isTouchingWater() && this.decayed.getY() >= (double)(this.decayed.world.getSeaLevel() - 3);
		}

		public boolean shouldContinue() {
			return super.shouldContinue();
		}

		protected boolean isTargetPos(WorldView world, BlockPos pos) {
			BlockPos blockPos = pos.up();
			return world.isAir(blockPos) && world.isAir(blockPos.up()) && world.getBlockState(pos).hasSolidTopSurface(world, pos, this.decayed);
		}

		public void start() {
			this.decayed.setTargetingUnderwater(false);
			this.decayed.navigation = this.decayed.landNavigation;
			super.start();
		}

		public void stop() {
			super.stop();
		}
	}

	static class TargetAboveWaterGoal extends Goal {
		private final DecayedEntity decayed;
		private final double speed;
		private final int minY;
		private boolean foundTarget;

		public TargetAboveWaterGoal(DecayedEntity decayed, double speed, int minY) {
			this.decayed = decayed;
			this.speed = speed;
			this.minY = minY;
		}

		public boolean canStart() {
			return !this.decayed.world.isDay() && this.decayed.isTouchingWater() && this.decayed.getY() < (double)(this.minY - 2);
		}

		public boolean shouldContinue() {
			return this.canStart() && !this.foundTarget;
		}

		public void tick() {
			if (this.decayed.getY() < (double)(this.minY - 1) && (this.decayed.getNavigation().isIdle())) {
				Vec3d vec3d = TargetFinder.findTargetTowards(this.decayed, 4, 8, new Vec3d(this.decayed.getX(), (double)(this.minY - 1), this.decayed.getZ()));
				if (vec3d == null) {
					this.foundTarget = true;
					return;
				}

				this.decayed.getNavigation().startMovingTo(vec3d.x, vec3d.y, vec3d.z, this.speed);
			}

		}

		public void start() {
			this.decayed.setTargetingUnderwater(true);
			this.foundTarget = false;
		}

		public void stop() {
			this.decayed.setTargetingUnderwater(false);
		}
	}
}
