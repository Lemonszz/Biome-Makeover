package party.lemons.biomemakeover.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.Box;
import net.minecraft.util.registry.Registry;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.Random;

public class S2C_DoEntityParticleCentered implements ClientPlayNetworking.PlayChannelHandler
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
		boolean varyY = buf.readBoolean();
		float velX = buf.readFloat();
		float velY = buf.readFloat();
		float velZ = buf.readFloat();

		client.execute(()->{
			for(int i = 0; i < count; ++i)
			{
				Box bb = entity.getBoundingBox();
				double xx = bb.getCenter().x;
				double zz = bb.getCenter().z;
				double yy;
				if(varyY)
					yy = RandomUtil.randomRange(bb.minY, bb.maxY);
				else
					yy = bb.minY;

				client.world.addParticle(particleType, xx, yy, zz, velX, velY, velZ);
			}
		});
	}
}
