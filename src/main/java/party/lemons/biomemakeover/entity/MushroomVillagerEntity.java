package party.lemons.biomemakeover.entity;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.passive.AbstractTraderEntity;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.WanderingTraderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.TraderOfferList;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import party.lemons.biomemakeover.entity.ai.DoorOpenInteractGoal;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.init.BMItems;

public class MushroomVillagerEntity extends AbstractTraderEntity
{
	public AttributeContainer attributeContainer;


	public MushroomVillagerEntity(World world)
	{
		super(BMEntities.MUSHROOM_TRADER, world);

		((MobNavigation)this.getNavigation()).setCanPathThroughDoors(false);
		this.getNavigation().setCanSwim(true);
	}

	protected void initGoals() {
		this.goalSelector.add(0, new SwimGoal(this));
		this.goalSelector.add(1, new StopFollowingCustomerGoal(this));
		this.goalSelector.add(1, new DoorOpenInteractGoal(this, true));
		this.goalSelector.add(2, new FleeEntityGoal(this, ZombieEntity.class, 8.0F, 0.5D, 0.5D));
		this.goalSelector.add(2, new FleeEntityGoal(this, EvokerEntity.class, 12.0F, 0.5D, 0.5D));
		this.goalSelector.add(2, new FleeEntityGoal(this, VindicatorEntity.class, 8.0F, 0.5D, 0.5D));
		this.goalSelector.add(2, new FleeEntityGoal(this, VexEntity.class, 8.0F, 0.5D, 0.5D));
		this.goalSelector.add(2, new FleeEntityGoal(this, PillagerEntity.class, 15.0F, 0.5D, 0.5D));
		this.goalSelector.add(2, new FleeEntityGoal(this, IllusionerEntity.class, 12.0F, 0.5D, 0.5D));
		this.goalSelector.add(2, new FleeEntityGoal(this, ZoglinEntity.class, 10.0F, 0.5D, 0.5D));
		this.goalSelector.add(2, new EscapeDangerGoal(this, 0.5D));
		this.goalSelector.add(2, new LookAtCustomerGoal(this));
		this.goalSelector.add(3, new WanderAroundFarGoal(this, 0.25D));
		this.goalSelector.add(4, new WanderNearTargetGoal(this, 0.35F, 5.0F));
		this.goalSelector.add(5, new LookAtEntityGoal(this, MobEntity.class, 8.0F));
	}

	@Override
	public AttributeContainer getAttributes()
	{
		if(attributeContainer == null)
			attributeContainer =  new AttributeContainer(
					MobEntity.createMobAttributes().build());
		return attributeContainer;
	}

	protected void fillRecipes() {
		TradeOffers.Factory[] tradesCommon = TRADES.get(1);
		TradeOffers.Factory[] tradesRare = TRADES.get(2);
		if (tradesCommon != null && tradesRare != null) {
			TraderOfferList traderOfferList = this.getOffers();
			this.fillRecipesFromPool(traderOfferList, tradesCommon, 5);
			int i = this.random.nextInt(tradesRare.length);
			TradeOffers.Factory factory = tradesRare[i];
			TradeOffer tradeOffer = factory.create(this, this.random);
			if (tradeOffer != null) {
				traderOfferList.add(tradeOffer);
			}

		}
	}

	public ActionResult interactMob(PlayerEntity player, Hand hand) {
		ItemStack itemStack = player.getStackInHand(hand);
		if (itemStack.getItem() != Items.VILLAGER_SPAWN_EGG && this.isAlive() && !this.hasCustomer() && !this.isBaby()) {
			if (hand == Hand.MAIN_HAND) {
				player.incrementStat(Stats.TALKED_TO_VILLAGER);
			}

			if (this.getOffers().isEmpty()) {
				return ActionResult.success(this.world.isClient);
			} else {
				if (!this.world.isClient) {
					this.setCurrentCustomer(player);
					this.sendOffers(player, this.getDisplayName(), 1);
				}

				return ActionResult.success(this.world.isClient);
			}
		} else {
			return super.interactMob(player, hand);
		}
	}

	@Override
	public boolean canSpawn(WorldView world) {
		return super.canSpawn(world);
	}

