package party.lemons.biomemakeover.util.extensions;

import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.PrioritizedGoal;

import java.util.Set;

public interface GoalSelectorExtension
{
	void copy(GoalSelector selector);
	Set<PrioritizedGoal> getGoals();

	static void copy(GoalSelector to, GoalSelector from)
	{
		((GoalSelectorExtension)to).copy(from);
	}
}
