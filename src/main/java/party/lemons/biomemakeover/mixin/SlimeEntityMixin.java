package party.lemons.biomemakeover.mixin;

import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import party.lemons.biomemakeover.util.access.SlimeEntityAccess;

@Mixin(SlimeEntity.class)
public abstract class SlimeEntityMixin implements SlimeEntityAccess
{
	@Shadow protected abstract void setSize(int size, boolean heal);

	@Override
	public void setSlimeSize(int size, boolean heal)
	{
		setSize(size, heal);
	}
}

