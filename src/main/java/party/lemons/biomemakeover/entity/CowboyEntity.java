package party.lemons.biomemakeover.entity;

import net.minecraft.block.entity.BannerPattern;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PatrolEntity;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.raid.RaiderEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Formatting;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
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

		armorDropChances[0] = 0.25F;
	}

	@Override
	protected void initGoals()
	{
		this.goalSelector.add(4, new PatrolEntity.PatrolGoal<>(this, 1D, 0.8D));
		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(2, new RaiderEntity.PatrolApproachGoal(this, 10.0F));
		this.goalSelector.add(3, new CrossbowAttackGoal<>(this, 1.0D, 16.0F));
		this.goalSelector.add(8, new WanderAroundGoal(this, 0.6D));
		this.goalSelector.add(4, new LookAtTargetGoal(this));
		this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 15.0F, 1.0F));
		this.goalSelector.add(10, new LookAtEntityGoal(this, MobEntity.class, 15.0F));
		this.targetSelector.add(1, (new RevengeGoal(this, RaiderEntity.class)).setGroupRevenge());
		this.targetSelector.add(2, new FollowTargetGoal<>(this, PlayerEntity.class, true));
		this.targetSelector.add(3, new FollowTargetGoal<>(this, MerchantEntity.class, false));
		this.targetSelector.add(3, new FollowTargetGoal<>(this, IronGolemEntity.class, true));

	}

	@Override
	public void tick()
	{
		super.tick();

		if(getVehicle() != null) getVehicle().yaw = yaw;
	}

	@Override
	protected void initEquipment(LocalDifficulty difficulty)
	{
		super.initEquipment(difficulty);

		this.equipStack(EquipmentSlot.HEAD, new ItemStack(BMItems.COWBOY_HAT));
	}

	@Override
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CompoundTag entityTag)
	{
		EntityData data = super.initialize(world, difficulty, spawnReason, entityData, entityTag);
		if(this.isPatrolLeader())
		{
			this.equipStack(EquipmentSlot.HEAD, getOminousBanner());
			this.setEquipmentDropChance(EquipmentSlot.HEAD, 2.0F);
		}
		return data;
	}

	public static ItemStack getOminousBanner()
	{
		ItemStack itemStack = new ItemStack(Items.WHITE_BANNER);
		CompoundTag compoundTag = itemStack.getOrCreateSubTag("BlockEntityTag");
		ListTag listTag = (new BannerPattern.Patterns()).add(BannerPattern.RHOMBUS_MIDDLE, DyeColor.CYAN).add(BannerPattern.STRIPE_BOTTOM, DyeColor.RED).add(BannerPattern.HALF_HORIZONTAL, DyeColor.BROWN).add(BannerPattern.TRIANGLES_TOP, DyeColor.BLACK).add(BannerPattern.BORDER, DyeColor.BLACK).add(BannerPattern.CIRCLE_MIDDLE, DyeColor.LIGHT_GRAY).add(BannerPattern.STRIPE_MIDDLE, DyeColor.BROWN).toTag();
		compoundTag.put("Patterns", listTag);
		itemStack.addHideFlag(ItemStack.TooltipSection.ADDITIONAL);
		itemStack.setCustomName((new TranslatableText("block.minecraft.ominous_banner")).formatted(Formatting.GOLD));
		return itemStack;
	}

	@Override
	public AttributeContainer getAttributes()
	{
		if(attributeContainer == null)
			attributeContainer = new AttributeContainer(PillagerEntity.createPillagerAttributes().build());
		return attributeContainer;
	}
}
