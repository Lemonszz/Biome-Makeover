package party.lemons.biomemakeover.item.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.phys.Vec3;
import party.lemons.biomemakeover.BMConfig;

import java.util.function.Supplier;

public class DepthsCurseEnchantment extends TickableAttributeEnchantment {
    public DepthsCurseEnchantment(Supplier<BMConfig.EnchantConfig> config) {
        super(config, true, Rarity.UNCOMMON, EnchantmentCategory.ARMOR_FEET, new EquipmentSlot[]{EquipmentSlot.FEET});
    }

    @Override
    public void onTick(LivingEntity entity, ItemStack stack, int level)
    {
        if(entity instanceof Player player)
            if(player.getAbilities().flying)
                return;

        if(entity.isInWaterOrBubble())
        {
            Vec3 vel = entity.getDeltaMovement();
            if(vel.y > -1F)
            {
                double max = 0.05 * level;
                double yV = Math.max(-max, vel.y - max);
                entity.setDeltaMovement(vel.x, yV, vel.z);
                entity.hasImpulse = true;
                entity.hurtMarked = true;
            }
        }
    }
}
