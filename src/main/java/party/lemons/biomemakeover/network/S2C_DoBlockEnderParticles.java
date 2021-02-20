package party.lemons.biomemakeover.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.Random;

public class S2C_DoBlockEnderParticles implements ClientPlayNetworking.PlayChannelHandler
{
	@Override
	public void receive(MinecraftClient cl, ClientPlayNetworkHandler clientPlayNetworkHandler, PacketByteBuf buf, PacketSender packetSender)
	{
		BlockPos pos = buf.readBlockPos();
		int count = buf.readInt();
		Random random = RandomUtil.RANDOM;

		cl.execute(()->{
			for(int i = 0; i < count; ++i) {
				int xOffset = random.nextInt(2) * 2 - 1;
				int zOffset = random.nextInt(2) * 2 - 1;
				double x = (double)pos.getX() + 0.5D + 0.25D * (double)xOffset;
				double y = (float)pos.getY() + random.nextFloat();
				double z = (double)pos.getZ() + 0.5D + 0.25D * (double)zOffset;
				double vX = random.nextFloat() * (float)xOffset;
				double vY = ((double)random.nextFloat() - 0.5D) * 0.125D;
				double vZ = random.nextFloat() * (float)zOffset;
				cl.world.addParticle(ParticleTypes.PORTAL, x, y, z, vX, vY, vZ);
			}
		});


	}
}
