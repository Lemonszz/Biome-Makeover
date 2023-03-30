package party.lemons.biomemakeover.mixin;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.init.BMItems;

@Mixin(Camel.class)
public abstract class CamelMixin extends AbstractHorse
{
	protected CamelMixin(EntityType<? extends AbstractHorse> entityType, Level level)
	{
		super(entityType, level);
	}

	@Inject(at = @At("RETURN"), method = "isFood", cancellable = true)
	private void isFood(ItemStack stack, CallbackInfoReturnable<Boolean> cbi)
	{
		if(stack.is(BMItems.ADDITIONAL_CAMEL_FOOD))
		{
			cbi.setReturnValue(true);
		}
	}
}
