package party.lemons.biomemakeover.init;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.Registry;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.util.RegistryHelper;

public class BMEffects
{
	public static final SoundEvent BUTTON_MUSHROOMS = new SoundEvent(BiomeMakeover.ID("button_mushrooms"));
	public static final SoundEvent GHOST_CHARGE = new SoundEvent(BiomeMakeover.ID("ghost_charge"));
	public static final SoundEvent GHOST_DEATH = new SoundEvent(BiomeMakeover.ID("ghost_death"));
	public static final SoundEvent GHOST_IDLE = new SoundEvent(BiomeMakeover.ID("ghost_idle"));
	public static final SoundEvent GHOST_HURT = new SoundEvent(BiomeMakeover.ID("ghost_hurt"));
	public static final SoundEvent GHOST_ANGRY = new SoundEvent(BiomeMakeover.ID("ghost_angry"));
	public static final SoundEvent SCUTTLER_RATTLE = new SoundEvent(BiomeMakeover.ID("scuttler_rattle"));
	public static final SoundEvent SCUTTLER_STEP = new SoundEvent(BiomeMakeover.ID("scuttler_step"));
	public static final SoundEvent SCUTTLER_HURT = new SoundEvent(BiomeMakeover.ID("scuttler_hurt"));
	public static final SoundEvent SCUTTLER_DEATH = new SoundEvent(BiomeMakeover.ID("scuttler_death"));

	public static void init()
	{
		RegistryHelper.register(Registry.SOUND_EVENT, SoundEvent.class, BMEffects.class);
	}
}
