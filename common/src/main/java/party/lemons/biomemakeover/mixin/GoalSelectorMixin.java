package party.lemons.biomemakeover.mixin;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import party.lemons.biomemakeover.util.extension.GoalSelectorExtension;

import java.util.Set;

@Mixin(GoalSelector.class)
public abstract class GoalSelectorMixin implements GoalSelectorExtension {

    @Shadow
    @Final
    private Set<WrappedGoal> availableGoals;

    @Shadow public abstract void addGoal(int priority, Goal goal);

    @Override
    public void bm_copy(GoalSelector from) {
        availableGoals.removeIf((g)->true);

        for(WrappedGoal goal : ((GoalSelectorExtension)from).bm_getGoals())
            addGoal(goal.getPriority(), goal.getGoal());
    }

    @Override
    public Set<WrappedGoal> bm_getGoals() {
        return availableGoals;
    }

    @Override
    public void bm_remove(Class<? extends Goal> goalClass) {
        availableGoals.removeIf(g->g.getGoal().getClass().equals(goalClass));
    }
}
