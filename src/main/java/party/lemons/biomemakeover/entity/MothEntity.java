package party.lemons.biomemakeover.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.ai.TargetFinder;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import party.lemons.biomemakeover.entity.ai.FlyWanderAroundGoal;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.init.BMEntities;

import java.util.EnumSet;
import java.util.Optional;

public class MothEntity extends HostileEntity
{
	private static final TrackedData<Boolean> TARGETING = DataTracker.registerData(MothEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	public boolean hasPlayedLoop = false;
	private float currentPitch;
	private float lastPitch;
	private AttractLightGoal attractLightGoal;
	private MoveToLightGoal moveToLightGoal;
	private BlockPos attactPos;
	private AttributeContainer attributeContainer;

	public MothEntity(World world)
	{
		super(BMEntities.MOTH, world);
		this.moveControl = new FlightMoveControl(this, 20, true);
		this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
		this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
		this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 16.0F);
		this.setPathfindingPenalty(PathNodeType.COCOA, -1.0F);
		this.setPathfindingPenalty(PathNodeType.FENCE, -1.0F);
	}

	@Override
	protected void initGoals() {

		this.attractLightGoal = new AttractLightGoal();
		this.moveToLightGoal = new MoveToLightGoal();

		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(1, new MeleeAttackGoal(this, 1.0D, false));
		this.goalSelector.add(2, new FleeEntityGoal<>(this, OwlEntity.class, 6.0F, 1.0D, 1.2D));
		this.goalSelector.add(3, this.attractLightGoal);
		this.goalSelector.add(4, this.moveToLightGoal);
		this.goalSelector.add(5, new FlyWanderAroundGoal(this));

		this.targetSelector.add(1, new FollowTargetGoal<>(this, PlayerEntity.class, true));
		this.targetSelector.add(2, new RevengeGoal(this));
	}

	@Override
	protected void initDataTracker()
	{
		super.initDataTracker();
		this.dataTracker.startTracking(TARGETING, false);
	}

