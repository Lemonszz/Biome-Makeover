package party.lemons.biomemakeover.crafting.witch;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import party.lemons.biomemakeover.crafting.witch.screen.WitchScreenHandler;

import java.util.OptionalInt;

public interface WitchQuestEntity
{
	void setCurrentCustomer(PlayerEntity customer);

	boolean hasCustomer();

	PlayerEntity getCurrentCustomer();

	WitchQuestList getQuests();

	void setQuestsFromServer(WitchQuestList quests);

	void completeQuest(WitchQuest quest);

	SoundEvent getYesSound();

	boolean canInteract(PlayerEntity playerEntity);

	World getWitchWorld();

	default void sendQuests(PlayerEntity player, Text text)
	{
		OptionalInt optionalInt = player.openHandledScreen(new SimpleNamedScreenHandlerFactory((ix, playerInventory, playerEntityx)->new WitchScreenHandler(ix, playerInventory, this), text));
		if(optionalInt.isPresent())
		{
			WitchQuestList quests = this.getQuests();
			if(!quests.isEmpty())
			{
				WitchQuestHandler.sendQuests(player, optionalInt.getAsInt(), quests);
			}
		}

	}
}
