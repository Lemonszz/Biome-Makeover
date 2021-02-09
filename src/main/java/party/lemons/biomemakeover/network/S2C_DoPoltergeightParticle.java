package party.lemons.biomemakeover.network;


import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.Random;

public class S2C_DoPoltergeightParticle implements ClientPlayNetworking.PlayChannelHandler
{
	@Override
	public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf data, PacketSender responseSender)
	{
		int x = data.readInt();
		int y = data.readInt();
		int z = data.readInt();

		client.execute(()->
		{
			ClientWorld world = MinecraftClient.getInstance().world;
			if(world == null) return;

			Random random = world.random;

			for(int i = 0; i < 2; i++)
			{
				world.addParticle(BMEffects.POLTERGEIST, x + random.nextDouble(), y + random.nextDouble(), z + random.nextDouble(), (random.nextDouble() / 20D) * RandomUtil.randomDirection(1F), 0.025F, (random.nextDouble() / 20D) * RandomUtil.randomDirection(1F));
			}
		});
	}
}