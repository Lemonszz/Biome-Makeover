package party.lemons.biomemakeover.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.Box;
import net.minecraft.util.registry.Registry;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.Random;

public class S2C_DoEntityParticle implements ClientPlayNetworking.PlayChannelHandler
{
	@Override
	public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender)
	{
		int entityID = buf.readInt();
		Entity entity = client.world.getEntityById(entityID);
		if(entity == null)
			return;

		int particleID = buf.readInt();
		ParticleEffect particleType = (ParticleEffect) Registry.PARTICLE_TYPE.get(particleID);
		if(!(particleType instanceof ParticleEffect))
			return;

		int count = buf.readInt();
		float offset = buf.readFloat();
		float velX = buf.readFloat();
		float velY = buf.readFloat();
		float velZ = buf.readFloat();

		client.execute(()->{
			Random random = client.world.random;

			for(int i = 0; i < count; ++i)
			{
				Box bb = entity.getBoundingBox();
				double xx = RandomUtil.randomRange(bb.minX - offset, bb.maxX + offset);
				double yy = RandomUtil.randomRange(bb.minY - offset, bb.maxY + offset);
				double zz = RandomUtil.randomRange(bb.minZ - offset, bb.maxZ + offset);
				client.world.addParticle(particleType, xx, yy, zz, velX, velY, velZ);
			}
		});
	}
}
