package party.lemons.biomemakeover.mixin.client;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.s2c.play.MobSpawnS2CPacket;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import party.lemons.biomemakeover.entity.MultiPartEntity;
import party.lemons.biomemakeover.util.TotemItem;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin
{

	/**
	 *  Handle Multipart entity spawning
	 */
	@Inject(
			at = @At(value = "INVOKE", target = "net/minecraft/entity/LivingEntity.updateTrackedPosition(DDD)V"),
			method = "onMobSpawn",
			locals = LocalCapture.CAPTURE_FAILSOFT
	)
	private void onMobSpawn(MobSpawnS2CPacket packet, CallbackInfo cbi, double d, double e, double f, float g, float h, LivingEntity livingEntity)
	{
		if(livingEntity instanceof MultiPartEntity)
			MultiPartEntity.handleClientSpawn(packet, livingEntity);
	}

	@Inject(at = @At("HEAD"), method = "getActiveTotemOfUndying", cancellable = true)
	private static void getActiveTotemOfUndying(PlayerEntity playerEntity, CallbackInfoReturnable<ItemStack> cbi)
	{
		Hand[] var1 = Hand.values();
		int var2 = var1.length;

		for(int var3 = 0; var3 < var2; ++var3) {
			Hand hand = var1[var3];
			ItemStack itemStack = playerEntity.getStackInHand(hand);
			if (itemStack.getItem() instanceof TotemItem) {
				cbi.setReturnValue(itemStack);
			}
		}
	}
}
