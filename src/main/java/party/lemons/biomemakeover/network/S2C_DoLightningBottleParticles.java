package party.lemons.biomemakeover.network;

import net.fabricmc.fabric.api.network.PacketConsumer;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.ParticlesMode;
import net.minecraft.client.particle.Particle;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.init.BMItems;
import party.lemons.biomemakeover.util.ClientUtil;

import java.util.Random;

public class S2C_DoLightningBottleParticles implements PacketConsumer
{
	@Override
	public void accept(PacketContext ctx, PacketByteBuf buf)
	{
		final boolean doBottleBreak = buf.readBoolean();
		BlockPos pos = buf.readBlockPos();

		ctx.getTaskQueue().execute(()->{
			Vec3d dir = Vec3d.ofBottomCenter(pos);
			Random random = ctx.getPlayer().getRandom();
			World world = ctx.getPlayer().world;

			if(doBottleBreak)
			{
				for(int i = 0; i < 8; ++i)
				{
					world.addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, new ItemStack(BMItems.LIGHTNING_BOTTLE)), dir.x, dir.y, dir.z, random.nextGaussian() * 0.15D, random.nextDouble() * 0.2D, random.nextGaussian() * 0.15D);
				}
				world.playSound(null, pos, SoundEvents.ENTITY_SPLASH_POTION_BREAK, SoundCategory.NEUTRAL, 1.0F, random.nextFloat() * 0.1F + 0.9F);
			}

			ParticleEffect particleEffect = BMEffects.LIGHTNING_SPARK;
			ParticlesMode mode = MinecraftClient.getInstance().options.particles;
			int particleCount = mode == ParticlesMode.ALL ? 100 : mode == ParticlesMode.DECREASED ? 50 : 10;

			for(int i = 0; i < particleCount; ++i) {
				double direction = random.nextDouble() * 4.0D;
				double ac = random.nextDouble() * Math.PI * 2.0D;
				double xVel = (Math.cos(ac) * direction) * 0.1D;
				double yVel = 0.01D + random.nextDouble() * 0.5D;
				double zVel = (Math.sin(ac) * direction) * 0.1D;

				Particle particle = ClientUtil.spawnParticle(particleEffect, particleEffect.getType().shouldAlwaysSpawn(), true,dir.x + xVel * 0.01D, dir.y + 0.3D, dir.z + zVel * 0.01D, xVel, yVel, zVel);
				if (particle != null) {
					particle.move((float)direction);
				}
			}
		});
	}
}
