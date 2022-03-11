package party.lemons.biomemakeover.entity.adjudicator;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.util.effect.BiomeMakeoverEffect;
import party.lemons.biomemakeover.util.effect.EffectHelper;

public class AdjudicatorMimicEntity extends Monster  implements AdjudicatorStateProvider, RangedAttackMob {
    public AdjudicatorMimicEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new RangedAttackGoal(this, 1.0D, 12, 15.0F));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(6, new RandomLookAroundGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true));
    }

    @Override
    protected void populateDefaultEquipmentSlots(DifficultyInstance difficultyInstance) {
        super.populateDefaultEquipmentSlots(difficultyInstance);

        handDropChances[0] = 0;
        handDropChances[1] = 0;

        setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BOW));
    }

    @Nullable
    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor serverLevelAccessor, DifficultyInstance difficultyInstance, MobSpawnType mobSpawnType, @Nullable SpawnGroupData spawnGroupData, @Nullable CompoundTag compoundTag) {
        populateDefaultEquipmentSlots(difficultyInstance);
        return super.finalizeSpawn(serverLevelAccessor, difficultyInstance, mobSpawnType, spawnGroupData, compoundTag);
    }
    @Override
    protected SoundEvent getAmbientSound()
    {
        return BMEffects.ADJUDICATOR_LAUGH.get();
    }

    @Override
    public void die(DamageSource damageSource) {
        playSound(BMEffects.ADJUDICATOR_NO.get(),1F, 1F);
        playSound(BMEffects.ADJUDICATOR_LAUGH.get(),0.25F, 1F);
        spawnAnim();
        remove(RemovalReason.DISCARDED);

        if(!level.isClientSide())
            EffectHelper.doEffect(level, BiomeMakeoverEffect.BLOCK_ENDER_PARTICLES, getOnPos());

        super.die(damageSource);
    }

    @Override
    public void baseTick() {
        if(firstTick)
        {
            playSound(BMEffects.ADJUDICATOR_MIMIC.get(), 1F, 1F);
        }

        super.baseTick();
    }

    @Override
    public void performRangedAttack(LivingEntity target, float pullProgress) {
        ItemStack itemStack = this.getProjectile(this.getItemInHand(ProjectileUtil.getWeaponHoldingHand(this, Items.BOW)));
        AbstractArrow arrow = ProjectileUtil.getMobArrow(this, itemStack, pullProgress);
        double d = target.getX() - this.getX();
        double e = target.getY(0.3333333333333333D) - arrow.getY();
        double f = target.getZ() - this.getZ();
        double g = Math.sqrt(d * d + f * f);
        arrow.shoot(d, e + g * 0.20000000298023224D, f, 1.6F, (float)(14 - this.level.getDifficulty().getId() * 4));
        this.playSound(SoundEvents.SKELETON_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level.addFreshEntity(arrow);
    }

    @Override
    public AdjudicatorState getState() {
        return AdjudicatorState.FIGHTING;
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return createMonsterAttributes()
                .add(Attributes.MAX_HEALTH, 1F)
                .add(Attributes.MOVEMENT_SPEED, 0.25F)
                .add(Attributes.ATTACK_DAMAGE, 3F);
    }
}
