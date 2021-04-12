package party.lemons.biomemakeover.mixin;

import net.minecraft.entity.mob.SlimeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import party.lemons.biomemakeover.util.access.SlimeEntityAccess;

@Mixin(SlimeEntity.class)
public abstract class SlimeEntityMixin implements SlimeEntityAccess
{
	@Shadow
	protected abstract void setSize(int size, boolean heal);

	@Override
	public void bm_setSlimeSize(int size, boolean heal)
	{
		setSize(size, heal);
	}
}

