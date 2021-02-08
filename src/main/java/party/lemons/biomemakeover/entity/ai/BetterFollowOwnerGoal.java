package party.lemons.biomemakeover.entity.ai;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.*;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.EnumSet;

public class BetterFollowOwnerGoal extends Goal
{
	protected final TameableEntity tameable;
	protected LivingEntity owner;
	protected final WorldView world;
	protected final double speed;
	protected final EntityNavigation navigation;
	protected int updateCountdownTicks;
	protected final float maxDistance;
	protected final float minDistance;
	protected float oldWaterPathfindingPenalty;
	protected final boolean leavesAllowed;

	public BetterFollowOwnerGoal(TameableEntity tameable, double speed, float minDistance, float maxDistance, boolean leavesAllowed)
	{
		this.tameable = tameable;
		this.world = tameable.world;
		this.speed = speed;
		this.navigation = tameable.getNavigation();
		this.minDistance = minDistance;
		this.maxDistance = maxDistance;
		this.leavesAllowed = leavesAllowed;
		this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
		if(!(tameable.getNavigation() instanceof MobNavigation) && !(tameable.getNavigation() instanceof BirdNavigation))
		{
			throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
		}
	}

	public boolean canStart()
	{
		LivingEntity livingEntity = this.tameable.getOwner();
		if(livingEntity == null)
		{
			return false;
		}
		else if(livingEntity.isSpectator())
		{
			return false;
		}
		else if(this.tameable.isSitting())
		{
			return false;
		}
		else if(this.tameable.squaredDistanceTo(livingEntity) < (double) (this.minDistance * this.minDistance))
		{
			return false;
		}
		else
		{
			this.owner = livingEntity;
			return true;
		}
	}

	public boolean shouldContinue()
	{
		if(this.navigation.isIdle())
		{
			return false;
		}
		else if(this.tameable.isSitting())
		{
			return false;
		}
		else
		{
			return this.tameable.squaredDistanceTo(this.owner) > (double) (this.maxDistance * this.maxDistance);
		}
	}

	public void start()
	{
		this.updateCountdownTicks = 0;
		this.oldWaterPathfindingPenalty = this.tameable.getPathfindingPenalty(PathNodeType.WATER);
		this.tameable.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
	}

	public void stop()
	{
		this.owner = null;
		this.navigation.stop();
		this.tameable.setPathfindingPenalty(PathNodeType.WATER, this.oldWaterPathfindingPenalty);
	}

	public void tick()
	{
		this.tameable.getLookControl().lookAt(this.owner, 10.0F, (float) this.tameable.getLookPitchSpeed());
		if(--this.updateCountdownTicks <= 0)
		{
			this.updateCountdownTicks = 10;
			if(!this.tameable.isLeashed() && !this.tameable.hasVehicle())
			{
				if(this.tameable.squaredDistanceTo(this.owner) >= 144.0D)
				{
					this.tryTeleport();
				}
				else
				{
					startFollowing();
				}

			}
		}
	}

	protected void startFollowing()
	{
		this.navigation.startMovingTo(this.owner, this.speed);
	}

	protected void tryTeleport()
	{
		BlockPos blockPos = this.owner.getBlockPos();

		for(int i = 0; i < 10; ++i)
		{
			int j = RandomUtil.randomRange(-3, 3);
			int k = RandomUtil.randomRange(-1, 1);
			int l = RandomUtil.randomRange(-3, 3);
			boolean didTeleport = this.tryTeleportTo(blockPos.getX() + j, blockPos.getY() + k, blockPos.getZ() + l);
			if(didTeleport)
			{
				return;
			}
		}

	}

	protected boolean tryTeleportTo(int x, int y, int z)
	{
		if(Math.abs((double) x - this.owner.getX()) < 2.0D && Math.abs((double) z - this.owner.getZ()) < 2.0D)
		{
			return false;
		}
		else if(!this.canTeleportTo(new BlockPos(x, y, z)))
		{
			return false;
		}
		else
		{
			this.tameable.refreshPositionAndAngles((double) x + 0.5D, (double) y, (double) z + 0.5D, this.tameable.yaw, this.tameable.pitch);
			this.navigation.stop();
			return true;
		}
	}

	protected boolean canTeleportTo(BlockPos pos)
	{
		PathNodeType pathNodeType = LandPathNodeMaker.getLandNodeType(this.world, pos.mutableCopy());
		if(pathNodeType != PathNodeType.WALKABLE)
		{
			return false;
		}
		else
		{
			BlockState blockState = this.world.getBlockState(pos.down());
			if(!this.leavesAllowed && blockState.getBlock() instanceof LeavesBlock)
			{
				return false;
			}
			else
			{
				BlockPos blockPos = pos.subtract(this.tameable.getBlockPos());
				return this.world.isSpaceEmpty(this.tameable, this.tameable.getBoundingBox().offset(blockPos));
			}
		}
	}
}
