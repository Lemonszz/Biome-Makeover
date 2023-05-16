package party.lemons.biomemakeover.item;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import party.lemons.taniwha.item.totem.TotemItem;

public class EnchantedTotemItem extends Item implements TotemItem
{
    public EnchantedTotemItem(Properties properties)
    {
        super(properties);
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return true;
    }

    @Override
    public boolean canActivate(LivingEntity entity)
    {
        return true;
    }

    @Override
    public void activateTotem(LivingEntity entity, ItemStack stack)
    {
        if (stack != null) {
            if (entity instanceof ServerPlayer) {
                ((ServerPlayer)entity).awardStat(Stats.ITEM_USED.get(this));
                CriteriaTriggers.USED_TOTEM.trigger((ServerPlayer)entity, stack);
            }

            entity.setHealth(entity.getMaxHealth() / 2F);
            entity.removeAllEffects();
            entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 500, 1));
            entity.addEffect(new MobEffectInstance(MobEffects.ABSORPTION, 1200, 3));
            entity.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 2000, 0));
            entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 2000, 0));
            entity.level().broadcastEntityEvent(entity, (byte)35);
        }
    }
}