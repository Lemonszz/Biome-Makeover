package party.lemons.biomemakeover.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.init.BMEntities;

import java.util.EnumSet;
import java.util.function.Predicate;

public class ScuttlerEntity extends AnimalEntity
{
	private static TrackedData<Boolean> RATTLING = DataTracker.registerData(ScuttlerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	private AttributeContainer attributeContainer;
	public float rattleTime = 0;

	public ScuttlerEntity(World world)
	{
		super(BMEntities.SCUTTLER, world);
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(1, new RattleGoal<>(this, 20.0F, PlayerEntity.class));
		this.goalSelector.add(1, new EscapeDangerGoal(this, 1.25D));
		this.goalSelector.add(3, new AnimalMateGoal(this, 1.0D));
		this.goalSelector.add(4, new FleeEntityGoal<>(this, PlayerEntity.class, 16.0F, 1.6D, 1.4D, (livingEntity) ->true));
		this.goalSelector.add(5, new FollowParentGoal(this, 1.1D));
		this.goalSelector.add(6, new AvoidDaylightGoal(1.0D));
		this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0D));
		this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.add(9, new LookAroundGoal(this));
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(RATTLING, false);
	}

	@Override
	public void tick()
	{
		super.tick();

		if(dataTracker.get(RATTLING))
		{
			double dir = Math.signum(Math.sin(rattleTime));
			rattleTime++;

			if(dir != Math.signum(Math.sin(rattleTime)))
			{
				playSound(BMEffects.SCUTTLER_RATTLE, 0.25F, 0.75F + random.nextFloat());
			}
		}
	}

	@Override
	public void onTrackedDataSet(TrackedData<?> data)
	{
		super.onTrackedDataSet(data);

		if(data == RATTLING)
			rattleTime = 0;
	}

	@Override
	public boolean canSpawn(WorldAccess world, SpawnReason spawnReason) {
		return world.getRandom().nextBoolean() && world.getEntitiesByClass(ScuttlerEntity.class, new Box(new BlockPos(getX(), getY(), getZ())).expand(50), (e)->true).isEmpty() &&  super.canSpawn(world, spawnReason);
	}

	@Override
	public int getLimitPerChunk() {
		return 1;
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
	protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions)
	{
		return 0.2F;
	}

	@Override
	public PassiveEntity createChild(ServerWorld world, PassiveEntity entity)
	{
		return null;
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state)
	{
		if (!state.getMaterial().isLiquid()) {
			playSound(BMEffects.SCUTTLER_STEP, 0.10F, 1.25F + random.nextFloat());
			spawnSprintingParticles();
		}
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return BMEffects.SCUTTLER_DEATH;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source)
	{
		return BMEffects.SCUTTLER_HURT;
	}

	@Override
	protected SoundEvent getAmbientSound()
	{
		return BMEffects.SCUTTLER_RATTLE;
	}

	@Override
	public int getMinAmbientSoundDelay()
	{
		return super.getMinAmbientSoundDelay() * 3;
	}

	private static class RattleGoal<T extends LivingEntity> extends Goal
	{
		private ScuttlerEntity scuttler;
		private float distance;
		private Class<T> targetClass;
		T targetEntity;
		private final TargetPredicate withinRangePredicate;

		public RattleGoal(ScuttlerEntity scuttlerEntity, float distance, Class<T> target)
		{
			this.setControls(EnumSet.of(Goal.Control.LOOK, Goal.Control.MOVE));
			this.scuttler = scuttlerEntity;
			this.distance = distance;
			this.targetClass = target;

			Predicate<LivingEntity> s = (l)->true;
			this.withinRangePredicate = (new TargetPredicate()).setBaseMaxDistance(distance).setPredicate(s.and(EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR));
		}

		@Override
		public boolean canStart()
		{
			if(scuttler.isTouchingWater())
				return false;

			this.targetEntity = scuttler.world.
					getClosestEntityIncludingUngeneratedChunks(
							targetClass,
							this.withinRangePredicate,
							this.scuttler, this.scuttler.getX(), this.scuttler.getY(), this.scuttler.getZ(),
							this.scuttler.getBoundingBox().expand(this.distance, 3.0D, this.distance));
			if (this.targetEntity == null || !scuttler.canSee(targetEntity) || !targetEntity.canSee(scuttler))
			{
				return false;
			}
			else
			{
				return scuttler.distanceTo(targetEntity) >= distance / 2;
			}
		}

		@Override
		public boolean shouldContinue()
		{
			double d = scuttler.distanceTo(targetEntity);
			return d > distance / 2 && d < distance && scuttler.canSee(targetEntity) && targetEntity.canSee(scuttler);
		}

		@Override
		public void stop()
		{
			scuttler.getDataTracker().set(ScuttlerEntity.RATTLING, false);
			super.stop();
		}

		@Override
		public void start()
		{
			scuttler.getDataTracker().set(ScuttlerEntity.RATTLING, true);
			scuttler.getNavigation().stop();
			super.start();
		}

		@Override
		public void tick()
		{
			scuttler.lookControl.lookAt(targetEntity, 30, 30);
		}
	}

	class AvoidDaylightGoal extends EscapeSunlightGoal {
		private int timer = 100;

		AvoidDaylightGoal(double speed)
		{
			super(ScuttlerEntity.this, speed);
		}

		@Override
		public boolean canStart()
		{
			if (this.mob.getTarget() == null)
			{
				if (this.timer > 0)
				{
					--this.timer;
					return false;
				}
				else
				{
					this.timer = 100;
					BlockPos pos = this.mob.getBlockPos();
					return ScuttlerEntity.this.world.isDay() && ScuttlerEntity.this.world.isSkyVisible(pos) && !((ServerWorld)ScuttlerEntity.this.world).isNearOccupiedPointOfInterest(pos) && this.targetShadedPos();
				}
			}
			else
			{
				return false;
			}
		}
	}

}
