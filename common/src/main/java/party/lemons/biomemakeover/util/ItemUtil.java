package party.lemons.biomemakeover.util;

import net.minecraft.core.Registry;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;

public class ItemUtil
{
    public static void shrinkStack(ItemStack stack, Player player)
    {
        if(!player.isCreative())
            stack.shrink(1);
    }

    public static void forEachEnchantment(Consumer consumer, ItemStack stack, boolean allowEmpty)
    {
        if(!stack.isEmpty() || allowEmpty)
        {
            ListTag listTag = stack.getEnchantmentTags();

            for(int i = 0; i < listTag.size(); ++i)
            {
                String string = listTag.getCompound(i).getString("id");
                int j = listTag.getCompound(i).getInt("lvl");
                Registry.ENCHANTMENT.getOptional(ResourceLocation.tryParse(string)).ifPresent((enchantment)->
                {
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
    public interface Consumer
    {
        void accept(Enchantment enchantment, ItemStack stack, int level);
    }
}
