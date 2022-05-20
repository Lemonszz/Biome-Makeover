package party.lemons.biomemakeover.entity.adjudicator.phase;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.ai.goal.GoalSelector;
import net.minecraft.world.level.Level;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;

import java.util.Random;

public abstract class AdjudicatorPhase
{
    protected final ResourceLocation phaseID;
    protected final GoalSelector goalSelector;
    protected final GoalSelector targetSelector;
    protected final Level level;
    protected final AdjudicatorEntity adjudicator;
    protected final RandomSource random;

    public AdjudicatorPhase(ResourceLocation phaseID, AdjudicatorEntity adjudicator)
    {
        this.phaseID = phaseID;
        this.adjudicator = adjudicator;
        this.level = adjudicator.level;
        this.random = level.random;

        goalSelector = new GoalSelector(level.getProfilerSupplier());
        targetSelector = new GoalSelector(level.getProfilerSupplier());
        initAI();

        adjudicator.PHASES.put(phaseID, this);
    }

    protected abstract void initAI();
    public abstract boolean isPhaseOver();

    public void tick()
    {

    }

    public void onEnterPhase()
    {

    }

    public void onExitPhase()
    {

    }

    public void onHurt(DamageSource source, float amount)
    {

    }

    public BlockPos getStartPosition()
    {
        return adjudicator.findSuitableArenaPos();
    }

    public boolean showBossBar()
    {
        return true;
    }

    public boolean isInvulnerable()
    {
        return false;
    }

    public boolean isVisible()
    {
        return false;
    }

    public abstract CompoundTag toTag();
    public abstract void fromTag(CompoundTag tag);

    public GoalSelector getGoalSelector()
    {
        return goalSelector;
    }

    public GoalSelector getTargetSelector()
    {
        return targetSelector;
    }

    public ResourceLocation getPhaseID()
    {
        return phaseID;
    }

    public boolean isSelectable()
    {
        return true;
    }

    public AdjudicatorPhase getNextPhase()
    {
        return adjudicator.TELEPORT;
    }
}