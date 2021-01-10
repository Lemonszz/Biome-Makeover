package party.lemons.biomemakeover.item;

import net.minecraft.block.DispenserBlock;
import net.minecraft.block.dispenser.DispenserBehavior;
import net.minecraft.block.dispenser.ProjectileDispenserBehavior;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.thrown.ExperienceBottleEntity;
import net.minecraft.entity.projectile.thrown.PotionEntity;
import net.minecraft.item.ExperienceBottleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPointer;
import net.minecraft.util.math.Position;
import net.minecraft.world.World;
import party.lemons.biomemakeover.entity.LightningBottleEntity;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.init.BMEntities;

public class LightningBottleItem extends Item
{
	public LightningBottleItem(Settings settings)
	{
		super(settings);

		DispenserBlock.registerBehavior(this, (pointer, stack)->
				(new ProjectileDispenserBehavior()
				{
					protected ProjectileEntity createProjectile(World world, Position position, ItemStack stack) {
						return new LightningBottleEntity(world, position.getX(), position.getY(), position.getZ());
					}

					protected float getVariation() {
						return super.getVariation() * 0.5F;
					}

					protected float getForce() {
						return super.getForce() * 1.25F;
					}
				}).dispense(pointer, stack));
	}

	public boolean hasGlint(ItemStack stack) {
		return true;
	}

	public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
		ItemStack itemStack = user.getStackInHand(hand);
		world.playSound(null,
				user.getX(), user.getY(), user.getZ(),
				BMEffects.LIGHTNING_BOTTLE_THROW, SoundCategory.NEUTRAL,
				0.5F, 0.4F / (RANDOM.nextFloat() * 0.4F + 0.8F));

		if (!world.isClient)
		{
			LightningBottleEntity bottleEntity = new LightningBottleEntity(world, user);
			bottleEntity.setItem(itemStack);
			bottleEntity.setProperties(user, user.pitch, user.yaw, -20.0F, 0.7F, 1.0F);
			world.spawnEntity(bottleEntity);
		}
		user.getItemCooldownManager().set(this, 45);
		user.incrementStat(Stats.USED.getOrCreateStat(this));
		if (!user.abilities.creativeMode) {
			itemStack.decrement(1);
		}

		return TypedActionResult.success(itemStack, world.isClient());
	}
}
