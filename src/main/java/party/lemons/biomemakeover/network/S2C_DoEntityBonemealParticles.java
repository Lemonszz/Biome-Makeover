package party.lemons.biomemakeover.network;


import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;

import java.util.Random;

public class S2C_DoEntityBonemealParticles implements ClientPlayNetworking.PlayChannelHandler
{
	@Override
	public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender)
	{
		double x = buf.readDouble();
		double y = buf.readDouble();
		double z = buf.readDouble();

		client.execute(()->
		{

			client.world.addParticle(ParticleTypes.HAPPY_VILLAGER, x, y, z, 0.0D, 0.0D, 0.0D);
			Random random = client.world.random;

			for(int i = 0; i < 15; ++i)
			{
				double vX = random.nextGaussian() * 0.02D;
				double vY = random.nextGaussian() * 0.02D;
				double vZ = random.nextGaussian() * 0.02D;
				double xx = -0.5 + x + random.nextDouble();
				double yy = y + random.nextDouble();
				double zz = -0.5 + z + random.nextDouble();
				client.world.addParticle(ParticleTypes.HAPPY_VILLAGER, xx, yy, zz, vX, vY, vZ);
			}
		});
	}
}
