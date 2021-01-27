package party.lemons.biomemakeover.util;

import io.netty.buffer.Unpooled;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.CustomPayloadS2CPacket;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.registry.Registry;
import party.lemons.biomemakeover.init.BMNetwork;

public final class EntityUtil
{
	public static Packet<?> createCustomSpawnPacket(Entity e)
	{
		return new CustomPayloadS2CPacket(BMNetwork.SPAWN_ENTITY, WriteEntitySpawn(e));
	}

	public static PacketByteBuf WriteEntitySpawn(Entity entity)
	{
		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeVarInt(Registry.ENTITY_TYPE.getRawId(entity.getType()));
		buf.writeUuid(entity.getUuid());
		buf.writeVarInt(entity.getEntityId());
		buf.writeDouble(entity.getX());
		buf.writeDouble(entity.getY());
		buf.writeDouble(entity.getZ());
		buf.writeFloat(entity.pitch);
		buf.writeFloat(entity.yaw);

		return buf;
	}

	public static void scatterItemStack(Entity e, ItemStack stack)
	{
		ItemScatterer.spawn(e.world, e.getX(), e.getY(), e.getZ(), stack);
	}

	private EntityUtil()
	{ }
}