	@Override
	public boolean canSpawn(WorldAccess world, SpawnReason spawnReason) {
		if(getY() > 56)
			return false;

		//BatEntity
		//return super.canSpawn(world, spawnReason);
		return world.getEntitiesByClass(MushroomVillagerEntity.class, new Box(new BlockPos(getX(), getY(), getZ())).expand(20), (e)->true).isEmpty() &&  super.canSpawn(world, spawnReason);
	}

	@Override
	public int getLimitPerChunk() {
		return 1;
	}

	@Override
	public boolean isLeveledTrader()
	{
		return false;
	}

	protected void afterUsing(TradeOffer offer) {
		if (offer.shouldRewardPlayerExperience()) {
			int i = 3 + this.random.nextInt(4);
			this.world.spawnEntity(new ExperienceOrbEntity(this.world, this.getX(), this.getY() + 0.5D, this.getZ(), i));
		}

	}

	protected SoundEvent getTradingSound(boolean sold) {
		return sold ? SoundEvents.ENTITY_WANDERING_TRADER_YES : SoundEvents.ENTITY_WANDERING_TRADER_NO;
	}

	public SoundEvent getYesSound() {
		return SoundEvents.ENTITY_WANDERING_TRADER_YES;
	}

	protected SoundEvent getAmbientSound() {
		return this.hasCustomer() ? SoundEvents.ENTITY_WANDERING_TRADER_TRADE : SoundEvents.ENTITY_WANDERING_TRADER_AMBIENT;
	}

	protected SoundEvent getHurtSound(DamageSource source) {
		return SoundEvents.ENTITY_WANDERING_TRADER_HURT;
	}

	protected SoundEvent getDeathSound() {
		return SoundEvents.ENTITY_WANDERING_TRADER_DEATH;
	}


	@Override
	public PassiveEntity createChild(ServerWorld world, PassiveEntity entity)
	{
		return null;
	}

	public static final Int2ObjectMap<TradeOffers.Factory[]> TRADES =
			new Int2ObjectOpenHashMap(
					ImmutableMap.of(1, new TradeOffers.Factory[]{
							new TradeOffers.SellItemFactory(Items.BROWN_MUSHROOM, 1, 3, 12, 1),
							new TradeOffers.SellItemFactory(Items.RED_MUSHROOM, 1, 3, 12, 1),
							new TradeOffers.SellItemFactory(BMBlocks.PURPLE_GLOWSHROOM, 2, 3, 12, 1),
							new TradeOffers.SellItemFactory(BMBlocks.ORANGE_GLOWSHROOM, 2, 3, 12, 1),
							new TradeOffers.SellItemFactory(BMBlocks.GREEN_GLOWSHROOM, 2, 3, 12, 1),
							new TradeOffers.SellItemFactory(BMBlocks.GREEN_GLOWSHROOM_BLOCK, 2, 2, 12, 1),
							new TradeOffers.SellItemFactory(BMBlocks.ORANGE_GLOWSHROOM_BLOCK, 2, 2, 12, 1),
							new TradeOffers.SellItemFactory(BMBlocks.PURPLE_GLOWSHROOM_BLOCK, 2, 2, 12, 1),
							new TradeOffers.SellItemFactory(BMBlocks.TALL_BROWN_MUSHROOM, 1, 2, 12, 1),
							new TradeOffers.SellItemFactory(BMBlocks.TALL_RED_MUSHROOM, 1, 2, 12, 1),
							new TradeOffers.SellItemFactory(BMBlocks.GLOWSHROOM_STEM, 2, 2, 12, 1),
							new TradeOffers.SellItemFactory(Blocks.MUSHROOM_STEM, 1, 2, 12, 1),
							new TradeOffers.SellItemFactory(Blocks.RED_MUSHROOM_BLOCK, 1, 2, 12, 1),
							new TradeOffers.SellItemFactory(Blocks.BROWN_MUSHROOM_BLOCK, 1, 2, 12, 1),
							new TradeOffers.SellItemFactory(BMBlocks.MYCELIUM_ROOTS, 1, 4, 12, 1)
						},
						2, new TradeOffers.Factory[]{
								new TradeOffers.SellItemFactory(BMItems.BUTTON_MUSHROOMS_MUSIC_DISK, 8, 1, 4, 1),
								new TradeOffers.SellItemFactory(BMItems.GLOWSHROOM_STEW, 1, 1, 4, 1)
					}));

}
