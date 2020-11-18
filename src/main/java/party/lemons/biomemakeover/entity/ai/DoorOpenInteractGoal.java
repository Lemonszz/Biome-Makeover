package party.lemons.biomemakeover.entity.ai;

import net.minecraft.entity.ai.goal.LongDoorInteractGoal;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.mob.MobEntity;

public class DoorOpenInteractGoal extends LongDoorInteractGoal
{
	public DoorOpenInteractGoal(MobEntity mob, boolean delayedClose)
	{
		super(mob, delayedClose);
	}

	@Override
	public boolean canStart()
	{
		return mob.getNavigation() instanceof MobNavigation;
	}
}
