package party.lemons.biomemakeover.crafting.witch;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;

public interface WitchQuestEntity
{
	void setCurrentCustomer(PlayerEntity customer);
	PlayerEntity getCurrentCustomer();
	WitchQuestList getQuests();
	void setQuestsFromServer(WitchQuestList quests);
	void completeQuest(WitchQuest quest);
	SoundEvent getYesSound();
	boolean canInteract(PlayerEntity playerEntity);
}
