package party.lemons.biomemakeover.mixin.caravan;

import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.*;
import party.lemons.biomemakeover.entity.camel.CamelExtension;
import party.lemons.biomemakeover.entity.camel.Caravanner;

@Mixin(Camel.class)
public abstract class CamelMixin extends AbstractHorse implements Caravanner, CamelExtension
{
    @Shadow protected abstract void positionRider(Entity entity, MoveFunction moveFunction);

    @Shadow @Final @Mutable public AnimationState sitAnimationState;
    @Shadow @Final @Mutable public AnimationState sitPoseAnimationState;
    @Shadow @Final @Mutable public AnimationState sitUpAnimationState;
    @Shadow @Final @Mutable public AnimationState idleAnimationState;
    @Shadow @Final @Mutable public AnimationState dashAnimationState;
    @Unique
    @Nullable
    private PathfinderMob bm_caravanHead = null;

    @Unique @Nullable
    private PathfinderMob bm_caravanTail = null;

    protected CamelMixin(EntityType<? extends AbstractHorse> entityType, Level level) {
        super(entityType, level);
    }

    @Unique
    private boolean isLeashedOrRidden()
    {
        return isLeashed() || (isVehicle() && isSaddled());
    }

    @Override
    public boolean isCaravanning() {
        return bm_caravanHead != null;
    }

    @Override
    public boolean canJoinCaravan() {
        return !isCaravanning() && !isLeashedOrRidden();
    }

    @Override
    public boolean isCaravanHead() {
        return getCaravanLeader() == null && getCaravanTail() != null;
    }

    @Override
    public <E extends PathfinderMob & Caravanner> @Nullable E getCaravanLeader() {
        return (E) bm_caravanHead;
    }

    @Override
    public <E extends PathfinderMob & Caravanner> @Nullable E getCaravanTail() {
        return (E) bm_caravanTail;
    }

    @Override
    public <E extends PathfinderMob & Caravanner> void followCaravan(E mob) {
        this.bm_caravanHead = mob;
        ((Caravanner)this.bm_caravanHead).setTail(this);
    }

    @Override
    public void stopFollowingCaravan() {
        if (this.bm_caravanHead != null) {
            ((Caravanner)this.bm_caravanHead).setTail(null);
        }
        this.bm_caravanHead = null;
    }

    @Override
    public boolean canBeFollowed() {
        return isLeashedOrRidden() && !hasCaravanTail();
    }

    @Override
    public boolean canBeLeader() {
        return isLeashedOrRidden();
    }

    @Override
    public <E extends PathfinderMob & Caravanner> void setTail(E tail) {
        this.bm_caravanTail = tail;
    }

    @Override
    public void setAnimationStates(AnimationState sitAnim, AnimationState sitPose, AnimationState sitUp, AnimationState idle, AnimationState dash) {
        sitAnimationState = sitAnim;
        sitPoseAnimationState = sitPose;
        sitUpAnimationState = sitUp;
        idleAnimationState = idle;
        dashAnimationState = dash;
    }
}
