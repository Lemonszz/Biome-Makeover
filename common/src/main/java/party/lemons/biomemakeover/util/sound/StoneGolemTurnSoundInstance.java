package party.lemons.biomemakeover.util.sound;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.EntityBoundSoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import party.lemons.biomemakeover.entity.StoneGolemEntity;
import party.lemons.biomemakeover.init.BMEffects;

public class StoneGolemTurnSoundInstance extends AbstractTickableSoundInstance {

    private final StoneGolemEntity golem;
    private int noTurnTime = 0;
    private int time = 0;

    public StoneGolemTurnSoundInstance(StoneGolemEntity golem)
    {
        super(BMEffects.STONE_GOLEM_TURN, SoundSource.NEUTRAL);
        this.golem = golem;
        this.volume = 0.5F;
        noTurnTime = 0;
        time = 0;
    }

    @Override
    public void tick() {
        if(!this.golem.isRemoved())
        {
            this.x = (float) this.golem.getX();
            this.y = (float) this.golem.getY();
            this.z = (float) this.golem.getZ();
        }

        time++;
        if(golem.yBodyRot == golem.yBodyRotO)
            noTurnTime++;
        else
            noTurnTime = 0;

        if(golem.isRemoved() || noTurnTime > 3 || !Minecraft.getInstance().getSoundManager().isActive(this))
        {
            if(time > 5 && !isStopped())
                Minecraft.getInstance().getSoundManager().queueTickingSound(new EntityBoundSoundInstance(BMEffects.STONE_GOLEM_STOP, SoundSource.NEUTRAL, 0.25F, 1F + ((-0.5F + golem.getRandom().nextFloat()) / 5F), golem));
            stop();
        }
    }
}
