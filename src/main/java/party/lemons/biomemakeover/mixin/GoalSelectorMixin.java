package party.lemons.biomemakeover.mixin;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import party.lemons.biomemakeover.util.extensions.GoalSelectorExtension;

import java.util.Set;

@Mixin(GoalSelector.class)
public abstract class GoalSelectorMixin implements GoalSelectorExtension
{
	@Shadow @Final private Set<PrioritizedGoal> goals;

	@Shadow public abstract void remove(Goal goal);

	@Shadow public abstract void add(int priority, Goal goal);

	@Override
	public void bm_remove(Class<? extends Goal> goalClass)
	{
		goals.removeIf(g->g.getGoal().getClass().equals(goalClass));
	}

	@Override
	public void bm_copy(GoalSelector from)
	{
		goals.removeIf((g)->true);

		for(PrioritizedGoal goal : ((GoalSelectorExtension)from).bm_getGoals())
			add(goal.getPriority(), goal.getGoal());
	}

	@Override
	public Set<PrioritizedGoal> bm_getGoals()
	{
		return goals;
	}
}
