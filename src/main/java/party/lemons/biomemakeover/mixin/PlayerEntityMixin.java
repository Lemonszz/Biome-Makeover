package party.lemons.biomemakeover.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerAbilities;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.init.BMItems;
import party.lemons.biomemakeover.util.EntityUtil;
import party.lemons.biomemakeover.util.RandomUtil;
import party.lemons.biomemakeover.util.extensions.Stuntable;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity
{
    @Shadow @Final public PlayerAbilities abilities;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    /////////////////
    ///Start Projectile Resistance
    /////////////////
    @Inject(at = @At("HEAD"), method = "damage", cancellable = true)
    private void onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cbi)
    {
        if(source.isProjectile())
        {
            double protection = EntityUtil.getProjectileResistance((LivingEntity)(Object)this);
            if(protection > 0D && (RandomUtil.RANDOM.nextDouble() * 30D) < protection)
                cbi.setReturnValue(true);
        }
    }

    /////////////////
    ///End Projectile Resistance
    /////////////////

    /////////////////
    ///Start Use Stunt Powder
    /////////////////

    @Inject(at = @At(value = "INVOKE", target = "net/minecraft/entity/player/PlayerEntity.getStackInHand(Lnet/minecraft/util/Hand;)Lnet/minecraft/item/ItemStack;"), method = "interact", cancellable = true)
    public void interact(Entity entity, Hand hand, CallbackInfoReturnable<ActionResult> cbi)
    {
        ItemStack itemStack = getStackInHand(hand);
        if(!itemStack.isEmpty() && itemStack.getItem() == BMItems.STUNT_POWDER)
        {
            if (entity instanceof LivingEntity) {
                if(entity instanceof Stuntable && !((Stuntable)entity).isStunted()) {
                    if (this.abilities.creativeMode) {
                        itemStack = itemStack.copy();
                    }

                    ActionResult result = BMItems.STUNT_POWDER.stuntEntity(itemStack, (PlayerEntity) (Object) this, (LivingEntity) entity, hand);
                    if (result.isAccepted()) {
                        if (itemStack.isEmpty() && !this.abilities.creativeMode) {
                            this.setStackInHand(hand, ItemStack.EMPTY);
                        }

                        cbi.setReturnValue(result);
                    }
                }
            }
        }
    }

    /////////////////
    ///End Use Stunt Powder
    /////////////////

}
