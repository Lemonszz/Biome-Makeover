package party.lemons.biomemakeover.entity.adjudicator;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.adjudicator.phase.*;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.util.BMUtil;
import party.lemons.biomemakeover.util.NBTUtil;
import party.lemons.biomemakeover.util.extensions.GoalSelectorExtension;

import java.util.List;
import java.util.Map;

public class AdjudicatorEntity extends HostileEntity implements RangedAttackMob, CrossbowUser, AdjudicatorStateProvider
{
	public static final TrackedData<Integer> STATE = DataTracker.registerData(AdjudicatorEntity.class, TrackedDataHandlerRegistry.INTEGER);
	private static final TrackedData<Boolean> CHARGING = DataTracker.registerData(AdjudicatorEntity.class, TrackedDataHandlerRegistry.BOOLEAN);

	public final Map<Identifier, AdjudicatorPhase> PHASES = Maps.newHashMap();

	public final AdjudicatorPhase IDLE = new IdleAdjudicatorPhase(BiomeMakeover.ID("idle"), this);
	public final TeleportingPhase TELEPORT = new TeleportingPhase(BiomeMakeover.ID("teleport"), this);
	public final BowAttackingPhase BOW_ATTACK = new BowAttackingPhase(BiomeMakeover.ID("bow_attack"), this);
	public final MeleeAttackingPhase MELEE_ATTACK = new MeleeAttackingPhase(BiomeMakeover.ID("melee_attack"), this);
	public final FangAttackingPhase FANG_ATTACK = new FangAttackingPhase(BiomeMakeover.ID("fang_attack"), this);
	public final RavagerChargePhase RAVAGER = new RavagerChargePhase(BiomeMakeover.ID("ravager"), this);
	public final SummonPhase SPAWN_EVOKERS = new SummonPhase(BiomeMakeover.ID("spawn_evoker"), this, 2, EntityType.EVOKER);
	public final SummonPhase SPAWN_VINDICATORS = new SummonPhase(BiomeMakeover.ID("spawn_vindicator"), this, 6, EntityType.VINDICATOR);
	public final SummonPhase SPAWN_VEX = new SummonPhase(BiomeMakeover.ID("spawn_vex"), this, 2, EntityType.VEX);
	public final SummonPhase SPAWN_MIX = new SummonPhase(BiomeMakeover.ID("spawn_mix"), this, 3, EntityType.VEX, EntityType.VINDICATOR, EntityType.EVOKER, EntityType.PILLAGER);
	public final MimicPhase MIMIC = new MimicPhase(BiomeMakeover.ID("mimic"), this);
	public final FangBarragePhase FANG_BARAGE = new FangBarragePhase(BiomeMakeover.ID("fang_barrage"), this);
	public final StoneGolemPhase STONE_GOLEM = new StoneGolemPhase(BiomeMakeover.ID("stone_golem"), this);

	private final ServerBossBar bossBar;
	private AdjudicatorPhase phase;
	private boolean active = false;
	private BlockPos homePos;
	private boolean firstTick = true;
	private Box roomBounds;
	private AttributeContainer attributes;
	private List<BlockPos> arenaPositions;
	public int stateTime = 0;
	private int finishFightTime = 0;

	public float renderRotPrevious = 0;

	public AdjudicatorEntity(World world)
	{
		super(BMEntities.ADJUDICATOR, world);
		this.bossBar = new ServerBossBar(this.getDisplayName(), BossBar.Color.BLUE, BossBar.Style.PROGRESS);

		this.setHealth(this.getMaxHealth());
		this.getNavigation().setCanSwim(true);
		this.experiencePoints = 50;

		setPhase(IDLE);
	}

	@Override
	protected void initGoals()
	{
		super.initGoals();
		AdjudicatorRoomListener.enableAdjudicator(this);
	}

	@Override
	protected void initDataTracker()
	{
		super.initDataTracker();
		dataTracker.startTracking(STATE, 0);
		dataTracker.startTracking(CHARGING, false);
	}

