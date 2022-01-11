package party.lemons.biomemakeover.entity.ai;

import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.player.Player;
import party.lemons.biomemakeover.crafting.witch.WitchQuestEntity;

import java.util.EnumSet;

public class WitchStopFollowingCustomerGoal extends Goal
{
    private final Witch witch;
    private final WitchQuestEntity witchQuest;

    public WitchStopFollowingCustomerGoal(Witch witch)
    {
        this.witch = witch;
        this.witchQuest = (WitchQuestEntity) witch;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse()
    {
        if(!this.witch.isAlive())
        {
            return false;
        }else if(this.witch.isInWater())
        {
            return false;
        }else if(!this.witch.isOnGround())
        {
            return false;
        }else if(this.witch.hurtMarked)
        {
            return false;
        }else
        {
            Player playerEntity = this.witchQuest.getCurrentCustomer();
            if(playerEntity == null)
            {
                return false;
            }else if(this.witch.distanceToSqr(playerEntity) > 16.0D)
            {
                return false;
            }else
            {
                return playerEntity.containerMenu != null;
            }
        }
    }
}
