package party.lemons.biomemakeover.entity.adjudicator.phase;

import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.entity.passive.GolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import party.lemons.biomemakeover.entity.StoneGolemEntity;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorState;
import party.lemons.biomemakeover.entity.ai.MountedCrossbowAttackGoal;
import party.lemons.biomemakeover.entity.ai.NonMovingBowAttackGoal;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.util.extensions.LootBlocker;

public class StoneGolemPhase extends AttackingPhase
{
	public StoneGolemPhase(Identifier phaseID, AdjudicatorEntity adjudicator)
	{
		super(phaseID, adjudicator);
	}

	@Override
	protected void initAI()
	{
		this.goalSelector.add(2, getAttackGoal());
		this.goalSelector.add(3, new LookAtEntityGoal(adjudicator, PlayerEntity.class, 20.0F));
		this.goalSelector.add(4, new LookAroundGoal(adjudicator));
		this.targetSelector.add(1, new RevengeGoal(adjudicator));
		this.targetSelector.add(2, new FollowTargetGoal<>(adjudicator, PlayerEntity.class, false));
		this.targetSelector.add(3, new FollowTargetGoal<>(adjudicator, GolemEntity.class, false));
	}

	@Override
	protected Goal getAttackGoal()
	{
		return new NonMovingBowAttackGoal<>(adjudicator, 12, 30);
	}

	@Override
	public void onEnterPhase()
	{
		super.onEnterPhase();
		adjudicator.setState(AdjudicatorState.FIGHTING);
		StoneGolemEntity golem = BMEntities.STONE_GOLEM.create(world);
		((LootBlocker)golem).setLootBlocked(true);
		golem.setStackInHand(Hand.MAIN_HAND, new ItemStack(Items.CROSSBOW));
		golem.updatePositionAndAngles(adjudicator.getX(), adjudicator.getY(), adjudicator.getZ(), adjudicator.yaw, adjudicator.pitch);
		world.spawnEntity(golem);
		adjudicator.startRiding(golem, true);
		adjudicator.clearArea(golem);

		ItemStack stack = new ItemStack(Items.BOW);
		stack.addEnchantment(Enchantments.PUNCH, 1);
		adjudicator.setStackInHand(Hand.MAIN_HAND, stack);

		adjudicator.playSound(BMEffects.ADJUDICATOR_SPELL_GRUNT, 1F, 1F);
	}

	@Override
	public void tick()
	{
		super.tick();
		adjudicator.selectTarget(PlayerEntity.class);
	}

	@Override
	public void onExitPhase()
	{
		adjudicator.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);

		if(adjudicator.getVehicle() instanceof StoneGolemEntity)
		{
			StoneGolemEntity golem = (StoneGolemEntity) adjudicator.getVehicle();
			adjudicator.stopRiding();
			golem.remove();
		}
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
	public BlockPos getStartPosition()
	{
		return adjudicator.getHomePosition();
	}

	@Override
	public boolean isInvulnerable()
	{
		return true;
	}
}
