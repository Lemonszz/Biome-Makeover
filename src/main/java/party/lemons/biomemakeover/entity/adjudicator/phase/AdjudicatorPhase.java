package party.lemons.biomemakeover.entity.adjudicator.phase;

import net.minecraft.entity.ai.goal.GoalSelector;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

public abstract class AdjudicatorPhase
{
	protected final Identifier phaseID;
	protected final GoalSelector goalSelector;
	protected final GoalSelector targetSelector;
	protected final World world;
	protected final AdjudicatorEntity adjudicator;
	protected final Random random;

	public AdjudicatorPhase(Identifier phaseID, AdjudicatorEntity adjudicator)
	{
		this.phaseID = phaseID;
		this.adjudicator = adjudicator;
		this.world = adjudicator.world;
		this.random = world.random;

		goalSelector = new GoalSelector(world.getProfilerSupplier());
		targetSelector = new GoalSelector(world.getProfilerSupplier());
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

	public Identifier getPhaseID()
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
