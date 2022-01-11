package party.lemons.biomemakeover.entity.adjudicator.phase;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;

public class IdleAdjudicatorPhase extends AdjudicatorPhase
{
    public IdleAdjudicatorPhase(ResourceLocation id, AdjudicatorEntity adjudicator)
    {
        super(id, adjudicator);
    }

    @Override
    protected void initAI()
    {

    }

    @Override
    public void tick()
    {
        super.tick();
        if(adjudicator.tickCount % 4 == 0)
            adjudicator.heal(1F);
    }

    @Override
    public boolean showBossBar()
    {
        return false;
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
    public boolean isSelectable()
    {
        return false;
    }

    @Override
    public boolean isPhaseOver()
    {
        return adjudicator.isActive();
    }
}