	@Override
	public AttributeContainer getAttributes()
	{
		if(attributeContainer == null)
			attributeContainer = new AttributeContainer(HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_FLYING_SPEED, 0.6F).add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D).build());
		return attributeContainer;
	}

	@Override
	public float getPathfindingFavor(BlockPos pos, WorldView world)
	{
		if(world.getBlockState(pos).isAir())
		{
			return 10F + world.getLightLevel(pos);
		}

		return 0;
	}

	@Environment(EnvType.CLIENT)
	public float getBodyPitch(float tickDelta)
	{
		return MathHelper.lerp(tickDelta, this.lastPitch, this.currentPitch);
	}

	private void updateBodyPitch() {
		this.lastPitch = this.currentPitch;
		if (this.isNearTarget())
		{
			this.currentPitch = Math.min(1.0F, this.currentPitch + 0.2F);
		}
		else
		{
			this.currentPitch = Math.max((float)Math.sin(age / 10F) / 10F, this.currentPitch - 0.24F);
		}
	}

	@Override
	protected EntityNavigation createNavigation(World world) {
		BirdNavigation birdNavigation = new BirdNavigation(this, world) {
			public boolean isValidPosition(BlockPos pos) {
				return !this.world.getBlockState(pos.down()).isAir();
			}

			public void tick() {
				if (!MothEntity.this.attractLightGoal.isRunning()) {
					super.tick();
				}
			}
		};
		birdNavigation.setCanPathThroughDoors(false);
		birdNavigation.setCanSwim(false);
		birdNavigation.setCanEnterOpenDoors(true);
		return birdNavigation;
	}

	private boolean isAttractive(BlockPos pos) {
		return this.world.canSetBlock(pos) && ((world.getBlockState(pos).getLuminance()) > 10) || this.world.getBlockState(pos).getBlock().isIn(BMBlocks.MOTH_ATTRACTIVE);
	}

	@Override
	protected float getVelocityMultiplier() {
		Block block = this.world.getBlockState(this.getBlockPos()).getBlock();
		float mult;
		if(block.isIn(BMBlocks.ITCHING_IVY_TAG))
			mult = 1F;
		else
			mult = block.getVelocityMultiplier();

		if (block != Blocks.WATER && block != Blocks.BUBBLE_COLUMN)
		{
			if(mult == 1F)
			{
				Block velBlock = this.world.getBlockState(this.getVelocityAffectingPos()).getBlock();
				if(!velBlock.isIn(BMBlocks.ITCHING_IVY_TAG))
				{
					return  velBlock.getVelocityMultiplier();
				}
				else
				{
					return mult;
				}
			}
			else
			{
				return mult;
			}
		}
		else {
			return mult;
		}
	}

	@Override
	protected void playStepSound(BlockPos pos, BlockState state) {
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return BMEffects.MOTH_IDLE;
	}

	@Override
	public boolean handleFallDamage(float fallDistance, float damageMultiplier) {
		return false;
	}

	@Override
	protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
	}

	@Override
	protected boolean hasWings() {
		return true;
	}

	@Override
	public EntityGroup getGroup() {
		return EntityGroup.ARTHROPOD;
	}

	@Override
	public void tick() {
		super.tick();
		this.updateBodyPitch();
	}

	private boolean isNearTarget()
	{
		//TODO:
		return false;
	}

	public boolean isTargeting()
	{
		return dataTracker.get(TARGETING);
	}

	private boolean isWithinDistance(BlockPos pos, int distance) {
		return pos.isWithinDistance(this.getBlockPos(), (double)distance);
	}

	private boolean isTooFar(BlockPos pos) {
		return !this.isWithinDistance(pos, 32);
	}

	private void startMovingTo(BlockPos pos) {
		Vec3d centerBottom = Vec3d.ofBottomCenter(pos);
		int yBias = 0;
		BlockPos blockPos = this.getBlockPos();
		int yPos = (int)centerBottom.y - blockPos.getY();
		if (yPos > 2) {
			yBias = 4;
		} else if (yPos < -2) {
			yBias = -4;
		}

		int hDifference = 6;
		int vDifference = 8;
		int distance = blockPos.getManhattanDistance(pos);
		if (distance < 15) {
			hDifference = distance / 2;
			vDifference = distance / 2;
		}

		Vec3d targetPosition = TargetFinder.findGroundTargetTowards(this, hDifference, vDifference, yBias, centerBottom, 0.3141592741012573D);
		if (targetPosition != null) {
			this.navigation.setRangeMultiplier(0.5F);
			this.navigation.startMovingTo(targetPosition.x, targetPosition.y, targetPosition.z, 1.0D);
		}
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return BMEffects.MOTH_DEATH;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source)
	{
		return BMEffects.MOTH_HURT;
	}

	@Override
	public boolean tryAttack(Entity target)
	{
		boolean attacked = super.tryAttack(target);
		if(attacked)
		{
			this.playSound(BMEffects.MOTH_BITE, 1.0F, 1.0F);
		}
		return attacked;
	}

	public class MoveToLightGoal extends NotAttackingGoal {
		private int ticks;

		MoveToLightGoal() {
			super();
			this.ticks = MothEntity.this.world.random.nextInt(10);
			this.setControls(EnumSet.of(Goal.Control.MOVE));
		}

		public boolean canMothContinue() {
			return MothEntity.this.attactPos != null && !MothEntity.this.hasPositionTarget() && MothEntity.this.isAttractive(MothEntity.this.attactPos) && !MothEntity.this.isWithinDistance(MothEntity.this.attactPos, 2);
		}

		public boolean canMothStart() {
			return this.canMothContinue();
		}

		public void start() {
			this.ticks = 0;
			super.start();
		}

		public void stop() {
			this.ticks = 0;
			MothEntity.this.navigation.stop();
			MothEntity.this.navigation.resetRangeMultiplier();
		}

		public void tick() {
			if (MothEntity.this.attactPos != null) {
				++this.ticks;
				if (this.ticks > 600) {
					MothEntity.this.attactPos = null;
				} else if (!MothEntity.this.navigation.isFollowingPath()) {
					if (MothEntity.this.isTooFar(MothEntity.this.attactPos)) {
						MothEntity.this.attactPos = null;
					} else {
						MothEntity.this.startMovingTo(MothEntity.this.attactPos);
					}
				}
			}
		}
	}

	private abstract class NotAttackingGoal extends Goal
	{
		private NotAttackingGoal() {
		}

		public abstract boolean canMothContinue();

		public abstract boolean canMothStart();

		public boolean canStart() {
			return this.canMothStart() && !MothEntity.this.isTargeting();
		}

		public boolean shouldContinue() {
			return this.canMothContinue() && !MothEntity.this.isTargeting();
		}
	}

	private class AttractLightGoal extends NotAttackingGoal
	{
		private int attractTicks = 0;
		private int lastAttractTick = 0;
		private boolean running;
		private Vec3d nextTarget;
		private int ticks = 0;

		AttractLightGoal() {
			super();
			this.setControls(EnumSet.of(Goal.Control.MOVE));
		}

		public boolean canMothStart() {
			if (MothEntity.this.world.isRaining()) {
				return false;
			} else if (MothEntity.this.random.nextFloat() < 0.7F) {
				return false;
			} else {
				Optional<BlockPos> optional = this.findLight();
				if (optional.isPresent())
				{
					MothEntity.this.attactPos = optional.get();
					MothEntity.this.navigation.startMovingTo((double)MothEntity.this.attactPos.getX() + 0.5D, (double)MothEntity.this.attactPos
							.getY() + 1.25F, (double)MothEntity.this.attactPos.getZ() + 0.5D, 1.2F);
					return true;
				} else {
					return false;
				}
			}
		}

		public boolean canMothContinue() {
			if (!this.running) {
				return false;
			} else if (MothEntity.this.attactPos == null) {
				return false;
			} else if (MothEntity.this.world.isRaining()) {
				return false;
			} else if (this.completedAttract()) {
				return MothEntity.this.random.nextFloat() < 0.2F;
			} else if (MothEntity.this.age % 20 == 0 && !MothEntity.this.isAttractive(MothEntity.this.attactPos)) {
				MothEntity.this.attactPos = null;
				return false;
			}
			else if(attactPos != null && attactPos.getY() < getY() && !MothEntity.this.isWithinDistance(attactPos, 1) && world.getBlockState(MothEntity.this.getBlockPos().down()).isOpaque()){
				MothEntity.this.attactPos = null;

				return false;
			}
			else {
				return true;
			}
		}

		private boolean completedAttract() {
			return this.attractTicks > 400;
		}

		private boolean isRunning() {
			return this.running;
		}

		private void cancel() {
			this.running = false;
		}

		public void start() {
			this.attractTicks = 0;
			this.ticks = 0;
			this.lastAttractTick = 0;
			this.running = true;
		}

		public void stop() {
			this.running = false;
			MothEntity.this.navigation.stop();
		}

		public void tick()
		{
			++this.ticks;
			if (this.ticks > 600) {
				MothEntity.this.attactPos = null;
			} else {

				Vec3d centerTarget = Vec3d.ofBottomCenter(attactPos);
				if(MothEntity.this.getY() < centerTarget.y)
					centerTarget = centerTarget.add(0, -1.25F, 0);
				else centerTarget = centerTarget.add(0, 1.25F, 0);

				if (centerTarget.distanceTo(MothEntity.this.getPos()) > 1.0D) {
					this.nextTarget = centerTarget;
					this.moveToNextTarget();
				} else {
					if (this.nextTarget == null) {
						this.nextTarget = centerTarget;
					}

					boolean isClose = MothEntity.this.getPos().distanceTo(this.nextTarget) <= 0.1D;
					boolean hasFinished = true;
					if (!isClose && this.ticks > 600) {
						MothEntity.this.attactPos = null;
					} else {
						if (isClose) {
							boolean getNewTarget = MothEntity.this.random.nextInt(25) == 0;
							if (getNewTarget) {
								this.nextTarget = new Vec3d(centerTarget.getX() + (double)this.getRandomOffset(), centerTarget.getY(), centerTarget.getZ() + (double)this.getRandomOffset());
								MothEntity.this.navigation.stop();
							} else {
								hasFinished = false;
							}

							MothEntity.this.getLookControl().lookAt(centerTarget.getX(), centerTarget.getY(), centerTarget.getZ());
						}

						if (hasFinished) {
							this.moveToNextTarget();
						}

						++this.attractTicks;
						if (MothEntity.this.random.nextFloat() < 0.05F && this.attractTicks > this.lastAttractTick + 60) {
							this.lastAttractTick = this.attractTicks;
						}

					}
				}
			}
		}

		private void moveToNextTarget() {
			MothEntity.this.getMoveControl().moveTo(this.nextTarget.getX(), this.nextTarget.getY(), this.nextTarget.getZ(), 0.35F);
		}

		private float getRandomOffset() {
			return (MothEntity.this.random.nextFloat() * 2.0F - 1.0F) * 0.33333334F;
		}

		private Optional<BlockPos> findLight() {
			return this.findLight(5.0D);
		}

		private Optional<BlockPos> findLight(double searchDistance) {
			BlockPos blockPos = MothEntity.this.getBlockPos();
			BlockPos.Mutable mutable = new BlockPos.Mutable();

			for(int y = 0; (double)y <= searchDistance; y = y > 0 ? -y : 1 - y) {
				for(int j = 0; (double)j < searchDistance; ++j) {
					for(int x = 0; x <= j; x = x > 0 ? -x : 1 - x) {
						for(int z = x < j && x > -j ? j : 0; z <= j; z = z > 0 ? -z : 1 - z) {
							mutable.set(blockPos, x, y - 1, z);
							if (blockPos.isWithinDistance(mutable, searchDistance) && MothEntity.this.isAttractive(mutable))
							{
								return Optional.of(mutable);
							}
						}
					}
				}
			}

			return Optional.empty();
		}
	}
}
