package party.lemons.biomemakeover.crafting.witch.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import party.lemons.biomemakeover.crafting.witch.*;
import party.lemons.biomemakeover.init.BMCriterion;
import party.lemons.biomemakeover.init.BMScreens;

public class WitchScreenHandler extends ScreenHandler
{
	private final WitchQuestEntity witch;
	private final SimpleInventory inventory;

	public WitchScreenHandler(int syncId, PlayerInventory playerInventory)
	{
		this(syncId, playerInventory, new SimpleWitch(playerInventory.player));
	}

	public WitchScreenHandler(int syncId, PlayerInventory playerInventory, WitchQuestEntity witch)
	{
		super(BMScreens.WITCH, syncId);
		this.witch = witch;
		this.inventory = new SimpleInventory(1);
		this.addSlot(new Slot(this.inventory, 0, 131, 36){
			@Override
			public boolean canInsert(ItemStack stack)
			{
				return false;
			}
		});

		int k;
		for(k = 0; k < 3; ++k)
		{
			for(int j = 0; j < 9; ++j)
			{
				this.addSlot(new Slot(playerInventory, j + k * 9 + 9, 6 + j * 18, 100 + k * 18));
			}
		}

		for(k = 0; k < 9; ++k)
		{
			this.addSlot(new Slot(playerInventory, k, 6 + k * 18, 158));
		}

	}

	public void onContentChanged(Inventory inventory)
	{
		super.onContentChanged(inventory);
	}

	public boolean canUse(PlayerEntity player)
	{
		return this.witch.getCurrentCustomer() == player && witch.canInteract(player);
	}

	public boolean canInsertIntoSlot(ItemStack stack, Slot slot)
	{
		return false;
	}

	public ItemStack transferSlot(PlayerEntity player, int index)
	{
		ItemStack itemStack = ItemStack.EMPTY;
		return itemStack;
	}

	private void playYesSound()
	{
		if(!this.witch.getWitchWorld().isClient)
		{
			Entity entity = (Entity) this.witch;
			entity.playSound(witch.getYesSound(), 1F, 1F);
		}

	}

	public void close(PlayerEntity player)
	{
		super.close(player);
		this.witch.setCurrentCustomer(null);
		if(!this.witch.getWitchWorld().isClient)
		{
			if(player.isAlive() && (!(player instanceof ServerPlayerEntity) || !((ServerPlayerEntity) player).isDisconnected()))
			{
				player.inventory.offerOrDrop(player.world, this.inventory.removeStack(0));
			}
			else
			{
				ItemStack itemStack = this.inventory.removeStack(0);
				if(!itemStack.isEmpty())
				{
					player.dropItem(itemStack, false);
				}
			}

		}
	}

	@Environment(EnvType.CLIENT)
	public void setQuests(WitchQuestList quests)
	{
		this.witch.setQuestsFromServer(quests);

		if(MinecraftClient.getInstance().currentScreen instanceof WitchScreen)
			((WitchScreen) MinecraftClient.getInstance().currentScreen).updateQuests();
	}

	public WitchQuestList getQuests()
	{
		return this.witch.getQuests();
	}

	public void completeQuest(PlayerEntity playerEntity, WitchQuest quest)
	{
		if(quest.hasItems(playerEntity.inventory))
		{
			if(!inventory.getStack(0).isEmpty())
			{
				playerEntity.dropItem(inventory.getStack(0), true);
			}
			if(!playerEntity.world.isClient())
				BMCriterion.WITCH_TRADE.trigger((ServerPlayerEntity) playerEntity);

			ItemStack reward = WitchQuestHandler.getRewardFor(quest, playerEntity.getRandom());
			witch.completeQuest(quest);
			playYesSound();

			quest.consumeItems(playerEntity.inventory);
			getQuests().remove(quest);
			inventory.setStack(0, reward);
		}
	}
}