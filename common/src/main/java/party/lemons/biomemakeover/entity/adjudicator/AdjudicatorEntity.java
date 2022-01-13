package party.lemons.biomemakeover.entity.adjudicator;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.BossEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.PlayerCreatable;
import party.lemons.biomemakeover.entity.StoneGolemEntity;
import party.lemons.biomemakeover.entity.adjudicator.phase.*;
import party.lemons.biomemakeover.entity.event.EntityEventBroadcaster;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.init.BMItems;
import party.lemons.biomemakeover.util.BMUtil;
import party.lemons.biomemakeover.util.NBTUtil;
import party.lemons.biomemakeover.util.extension.GoalSelectorExtension;

import java.util.List;
import java.util.Map;

public class AdjudicatorEntity extends Monster implements PowerableMob, AdjudicatorStateProvider, RangedAttackMob, CrossbowAttackMob, EntityEventBroadcaster {

    public static final EntityDataAccessor<Integer> STATE = SynchedEntityData.defineId(AdjudicatorEntity.class, EntityDataSerializers.INT);     //Adjudicator Phase
    private static final EntityDataAccessor<Boolean> CHARGING = SynchedEntityData.defineId(AdjudicatorEntity.class, EntityDataSerializers.BOOLEAN); //Crossbow Charging
    private static final EntityDataAccessor<Boolean> INVULNERABLE = SynchedEntityData.defineId(AdjudicatorEntity.class, EntityDataSerializers.BOOLEAN); //Invulnerable

    /*
		Adjudicator Phases
	 */
    public final Map<ResourceLocation, AdjudicatorPhase> PHASES = Maps.newHashMap();
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

    private final ServerBossEvent bossBar;
    private AdjudicatorPhase phase;
    private boolean active = false;
    private BlockPos homePos;
    private boolean firstTick = true;
    private AABB roomBounds;
    private List<BlockPos> arenaPositions;
    public int stateTime = 0;
    private int finishFightTime = 0;

    public float renderRotPrevious = 0;

    public AdjudicatorEntity(EntityType<? extends AdjudicatorEntity> entityType, Level level) {
        super(entityType, level);

        this.bossBar = new ServerBossEvent(this.getDisplayName(), BossEvent.BossBarColor.BLUE, BossEvent.BossBarOverlay.PROGRESS);

        this.setHealth(this.getMaxHealth());
        this.getNavigation().setCanFloat(true);
        this.xpReward = 50;

        setPhase(IDLE);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        AdjudicatorRoomListener.enableAdjudicator(this);    //Listen for break/place events for this adjudicator
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        getEntityData().define(STATE, 0);
        getEntityData().define(CHARGING, false);
        getEntityData().define(INVULNERABLE, false);
    }

