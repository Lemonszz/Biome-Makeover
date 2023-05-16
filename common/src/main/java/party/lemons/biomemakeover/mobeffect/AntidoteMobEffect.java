package party.lemons.biomemakeover.mobeffect;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.init.BMAdvancements;
import party.lemons.biomemakeover.util.extension.Stuntable;

import java.util.stream.Collectors;

public class AntidoteMobEffect extends InstantenousMobEffect {
    public AntidoteMobEffect() {
        super(MobEffectCategory.BENEFICIAL, 0xFFFFFF);
    }

    public void doEffect(LivingEntity target)
    {
        target.getActiveEffects().stream().filter((e) -> e.getEffect().getCategory() == MobEffectCategory.HARMFUL).toList().forEach(e->target.removeEffect(e.getEffect()));

        if(!target.level().isClientSide())
        {
            if(target instanceof Player)
                BMAdvancements.ANTIDOTE.trigger((ServerPlayer) target);

            if(target instanceof Stuntable)
                ((Stuntable) target).setStunted(false);
        }
    }

    @Override
    public void applyInstantenousEffect(Entity source, Entity attacker, LivingEntity target, int amplifier, double proximity) {
        doEffect(target);
    }

    @Override
    public void addAttributeModifiers(LivingEntity livingEntity, AttributeMap attributeMap, int i) {
        doEffect(livingEntity);
        super.addAttributeModifiers(livingEntity, attributeMap, i);
    }
}
