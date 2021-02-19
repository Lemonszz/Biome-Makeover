package party.lemons.biomemakeover.entity.adjudicator.phase;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Identifier;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;

public class RavagerChargePhase extends AdjudicatorPhase
{
	public RavagerChargePhase(Identifier phaseID, AdjudicatorEntity adjudicator)
	{
		super(phaseID, adjudicator);
	}

	@Override
	protected void initAI()
	{
		this.goalSelector.add(0, new LookAtEntityGoal(adjudicator, PlayerEntity.class, 20.0F));
		this.targetSelector.add(1, new RevengeGoal(adjudicator));
		this.targetSelector.add(2, new FollowTargetGoal<>(adjudicator, PlayerEntity.class, true));
		this.targetSelector.add(3, new FollowTargetGoal<>(adjudicator, GolemEntity.class, true));
	}

	@Override
	public void onEnterPhase()
	{
		super.onEnterPhase();
		adjudicator.teleportToRandomArenaPos();

		RavagerEntity ravager = EntityType.RAVAGER.create(world);
		ravager.updatePositionAndAngles(adjudicator.getX(), adjudicator.getY(), adjudicator.getZ(), adjudicator.yaw, adjudicator.pitch);
		world.spawnEntity(ravager);
		adjudicator.startRiding(ravager, true);
	}

	@Override
	public boolean isPhaseOver()
	{
		return !adjudicator.hasVehicle();
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
	public boolean isInvulnerable()
	{
		return true;
	}
}
