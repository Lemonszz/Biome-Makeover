package party.lemons.biomemakeover.item;

import net.minecraft.core.Position;
import net.minecraft.core.dispenser.AbstractProjectileDispenseBehavior;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DispenserBlock;
import party.lemons.biomemakeover.entity.LightningBottleEntity;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.util.RandomUtil;

public class LightningBottleItem extends Item
{
    public LightningBottleItem(Properties settings)
    {
        super(settings);

        DispenserBlock.registerBehavior(this, (pointer, stack)->(new AbstractProjectileDispenseBehavior()
        {
            @Override
            protected Projectile getProjectile(Level level, Position position, ItemStack itemStack) {
                return new LightningBottleEntity(level, position.x(), position.y(), position.z());
            }

            @Override
            protected float getUncertainty() {
                return super.getUncertainty() * 0.5F;
            }

            @Override
            protected float getPower() {
                return super.getPower() * 1.25F;
            }
        }).dispense(pointer, stack));
    }

    @Override
    public boolean isFoil(ItemStack stack)
    {
        return true;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player user, InteractionHand interactionHand) {
        ItemStack itemStack = user.getItemInHand(interactionHand);
        level.playSound(null, user.getX(), user.getY(), user.getZ(), BMEffects.LIGHTNING_BOTTLE_THROW, SoundSource.NEUTRAL, 0.5F, 0.4F / (RandomUtil.RANDOM.nextFloat() * 0.4F + 0.8F));

        if(!level.isClientSide())
        {
            LightningBottleEntity bottleEntity = new LightningBottleEntity(level, user);
            bottleEntity.setItem(itemStack);
            bottleEntity.shootFromRotation(user, user.getXRot(), user.getYRot(), -20.0F, 0.7F, 1.0F);
            level.addFreshEntity(bottleEntity);
        }
        user.getCooldowns().addCooldown(this, 45);
        user.awardStat(Stats.ITEM_USED.get(this));
        if(!user.isCreative())
        {
            itemStack.shrink(1);
        }

        return InteractionResultHolder.success(itemStack);
    }
}