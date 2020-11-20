package party.lemons.biomemakeover.mixin;

import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.init.BMCriterion;

@Mixin(InventoryChangedCriterion.class)
public class InventoryChangedCriterionMixin
{
	@Inject(at = @At("HEAD"), method = "trigger(Lnet/minecraft/server/network/ServerPlayerEntity;Lnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/item/ItemStack;)V")
	public void trigger(ServerPlayerEntity player, PlayerInventory inventory, ItemStack stack, CallbackInfo cbi)
	{
		BMCriterion.WEAR_ARMOUR.trigger(player);
	}
}
