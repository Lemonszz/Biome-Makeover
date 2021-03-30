package party.lemons.biomemakeover.item;

import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.stat.Stats;
import org.apache.logging.log4j.core.jmx.Server;
import party.lemons.biomemakeover.util.TotemItem;

public class EnchantedTotemItem extends Item implements TotemItem
{
	public EnchantedTotemItem(Settings settings)
	{
		super(settings);
	}

	@Override
	public boolean hasGlint(ItemStack stack)
	{
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
			if (entity instanceof ServerPlayerEntity) {
				((ServerPlayerEntity)entity).incrementStat(Stats.USED.getOrCreateStat(Items.TOTEM_OF_UNDYING));
				Criteria.USED_TOTEM.trigger((ServerPlayerEntity)entity, stack);
			}

			entity.setHealth(entity.getMaxHealth() / 2F);
			entity.clearStatusEffects();
			entity.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 500, 1));
			entity.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 1200, 3));
			entity.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 2000, 0));
			entity.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 2000, 0));
			entity.world.sendEntityStatus(entity, (byte)35);
		}
	}
}
