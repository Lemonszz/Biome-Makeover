package party.lemons.biomemakeover.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.enchantments.BMEnchantment;
import party.lemons.biomemakeover.util.ItemStackUtil;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity
{
	@Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

	@Inject(at = @At("TAIL"), method = "tick")
	public void tick(CallbackInfo cbi)
	{
		if(!getEntityWorld().isClient())
		{
			for(EquipmentSlot slot : EquipmentSlot.values())
			{
				ItemStack stack = getEquippedStack(slot);
				if(!stack.isEmpty())
				{
					ItemStackUtil.forEachEnchantment((en, st, lvl)->
					{
						if(en instanceof BMEnchantment)
						{
							((BMEnchantment) en).onTick(this, st, lvl);
						}
					}, stack);
				}
			}
		}
	}

	protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world)
	{
		super(entityType, world);
	}
}
