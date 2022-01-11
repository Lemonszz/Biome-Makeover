package party.lemons.biomemakeover.mixin;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.init.BMPotions;

@Mixin(Witch.class)
public abstract class WitchMixin_Antidote extends Raider
{
    protected WitchMixin_Antidote(EntityType<? extends Raider> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow public abstract boolean isDrinkingPotion();

    @Shadow public abstract void setUsingItem(boolean bl);

    @Shadow private int usingTime;

    @Shadow @Final private static AttributeModifier SPEED_MODIFIER_DRINKING;

    @ModifyVariable(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/monster/Witch;setItemSlot(Lnet/minecraft/world/entity/EquipmentSlot;Lnet/minecraft/world/item/ItemStack;)V", ordinal = 1), method = "aiStep")
    public Potion changePotion(Potion potion)
    {
        if(random.nextFloat() < 0.10)
        {
            for(MobEffectInstance effect : getActiveEffects())
                if(effect.getEffect().getCategory() == MobEffectCategory.HARMFUL)
                {
                    return BMPotions.ANTIDOTE_POT;
                }
        }
        return potion;
    }

    @Inject(at = @At("TAIL"), method = "aiStep")
    public void tickMovement(CallbackInfo cbi)
    {
        if(!isDrinkingPotion())
        {
            if(random.nextFloat() < 0.10)
            {
                boolean found = false;
                for(MobEffectInstance effect : getActiveEffects())
                    if(effect.getEffect().getCategory() == MobEffectCategory.HARMFUL)
                    {
                        found = true;
                        break;
                    }

                if(found)
                {
                    this.setItemSlot(EquipmentSlot.MAINHAND, PotionUtils.setPotion(new ItemStack(Items.POTION), BMPotions.ANTIDOTE_POT));
                    this.usingTime = this.getMainHandItem().getUseDuration();
                    this.setUsingItem(true);
                    if(!this.isSilent())
                    {
                        this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.WITCH_DRINK, this.getSoundSource(), 1.0F, 0.8F + this.random.nextFloat() * 0.4F);
                    }

                    AttributeInstance attributeInstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
                    attributeInstance.removeModifier(SPEED_MODIFIER_DRINKING);
                    attributeInstance.addTransientModifier(SPEED_MODIFIER_DRINKING);
                }
            }
        }
    }

}
