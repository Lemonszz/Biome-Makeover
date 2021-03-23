package party.lemons.biomemakeover.util.extensions;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.mob.MobEntity;
import party.lemons.biomemakeover.util.access.MobEntityAccess;

import java.util.Set;

public interface GoalSelectorExtension
{
	void copy(GoalSelector selector);
	Set<PrioritizedGoal> getGoals();
	void remove(Class<? extends Goal> goalClass);

	static void copy(GoalSelector to, GoalSelector from)
	{
		((GoalSelectorExtension)to).copy(from);
	}

	static void removeGoal(MobEntity mob, Class<? extends Goal> goalClass)
	{
		removeFrom(((MobEntityAccess)mob).getGoalSelector(), goalClass);
	}

	static void removeTarget(MobEntity mob, Class<? extends Goal> goalClass)
	{
		removeFrom(((MobEntityAccess)mob).getTargetSelector(), goalClass);
	}

	static void removeFrom(GoalSelector goalSelector, Class<? extends Goal> goalClass)
	{
		((GoalSelectorExtension)goalSelector).remove(goalClass);
	}
}
