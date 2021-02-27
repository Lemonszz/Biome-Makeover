package party.lemons.biomemakeover.entity.adjudicator.phase;

import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorState;
import party.lemons.biomemakeover.entity.adjudicator.ai.AdjudicatorFangGoal;

public class FangAttackingPhase extends AttackingPhase
{
	public FangAttackingPhase(Identifier id, AdjudicatorEntity adjudicator)
	{
		super(id, adjudicator);
	}

	@Override
	public void onEnterPhase()
	{
		super.onEnterPhase();
		adjudicator.setState(AdjudicatorState.SUMMONING);
	}

	@Override
	protected Goal getAttackGoal()
	{
		return new AdjudicatorFangGoal(adjudicator);
	}
}
