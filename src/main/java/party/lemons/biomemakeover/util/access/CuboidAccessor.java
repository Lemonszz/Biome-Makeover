package party.lemons.biomemakeover.util.access;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;

public interface CuboidAccessor
{
	@Environment(EnvType.CLIENT)
	ModelPart.Quad[] bm_getSides();
}
