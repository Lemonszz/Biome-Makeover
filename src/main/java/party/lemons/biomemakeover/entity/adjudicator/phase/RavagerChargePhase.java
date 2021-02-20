package party.lemons.biomemakeover.entity.adjudicator.phase;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.CrossbowAttackGoal;
import net.minecraft.entity.ai.goal.FollowTargetGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorState;
import party.lemons.biomemakeover.entity.ai.MountedCrossbowAttackGoal;

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
		this.goalSelector.add(1, new MountedCrossbowAttackGoal<>(adjudicator, 25.0F));

		this.targetSelector.add(1, new RevengeGoal(adjudicator));
		this.targetSelector.add(2, new FollowTargetGoal<>(adjudicator, PlayerEntity.class, false));
		this.targetSelector.add(3, new FollowTargetGoal<>(adjudicator, GolemEntity.class, true));
	}

	@Override
	public void onEnterPhase()
	{
		super.onEnterPhase();
		adjudicator.setState(AdjudicatorState.FIGHTING);
		RavagerEntity ravager = EntityType.RAVAGER.create(world);
		ravager.updatePositionAndAngles(adjudicator.getX(), adjudicator.getY(), adjudicator.getZ(), adjudicator.yaw, adjudicator.pitch);
		world.spawnEntity(ravager);
		adjudicator.startRiding(ravager, true);

		ItemStack stack = new ItemStack(Items.CROSSBOW);
		stack.addEnchantment(Enchantments.MULTISHOT, 3);
		adjudicator.setStackInHand(Hand.MAIN_HAND, stack);
	}

	@Override
	public void tick()
	{
		super.tick();
	}

	@Override
	public void onExitPhase()
	{
		adjudicator.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
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
