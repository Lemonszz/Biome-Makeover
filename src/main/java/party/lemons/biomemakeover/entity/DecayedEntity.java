package party.lemons.biomemakeover.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetFinder;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.ai.pathing.SwimNavigation;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.passive.MerchantEntity;
import net.minecraft.entity.passive.TurtleEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ShieldItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.init.BMEnchantments;
import party.lemons.biomemakeover.init.BMEntities;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class DecayedEntity extends ZombieEntity
{
	private static final TrackedData<Boolean> SHIELD_DOWN = DataTracker.registerData(DecayedEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	private boolean targetingUnderwater;
	protected final SwimNavigation waterNavigation;
	protected final MobNavigation landNavigation;
	private AttributeContainer attributeContainer;
	private int shieldDisableTime = 0;
	private int shieldHealth = 30;
	private boolean isDummy = false;

	public DecayedEntity(World world)
	{
		super(BMEntities.DECAYED, world);
		this.moveControl = new DecayedMoveControl(this);
		this.setPathfindingPenalty(PathNodeType.WATER, 0.0F);
		this.waterNavigation = new SwimNavigation(this, world);
		this.landNavigation = new MobNavigation(this, world);
	}

	protected void initCustomGoals() {
		this.goalSelector.add(2, new SurfaceWanderGoal(this, 1.0D));
		this.goalSelector.add(1, new ZombieAttackGoal(this, 1.0D, false));
		this.goalSelector.add(5, new LeaveWaterGoal(this, 1.0D));
		this.goalSelector.add(6, new TargetAboveWaterGoal(this, 1.0D, this.world.getSeaLevel()));
		this.goalSelector.add(7, new WanderAroundGoal(this, 1.0D));
		this.targetSelector.add(1, (new RevengeGoal(this, DecayedEntity.class)).setGroupRevenge(ZombifiedPiglinEntity.class));
		this.targetSelector.add(2, new FollowTargetGoal(this, PlayerEntity.class, 0, true, false, (e)->true));
		this.targetSelector.add(3, new FollowTargetGoal(this, MerchantEntity.class, false));
		this.targetSelector.add(3, new FollowTargetGoal(this, IronGolemEntity.class, true));
		this.targetSelector.add(5, new FollowTargetGoal(this, TurtleEntity.class, 10, true, false, TurtleEntity.BABY_TURTLE_ON_LAND_FILTER));
	}

	@Override
	protected void initDataTracker()
	{
		super.initDataTracker();
		dataTracker.startTracking(SHIELD_DOWN, false);
	}

	@Override
	protected void mobTick()
	{
		super.mobTick();
		if(!isSubmergedInWater() && isAttacking() && !dataTracker.get(SHIELD_DOWN))
			setCurrentHand(Hand.OFF_HAND);
		else
			clearActiveItem();

		if(!world.isClient())
		{
			if(shieldDisableTime > 0)
			{
				shieldDisableTime--;
				if(shieldDisableTime <= 0)
				{
					dataTracker.set(SHIELD_DOWN, false);
				}
			}
			else if(shieldDisableTime > 0 && !dataTracker.get(SHIELD_DOWN))
			{
				dataTracker.set(SHIELD_DOWN, true);
			}
		}
	}

	@Override
	protected void damageShield(float amount)
	{
		if (world.isClient() || !(this.activeItemStack.getItem() instanceof ShieldItem))
			return;

		shieldHealth -= amount;
		if(shieldHealth <= 0)
		{
			clearActiveItem();
			setStackInHand(Hand.OFF_HAND, ItemStack.EMPTY);
			playSound(SoundEvents.ITEM_SHIELD_BREAK, 0.8F, 0.8F + this.world.random.nextFloat() * 0.4F);
		}
		else
		{
			this.playSound(SoundEvents.ITEM_SHIELD_BLOCK, 1.0F, 0.8F + this.world.random.nextFloat() * 0.4F);
			shieldDisableTime = Math.max((int)(13F + (amount * 3F)), shieldDisableTime);
		}
	}

	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CompoundTag entityTag)
	{
		entityData = super.initialize(world, difficulty, spawnReason, entityData, entityTag);

		setLeftHanded(random.nextBoolean());
		if (this.random.nextFloat() < 0.15F * (difficulty.getClampedLocalDifficulty() + 1)) {
			int armourLevel = 0;
			float stopChance = this.world.getDifficulty() == Difficulty.HARD ? 0.05F : 0.1F;
			if (this.random.nextFloat() < 0.2F) {
				++armourLevel;
			}

			if (this.random.nextFloat() < 0.2F) {
				++armourLevel;
			}

			if (this.random.nextFloat() < 0.2F) {
				++armourLevel;
			}

			boolean stop = true;
			EquipmentSlot[] slots = EquipmentSlot.values();
			int length = slots.length;

			for(int j = 0; j < length; ++j) {
				EquipmentSlot equipmentSlot = slots[j];
				if (equipmentSlot.getType() == EquipmentSlot.Type.ARMOR) {
					ItemStack itemStack = this.getEquippedStack(equipmentSlot);
					if (!stop && this.random.nextFloat() < stopChance) {
						break;
					}

					stop = false;
					if (itemStack.isEmpty())
					{
						Item item = getEquipmentForSlot(equipmentSlot, armourLevel);
						if (item != null) {
							ItemStack stack = new ItemStack(item);
							stack.addEnchantment(BMEnchantments.DECAY_CURSE, random.nextInt(4));
							this.equipStack(equipmentSlot, stack);
						}
					}
				}
			}
		}

		ItemStack shield = new ItemStack(Items.SHIELD);
		shield.addEnchantment(BMEnchantments.DECAY_CURSE, random.nextInt(4));
		this.equipStack(EquipmentSlot.OFFHAND, shield);
		return entityData;
	}

	@Override
	public AttributeContainer getAttributes()
	{
		if(attributeContainer == null)
			attributeContainer = new AttributeContainer(HostileEntity.createHostileAttributes()
					.add(EntityAttributes.GENERIC_FOLLOW_RANGE, 35.0D)
					.add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.23000000417232513D)
					.add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0D)
					.add(EntityAttributes.GENERIC_ARMOR, 2.0D)
					.add(EntityAttributes.ZOMBIE_SPAWN_REINFORCEMENTS).build());

		return attributeContainer;
	}

	public boolean hasShield()
	{
		for(Hand hand : Hand.values())
		{
			ItemStack st = getStackInHand(hand);
			if(!st.isEmpty() && st.getItem() == Items.SHIELD)
				return true;
		}

		return false;
	}

	public ItemStack getShieldStack()
	{
		for(Hand hand : Hand.values())
		{
			ItemStack st = getStackInHand(hand);
			if(!st.isEmpty() && st.getItem() == Items.SHIELD)
				return st;
		}
		return ItemStack.EMPTY;
	}

	protected boolean shouldBreakDoors() {
		return false;
	}

	protected SoundEvent getAmbientSound() {
		return this.isTouchingWater() ? BMEffects.DECAYED_AMBIENT_WATER : BMEffects.DECAYED_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource source) {
		return this.isTouchingWater() ? BMEffects.DECAYED_HURT_WATER : BMEffects.DECAYED_HURT;
	}

	protected SoundEvent getDeathSound() {
		return this.isTouchingWater() ? BMEffects.DECAYED_DEATH_WATER : BMEffects.DECAYED_DEATH;
	}

	protected SoundEvent getStepSound() {
		return BMEffects.DECAYED_STEP;
	}

	protected SoundEvent getSwimSound() {
		return BMEffects.DECAYED_SWIM;
	}

	protected ItemStack getSkull() {
		return ItemStack.EMPTY;
	}

	protected boolean canConvertInWater() {
		return false;
	}

	public boolean canSpawn(WorldView world) {
		return world.intersectsEntities(this);
	}

	public boolean canFly() {
		return !this.isSwimming();
	}

	private boolean isTargetingUnderwater() {
		if (this.targetingUnderwater) {
			return true;
		} else {
			LivingEntity livingEntity = this.getTarget();
			return livingEntity != null && livingEntity.isTouchingWater();
		}
	}

	public void travel(Vec3d movementInput) {
		if (this.canMoveVoluntarily() && this.isTouchingWater() && this.isTargetingUnderwater()) {
			this.updateVelocity(0.05F, movementInput);
			this.move(MovementType.SELF, this.getVelocity());
			this.setVelocity(this.getVelocity().multiply(0.9D));
		} else {
			super.travel(movementInput);
		}

	}

	@Override
	@Environment(EnvType.CLIENT)
	public float getLeaningPitch(float tickDelta)
	{
		if(isDummy())
			return 3F;

		return super.getLeaningPitch(tickDelta);
	}

	public void updateSwimming() {
		if (!this.world.isClient) {
			if(isTouchingWater())
				stepHeight = 1;
			else
				stepHeight = 0.5F;

			if (this.canMoveVoluntarily() && this.submergedInWater && this.isTargetingUnderwater()) {
				this.navigation = this.waterNavigation;
				this.setSwimming(true);
			} else {
				this.navigation = this.landNavigation;
				this.setSwimming(false);
			}
		}

	}

	public void setDummy()
	{
		this.isDummy = true;
	}

	public boolean isDummy()
	{
		return isDummy;
	}

	public void setTargetingUnderwater(boolean targetingUnderwater) {
		this.targetingUnderwater = targetingUnderwater;
	}

	@Override
	public void writeCustomDataToTag(CompoundTag tag)
	{
		super.writeCustomDataToTag(tag);
		tag.putInt("ShieldHealth", shieldHealth);
	}

	@Override
	public void readCustomDataFromTag(CompoundTag tag)
	{
		super.readCustomDataFromTag(tag);
		if(tag.contains("ShieldHealth"))
			shieldHealth = tag.getInt("ShieldHealth");
	}

	public static boolean validSpawn(EntityType<?> type, ServerWorldAccess world, SpawnReason spawnReason, BlockPos pos, Random random) {
		boolean validReasons = world.getDifficulty() != Difficulty.PEACEFUL && isSpawnDark(world, pos, random) && (spawnReason == SpawnReason.SPAWNER || world.getFluidState(pos).isIn(FluidTags.WATER));
		return validReasons && pos.getY() <= world.getSeaLevel();
	}


	static class DecayedMoveControl extends MoveControl {
		private final DecayedEntity decayed;

		public DecayedMoveControl(DecayedEntity decayed) {
			super(decayed);
			this.decayed = decayed;
		}

		public void tick() {
			LivingEntity livingEntity = this.decayed.getTarget();
			if (this.decayed.isTargetingUnderwater() && this.decayed.isTouchingWater()) {
				if (livingEntity != null && livingEntity.getY() > this.decayed.getY() || this.decayed.targetingUnderwater) {
					this.decayed.setVelocity(this.decayed.getVelocity().add(0.0D, 0.002D, 0.0D));
				}

				if (this.state != MoveControl.State.MOVE_TO || this.decayed.getNavigation().isIdle()) {
					this.decayed.setMovementSpeed(0.0F);
					return;
				}

				double d = this.targetX - this.decayed.getX();
				double e = this.targetY - this.decayed.getY();
				double f = this.targetZ - this.decayed.getZ();
				double g = MathHelper.sqrt(d * d + e * e + f * f);
				e /= g;
				float h = (float)(MathHelper.atan2(f, d) * 57.2957763671875D) - 90.0F;
				this.decayed.yaw = this.changeAngle(this.decayed.yaw, h, 90.0F);
				this.decayed.bodyYaw = this.decayed.yaw;
				float i = (float)(this.speed * this.decayed.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
				float j = MathHelper.lerp(0.125F, this.decayed.getMovementSpeed(), i);
				this.decayed.setMovementSpeed(j);
				this.decayed.setVelocity(this.decayed.getVelocity().add((double)j * d * 0.005D, (double)j * e * 0.1D, (double)j * f * 0.005D));
			} else {
				if (!this.decayed.onGround) {
					this.decayed.setVelocity(this.decayed.getVelocity().add(0.0D, -0.008D, 0.0D));
				}

				super.tick();
			}

		}
	}


	static class SurfaceWanderGoal extends WanderAroundGoal
	{
		public SurfaceWanderGoal(PathAwareEntity mob, double speed)
		{
			super(mob, speed);
		}

		@Override
		public boolean canStop()
		{
			return !mob.isTouchingWater() && super.canStop();
		}

		@Override
		public boolean shouldContinue()
		{
			return !mob.isTouchingWater() && super.shouldContinue();
		}
	}

	static class LeaveWaterGoal extends MoveToTargetPosGoal
	{
		private final DecayedEntity decayed;

		public LeaveWaterGoal(DecayedEntity decayed, double speed) {
			super(decayed, speed, 8, 2);
			this.decayed = decayed;
		}

		public boolean canStart() {
			return super.canStart() && !this.decayed.world.isDay() && this.decayed.isTouchingWater() && this.decayed.getY() >= (double)(this.decayed.world.getSeaLevel() - 3);
		}

		public boolean shouldContinue() {
			return super.shouldContinue();
		}

		protected boolean isTargetPos(WorldView world, BlockPos pos) {
			BlockPos blockPos = pos.up();
			return world.isAir(blockPos) && world.isAir(blockPos.up()) && world.getBlockState(pos).hasSolidTopSurface(world, pos, this.decayed);
		}

		public void start() {
			this.decayed.setTargetingUnderwater(false);
			this.decayed.navigation = this.decayed.landNavigation;
			super.start();
		}

		public void stop() {
			super.stop();
		}
	}

	static class TargetAboveWaterGoal extends Goal {
		private final DecayedEntity decayed;
		private final double speed;
		private final int minY;
		private boolean foundTarget;

		public TargetAboveWaterGoal(DecayedEntity decayed, double speed, int minY) {
			this.decayed = decayed;
			this.speed = speed;
			this.minY = minY;
		}

		public boolean canStart() {
			return !this.decayed.world.isDay() && this.decayed.isTouchingWater() && this.decayed.getY() < (double)(this.minY - 2);
		}

		public boolean shouldContinue() {
			return this.canStart() && !this.foundTarget;
		}

		public void tick() {
			if (this.decayed.getY() < (double)(this.minY - 1) && (this.decayed.getNavigation().isIdle())) {
				Vec3d vec3d = TargetFinder.findTargetTowards(this.decayed, 4, 8, new Vec3d(this.decayed.getX(), (double)(this.minY - 1), this.decayed.getZ()));
				if (vec3d == null) {
					this.foundTarget = true;
					return;
				}

				this.decayed.getNavigation().startMovingTo(vec3d.x, vec3d.y, vec3d.z, this.speed);
			}

		}

		public void start() {
			this.decayed.setTargetingUnderwater(true);
			this.foundTarget = false;
		}

		public void stop() {
			this.decayed.setTargetingUnderwater(false);
		}
	}
}
