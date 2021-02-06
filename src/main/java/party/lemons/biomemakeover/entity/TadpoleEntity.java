package party.lemons.biomemakeover.entity;

import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.init.BMItems;

public class TadpoleEntity extends FishEntity
{
	private AttributeContainer attributeContainer;
	private int babyTime = -6000;

	public TadpoleEntity(World world)
	{
		super(BMEntities.TADPOLE, world);
	}

	@Override
	public void tick()
	{
		super.tick();
		if(!world.isClient())
		{
			babyTime++;
			if(!isBaby())
			{
				ToadEntity toad = BMEntities.TOAD.create(world);
				toad.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), bodyYaw, pitch);
				toad.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 200, 0));
				((ServerWorld) world).spawnEntityAndPassengers(toad);

				remove();
			}
		}
	}

	public ActionResult interactMob(PlayerEntity player, Hand hand)
	{
		ItemStack itemStack = player.getStackInHand(hand);
		if(!itemStack.isEmpty() && itemStack.getItem() == BMItems.DRAGONFLY_WINGS)
		{
			if(this.isBaby())
			{
				itemStack.decrement(1);
				this.growUp(Math.abs(babyTime / 20));
				return ActionResult.success(this.world.isClient);
			}

			if(this.world.isClient)
			{
				return ActionResult.CONSUME;
			}
		}

		return super.interactMob(player, hand);
	}

	public void growUp(int age)
	{
		babyTime += age;
	}

	@Override
	public void setBaby(boolean baby)
	{
		babyTime = baby ? -24000 : 0;
	}

	@Override
	public boolean isBaby()
	{
		return babyTime < 0;
	}

	@Override
	public void writeCustomDataToTag(CompoundTag tag)
	{
		super.writeCustomDataToTag(tag);
		tag.putInt("BabyTime", babyTime);
	}

	@Override
	public void readCustomDataFromTag(CompoundTag tag)
	{
		super.readCustomDataFromTag(tag);
		if(tag.contains("BabyTime")) babyTime = tag.getInt("BabyTime");
	}

	@Override
	protected ItemStack getFishBucketItem()
	{
		return new ItemStack(BMItems.TADPOLE_BUCKET);
	}

	@Override
	protected SoundEvent getFlopSound()
	{
		return SoundEvents.ENTITY_TROPICAL_FISH_FLOP;
	}

	@Override
	public AttributeContainer getAttributes()
	{
		if(attributeContainer == null)
			attributeContainer = new AttributeContainer(MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D).build());
		return attributeContainer;
	}
}
