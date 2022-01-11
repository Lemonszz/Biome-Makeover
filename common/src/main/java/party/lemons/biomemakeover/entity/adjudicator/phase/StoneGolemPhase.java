package party.lemons.biomemakeover.entity.adjudicator.phase;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import party.lemons.biomemakeover.entity.StoneGolemEntity;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorState;
import party.lemons.biomemakeover.entity.ai.NonMovingBowAttackGoal;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.util.extension.LootBlocker;

public class StoneGolemPhase extends AttackingPhase
{
    public StoneGolemPhase(ResourceLocation phaseID, AdjudicatorEntity adjudicator)
    {
        super(phaseID, adjudicator);
    }

    @Override
    protected void initAI()
    {
        this.goalSelector.addGoal(2, getAttackGoal());
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(adjudicator, Player.class, 20.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(adjudicator));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(adjudicator));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(adjudicator, Player.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(adjudicator, AbstractGolem.class, false));
    }

    @Override
    protected Goal getAttackGoal()
    {
        return new NonMovingBowAttackGoal<>(adjudicator, 12, 30);
    }

    @Override
    public void onEnterPhase()
    {
        super.onEnterPhase();
        adjudicator.setState(AdjudicatorState.FIGHTING);

        StoneGolemEntity golem = BMEntities.STONE_GOLEM.create(level);
        ((LootBlocker)golem).setLootBlocked(true);
        golem.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.CROSSBOW));
        golem.moveTo(adjudicator.getX(), adjudicator.getY(), adjudicator.getZ(), adjudicator.getYRot(), adjudicator.getXRot());
        level.addFreshEntity(golem);
        adjudicator.startRiding(golem, true);
        adjudicator.clearArea(golem);

        ItemStack stack = new ItemStack(Items.BOW);
        stack.enchant(Enchantments.PUNCH_ARROWS, 1);
        adjudicator.setItemInHand(InteractionHand.MAIN_HAND, stack);

        adjudicator.playSound(BMEffects.ADJUDICATOR_SPELL_GRUNT, 1F, 1F);
    }

    @Override
    public void tick()
    {
        super.tick();
        adjudicator.selectTarget(Player.class);
    }

    @Override
    public void onExitPhase()
    {
        adjudicator.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);

        if(adjudicator.getVehicle() instanceof StoneGolemEntity golem)
        {
            adjudicator.stopRiding();
            golem.remove(Entity.RemovalReason.DISCARDED);
        }
    }

    @Override
    public boolean isPhaseOver()
    {
        return !adjudicator.isPassenger();
    }

    @Override
    public CompoundTag toTag()
    {
        return new CompoundTag();
    }

    @Override
    public void fromTag(CompoundTag tag)
    {

    }

    @Override
    public BlockPos getStartPosition()
    {
        return adjudicator.getHomePosition();
    }

    @Override
    public boolean isInvulnerable()
    {
        return true;
    }
}