    @Override
    public void tick() {
        stateTime++;
        if(!level.isClientSide() && firstTick)  //Set up arena
        {
            homePos = getOnPos();    //Home pos is generally the center of the arena. The position the adjudicator will return to if out of combat.

            roomBounds = new AABB(homePos.below(4)).inflate(13,0, 13).expandTowards(0, 13, 0);    //Get the room bounds.
            firstTick = false;

			/*
				Find valid arena locations, these are placed in the structure then processed here.
				These are the locations we can spawn things at or teleport to.
			 */
            arenaPositions = Lists.newArrayList();
            arenaPositions.add(homePos);
            BlockPos.betweenClosed((int)roomBounds.minX, (int)roomBounds.minY, (int)roomBounds.minZ, (int)roomBounds.maxX, (int)roomBounds.maxY, (int)roomBounds.maxZ)
                    .forEach(b->{
                        if(level.getBlockState(b).is(Blocks.SMOOTH_QUARTZ))   //TODO: make marker block
                        {
                            level.setBlock(b, Blocks.AIR.defaultBlockState(), 3);
                            arenaPositions.add(b.immutable());
                        }
                    });
        }

        super.tick();

        //Update the phase
        if(!level.isClientSide() && phase != null)
        {
            phase.tick();

            if(phase.isPhaseOver())
                setPhase(phase.getNextPhase());
        }

        //Update Boss bar
        this.bossBar.setProgress(this.getHealth() / this.getMaxHealth());

        //Casting Particles
        tickCastParticles();

        //Anti-cheese if we're out of the arena, teleport back in.
        if(!level.isClientSide())
        {
            if(!getArenaBounds().contains(position()))
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
        if (this.level.isClientSide && isCasting())
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

            float angle = this.yBodyRot * 0.017453292F + Mth.cos((float)this.tickCount * 0.6662F) * 0.25F;
            float xOffset = Mth.cos(angle);
            float zOffset = Mth.sin(angle);
            this.level.addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() + (double)xOffset * 0.6D, this.getY() + 1.8D, this.getZ() + (double)zOffset * 0.6D, r, g, b);
            this.level.addParticle(ParticleTypes.ENTITY_EFFECT, this.getX() - (double)xOffset * 0.6D, this.getY() + 1.8D, this.getZ() - (double)zOffset * 0.6D, r, g, b);
        }
    }

    /*
		Update tracked players
	 */
    private void updatePlayers()
    {
        if(!active || level.isClientSide())
            return;

        //If there are no valid players within the arena, teleport home and reset the fight.
        List<Player> players = level.getEntitiesOfClass(Player.class, getArenaBounds(), EntitySelector.NO_SPECTATORS);
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
        List<Player> playersWithSpectator = level.getEntitiesOfClass(Player.class, getArenaBounds(), (p)->true);
        List<ServerPlayer> toRemove = Lists.newArrayList();

        for(ServerPlayer playerEntity : bossBar.getPlayers()) //Loop through current players tracked by the boss bar
        {
            //Remove them from tracking if they're no longer valid
            if(!playersWithSpectator.contains(playerEntity))
                toRemove.add(playerEntity);
        }
        toRemove.forEach(bossBar::removePlayer);

        //Loop though valid players
        for(Player playerEntity : playersWithSpectator)
        {
            //Add them to boss bar tracked players
            if(!bossBar.getPlayers().contains(playerEntity))
                bossBar.addPlayer((ServerPlayer) playerEntity);
        }
    }

    @Override
    public boolean hurt(DamageSource damageSource, float amount) {
        //9/01/21 WTF does this comment mean
        //Activate the fight if the tracker if the player
        if(!active && damageSource.getEntity() instanceof Player)
        {
            active = true;
        }
        if(phase != null)
            phase.onHurt(damageSource, amount);

        return super.hurt(damageSource, amount);
    }

    @Override
    public boolean isDamageSourceBlocked(DamageSource damageSource) {
        //ALSO WTF DOES THIS MEAN??
        //Bug fix: ensure the adjudicator is in some sort of phase
        if(active && phase == null)
        {
            setPhase(TELEPORT);
            return true;
        }

        //Don't hurt in idle phase if attacker isn't a player
        if(phase == IDLE && !(damageSource.getEntity() instanceof Player))
            return true;

        //Don't hurt if phase is invulnerable phase
        if(phase.isInvulnerable() && !damageSource.isBypassInvul())
            return true;


        return super.isDamageSourceBlocked(damageSource);
    }

    @Override
    protected void dropCustomDeathLoot(DamageSource damageSource, int i, boolean bl) {
        //Drop totem and tapestry
        //This isn't done via loot table because we want the item to be coveted
        ItemEntity enchantedTotem = this.spawnAtLocation(BMItems.ENCHANTED_TOTEM);
        if (enchantedTotem != null)
            enchantedTotem.setExtendedLifetime();

        ItemEntity tapestry = this.spawnAtLocation(BMBlocks.ADJUDICATOR_TAPESTRY);
        if(tapestry != null)
            tapestry.setExtendedLifetime();
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
    public void onSyncedDataUpdated(EntityDataAccessor<?> data) {
        if(data == STATE)
        {
            stateTime = 0;
            renderRotPrevious = 0;

            xo = getX();
            yo = getY();
            zo = getZ();
            xOld = getX();
            yOld = getY();
            zOld = getZ();
        }
        super.onSyncedDataUpdated(data);
    }

    @Override
    public void die(DamageSource damageSource) {

        if(phase != null)
            phase.onExitPhase();

        active = false;
        bossBar.removeAllPlayers();
        bossBar.setVisible(false);
        super.die(damageSource);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer serverPlayer) {
        super.stopSeenByPlayer(serverPlayer);
        this.bossBar.removePlayer(serverPlayer);
    }

    private void setUpPhase()
    {
        //Stop pathing
        getNavigation().stop();

        //Copy AI from phase
        goalSelector.getRunningGoals().forEach(WrappedGoal::stop);
        GoalSelectorExtension.copy(goalSelector, phase.getGoalSelector());
        GoalSelectorExtension.copy(targetSelector, phase.getTargetSelector());

        bossBar.setVisible(phase.showBossBar());

        getEntityData().set(INVULNERABLE, phase.isInvulnerable());
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);

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
                arenaPosTags.add(NbtUtils.writeBlockPos(pos));
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
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        firstTick = tag.getBoolean("FirstTick");
        active = tag.getBoolean("BossActive");
        homePos = NBTUtil.readBlockPos(tag);
        roomBounds = NBTUtil.readBox(tag);

        if(tag.contains("ArenaPositions"))
        {
            ListTag arenaTags = tag.getList("ArenaPositions", Tag.TAG_COMPOUND);
            this.arenaPositions = Lists.newArrayList();
            for(int i = 0; i < arenaTags.size(); i++)
            {
                arenaPositions.add(NbtUtils.readBlockPos(arenaTags.getCompound(i)));
            }
        }

        ResourceLocation phaseID = new ResourceLocation(tag.getString("Phase"));
        AdjudicatorPhase adjPhase = PHASES.get(phaseID);
        adjPhase.fromTag(tag.getCompound("PhaseData"));
        this.phase = adjPhase;
        setUpPhase();

        setState(AdjudicatorState.values()[tag.getInt("State")]);
    }

    /*
        Select a random player in the room rather than focusing on a single person.
 */
    private final TargetingConditions targetPredicate = TargetingConditions.forCombat().range(32);
    public void selectTarget(Class<? extends LivingEntity> targetClass) {
        LivingEntity targetEntity = null;

        if (targetClass != Player.class && targetClass != ServerPlayer.class) {
            targetEntity = level.getNearestEntity(targetClass, this.targetPredicate, this, getX(), getEyeY(), getZ(), getArenaBounds());
        } else {
            targetEntity = level.getNearestPlayer(this.targetPredicate, this, getX(), getEyeY(), getZ());
        }

        if(targetEntity != null)
            setTarget(targetEntity);
    }

    public void setActive()
    {
        this.active = true;
    }

    public BlockPos getHomePosition()
    {
        return homePos;
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
    public void setCustomName(@Nullable Component component) {
        super.setCustomName(component);
        this.bossBar.setName(this.getDisplayName());
    }

    /*
        Don't slow down (cobwebs, berry bushes, etc).
    */
    @Override
    public void makeStuckInBlock(BlockState blockState, Vec3 vec3) {

    }

    /*
        Don't Despawn.
 */
    @Override
    public void checkDespawn() {
        this.noActionTime = 0;
    }

    /*
            Don't go through portals.
     */
    @Override
    public boolean canChangeDimensions() {
        return false;
    }

    /*
        Don't take fall damage
 */
    @Override
    public boolean causeFallDamage(float f, float g, DamageSource damageSource) {
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
    public MobType getMobType() {
        return MobType.ILLAGER;
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
            }while(arenaPos.closerThan(getOnPos(), 1));
            return arenaPos;
        }
        return null;
    }

    @Override
    public AdjudicatorState getState()
    {
        return AdjudicatorState.values()[getEntityData().get(STATE) % AdjudicatorState.values().length];
    }

    public void setState(AdjudicatorState state)
    {
        getEntityData().set(STATE, state.ordinal());
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
        if(level.getBlockState(pos.below()).isAir())
            level.setBlock(pos.below(), Blocks.COBBLESTONE.defaultBlockState(), 3);

        this.moveTo(pos.getX() + 0.5F, pos.getY() + 1F, pos.getZ() + 0.5F);
        clearArea(this);
    }

    /*
		Anti-Cheese: Break blocks around an entity to prevent trapping
	 */
    public void clearArea(Entity e)
    {
        if(this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING))
        {
            AABB hitBox = e.getBoundingBox();
            destroyArea(hitBox);

            if(e.isInWall())
            {
                hitBox = hitBox.inflate(1F);
                destroyArea(hitBox);
            }
        }
    }

    private void destroyArea(AABB hitBox)
    {
        BlockPos.
                betweenClosed(new BlockPos((int) hitBox.minX, (int) hitBox.minY, (int) hitBox.minZ),
                        new BlockPos((int) hitBox.maxX, (int) hitBox.maxY, (int) hitBox.maxZ))
                .forEach(b->
                {
                    if(WitherBoss.canDestroy(level.getBlockState(b)))
                    {
                        this.level.destroyBlock(b, true, this);
                        this.level.levelEvent(null, 1022, b, 0);
                    }
                });
    }

    public boolean isCasting()
    {
        AdjudicatorState state = getState();
        return state == AdjudicatorState.SUMMONING || state == AdjudicatorState.TELEPORT;
    }

    public AABB getArenaBounds()
    {
        return roomBounds;
    }

    @Override
    public boolean isPowered() {
        return getEntityData().get(INVULNERABLE);
    }

    @Override
    public void setChargingCrossbow(boolean bl) {
        this.getEntityData().set(CHARGING, bl);
    }

    public boolean isChargingCrossbow()
    {
        return getEntityData().get(CHARGING);
    }

    @Override
    public void shootCrossbowProjectile(LivingEntity target, ItemStack itemStack, Projectile projectile, float multiShotSpray) {
        this.shootCrossbowProjectile(this, target, projectile, multiShotSpray, 1.6F);
    }

    @Override
    public void onCrossbowAttackPerformed() {

    }

    @Override
    public void remove(RemovalReason removalReason) {
        super.remove(removalReason);
        AdjudicatorRoomListener.disableAdjudicator(this);
    }

    @Override
    public boolean canAttack(LivingEntity target) {
        if(getVehicle() == target)
            return false;

        if(target instanceof PlayerCreatable && !((PlayerCreatable) target).isPlayerCreated())
            return false;

        return super.canAttack(target);
    }

    @Override
    public void performRangedAttack(LivingEntity target, float pullProgress)
    {
        if(getMainHandItem().getItem() instanceof CrossbowItem)    //Handle crossbow shoot
        {
            performCrossbowAttack(this, 2F);
        }
        else    //Handle bow shoot
        {
            ItemStack arrowTypeStack = this.getProjectile(this.getItemInHand(BMUtil.getHandPossiblyHolding(this, (i)->i.getItem() instanceof BowItem)));
            AbstractArrow arrow = ProjectileUtil.getMobArrow(this, arrowTypeStack, pullProgress);

            double distanceX = target.getX() - this.getX();
            double distanceY = target.getY(0.333F) - arrow.getY();
            double distanceZ = target.getZ() - this.getZ();

            double arc = Math.sqrt(distanceX * distanceX + distanceZ * distanceZ);
            arrow.shoot(distanceX, distanceY + arc * 0.2F, distanceZ, 1.6F, (float) (14 - this.level.getDifficulty().getId() * 4));

            this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
            this.level.addFreshEntity(arrow);
        }
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 255F)
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .add(Attributes.ATTACK_DAMAGE, 3F);
    }
}
