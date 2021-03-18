package party.lemons.biomemakeover.util.sound;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.EntityTrackingSoundInstance;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.sound.SoundCategory;
import party.lemons.biomemakeover.entity.StoneGolemEntity;
import party.lemons.biomemakeover.init.BMEffects;

public class StoneGolemTurnSoundInstance extends MovingSoundInstance
{
	private final StoneGolemEntity golem;
	private int noTurnTime = 0;
	private int time = 0;

	public StoneGolemTurnSoundInstance(StoneGolemEntity golem)
	{
		super(BMEffects.STONE_GOLEM_TURN, SoundCategory.NEUTRAL);
		this.golem = golem;
		this.volume = 0.5F;
		noTurnTime = 0;
		time = 0;
	}

	@Override
	public void tick()
	{
		if(!this.golem.removed)
		{
			this.x = (float) this.golem.getX();
			this.y = (float) this.golem.getY();
			this.z = (float) this.golem.getZ();

		}

		time++;
		if(golem.bodyYaw == golem.prevBodyYaw)
			noTurnTime++;
		else
			noTurnTime = 0;

		if(golem.removed || noTurnTime > 3 || !MinecraftClient.getInstance().getSoundManager().isPlaying(this))
		{
			if(time > 5 && !isDone())
				MinecraftClient.getInstance().getSoundManager().playNextTick(new EntityTrackingSoundInstance(BMEffects.STONE_GOLEM_STOP, SoundCategory.NEUTRAL, 0.25F, 1F + ((-0.5F + golem.getRandom().nextFloat()) / 5F), golem));
			setDone();
		}
	}
}
