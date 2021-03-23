package party.lemons.biomemakeover.mixin;

import com.google.common.collect.Lists;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.EnchantRandomlyLootFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Collections;
import java.util.List;
import java.util.Random;

@Mixin(EnchantRandomlyLootFunction.class)
public class EnchantRandomlyLootFunctionMixin
{
	@Inject(
			at = @At(value = "INVOKE", target = "Ljava/util/List;get(I)Ljava/lang/Object;", ordinal = 0, shift = At.Shift.BY, by = -2),
			method = "process",
			locals = LocalCapture.CAPTURE_FAILEXCEPTION
	)
	public void process(ItemStack stack, LootContext context, CallbackInfoReturnable<ItemStack> cbi, Random random, boolean bl, List<Enchantment> list)
	{
		/*
			Remove all curses from possible enchantment list except a max of 2.
			This almost mimics vanilla's behaviour, as there's only 2 curses.
			Since we have many more curses, without this, curses are over represented.
		 */

		long curseCount = list.stream().filter(Enchantment::isCursed).count();

		if(curseCount > 2)
		{
			Collections.shuffle(list);
			List<Enchantment> toRemove = Lists.newArrayList();
			int found = 0;
			for(Enchantment e : list)
			{
				if(e.isCursed())
				{
					if(found < 2)
					{
						found++;
					}
					else
					{
						toRemove.add(e);
					}
				}
			}

			list.removeAll(toRemove);
		}
	}
}