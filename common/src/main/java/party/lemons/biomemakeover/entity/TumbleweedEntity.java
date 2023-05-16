package party.lemons.biomemakeover.entity;

import dev.architectury.networking.NetworkManager;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.level.Level;
import org.joml.Quaternionf;
import org.joml.Vector3d;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.level.WindSystem;
import party.lemons.biomemakeover.util.OldQuat;

public class TumbleweedEntity extends Entity
{
    public Quaternionf quaternion = new Quaternionf(0, 0, 0, 1);
    public Quaternionf prevQuaternion = new Quaternionf(0, 0, 0, 1);
    private Vector3d prevVelocity;
    private float xRot = 0;
    private float zRot = 0;
    private double disX = 0;
    private double disZ = 0;
    private float windOffset = 0;
    private final float acceleration = 0.0025F;
    private float age = 0;
    private int stuckX, stuckZ, staticTime;

    public TumbleweedEntity(EntityType<? extends TumbleweedEntity> type, Level level)
    {
        super(type, level);
        windOffset = 1F - (level.getRandom().nextFloat() / 3);
    }

    @Override
    public void tick()
    {
        age++;
        if(age > 1500) kill();

        if(!isInWater()) this.setDeltaMovement(this.getDeltaMovement().add(0.0D, -0.04D, 0.0D));

        super.tick();

        prevQuaternion = new Quaternionf(quaternion);
        prevVelocity = new Vector3d(getDeltaMovement().x, getDeltaMovement().y, getDeltaMovement().z);

        double pX = getX();
        double pZ = getZ();

        this.move(MoverType.SELF, getDeltaMovement());

        double fX = getX();
        double fZ = getZ();
        disX = fX - pX;
        disZ = fZ - pZ;

        double vX = 0, vY = 0, vZ = 0;

        if(!level().isClientSide())
        {
            vX = step(getDeltaMovement().x(), (WindSystem.windX * windOffset), acceleration);
            vZ = step(getDeltaMovement().z(), (WindSystem.windZ * windOffset), acceleration);
            vY = getDeltaMovement().y();

            if(onGround())
            {
                vY = Mth.clamp(Math.abs(prevVelocity.y) * 0.75D, 0.31F, 2);
                this.playSound(BMEffects.TUMBLEWEED_TUMBLE.get(), 0.25F, 1.0F);
            }

            if(isInWater())
            {
                vX *= 0.75F;
                vZ *= 0.75F;
                vY = 0.1F;
            }
        }else
        {
            if(onGround())
            {
                makeParticles(15);
                xRot = (float) -(disX / 0.25D);
                zRot = (float) (disZ / 0.25D);
            }else
            {
                xRot = (float) -(disX / 0.6D);
                zRot = (float) (disZ / 0.6D);
            }

            OldQuat rot = new OldQuat(zRot, 0F, xRot, false);
            rot.mul(new OldQuat(quaternion.x, quaternion.y, quaternion.z, quaternion.w));
            quaternion = new Quaternionf(rot.i(), rot.j(), rot.k(), rot.r());;
        }

        if(!level().isClientSide())
        {
            setDeltaMovement(vX, vY, vZ);
            hasImpulse = true;
            hurtMarked = true;
        }

        if((int) getX() == stuckX && (int) getZ() == stuckZ)
        {
            staticTime++;
            if(staticTime >= 100)
            {
                kill();
            }
        }else
        {
            staticTime = 0;
        }
        stuckX = (int) getX();
        stuckZ = (int) getZ();
    }

    @Override
    public void kill()
    {
        makeParticles(30);
        super.kill();
    }

    @Override
    protected void defineSynchedData() {

    }

    @Override
    public boolean hurt(DamageSource damageSource, float f) {
        if(!damageSource.is(BMEntities.TUMBLEWEED_IMMUNE_DAMAGE))
        {
            this.playSound(BMEffects.TUMBLEWEED_BREAK.get(), 0.25F, 1.0F);
            kill();
        }

        return true;
    }

    private void makeParticles(int count)
    {
        for(int i = 0; i < count; i++)
        {
            level().addParticle(new BlockParticleOption(ParticleTypes.BLOCK, BMBlocks.TUMBLEWEED.get().defaultBlockState()), -0.5D + (getX() + random.nextDouble()), getY() + random.nextDouble(), -0.5D + (getZ() + random.nextDouble()), 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public boolean isPushable()
    {
        return true;
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    protected void addAdditionalSaveData(CompoundTag compoundTag) {

    }

    @Override
    public boolean isPickable() {
        return !this.isRemoved();
    }

    @Override
    public boolean canCollideWith(Entity entity) {
        return true;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket()
    {
        return NetworkManager.createAddEntityPacket(this);
    }

    private double step(double val, double target, double step)
    {
        if(val < target) return Math.min(val + step, target);
        else return Math.max(val - step, target);
    }
}