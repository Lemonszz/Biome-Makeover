package party.lemons.biomemakeover.entity.ai;

import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.entity.ai.pathing.LandPathNodeMaker;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.passive.TameableEntity;
import net.minecraft.util.math.BlockPos;

public class FlyingFollowOwnerGoal extends BetterFollowOwnerGoal
{
	public FlyingFollowOwnerGoal(TameableEntity tameable, double speed, float minDistance, float maxDistance, boolean leavesAllowed)
	{
		super(tameable, speed, minDistance, maxDistance, leavesAllowed);
	}

	protected void startFollowing()
	{
		this.navigation.startMovingTo(this.owner, owner.isFallFlying() ? owner.flyingSpeed : speed);
	}

	@Override
	protected boolean canTeleportTo(BlockPos pos)
	{
		PathNodeType pathNodeType = LandPathNodeMaker.getLandNodeType(this.world, pos.mutableCopy());
		if(pathNodeType != PathNodeType.WALKABLE && pathNodeType != PathNodeType.OPEN)
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
