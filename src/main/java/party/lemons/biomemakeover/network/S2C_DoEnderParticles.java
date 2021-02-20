package party.lemons.biomemakeover.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;

public class S2C_DoEnderParticles implements ClientPlayNetworking.PlayChannelHandler
{
	@Override
	public void receive(MinecraftClient client, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf buf, PacketSender packetSender)
	{
		int entityID = buf.readInt();
		Entity entity = client.world.getEntityById(entityID);
		if(entity == null)
			return;

		int count = buf.readInt();

		client.execute(()->{
			for(int i = 0; i < count; i++)
			{
				client.world.addParticle(ParticleTypes.PORTAL, entity.getParticleX(0.5D), entity.getRandomBodyY() - 0.25D, entity.getParticleZ(0.5D), (client.world.random.nextDouble() - 0.5D) * 2.0D, -client.world.random.nextDouble(), (client.world.random.nextDouble() - 0.5D) * 2.0D);
			}
		});
	}
}
