package party.lemons.biomemakeover.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import party.lemons.biomemakeover.util.Stuntable;

public class StuntPowderItem extends Item
{
	public StuntPowderItem(Settings settings)
	{
		super(settings);
	}

	@Override
	public ActionResult useOnEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand)
	{
		if(entity instanceof PassiveEntity)
		{
			if(entity.isBaby())
			{
				if(!entity.world.isClient())
				{
					((Stuntable) entity).setStunted(true);
					return ActionResult.SUCCESS;
				}
				return ActionResult.CONSUME;
			}
		}
		return super.useOnEntity(stack, user, entity, hand);
	}
}
