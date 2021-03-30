package party.lemons.biomemakeover.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public interface TotemItem
{
	boolean canActivate(LivingEntity entity);
	void activateTotem(LivingEntity entity, ItemStack stack);
}
