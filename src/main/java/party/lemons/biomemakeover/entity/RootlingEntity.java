package party.lemons.biomemakeover.entity;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.block.BlockState;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.init.BMItems;
import party.lemons.biomemakeover.init.BMNetwork;
import party.lemons.biomemakeover.util.EntityUtil;
import party.lemons.biomemakeover.util.NetworkUtil;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.EnumSet;
import java.util.List;
import java.util.Random;

public class RootlingEntity extends AnimalEntity implements Shearable
{
	public static final TrackedData<Boolean> HAS_FLOWER = DataTracker.registerData(RootlingEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
	public static final TrackedData<Integer> FLOWER_TYPE = DataTracker.registerData(RootlingEntity.class, TrackedDataHandlerRegistry.INTEGER);
	public static final Item[] PETAL_ITEMS = new Item[]{BMItems.BLUE_PETALS, BMItems.BROWN_PETALS, BMItems.CYAN_PETALS, BMItems.GRAY_PETALS, BMItems.LIGHT_BLUE_PETALS, BMItems.PURPLE_PETALS,};

	private AttributeContainer attributeContainer;
	private boolean hasAction = false;
	public RootlingEntity forcedDancePartner = null;
	private int actionCooldown;
	private int growTime = 0;

	public RootlingEntity(World world)
	{
		super(BMEntities.ROOTLING, world);

		actionCooldown = RandomUtil.randomRange(0, 500);
	}

	protected void initGoals()
	{
		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(1, new EscapeDangerGoal(this, 1.25D));
		this.goalSelector.add(2, new GetInRainGoal());
		this.goalSelector.add(4, new FleeEntityGoal<>(this, LivingEntity.class, 8F, 1.6D, 1.4D, (e)->
		{
			return e.getEquippedStack(EquipmentSlot.MAINHAND).getItem() == Items.SHEARS || e.getEquippedStack(EquipmentSlot.OFFHAND).getItem() == Items.SHEARS;
		}));
		this.goalSelector.add(5, new TemptGoal(this, 1D, Ingredient.ofItems(Items.BONE_MEAL), false));

		this.goalSelector.add(6, new DanceGoal());
		this.goalSelector.add(7, new FollowEntityGoal());
		this.goalSelector.add(8, new InspectFlowerGoal());
		this.goalSelector.add(9, new WanderAroundFarGoal(this, 1.0D));
		this.goalSelector.add(10, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
		this.goalSelector.add(11, new LookAroundGoal(this));
	}

	@Override
	protected void initDataTracker()
	{
		super.initDataTracker();
		dataTracker.startTracking(HAS_FLOWER, true);
		dataTracker.startTracking(FLOWER_TYPE, 0);
	}

	@Override
	protected void mobTick()
	{
		super.mobTick();
		if(!world.isClient())
		{
			if(!hasAction()) actionCooldown--;
			if(growTime > 0)
			{
				growTime--;
				if(isTouchingWaterOrRain() && random.nextInt(5) == 0)
					growTime--;
				if(growTime <= 0 && !hasFlower())
					setFlowered(true);
			}
		}
	}

	@Override
	public AttributeContainer getAttributes()
	{
		if(attributeContainer == null)
			attributeContainer = new AttributeContainer(MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D).build());
		return attributeContainer;
	}

	@Override
	public ActionResult interactMob(PlayerEntity player, Hand hand)
	{
		ItemStack itemStack = player.getStackInHand(hand);
		if(itemStack.getItem() == Items.SHEARS)
		{
			if(!this.world.isClient && this.isShearable())
			{
				this.sheared(SoundCategory.PLAYERS);
				itemStack.damage(1, player, (p)->p.sendToolBreakStatus(hand));
				return ActionResult.SUCCESS;
			}else
			{
				return ActionResult.CONSUME;
			}
		}else if(itemStack.getItem() == Items.BONE_MEAL)
		{
			if(!hasFlower())
			{
				if(world.isClient())
				{
					return ActionResult.CONSUME;
				}else
				{
					PacketByteBuf buf = PacketByteBufs.create();
					buf.writeDouble(getX());
					buf.writeDouble(getY());
					buf.writeDouble(getZ());
					NetworkUtil.serverSendTracking(world, getBlockPos(), BMNetwork.SPAWN_BONEMEAL_ENTITY_PARTICLES, buf);

					if(random.nextInt(3) == 0)
					{
						setFlowered(true);
					}
					if(!player.isCreative()) itemStack.decrement(1);
					return ActionResult.SUCCESS;
				}
			}
		}
		return super.interactMob(player, hand);
	}

	public int getActionCooldown()
	{
		return actionCooldown;
	}

	public void setActionCoolDown(int cooldown)
	{
		this.actionCooldown = cooldown;
	}

	@Override
	public PassiveEntity createChild(ServerWorld world, PassiveEntity entity)
	{
		return null;
	}

	@Override
	public void writeCustomDataToTag(CompoundTag tag)
	{
		super.writeCustomDataToTag(tag);

		tag.putInt("ActionCooldown", actionCooldown);
		tag.putInt("GrowTime", growTime);
		tag.putBoolean("HasFlower", dataTracker.get(HAS_FLOWER));
		tag.putInt("FlowerType", getFlowerIndex());
	}

	@Override
	public void readCustomDataFromTag(CompoundTag tag)
	{
		super.readCustomDataFromTag(tag);
		actionCooldown = tag.getInt("ActionCooldown");
		growTime = tag.getInt("GrowTime");
		dataTracker.set(HAS_FLOWER, tag.getBoolean("HasFlower"));
		dataTracker.set(FLOWER_TYPE, tag.getInt("FlowerType"));
	}

	protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions)
	{
		return dimensions.height * 0.6F;
	}

