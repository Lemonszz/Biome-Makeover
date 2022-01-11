package party.lemons.biomemakeover.mixin.totem;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.util.TotemItem;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity
{
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Shadow public abstract ItemStack getItemInHand(InteractionHand interactionHand);

    @Inject(at = @At("HEAD"), method = "checkTotemDeathProtection", cancellable = true)
    private void tryUseTotem(DamageSource source, CallbackInfoReturnable<Boolean> cbi)
    {
        if (!source.isBypassInvul())
        {
            for(InteractionHand hand : InteractionHand.values())
            {
                ItemStack stack = this.getItemInHand(hand);
                if (stack.getItem() instanceof TotemItem && ((TotemItem) stack.getItem()).canActivate((LivingEntity)(Object)this)) {
                    ItemStack activateStack = stack.copy();
                    stack.shrink(1);

                    ((TotemItem)activateStack.getItem()).activateTotem((LivingEntity)(Object)this, activateStack);

                    cbi.setReturnValue(true);
                }
            }

        }
    }
}