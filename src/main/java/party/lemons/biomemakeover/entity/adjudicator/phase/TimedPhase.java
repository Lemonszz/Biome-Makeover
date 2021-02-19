package party.lemons.biomemakeover.entity.adjudicator.phase;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;

public abstract class TimedPhase extends AdjudicatorPhase
{
	private int time = 0;
	private final int maxTime;

	public TimedPhase(Identifier id, int maxTime, AdjudicatorEntity adjudicator)
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
