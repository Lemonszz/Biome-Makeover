package party.lemons.biomemakeover.entity.ai;


import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.mob.SpellcastingIllagerEntity;

import java.util.EnumSet;

public class LookAtTargetGoal extends Goal
{
	private PathAwareEntity entity;

	public LookAtTargetGoal(PathAwareEntity entity)
	{
		this.setControls(EnumSet.of(Goal.Control.LOOK));
		this.entity = entity;
	}

	public boolean canStart()
	{
		return entity.getTarget() != null;
	}

	public void start()
	{
		super.start();
	}

	public void stop()
	{
		super.stop();
	}

	public void tick()
	{
		if(entity.getTarget() != null)
			entity.getLookControl().lookAt(entity.getTarget(), (float)entity.getBodyYawSpeed(), (float)entity.getLookPitchSpeed());
	}
}
