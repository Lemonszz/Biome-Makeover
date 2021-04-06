package party.lemons.biomemakeover.compat.rei;

import me.shedaniel.rei.api.DisplayHelper;
import me.shedaniel.rei.api.EntryRegistry;
import me.shedaniel.rei.api.OverlayDecider;
import me.shedaniel.rei.api.plugins.REIPluginV0;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.gui.DirectionalDataScreen;
import party.lemons.biomemakeover.init.BMItems;

public class BMRei implements REIPluginV0
{
	@Override
	public Identifier getPluginIdentifier()
	{
		return BiomeMakeover.ID("biomemakeover_plugin");
	}

	@Override
	public void registerEntries(EntryRegistry entryRegistry) {
		entryRegistry.removeEntryIf(e-> e.getItem() != null && e.getItem() == BMItems.ICON_ITEM);
	}

	@Override
	public void registerBounds(DisplayHelper displayHelper)
	{
		displayHelper.registerHandler(new OverlayDecider()
		{
			@Override
			public boolean isHandingScreen(Class<?> screen)
			{
				if(screen == DirectionalDataScreen.class)
					return true;

				return false;
			}

			@Override
			public ActionResult shouldScreenBeOverlayed(Class<?> screen)
			{
				return ActionResult.FAIL;
			}
		});
	}
}
