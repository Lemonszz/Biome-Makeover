package party.lemons.biomemakeover.entity.adjudicator.phase;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorState;
import party.lemons.biomemakeover.entity.event.EntityEvent;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.util.NBTUtil;
import party.lemons.biomemakeover.util.NetworkUtil;
import party.lemons.biomemakeover.util.effect.BiomeMakeoverEffect;
import party.lemons.biomemakeover.util.effect.EffectHelper;

import java.util.List;
import java.util.stream.Collectors;

public class TeleportingPhase extends TimedPhase
{
    private BlockPos teleportPos;
    private AdjudicatorPhase nextPhase;

    public TeleportingPhase(ResourceLocation phaseID, AdjudicatorEntity adjudicator)
    {
        super(phaseID, 30, adjudicator);
        teleportPos = adjudicator.getOnPos();
    }

    @Override
    protected void initAI()
    {

    }

    @Override
    public void tick()
    {
        super.tick();
        adjudicator.broadcastEvent(adjudicator, EntityEvent.ENDER_PARTICLES);
        adjudicator.broadcastEvent(adjudicator, EntityEvent.TELEPORT_PARTICLES);
        EffectHelper.doEffect(level, BiomeMakeoverEffect.BLOCK_ENDER_PARTICLES, teleportPos);
    }

    @Override
    public void onEnterPhase()
    {
        super.onEnterPhase();
        nextPhase = selectNextPhase();
        teleportPos = nextPhase.getStartPosition();
        adjudicator.setState(AdjudicatorState.TELEPORT);
        adjudicator.playSound(BMEffects.ADJUDICATOR_SPELL_3, 1F, 1F);
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
        List<AdjudicatorPhase> phases = adjudicator.PHASES.values().stream().filter(AdjudicatorPhase::isSelectable).toList();
        AdjudicatorPhase nextPhase;
        do{
            nextPhase = phases.get(level.random.nextInt(phases.size()));
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