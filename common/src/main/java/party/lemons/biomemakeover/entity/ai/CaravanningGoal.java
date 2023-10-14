package party.lemons.biomemakeover.entity.ai;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.phys.Vec3;
import party.lemons.biomemakeover.entity.camel.Caravanner;

import java.util.EnumSet;
import java.util.List;

public class CaravanningGoal<Mob extends PathfinderMob & Caravanner> extends Goal {
    private static final int CARAVAN_LIMIT = 8;

    public final Mob mob;
    private double speedModifier;
    private int distCheckCounter;

    public CaravanningGoal(Mob llama, double d) {
        this.mob = llama;
        this.speedModifier = d;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (!this.mob.canBeLeader() && !this.mob.isCaravanning())
        {
            List<Entity> list = this.mob.level().getEntities(this.mob, this.mob.getBoundingBox().inflate(9.0, 4.0, 9.0), e -> e instanceof Caravanner);

            Caravanner target = null;
            double distance = Double.MAX_VALUE;

            for(Entity entity : list) {
                Caravanner checkTarget = (Caravanner)entity;
                if (checkTarget.isCaravanning() && !checkTarget.hasCaravanTail()) {
                    double distToCheck = this.mob.distanceToSqr((PathfinderMob)checkTarget);
                    if (!(distToCheck > distance)) {
                        distance = distToCheck;
                        target = checkTarget;
                    }
                }
            }

            if (target == null) {
                for(Entity entity : list) {
                    Caravanner checkTarget = (Caravanner)entity;
                    if (checkTarget.canBeFollowed()) {
                        double checkDistance = this.mob.distanceToSqr((PathfinderMob)checkTarget);
                        if (!(checkDistance > distance)) {
                            distance = checkDistance;
                            target = checkTarget;
                        }
                    }
                }
            }

            if (target == null)
            {
                return false;
            } else if (distance < 4.0) {
                return false;
            } else if (!target.canBeFollowed() && !this.firstIsLeashed((Mob)target, 1)) {
                return false;
            } else
            {
                this.mob.followCaravan((Mob)target);
                return true;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        if (this.mob.isCaravanning() && this.mob.getCaravanLeader().isAlive() && this.firstIsLeashed(this.mob, 0)) {
            double d = this.mob.distanceToSqr(this.mob.getCaravanLeader());
            if (d > 676.0) {
                if (this.speedModifier <= 3.0) {
                    this.speedModifier *= 1.2;
                    this.distCheckCounter = reducedTickDelay(40);
                    return true;
                }

                if (this.distCheckCounter == 0) {
                    return false;
                }
            }

            if (this.distCheckCounter > 0) {
                --this.distCheckCounter;
            }

            return true;
        } else {
            return false;
        }
    }

    @Override
    public void stop() {
        this.mob.stopFollowingCaravan();
        this.speedModifier = 2.1;
    }

    @Override
    public void tick() {
        if (this.mob.isCaravanning()) {
            if (!(this.mob.getLeashHolder() instanceof LeashFenceKnotEntity)) {
                PathfinderMob leader = this.mob.getCaravanLeader();
                double d = this.mob.distanceTo(leader);
                float f = 2.0F;
                Vec3 dist = new Vec3(leader.getX() - this.mob.getX(), leader.getY() - this.mob.getY(), leader.getZ() - this.mob.getZ())
                        .normalize()
                        .scale(Math.max(d - f, 0.0));
                this.mob.getNavigation().moveTo(this.mob.getX() + dist.x, this.mob.getY() + dist.y, this.mob.getZ() + dist.z, this.speedModifier);
            }
        }
    }

    private <E extends PathfinderMob & Caravanner> boolean firstIsLeashed(E llama, int i) {
        if (i > CARAVAN_LIMIT) {
            return false;
        } else if (llama.hasCaravanLeader())
        {
            return llama.getCaravanLeader().canBeLeader() || this.firstIsLeashed(llama.getCaravanLeader(), ++i);
        }
        else
        {
            return false;
        }
    }
}