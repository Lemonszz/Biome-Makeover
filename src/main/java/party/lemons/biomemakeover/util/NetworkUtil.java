package party.lemons.biomemakeover.util;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.network.ServerSidePacketRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import party.lemons.biomemakeover.init.BMNetwork;

public class NetworkUtil
{
	public static void serverSendToNearby(World world, Identifier packet, PacketByteBuf buf, double x, double y, double z)
	{
		if(world.isClient) return;

		ServerWorld sw = (ServerWorld) world;

		for(ServerPlayerEntity pl : sw.getPlayers())
		{
			if(pl.getServerWorld() != sw) return;

			BlockPos pos = pl.getBlockPos();
			if(pos.isWithinDistance(new Vec3d(x, y, z), 32))
			{
				ServerSidePacketRegistry.INSTANCE.sendToPlayer(pl, packet, buf);
			}
		}
	}

	public static void doLightningSplash(World world, boolean doBottle, BlockPos pos)
	{
		if(world.isClient) return;

		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeBoolean(doBottle);
		buf.writeBlockPos(pos);

		serverSendTracking(world, pos, BMNetwork.SPAWN_LIGHTNING_BOTTLE_PARTICLES, buf);
	}

	public static void doLightningEntity(World world, LivingEntity entity, int count)
	{
		if(world.isClient) return;

		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(entity.getEntityId());
		buf.writeInt(count);

		serverSendTracking(world, entity.getBlockPos(), BMNetwork.SPAWN_LIGHTNING_ENTITY_PARTICLES, buf);
	}

	public static void doEntityParticle(World world, ParticleEffect effect, Entity e, int count, float offset)
	{
		if(world.isClient) return;

		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeInt(e.getEntityId());
		buf.writeInt(Registry.PARTICLE_TYPE.getRawId((ParticleType<?>)effect));
		buf.writeInt(count);
		buf.writeFloat(offset);

		buf.writeDouble(world.random.nextGaussian() * 0.02D);
		buf.writeDouble(world.random.nextGaussian() * 0.02D);
		buf.writeDouble(world.random.nextGaussian() * 0.02D);
		serverSendTracking(world, e.getBlockPos(), BMNetwork.ENTITY_PARTICLE, buf);
	}

	public static void serverSendTracking(World world, BlockPos blockPos, Identifier id, PacketByteBuf buf)
	{
		if(world.isClient()) return;

		for(ServerPlayerEntity pl : PlayerLookup.tracking((ServerWorld) world, blockPos))
		{
			ServerPlayNetworking.send(pl, id, buf);
		}
	}

	public static void sendSlideTime(PlayerEntity player, int time)
	{
		if(player.world.isClient()) return;

		PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
		buf.writeVarInt(time);
		ServerPlayNetworking.send((ServerPlayerEntity) player, BMNetwork.SET_SLIDE_TIME, buf);
	}
}
