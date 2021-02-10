package party.lemons.biomemakeover.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;

public enum RLayer
{
	TRIPWIRE,
	CUTOUT_MIPPED,
	CUTOUT,
	TRANSLUCENT;

	@Environment(EnvType.CLIENT)
	public RenderLayer getAsRenderLayer()
	{
		switch(this)
		{
			case TRIPWIRE:
				return RenderLayer.getTripwire();
			case CUTOUT_MIPPED:
				return RenderLayer.getCutoutMipped();
			case CUTOUT:
				return RenderLayer.getCutout();
			case TRANSLUCENT:
				return RenderLayer.getTranslucent();
		}
		return null;
	}
}
