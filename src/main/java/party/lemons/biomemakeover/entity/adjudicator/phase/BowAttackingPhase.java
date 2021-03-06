package party.lemons.biomemakeover.entity.adjudicator.phase;

import net.minecraft.entity.ai.goal.BowAttackGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;
import party.lemons.biomemakeover.init.BMEffects;

public class BowAttackingPhase extends AttackingPhase
{
	public BowAttackingPhase(Identifier id, AdjudicatorEntity adjudicator)
	{
		super(id, adjudicator);
	}

	@Override
	protected Goal getAttackGoal()
	{
		return new BowAttackGoal<>(adjudicator, 0.75F, 12, 30);
	}

	@Override
	public void onEnterPhase()
	{
		super.onEnterPhase();
		adjudicator.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.BOW));
		adjudicator.playSound(BMEffects.ADJUDICATOR_GRUNT, 1F, 1F);
	}

	@Override
	public void onExitPhase()
	{
		super.onExitPhase();
		adjudicator.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
	}
}
