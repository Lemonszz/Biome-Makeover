package party.lemons.biomemakeover.mixin.caravan;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.LlamaFollowCaravanGoal;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.Llama;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.entity.ai.CaravanningGoal;
import party.lemons.biomemakeover.entity.camel.Caravanner;
import party.lemons.biomemakeover.util.extension.GoalSelectorExtension;

@Mixin(Llama.class)
public abstract class LlamaMixin extends AbstractChestedHorse implements Caravanner {
    private LlamaMixin(EntityType<? extends AbstractChestedHorse> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At("TAIL"), method = "registerGoals")
    private void onGoalRegister(CallbackInfo cbi){
        GoalSelectorExtension.removeGoal(this, LlamaFollowCaravanGoal.class);
        this.goalSelector.addGoal(2, new CaravanningGoal<>(this, 2.1F));
    }

    @Unique @Nullable
    private PathfinderMob bm_caravanHead = null;

    @Unique @Nullable
    private PathfinderMob bm_caravanTail = null;

    @Override
    public boolean isCaravanning() {
        return bm_caravanHead != null;
    }

    @Override
    public boolean canJoinCaravan() {
        return !isCaravanning() && !isLeashed();
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
        return isLeashed() && !hasCaravanTail();
    }

    @Override
    public boolean canBeLeader() {
        return isLeashed();
    }

    @Override
    public <E extends PathfinderMob & Caravanner> void setTail(E tail) {
        this.bm_caravanTail = tail;
    }
}
