package party.lemons.biomemakeover.entity.adjudicator;

import net.minecraft.entity.EntityData;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.util.NetworkUtil;

public class AdjudicatorMimicEntity extends HostileEntity implements AdjudicatorStateProvider, RangedAttackMob
{
	private AttributeContainer attributes;

	public AdjudicatorMimicEntity(World world)
	{
		super(BMEntities.ADJUDICATOR_MIMIC, world);
	}

	@Override
	protected void initGoals()
	{
		super.initGoals();
		this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0D));
		this.goalSelector.add(4, new BowAttackGoal<>(this, 1.0D, 12, 15.0F));
		this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 8.0F));
		this.goalSelector.add(6, new LookAroundGoal(this));

		this.targetSelector.add(1, new RevengeGoal(this));
		this.targetSelector.add(2, new FollowTargetGoal<>(this, PlayerEntity.class, true));
	}

	@Override
	protected void initEquipment(LocalDifficulty difficulty)
	{
		super.initEquipment(difficulty);
		handDropChances[0] = 0;
		handDropChances[1] = 0;

		equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
	}

	@Override
	public @Nullable EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable CompoundTag entityTag)
	{
		initEquipment(difficulty);
		return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
	}

	@Override
	protected SoundEvent getAmbientSound()
	{
		return BMEffects.ADJUDICATOR_LAUGH;
	}

	@Override
	public void onDeath(DamageSource source)
	{
		playSound(BMEffects.ADJUDICATOR_NO,1F, 1F);
		playSound(BMEffects.ADJUDICATOR_LAUGH,0.25F, 1F);
		playSpawnEffects();
		remove();

		if(!world.isClient()) NetworkUtil.doBlockEnderParticles(world, getBlockPos(), 6);
	}

	@Override
	public void baseTick()
	{
		if(firstUpdate)
		{
			playSound(BMEffects.ADJUDICATOR_MIMIC, 1F, 1F);
		}

		super.baseTick();
	}

	@Override
	public AttributeContainer getAttributes()
	{
		if(attributes == null)
			attributes = new AttributeContainer(HostileEntity.createHostileAttributes()
						.add(EntityAttributes.GENERIC_MAX_HEALTH, 1F)
                        .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D)
                        .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0D)
                        .build());
		return attributes;
	}

	@Override
	public AdjudicatorState getState()
	{
		return AdjudicatorState.FIGHTING;
	}

	@Override
	public void attack(LivingEntity target, float pullProgress)
	{
		ItemStack itemStack = this.getArrowType(this.getStackInHand(ProjectileUtil.getHandPossiblyHolding(this, Items.BOW)));
		PersistentProjectileEntity persistentProjectileEntity = this.createArrowProjectile(itemStack, pullProgress);
		double d = target.getX() - this.getX();
		double e = target.getBodyY(0.3333333333333333D) - persistentProjectileEntity.getY();
		double f = target.getZ() - this.getZ();
		double g = MathHelper.sqrt(d * d + f * f);
		persistentProjectileEntity.setVelocity(d, e + g * 0.20000000298023224D, f, 1.6F, (float)(14 - this.world.getDifficulty().getId() * 4));
		this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
		this.world.spawnEntity(persistentProjectileEntity);
	}

	protected PersistentProjectileEntity createArrowProjectile(ItemStack arrow, float damageModifier) {
		return ProjectileUtil.createArrowProjectile(this, arrow, damageModifier);
	}
}
