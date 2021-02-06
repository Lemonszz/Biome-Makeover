package party.lemons.biomemakeover.crafting.witch;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;

public class SimpleWitch implements WitchQuestEntity
{
	private final PlayerEntity player;
	private WitchQuestList quests = new WitchQuestList();

	public SimpleWitch(PlayerEntity playerEntity)
	{
		this.player = playerEntity;
	}


	@Override
	public void setCurrentCustomer(PlayerEntity customer)
	{

	}

	@Override
	public boolean hasCustomer()
	{
		return player != null;
	}

	@Override
	public PlayerEntity getCurrentCustomer()
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
		return SoundEvents.ENTITY_WITCH_CELEBRATE;
	}

	@Override
	public boolean canInteract(PlayerEntity playerEntity)
	{
		return true;
	}

	@Override
	public World getWitchWorld()
	{
		return player.world;
	}
}