	public boolean hasFlower()
	{
		return dataTracker.get(HAS_FLOWER);
	}

	public void setFlowered(boolean hasFlower)
	{
		boolean current = dataTracker.get(HAS_FLOWER);
		if(current != hasFlower)
		{
			dataTracker.set(HAS_FLOWER, hasFlower);
			if(hasFlower)
			{
				randomizeFlower();
			}else
			{
				EntityUtil.scatterItemStack(this, new ItemStack(PETAL_ITEMS[dataTracker.get(FLOWER_TYPE)], RandomUtil.randomRange(1, 4)));
			}
		}
	}

	public boolean hasAction()
	{
		return hasAction;
	}

	public void setHasAction(boolean dancing)
	{
		this.hasAction = dancing;
	}

	@Override
	public void sheared(SoundCategory sound)
	{
		//TODO: custom sound
		this.world.playSoundFromEntity(null, this, SoundEvents.ENTITY_SHEEP_SHEAR, sound, 1.0F, 1.0F);
		this.setFlowered(false);
		growTime = RandomUtil.randomRange(600, 1200);
	}

	@Override
	public boolean isShearable()
	{
		return hasFlower();
	}

	public int getFlowerIndex()
	{
		return dataTracker.get(FLOWER_TYPE);
	}

	@Override
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CompoundTag entityTag)
	{
		EntityData superData = super.initialize(world, difficulty, spawnReason, entityData, entityTag);
		randomizeFlower();
		return superData;
	}

	public void randomizeFlower()
	{
		dataTracker.set(FLOWER_TYPE, random.nextInt(PETAL_ITEMS.length));
	}


	private static final TargetPredicate VALID_ROOTLING_PARTNER = (new TargetPredicate()).setBaseMaxDistance(8.0D).includeInvulnerable().includeTeammates().includeHidden();
	public static int MAX_DANCE_TIME = 60;
	public static int MAX_FOLLOW_TIME = 120;
	public static int MAX_INSPECT_TIME = 200;
	public static int MAX_ACTION_COOLDOWN = 500;


	public class InspectFlowerGoal extends Goal
	{
		private BlockPos targetPos;
		private BlockState targetState;
		private int timer;

		public InspectFlowerGoal()
		{
			this.setControls(EnumSet.of(Goal.Control.MOVE, Control.LOOK));
		}

		@Override
		public boolean canStart()
		{
			if(random.nextInt(10) == 0 || actionCooldown > 0 || hasAction()) return false;

			targetPos = findFlower();
			return targetPos != null;
		}

		@Override
		public void tick()
		{
			timer++;
			if(squaredDistanceTo(targetPos.getX() + 0.5F, targetPos.getY() + 0.5F, targetPos.getZ() + 0.5F) > 2F)
			{
				getMoveControl().moveTo(targetPos.getX() + 0.5F, targetPos.getY() + 0.5F, targetPos.getZ() + 0.5F, 0.6F);
			}
			float targetY = targetPos.getY();
			if(targetState.isIn(BlockTags.TALL_FLOWERS)) targetY += 1;

			getLookControl().lookAt(targetPos.getX() + 0.5F, targetY, targetPos.getZ() + 0.5F);
		}

		@Override
		public boolean shouldContinue()
		{
			BlockState st = world.getBlockState(targetPos);
			if(st != targetState) return false;
			return timer <= MAX_INSPECT_TIME;
		}

		@Override
		public void stop()
		{
			this.timer = 0;
			setHasAction(false);
		}

		@Override
		public void start()
		{
			setHasAction(true);
			setActionCoolDown(MAX_ACTION_COOLDOWN);
		}

		private BlockPos findFlower()
		{
			BlockPos startPos = getBlockPos();
			List<BlockPos> spots = Lists.newArrayList();

			BlockPos.Mutable m = new BlockPos.Mutable(startPos.getX(), startPos.getY(), startPos.getZ());
			for(int x = startPos.getX() - 2; x < startPos.getX() + 2; x++)
			{
				for(int z = startPos.getZ() - 2; z < startPos.getZ() + 2; z++)
				{
					m.set(x, startPos.getY(), z);
					BlockState checkState = world.getBlockState(m);
					if(checkState.isIn(BlockTags.FLOWERS))
					{
						spots.add(new BlockPos(m.getX(), m.getY(), m.getZ()));
					}
				}
			}
			if(spots.isEmpty()) return null;

			BlockPos pos = spots.get(world.random.nextInt(spots.size()));
			if(pos != null) targetState = world.getBlockState(pos);

			return pos;
		}
	}

	public class FollowEntityGoal extends Goal
	{
		public LivingEntity followPartner;
		private int timer = 0;

		public FollowEntityGoal()
		{
		}

		@Override
		public boolean canStart()
		{
			if(hasAction() || getActionCooldown() >= 0 || random.nextInt(10) != 0) return false;

			followPartner = findFollowPartner();

			return followPartner != null;
		}

		@Override
		public boolean shouldContinue()
		{
			return this.followPartner.isAlive() && this.timer < MAX_FOLLOW_TIME;
		}


		@Override
		public void stop()
		{
			this.followPartner = null;
			this.timer = 0;
			setHasAction(false);
		}

		@Override
		public void start()
		{
			setHasAction(true);
			setActionCoolDown(MAX_ACTION_COOLDOWN);
		}

		@Override
		public void tick()
		{
			getLookControl().lookAt(this.followPartner, 10.0F, (float) getLookPitchSpeed());
			getNavigation().startMovingTo(this.followPartner, 1D);
			if(squaredDistanceTo(followPartner) <= 4D) getNavigation().stop();

			++this.timer;
		}

		private LivingEntity findFollowPartner()
		{
			List<AnimalEntity> list = world.getTargets(AnimalEntity.class, VALID_ROOTLING_PARTNER, RootlingEntity.this, getBoundingBox().expand(8.0D));
			double minDistance = Double.MAX_VALUE;
			LivingEntity closestPossible = null;

			for(AnimalEntity e : list)
			{
				if(squaredDistanceTo(e) < minDistance)
				{
					if(e instanceof RootlingEntity) continue;

					closestPossible = e;
					minDistance = squaredDistanceTo(e);
				}
			}
			return closestPossible;
		}
	}

	public class DanceGoal extends Goal
	{
		private RootlingEntity partner = null;
		private int timer;

		public DanceGoal()
		{
			this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK, Control.JUMP));
		}

		public boolean canStart()
		{

			if(forcedDancePartner != null)
			{
				partner = forcedDancePartner;
				forcedDancePartner = null;
				return true;
			}

			if(hasAction() || getActionCooldown() >= 0) return false;

			this.partner = this.findPartner();
			return this.partner != null;
		}

		@Override
		public boolean shouldContinue()
		{
			return this.partner.isAlive() && this.partner.hasAction() && this.timer < MAX_DANCE_TIME;
		}

		@Override
		public void stop()
		{
			this.partner = null;
			this.timer = 0;

			setHasAction(false);
			if(partner != null) partner.setHasAction(false);
		}

		@Override
		public void start()
		{
			setHasAction(true);
			partner.setHasAction(true);
			setActionCoolDown(MAX_ACTION_COOLDOWN);
		}

		private RootlingEntity findPartner()
		{
			List<RootlingEntity> list = world.getTargets(RootlingEntity.class, VALID_ROOTLING_PARTNER, RootlingEntity.this, getBoundingBox().expand(8.0D));
			double minDistance = Double.MAX_VALUE;
			RootlingEntity closestPossible = null;

			for(RootlingEntity rootlingEntity : list)
			{
				if(!rootlingEntity.hasAction() && rootlingEntity.getActionCooldown() <= 0 && squaredDistanceTo(rootlingEntity) < minDistance)
				{
					closestPossible = rootlingEntity;
					minDistance = squaredDistanceTo(rootlingEntity);
				}
			}

			if(closestPossible != null) closestPossible.forcedDancePartner = RootlingEntity.this;
			return closestPossible;
		}

		public void tick()
		{
			getLookControl().lookAt(this.partner, 10.0F, (float) getLookPitchSpeed());
			getNavigation().startMovingTo(this.partner, 1D);
			++this.timer;
			if(this.timer < MAX_DANCE_TIME && squaredDistanceTo(this.partner) < 9.0D)
			{
				getJumpControl().setActive();
			}
		}
	}

	public class GetInRainGoal extends Goal
	{
		private double targetX;
		private double targetY;
		private double targetZ;

		public GetInRainGoal()
		{
			this.setControls(EnumSet.of(Goal.Control.MOVE));
		}

		public boolean canStart()
		{
			if(world.isRaining() && !world.isSkyVisible(getBlockPos())) return true;

			return this.targetSkyPos();
		}

		protected boolean targetSkyPos()
		{
			Vec3d vec3d = this.locateSkyPos();
			if(vec3d == null)
			{
				return false;
			}else
			{
				this.targetX = vec3d.x;
				this.targetY = vec3d.y;
				this.targetZ = vec3d.z;
				return true;
			}
		}

		public boolean shouldContinue()
		{
			return !getNavigation().isIdle();
		}

		public void start()
		{
			getNavigation().startMovingTo(this.targetX, this.targetY, this.targetZ, 1);
		}

		protected Vec3d locateSkyPos()
		{
			Random random = getRandom();
			BlockPos blockPos = getBlockPos();

			for(int i = 0; i < 10; ++i)
			{
				BlockPos blockPos2 = blockPos.add(random.nextInt(20) - 10, random.nextInt(6) - 3, random.nextInt(20) - 10);
				if(world.isSkyVisible(blockPos2) && getPathfindingFavor(blockPos2) < 0.0F)
				{
					return Vec3d.ofBottomCenter(blockPos2);
				}
			}

			return null;
		}
	}

}
