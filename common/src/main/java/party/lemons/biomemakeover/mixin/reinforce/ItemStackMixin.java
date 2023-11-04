package party.lemons.biomemakeover.mixin.reinforce;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.crafting.reinforcement.ReinforcementHandler;

@Mixin(ItemStack.class)
public abstract class ItemStackMixin
{
    @Inject(at = @At("HEAD"), method = "hurt", cancellable = true)
    public void hurt(int i, RandomSource randomSource, @Nullable ServerPlayer serverPlayer, CallbackInfoReturnable<Boolean> cbi)
    {
        ItemStack self = (ItemStack)(Object)this;
        if(ReinforcementHandler.isReinforced(self))
        {
            if(ReinforcementHandler.damageItem(self, i, randomSource, serverPlayer))
                cbi.setReturnValue(self.getDamageValue() >= self.getMaxDamage());
        }
    }
    @Shadow
    public abstract Item getItem();
}
