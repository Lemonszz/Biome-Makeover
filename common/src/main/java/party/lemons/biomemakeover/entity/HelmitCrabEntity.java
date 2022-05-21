package party.lemons.biomemakeover.entity;

import com.google.common.collect.Lists;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.NodeEvaluator;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.entity.ai.PredicateTemptGoal;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.item.HatItem;

import java.util.EnumSet;
import java.util.List;

/*
    🦀🦀🦀🦀🦀🦀🦀🦀🦀🦀🦀🦀🦀🦀🦀🦀🦀🦀🦀🦀🦀🦀🦀🦀🦀🦀🦀🦀🦀🦀🦀🦀🦀🦀
 */
public class HelmitCrabEntity extends Animal
{
	public static final EntityDataAccessor<ItemStack> SHELL_ITEM = SynchedEntityData.defineId(HelmitCrabEntity.class, EntityDataSerializers.ITEM_STACK);
	public static final EntityDataAccessor<Boolean> HIDING = SynchedEntityData.defineId(HelmitCrabEntity.class, EntityDataSerializers.BOOLEAN);
	private boolean targetingUnderwater;
	private int shellChangeCooldown = 0;

	public HelmitCrabEntity(EntityType<? extends Animal> entityType, Level level) {
		super(BMEntities.HELMIT_CRAB.get(), level);

		setPathfindingMalus(BlockPathTypes.WATER, 0);
		setPathfindingMalus(BlockPathTypes.WATER_BORDER, 0);
		getNavigation().setCanFloat(true);

	}
	private int hideTime = 0;

