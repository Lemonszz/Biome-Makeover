package party.lemons.biomemakeover.mixin.client;

import net.minecraft.client.model.ModelPart;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import party.lemons.biomemakeover.util.access.CuboidAccessor;

@Mixin(ModelPart.Cuboid.class)
public class CuboidMixin implements CuboidAccessor
{
	@Shadow
	@Final
	private ModelPart.Quad[] sides;

	@Override
	public ModelPart.Quad[] bm_getSides()
	{
		return sides;
	}
}
