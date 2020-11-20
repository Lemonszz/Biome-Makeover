package party.lemons.biomemakeover.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import party.lemons.biomemakeover.entity.BMBoatEntity;
import party.lemons.biomemakeover.util.boat.BoatType;

import java.util.List;
import java.util.function.Supplier;

public class BMBoatItem extends BMItem
{
	private Supplier<BoatType> type;

	public BMBoatItem(Supplier<BoatType> boatType, Item.Settings settings)
	{
		super(settings);
		this.type = boatType;
	}

	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand)
	{
		ItemStack itemStack = user.getStackInHand(hand);
		HitResult hitResult = Item.raycast(world, user, RaycastContext.FluidHandling.ANY);
		if (hitResult.getType() == HitResult.Type.MISS)
		{
			return TypedActionResult.pass(itemStack);
		}
		else
		{
			Vec3d vec3d = user.getRotationVec(1.0F);
			List<Entity> list = world.getOtherEntities(user, user.getBoundingBox().stretch(vec3d.multiply(5.0D)).expand(1.0D), EntityPredicates.EXCEPT_SPECTATOR.and(Entity::collides));
			if (!list.isEmpty())
			{
				Vec3d cameraPosVec = user.getCameraPosVec(1.0F);

				for(Entity entity : list)
				{
					Box box = entity.getBoundingBox().expand(entity.getTargetingMargin());
					if(box.contains(cameraPosVec))
					{
						return TypedActionResult.pass(itemStack);
					}
				}
			}

			if (hitResult.getType() == HitResult.Type.BLOCK) {
				BMBoatEntity boatEntity = createBoat(world, hitResult.getPos().x, hitResult.getPos().y, hitResult.getPos().z, user.yaw);
				if (!world.isSpaceEmpty(boatEntity, boatEntity.getBoundingBox().expand(-0.1D)))
				{
					return TypedActionResult.fail(itemStack);
				}
				else
					{
					if (!world.isClient)
					{
						world.spawnEntity(boatEntity);
						if (!user.abilities.creativeMode)
						{
							itemStack.decrement(1);
						}
					}

					user.incrementStat(Stats.USED.getOrCreateStat(this));
					return TypedActionResult.success(itemStack);
				}
			}
			else
			{
				return TypedActionResult.pass(itemStack);
			}
		}
	}

	public BMBoatEntity createBoat(World world, double x, double y, double z, float yaw)
	{
		BMBoatEntity boatEntity = new BMBoatEntity(world, x, y, z);
		boatEntity.setBoatType(type.get());
		boatEntity.yaw = yaw;
		return boatEntity;
	}
}
