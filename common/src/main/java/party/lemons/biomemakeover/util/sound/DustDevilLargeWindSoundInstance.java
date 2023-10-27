package party.lemons.biomemakeover.util.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundSource;
import party.lemons.biomemakeover.entity.DustDevilEntity;
import party.lemons.biomemakeover.init.BMEffects;

public class DustDevilLargeWindSoundInstance extends LoopingAlternatingEntitySound<DustDevilEntity>
{
    public DustDevilLargeWindSoundInstance(DustDevilEntity dustDevil) {
        super(dustDevil, BMEffects.DUST_DEVIL_WIND.get(), SoundSource.HOSTILE, 1.0F, 0.5F);
    }

    @Override
    protected AbstractTickableSoundInstance getAlternativeSoundInstance() {
        return new DustDevilIdleWindSoundInstance(devil);
    }

    @Override
    protected boolean shouldSwitchSounds() {
        return !(devil.tornadoStartAnimation.isStarted() || devil.tornadoAnimation.isStarted());
    }
}
