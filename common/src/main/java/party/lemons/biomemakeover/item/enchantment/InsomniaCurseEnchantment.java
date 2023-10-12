package party.lemons.biomemakeover.item.enchantment;

import net.minecraft.stats.Stats;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import party.lemons.biomemakeover.BMConfig;

import java.util.function.Supplier;

public class InsomniaCurseEnchantment extends TickableAttributeEnchantment
{
    public InsomniaCurseEnchantment(Supplier<BMConfig.EnchantConfig> config) {
        super(config, true, Rarity.UNCOMMON, EnchantmentCategory.ARMOR, new EquipmentSlot[]{EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET});
    }

    @Override
    public void onTick(LivingEntity entity, ItemStack stack, int level)
    {
        if(entity instanceof Player player)
        {
            if(!player.isSleeping())
            {
                player.awardStat(Stats.TIME_SINCE_REST, level);
            }
        }
    }
}
