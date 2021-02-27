package party.lemons.biomemakeover.mixin;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
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
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;damage(ILnet/minecraft/entity/LivingEntity;Ljava/util/function/Consumer;)V"),
			method = "onStoppedUsing", locals = LocalCapture.CAPTURE_FAILHARD)
	public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks, CallbackInfo cbi, PlayerEntity playerEntity, boolean bl, ItemStack itemStack, int i, float f, boolean bl2, ArrowItem arrowItem, PersistentProjectileEntity projectile)
	{
		int inaccuracy = EnchantmentHelper.getLevel(BMEnchantments.INACCURACY_CURSE, stack);
		if(inaccuracy >= 1)
		{
			float pitch = playerEntity.pitch + RandomUtil.randomDirection(world.random.nextFloat() * (inaccuracy * 1.3F));
			float yaw = playerEntity.yaw + RandomUtil.randomDirection(world.random.nextFloat() * (inaccuracy * 1.3F));
			projectile.setProperties(playerEntity, pitch, yaw, 0.0F, f * 3.0F, 1.0F);
		}
	}
}
