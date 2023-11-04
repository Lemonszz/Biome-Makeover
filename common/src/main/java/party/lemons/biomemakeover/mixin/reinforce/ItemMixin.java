package party.lemons.biomemakeover.mixin.reinforce;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.crafting.reinforcement.ReinforcementHandler;

import java.util.List;

@Mixin(Item.class)
public class ItemMixin
{
    @Inject(at = @At("HEAD"), method = "isBarVisible", cancellable = true)
    public void isBarVisible(ItemStack itemStack, CallbackInfoReturnable<Boolean> cbi)
    {
        if(ReinforcementHandler.isReinforced(itemStack))
            cbi.setReturnValue(true);

    }

    @Inject(at = @At("HEAD"), method = "getBarWidth", cancellable = true)
    public void getBarWidth(ItemStack itemStack, CallbackInfoReturnable<Integer> cbi)
    {
        if(ReinforcementHandler.isReinforced(itemStack))
            cbi.setReturnValue(ReinforcementHandler.getBarWidth(itemStack));
    }

    @Inject(at = @At("HEAD"), method = "getBarColor", cancellable = true)
    public void getBarColor(ItemStack itemStack, CallbackInfoReturnable<Integer> cbi)
    {
        if(ReinforcementHandler.isReinforced(itemStack))
            cbi.setReturnValue(ReinforcementHandler.getBarColor(itemStack));
    }

    @Inject(at = @At("HEAD"), method = "appendHoverText")
    public void onAppendText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag, CallbackInfo cbi)
    {
        if(ReinforcementHandler.isReinforced(itemStack))
            if(tooltipFlag.isAdvanced())
            {
                list.add(Component.translatable("text.biomemakeover.reinforced_advanced",
                        ReinforcementHandler.getReinforcementAmount(itemStack),
                        ReinforcementHandler.getMaxReinforcement(itemStack)).withStyle(ChatFormatting.AQUA));
            }
            else {
                list.add(Component.translatable("text.biomemakeover.reinforced").withStyle(ChatFormatting.AQUA));
            }
    }
}
