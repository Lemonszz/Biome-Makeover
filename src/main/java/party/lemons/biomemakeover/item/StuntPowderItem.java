package party.lemons.biomemakeover.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.util.NetworkUtil;
import party.lemons.biomemakeover.util.extensions.Stuntable;

public class StuntPowderItem extends Item
{
	public StuntPowderItem(Settings settings)
	{
		super(settings);
	}

	public ActionResult stuntEntity(ItemStack stack, PlayerEntity user, LivingEntity entity, Hand hand)
	{
		if(entity instanceof Stuntable)
		{
			if(entity.isBaby() && !((Stuntable)entity).isStunted())
			{
				if(!entity.world.isClient())
				{
					((Stuntable) entity).setStunted(true);
					NetworkUtil.doEntityParticle(user.world, ParticleTypes.WARPED_SPORE, entity, 15, 0.2F);
					stack.decrement(1);
				}
				return ActionResult.SUCCESS;
			}
		}
		return super.useOnEntity(stack, user, entity, hand);
	}

}
