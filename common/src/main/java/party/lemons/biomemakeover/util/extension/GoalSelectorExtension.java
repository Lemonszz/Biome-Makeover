package party.lemons.biomemakeover.util.extension;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import party.lemons.biomemakeover.mixin.MobAccess;

import java.util.Set;

public interface GoalSelectorExtension
{
    void bm_copy(GoalSelector selector);
    Set<WrappedGoal> bm_getGoals();
    void bm_remove(Class<? extends Goal> goalClass);

    static void copy(GoalSelector to, GoalSelector from)
    {
        ((GoalSelectorExtension)to).bm_copy(from);
    }

    static void removeGoal(LivingEntity mob, Class<? extends Goal> goalClass)
    {
        removeFrom(((MobAccess)mob).getGoalSelector(), goalClass);
    }

    static void removeTarget(LivingEntity mob, Class<? extends Goal> goalClass)
    {
        removeFrom(((MobAccess)mob).getTargetSelector(), goalClass);
    }

    static void removeFrom(GoalSelector goalSelector, Class<? extends Goal> goalClass)
    {
        ((GoalSelectorExtension)goalSelector).bm_remove(goalClass);
    }
}