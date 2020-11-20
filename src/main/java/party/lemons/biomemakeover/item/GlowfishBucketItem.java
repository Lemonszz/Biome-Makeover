package party.lemons.biomemakeover.item;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.FishBucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import party.lemons.biomemakeover.init.BMCriterion;

public class GlowfishBucketItem extends FishBucketItem
{
	public GlowfishBucketItem(EntityType<?> type, Fluid fluid, Settings settings)
	{
		super(type, fluid, settings);
	}

	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
	{
		TypedActionResult<ItemStack> res = super.use(world, user, hand);

		if(!world.isClient() && res.getResult().isAccepted())
		{
			BMCriterion.GLOWFISH_SAVE.trigger((ServerPlayerEntity) user);
		}

		return res;
	}
}
