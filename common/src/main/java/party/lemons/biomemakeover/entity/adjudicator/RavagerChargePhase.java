package party.lemons.biomemakeover.entity.adjudicator;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import party.lemons.biomemakeover.entity.adjudicator.phase.AdjudicatorPhase;
import party.lemons.biomemakeover.entity.ai.MountedCrossbowAttackGoal;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.util.extension.LootBlocker;

public class RavagerChargePhase extends AdjudicatorPhase
{
    public RavagerChargePhase(ResourceLocation phaseID, AdjudicatorEntity adjudicator)
    {
        super(phaseID, adjudicator);
    }

    @Override
    protected void initAI()
    {
        this.goalSelector.addGoal(0, new LookAtPlayerGoal(adjudicator, Player.class, 20.0F));
        this.goalSelector.addGoal(1, new MountedCrossbowAttackGoal<>(adjudicator, 25.0F));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(adjudicator));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(adjudicator, Player.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(adjudicator, AbstractGolem.class, true));
    }

    @Override
    public void onEnterPhase()
    {
        super.onEnterPhase();
        adjudicator.setState(AdjudicatorState.FIGHTING);
        Ravager ravager = EntityType.RAVAGER.create(level);
        ((LootBlocker)ravager).setLootBlocked(true);
        ravager.moveTo(adjudicator.getX(), adjudicator.getY(), adjudicator.getZ(), adjudicator.getYRot(), adjudicator.getXRot());
        level.addFreshEntity(ravager);
        adjudicator.clearArea(ravager);
        adjudicator.startRiding(ravager, true);

        ItemStack stack = new ItemStack(Items.CROSSBOW);
        stack.enchant(Enchantments.MULTISHOT, 3);
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

        if(adjudicator.getVehicle() instanceof Ravager)
        {
            Ravager ravagerEntity = (Ravager) adjudicator.getVehicle();
            adjudicator.stopRiding();
            ravagerEntity.remove(Entity.RemovalReason.DISCARDED);
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