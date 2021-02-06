package party.lemons.biomemakeover.init;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.screen.ScreenHandlerType;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.crafting.witch.screen.WitchScreenHandler;
import party.lemons.biomemakeover.gui.AltarScreenHandler;

public class BMScreens
{
	public static ScreenHandlerType<WitchScreenHandler> WITCH;
	public static ScreenHandlerType<AltarScreenHandler> ALTAR;

	public static void init()
	{
		WITCH = ScreenHandlerRegistry.registerSimple(BiomeMakeover.ID("witch"), WitchScreenHandler::new);
		ALTAR = ScreenHandlerRegistry.registerSimple(BiomeMakeover.ID("altar"), AltarScreenHandler::new);
	}
}
