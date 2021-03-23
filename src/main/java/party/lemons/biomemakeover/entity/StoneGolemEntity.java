package party.lemons.biomemakeover.entity;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.Durations;
import net.minecraft.entity.ai.control.BodyControl;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.UniversalAngerGoal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.IntRange;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.entity.ai.BetterCrossbowAttackGoal;
import party.lemons.biomemakeover.entity.ai.EmptyMobNavigation;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.util.sound.StoneGolemTurnSoundInstance;

import java.util.List;
import java.util.UUID;
import java.util.function.Predicate;

public class StoneGolemEntity extends GolemEntity implements CrossbowUser, Angerable, MultiPartEntity<EntityPart<StoneGolemEntity>>
{
	private static final TrackedData<Boolean> CHARGING = DataTracker.registerData(StoneGolemEntity.class, TrackedDataHandlerRegistry.BOOLEAN);;
	private static final TrackedData<Boolean> PLAYER_CREATED = DataTracker.registerData(StoneGolemEntity.class, TrackedDataHandlerRegistry.BOOLEAN);;
	private static final IntRange ANGER_TIME_RANGE = Durations.betweenSeconds(20, 39);;

	private AttributeContainer attributeContainer;
	private int angerTime;
	private int holdCooldown = 0;
	private UUID angryAt;
	private EntityPart<StoneGolemEntity> BODY = new EntityPart<>(this, 1.6F, 2F, 0, 0.5F, 0);
	private List<EntityPart<StoneGolemEntity>> parts = Lists.newArrayList(BODY);

	public StoneGolemEntity(World world)
	{
		super(BMEntities.STONE_GOLEM, world);
	}

	@Override
	protected EntityNavigation createNavigation(World world) {
		return new EmptyMobNavigation(this, world);
	}

	@Override
	protected BodyControl createBodyControl()
	{
		return new StoneGolemBodyControl(this);
	}

