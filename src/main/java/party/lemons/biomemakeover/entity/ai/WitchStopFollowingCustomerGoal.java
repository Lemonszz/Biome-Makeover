package party.lemons.biomemakeover.entity.ai;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.player.PlayerEntity;
import party.lemons.biomemakeover.crafting.witch.WitchQuestEntity;

import java.util.EnumSet;

public class WitchStopFollowingCustomerGoal extends Goal
{
	private final WitchEntity witch;
	private final WitchQuestEntity witchQuest;

	public WitchStopFollowingCustomerGoal(WitchEntity witch)
	{
		this.witch = witch;
		this.witchQuest = (WitchQuestEntity) witch;
		this.setControls(EnumSet.of(Goal.Control.JUMP, Goal.Control.MOVE));
	}

	public boolean canStart()
	{
		if(!this.witch.isAlive())
		{
			return false;
		}else if(this.witch.isTouchingWater())
		{
			return false;
		}else if(!this.witch.isOnGround())
		{
			return false;
		}else if(this.witch.velocityModified)
		{
			return false;
		}else
		{
			PlayerEntity playerEntity = this.witchQuest.getCurrentCustomer();
			if(playerEntity == null)
			{
				return false;
			}else if(this.witch.squaredDistanceTo(playerEntity) > 16.0D)
			{
				return false;
			}else
			{
				return playerEntity.currentScreenHandler != null;
			}
		}
	}

	public void start()
	{
		this.witch.getNavigation().stop();
	}

	public void stop()
	{
		this.witchQuest.setCurrentCustomer(null);
	}
}