	@Override
	public void tick()
	{
		stateTime++;
		if(!world.isClient() && firstTick)
		{
			homePos = getBlockPos();
			roomBounds = new Box(homePos).expand(13,0, 13).stretch(0, 13, 0);
			firstTick = false;

			arenaPositions = Lists.newArrayList();
			arenaPositions.add(homePos);
			BlockPos.iterate((int)roomBounds.minX, (int)roomBounds.minY, (int)roomBounds.minZ, (int)roomBounds.maxX, (int)roomBounds.maxY, (int)roomBounds.maxZ)
					.forEach(b->{
						if(world.getBlockState(b).isOf(Blocks.SMOOTH_QUARTZ))   //TODO: make marker block
						{
							world.setBlockState(b, Blocks.AIR.getDefaultState());
							arenaPositions.add(b.toImmutable());
						}
					});
		}
		super.tick();

		if(!world.isClient() && phase != null)
		{
			phase.tick();

			if(phase.isPhaseOver())
				setPhase(phase.getNextPhase());
		}
		this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());

		if (this.world.isClient && isCasting())
		{
			double r, g, b;
			if(getState() == AdjudicatorState.TELEPORT)
			{
				r = 158F / 255F;
				g = 60F / 255F;
				b = 194F / 255F;
			}
			else
			{
				r = 145F / 255F;
				g = 145F / 255F;
				b = 145F / 255F;
			}

			float angle = this.bodyYaw * 0.017453292F + MathHelper.cos((float)this.age * 0.6662F) * 0.25F;
			float xOffset = MathHelper.cos(angle);
			float zOffset = MathHelper.sin(angle);
			this.world.addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + (double)xOffset * 0.6D, this.getY() + 1.8D, this.getZ() + (double)zOffset * 0.6D, r, g, b);
			this.world.addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() - (double)xOffset * 0.6D, this.getY() + 1.8D, this.getZ() - (double)zOffset * 0.6D, r, g, b);
		}

		if(!world.isClient())
		{
			if(!getArenaBounds().contains(getPos()))
			{
				stopRiding();
				teleportHome();
			}
		}

		if(active)
			updatePlayers();
	}

	private void updatePlayers()
	{
		if(world.isClient())
			return;

		List<PlayerEntity> players = world.getEntitiesByClass(PlayerEntity.class, getArenaBounds(), EntityPredicates.EXCEPT_SPECTATOR);
		if(players.isEmpty())
		{
			finishFightTime++;
			if(finishFightTime > 350)
			{
				teleportHome();
				finishFightTime = 0;
				setState(AdjudicatorState.WAITING);
				setPhase(IDLE);
				active = false;
			}
		}
		else
		{
			finishFightTime = 0;
		}

		List<PlayerEntity> playersWithSpectator = world.getEntitiesByClass(PlayerEntity.class, getArenaBounds(), (p)->true);
		for(PlayerEntity playerEntity : bossBar.getPlayers())
		{
			if(!playersWithSpectator.contains(playerEntity))
				bossBar.removePlayer((ServerPlayerEntity) playerEntity);
		}

		for(PlayerEntity playerEntity : playersWithSpectator)
		{
			if(!bossBar.getPlayers().contains(playerEntity))
				bossBar.addPlayer((ServerPlayerEntity) playerEntity);
		}
	}

	public boolean isCasting()
	{
		AdjudicatorState state = getState();
		return state == AdjudicatorState.SUMMONING || state == AdjudicatorState.TELEPORT;
	}

	@Override
	public boolean damage(DamageSource source, float amount)
	{
		if(!active && source.getSource() instanceof PlayerEntity)
		{
			active = true;
		}
		if(phase != null)
			phase.onHurt(source, amount);

		return super.damage(source, amount);
	}

	@Override
	public boolean isInvulnerableTo(DamageSource damageSource)
	{
		if(phase == IDLE && !(damageSource.getSource() instanceof PlayerEntity))
			return true;

		if(phase.isInvulnerable() && !damageSource.isOutOfWorld())
			return true;

		return super.isInvulnerableTo(damageSource);
	}

	private void setPhase(AdjudicatorPhase phase)
	{
		if(this.phase != null)
			this.phase.onExitPhase();

		this.phase = phase;

		if(this.phase != null)
		{
			this.phase.onEnterPhase();
			setUpPhase();
		}
	}

	@Override
	public void onTrackedDataSet(TrackedData<?> data)
	{
		if(data == STATE)
		{
			stateTime = 0;
			renderRotPrevious = 0;

			prevX = getX();
			prevY = getY();
			prevZ = getZ();
		}


		super.onTrackedDataSet(data);
	}

	private void setUpPhase()
	{
		getNavigation().stop();
		//Copy AI from phase
		goalSelector.getRunningGoals().forEach(PrioritizedGoal::stop);
		GoalSelectorExtension.copy(goalSelector, phase.getGoalSelector());
		GoalSelectorExtension.copy(targetSelector, phase.getTargetSelector());

		bossBar.setVisible(phase.showBossBar());
	}

	@Override
	public void writeCustomDataToTag(CompoundTag tag)
	{
		super.writeCustomDataToTag(tag);
		tag.putBoolean("FirstTick", firstTick);
		tag.putBoolean("BossActive", active);
		if(homePos != null)
			NBTUtil.writeBlockPos(homePos, tag);
		if(roomBounds != null)
			NBTUtil.writeBox(roomBounds, tag);

		if(arenaPositions != null)
		{
			ListTag arenaPosTags = new ListTag();
			for(BlockPos pos : arenaPositions)
			{
				arenaPosTags.add(NbtHelper.fromBlockPos(pos));
			}
			tag.put("ArenaPositions", arenaPosTags);
		}

		tag.putString("Phase", phase.getPhaseID().toString());
		tag.put("PhaseData", phase.toTag());
	}

	@Override
	public void readCustomDataFromTag(CompoundTag tag)
	{
		super.readCustomDataFromTag(tag);
		firstTick = tag.getBoolean("FirstTick");
		active = tag.getBoolean("BossActive");
		homePos = NBTUtil.readBlockPos(tag);
		roomBounds = NBTUtil.readBox(tag);

		if(tag.contains("ArenaPositions"))
		{
			ListTag arenaTags = tag.getList("ArenaPositions", NbtType.COMPOUND);
			this.arenaPositions = Lists.newArrayList();
			for(int i = 0; i < arenaTags.size(); i++)
			{
				arenaPositions.add(NbtHelper.toBlockPos(arenaTags.getCompound(i)));
			}
		}

		Identifier phaseID = new Identifier(tag.getString("Phase"));
		AdjudicatorPhase adjPhase = PHASES.get(phaseID);
		adjPhase.fromTag(tag.getCompound("PhaseData"));
		this.phase = adjPhase;
		setUpPhase();
	}

	@Override
	public AttributeContainer getAttributes()
	{
		if(attributes == null)
			attributes = new AttributeContainer(HostileEntity.createHostileAttributes()
								.add(EntityAttributes.GENERIC_MAX_HEALTH, 225F)
                                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D)
                                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0D)
                                .build());
		return attributes;
	}


	private final TargetPredicate targetPredicate = (new TargetPredicate()).setBaseMaxDistance(32);
	public void selectTarget(Class targetClass) {
		LivingEntity targetEntity = null;

		if (targetClass != PlayerEntity.class && targetClass != ServerPlayerEntity.class) {
			targetEntity = world.getClosestEntityIncludingUngeneratedChunks(targetClass, this.targetPredicate, this, getX(), getEyeY(), getZ(), getArenaBounds());
		} else {
			targetEntity = world.getClosestPlayer(this.targetPredicate, this, getX(), getEyeY(), getZ());
		}

		if(targetEntity != null)
			setTarget(targetEntity);
	}

	@Override
	protected SoundEvent getAmbientSound()
	{
		return isActive() ? BMEffects.ADJUDICATOR_LAUGH : BMEffects.ADJUDICATOR_IDLE;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource source)
	{
		return BMEffects.ADJUDICATOR_HURT;
	}

	@Override
	protected SoundEvent getDeathSound()
	{
		return BMEffects.ADJUDICATOR_DEATH;
	}

	@Override
	public void setCustomName(@Nullable Text name) {
		super.setCustomName(name);
		this.bossBar.setName(this.getDisplayName());
	}

	@Override
	public void onStartedTrackingBy(ServerPlayerEntity player) {
		super.onStartedTrackingBy(player);
		this.bossBar.addPlayer(player);
	}

	@Override
	public void onStoppedTrackingBy(ServerPlayerEntity player) {
		super.onStoppedTrackingBy(player);
		this.bossBar.removePlayer(player);
	}

	/*
			Don't slow down (cobwebs, berry bushes, etc).
	 */
	@Override
	public void slowMovement(BlockState state, Vec3d multiplier) {
	}

	/*
			Don't Despawn.
	 */
	@Override
	public void checkDespawn() {
		this.despawnCounter = 0;
	}

	/*
			Don't go through portals.
	 */
	@Override
	public boolean canUsePortals() {
		return false;
	}

	/*
			Don't take fall damage
	 */
	@Override
	public boolean handleFallDamage(float fallDistance, float damageMultiplier) {
		return false;
	}

	/*
		is the fight active.
	 */
	public boolean isActive()
	{
		return active;
	}

	@Override
	public EntityGroup getGroup()
	{
		return EntityGroup.ILLAGER;
	}

	//Arrow attack.
	@Override
	public void attack(LivingEntity target, float pullProgress)
	{
		if(getMainHandStack().getItem() instanceof CrossbowItem)
		{
			shoot(this, 2F);
		}
		else
		{
			ItemStack arrowTypeStack = this.getArrowType(this.getStackInHand(BMUtil.getHandPossiblyHolding(this, (i)->i.getItem() instanceof BowItem)));
			PersistentProjectileEntity arrow = ProjectileUtil.createArrowProjectile(this, arrowTypeStack, pullProgress);

			double distanceX = target.getX() - this.getX();
			double distanceY = target.getBodyY(0.333F) - arrow.getY();
			double distanceZ = target.getZ() - this.getZ();

			double arc = MathHelper.sqrt(distanceX * distanceX + distanceZ * distanceZ);
			arrow.setVelocity(distanceX, distanceY + arc * 0.2F, distanceZ, 1.6F, (float) (14 - this.world.getDifficulty().getId() * 4));

			this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
			this.world.spawnEntity(arrow);
		}
	}

	public BlockPos findSuitableArenaPos()
	{
		if(arenaPositions == null)
		{
			//TODO: best guess?
		}
		else
		{
			BlockPos arenaPos;
			do{
				arenaPos = arenaPositions.get(random.nextInt(arenaPositions.size()));
			}while(arenaPos.isWithinDistance(getBlockPos(), 1));
			return arenaPos;
		}
		return null;
	}

	@Override
	public AdjudicatorState getState()
	{
		return AdjudicatorState.values()[dataTracker.get(STATE) % AdjudicatorState.values().length];
	}

	public void setState(AdjudicatorState state)
	{
		dataTracker.set(STATE, state.ordinal());
		stateTime = 0;
	}

	public void teleportToRandomArenaPos()
	{
		teleportTo(findSuitableArenaPos());
	}

	public void teleportHome()
	{
		teleportTo(homePos);
	}

	public void teleportTo(BlockPos pos)
	{
		this.refreshPositionAfterTeleport(pos.getX() + 0.5F, pos.getY() + 1F, pos.getZ() + 0.5F);
	}

	public Box getArenaBounds()
	{
		return roomBounds;
	}

	@Environment(EnvType.CLIENT)
	public boolean isCharging() {
		return this.dataTracker.get(CHARGING);
	}

	public void setCharging(boolean charging) {
		this.dataTracker.set(CHARGING, charging);
	}

	@Override
	public void shoot(LivingEntity target, ItemStack crossbow, ProjectileEntity projectile, float multiShotSpray) {
		this.shoot(this, target, projectile, multiShotSpray, 1.6F);
	}

	@Override
	public void postShoot()
	{

	}

	public BlockPos getHomePosition()
	{
		return homePos;
	}

	public void setActive()
	{
		this.active = true;
	}

	@Override
	public void remove()
	{
		super.remove();
		AdjudicatorRoomListener.disableAdjudicator(this);
	}
}
