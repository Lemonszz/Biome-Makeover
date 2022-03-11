package party.lemons.biomemakeover.mixin.enchantment;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.init.BMEnchantments;
import party.lemons.biomemakeover.item.enchantment.BMEnchantment;
import party.lemons.biomemakeover.network.S2C_SyncSlide;
import party.lemons.biomemakeover.util.ItemUtil;
import party.lemons.biomemakeover.util.NetworkUtil;
import party.lemons.biomemakeover.util.extension.Slideable;

import java.util.Collection;
import java.util.Iterator;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity
{
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(at = @At("TAIL"), method = "tick")
    public void tick(CallbackInfo cbi)
    {
        if(!getLevel().isClientSide())
        {
            Iterator<Pair<EquipmentSlot, ItemStack>> it = attributeStacks.iterator();
            while(it.hasNext())
            {
                Pair<EquipmentSlot, ItemStack> pair = it.next();
                ItemStack st = pair.getSecond();
                if(!hasStackEquipInSlot(st, pair.getFirst()))
                {
                    ItemUtil.forEachEnchantment((en, stack, lvl)->
                    {
                        if(en instanceof BMEnchantment)
                        {
                            ((BMEnchantment) en).removeAttributes((LivingEntity) (Object) this, pair.getFirst());
                        }
                    }, st, true);
                    it.remove();
                }
            }

            for(EquipmentSlot slot : EquipmentSlot.values())
            {
                ItemStack stack = getItemBySlot(slot);
                if(!stack.isEmpty())
                {
                    ItemUtil.forEachEnchantment((en, st, lvl)->
                    {
                        if(en instanceof BMEnchantment)
                        {
                            ((BMEnchantment) en).onTick((LivingEntity) (Object) this, st, lvl);
                            if(!hasAttributeStack(st) && ((BMEnchantment) en).addAttributes((LivingEntity) (Object) this, st, slot, lvl))
                            {
                                attributeStacks.add(new Pair<>(slot, st));
                            }
                        }
                    }, stack);
                }
            }
        }
    }

    @ModifyArg(method = "causeFallDamage", at = @At(value = "INVOKE", target="Lnet/minecraft/world/entity/LivingEntity;calculateFallDamage(FF)I"), index = 0)
    private float changeFallDistance(float distance){
        if(distance >= 3.0)
            return distance + EnchantmentHelper.getEnchantmentLevel(BMEnchantments.BUCKLING_CURSE.get(), (LivingEntity)(Object)this);

        return distance;
    }

    @Unique
    private boolean hasStackEquipInSlot(ItemStack stack, EquipmentSlot slot)
    {
        return getItemBySlot(slot).equals(stack);
    }

    @Unique
    private final Collection<Pair<EquipmentSlot, ItemStack>> attributeStacks = Lists.newArrayList();

    @Unique
    public boolean hasAttributeStack(ItemStack stack)
    {
        for(Pair<EquipmentSlot, ItemStack> pair : attributeStacks)
        {
            if(pair.getSecond().equals(stack)) return true;
        }
        return false;
    }

    @Shadow
    public abstract ItemStack getItemBySlot(EquipmentSlot var1);

}
