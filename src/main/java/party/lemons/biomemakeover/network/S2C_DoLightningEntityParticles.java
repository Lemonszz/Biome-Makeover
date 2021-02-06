package party.lemons.biomemakeover.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.util.math.Vec3d;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.util.ClientUtil;

import java.util.Random;

public class S2C_DoLightningEntityParticles implements ClientPlayNetworking.PlayChannelHandler
{

	@Override
	public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender)
	{
		int entityID = buf.readInt();
		int count = buf.readInt();

		client.execute(()->
		{
			Entity e = client.world.getEntityById(entityID);
			if(e == null || !(e instanceof LivingEntity)) return;
			Random random = ((LivingEntity) e).getRandom();
			Vec3d entityPos = e.getPos();
			ParticleEffect particleEffect = BMEffects.LIGHTNING_SPARK;

			for(int i = 0; i < count; ++i)
			{
				double speed = random.nextDouble() * 1.0D;
				double ac = random.nextDouble() * Math.PI * 2.0D;
				double xVel = ((Math.cos(ac) * speed) * 0.1D) / 10F;
				double yVel = 0.01D + random.nextDouble() * 0.5D;
				double zVel = ((Math.sin(ac) * speed) * 0.1D) / 10F;

				Particle particle = ClientUtil.spawnParticle(particleEffect, particleEffect.getType().shouldAlwaysSpawn(), true, entityPos.x + xVel * 0.01D, entityPos.y + 0.3D, entityPos.z + zVel * 0.01D, xVel, yVel, zVel);
				if(particle != null)
				{
					particle.move((float) speed);
				}
			}
		});
	}
}
