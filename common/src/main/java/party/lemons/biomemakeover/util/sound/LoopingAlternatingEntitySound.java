package party.lemons.biomemakeover.util.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;

public abstract class LoopingAlternatingEntitySound<T extends Entity> extends AbstractTickableSoundInstance {
    protected final T devil;
    protected boolean hasSwitched;
    protected final float soundPitch;
    protected final float soundVolume;

    public LoopingAlternatingEntitySound(T dustDevil, SoundEvent soundEvent, SoundSource soundSource, float pitch, float volume) {
        super(soundEvent, soundSource, SoundInstance.createUnseededRandom());
        this.devil = dustDevil;
        this.x = (float) dustDevil.getX();
        this.y = (float) dustDevil.getY();
        this.z = (float) dustDevil.getZ();
        this.looping = true;
        this.delay = 0;
        this.volume = 0.0F; //set the volume to 0, so it doesn't start playing until we have checked if it should switch

        this.soundPitch = pitch;
        this.soundVolume = volume;
    }

    @Override
    public void tick() {
        boolean bl = this.shouldSwitchSounds();
        if (bl && !this.isStopped()) {
            Minecraft.getInstance().getSoundManager().queueTickingSound(this.getAlternativeSoundInstance());
            this.hasSwitched = true;
        }

        if (!this.devil.isRemoved() && !this.hasSwitched) {
            this.x = (float) this.devil.getX();
            this.y = (float) this.devil.getY();
            this.z = (float) this.devil.getZ();
            this.pitch = getSoundPitch();
            this.volume = getSoundVolume();
        } else {
            this.stop();
        }
    }

    private float getSoundVolume()
    {
        return soundVolume;
    }

    private float getSoundPitch()
    {
        return soundPitch;
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    public boolean canPlaySound() {
        return !this.devil.isSilent();
    }

    protected abstract AbstractTickableSoundInstance getAlternativeSoundInstance();

    protected abstract boolean shouldSwitchSounds();
}