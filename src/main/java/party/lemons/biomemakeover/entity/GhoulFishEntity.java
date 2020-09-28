package party.lemons.biomemakeover.entity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetFinder;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import party.lemons.biomemakeover.init.BMEntities;
import software.bernie.geckolib.animation.builder.AnimationBuilder;
import software.bernie.geckolib.animation.controller.EntityAnimationController;
import software.bernie.geckolib.entity.IAnimatedEntity;
import software.bernie.geckolib.event.AnimationTestEvent;
import software.bernie.geckolib.manager.EntityAnimationManager;

import java.util.EnumSet;

public class GhoulFishEntity extends PathAwareEntity implements IAnimatedEntity, Flutterer
{
	public AttributeContainer attributeContainer;
	EntityAnimationManager manager = new EntityAnimationManager();
	EntityAnimationController controller = new EntityAnimationController(this, "swimController", 20, this::animationPredicate);

	public GhoulFishEntity(World world)
	{
		super(BMEntities.GHOUL_FISH, world);
		manager.addAnimationController(controller);
		this.moveControl = new FlightMoveControl(this, 20, true);
		this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
		this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
		this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 16.0F);
		this.setPathfindingPenalty(PathNodeType.COCOA, -1.0F);
		this.setPathfindingPenalty(PathNodeType.FENCE, -1.0F);
	}

	public float getPathfindingFavor(BlockPos pos, WorldView world) {
		return world.getBlockState(pos).isAir() ? 10.0F : 0.0F;
	}

	protected void initGoals() {
		this.goalSelector.add(0, new FlySwimWanderGoal());
		//this.goalSelector.add(2, new FleeEntityGoal(this, PlayerEntity.class, 8.0F, 1.6D, 1.4D, EntityPredicates.EXCEPT_SPECTATOR));
	}

	@Override
	public EntityAnimationManager getAnimationManager()
	{
		return manager;
	}

	private <E extends GhoulFishEntity> boolean animationPredicate(AnimationTestEvent<E> event)
	{
		controller.setAnimation(new AnimationBuilder().addAnimation("swim", true));
		return true;
	}

	@Override
	public AttributeContainer getAttributes()
	{
		if(attributeContainer == null)
		{
			attributeContainer =  new AttributeContainer(MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_FLYING_SPEED, 0.8).add(EntityAttributes.GENERIC_MAX_HEALTH, 3.0D).build());
		}

		return attributeContainer;
	}

	@Override
	public void tick()
	{
		super.tick();

	}

	protected EntityNavigation createNavigation(World world) {
		BirdNavigation birdNavigation = new BirdNavigation(this, world) {
			public boolean isValidPosition(BlockPos pos) {
				return !this.world.getBlockState(pos.down()).isAir();
			}

			public void tick()
			{
				if(random.nextInt(600) == 0)
				{
					if(getTargetPos() != null)
					{
						BlockPos target = getTargetPos();
						GhoulFishEntity.this.teleportTo(target.getX() + 0.5F, target.getY() + 0.5F, target.getZ() + 0.5F);
					}
				}

				super.tick();
			}
		};
		birdNavigation.setCanPathThroughDoors(false);
		birdNavigation.setCanSwim(false);
		birdNavigation.setCanEnterOpenDoors(true);
		return birdNavigation;
	}

	protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
		return dimensions.height * 0.65F;
	}

	private boolean teleportTo(double x, double y, double z) {
		BlockPos.Mutable mutable = new BlockPos.Mutable(x, y, z);

		while(mutable.getY() > 0 && !this.world.getBlockState(mutable).getMaterial().blocksMovement()) {
			mutable.move(Direction.DOWN);
		}

		BlockState blockState = this.world.getBlockState(mutable);
		boolean bl = blockState.getMaterial().blocksMovement();
		boolean bl2 = blockState.getFluidState().isIn(FluidTags.WATER);
		if (bl && !bl2) {
			boolean bl3 = this.teleport(x, y, z, true);
			if (bl3 && !this.isSilent()) {
				this.world.playSound(null, this.prevX, this.prevY, this.prevZ, SoundEvents.ENTITY_ENDERMAN_TELEPORT, this.getSoundCategory(), 1.0F, 1.0F);
				this.playSound(SoundEvents.ENTITY_ENDERMAN_TELEPORT, 1.0F, 1.0F);
			}

			return bl3;
		} else {
			return false;
		}
	}

	public int getLimitPerChunk() {
		return 4;
	}
	public EntityGroup getGroup() {
		return EntityGroup.DEFAULT;
	}

	public boolean handleFallDamage(float fallDistance, float damageMultiplier) {
		return false;
	}

	protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
	}


	class FlySwimWanderGoal extends Goal {
		FlySwimWanderGoal() {
			this.setControls(EnumSet.of(Goal.Control.MOVE));
		}

		public boolean canStart() {
			return GhoulFishEntity.this.navigation.isIdle() && GhoulFishEntity.this.random.nextInt(10) == 0;
		}

		public boolean shouldContinue() {
			return GhoulFishEntity.this.navigation.isFollowingPath();
		}

		public void start() {
			Vec3d vec3d = this.getRandomLocation();
			if (vec3d != null) {
				GhoulFishEntity.this.navigation.startMovingAlong(GhoulFishEntity.this.navigation.findPathTo(new BlockPos(vec3d), 1), 1.0D);
			}

		}

		private Vec3d getRandomLocation() {
			Vec3d vec3d3 = GhoulFishEntity.this.getRotationVec(0.0F);

			Vec3d dir = TargetFinder.findAirTarget(GhoulFishEntity.this, 8, 15, vec3d3, 1.5F, 4, 1);
			return dir != null ? dir : TargetFinder.findGroundTarget(GhoulFishEntity.this, 16, 4, -2, vec3d3, 1.5F);
		}
	}
}
