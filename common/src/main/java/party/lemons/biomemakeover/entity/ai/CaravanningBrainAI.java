package party.lemons.biomemakeover.entity.ai;

import com.google.common.collect.ImmutableMap;
import net.minecraft.Util;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import party.lemons.biomemakeover.entity.camel.Caravanner;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class CaravanningBrainAI extends Behavior<Camel> {

    public CaravanningBrainAI() {
        super(Util.make(() -> {
            ImmutableMap.Builder<MemoryModuleType<?>, MemoryStatus> builder = ImmutableMap.builder();
            builder.put(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED);
            builder.put(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED);
            builder.put(MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_ABSENT);
            builder.put(MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT);
            return builder.build();
        }));
    }

    @Override
    protected boolean timedOut(long l) {
        return false;
    }

    protected boolean canStillUse(ServerLevel serverLevel, Camel camel, long l)
    {
        Caravanner caravanner = (Caravanner) camel;

        if (caravanner.isCaravanning() && caravanner.getCaravanLeader().isAlive() && this.firstIsLeashed((PathfinderMob & Caravanner)camel, 0)) {
            double d = camel.distanceToSqr(caravanner.getCaravanLeader());
            if (d > 676.0) {
                return false;
            }
        } else {
            return false;
        }

        return caravanner.hasCaravanLeader()
                && !camel.getBrain().hasMemoryValue(MemoryModuleType.BREED_TARGET)
                && !camel.getBrain().hasMemoryValue(MemoryModuleType.IS_PANICKING);
    }

    protected void start(ServerLevel serverLevel, Camel mob, long l)
    {
        Caravanner caravanner = (Caravanner) mob;

        if (!caravanner.canBeLeader() && !caravanner.isCaravanning())
        {
            List<Entity> list = mob.level().getEntities(mob, mob.getBoundingBox().inflate(9.0, 4.0, 9.0), e -> e instanceof Caravanner);

            Caravanner target = null;
            double distance = Double.MAX_VALUE;

            for(Entity entity : list) {
                Caravanner checkTarget = (Caravanner)entity;
                if (checkTarget.isCaravanning() && !checkTarget.hasCaravanTail()) {
                    double distToCheck = mob.distanceToSqr((PathfinderMob)checkTarget);
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
                        double checkDistance = mob.distanceToSqr((PathfinderMob)checkTarget);
                        if (!(checkDistance > distance)) {
                            distance = checkDistance;
                            target = checkTarget;
                        }
                    }
                }
            }

            if (!(target == null || distance < 4.0 || !target.canBeFollowed() && !firstIsLeashed((PathfinderMob & Caravanner)target, 1)))
            {
                caravanner.followCaravan((PathfinderMob & Caravanner)target);
            }
        }
    }

    private <E extends PathfinderMob & Caravanner> boolean firstIsLeashed(E llama, int i) {
        if (i > 12) {
            return false;
        } else if (llama.hasCaravanLeader())
        {
            return llama.getCaravanLeader().canBeLeader() || firstIsLeashed(llama.getCaravanLeader(), ++i);
        }
        else
        {
            return false;
        }
    }

    protected void stop(ServerLevel serverLevel, Camel pathfinderMob, long l) {
        Brain<?> brain = pathfinderMob.getBrain();
        brain.eraseMemory(MemoryModuleType.WALK_TARGET);
        brain.eraseMemory(MemoryModuleType.LOOK_TARGET);

        ((Caravanner)pathfinderMob).stopFollowingCaravan();
    }

    protected void tick(ServerLevel serverLevel, Camel camel, long l) {
        Caravanner caravanner = (Caravanner) camel;

        if (caravanner.isCaravanning()) {
            if (!(camel.getLeashHolder() instanceof LeashFenceKnotEntity)) {
                PathfinderMob leader = caravanner.getCaravanLeader();
                double d = camel.distanceTo(leader);
                float f = 2.0F;
                Vec3 dist = new Vec3(leader.getX() - camel.getX(), leader.getY() - camel.getY(), leader.getZ() - camel.getZ())
                        .normalize()
                        .scale(Math.max(d - f, 0.0));
                camel.getNavigation().moveTo(camel.getX() + dist.x, camel.getY() + dist.y, camel.getZ() + dist.z, 2.5F);
            }
        }
    }
}
