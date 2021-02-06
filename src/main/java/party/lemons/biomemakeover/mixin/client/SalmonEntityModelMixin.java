package party.lemons.biomemakeover.mixin.client;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.SalmonEntityModel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import party.lemons.biomemakeover.util.access.SalmonEntityModelAccessor;

@Mixin(SalmonEntityModel.class)
public class SalmonEntityModelMixin implements SalmonEntityModelAccessor
{
	@Shadow
	@Final
	public ModelPart tail;

	@Override
	public ModelPart getTail()
	{
		return tail;
	}
}
