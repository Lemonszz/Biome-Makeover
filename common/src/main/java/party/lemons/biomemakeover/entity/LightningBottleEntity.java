package party.lemons.biomemakeover.entity;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.init.BMItems;
import party.lemons.biomemakeover.init.BMPotions;
import party.lemons.biomemakeover.util.NetworkUtil;

import java.util.Iterator;
import java.util.List;

public class LightningBottleEntity extends ThrowableItemProjectile
{
    public LightningBottleEntity(EntityType<? extends LightningBottleEntity> entityType, Level world)
    {
        super(entityType, world);
    }

    public LightningBottleEntity(Level world, LivingEntity owner)
    {
        super(BMEntities.LIGHTNING_BOTTLE.get(), owner, world);
    }

    public LightningBottleEntity(Level world, double x, double y, double z)
    {
        super(BMEntities.LIGHTNING_BOTTLE.get(), x, y, z, world);
    }

    @Override
    protected Item getDefaultItem()
    {
        return BMItems.LIGHTNING_BOTTLE.get();
    }

    @Override
    protected float getGravity()
    {
        return 0.07F;
    }

    @Override
    protected void onHit(HitResult hitResult)
    {
        super.onHit(hitResult);
        NetworkUtil.doLightningSplash(level, true, getOnPos());

        if(!this.level.isClientSide())
        {
            level.playSound(null, getOnPos(), BMEffects.BOTTLE_THUNDER.get(), SoundSource.NEUTRAL, 50F, 0.8F + this.random.nextFloat() * 0.2F);

            AABB box = this.getBoundingBox().inflate(4.0D, 2.0D, 4.0D);
            List<LivingEntity> entities = this.level.getEntitiesOfClass(LivingEntity.class, box, EntitySelector.LIVING_ENTITY_STILL_ALIVE);
            if(!entities.isEmpty())
            {
                for (LivingEntity e : entities)
                {
                    double distance = this.distanceToSqr(e);
                    if (distance < 16.0D) {
                        int fireTicks = e.getRemainingFireTicks();
                        boolean isInvul = e.isInvulnerable();

                        LightningBolt dummyLightning = new LightningBolt(EntityType.LIGHTNING_BOLT, level);
                        dummyLightning.setPos(e.getX(), e.getY(), e.getZ());
                        e.setInvulnerable(true);
                        e.thunderHit((ServerLevel) level, dummyLightning);

                        e.setRemainingFireTicks(fireTicks);
                        e.setInvulnerable(isInvul);
                        dummyLightning.remove(RemovalReason.DISCARDED);
                    }
                }
            }

            //Loop through again to grab transformed entities for damage & effect
            entities = this.level.getEntitiesOfClass(LivingEntity.class, box, EntitySelector.LIVING_ENTITY_STILL_ALIVE);
            if(!entities.isEmpty())
            {

                for (LivingEntity e : entities) {
                    double distance = this.distanceToSqr(e);
                    if (distance < 16.0D) {
                        NetworkUtil.doLightningEntity(level, e, 100);

                        if (!e.hasEffect(BMPotions.SHOCKED.get())) {
                            e.addEffect(new MobEffectInstance(BMPotions.SHOCKED.get(), 1000, 0));
                        } else {
                            e.addEffect(new MobEffectInstance(BMPotions.SHOCKED.get(), 1000, Math.min(3, e.getEffect(BMPotions.SHOCKED.get()).getAmplifier() + 1)));
                        }
                        e.hurt(DamageSource.indirectMagic(this, this.getOwner()), 0);
                        if (getOwner() instanceof LivingEntity) {
                            e.setLastHurtByMob((LivingEntity) getOwner());
                        }

                        if (e.getHealth() > e.getMaxHealth()) e.setHealth(e.getMaxHealth());
                    }
                }
            }
            this.remove(RemovalReason.DISCARDED);

        }

    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket()
    {
        return NetworkManager.createAddEntityPacket(this);
    }
}