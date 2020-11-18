package party.lemons.biomemakeover.network;


import net.fabricmc.fabric.api.network.PacketConsumer;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.registry.Registry;

import java.util.UUID;

public class S2C_SpawnEntityCustom implements PacketConsumer
{
	@Override
	public void accept(PacketContext ctx, PacketByteBuf data)
	{
		EntityType<?> type = Registry.ENTITY_TYPE.get(data.readVarInt());
		UUID entityUUID = data.readUuid();
		int entityID = data.readVarInt();
		double x = data.readDouble();
		double y = data.readDouble();
		double z = data.readDouble();
		float pitch = data.readFloat();
		float yaw = data.readFloat();

		ctx.getTaskQueue().execute(() -> {
			ClientWorld world = MinecraftClient.getInstance().world;
			Entity entity = type.create(world);
			if(entity != null) {
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