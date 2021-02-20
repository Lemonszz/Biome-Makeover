package party.lemons.biomemakeover.entity.ai;

import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.CrossbowAttackGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.IntRange;

import java.util.EnumSet;

public class MountedCrossbowAttackGoal<T extends HostileEntity & RangedAttackMob & CrossbowUser> extends Goal
{
	public static final IntRange chargeTime = new IntRange(5, 15);
	private final T actor;
	private Stage stage;
	private final float squaredRange;
	private int seeingTargetTicker;
	private int chargedTicksLeft;

	public MountedCrossbowAttackGoal(T actor, float range)
	{
		this.stage = Stage.UNCHARGED;
		this.actor = actor;
		this.squaredRange = range * range;
		this.setControls(EnumSet.of(Goal.Control.LOOK));
	}

	public boolean canStart()
	{
		return this.hasAliveTarget() && this.isEntityHoldingCrossbow();
	}

	private boolean isEntityHoldingCrossbow()
	{
		return this.actor.isHolding(Items.CROSSBOW);
	}

	public boolean shouldContinue()
	{
		return this.canStart();
	}

	private boolean hasAliveTarget()
	{
		return this.actor.getTarget() != null && this.actor.getTarget().isAlive();
	}

	public void stop()
	{
		super.stop();
		this.actor.setAttacking(false);
		this.actor.setTarget(null);
		this.seeingTargetTicker = 0;
		if (this.actor.isUsingItem())
		{
			this.actor.clearActiveItem();
			this.actor.setCharging(false);
			CrossbowItem.setCharged(this.actor.getActiveItem(), false);
		}

	}

	public void tick()
	{
		LivingEntity livingEntity = this.actor.getTarget();
		if (livingEntity != null)
		{
			boolean canSee = this.actor.getVisibilityCache().canSee(livingEntity);
			boolean tickerTicking = this.seeingTargetTicker > 0;
			//Reset see time if sightline changes
			if (canSee != tickerTicking) {
				this.seeingTargetTicker = 0;
			}

			if (canSee)
			{
				++this.seeingTargetTicker;
			}
			else
			{
				--this.seeingTargetTicker;
			}

			double distance = this.actor.squaredDistanceTo(livingEntity);
			boolean outOfRange = (distance > (double)this.squaredRange || this.seeingTargetTicker < 5) && this.chargedTicksLeft == 0;
			this.actor.getLookControl().lookAt(livingEntity, 30.0F, 30.0F);

			if (this.stage == Stage.UNCHARGED)
			{
				if (!outOfRange)
				{
					this.actor.setCurrentHand(ProjectileUtil.getHandPossiblyHolding(this.actor, Items.CROSSBOW));
					this.stage = Stage.CHARGING;
					this.actor.setCharging(true);
				}
			}
			else if (this.stage == Stage.CHARGING)
			{
				if (!this.actor.isUsingItem()) {
					this.stage = Stage.UNCHARGED;
				}

				int changeTime = this.actor.getItemUseTime();
				ItemStack itemStack = this.actor.getActiveItem();
				if (changeTime >= CrossbowItem.getPullTime(itemStack))
				{
					this.actor.stopUsingItem();
					this.stage = Stage.CHARGED;
					this.chargedTicksLeft = 2 + this.actor.getRandom().nextInt(10);
					this.actor.setCharging(false);
				}
			}
			else if (this.stage == Stage.CHARGED)
			{
				--this.chargedTicksLeft;
				if (this.chargedTicksLeft == 0)
				{
					this.stage = Stage.READY_TO_ATTACK;
				}
			} else if (this.stage == Stage.READY_TO_ATTACK && canSee)
			{
				this.actor.attack(livingEntity, 1.0F);
				ItemStack cbStack = this.actor.getStackInHand(ProjectileUtil.getHandPossiblyHolding(this.actor, Items.CROSSBOW));
				CrossbowItem.setCharged(cbStack, false);
				this.stage = Stage.UNCHARGED;
			}

		}
	}

	enum Stage {
		UNCHARGED,
		CHARGING,
		CHARGED,
		READY_TO_ATTACK;
	}
}
