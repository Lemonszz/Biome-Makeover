package party.lemons.biomemakeover.util.access;

import net.minecraft.entity.ai.goal.GoalSelector;

public interface MobEntityAccess
{
	GoalSelector bm_getGoalSelector();
	GoalSelector bm_getTargetSelector();
}
