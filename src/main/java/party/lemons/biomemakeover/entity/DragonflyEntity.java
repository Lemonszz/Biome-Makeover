package party.lemons.biomemakeover.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.Flutterer;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import party.lemons.biomemakeover.entity.ai.FlyWanderAroundGoal;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.util.RandomUtil;
import party.lemons.biomemakeover.util.sound.DragonflySoundInstance;

public class DragonflyEntity extends ToadTargetEntity implements Flutterer
{
	public boolean hasPlayedLoop = false;
	private AttributeContainer attributeContainer;
	private static final TrackedData<Integer> VARIANT = DataTracker.registerData(DragonflyEntity.class, TrackedDataHandlerRegistry.INTEGER);

	public DragonflyEntity(World world)
	{
		super(BMEntities.DRAGONFLY, world);
		this.moveControl = new FlightMoveControl(this, 20, true);
		this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
		this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
		this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 16.0F);
		this.setPathfindingPenalty(PathNodeType.COCOA, -1.0F);
		this.setPathfindingPenalty(PathNodeType.FENCE, -1.0F);
	}

	protected void initDataTracker() {
		super.initDataTracker();
		this.dataTracker.startTracking(VARIANT, 2);
	}

	public float getPathfindingFavor(BlockPos pos, WorldView world) {
		return world.getBlockState(pos).isAir() ? 10.0F : 0.0F;
	}

	protected void initGoals() {
		this.goalSelector.add(8, new FlyWanderAroundGoal(this));
		this.goalSelector.add(9, new SwimGoal(this));
	}

	@Override
	public void writeCustomDataToTag(CompoundTag tag)
	{
		super.writeCustomDataToTag(tag);
		tag.putInt("Variant", getVariant());
	}

	@Override
	public void readCustomDataFromTag(CompoundTag tag)
	{
		super.readCustomDataFromTag(tag);
		dataTracker.set(VARIANT, tag.getInt("Variant"));
	}

	public int getVariant()
	{
		return dataTracker.get(VARIANT);
	}

	@Override
	public AttributeContainer getAttributes()
	{
		if(attributeContainer == null)
			attributeContainer =  new AttributeContainer(
					MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_FLYING_SPEED, 0.6F).add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D).build());
		return attributeContainer;
	}

	public boolean handleFallDamage(float fallDistance, float damageMultiplier) {
		return false;
	}

	protected void fall(double heightDifference, boolean onGround, BlockState landedState, BlockPos landedPosition) {
	}

	protected boolean hasWings() {
		return true;
	}

	@Override
	public EntityData initialize(ServerWorldAccess world, LocalDifficulty difficulty, SpawnReason spawnReason, EntityData entityData, CompoundTag entityTag)
	{
		int variant = RandomUtil.randomBias(0, 5);
		dataTracker.set(VARIANT, variant);

		return super.initialize(world, difficulty, spawnReason, entityData, entityTag);
	}

	@Override
	public void setEntityId(int id)
	{
		super.setEntityId(id);
	}

	protected void playStepSound(BlockPos pos, BlockState state) {
	}

	protected SoundEvent getAmbientSound() {
		return null;
	}

	protected SoundEvent getHurtSound(DamageSource source) {
		return BMEffects.DRAGONFLY_HURT;
	}

	protected SoundEvent getDeathSound() {
		return BMEffects.DRAGONFLY_DEATH;
	}

	protected float getSoundVolume() {
		return 0.4F;
	}

	protected EntityNavigation createNavigation(World world) {
		BirdNavigation birdNavigation = new BirdNavigation(this, world) {
			public boolean isValidPosition(BlockPos pos) {
				return !this.world.getBlockState(pos.down()).isAir();
			}
		};
		birdNavigation.setCanPathThroughDoors(false);
		birdNavigation.setCanSwim(false);
		birdNavigation.setCanEnterOpenDoors(true);
		return birdNavigation;
	}
}
