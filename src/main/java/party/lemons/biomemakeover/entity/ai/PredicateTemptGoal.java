package party.lemons.biomemakeover.entity.ai;

import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;

import java.util.function.Predicate;

public class PredicateTemptGoal extends TemptGoal
{
	private Predicate<ItemStack> food;

	public PredicateTemptGoal(PathAwareEntity mob, double speed, Predicate<ItemStack> food, boolean canBeScared)
	{
		super(mob, speed, null, canBeScared);
		this.food = food;
	}

	public PredicateTemptGoal(PathAwareEntity mob, double speed, boolean canBeScared, Predicate<ItemStack> food)
	{
		super(mob, speed, canBeScared, null);
		this.food = food;
	}


	@Override
	protected boolean isTemptedBy(ItemStack stack)
	{
		return food.test(stack);
	}
}
