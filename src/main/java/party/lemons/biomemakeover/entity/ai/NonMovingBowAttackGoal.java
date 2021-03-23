package party.lemons.biomemakeover.entity.ai;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.RangedAttackMob;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.projectile.ProjectileUtil;
import net.minecraft.item.BowItem;
import net.minecraft.item.Items;

import java.util.EnumSet;

public class NonMovingBowAttackGoal<T extends HostileEntity & RangedAttackMob> extends Goal
{
	private final T actor;
	private int attackInterval;
	private int cooldown = -1;
	private int targetSeeingTicker;
	private int combatTicks = -1;

	public NonMovingBowAttackGoal(T actor, int attackInterval, float range) {
		this.actor = actor;
		this.attackInterval = attackInterval;
		this.setControls(EnumSet.of(Goal.Control.MOVE, Goal.Control.LOOK));
	}

	public void setAttackInterval(int attackInterval) {
		this.attackInterval = attackInterval;
	}

	public boolean canStart() {
		return this.actor.getTarget() != null && this.isHoldingBow();
	}

	protected boolean isHoldingBow() {
		return this.actor.isHolding(Items.BOW);
	}

	public boolean shouldContinue() {
		return (this.canStart() || !this.actor.getNavigation().isIdle()) && this.isHoldingBow();
	}

	public void start() {
		super.start();
		this.actor.setAttacking(true);
	}

	public void stop() {
		super.stop();
		this.actor.setAttacking(false);
		this.targetSeeingTicker = 0;
		this.cooldown = -1;
		this.actor.clearActiveItem();
	}

	public void tick() {
		LivingEntity livingEntity = this.actor.getTarget();
		if (livingEntity != null) {
			double d = this.actor.squaredDistanceTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
			boolean bl = this.actor.getVisibilityCache().canSee(livingEntity);
			boolean bl2 = this.targetSeeingTicker > 0;
			if (bl != bl2) {
				this.targetSeeingTicker = 0;
			}

			if (bl) {
				++this.targetSeeingTicker;
			} else {
				--this.targetSeeingTicker;
			}

			if (this.combatTicks > -1) {
				this.actor.lookAtEntity(livingEntity, 30.0F, 30.0F);
			} else {
				this.actor.getLookControl().lookAt(livingEntity, 30.0F, 30.0F);
			}

			if (this.actor.isUsingItem()) {
				if (!bl && this.targetSeeingTicker < -60) {
					this.actor.clearActiveItem();
				} else if (bl) {
					int i = this.actor.getItemUseTime();
					if (i >= 20) {
						this.actor.clearActiveItem();
						this.actor.attack(livingEntity, BowItem.getPullProgress(i));
						this.cooldown = this.attackInterval;
					}
				}
			} else if (--this.cooldown <= 0 && this.targetSeeingTicker >= -60) {
				this.actor.setCurrentHand(ProjectileUtil.getHandPossiblyHolding(this.actor, Items.BOW));
			}

		}
	}
}
