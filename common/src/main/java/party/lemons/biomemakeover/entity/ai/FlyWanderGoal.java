package party.lemons.biomemakeover.entity.ai;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.AirAndWaterRandomPos;
import net.minecraft.world.entity.ai.util.HoverRandomPos;
import net.minecraft.world.phys.Vec3;

import java.util.EnumSet;

public class FlyWanderGoal extends Goal
{
    private final PathfinderMob entity;

    public FlyWanderGoal(PathfinderMob e)
    {
        this.entity = e;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        return entity.getNavigation().isDone();
    }

    @Override
    public boolean canContinueToUse() {
        return entity.getNavigation().isInProgress();
    }

    public void start()
    {
        Vec3 vec3d = this.getRandomLocation();
        if(vec3d != null)
        {
            entity.getNavigation().moveTo(entity.getNavigation().createPath(new BlockPos(vec3d), 1), 1.0D);
        }

    }

    private Vec3 getRandomLocation()
    {
        Vec3 vec3d3 = entity.getViewVector(0.0F);
        Vec3 vec3d4 = HoverRandomPos.getPos(entity, 8, 7, vec3d3.x, vec3d3.z, 1.5707964F, 2, 1);
        return vec3d4 != null ? vec3d4 : AirAndWaterRandomPos.getPos(entity, 8, 4, -2, vec3d3.x, vec3d3.z, 1.5707963705062866D);
    }
}