package party.lemons.biomemakeover.crafting.witch.menu;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import dev.architectury.networking.NetworkManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.crafting.witch.*;
import party.lemons.biomemakeover.init.BMAdvancements;
import party.lemons.biomemakeover.init.BMScreens;

public class WitchMenu extends AbstractContainerMenu {

    private final WitchQuestEntity witch;
    private final SimpleContainer container;

    public WitchMenu(int containerID, Inventory playerInventory)
    {
        this(containerID, playerInventory, new SimpleWitch(playerInventory.player));
    }

    public WitchMenu(int containerID, Inventory playerInventory, WitchQuestEntity witch)
    {
        super(BMScreens.WITCH, containerID);

        this.witch = witch;
        this.container = new SimpleContainer(1);

        this.addSlot(new Slot(this.container, 0, 131, 36)
        {
            @Override
            public boolean mayPlace(ItemStack itemStack) {
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

    @Override
    public boolean stillValid(Player player) {
        return this.witch.getCurrentCustomer() == player && witch.canInteract(player);
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack itemStack, Slot slot) {
        return false;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int i)
    {
        if(i == 0)
        {
            ItemStack stack = getSlot(0).getItem();

            if (!this.moveItemStackTo(stack, 1, 37, true)) {
                return ItemStack.EMPTY;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void removed(Player player) {
        super.removed(player);
        this.witch.setCurrentCustomer(null);
        if(!this.witch.getWitchLevel().isClientSide())
        {
            if(player.isAlive() && (!(player instanceof ServerPlayer) || !((ServerPlayer) player).hasDisconnected()))
            {
                player.getInventory().placeItemBackInInventory(container.removeItemNoUpdate(0));
            }else
            {
                ItemStack itemStack = this.container.removeItemNoUpdate(0);
                if(!itemStack.isEmpty())
                {
                    player.drop(itemStack, false);
                }
            }

        }
    }

    @Environment(EnvType.CLIENT)
    public void setQuests(WitchQuestList quests)
    {
        this.witch.setQuestsFromServer(quests);

        if(Minecraft.getInstance().screen instanceof WitchScreen)
            ((WitchScreen) Minecraft.getInstance().screen).updateQuests();
    }

    public WitchQuestList getQuests()
    {
        return this.witch.getQuests();
    }

    private void playYesSound()
    {
        if(!this.witch.getWitchLevel().isClientSide())
        {
            Entity entity = (Entity) this.witch;
            entity.playSound(witch.getYesSound(), 1F, 1F);
        }
    }

    public void completeQuest(Player playerEntity, WitchQuest quest)
    {
        if(quest.hasItems(playerEntity.getInventory()))
        {
            if(!container.getItem(0).isEmpty())
            {
                playerEntity.drop(container.getItem(0), true);
            }

            if(!playerEntity.level.isClientSide()) BMAdvancements.WITCH_TRADE.trigger((ServerPlayer) playerEntity);

            ItemStack reward = WitchQuestHandler.getRewardFor(quest, playerEntity.getRandom());
            witch.completeQuest(quest);
            playYesSound();

            quest.consumeItems(playerEntity.getInventory());
            getQuests().remove(quest);
            container.setItem(0, reward);
        }
    }
}
