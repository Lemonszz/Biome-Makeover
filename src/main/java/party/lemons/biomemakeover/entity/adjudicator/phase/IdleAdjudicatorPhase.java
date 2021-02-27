package party.lemons.biomemakeover.entity.adjudicator.phase;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;

public class IdleAdjudicatorPhase extends AdjudicatorPhase
{
	public IdleAdjudicatorPhase(Identifier id, AdjudicatorEntity adjudicator)
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
		if(adjudicator.age % 4 == 0)
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
