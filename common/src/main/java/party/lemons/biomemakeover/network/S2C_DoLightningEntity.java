package party.lemons.biomemakeover.network;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.init.BMNetwork;
import party.lemons.biomemakeover.network.S2C_DoLightningSplash;
import party.lemons.biomemakeover.util.ClientUtil;

import java.util.Random;

public class S2C_DoLightningEntity extends BaseS2CMessage
{
    private int entityID;
    private int count;

    public S2C_DoLightningEntity(int id, int count) {
        this.entityID = id;
        this.count = count;
    }

    public S2C_DoLightningEntity(FriendlyByteBuf buf)
    {
        entityID = buf.readInt();
        count = buf.readInt();
    }

    @Override
    public MessageType getType() {
        return BMNetwork.LIGHTNING_ENTITY;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(entityID);
        buf.writeInt(count);

    }

    @Override
    public void handle(NetworkManager.PacketContext context)
    {
        Entity e = Minecraft.getInstance().level.getEntity(entityID);
        if(e == null || !(e instanceof LivingEntity))
            return;

        context.queue(()->{
            RandomSource random = ((LivingEntity) e).getRandom();
            Vec3 entityPos = e.position();
            SimpleParticleType particleEffect = BMEffects.LIGHTNING_SPARK.get();

            for(int i = 0; i < count; ++i)
            {
                double speed = random.nextDouble() * 1.0D;
                double ac = random.nextDouble() * Math.PI * 2.0D;
                double xVel = ((Math.cos(ac) * speed) * 0.1D) / 10F;
                double yVel = 0.01D + random.nextDouble() * 0.5D;
                double zVel = ((Math.sin(ac) * speed) * 0.1D) / 10F;

                Particle particle = ClientUtil.spawnParticle(particleEffect, particleEffect.getType().getOverrideLimiter(), true, entityPos.x + xVel * 0.01D, entityPos.y + 0.3D, entityPos.z + zVel * 0.01D, xVel, yVel, zVel);
                if(particle != null)
                {
                    particle.setPower((float) speed);
                }
            }
        });
    }
}
