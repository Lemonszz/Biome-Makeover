package party.lemons.biomemakeover.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.util.MathUtils;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.EnumSet;
import java.util.List;

public class ToadEntity extends AnimalEntity
{
	private static final TrackedData<Integer> TONGUE_ENTITY = DataTracker.registerData(ToadEntity.class, TrackedDataHandlerRegistry.INTEGER);

	private int jumpTicks;
	private int jumpDuration;
	private boolean lastOnGround;
	private int ticksUntilJump;
	public float tongueDistance;
	public float targetTongueDistance;
	public float mouthDistance = 0;
	public int eatCooldown = 0;

	private AttributeContainer attributeContainer;

	public ToadEntity(World world)
	{
		super(BMEntities.TOAD, world);
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

	public float getPathfindingFavor(BlockPos pos, WorldView world)
	{
		 if(world.getBlockState(pos).isIn(BMBlocks.LILY_PADS))
		 	return 100;

		return super.getPathfindingFavor(pos, world);
	}

	private TargetPredicate predicate = new TargetPredicate().setPredicate((e)->e.distanceTo(e) < 10);

	@Override
	public void tick()
	{
		super.tick();

		if(hasTongueEntity())
		{
			Entity e = world.getEntityById(getTongueEntityID());
			if(e != null && !e.hasVehicle())
			{
				getLookControl().lookAt(e.getX(), (e.getBoundingBox().minY + 0.25F), e.getZ(), 100, 100);
				bodyYaw = getTargetYaw();
				headYaw = getTargetYaw();
				pitch = getTargetPitch();

				float speed = 10;
				targetTongueDistance = (this.distanceTo(e) * 16) - ((float)(e.getBoundingBox().maxX - e.getBoundingBox().minX) * 16F);
				if(tongueDistance > targetTongueDistance)
					speed *= 2;

				tongueDistance = MathUtils.approachValue(tongueDistance, targetTongueDistance, speed);
			}
			else//TODO: clean this
			{
				targetTongueDistance = 0;
				tongueDistance = MathUtils.approachValue(tongueDistance, 0, 20);
			}
		}
		else
		{
			targetTongueDistance = 0;
			tongueDistance = MathUtils.approachValue(tongueDistance, 0, 20);
		}

	}

	public boolean isTongueReady()
	{
		float yaw = Math.abs(((bodyYaw + 1) % 360) - getTargetYaw());
		boolean dis = Math.abs(tongueDistance - targetTongueDistance) < 5;
		return dis && (yaw < 4F || yaw >= 360);
	}

	public float getTargetYaw() {
		double xx = lookControl.getLookX() - getX();
		double zz = lookControl.getLookZ() - getZ();
		return (float)(MathHelper.atan2(zz, xx) * 57.2957763671875D) - 90.0F;
	}

	public float getTargetPitch() {
		double xx = lookControl.getLookX() - getX();
		double yy = lookControl.getLookY() - getEyeY();
		double zz = lookControl.getLookZ() - getZ();
		double sqrt = MathHelper.sqrt(xx * xx + zz * zz);
		return (float)(-(MathHelper.atan2(yy, sqrt) * 57.2957763671875D));
	}

	public boolean canUseTongue()
	{
		return !this.hasVehicle();
	}

	public void mobTick() {

		eatCooldown--;
		if(eatCooldown <= 0 && !hasTongueEntity())
		{
			List<ToadTargetEntity> targets = world.getEntitiesByClass(ToadTargetEntity.class, getBoundingBox().expand(3, 3, 3), (e)->canSee(e) && !e.isBeingEaten());
			ToadTargetEntity closest = world.getClosestEntity(targets, predicate, this, getX(), getY(), getZ());

			if(!canUseTongue() || closest == null || closest.hasVehicle() || targets.isEmpty())
				clearTongueEntity();
			else
			{
				eatCooldown = 350;
				setTongueEntity(closest);
			}
		}
		else
		{
			Entity e = world.getEntityById(getTongueEntityID());
			if(!canUseTongue() || e == null || !e.isAlive())
				clearTongueEntity();
		}

		if (this.ticksUntilJump > 0) {
			--this.ticksUntilJump;
		}

		if(ticksUntilJump <= 0 && moveControl.isMoving())
		{
			ticksUntilJump = RandomUtil.randomRange(20, 100);
			jump();
			this.playSound(this.getJumpSound(), this.getSoundVolume(), ((this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F) * 0.8F);
		}

		this.lastOnGround = this.onGround;
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
