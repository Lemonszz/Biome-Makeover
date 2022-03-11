package party.lemons.biomemakeover.entity.adjudicator.phase;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.RangedBowAttackGoal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;
import party.lemons.biomemakeover.init.BMEffects;

public class BowAttackingPhase extends AttackingPhase
{
    public BowAttackingPhase(ResourceLocation id, AdjudicatorEntity adjudicator)
    {
        super(id, adjudicator);
    }

    @Override
    protected Goal getAttackGoal()
    {
        return new RangedBowAttackGoal<>(adjudicator, 0.75F, 12, 30);
    }

    @Override
    public void onEnterPhase()
    {
        super.onEnterPhase();
        adjudicator.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.BOW));
        adjudicator.playSound(BMEffects.ADJUDICATOR_GRUNT.get(), 1F, 1F);
    }

    @Override
    public void onExitPhase()
    {
        super.onExitPhase();
        adjudicator.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
    }
}