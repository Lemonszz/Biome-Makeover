package party.lemons.biomemakeover.mixin.enchantment;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import party.lemons.biomemakeover.init.BMEnchantments;
import party.lemons.biomemakeover.util.RandomUtil;

@Mixin(BowItem.class)
public class BowItemMixin
{
    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/ItemStack;hurtAndBreak(ILnet/minecraft/world/entity/LivingEntity;Ljava/util/function/Consumer;)V"),
            method = "releaseUsing", locals = LocalCapture.CAPTURE_FAILSOFT)
    public void onStoppedUsing(ItemStack stack, Level level, LivingEntity livingEntity, int remainingUseTicks, CallbackInfo cbi, Player player, boolean bl, ItemStack itemStack2, int j, float f, boolean bl2, ArrowItem arrowItem, AbstractArrow abstractArrow)
    {
        int inaccuracy = EnchantmentHelper.getItemEnchantmentLevel(BMEnchantments.INACCURACY_CURSE, stack);
        if(inaccuracy >= 1)
        {
            float pitch = player.getXRot() + RandomUtil.randomDirection(level.random.nextFloat() * (inaccuracy * 1.3F));
            float yaw = player.getYRot() + RandomUtil.randomDirection(level.random.nextFloat() * (inaccuracy * 1.3F));
            abstractArrow.shootFromRotation(player, pitch, yaw, 0.0F, f * 3.0F, 1.0F);
        }
    }
}
