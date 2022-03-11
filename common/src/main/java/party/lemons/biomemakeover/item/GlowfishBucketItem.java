package party.lemons.biomemakeover.item;

import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.TropicalFish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.init.BMAdvancements;

import java.util.List;
import java.util.function.Supplier;

public class GlowfishBucketItem extends BMMobBucketItem {

    public GlowfishBucketItem(Supplier entityType, Fluid fluid, SoundEvent soundEvent, Properties properties) {
        super(entityType, fluid, soundEvent, properties);
    }

    public InteractionResultHolder<ItemStack> use(Level world, ServerPlayer user, InteractionHand hand)
    {
        InteractionResultHolder<ItemStack> res = super.use(world, user, hand);

        if(!world.isClientSide() && res.getResult().consumesAction())
        {
            BMAdvancements.GLOWFISH_SAVE.trigger(user);
        }

        return res;
    }
}
