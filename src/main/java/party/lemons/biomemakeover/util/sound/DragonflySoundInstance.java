package party.lemons.biomemakeover.util.sound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.entity.Entity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.MathHelper;
import party.lemons.biomemakeover.entity.DragonflyEntity;
import party.lemons.biomemakeover.init.BMEffects;

@Environment(EnvType.CLIENT)
public class DragonflySoundInstance extends MovingSoundInstance
{
	protected final DragonflyEntity dragonfly;
	private boolean replaced;

	public DragonflySoundInstance(DragonflyEntity dragonfly)
	{
		super(BMEffects.DRAGONFLY_LOOP, SoundCategory.NEUTRAL);
		this.dragonfly = dragonfly;
		this.x = (float) dragonfly.getX();
		this.y = (float) dragonfly.getY();
		this.z = (float) dragonfly.getZ();
		this.repeat = true;
		this.repeatDelay = 0;
		this.volume = 0.0F;
	}

	public void tick()
	{
		if(!this.dragonfly.removed && !this.replaced)
		{
			this.x = (float) this.dragonfly.getX();
			this.y = (float) this.dragonfly.getY();
			this.z = (float) this.dragonfly.getZ();
			float f = MathHelper.sqrt(Entity.squaredHorizontalLength(this.dragonfly.getVelocity()));
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
		return this.dragonfly.isBaby() ? 1.1F : 0.7F;
	}

	private float getMaxPitch()
	{
		return this.dragonfly.isBaby() ? 1.5F : 1.1F;
	}

	public boolean shouldAlwaysPlay()
	{
		return true;
	}

	public boolean canPlay()
	{
		return !this.dragonfly.isSilent();
	}
}
