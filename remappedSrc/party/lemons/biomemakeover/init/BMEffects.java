package party.lemons.biomemakeover.init;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.Registry;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.util.RegistryHelper;

public class BMEffects
{
	public static final SoundEvent BUTTON_MUSHROOMS = new SoundEvent(BiomeMakeover.ID("button_mushrooms"));

	public static void init()
	{
		RegistryHelper.register(Registry.SOUND_EVENT, SoundEvent.class, BMEffects.class);
	}
}
