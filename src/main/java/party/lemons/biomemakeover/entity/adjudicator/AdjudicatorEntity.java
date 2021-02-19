package party.lemons.biomemakeover.entity.adjudicator;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityGroup;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
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
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.server.network.ServerPlayerEntity;
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
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.util.BMUtil;
import party.lemons.biomemakeover.util.NBTUtil;
import party.lemons.biomemakeover.util.extensions.GoalSelectorExtension;

import java.util.List;
import java.util.Map;

public class AdjudicatorEntity extends HostileEntity implements RangedAttackMob
{
	public static final TrackedData<Integer> STATE = DataTracker.registerData(AdjudicatorEntity.class, TrackedDataHandlerRegistry.INTEGER);

	public final Map<Identifier, AdjudicatorPhase> PHASES = Maps.newHashMap();

	public final AdjudicatorPhase IDLE = new IdleAdjudicatorPhase(BiomeMakeover.ID("idle"), this);
	public final TeleportingPhase TELEPORT = new TeleportingPhase(BiomeMakeover.ID("teleport"), this);
	public final BowAttackingPhase BOW_ATTACK = new BowAttackingPhase(BiomeMakeover.ID("bow_attack"), this);
	public final MeleeAttackingPhase MELEE_ATTACK = new MeleeAttackingPhase(BiomeMakeover.ID("melee_attack"), this);
	public final RavagerChargePhase RAVAGER = new RavagerChargePhase(BiomeMakeover.ID("ravager"), this);

	private final ServerBossBar bossBar;
	private AdjudicatorPhase phase;
	private boolean active = false;
	private BlockPos homePos;
	private boolean firstTick = true;
	private Box roomBounds;
	private AttributeContainer attributes;
	private List<BlockPos> arenaPositions;
	public int stateTime = 0;

	public float teleRotPrevious = 0;

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
	protected void initDataTracker()
	{
		super.initDataTracker();
		dataTracker.startTracking(STATE, 0);
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
	}

	@Override
	public boolean damage(DamageSource source, float amount)
	{
		if(!active && source.getSource() instanceof PlayerEntity)
			active = true;

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
			teleRotPrevious = 0;
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
                                .add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D)
                                .add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0D)
                                .build());
		return attributes;
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
		ItemStack arrowTypeStack = this.getArrowType(this.getStackInHand(BMUtil.getHandPossiblyHolding(this, (i)->i.getItem() instanceof BowItem)));
		PersistentProjectileEntity arrow = ProjectileUtil.createArrowProjectile(this, arrowTypeStack, pullProgress);

		double distanceX = target.getX() - this.getX();
		double distanceY = target.getBodyY(0.333F) - arrow.getY();
		double distanceZ = target.getZ() - this.getZ();

		double arc = MathHelper.sqrt(distanceX * distanceX + distanceZ * distanceZ);
		arrow.setVelocity(distanceX, distanceY + arc * 0.2F, distanceZ, 1.6F, (float)(14 - this.world.getDifficulty().getId() * 4));

		this.playSound(SoundEvents.ENTITY_SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
		this.world.spawnEntity(arrow);
	}

	public BlockPos findSuitableArenaPos()
	{
		if(arenaPositions == null)
		{
			//TODO: best guess?
		}
		else
		{
			return arenaPositions.get(random.nextInt(arenaPositions.size()));
		}
		return null;
	}

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
		this.updatePositionAndAngles(pos.getX() + 0.5F, pos.getY() + 1F, pos.getZ() + 0.5F, yaw, pitch);
	}
}
