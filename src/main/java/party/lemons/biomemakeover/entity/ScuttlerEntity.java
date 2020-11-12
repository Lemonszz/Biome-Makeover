package party.lemons.biomemakeover.entity;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.*;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.init.BMItems;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class ScuttlerEntity extends AnimalEntity
{
	private static final TrackedData<Boolean> RATTLING = DataTracker.registerData(ScuttlerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	public static final TrackedData<Boolean> EATING = DataTracker.registerData(ScuttlerEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	public Ingredient TEMPT_ITEM = Ingredient.ofItems(BMItems.PINK_PETALS);
	private AttributeContainer attributeContainer;
	public float rattleTime = 0;
	private int eatCooldown = 100;
	public int eatTime = 0;
	private boolean passive = false;
	private TemptGoal temptGoal;

	public ScuttlerEntity(World world)
	{
		super(BMEntities.SCUTTLER, world);
	}

	@Override
	protected void initGoals() {
		TEMPT_ITEM = Ingredient.ofItems(BMItems.PINK_PETALS);

		this.temptGoal = new TemptGoal(this, 0.7D, TEMPT_ITEM, true);

		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(1, new RattleGoal<>(this, 20.0F, PlayerEntity.class));
		this.goalSelector.add(2, new EscapeDangerGoal(this, 1.25D));
		this.goalSelector.add(3, new AnimalMateGoal(this, 1.0D));
		this.goalSelector.add(4, temptGoal);
		this.goalSelector.add(5, new FleeEntityGoal<>(this, PlayerEntity.class, 16.0F, 1.6D, 1.4D, (livingEntity) ->!passive));
		this.goalSelector.add(6, new FollowParentGoal(this, 1.1D));
		this.goalSelector.add(7, new EatFlowerGoal());
		this.goalSelector.add(8, new AvoidDaylightGoal(1.0D));
		this.goalSelector.add(9, new WanderAroundFarGoal(this, 1.0D));
		this.goalSelector.add(10, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.add(11, new LookAroundGoal(this));
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(RATTLING, false);
		this.dataTracker.startTracking(EATING, false);
	}

	@Override
	public void tick()
	{
		super.tick();
		eatCooldown--;
		if(dataTracker.get(EATING))
			eatTime--;
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
	public ActionResult interactMob(PlayerEntity player, Hand hand) {
		ItemStack itemStack = player.getStackInHand(hand);
		Item item = itemStack.getItem();
		if (this.world.isClient)
		{
			if (passive)
			{
				return ActionResult.SUCCESS;
			}
			else
			{
				return !this.isBreedingItem(itemStack) || this.getHealth() >= this.getMaxHealth() && passive ? ActionResult.PASS : ActionResult.SUCCESS;
			}
		}
		else
		{
			if (passive)
			{
				if (item.isFood() && this.isBreedingItem(itemStack) && this.getHealth() < this.getMaxHealth()) {
					this.eat(player, itemStack);
					this.heal((float)item.getFoodComponent().getHunger());
					return ActionResult.CONSUME;
				}
			}
			else if (this.isBreedingItem(itemStack))
			{
				this.eat(player, itemStack);
				if (this.random.nextInt(3) == 0)
				{
					passive = true;
					this.world.sendEntityStatus(this, (byte)7);
				}
				else
				{
					this.world.sendEntityStatus(this, (byte)6);
				}
				this.setPersistent();
				return ActionResult.CONSUME;
			}
			ActionResult actionResult;
			actionResult = super.interactMob(player, hand);
			if (actionResult.isAccepted()) {
				this.setPersistent();
			}
			return actionResult;
		}
	}

	@Override
	public boolean isInvulnerableTo(DamageSource damageSource) {
		if(damageSource == DamageSource.CACTUS)
			return true;

		return super.isInvulnerableTo(damageSource);
	}

	@Override
	public boolean isBreedingItem(ItemStack stack) {
		return TEMPT_ITEM.test(stack);
	}

	@Override
	public boolean canBeLeashedBy(PlayerEntity player)
	{
		return super.canBeLeashedBy(player) && isPassive();
	}

	@Override
	public PassiveEntity createChild(ServerWorld world, PassiveEntity entity)
	{
		ScuttlerEntity baby = BMEntities.SCUTTLER.create(world);
		baby.setPassive(true);
		return baby;
	}

	@Override
	public void onTrackedDataSet(TrackedData<?> data)
	{
		super.onTrackedDataSet(data);

		if(data == RATTLING)
			rattleTime = 0;
	}

	public boolean isPassive()
	{
		return passive;
	}

	public void setPassive(boolean passive)
	{
		this.passive = passive;
	}

	@Override
	public void writeCustomDataToTag(CompoundTag tag)
	{
		super.writeCustomDataToTag(tag);
		tag.putBoolean("Passive", passive);
		tag.putInt("EatCooldown", eatCooldown);
	}

	@Override
	public void readCustomDataFromTag(CompoundTag tag)
	{
		super.readCustomDataFromTag(tag);
		passive = tag.getBoolean("Passive");
		eatCooldown = tag.getInt("EatCooldown");
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

	@Override
	protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions)
	{
		return 0.2F;
	}

	@Override
	public boolean canSpawn(WorldAccess world, SpawnReason spawnReason) {
		return world.getRandom().nextBoolean() && world.getEntitiesByClass(ScuttlerEntity.class, new Box(new BlockPos(getX(), getY(), getZ())).expand(50), (e)->true).isEmpty() &&  super.canSpawn(world, spawnReason);
	}

	@Override
	public int getLimitPerChunk() {
		return 1;
	}


	private static class RattleGoal<T extends LivingEntity> extends Goal
	{
		private final ScuttlerEntity scuttler;
		private final float distance;
		private final Class<T> targetClass;
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
			if(scuttler.isTouchingWater() || scuttler.isPassive())
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
				return !targetEntity.isHolding(BMItems.PINK_PETALS) && scuttler.distanceTo(targetEntity) >= distance / 2;
			}
		}

		@Override
		public boolean shouldContinue()
		{
			if(targetEntity.isHolding(BMItems.PINK_PETALS))
				return false;

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

	public class EatFlowerGoal extends Goal
	{
		private BlockPos targetPos;

		public EatFlowerGoal()
		{
			this.setControls(EnumSet.of(Goal.Control.MOVE, Control.LOOK));
		}

		@Override
		public boolean canStart()
		{
			if(eatCooldown > 0)
				return false;

			BlockPos eatPos = findCactus();
			if(eatPos != null)
			{
				targetPos = eatPos;
				return true;
			}
			return false;
		}

		@Override
		public void tick()
		{
			if(squaredDistanceTo(targetPos.getX() + 0.5F, targetPos.getY() + 0.5F, targetPos.getZ() + 0.5F) > 2F)
			{
				getMoveControl().moveTo(targetPos.getX() + 0.5F, targetPos.getY() + 0.5F, targetPos.getZ() + 0.5F, 0.6F);
			}
			getLookControl().lookAt(targetPos.getX() + 0.5F, targetPos.getY() + 0.5F, targetPos.getZ() + 0.5F);
			if(eatTime <= 1)
			{
				BlockState st = world.getBlockState(targetPos);
				if(st.isOf(BMBlocks.BARREL_CACTUS_FLOWERED))
				{
					world.setBlockState(targetPos, BMBlocks.BARREL_CACTUS.getDefaultState());
					ItemScatterer.spawn(world, targetPos.getX(), targetPos.getY(), targetPos.getZ(), new ItemStack(BMItems.PINK_PETALS));
					eatCooldown = 100 + random.nextInt(200);
				}
			}
		}

		@Override
		public boolean shouldContinue()
		{
			if(eatTime <= 0 || eatCooldown > 0)
				return false;

			BlockState st = world.getBlockState(targetPos);
			if(!st.isOf(BMBlocks.BARREL_CACTUS_FLOWERED))
				return false;

			return squaredDistanceTo(targetPos.getX() + 0.5F, targetPos.getY() + 0.5F, targetPos.getZ() + 0.5F) <= 2;
		}

		@Override
		public void start()
		{
			dataTracker.set(EATING, true);
			eatTime = 20 + random.nextInt(40);
		}

		@Override
		public void stop()
		{
			dataTracker.set(EATING, false);
		}

		private BlockPos findCactus()
		{
			BlockPos startPos = getBlockPos();
			List<BlockPos> spots = Lists.newArrayList();

			BlockPos.Mutable m = new BlockPos.Mutable(startPos.getX(), startPos.getY(), startPos.getZ());
			for(int x = startPos.getX() - 2; x < startPos.getX() + 2; x++)
			{
				for(int z = startPos.getZ() - 2; z < startPos.getZ() + 2; z++)
				{
					m.set(x, startPos.getY(), z);
					BlockState checkState = world.getBlockState(m);
					if(checkState.isOf(BMBlocks.BARREL_CACTUS_FLOWERED))
					{
						spots.add(new BlockPos(m.getX(), m.getY(), m.getZ()));
					}
				}
			}
			if(spots.isEmpty())
				return null;
			return spots.get(world.random.nextInt(spots.size()));
		}
	}

}
