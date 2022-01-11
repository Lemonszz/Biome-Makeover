package party.lemons.biomemakeover.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.ParticleStatus;
import net.minecraft.client.particle.Particle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.init.BMItems;
import party.lemons.biomemakeover.init.BMNetwork;
import party.lemons.biomemakeover.util.ClientUtil;

import java.util.Random;

public class S2C_DoLightningSplash extends BaseS2CMessage
{
    private BlockPos pos;
    private boolean doBottle;

    public S2C_DoLightningSplash(boolean doBottle, BlockPos pos)
    {
        this.pos = pos;
        this.doBottle = doBottle;
    }

    public S2C_DoLightningSplash(FriendlyByteBuf buf)
    {
        this.doBottle = buf.readBoolean();
        this.pos = buf.readBlockPos();
    }

    @Override
    public MessageType getType() {
        return BMNetwork.LIGHTNING_SPLASH;
    }

    @Override
    public void write(FriendlyByteBuf buf)
    {
        buf.writeBoolean(doBottle);
        buf.writeBlockPos(pos);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(()->{
            Vec3 dir = Vec3.atBottomCenterOf(pos);
            Random random = context.getPlayer().getRandom();
            Level level =  context.getPlayer().level;

            if(doBottle)
            {
                for(int i = 0; i < 8; ++i)
                {
                    level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(BMItems.LIGHTNING_BOTTLE)), dir.x, dir.y, dir.z, random.nextGaussian() * 0.15D, random.nextDouble() * 0.2D, random.nextGaussian() * 0.15D);
                }
                level.playSound(null, pos, SoundEvents.SPLASH_POTION_BREAK, SoundSource.NEUTRAL, 1.0F, random.nextFloat() * 0.1F + 0.9F);
            }

            ParticleOptions particleEffect = BMEffects.LIGHTNING_SPARK.get();
            ParticleStatus mode = Minecraft.getInstance().options.particles;
            int particleCount = mode == ParticleStatus.ALL ? 100 : mode == ParticleStatus.DECREASED ? 50 : 10;

            for(int i = 0; i < particleCount; ++i)
            {
                double direction = random.nextDouble() * 4.0D;
                double ac = random.nextDouble() * Math.PI * 2.0D;
                double xVel = (Math.cos(ac) * direction) * 0.1D;
                double yVel = 0.01D + random.nextDouble() * 0.5D;
                double zVel = (Math.sin(ac) * direction) * 0.1D;

                Particle particle = ClientUtil.spawnParticle(particleEffect, particleEffect.getType().getOverrideLimiter(), true, dir.x + xVel * 0.01D, dir.y + 0.3D, dir.z + zVel * 0.01D, xVel, yVel, zVel);
                if(particle != null)
                {
                    particle.setPower((float) direction);
                }
            }
        });
    }
}
