package party.lemons.biomemakeover.entity.adjudicator;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvironmentInterface;
import net.fabricmc.api.EnvironmentInterfaces;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.entity.feature.SkinOverlayOwner;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.PrioritizedGoal;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.boss.ServerBossBar;
import net.minecraft.entity.boss.WitherEntity;
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
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.StoneGolemEntity;
import party.lemons.biomemakeover.entity.adjudicator.phase.*;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.init.BMItems;
import party.lemons.biomemakeover.util.BMUtil;
import party.lemons.biomemakeover.util.NBTUtil;
import party.lemons.biomemakeover.util.extensions.GoalSelectorExtension;

import java.awt.*;
import java.util.List;
import java.util.Map;

@EnvironmentInterfaces({@EnvironmentInterface(
		value = EnvType.CLIENT,
		itf = SkinOverlayOwner.class
)})
public class AdjudicatorEntity extends HostileEntity implements RangedAttackMob, CrossbowUser, AdjudicatorStateProvider, SkinOverlayOwner
{
	public static final TrackedData<Integer> STATE = DataTracker.registerData(AdjudicatorEntity.class, TrackedDataHandlerRegistry.INTEGER);     //Adjudicator Phase
	private static final TrackedData<Boolean> CHARGING = DataTracker.registerData(AdjudicatorEntity.class, TrackedDataHandlerRegistry.BOOLEAN); //Crossbow Charging
	private static final TrackedData<Boolean> INVULNERABLE = DataTracker.registerData(AdjudicatorEntity.class, TrackedDataHandlerRegistry.BOOLEAN); //Invulnerable

