package party.lemons.biomemakeover.util.sound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.MathHelper;
import party.lemons.biomemakeover.entity.DragonflyEntity;
import party.lemons.biomemakeover.init.BMEffects;

@Environment(EnvType.CLIENT)
public class EntityLoopSoundInstance extends MovingSoundInstance
{
	protected final LivingEntity entity;
	private boolean replaced;

	public EntityLoopSoundInstance(LivingEntity entity, SoundEvent soundEvent)
	{
		super(soundEvent, entity.getSoundCategory());
		this.entity = entity;
		this.x = (float) entity.getX();
		this.y = (float) entity.getY();
		this.z = (float) entity.getZ();
		this.repeat = true;
		this.repeatDelay = 0;
		this.volume = 0.0F;
	}

	public void tick()
	{
		if(!this.entity.removed && !this.replaced)
		{
			this.x = (float) this.entity.getX();
			this.y = (float) this.entity.getY();
			this.z = (float) this.entity.getZ();
			float f = MathHelper.sqrt(Entity.squaredHorizontalLength(this.entity.getVelocity()));
			if((double) f >= 0.01D)
			{
				this.pitch = MathHelper.lerp(MathHelper.clamp(f, this.getMinPitch(), this.getMaxPitch()), this.getMinPitch(), this.getMaxPitch());
				this.volume = MathHelper.lerp(MathHelper.clamp(f, 0.0F, 0.5F), 0.0F, 1.2F);
			}else
			{
				this.pitch = 0.0F;
				this.volume = 0.0F;
			}

		}else
		{
			this.setDone();
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

	public boolean shouldAlwaysPlay()
	{
		return true;
	}

	public boolean canPlay()
	{
		return !this.entity.isSilent();
	}
}
