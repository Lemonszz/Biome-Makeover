package party.lemons.biomemakeover.entity.ai;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.player.Player;
import party.lemons.biomemakeover.crafting.witch.WitchQuestEntity;

public class WitchLookAtCustomerGoal extends LookAtPlayerGoal
{
    private final WitchQuestEntity witch;

    public WitchLookAtCustomerGoal(Witch witch)
    {
        super(witch, Player.class, 8.0F);
        this.witch = (WitchQuestEntity) witch;
    }

    @Override
    public boolean canUse() {
        if(this.witch.hasCustomer())
        {
            this.lookAt = this.witch.getCurrentCustomer();
            return true;
        }else
        {
            return false;
        }
    }
}