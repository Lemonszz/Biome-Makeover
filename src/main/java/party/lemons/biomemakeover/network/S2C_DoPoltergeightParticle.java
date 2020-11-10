package party.lemons.biomemakeover.network;


import net.fabricmc.fabric.api.network.PacketConsumer;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.registry.Registry;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.Random;
import java.util.UUID;

public class S2C_DoPoltergeightParticle implements PacketConsumer
{
	@Override
	public void accept(PacketContext ctx, PacketByteBuf data)
	{
		int x = data.readInt();
		int y = data.readInt();
		int z = data.readInt();

		ctx.getTaskQueue().execute(() ->
		{
			ClientWorld world = MinecraftClient.getInstance().world;
			if(world == null)
				return;

			Random random = world.random;

			for(int i = 0; i < 2; i++)
			{
				world.addParticle(ParticleTypes.SOUL, x + random.nextDouble() - 0.5D, y + random.nextDouble(), z + random.nextDouble(), (random.nextDouble() / 20D) * RandomUtil.randomDirection(1F), 0.025F, (random.nextDouble() / 20D) * RandomUtil.randomDirection(1F));
			}
		});
	}
}