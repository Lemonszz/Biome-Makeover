package party.lemons.biomemakeover.mixin.client;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.s2c.play.MobSpawnS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import party.lemons.biomemakeover.entity.MultiPartEntity;

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
}
