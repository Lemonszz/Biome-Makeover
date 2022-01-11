package party.lemons.biomemakeover.entity.ai;

import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.CrossbowAttackMob;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;

import java.util.EnumSet;

public class MountedCrossbowAttackGoal<T extends Monster & RangedAttackMob & CrossbowAttackMob> extends Goal
{
    public static final UniformInt chargeTime = TimeUtil.rangeOfSeconds(1, 2);

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
        this.setFlags(EnumSet.of(Goal.Flag.LOOK));
    }

    public boolean canStart()
    {
        return this.hasAliveTarget() && this.isEntityHoldingCrossbow();
    }

    private boolean isEntityHoldingCrossbow()
    {
        return this.actor.isHolding(Items.CROSSBOW);
    }

    @Override
    public boolean canUse() {
        return canStart();
    }

    public boolean canContinueToUse()
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
        this.actor.setAggressive(false);
        this.actor.setTarget(null);
        this.seeingTargetTicker = 0;
        if (this.actor.isUsingItem())
        {
            this.actor.stopUsingItem();
            this.actor.setChargingCrossbow(false);
            CrossbowItem.setCharged(this.actor.getUseItem(), false);
        }

    }

    public void tick()
    {
        LivingEntity livingEntity = this.actor.getTarget();
        if (livingEntity != null)
        {
            boolean canSee = this.actor.getSensing().hasLineOfSight(livingEntity);
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

            double distance = this.actor.distanceToSqr(livingEntity);
            boolean outOfRange = (distance > (double)this.squaredRange || this.seeingTargetTicker < 5) && this.chargedTicksLeft == 0;
            this.actor.getLookControl().setLookAt(livingEntity, 30.0F, 30.0F);

            if (this.stage == Stage.UNCHARGED)
            {
                if (!outOfRange)
                {
                    this.actor.startUsingItem(ProjectileUtil.getWeaponHoldingHand(this.actor, Items.CROSSBOW));
                    this.stage = Stage.CHARGING;
                    this.actor.setChargingCrossbow(true);
                }
            }
            else if (this.stage == Stage.CHARGING)
            {
                if (!this.actor.isUsingItem()) {
                    this.stage = Stage.UNCHARGED;
                }

                int changeTime = this.actor.getTicksUsingItem();
                ItemStack itemStack = this.actor.getUseItem();
                if (changeTime >= CrossbowItem.getChargeDuration(itemStack))
                {
                    this.actor.stopUsingItem();
                    this.stage = Stage.CHARGED;
                    this.chargedTicksLeft = 2 + this.actor.getRandom().nextInt(10);
                    this.actor.setChargingCrossbow(false);
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
                this.actor.performCrossbowAttack(this.actor, 1.0F);
                ItemStack cbStack = this.actor.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this.actor, Items.CROSSBOW));
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