	/*
		Adjudicator Phases
	 */
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
		AdjudicatorRoomListener.enableAdjudicator(this);    //Listen for break/place events for this adjudicator
	}

	@Override
	protected void initDataTracker()
	{
		super.initDataTracker();
		dataTracker.startTracking(STATE, 0);
		dataTracker.startTracking(CHARGING, false);
		dataTracker.startTracking(INVULNERABLE, false);
	}

	@Override
	public void tick()
	{
		stateTime++;
		if(!world.isClient() && firstTick)  //Set up arena
		{
			homePos = getBlockPos();    //Home pos is generally the center of the arena. The position the adjudicator will return to if out of combat.

			roomBounds = new Box(homePos.down(4)).expand(13,0, 13).stretch(0, 13, 0);    //Get the room bounds.
			firstTick = false;

			/*
				Find valid arena locations, these are placed in the structure then processed here.
				These are the locations we can spawn things at or teleport to.
			 */
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

		//Update the phase
		if(!world.isClient() && phase != null)
		{
			phase.tick();

			if(phase.isPhaseOver())
				setPhase(phase.getNextPhase());
		}

		//Update Boss bar
		this.bossBar.setPercent(this.getHealth() / this.getMaxHealth());

		//Casting Particles
		tickCastParticles();

		//Anti-cheese if we're out of the arena, teleport back in.
		if(!world.isClient())
		{
			if(!getArenaBounds().contains(getPos()))
			{
				stopRiding();
				teleportHome();
			}
		}

		//Update tracked players
		updatePlayers();
	}

	/*
		Do casting particles
	 */
	private void tickCastParticles()
	{
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
	}

	/*
		Update tracked players
	 */
	private void updatePlayers()
	{
		if(!active || world.isClient())
			return;

		//If there are no valid players within the arena, teleport home and reset the fight.
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
			finishFightTime = 0;    //There are valid players, reset reset timer
		}

		/*
			Update boss bar tracked players
			Players within the boss room get the boss health bar, those outside do not.
		 */
		//Get all valid players + spectator
		List<PlayerEntity> playersWithSpectator = world.getEntitiesByClass(PlayerEntity.class, getArenaBounds(), (p)->true);
		for(ServerPlayerEntity playerEntity : bossBar.getPlayers()) //Loop through current players tracked by the boss bar
		{
			//Remove them from tracking if they're no longer valid
			if(!playersWithSpectator.contains(playerEntity))
				bossBar.removePlayer(playerEntity);
		}

		//Loop though valid players
		for(PlayerEntity playerEntity : playersWithSpectator)
		{
			//Add them to boss bar tracked players
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
		//Activate the fight if the tracker if the player
		if(!active && source.getAttacker() instanceof PlayerEntity)
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
		//Bug fix: ensure the adjudicator is in some sort of phase
		if(active && phase == null)
		{
			setPhase(TELEPORT);
			return true;
		}

		//Don't hurt in idle phase if attacker isn't a player
		if(phase == IDLE && !(damageSource.getAttacker() instanceof PlayerEntity))
			return true;

		//Don't hurt if phase is invulnerable phase
		if(phase.isInvulnerable() && !damageSource.isOutOfWorld())
			return true;

		return super.isInvulnerableTo(damageSource);
	}

	@Override
	protected void dropEquipment(DamageSource source, int lootingMultiplier, boolean allowDrops) {
		//Drop totem and tapestry
		//This isn't done via loot table because we want the item to be coveted
		ItemEntity enchantedTotem = this.dropItem(BMItems.ENCHANTED_TOTEM);
		if (enchantedTotem != null)
			enchantedTotem.setCovetedItem();

		ItemEntity tapestry = this.dropItem(BMBlocks.ADJUDICATOR_TAPESTRY);
		if(tapestry != null)
			tapestry.setCovetedItem();
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
			lastRenderX = getX();
			lastRenderY = getY();
			lastRenderZ = getZ();
		}


		super.onTrackedDataSet(data);
	}

	@Override
	public void onDeath(DamageSource source)
	{
		if(phase != null)
			phase.onExitPhase();

		bossBar.clearPlayers();
		bossBar.setVisible(false);
		super.onDeath(source);
	}

	private void setUpPhase()
	{
		//Stop pathing
		getNavigation().stop();

		//Copy AI from phase
		goalSelector.getRunningGoals().forEach(PrioritizedGoal::stop);
		GoalSelectorExtension.copy(goalSelector, phase.getGoalSelector());
		GoalSelectorExtension.copy(targetSelector, phase.getTargetSelector());

		bossBar.setVisible(phase.showBossBar());

		dataTracker.set(INVULNERABLE, phase.isInvulnerable());
	}

	@Override
	public boolean isPushable()
	{
		return false;
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

		if(phase == null)
			phase = IDLE;

		tag.putString("Phase", phase.getPhaseID().toString());
		tag.put("PhaseData", phase.toTag());
		tag.putInt("State", getState().ordinal());
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

		setState(AdjudicatorState.values()[tag.getInt("State")]);
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


	/*
			Select a random player in the room rather than focusing on a single person.
	 */
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
		if(getMainHandStack().getItem() instanceof CrossbowItem)    //Handle crossbow shoot
		{
			shoot(this, 2F);
		}
		else    //Handle bow shoot
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

	/*
		Finds one of the preset arena positions
	 */
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
		if(world.getBlockState(pos.down()).isAir())
			world.setBlockState(pos.down(), Blocks.COBBLESTONE.getDefaultState());

		this.refreshPositionAfterTeleport(pos.getX() + 0.5F, pos.getY() + 1F, pos.getZ() + 0.5F);
		clearArea(this);
	}

	/*
		Anti-Cheese: Break blocks around an entity to prevent trapping
	 */
	public void clearArea(Entity e)
	{
		if(this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING))
		{
			Box hitBox = e.getBoundingBox();
			destroyArea(hitBox);

			if(e.isInsideWall())
			{
				hitBox = hitBox.expand(1F);
				destroyArea(hitBox);
			}
		}
	}

	private void destroyArea(Box hitBox)
	{
		BlockPos.
				iterate(new BlockPos((int) hitBox.minX, (int) hitBox.minY, (int) hitBox.minZ),
				        new BlockPos((int) hitBox.maxX, (int) hitBox.maxY, (int) hitBox.maxZ))
				.forEach(b->
				         {
					         if(WitherEntity.canDestroy(world.getBlockState(b)))
					         {
						         this.world.breakBlock(b, true, this);
						         this.world.syncWorldEvent(null, 1022, b, 0);
					         }
				         });
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

	@Override
	public boolean canTarget(LivingEntity target)
	{
		if(getVehicle() == target)
			return false;

		if(target instanceof StoneGolemEntity && !((StoneGolemEntity) target).isPlayerCreated())
				return false;

		return super.canTarget(target);
	}

	@Override
	public boolean shouldRenderOverlay() {
		return dataTracker.get(INVULNERABLE);
	}
}
