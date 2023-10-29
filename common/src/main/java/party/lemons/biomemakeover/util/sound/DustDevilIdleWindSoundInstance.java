package party.lemons.biomemakeover.util.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundSource;
import party.lemons.biomemakeover.entity.DustDevilEntity;
import party.lemons.biomemakeover.init.BMEffects;

public class DustDevilIdleWindSoundInstance extends LoopingAlternatingEntitySound<DustDevilEntity>
{
    public DustDevilIdleWindSoundInstance(DustDevilEntity dustDevil) {
        super(dustDevil, BMEffects.DUST_DEVIL_WIND_IDLE.get(), SoundSource.HOSTILE, 1.0F, 0.25F);
    }

    @Override
    protected AbstractTickableSoundInstance getAlternativeSoundInstance() {
        return devil.isGrinding() ? new DustDevilGrindingWindSoundInstance(devil) : new DustDevilLargeWindSoundInstance(devil);
    }

    @Override
    protected boolean shouldSwitchSounds() {
        return devil.isGrinding() || devil.tornadoStartAnimation.isStarted() || devil.tornadoAnimation.isStarted();
    }
}
