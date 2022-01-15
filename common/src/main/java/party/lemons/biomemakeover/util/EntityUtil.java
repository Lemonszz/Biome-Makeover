package party.lemons.biomemakeover.util;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.Containers;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableInt;
import party.lemons.biomemakeover.init.BMEntities;

import java.util.Collection;

public final class EntityUtil
{
    public static void applyProjectileResistance(Iterable<ItemStack> equipment, MutableInt resistance)
    {
        MutableInt slotIndex = new MutableInt(0);
        equipment.forEach(e->{
            if(!e.isEmpty())
            {
                EquipmentSlot slot = EquipmentSlot.values()[2 + slotIndex.getValue()];
                if(e.getAttributeModifiers(slot).containsKey(BMEntities.ATT_PROJECTILE_RESISTANCE))
                {
                    Collection<AttributeModifier> modifiers = e.getAttributeModifiers(slot).get(BMEntities.ATT_PROJECTILE_RESISTANCE);
                    for(AttributeModifier mod : modifiers)
                    {
                        resistance.add(mod.getAmount());
                    }
                }
            }
            slotIndex.add(1);
        });
    }

    public static double getProjectileResistance(LivingEntity e)
    {
        double res = 0;
        for(EquipmentSlot slot : EquipmentSlot.values())
        {
            ItemStack st = e.getItemBySlot(slot);
            if(!st.isEmpty() && st.getAttributeModifiers(slot).containsKey(BMEntities.ATT_PROJECTILE_RESISTANCE))
            {
                Collection<AttributeModifier> modifiers = st.getAttributeModifiers(slot).get(BMEntities.ATT_PROJECTILE_RESISTANCE);
                for(AttributeModifier mod : modifiers)
                {
                    res += mod.getAmount();
                }
            }
        }
        return res;
    }

    private EntityUtil()
    {
    }

    public static boolean attemptProjectileResistanceBlock(LivingEntity entity, DamageSource source)
    {
        if (source.isProjectile()) {
            double protection = EntityUtil.getProjectileResistance(entity);
            if (protection > 0D && (entity.getRandom().nextDouble() * 30D) < protection) {
                entity.playSound(SoundEvents.SHIELD_BLOCK, 1F, 0.8F + entity.getRandom().nextFloat() * 0.4F);
                return true;
            }
        }

        return false;
    }

    public static void scatterItemStack(Entity e, ItemStack stack)
    {
        Containers.dropItemStack(e.level, e.getX(), e.getY(), e.getZ(), stack);
    }
}
