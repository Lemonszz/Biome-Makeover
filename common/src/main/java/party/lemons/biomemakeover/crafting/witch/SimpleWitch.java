package party.lemons.biomemakeover.crafting.witch;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class SimpleWitch implements WitchQuestEntity
{
    private final Player player;
    private WitchQuestList quests = new WitchQuestList();

    public SimpleWitch(Player playerEntity)
    {
        this.player = playerEntity;
    }


    @Override
    public void setCurrentCustomer(Player customer)
    {

    }

    @Override
    public boolean hasCustomer()
    {
        return player != null;
    }

    @Override
    public Player getCurrentCustomer()
    {
        return player;
    }

    @Override
    public WitchQuestList getQuests()
    {
        return quests;
    }

    @Override
    public void setQuestsFromServer(WitchQuestList quests)
    {
        this.quests = quests;
    }

    @Override
    public void completeQuest(WitchQuest quest)
    {

    }

    @Override
    public SoundEvent getYesSound()
    {
        return SoundEvents.WITCH_CELEBRATE;
    }

    @Override
    public boolean canInteract(Player playerEntity)
    {
        return true;
    }

    @Override
    public Level getWitchLevel() {
        return player.level;
    }
}