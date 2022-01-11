package party.lemons.biomemakeover.entity.adjudicator.phase;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.AbstractGolem;
import net.minecraft.world.entity.player.Player;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;

public abstract class AttackingPhase extends TimedPhase
{
    public AttackingPhase(ResourceLocation id, AdjudicatorEntity adjudicator)
    {
        super(id, 200, adjudicator);
    }

    @Override
    protected void initAI()
    {
        this.goalSelector.addGoal(0, new FloatGoal(adjudicator));
        this.goalSelector.addGoal(1, new RandomStrollGoal(adjudicator, 1.0D));
        this.goalSelector.addGoal(2, getAttackGoal());
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(adjudicator, Player.class, 20.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(adjudicator));
        this.targetSelector.addGoal(1, new HurtByTargetGoal(adjudicator));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(adjudicator, Player.class, false));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(adjudicator, AbstractGolem.class, false));
    }

    protected abstract Goal getAttackGoal();

    @Override
    public void onEnterPhase()
    {
        super.onEnterPhase();
        adjudicator.selectTarget(Player.class);
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
}