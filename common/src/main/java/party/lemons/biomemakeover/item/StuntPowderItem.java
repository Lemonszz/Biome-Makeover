package party.lemons.biomemakeover.item;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.InteractionEvent;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import party.lemons.biomemakeover.util.NetworkUtil;
import party.lemons.biomemakeover.util.extension.Stuntable;
import party.lemons.taniwha.util.ItemUtil;

public class StuntPowderItem extends Item
{
    public StuntPowderItem(Properties properties)
    {
        super(properties);

        InteractionEvent.INTERACT_ENTITY.register(((player, entity, hand) -> {
            ItemStack stack = player.getItemInHand(hand);
            if(!stack.isEmpty() && stack.getItem() == this && entity instanceof LivingEntity) {

                if(stuntEntity(stack, player, (LivingEntity) entity, hand).consumesAction()) {
                    player.level().gameEvent(entity, GameEvent.ENTITY_INTERACT, entity.position());
                    return EventResult.interruptTrue();
                }
            }
            return EventResult.pass();
        }));
    }

    public InteractionResult stuntEntity(ItemStack stack, Player user, LivingEntity entity, InteractionHand hand)
    {
        if(entity instanceof Stuntable stuntable)
        {
            if((entity.isBaby() || stuntable.isAlwaysBaby()) && !stuntable.isStunted())
            {
                if(!entity.level().isClientSide())
                {
                    stuntable.setStunted(true);
                    NetworkUtil.doEntityParticle(user.level(), ParticleTypes.WARPED_SPORE, entity, 15, 0.2F);
                    ItemUtil.shrinkStack(stack, user);
                }
                return InteractionResult.SUCCESS;
            }
        }

        return super.interactLivingEntity(stack, user, entity, hand);
    }
}
