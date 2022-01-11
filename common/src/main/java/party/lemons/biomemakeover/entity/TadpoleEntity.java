package party.lemons.biomemakeover.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.init.BMItems;

public class TadpoleEntity extends AbstractFish {

    private int babyTime = -6000;

    public TadpoleEntity(EntityType<? extends AbstractFish> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public void tick() {
        super.tick();

        if(!level.isClientSide())
        {
            babyTime++;
            if(!isBaby())
            {
                ToadEntity toad = BMEntities.TOAD.create(level);
                toad.moveTo(this.getX(), this.getY(), this.getZ(), yBodyRot, getXRot());
                toad.addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 200, 0));
                ((ServerLevel) level).addFreshEntityWithPassengers(toad);

                remove(RemovalReason.DISCARDED);
            }
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {

        ItemStack itemStack = player.getItemInHand(hand);
        if(!itemStack.isEmpty() && itemStack.getItem() == BMItems.DRAGONFLY_WINGS)
        {
            if(this.isBaby())
            {
                itemStack.shrink(1);
                this.growUp(Math.abs(babyTime / 20));
                return InteractionResult.sidedSuccess(level.isClientSide());
            }

            if(this.level.isClientSide())
            {
                return InteractionResult.CONSUME;
            }
        }

        return super.mobInteract(player, hand);
    }

    public void growUp(int age)
    {
        babyTime += age;
    }

    @Override
    public void setBaby(boolean baby) {
        babyTime = baby ? -24000 : 0;
    }

    @Override
    public boolean isBaby() {
        return babyTime < 0;
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("BabyTime", babyTime);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
        if(tag.contains("BabyTime")) babyTime = tag.getInt("BabyTime");
    }

    @Override
    protected SoundEvent getFlopSound() {
        return SoundEvents.TROPICAL_FISH_FLOP;
    }

    @Override
    public ItemStack getBucketItemStack()
    {
        return new ItemStack(BMItems.TADPOLE_BUCKET);
    }

    public static AttributeSupplier.Builder createAttributes()
    {
        return createMobAttributes()
                .add(Attributes.MAX_HEALTH, 10D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D);
    }
}
