package party.lemons.biomemakeover.item;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import party.lemons.biomemakeover.util.EntityUtil;
import party.lemons.taniwha.item.animation.CustomUseAnimationItem;
import party.lemons.taniwha.util.ItemUtil;

public class AloeVeraItem extends ItemNameBlockItem implements CustomUseAnimationItem {
    public AloeVeraItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return 48;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand) {
        player.startUsingItem(interactionHand);

        return new InteractionResultHolder<>(InteractionResult.PASS, player.getItemInHand(interactionHand));
    }

    @Override
    public ItemStack finishUsingItem(ItemStack itemStack, Level level, LivingEntity livingEntity) {
        if (livingEntity instanceof Player && !((Player)livingEntity).getAbilities().instabuild) {
            itemStack.shrink(1);
        }

        return itemStack;
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.EAT;
    }

    @Override
    public void triggerItemUseEffects(LivingEntity livingEntity, ItemStack itemStack, int i) {
        EntityUtil.spawnItemParticles(livingEntity, itemStack, 1);
    }
}
