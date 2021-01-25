package party.lemons.biomemakeover.mixin;

import com.google.common.collect.Lists;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Pair;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.enchantments.BMEnchantment;
import party.lemons.biomemakeover.util.ItemStackUtil;

import java.util.Collection;
import java.util.Iterator;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity
{
	@Shadow public abstract ItemStack getEquippedStack(EquipmentSlot slot);

	@Inject(at = @At("TAIL"), method = "tick")
	public void tick(CallbackInfo cbi)
	{
		if(!getEntityWorld().isClient())
		{
			Iterator<Pair<EquipmentSlot, ItemStack>> it = attributeStacks.iterator();
			while(it.hasNext())
			{
				Pair<EquipmentSlot, ItemStack> pair = it.next();
				ItemStack st = pair.getRight();
				if(!hasStackEquipInSlot(st, pair.getLeft()))
				{
					ItemStackUtil.forEachEnchantment((en, stack, lvl)->
					{
						if(en instanceof BMEnchantment)
						{
							((BMEnchantment) en).removeAttributes((LivingEntity)(Object)this, pair.getLeft());
						}
					}, st, true);
					it.remove();
				}
			}

			for(EquipmentSlot slot : EquipmentSlot.values())
			{
				ItemStack stack = getEquippedStack(slot);
				if(!stack.isEmpty())
				{
					ItemStackUtil.forEachEnchantment((en, st, lvl)->
					{
						if(en instanceof BMEnchantment)
						{
							((BMEnchantment) en).onTick((LivingEntity)(Object)this, st, lvl);
							if(!hasAttributeStack(st) && ((BMEnchantment)en).addAttributes((LivingEntity)(Object)this, st, slot, lvl))
							{
								attributeStacks.add(new Pair<>(slot, st));
							}
						}
					}, stack);
				}
			}
		}
	}

	private boolean hasStackEquipInSlot(ItemStack stack, EquipmentSlot slot)
	{
		return getEquippedStack(slot).equals(stack);
	}

	private Collection<Pair<EquipmentSlot, ItemStack>> attributeStacks = Lists.newArrayList();

	public boolean hasAttributeStack(ItemStack stack)
	{
		for(Pair<EquipmentSlot, ItemStack> pair : attributeStacks)
		{
			if(pair.getRight().equals(stack))
				return true;
		}
		return false;
	}

	protected LivingEntityMixin(EntityType<? extends LivingEntity> entityType, World world)
	{
		super(entityType, world);
	}
}
