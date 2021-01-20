package party.lemons.biomemakeover.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class ItemStackUtil
{
	public static void forEachEnchantment(Consumer consumer, ItemStack stack, boolean allowEmpty)
	{
		if (!stack.isEmpty() || allowEmpty) {
			ListTag listTag = stack.getEnchantments();

			for(int i = 0; i < listTag.size(); ++i) {
				String string = listTag.getCompound(i).getString("id");
				int j = listTag.getCompound(i).getInt("lvl");
				Registry.ENCHANTMENT.getOrEmpty(Identifier.tryParse(string)).ifPresent((enchantment) -> {
					consumer.accept(enchantment, stack, j);
				});
			}

		}
	}

	public static void forEachEnchantment(Consumer consumer, ItemStack stack)
	{
		forEachEnchantment(consumer, stack, false);
	}

	@FunctionalInterface
	public interface Consumer {
		void accept(Enchantment enchantment, ItemStack stack, int level);
	}
}
