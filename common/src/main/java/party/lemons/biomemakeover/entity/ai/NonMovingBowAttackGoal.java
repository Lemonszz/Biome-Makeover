package party.lemons.biomemakeover.entity.ai;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.Items;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;

import java.util.EnumSet;

public class NonMovingBowAttackGoal<T extends Monster & RangedAttackMob> extends Goal
{
    private final T actor;
    private int attackInterval;
    private int cooldown = -1;
    private int targetSeeingTicker;
    private int combatTicks = -1;

    public NonMovingBowAttackGoal(T actor, int attackInterval, float range) {
        this.actor = actor;
        this.attackInterval = attackInterval;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public void setAttackInterval(int attackInterval) {
        this.attackInterval = attackInterval;
    }

    public boolean canUse() {
        return this.actor.getTarget() != null && this.isHoldingBow();
    }

    protected boolean isHoldingBow() {
        return this.actor.isHolding(Items.BOW);
    }

    public boolean canContinueToUse() {
        return (this.canUse() || !this.actor.getNavigation().isDone()) && this.isHoldingBow();
    }

    public void start() {
        super.start();
        this.actor.setAggressive(true);
    }

    public void stop() {
        super.stop();
        this.actor.setAggressive(false);
        this.targetSeeingTicker = 0;
        this.cooldown = -1;
        this.actor.stopUsingItem();
    }

    public void tick() {
        LivingEntity livingEntity = this.actor.getTarget();
        if (livingEntity != null) {
            double d = this.actor.distanceToSqr(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
            boolean bl = this.actor.getSensing().hasLineOfSight(livingEntity);
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
                this.actor.lookAt(livingEntity, 30.0F, 30.0F);
            } else {
                this.actor.getLookControl().setLookAt(livingEntity, 30.0F, 30.0F);
            }

            if (this.actor.isUsingItem()) {
                if (!bl && this.targetSeeingTicker < -60) {
                    this.actor.stopUsingItem();
                } else if (bl) {
                    int i = this.actor.getTicksUsingItem();
                    if (i >= 20) {
                        this.actor.stopUsingItem();
                        this.actor.performRangedAttack(livingEntity, BowItem.getPowerForTime(i));
                        this.cooldown = this.attackInterval;
                    }
                }
            } else if (--this.cooldown <= 0 && this.targetSeeingTicker >= -60) {
                this.actor.startUsingItem(ProjectileUtil.getWeaponHoldingHand(this.actor, Items.BOW));
            }

        }
    }
}