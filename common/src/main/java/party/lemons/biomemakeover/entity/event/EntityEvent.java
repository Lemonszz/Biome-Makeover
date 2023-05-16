package party.lemons.biomemakeover.entity.event;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.function.Consumer;

public enum EntityEvent
{
    BONEMEAL_PARTICLE(e->entityParticles(e, ParticleTypes.HAPPY_VILLAGER, 15, 0.02F, 0,0, 0)),
    ENDER_PARTICLES(e->entityParticles(e, ParticleTypes.PORTAL, 10, 0.5F, (e.level().random.nextDouble() - 0.5D),  -e.level().random.nextDouble(), (e.level().random.nextDouble() - 0.5D) * 2.0D)),
    TELEPORT_PARTICLES(e->centeredEntityParticles(e, BMEffects.TELEPORT.get(), 10, true, e.level().random.nextGaussian() * 0.02D, e.level().random.nextGaussian() * 0.02D, e.level().random.nextGaussian() * 0.02D));

    private static void centeredEntityParticles(Entity entity, SimpleParticleType particle, int count, boolean varyY, double vX, double vY, double vZ)
    {
        for(int i = 0; i < count; i++)
        {
            AABB bb = entity.getBoundingBox();
            double xx = bb.getCenter().x;
            double zz = bb.getCenter().z;
            double yy;
            if(varyY)
                yy = RandomUtil.randomRange(bb.minY, bb.maxY);
            else
                yy = bb.minY;

            entity.level().addParticle(particle,
                    xx,
                    yy,
                    xx,
                    vX, vY, vZ);
        }
    }


    private static void entityParticles(Entity entity, SimpleParticleType particle, int count, float offset, double vX, double vY, double vZ)
    {
        for(int i = 0; i < count; i++)
        {
            entity.level().addParticle(particle,
                    entity.getRandomX(offset),
                    entity.getRandomY(),
                    entity.getRandomZ(offset),
                    vX, vY, vZ);
        }
    }

    private static void enderParticles(Entity entity)
    {
        for(int i = 0; i < 10; i++)
        {
            entity.level().addParticle(ParticleTypes.PORTAL,
                    entity.getRandomX(0.5F),
                    entity.getRandomY(),
                    entity.getRandomZ(0.5F),
                    (entity.level().random.nextDouble() - 0.5D) * 2.0D, -entity.level().random.nextDouble(), (entity.level().random.nextDouble() - 0.5D) * 2.0D);
        }
    }

    private static void entityBonemeal(Entity entity)
    {
        entity.level().addParticle(ParticleTypes.HAPPY_VILLAGER, entity.getX(), entity.getY(), entity.getZ(), 0, 0, 0);

        for(int i = 0; i < 15; i++)
        {
            double xx = entity.getRandomX(0.02D);
            double yy = entity.getRandomY();
            double zz = entity.getRandomZ(0.02D);

            entity.level().addParticle(ParticleTypes.HAPPY_VILLAGER, xx, yy, zz, 0, 0, 0);
        }
    }

    private final Consumer<Entity> behaviour;
    EntityEvent(Consumer<Entity> behaviour)
    {
        this.behaviour = behaviour;
    }

    public void execute(Entity entity)
    {
        this.behaviour.accept(entity);
    }
}
