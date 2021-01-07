package party.lemons.biomemakeover.entity.ai;

import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.entity.player.PlayerEntity;
import party.lemons.biomemakeover.crafting.witch.WitchQuestEntity;

public class WitchLookAtCustomerGoal extends LookAtEntityGoal
{
	private final WitchQuestEntity witch;

	public WitchLookAtCustomerGoal(WitchEntity witch) {
		super(witch, PlayerEntity.class, 8.0F);
		this.witch = (WitchQuestEntity) witch;
	}

	public boolean canStart() {
		if (this.witch.hasCustomer()) {
			this.target = this.witch.getCurrentCustomer();
			return true;
		} else {
			return false;
		}
	}
}
