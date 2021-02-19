package party.lemons.biomemakeover.entity.adjudicator.phase;

import net.minecraft.util.Identifier;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorState;

import java.util.List;
import java.util.stream.Collectors;

public class TeleportingPhase extends TimedPhase
{
	public TeleportingPhase(Identifier phaseID, AdjudicatorEntity adjudicator)
	{
		super(phaseID, 30, adjudicator);
	}

	@Override
	protected void initAI()
	{

	}

	@Override
	public void tick()
	{
		super.tick();
	}

	@Override
	public void onEnterPhase()
	{
		super.onEnterPhase();
		adjudicator.setState(AdjudicatorState.TELEPORT);
	}

	@Override
	public void onExitPhase()
	{
		super.onExitPhase();
		adjudicator.teleportToRandomArenaPos();
		adjudicator.setState(AdjudicatorState.FIGHTING);
	}

	@Override
	public AdjudicatorPhase getNextPhase()
	{
		List<AdjudicatorPhase> phases =  adjudicator.PHASES.values().stream().filter(AdjudicatorPhase::isSelectable).collect(Collectors.toList());
		AdjudicatorPhase nextPhase;
		do{
			nextPhase = phases.get(world.random.nextInt(phases.size()));
		}while(nextPhase == this);

		return nextPhase;
	}

	@Override
	public boolean isSelectable()
	{
		return false;
	}
}