	@Override
	protected void registerGoals()
	{
		this.goalSelector.addGoal(0, new HideGoal());
		this.goalSelector.addGoal(1, new EscapeIntoShellGoal(1.25D));
		this.goalSelector.addGoal(3,new BreedGoal(this, 1.0D));
		this.goalSelector.addGoal(4, new PredicateTemptGoal(this, 1.2D, (i)->i.is(ItemTags.FISHES), false));
		this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.1D));
		this.goalSelector.addGoal(6, new LeaveWaterGoal(1.0D));
		this.goalSelector.addGoal(7, new MeleeAttackGoal(this, 1, false));
		this.goalSelector.addGoal(8, new SeekShellGoal());
		this.goalSelector.addGoal(9, new WaterAvoidingRandomStrollGoal(this, 1.0D));
		this.goalSelector.addGoal(10, new LookAtPlayerGoal(this, Player.class, 6.0F));
		this.goalSelector.addGoal(11, new RandomLookAroundGoal(this));
	}

	@Override
	protected BodyRotationControl createBodyControl()
	{
		return new CrabBodyControl(this);
	}

	@Override
	protected void defineSynchedData()
	{
		getEntityData().define(SHELL_ITEM, ItemStack.EMPTY);
		getEntityData().define(HIDING, false);
		super.defineSynchedData();
	}

	@Override
	public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag)
	{
		if(isBaby())
			return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);

		if (this.random.nextFloat() < 0.6F)
		{
			int tier = 0;
			for(int i = 0; i < 4; i++)
			{
				if (this.random.nextFloat() < 0.05F) {
					++tier;
				}
			}

			ItemStack itemStack;
			switch (tier)
			{
				case 1:
					itemStack = new ItemStack(Items.LEATHER_HELMET);
					break;
				case 2:
					itemStack = new ItemStack(Items.GOLDEN_HELMET);
					break;
				case 3:
					itemStack = new ItemStack(Items.IRON_HELMET);
					break;
				case 4:
					itemStack = new ItemStack(Items.DIAMOND_HELMET);
					break;
				default:
					itemStack = new ItemStack(Items.NAUTILUS_SHELL);
					break;
			}
			if(random.nextFloat() < 0.05F)
				EnchantmentHelper.enchantItem(this.random, itemStack, (int)(5.0F + (float)this.random.nextInt(10)), false);

			if(itemStack.isDamageableItem())
				itemStack.setDamageValue(itemStack.getMaxDamage() - this.random.nextInt(1 + this.random.nextInt(Math.max(itemStack.getMaxDamage() - 3, 1))));

			getEntityData().set(SHELL_ITEM, itemStack); //Not using setShell as that sets cool down and plays sound.
		}

		return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
	}

	public void updateSwimming() {
		if (!this.level.isClientSide()) {
			if (this.isEffectiveAi() && this.isInWater() && this.isTargetingUnderwater()) {
				this.setSwimming(true);
			} else {
				this.setSwimming(false);
			}
		}
	}

	@Override
	public Iterable<ItemStack> getArmorSlots()
	{
		return Lists.newArrayList(getShellItemStack());
	}

	@Override
	public Iterable<ItemStack> getHandSlots()
	{
		return Lists.newArrayList(getShellItemStack());
	}

	private boolean isTargetingUnderwater() {
		if (this.targetingUnderwater) {
			return true;
		} else {
			LivingEntity livingEntity = this.getTarget();
			return livingEntity != null && livingEntity.isInWater();
		}
	}

	@Override
	protected void customServerAiStep() {
		super.customServerAiStep();

		if(shellChangeCooldown > 0)
			shellChangeCooldown--;

		if(isHiding())
		{
			hideTime++;
			if(hideTime > 250)
			{
				if(random.nextInt(100) == 0)
					setHiding(false);
			}
		}
	}

	@Override
	public boolean isFood(ItemStack stack)
	{
		return stack.is(ItemTags.FISHES);
	}

	public boolean prefersShell(ItemStack itemStack) {
		//Check if it's a valid shell
		if (itemStack.isEmpty() || !isValidShellItem(itemStack.getItem()))
			return false;

		ItemStack currentShell = getShellItemStack();
		if (EnchantmentHelper.hasBindingCurse(currentShell))   //If our shell has binding, we can't switch
		{
			return false;
		}

		if (currentShell.isEmpty())  //If not wearing a shell, a new one is always preferred
			return true;
		else if(currentShell.getItem() != Items.NAUTILUS_SHELL && itemStack.getItem() == Items.NAUTILUS_SHELL)  //Always swap for nautalis
			return true;
		else if(currentShell.getItem() != itemStack.getItem() && (itemStack.getItem() instanceof BlockItem && ((BlockItem)itemStack.getItem()).getBlock() instanceof AbstractSkullBlock))
			return true;
		else if(!(currentShell.getItem() instanceof ArmorItem) && itemStack.getItem() == Blocks.CARVED_PUMPKIN.asItem())
			return true;
		else if(currentShell.getItem() != Items.SHULKER_SHELL && itemStack.getItem() == Items.SHULKER_SHELL) //Take shulker shells as well
			return true;
		else if (itemStack.getItem() instanceof ArmorItem && ((ArmorItem) itemStack.getItem()).getSlot() == EquipmentSlot.HEAD) {
			if(currentShell.getItem() instanceof HatItem)  //If the current item is a hat, only swap if it's another hat
			{
				if(itemStack.getItem() instanceof HatItem)
					return currentShell.getItem() != itemStack.getItem();
				else
					return true;
			}
			else if (itemStack.getItem() instanceof HatItem) //If it's a hat, at this point we know we're not wearing a hat, so we want it
			{
				return true;
			}
			else if (!(currentShell.getItem() instanceof ArmorItem)) //If we're not wearing armor, we should swap
				return true;
			else {
				ArmorItem newShell = (ArmorItem) itemStack.getItem();
				ArmorItem current = (ArmorItem) currentShell.getItem();

				if (newShell.getDefense() != current.getDefense()) {  //If we've got more protection, choose that
					return newShell.getDefense() > current.getDefense();
				} else if (newShell.getToughness() != current.getToughness()) { //take best toughness
					return newShell.getToughness() > current.getToughness();
				} else {
					return this.prefersNewDamageableItem(itemStack, currentShell);  //Take less damaged
				}
			}
		}
		return false;
	}

	public boolean prefersNewDamageableItem(ItemStack newStack, ItemStack oldStack) {
		if (newStack.getDamageValue() >= oldStack.getDamageValue() && (!newStack.hasTag() || oldStack.hasTag())) {
			if (newStack.hasTag() && oldStack.hasTag()) {
				return newStack.getTag().getAllKeys().stream().anyMatch((string) -> !string.equals("Damage"))
						&& !oldStack.getTag().getAllKeys().stream().anyMatch((string) -> !string.equals("Damage"));
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag)
	{
		super.readAdditionalSaveData(tag);
		if(tag.contains("Shell"))
			getEntityData().set(SHELL_ITEM, ItemStack.of(tag.getCompound("Shell")));

		hideTime = tag.getInt("HideTime");
		shellChangeCooldown = tag.getInt("ShellCooldown");
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag)
	{
		super.addAdditionalSaveData(tag);
		tag.put("Shell", getEntityData().get(SHELL_ITEM).save(new CompoundTag()));
		tag.putInt("HideTime", hideTime);
		tag.putInt("ShellCooldown", shellChangeCooldown);
	}

	@Override
	public float getWalkTargetValue(BlockPos pos, LevelReader level)
	{
		BlockState block = level.getBlockState(pos.below());
		if(block.is(Blocks.SAND))
			return 10;
		else if(block.is(Blocks.WATER))
			return 7;
		else
			return level.getLightEmission(pos) - 0.5F;
	}


	public boolean isValidShellItem(Item item)
	{
		if(isBaby())
			return false;

		if(item instanceof ArmorItem && ((ArmorItem)item).getSlot() == EquipmentSlot.HEAD)
			return true;

		return item == Items.NAUTILUS_SHELL || item == Items.SHULKER_SHELL || (item instanceof BlockItem && ((BlockItem)item).getBlock() instanceof AbstractSkullBlock) || item == Blocks.CARVED_PUMPKIN.asItem();
	}

	public void setShellItem(ItemStack stack)
	{
		getEntityData().set(SHELL_ITEM, stack);

		if (!stack.isEmpty()) {
			SoundEvent soundEvent = SoundEvents.ARMOR_EQUIP_GENERIC;
			Item item = stack.getItem();
			if (item instanceof ArmorItem) {
				soundEvent = ((ArmorItem)item).getMaterial().getEquipSound();
			} else if (item == Items.ELYTRA) {
				soundEvent = SoundEvents.ARMOR_EQUIP_ELYTRA;
			}
			this.playSound(soundEvent, 1.0F, 1.0F);
		}
	}

	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes().add(Attributes.ATTACK_DAMAGE, 1F).add(Attributes.MAX_HEALTH, 10F).add(Attributes.MOVEMENT_SPEED, 0.25D);
	}

	@Override
	public ItemStack getItemBySlot(EquipmentSlot slot)
	{
		if(slot == EquipmentSlot.HEAD)
			return getEntityData().get(SHELL_ITEM);

		return super.getItemBySlot(slot);
	}

	@Nullable
	@Override
	public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob)
	{
		return BMEntities.HELMIT_CRAB.get().create(serverLevel);
	}


	@Override
	public boolean canBreatheUnderwater()
	{
		return true;
	}

	public boolean canHide()
	{
		return !getShellItemStack().isEmpty();
	}

	public ItemStack getShellItemStack()
	{
		return getEntityData().get(SHELL_ITEM);
	}

	public boolean isHiding()
	{
		return getEntityData().get(HIDING);
	}

	private void setHiding(boolean hiding)
	{
		getEntityData().set(HIDING, hiding);

		if(hiding)
		{
			getNavigation().stop();
			hideTime = 0;
			setTarget(null);
		}
	}

	public void travel(Vec3 vec3) {
		if (this.isEffectiveAi() || this.isControlledByLocalInstance()) {
			boolean bl;
			double d = 0.08;
			boolean bl2 = bl = this.getDeltaMovement().y <= 0.0;
			if (bl && this.hasEffect(MobEffects.SLOW_FALLING)) {
				d = 0.01;
				this.resetFallDistance();
			}
			FluidState fluidState = this.level.getFluidState(this.blockPosition());
			if (this.isInWater() && this.isAffectedByFluids() && !this.canStandOnFluid(fluidState)) {
				if(getNavigation().getTargetPos() != null && getNavigation().getTargetPos().getY() > getY())
					getJumpControl().jump();

				//Move towards sea floor rather than float slowly
				if(!onGround && !jumping && getDeltaMovement().y() > -0.1F)
					setDeltaMovement(getDeltaMovement().add(0, -0.025F, 0));

				double e = this.getY();
				float f = this.isSprinting() ? 0.9f : this.getWaterSlowDown();
				float g = 0.02f;
				float h = EnchantmentHelper.getDepthStrider(this);
				if (h > 3.0f) {
					h = 3.0f;
				}
				if (!this.onGround) {
					h *= 0.5f;
				}
				if (h > 0.0f) {
					f += (0.54600006f - f) * h / 3.0f;
					g += (this.getSpeed() - g) * h / 3.0f;
				}
				if (this.hasEffect(MobEffects.DOLPHINS_GRACE)) {
					f = 0.96f;
				}
				this.moveRelative(g, vec3);
				this.move(MoverType.SELF, this.getDeltaMovement());
				Vec3 vec32 = this.getDeltaMovement();
				if (this.horizontalCollision && this.onClimbable()) {
					vec32 = new Vec3(vec32.x, 0.2, vec32.z);
				}
				this.setDeltaMovement(vec32.multiply(f, 0.8f, f));
				Vec3 vec33 = this.getFluidFallingAdjustedMovement(d, bl, this.getDeltaMovement());
				this.setDeltaMovement(vec33);
				if (this.horizontalCollision && this.isFree(vec33.x, vec33.y + (double)0.6f - this.getY() + e, vec33.z)) {
					this.setDeltaMovement(vec33.x, 0.3f, vec33.z);
				}
			} else if (this.isInLava() && this.isAffectedByFluids() && !this.canStandOnFluid(fluidState)) {
				Vec3 vec34;
				double e = this.getY();
				this.moveRelative(0.02f, vec3);
				this.move(MoverType.SELF, this.getDeltaMovement());
				if (this.getFluidHeight(FluidTags.LAVA) <= this.getFluidJumpThreshold()) {
					this.setDeltaMovement(this.getDeltaMovement().multiply(0.5, 0.8f, 0.5));
					vec34 = this.getFluidFallingAdjustedMovement(d, bl, this.getDeltaMovement());
					this.setDeltaMovement(vec34);
				} else {
					this.setDeltaMovement(this.getDeltaMovement().scale(0.5));
				}
				if (!this.isNoGravity()) {
					this.setDeltaMovement(this.getDeltaMovement().add(0.0, -d / 4.0, 0.0));
				}
				vec34 = this.getDeltaMovement();
				if (this.horizontalCollision && this.isFree(vec34.x, vec34.y + (double)0.6f - this.getY() + e, vec34.z)) {
					this.setDeltaMovement(vec34.x, 0.3f, vec34.z);
				}
			} else if (this.isFallFlying()) {
				double n;
				float o;
				double m;
				Vec3 vec35 = this.getDeltaMovement();
				if (vec35.y > -0.5) {
					this.fallDistance = 1.0f;
				}
				Vec3 vec36 = this.getLookAngle();
				float f = this.getXRot() * ((float)Math.PI / 180);
				double i = Math.sqrt(vec36.x * vec36.x + vec36.z * vec36.z);
				double j = vec35.horizontalDistance();
				double k = vec36.length();
				double l = Math.cos(f);
				l = l * l * Math.min(1.0, k / 0.4);
				vec35 = this.getDeltaMovement().add(0.0, d * (-1.0 + l * 0.75), 0.0);
				if (vec35.y < 0.0 && i > 0.0) {
					m = vec35.y * -0.1 * l;
					vec35 = vec35.add(vec36.x * m / i, m, vec36.z * m / i);
				}
				if (f < 0.0f && i > 0.0) {
					m = j * (double)(-Mth.sin(f)) * 0.04;
					vec35 = vec35.add(-vec36.x * m / i, m * 3.2, -vec36.z * m / i);
				}
				if (i > 0.0) {
					vec35 = vec35.add((vec36.x / i * j - vec35.x) * 0.1, 0.0, (vec36.z / i * j - vec35.z) * 0.1);
				}
				this.setDeltaMovement(vec35.multiply(0.99f, 0.98f, 0.99f));
				this.move(MoverType.SELF, this.getDeltaMovement());
				if (this.horizontalCollision && !this.level.isClientSide && (o = (float)((n = j - (m = this.getDeltaMovement().horizontalDistance())) * 10.0 - 3.0)) > 0.0f) {
					this.playSound(o > 4 ? this.getFallSounds().big() : this.getFallSounds().small(), 1.0f, 1.0f);
					this.hurt(DamageSource.FLY_INTO_WALL, o);
				}
				if (this.onGround && !this.level.isClientSide) {
					this.setSharedFlag(7, false);
				}
			} else {
				BlockPos blockPos = this.getBlockPosBelowThatAffectsMyMovement();
				float p = this.level.getBlockState(blockPos).getBlock().getFriction();
				float f = this.onGround ? p * 0.91f : 0.91f;
				Vec3 vec37 = this.handleRelativeFrictionAndCalculateMovement(vec3, p);
				double q = vec37.y;
				if (this.hasEffect(MobEffects.LEVITATION)) {
					q += (0.05 * (double)(this.getEffect(MobEffects.LEVITATION).getAmplifier() + 1) - vec37.y) * 0.2;
					this.resetFallDistance();
				} else if (!this.level.isClientSide || this.level.hasChunkAt(blockPos)) {
					if (!this.isNoGravity()) {
						q -= d;
					}
				} else {
					q = this.getY() > (double)this.level.getMinBuildHeight() ? -0.1 : 0.0;
				}
				if (this.shouldDiscardFriction()) {
					this.setDeltaMovement(vec37.x, q, vec37.z);
				} else {
					this.setDeltaMovement(vec37.x * (double)f, q * (double)0.98f, vec37.z * (double)f);
				}
			}
		}
		this.calculateEntityAnimation(this, this instanceof FlyingAnimal);
	}

	@Override
	public MobType getMobType()
	{
		return MobType.ARTHROPOD;
	}

	public void setTargetingUnderwater(boolean targetingUnderwater) {
		this.targetingUnderwater = targetingUnderwater;
	}

	private class EscapeIntoShellGoal extends PanicGoal
	{
		private boolean isFire = false;
		boolean hasAttacked = false;
		boolean attacking = false;
		BlockPos attackTargetPos = null;

		public EscapeIntoShellGoal(double speed)
		{
			super(HelmitCrabEntity.this, speed);
		}

		@Override
		public boolean canUse()
		{
			if(isHiding())
				return false;

			if (this.mob.getLastHurtByMob() == null && !this.mob.isOnFire())
			{
				return false;
			}
			else
			{
				if (this.mob.isOnFire())
				{
					BlockPos blockPos = this.lookForWater(this.mob.level, this.mob, 5);
					if (blockPos != null) {
						this.posX = blockPos.getX();
						this.posY = blockPos.getY();
						this.posZ = blockPos.getZ();
						isFire = true;
						hasAttacked = true;
						return true;
					}
				}
				isFire = false;
				hasAttacked = false;
				return this.findRandomPosition();
			}
		}

		@Override
		public void start() {

			if(!isFire && mob.getLastHurtByMob() != null && distanceTo(mob.getLastHurtByMob()) < 4)
			{
				BlockPos attackerPos = mob.getLastHurtByMob().blockPosition();
				this.mob.getNavigation().moveTo(attackerPos.getX(), attackerPos.getY(), attackerPos.getZ(), this.speedModifier);
				this.isRunning = true;
				attacking = true;
				attackTargetPos = attackerPos;
			}
			else {
				attacking = false;
				super.start();
			}
		}

		@Override
		public void tick()
		{
			super.tick();

			if(isFire || !isRunning)
				return;

			if(attacking && mob.getLastHurtByMob() != null && mob.getLastHurtByMob().isAlive())
			{
				boolean inValidTargetRange = distanceTo(mob.getLastHurtByMob()) < 2F;
				if(mob.distanceToSqr(attackTargetPos.getX() + 0.5F, attackTargetPos.getY() + 0.5F, attackTargetPos.getZ() + 0.5F) < 1 || inValidTargetRange) {
					if(inValidTargetRange)
						doHurtTarget(mob.getLastHurtByMob());
					attacking = false;
					this.mob.getNavigation().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
				}
			}

			if(canHide() && isRunning && random.nextInt(40) == 0)
			{
				setHiding(true);
				stop();
			}
		}
	}


	private class HideGoal extends Goal
	{
		public HideGoal()
		{

			setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP, Flag.TARGET));
		}

		@Override
		public void tick()
		{
			getNavigation().stop();
			getMoveControl().setWantedPosition(getX(), getY(), getZ(), 100F);
			super.tick();
		}

		@Override
		public boolean canUse() {
			return isHiding();
		}

		@Override
		public boolean canContinueToUse()
		{
			return isHiding();
		}

		@Override
		public boolean isInterruptable() {
			return !isHiding();
		}
	}

	class LeaveWaterGoal extends MoveToBlockGoal {

		public LeaveWaterGoal(double speed) {
			super(HelmitCrabEntity.this, speed, 8, 2);
		}

		@Override
		public boolean canUse()
		{
			return super.canUse() && isInWater();
		}


		public void start() {
			setTargetingUnderwater(false);
			super.start();
		}

		@Override
		protected boolean isValidTarget(LevelReader level, BlockPos pos)
		{
			BlockPos blockPos = pos.above();
			return level.isEmptyBlock(blockPos) && level.isEmptyBlock(blockPos.above()) && level.getBlockState(pos).entityCanStandOn(level, pos, HelmitCrabEntity.this);
		}

	}

	private class SeekShellGoal extends Goal
	{
		private ItemEntity itemEntity = null;

		public SeekShellGoal()
		{
			setFlags(EnumSet.of(Flag.MOVE));
		}

		@Override
		public void tick() {
			super.tick();

			if(itemEntity.isAlive())
			{
				getMoveControl().setWantedPosition(itemEntity.getX(), itemEntity.getY(), itemEntity.getZ(), 1.2F);
				if(distanceTo(itemEntity) < 0.5F)
				{
					itemEntity.remove(RemovalReason.DISCARDED);
					if(!getShellItemStack().isEmpty())
						spawnAtLocation(getShellItemStack());
					setShellItem(itemEntity.getItem());
					shellChangeCooldown = 500 + random.nextInt(300);
				}
			}
		}

		@Override
		public boolean canUse()
		{
			if(isBaby() || shellChangeCooldown > 0)
				return false;

			seekShell();
			return itemEntity != null && !itemEntity.isRemoved();
		}


		private void seekShell()
		{
			List<ItemEntity> entities = level.getEntitiesOfClass(ItemEntity.class, getBoundingBox().inflate(5D), i-> prefersShell(i.getItem()));
			if(!entities.isEmpty())
			{
				itemEntity = entities.get(random.nextInt(entities.size()));
			}
		}

		@Override
		public boolean canContinueToUse()
		{
			return itemEntity != null && !itemEntity.isRemoved();
		}
	}


	private class CrabBodyControl extends BodyRotationControl
	{
		private final Mob mob;
		private static final int HEAD_STABLE_ANGLE = 15;
		private static final int DELAY_UNTIL_STARTING_TO_FACE_FORWARD = 10;
		private static final int HOW_LONG_IT_TAKES_TO_FACE_FORWARD = 10;
		private int headStableTime;
		private float lastStableYHeadRot;

		public CrabBodyControl(Mob mob) {
			super(mob);
			this.mob = mob;
		}

		public void clientTick() {
			if (this.isMoving()) {
				this.mob.yBodyRot = this.mob.getYRot() + 90F;
				this.rotateHeadIfNecessary();
				this.lastStableYHeadRot = this.mob.yHeadRot;
				this.headStableTime = 0;
				return;
			}
			if (this.notCarryingMobPassengers()) {
				if (Math.abs(this.mob.yHeadRot - this.lastStableYHeadRot) > HEAD_STABLE_ANGLE) {
					this.headStableTime = 0;
					this.lastStableYHeadRot = this.mob.yHeadRot;
					this.rotateBodyIfNecessary();
				} else {
					++this.headStableTime;
					if (this.headStableTime > DELAY_UNTIL_STARTING_TO_FACE_FORWARD) {
						this.rotateHeadTowardsFront();
					}
				}
			}
		}

		private void rotateBodyIfNecessary() {
			this.mob.yBodyRot = Mth.rotateIfNecessary(this.mob.yBodyRot, this.mob.yHeadRot, this.mob.getMaxHeadYRot());
		}

		private void rotateHeadIfNecessary() {
			this.mob.yHeadRot = Mth.rotateIfNecessary(this.mob.yHeadRot, this.mob.yBodyRot, this.mob.getMaxHeadYRot());
		}

		private void rotateHeadTowardsFront() {
			int i = this.headStableTime - HOW_LONG_IT_TAKES_TO_FACE_FORWARD;
			float f = Mth.clamp((float)i / HOW_LONG_IT_TAKES_TO_FACE_FORWARD, 0.0f, 1.0f);
			float g = (float)this.mob.getMaxHeadYRot() * (1.0f - f);
			this.mob.yBodyRot = Mth.rotateIfNecessary(this.mob.yBodyRot, this.mob.yHeadRot, g);
		}

		private boolean notCarryingMobPassengers() {
			return !(this.mob.getFirstPassenger() instanceof Mob);
		}

		private boolean isMoving() {
			double e;
			double d = this.mob.getX() - this.mob.xo;
			return d * d + (e = this.mob.getZ() - this.mob.zo) * e > 2.500000277905201E-7;
		}
	}
}