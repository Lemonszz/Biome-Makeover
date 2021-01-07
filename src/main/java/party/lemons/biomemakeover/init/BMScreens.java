package party.lemons.biomemakeover.init;

import net.fabricmc.fabric.api.screenhandler.v1.ScreenHandlerRegistry;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.screen.ScreenHandlerType;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.crafting.witch.screen.WitchScreenHandler;

public class BMScreens
{
	public static ScreenHandlerType<WitchScreenHandler> WITCH;

	public static void init()
	{
		WITCH = ScreenHandlerRegistry.registerSimple(BiomeMakeover.ID("witch"), WitchScreenHandler::new);
	}
}
