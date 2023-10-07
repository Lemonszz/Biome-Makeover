package party.lemons.biomemakeover.item;

import com.google.common.collect.Lists;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import party.lemons.biomemakeover.BMConfig;
import party.lemons.biomemakeover.init.BMEnchantments;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Cursing
{
    private static final List<Enchantment> curses = Lists.newArrayList();

    public static boolean isValidForCurse(ItemStack stack)
    {
        if(stack.isEmpty() || stack.getItem() == Items.ENCHANTED_BOOK) return false;

        if(stack.getItem() == Items.BOOK) return true;

        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        if(enchantments.isEmpty() || (stack.hasTag() && stack.getTag().contains("BMCursed"))) return false;

        boolean hasNewCompatibleCurse = false;
        for(Enchantment enchantment : enchantments.keySet())
        {
            if(!BuiltInRegistries.ENCHANTMENT.wrapAsHolder(enchantment).is(BMEnchantments.ALTAR_CANT_UPGRADE) && enchantment.getMaxLevel() > 1 && !enchantment.isCurse() && (!BMConfig.INSTANCE.strictAltarCursing || enchantments.get(enchantment) < enchantment.getMaxLevel() + 1)) return true;

            if(enchantment.isCurse() && enchantment.canEnchant(stack) && !enchantments.containsKey(enchantment))
                hasNewCompatibleCurse = true;
        }
        return hasNewCompatibleCurse;
    }

    public static Enchantment getRandomCurse(RegistryAccess registryAccess, RandomSource random)
    {
        if(curses.isEmpty())
        {
            curses.addAll(registryAccess.registryOrThrow(Registries.ENCHANTMENT).stream().filter(e->e.isCurse() && !BuiltInRegistries.ENCHANTMENT.wrapAsHolder(e).is(BMEnchantments.ALTAR_CURSE_EXCLUDED)).toList());
        }
        if(curses.isEmpty())
            return null;

        return curses.get(random.nextInt(curses.size()));
    }

    public static ItemStack curseItemStack(Level level, ItemStack stack, RandomSource random)
    {
        if(isValidForCurse(stack))
        {
            if(stack.getItem() == Items.BOOK)
            {
                ItemStack newStack = new ItemStack(Items.ENCHANTED_BOOK);
                Enchantment curse = Cursing.getRandomCurse(level.registryAccess(), level.random);
                if(curse == null)
                    return ItemStack.EMPTY;
                EnchantedBookItem.addEnchantment(newStack, new EnchantmentInstance(curse, 1));

                return newStack;
            }

            Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
            List<Enchantment> validEnchants = enchantments.keySet().stream().filter(
                    e -> !BuiltInRegistries.ENCHANTMENT.wrapAsHolder(e).is(BMEnchantments.ALTAR_CANT_UPGRADE) && e.getMaxLevel() > 1 && !e.isCurse() && (!BMConfig.INSTANCE.strictAltarCursing || enchantments.get(e) < e.getMaxLevel() + 1)
            ).toList();

            Enchantment toUpgrade = validEnchants.get(random.nextInt(validEnchants.size()));
            enchantments.put(toUpgrade, enchantments.get(toUpgrade) + 1);

            Enchantment curse = getRandomCurse(level.registryAccess(), random);
            if(curse == null)
                return ItemStack.EMPTY;

            int attempts = 0;   //Attempts is to stop a potential infinite loop, if code is up to this point we SHOULD have a curse that's compatible, we gonna brute force it at this point lol
            while(enchantments.containsKey(curse) || !curse.canEnchant(stack))
            {
                curse = getRandomCurse(level.registryAccess(), random);
                if(curse == null)
                    return ItemStack.EMPTY;

                attempts++;
                if(attempts >= 100)
                {
                    curse = null;
                    break;
                }
            }

            //Brute force tactic
            if(curse == null)
            {
                for(Enchantment enchantment : level.registryAccess().registryOrThrow(Registries.ENCHANTMENT).stream().sorted((e, e1)-> RandomUtil.randomRange(-1, 1)).collect(Collectors.toList()))
                {
                    if(enchantment.isCurse() && enchantment.canEnchant(stack) && !enchantments.containsKey(enchantment))
                        curse = enchantment;
                }
            }

            if(curse == null)
                return ItemStack.EMPTY;

            int curseLevel = curse.getMaxLevel() == 1 ? 1 : RandomUtil.randomRange(curse.getMinLevel(), curse.getMaxLevel());
            enchantments.put(curse, curseLevel);
            CompoundTag tag = stack.getOrCreateTag();
            tag.putBoolean("BMCursed", true);
            stack.setTag(tag);
            stack.setRepairCost(39);

            EnchantmentHelper.setEnchantments(enchantments, stack);
            return stack;
        }

        return ItemStack.EMPTY;
    }
}
