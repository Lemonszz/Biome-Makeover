package party.lemons.biomemakeover.network;


import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.registry.Registry;

import java.util.UUID;

public class S2C_SpawnEntityCustom implements ClientPlayNetworking.PlayChannelHandler
{
	@Override
	public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf data, PacketSender responseSender)
	{
		EntityType<?> type = Registry.ENTITY_TYPE.get(data.readVarInt());
		UUID entityUUID = data.readUuid();
		int entityID = data.readVarInt();
		double x = data.readDouble();
		double y = data.readDouble();
		double z = data.readDouble();
		float pitch = data.readFloat();
		float yaw = data.readFloat();

		client.execute(()->
		{
			ClientWorld world = MinecraftClient.getInstance().world;
			Entity entity = type.create(world);
			if(entity != null)
			{
				entity.updatePosition(x, y, z);
				entity.updateTrackedPosition(x, y, z);
				entity.pitch = pitch;
				entity.yaw = yaw;

				entity.setEntityId(entityID);
				entity.setUuid(entityUUID);
				world.addEntity(entityID, entity);
			}
		});
	}
}