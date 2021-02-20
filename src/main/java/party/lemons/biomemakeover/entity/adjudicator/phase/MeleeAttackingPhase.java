package party.lemons.biomemakeover.entity.adjudicator.phase;

import net.minecraft.entity.ai.goal.BowAttackGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;

public class MeleeAttackingPhase extends AttackingPhase
{
	public MeleeAttackingPhase(Identifier id, AdjudicatorEntity adjudicator)
	{
		super(id, adjudicator);
	}

	@Override
	protected Goal getAttackGoal()
	{
		return new MeleeAttackGoal(adjudicator, 1F, true);
	}

	@Override
	public void onEnterPhase()
	{
		super.onEnterPhase();
		adjudicator.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.IRON_AXE));
	}

	@Override
	public void onExitPhase()
	{
		super.onExitPhase();
		adjudicator.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
	}
}
