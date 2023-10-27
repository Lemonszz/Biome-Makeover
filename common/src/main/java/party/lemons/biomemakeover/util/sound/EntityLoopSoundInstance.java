package party.lemons.biomemakeover.util.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;

public class EntityLoopSoundInstance extends AbstractTickableSoundInstance
{
    protected final LivingEntity entity;
    public EntityLoopSoundInstance(LivingEntity entity, SoundEvent soundEvent, RandomSource randomSource) {
        super(soundEvent, entity.getSoundSource(), randomSource);

        this.entity = entity;
        this.x = (float) entity.getX();
        this.y = (float) entity.getY();
        this.z = (float) entity.getZ();
        this.looping = true;
        this.delay = 0;
        this.volume = 0.0F;
    }

    @Override
    public void tick()
    {
        if(!this.entity.isRemoved())
        {
            this.x = (float) this.entity.getX();
            this.y = (float) this.entity.getY();
            this.z = (float) this.entity.getZ();
            float f = (float)this.entity.getDeltaMovement().horizontalDistance();
            if((double) f >= 0.01D)
            {
                this.pitch = Mth.lerp(Mth.clamp(f, this.getMinPitch(), this.getMaxPitch()), this.getMinPitch(), this.getMaxPitch());
                this.volume = Mth.lerp(Mth.clamp(f, 0.0F, 0.5F), 0.0F, 1.2F);
            }else
            {
                this.pitch = 0.0F;
                this.volume = 0.0F;
            }

        }else
        {
            this.stop();
        }
    }

    private float getMinPitch()
    {
        return this.entity.isBaby() ? 1.1F : 0.7F;
    }

    private float getMaxPitch()
    {
        return this.entity.isBaby() ? 1.5F : 1.1F;
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    public boolean canPlaySound() {
        return !this.entity.isSilent();
    }
}