	@Override
	protected void initGoals()
	{
		super.initGoals();

		this.goalSelector.add(1, new BetterCrossbowAttackGoal<>(this, 1.0D, 24.0F));
		this.goalSelector.add(2, new LookAtEntityGoal(this, PlayerEntity.class, 5.0F, 1.0F));
		this.goalSelector.add(3, new LookAtEntityGoal(this, MobEntity.class, 5.0F));
		this.goalSelector.add(4, new LookAtEntityGoal(this, StoneGolemEntity.class, 10.0F));
		this.goalSelector.add(5, new LookAtEntityGoal(this, GolemEntity.class, 5.0F));

		this.targetSelector.add(1, new RevengeGoal(this));
		this.targetSelector.add(2, new FollowTargetGoal<>(this, MerchantEntity.class, false));
		this.targetSelector.add(3, new FollowTargetGoal<>(this, StoneGolemEntity.class, false));
		this.targetSelector.add(4, new FollowTargetGoal<>(this, IronGolemEntity.class, 10, true, false, this::shouldAngerAt));
		this.targetSelector.add(5, new FollowTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::shouldAngerAt));
		this.targetSelector.add(6, new FollowTargetGoal<>(this, MobEntity.class, 5, false, false, (livingEntity) -> {
			return livingEntity instanceof Monster && !(livingEntity instanceof CreeperEntity);
		}));
		this.targetSelector.add(4, new UniversalAngerGoal<>(this, false));
	}

	@Override
	protected void initDataTracker()
	{
		super.initDataTracker();
		dataTracker.startTracking(CHARGING,false);
		dataTracker.startTracking(PLAYER_CREATED,false);
	}

	@Override
	public AttributeContainer getAttributes()
	{
		if(attributeContainer == null)
			attributeContainer = new AttributeContainer(HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 60).build());
		return attributeContainer;
	}

	@Override
	protected ActionResult interactMob(PlayerEntity player, Hand hand)
	{
		if(isPlayerCreated())
		{
			ItemStack playerStack = player.getStackInHand(hand);

			if(!playerStack.isEmpty() && playerStack.getItem() == BMBlocks.CLADDED_STONE.asItem())
			{
				float currentHealth = this.getHealth();
				this.heal(15.0F);
				if(this.getHealth() == currentHealth)
				{
					return ActionResult.PASS;
				}
				else
				{
					float g = 1.0F + (this.random.nextFloat() - this.random.nextFloat()) * 0.2F;
					this.playSound(SoundEvents.ENTITY_IRON_GOLEM_REPAIR, 1.0F, g);
					if(!player.abilities.creativeMode)
					{
						playerStack.decrement(1);
					}

					return ActionResult.success(this.world.isClient);
				}
			}
			else if(isHolding() && holdCooldown <= 0)
			{
				if(!world.isClient())
					player.inventory.offerOrDrop(world, getEquippedStack(EquipmentSlot.MAINHAND));
				return ActionResult.SUCCESS;
			}
			else
			{
				if(!playerStack.isEmpty())
				{
					if(playerStack.getItem() == Items.CROSSBOW)
					{
						if(!world.isClient())
						{
							ItemStack newStack = playerStack.copy();
							newStack.setCount(1);
							equipStack(EquipmentSlot.MAINHAND, newStack);
							playerStack.decrement(1);
							holdCooldown++;
						}
						return ActionResult.SUCCESS;
					}
				}
			}
		}
		return super.interactMob(player, hand);
	}

	@Override
	public void tick()
	{
		super.tick();
		holdCooldown = 0;

		if(bodyYaw != prevBodyYaw && world.isClient())
		{
			playRotateSound();
		}
	}

	private boolean isHolding()
	{
		return !getEquippedStack(EquipmentSlot.MAINHAND).isEmpty();
	}

	public boolean isCharging() {
		return this.dataTracker.get(CHARGING);
	}

	@Override
	protected @Nullable SoundEvent getDeathSound()
	{
		return BMEffects.STONE_GOLEM_DEATH;
	}

	@Override
	protected @Nullable SoundEvent getHurtSound(DamageSource source)
	{
		return BMEffects.STONE_GOLEM_HURT;
	}

	@Override
	protected void initEquipment(LocalDifficulty difficulty) {
		this.equipStack(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
	}

	@Nullable
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, @Nullable EntityData entityData, @Nullable CompoundTag entityTag) {
		this.initEquipment(difficulty);
		this.updateEnchantments(difficulty);
		return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
	}

	@Override
	public void setCharging(boolean charging)
	{
		this.dataTracker.set(CHARGING, charging);
	}

	@Override
	public void shoot(LivingEntity target, ItemStack crossbow, ProjectileEntity projectile, float multiShotSpray) {
		this.shoot(this, target, projectile, multiShotSpray, 1.6F);
	}

	@Override
	public void tickMovement()
	{
		super.tickMovement();
		updateParts();
	}

	@Override
	public void postShoot()
	{
	}

	@Override
	public void takeKnockback(float f, double d, double e)
	{
	}

	@Override
	public int getLookYawSpeed()
	{
		return 2;
	}

	@Override
	public int getLookPitchSpeed()
	{
		return 20;
	}

	public boolean canUseRangedWeapon(RangedWeaponItem weapon) {
		return weapon == Items.CROSSBOW;
	}

	@Override
	public void attack(LivingEntity target, float pullProgress) {
		this.shoot(this, 1.6F);
	}

	public ItemStack getArrowType(ItemStack stack) {
		if (stack.getItem() instanceof RangedWeaponItem) {
			Predicate<ItemStack> predicate = ((RangedWeaponItem)stack.getItem()).getHeldProjectiles();
			ItemStack itemStack = RangedWeaponItem.getHeldProjectile(this, predicate);
			return itemStack.isEmpty() ? new ItemStack(Items.ARROW) : itemStack;
		} else {
			return ItemStack.EMPTY;
		}
	}

	@Override
	protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions)
	{
		return 2F;
	}

	@Override
	public boolean shouldAngerAt(LivingEntity entity)
	{
		if(EntityPredicates.EXCEPT_CREATIVE_SPECTATOR_OR_PEACEFUL.test(entity) && !isPlayerCreated())
			return true;

		return Angerable.super.shouldAngerAt(entity);
	}

	@Override
	public boolean canTarget(LivingEntity target)
	{
		if(isPlayerCreated())
		{
			if(target instanceof PlayerEntity || target instanceof MerchantEntity)
				return false;
			if(target instanceof IronGolemEntity && !((IronGolemEntity) target).isPlayerCreated())
				return false;
		}
		else
		{
			if(target instanceof HostileEntity)
				return false;
			if(target instanceof IronGolemEntity && ((IronGolemEntity) target).isPlayerCreated())
				return false;
		}

		if(target instanceof StoneGolemEntity)
			return ((StoneGolemEntity) target).isPlayerCreated() != isPlayerCreated();

		return super.canTarget(target);
	}

	public boolean isPlayerCreated()
	{
		return dataTracker.get(PLAYER_CREATED);
	}

	public void setPlayerCreated(boolean playerCreated)
	{
		dataTracker.set(PLAYER_CREATED, playerCreated);
	}

	protected int getNextAirUnderwater(int air) {
		return air;
	}

	public void writeCustomDataToTag(CompoundTag tag) {
		super.writeCustomDataToTag(tag);
		tag.putBoolean("PlayerCreated", this.isPlayerCreated());
		this.angerToTag(tag);
	}

	public void readCustomDataFromTag(CompoundTag tag) {
		super.readCustomDataFromTag(tag);
		this.setPlayerCreated(tag.getBoolean("PlayerCreated"));
		this.angerFromTag((ServerWorld)this.world, tag);
	}

	public boolean isCollidable() {
		return this.isAlive();
	}

	@Override
	public void setAngerTime(int ticks) {
		this.angerTime = ticks;
	}

	@Override
	public int getAngerTime() {
		return this.angerTime;
	}

	@Override
	public void setAngryAt(@Nullable UUID uuid) {
		this.angryAt = uuid;
	}

	@Override
	public UUID getAngryAt() {
		return this.angryAt;
	}

	@Override
	public void chooseRandomAngerTime() {
		this.setAngerTime(ANGER_TIME_RANGE.choose(this.random));
	}

	@Override
	public boolean damagePart(EntityPart<StoneGolemEntity> part, DamageSource source, float amount)
	{
		return this.damage(source, amount);
	}

	@Override
	public List<EntityPart<StoneGolemEntity>> getParts()
	{
		return parts;
	}

	@Override
	public Box getVisibilityBoundingBox()
	{
		return super.getVisibilityBoundingBox().expand(1F, 3F, 1F);
	}

	@Environment(EnvType.CLIENT)
	public IllagerEntity.State getState() {
		if (this.isCharging()) {
			return IllagerEntity.State.CROSSBOW_CHARGE;
		} else if (this.isHolding(Items.CROSSBOW)) {
			return IllagerEntity.State.CROSSBOW_HOLD;
		} else {
			return this.isAttacking() ? IllagerEntity.State.ATTACKING : IllagerEntity.State.NEUTRAL;
		}
	}

	@Environment(EnvType.CLIENT)
	private void playRotateSound()
	{
		boolean nulled = turnSound == null;
		if(nulled || turnSound.isDone())
		{
			if(!nulled)
				MinecraftClient.getInstance().getSoundManager().stop(turnSound);

			turnSound = new StoneGolemTurnSoundInstance(this);
			MinecraftClient.getInstance().getSoundManager().play(turnSound);
		}
		else if(!nulled)
			turnSound.tick();
	}

	@Environment(EnvType.CLIENT)
	private StoneGolemTurnSoundInstance turnSound = null;

	public IronGolemEntity.Crack getCrack() {
		return IronGolemEntity.Crack.from(this.getHealth() / this.getMaxHealth());
	}

	@Override
	public boolean damage(DamageSource source, float amount) {
		IronGolemEntity.Crack crack = this.getCrack();
		boolean bl = super.damage(source, amount);
		if (bl && this.getCrack() != crack) {
			this.playSound(SoundEvents.ENTITY_IRON_GOLEM_DAMAGE, 1.0F, 1.0F);
		}
		return bl;
	}

	@Override
	public double getMountedHeightOffset()
	{
		return 2F;
	}

	@Override
	public boolean canBeControlledByRider()
	{
		return false;
	}



	private static final class StoneGolemBodyControl extends BodyControl
	{
		private final MobEntity entity;
		private int activeTicks;
		private float lastHeadYaw;

		public StoneGolemBodyControl(MobEntity entity)
		{
			super(entity);
			this.entity = entity;
		}

		public void tick() {
			++this.activeTicks;
			this.rotateBody();
			this.rotateLook();

		}

		public void pushAwayFrom(Entity entity) {
		}

		private void rotateLook() {
			this.entity.bodyYaw = MathHelper.stepAngleTowards(this.entity.bodyYaw, this.entity.headYaw, (float)this.entity.getBodyYawSpeed());
		}

		private void rotateBody() {
			int i = this.activeTicks - 10;
			float f = MathHelper.clamp((float)i / 10.0F, 0.0F, 1.0F);
			float g = (float)this.entity.getBodyYawSpeed() * (1.0F - f);
			this.entity.bodyYaw = MathHelper.stepAngleTowards(this.entity.bodyYaw, this.entity.headYaw, g);
		}
	}
}
