package party.lemons.biomemakeover.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableWitchTargetGoal;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.crafting.witch.WitchQuest;
import party.lemons.biomemakeover.crafting.witch.WitchQuestEntity;
import party.lemons.biomemakeover.crafting.witch.WitchQuestHandler;
import party.lemons.biomemakeover.crafting.witch.WitchQuestList;
import party.lemons.biomemakeover.crafting.witch.data.QuestCategories;
import party.lemons.biomemakeover.entity.ai.WitchLookAtCustomerGoal;
import party.lemons.biomemakeover.entity.ai.WitchStopFollowingCustomerGoal;
import party.lemons.biomemakeover.init.BMItems;

@Mixin(Witch.class)
public class WitchMixin_Quests extends Raider implements WitchQuestEntity
{
    @Shadow private NearestAttackableWitchTargetGoal<Player> attackPlayersGoal;
    private Player customer;
    private WitchQuestList quests;
    private int replenishTime;
    private int despawnShield = 0;

    @Inject(at = @At("TAIL"), method = "<init>")
    public void onConstruct(EntityType<? extends Witch> entityType, Level world, CallbackInfo cbi)
    {
        quests = new WitchQuestList();
        quests.populate(getRandom());
        replenishTime = world.random.nextInt(24000);
    }

    @Inject(at = @At("TAIL"), method = "registerGoals")
    public void initGoals(CallbackInfo cbi)
    {
        this.targetSelector.removeGoal(attackPlayersGoal);
        attackPlayersGoal = new NearestAttackableWitchTargetGoal<>(this, Player.class, 10, true, false, (e)->e.getType() == EntityType.PLAYER && !canInteract((Player) e));
        this.targetSelector.addGoal(3, attackPlayersGoal);

        this.goalSelector.addGoal(1, new WitchStopFollowingCustomerGoal((Witch)(Object)this));
        this.goalSelector.addGoal(1, new WitchLookAtCustomerGoal((Witch)(Object)this));
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        if(this.isAlive() && !this.hasCustomer() && canInteract(player))
        {
            if(!this.level.isClientSide())
            {
                despawnShield = 12000;
                this.setCurrentCustomer(player);
                this.sendQuests(player, this.getDisplayName());
            }
            return InteractionResult.sidedSuccess(level.isClientSide());
        }else
        {
            return super.mobInteract(player, hand);
        }
    }

    @Override
    protected void dropFromLootTable(DamageSource damageSource, boolean causedByPlayer)
    {
        if(causedByPlayer && random.nextInt(20) == 0)
        {
            spawnAtLocation(new ItemStack(BMItems.WITCH_HAT.get()));
        }
        super.dropFromLootTable(damageSource, causedByPlayer);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if(!level.isClientSide())
        {
            if(despawnShield > 0) despawnShield--;

            if(replenishTime > 0) replenishTime--;
            else
            {
                for(int i = quests.size(); i < 3; i++)
                {
                    quests.add(WitchQuestHandler.createQuest(random));
                }
                replenishTime = 3000 + random.nextInt(21000);
            }
        }
    }

    @Override
    public boolean canAttack(LivingEntity target)
    {
        if(target.getType() == EntityType.PLAYER && canInteract((Player) target)) return false;

        return super.canAttack(target);
    }

    @Override
    public boolean canInteract(Player player)
    {
        return getTarget() == null && !hasActiveRaid() && playerHasHat(player) && QuestCategories.hasQuests();
    }

    public boolean playerHasHat(Player player)
    {
        return player.getItemBySlot(EquipmentSlot.HEAD).getItem() == BMItems.WITCH_HAT.get();
    }

    @Override
    public void die(DamageSource source)
    {
        super.die(source);
        this.resetCustomer();
    }

    protected void resetCustomer()
    {
        this.setCurrentCustomer(null);
    }

    public void setCurrentCustomer(Player customer)
    {
        this.customer = customer;
    }

    public Player getCurrentCustomer()
    {
        return customer;
    }

    public WitchQuestList getQuests()
    {
        return quests;
    }

    public void setQuestsFromServer(WitchQuestList quests)
    {
        this.quests = quests;
    }

    public void completeQuest(WitchQuest quest)
    {

    }

    @Override
    public Level getWitchLevel()
    {
        return level;
    }

    public SoundEvent getYesSound()
    {
        return SoundEvents.WITCH_CELEBRATE;
    }

    public boolean hasCustomer()
    {
        return getCurrentCustomer() != null;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
        tag.put("Quests", quests.toTag());
        tag.putInt("DespawnShield", despawnShield);
        tag.putInt("ReplenishTime", replenishTime);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        quests = new WitchQuestList(tag.getCompound("Quests"));
        despawnShield = tag.getInt("DespawnShield");
        replenishTime = tag.getInt("ReplenishTime");
    }

    @Override
    public boolean removeWhenFarAway(double d) {
        if(despawnShield > 0) return false;

        return super.removeWhenFarAway(d);
    }

    @Override
    public boolean requiresCustomPersistence() {
        if(despawnShield > 0) return true;

        return super.requiresCustomPersistence();
    }

    @Override
    public void applyRaidBuffs(int i, boolean bl) {

    }

    @Override
    public SoundEvent getCelebrateSound() {
        return null;
    }

    protected WitchMixin_Quests(EntityType<? extends Raider> entityType, Level level) {
        super(entityType, level);
    }
}
