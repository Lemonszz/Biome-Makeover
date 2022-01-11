package party.lemons.biomemakeover.entity;

import com.google.common.collect.ImmutableMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.monster.*;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMItems;
import party.lemons.biomemakeover.util.MerchantTrades;

public class MushroomVillagerEntity extends AbstractVillager {

    public MushroomVillagerEntity(EntityType<? extends AbstractVillager> type, Level level) {
        super(type, level);

        ((GroundPathNavigation)getNavigation()).setCanOpenDoors(true);
        this.getNavigation().setCanFloat(true);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new TradeWithPlayerGoal(this));
        this.goalSelector.addGoal(1, new OpenDoorGoal(this, true));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Zombie.class, 8.0F, 0.5D, 0.5D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Evoker.class, 12.0F, 0.5D, 0.5D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Vindicator.class, 8.0F, 0.5D, 0.5D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Vex.class, 8.0F, 0.5D, 0.5D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Pillager.class, 15.0F, 0.5D, 0.5D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Illusioner.class, 12.0F, 0.5D, 0.5D));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, Zoglin.class, 10.0F, 0.5D, 0.5D));
        this.goalSelector.addGoal(2, new PanicGoal(this, 0.5D));
        this.goalSelector.addGoal(2, new LookAtTradingPlayerGoal(this));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 0.25D));
        this.goalSelector.addGoal(4, new MoveTowardsTargetGoal(this, 0.35F, 5.0F));
        this.goalSelector.addGoal(5, new LookAtPlayerGoal(this, Mob.class, 8.0F));
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if(itemStack.getItem() != Items.VILLAGER_SPAWN_EGG && this.isAlive() && !this.isTrading() && !this.isBaby())
        {
            if(hand == InteractionHand.MAIN_HAND)
            {
                player.awardStat(Stats.TALKED_TO_VILLAGER);
            }

            if(this.getOffers().isEmpty())
            {
                return InteractionResult.sidedSuccess(this.level.isClientSide());
            }else
            {
                if(!this.level.isClientSide())
                {
                    this.setTradingPlayer(player);
                    this.openTradingScreen(player, this.getDisplayName(), 1);
                }

                return InteractionResult.sidedSuccess(this.level.isClientSide());
            }
        }else
        {
            return super.mobInteract(player, hand);
        }
    }

    @Override
    public boolean checkSpawnRules(LevelAccessor level, MobSpawnType mobSpawnType) {
        if(getY() > 56) return false;

        return level.getEntitiesOfClass(MushroomVillagerEntity.class, new AABB(new BlockPos(getX(), getY(), getZ())).inflate(20), (e)->true).isEmpty() && super.checkSpawnRules(level, mobSpawnType);
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 1;
    }

    @Override
    protected void rewardTradeXp(MerchantOffer merchantOffer) {
        if(merchantOffer.shouldRewardExp())
        {
            int i = 3 + this.random.nextInt(4);
            this.level.addFreshEntity(new ExperienceOrb(this.level, this.getX(), this.getY() + 0.5D, this.getZ(), i));
        }
    }

    @Override
    protected void updateTrades() {
        VillagerTrades.ItemListing[] tradesCommon = TRADES.get(1);
        VillagerTrades.ItemListing[] tradesRare = TRADES.get(2);
        if(tradesCommon != null && tradesRare != null)
        {
            MerchantOffers traderOfferList = this.getOffers();
            this.addOffersFromItemListings(traderOfferList, tradesCommon, 5);
            int i = this.random.nextInt(tradesRare.length);
            VillagerTrades.ItemListing factory = tradesRare[i];
            MerchantOffer tradeOffer = factory.getOffer(this, this.random);
            if(tradeOffer != null)
            {
                traderOfferList.add(tradeOffer);
            }

        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    @Override
    public boolean showProgressBar() {
        return false;
    }

    @Override
    protected SoundEvent getTradeUpdatedSound(boolean sold) {
        return sold ? SoundEvents.WANDERING_TRADER_YES : SoundEvents.WANDERING_TRADER_NO;
    }

    @Override
    public SoundEvent getNotifyTradeSound() {
        return SoundEvents.WANDERING_TRADER_YES;
    }

    @Override
    protected SoundEvent getAmbientSound()
    {
        return this.isTrading() ? SoundEvents.WANDERING_TRADER_TRADE : SoundEvents.WANDERING_TRADER_AMBIENT;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.WANDERING_TRADER_HURT;
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.WANDERING_TRADER_DEATH;
    }

    public static final Int2ObjectMap<VillagerTrades.ItemListing[]> TRADES = new Int2ObjectOpenHashMap<>(ImmutableMap.of(1, new VillagerTrades.ItemListing[]{new MerchantTrades.ItemsForEmeralds(Items.BROWN_MUSHROOM, 1, 3, 12, 1), new MerchantTrades.ItemsForEmeralds(Items.RED_MUSHROOM, 1, 3, 12, 1), new MerchantTrades.ItemsForEmeralds(BMBlocks.PURPLE_GLOWSHROOM, 2, 3, 12, 1), new MerchantTrades.ItemsForEmeralds(BMBlocks.ORANGE_GLOWSHROOM, 2, 3, 12, 1), new MerchantTrades.ItemsForEmeralds(BMBlocks.GREEN_GLOWSHROOM, 2, 3, 12, 1), new MerchantTrades.ItemsForEmeralds(BMBlocks.GREEN_GLOWSHROOM_BLOCK, 2, 2, 12, 1), new MerchantTrades.ItemsForEmeralds(BMBlocks.ORANGE_GLOWSHROOM_BLOCK, 2, 2, 12, 1), new MerchantTrades.ItemsForEmeralds(BMBlocks.PURPLE_GLOWSHROOM_BLOCK, 2, 2, 12, 1), new MerchantTrades.ItemsForEmeralds(BMBlocks.TALL_BROWN_MUSHROOM, 1, 2, 12, 1), new MerchantTrades.ItemsForEmeralds(BMBlocks.TALL_RED_MUSHROOM, 1, 2, 12, 1), new MerchantTrades.ItemsForEmeralds(BMBlocks.GLOWSHROOM_STEM, 2, 2, 12, 1), new MerchantTrades.ItemsForEmeralds(Blocks.MUSHROOM_STEM, 1, 2, 12, 1), new MerchantTrades.ItemsForEmeralds(Blocks.RED_MUSHROOM_BLOCK, 1, 2, 12, 1), new MerchantTrades.ItemsForEmeralds(Blocks.BROWN_MUSHROOM_BLOCK, 1, 2, 12, 1), new MerchantTrades.ItemsForEmeralds(BMBlocks.MYCELIUM_ROOTS, 1, 4, 12, 1)}, 2, new VillagerTrades.ItemListing[]{new MerchantTrades.ItemsForEmeralds(BMItems.BUTTON_MUSHROOMS_MUSIC_DISK, 8, 1, 4, 1), new MerchantTrades.ItemsForEmeralds(BMItems.GLOWSHROOM_STEW, 1, 1, 4, 1)}));

}
