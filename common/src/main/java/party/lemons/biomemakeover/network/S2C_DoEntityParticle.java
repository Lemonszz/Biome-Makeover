package party.lemons.biomemakeover.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import party.lemons.biomemakeover.init.BMNetwork;
import party.lemons.biomemakeover.util.RandomUtil;

public class S2C_DoEntityParticle extends BaseS2CMessage
{
    private int entityID;
    private ParticleOptions effect;
    private int count;
    private float offset;

    private float velX, velY, velZ;


    public S2C_DoEntityParticle(Entity entity, ParticleOptions effect, int count, float offset)
    {
        this.entityID = entity.getId();
        this.effect=  effect;
        this.count = count;
        this.offset = offset;
    }

    public S2C_DoEntityParticle(FriendlyByteBuf buf)
    {
        entityID = buf.readInt();

        ResourceLocation particleID = buf.readResourceLocation();
        effect = (ParticleOptions) BuiltInRegistries.PARTICLE_TYPE.get(particleID);

        count = buf.readInt();
        offset = buf.readFloat();
        velX = buf.readFloat();
        velY = buf.readFloat();
        velZ = buf.readFloat();
    }

    @Override
    public MessageType getType() {
        return BMNetwork.ENTITY_PARTICLE;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(entityID);
        buf.writeResourceLocation(BuiltInRegistries.PARTICLE_TYPE.getKey((ParticleType<?>)effect));
        buf.writeInt(count);
        buf.writeFloat(offset);

        buf.writeDouble(RandomUtil.RANDOM.nextGaussian() * 0.02D);
        buf.writeDouble(RandomUtil.RANDOM.nextGaussian() * 0.02D);
        buf.writeDouble(RandomUtil.RANDOM.nextGaussian() * 0.02D);
    }

    @Override
    public void handle(NetworkManager.PacketContext context)
    {
        Entity entity = context.getPlayer().level().getEntity(entityID);
        if(entity == null)
            return;
        if(effect == null)
            return;

        context.queue(()->{
            for(int i = 0; i < count; ++i)
            {
                AABB bb = entity.getBoundingBox();
                double xx = RandomUtil.randomRange(bb.minX - offset, bb.maxX + offset);
                double yy = RandomUtil.randomRange(bb.minY - offset, bb.maxY + offset);
                double zz = RandomUtil.randomRange(bb.minZ - offset, bb.maxZ + offset);
                entity.level().addParticle(effect, xx, yy, zz, velX, velY, velZ);
            }
        });
    }

}
