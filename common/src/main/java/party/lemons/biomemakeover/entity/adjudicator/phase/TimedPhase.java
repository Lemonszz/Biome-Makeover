package party.lemons.biomemakeover.entity.adjudicator.phase;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;

public abstract class TimedPhase extends AdjudicatorPhase
{
    protected int time = 0;
    protected final int maxTime;

    public TimedPhase(ResourceLocation id, int maxTime, AdjudicatorEntity adjudicator)
    {
        super(id, adjudicator);

        this.maxTime = maxTime;
    }

    @Override
    public void tick()
    {
        time++;
    }

    @Override
    public void onExitPhase()
    {
        super.onExitPhase();
        time = 0;
    }

    @Override
    public CompoundTag toTag()
    {
        CompoundTag tag = new CompoundTag();
        tag.putInt("time", time);
        return tag;
    }

    @Override
    public void fromTag(CompoundTag tag)
    {
        time = tag.getInt("time");
    }

    @Override
    public boolean isPhaseOver()
    {
        return time >= maxTime;
    }
}