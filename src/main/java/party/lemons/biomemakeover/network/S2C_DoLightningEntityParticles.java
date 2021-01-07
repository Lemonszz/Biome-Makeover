package party.lemons.biomemakeover.network;

import net.fabricmc.fabric.api.network.PacketConsumer;
import net.fabricmc.fabric.api.network.PacketContext;
import net.minecraft.client.particle.Particle;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
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

public class S2C_DoLightningEntityParticles implements PacketConsumer
{
	@Override
	public void accept(PacketContext ctx, PacketByteBuf buf)
	{
		int entityID = buf.readInt();
		int count = buf.readInt();

		ctx.getTaskQueue().execute(()->
		{
			Entity e = ctx.getPlayer().world.getEntityById(entityID);
			if(e == null || !(e instanceof LivingEntity))
				return;
			Random random = ((LivingEntity) e).getRandom();
			Vec3d entityPos = e.getPos();
			ParticleEffect particleEffect = BMEffects.LIGHTNING_SPARK;

			for(int i = 0; i < count; ++i) {
				double speed = random.nextDouble() * 1.0D;
				double ac = random.nextDouble() * Math.PI * 2.0D;
				double xVel = ((Math.cos(ac) * speed) * 0.1D) / 10F;
				double yVel = 0.01D + random.nextDouble() * 0.5D;
				double zVel = ((Math.sin(ac) * speed) * 0.1D) / 10F;

				Particle particle = ClientUtil.spawnParticle(particleEffect, particleEffect.getType().shouldAlwaysSpawn(), true,entityPos.x + xVel * 0.01D, entityPos.y + 0.3D, entityPos.z + zVel * 0.01D, xVel, yVel, zVel);
				if (particle != null) {
					particle.move((float)speed);
				}
			}
		});
	}
}
