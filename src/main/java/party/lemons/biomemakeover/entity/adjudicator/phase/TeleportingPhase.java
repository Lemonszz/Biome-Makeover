package party.lemons.biomemakeover.entity.adjudicator.phase;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorState;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.util.NBTUtil;
import party.lemons.biomemakeover.util.NetworkUtil;

import java.util.List;
import java.util.stream.Collectors;

public class TeleportingPhase extends TimedPhase
{
	private BlockPos teleportPos;
	private AdjudicatorPhase nextPhase;

	public TeleportingPhase(Identifier phaseID, AdjudicatorEntity adjudicator)
	{
		super(phaseID, 30, adjudicator);
		teleportPos = adjudicator.getBlockPos();
	}

	@Override
	protected void initAI()
	{

	}

	@Override
	public void tick()
	{
		super.tick();
		NetworkUtil.doEnderParticles(world, adjudicator, 6);
		NetworkUtil.doCenteredEntityParticle(world, BMEffects.TELEPORT, adjudicator, 10, true);
		NetworkUtil.doBlockEnderParticles(world, teleportPos, 5);
	}

	@Override
	public void onEnterPhase()
	{
		super.onEnterPhase();
		nextPhase = selectNextPhase();
		teleportPos = nextPhase.getStartPosition();
		adjudicator.setState(AdjudicatorState.TELEPORT);
	}

	@Override
	public void onExitPhase()
	{
		super.onExitPhase();
		adjudicator.teleportTo(teleportPos);
		adjudicator.setState(AdjudicatorState.FIGHTING);
	}

	@Override
	public AdjudicatorPhase getNextPhase()
	{
		return nextPhase;
	}

	public AdjudicatorPhase selectNextPhase()
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

	@Override
	public CompoundTag toTag()
	{
		CompoundTag tag = super.toTag();
		NBTUtil.writeBlockPos(teleportPos, tag);
		return tag;
	}

	@Override
	public void fromTag(CompoundTag tag)
	{
		super.fromTag(tag);
		teleportPos = NBTUtil.readBlockPos(tag);
	}
}
