package party.lemons.biomemakeover.entity.adjudicator.phase;

import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;

public abstract class AttackingPhase extends TimedPhase
{
	public AttackingPhase(Identifier id, AdjudicatorEntity adjudicator)
	{
		super(id, 200, adjudicator);
	}

	@Override
	protected void initAI()
	{
		this.goalSelector.add(0, new SwimGoal(adjudicator));
		this.goalSelector.add(1, new WanderAroundGoal(adjudicator, 1.0D));
		this.goalSelector.add(2, getAttackGoal());
		this.goalSelector.add(3, new LookAtEntityGoal(adjudicator, PlayerEntity.class, 20.0F));
		this.goalSelector.add(4, new LookAroundGoal(adjudicator));
		this.targetSelector.add(1, new RevengeGoal(adjudicator));
		this.targetSelector.add(2, new FollowTargetGoal<>(adjudicator, PlayerEntity.class, true));
		this.targetSelector.add(3, new FollowTargetGoal<>(adjudicator, GolemEntity.class, true));
	}

	protected abstract Goal getAttackGoal();

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
