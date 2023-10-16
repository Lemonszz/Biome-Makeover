package party.lemons.biomemakeover.item;

import net.minecraft.nbt.CompoundTag;
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
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.util.EntityUtil;
import party.lemons.taniwha.item.animation.CustomUseAnimationItem;

public class AloeVeraItem extends ItemNameBlockItem implements CustomUseAnimationItem {
    public AloeVeraItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public int getUseDuration(ItemStack itemStack) {
        return 48;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand interactionHand)
    {
        ItemStack stack = player.getItemInHand(interactionHand);

        if(!player.isUsingItem()) {
            player.startUsingItem(interactionHand);
            return new InteractionResultHolder<>(InteractionResult.PASS, stack);
        }
        return new InteractionResultHolder<>(InteractionResult.PASS, stack);
    }
    @Override
    public InteractionResult interactLivingEntity(ItemStack itemStack, Player player, LivingEntity livingEntity, InteractionHand interactionHand) {
        if(!player.isUsingItem()) {
            applyAloe(livingEntity);
            player.getCooldowns().addCooldown(this, 30);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity user) {
        if (user instanceof Player player && !player.getAbilities().instabuild) {
            stack.shrink(1);
            player.getCooldowns().addCooldown(this, 30);
        }
        applyAloe(user);

        return stack;
    }

    public void applyAloe(LivingEntity user)
    {
        if(user.level().isClientSide())
        {
            user.clearFire();
            user.heal(2F);
            //TODO: particles/sound
        }
    }

    @Override
    public UseAnim getUseAnimation(ItemStack itemStack) {
        return UseAnim.EAT;
    }

    @Override
    public void triggerItemUseEffects(LivingEntity livingEntity, ItemStack itemStack, int i) {
        EntityUtil.spawnItemParticles(livingEntity, itemStack, 2);
    }
}
