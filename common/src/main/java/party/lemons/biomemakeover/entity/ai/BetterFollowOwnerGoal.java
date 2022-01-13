package party.lemons.biomemakeover.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.EnumSet;

public class BetterFollowOwnerGoal extends Goal
{
    protected final TamableAnimal tameable;
    protected LivingEntity owner;
    protected final Level level;
    protected final double speed;
    protected final PathNavigation navigation;
    protected int updateCountdownTicks;
    protected final float maxDistance;
    protected final float minDistance;
    protected float oldWaterPathfindingPenalty;
    protected final boolean leavesAllowed;

    public BetterFollowOwnerGoal(TamableAnimal tameable, double speed, float minDistance, float maxDistance, boolean leavesAllowed)
    {
        this.tameable = tameable;
        this.level = tameable.level;
        this.speed = speed;
        this.navigation = tameable.getNavigation();
        this.minDistance = minDistance;
        this.maxDistance = maxDistance;
        this.leavesAllowed = leavesAllowed;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        if(!(tameable.getNavigation() instanceof GroundPathNavigation) && !(tameable.getNavigation() instanceof FlyingPathNavigation))
        {
            throw new IllegalArgumentException("Unsupported mob type for FollowOwnerGoal");
        }
    }

    @Override
    public boolean canUse() {
        LivingEntity livingEntity = this.tameable.getOwner();
        if(livingEntity == null)
        {
            return false;
        }
        else if(livingEntity.isSpectator())
        {
            return false;
        }
        else if(this.tameable.isOrderedToSit())
        {
            return false;
        }
        else if(this.tameable.distanceToSqr(livingEntity) < (double) (this.minDistance * this.minDistance))
        {
            return false;
        }
        else
        {
            this.owner = livingEntity;
            return true;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if(this.navigation.isDone())
        {
            return false;
        }
        else if(this.tameable.isOrderedToSit())
        {
            return false;
        }
        else
        {
            return this.tameable.distanceToSqr(this.owner) > (double) (this.maxDistance * this.maxDistance);
        }
    }

    public void start()
    {
        this.updateCountdownTicks = 0;
        this.oldWaterPathfindingPenalty = this.tameable.getPathfindingMalus(BlockPathTypes.WATER);
        this.tameable.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
    }

    public void stop()
    {
        this.owner = null;
        this.navigation.stop();
        this.tameable.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterPathfindingPenalty);
    }

    public void tick()
    {
        this.tameable.getLookControl().setLookAt(this.owner, 10.0F, (float) this.tameable.getMaxHeadXRot());
        if(--this.updateCountdownTicks <= 0)
        {
            this.updateCountdownTicks = 10;
            if(!this.tameable.isLeashed() && !this.tameable.isPassenger())
            {
                if(this.tameable.distanceToSqr(this.owner) >= 144.0D)
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
        this.navigation.moveTo(this.owner, this.speed);
    }

    protected void tryTeleport()
    {
        BlockPos blockPos = this.owner.getOnPos();

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
            this.tameable.moveTo((double) x + 0.5D, (double) y, (double) z + 0.5D, this.tameable.xRotO, this.tameable.yRotO);
            this.navigation.stop();
            return true;
        }
    }

    protected boolean canTeleportTo(BlockPos pos)
    {
        BlockPathTypes pathNodeType = WalkNodeEvaluator.getBlockPathTypeStatic(this.level, pos.mutable());
        if(pathNodeType != BlockPathTypes.WALKABLE)
        {
            return false;
        }
        else
        {
            BlockState blockState = this.level.getBlockState(pos.below());
            if(!this.leavesAllowed && blockState.getBlock() instanceof LeavesBlock)
            {
                return false;
            }
            else
            {
                BlockPos blockPos = pos.subtract(this.tameable.getOnPos());
                return this.level.noCollision(this.tameable, this.tameable.getBoundingBox().move(blockPos));
            }
        }
    }
}