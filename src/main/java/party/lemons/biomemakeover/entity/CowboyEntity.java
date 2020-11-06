package party.lemons.biomemakeover.entity;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PatrolEntity;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.entity.mob.SpellcastingIllagerEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import party.lemons.biomemakeover.entity.ai.LookAtTargetGoal;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.init.BMItems;

public class CowboyEntity extends PillagerEntity
{
	private AttributeContainer attributeContainer;

	public CowboyEntity(World world)
	{
		super(BMEntities.COWBOY, world);
	}

	@Override
	protected void initGoals() {
		this.goalSelector.add(4, new PatrolEntity.PatrolGoal<>(this, 1D, 0.8D));
		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(2, new RaiderEntity.PatrolApproachGoal(this, 10.0F));
		this.goalSelector.add(3, new CrossbowAttackGoal<>(this, 1.0D, 16.0F));
		this.goalSelector.add(8, new WanderAroundGoal(this, 0.6D));
		this.goalSelector.add(4, new LookAtTargetGoal(this));
		this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 15.0F, 1.0F));
		this.goalSelector.add(10, new LookAtEntityGoal(this, MobEntity.class, 15.0F));
		this.targetSelector.add(1, (new RevengeGoal(this, new Class[]{RaiderEntity.class})).setGroupRevenge());
		this.targetSelector.add(2, new FollowTargetGoal<>(this, PlayerEntity.class, true));
		this.targetSelector.add(3, new FollowTargetGoal<>(this, MerchantEntity.class, false));
		this.targetSelector.add(3, new FollowTargetGoal<>(this, IronGolemEntity.class, true));

	}

	@Override
	public void tick()
	{
		super.tick();

		if(getVehicle() != null)
			getVehicle().yaw = yaw;
	}

	@Override
	protected void initEquipment(LocalDifficulty difficulty) {
		super.initEquipment(difficulty);

		this.equipStack(EquipmentSlot.HEAD, new ItemStack(BMItems.COWBOY_HAT));
	}

	@Override
	public AttributeContainer getAttributes()
	{
		if(attributeContainer == null)
			attributeContainer =  new AttributeContainer(
					PillagerEntity.createPillagerAttributes().build());
		return attributeContainer;
	}
}
