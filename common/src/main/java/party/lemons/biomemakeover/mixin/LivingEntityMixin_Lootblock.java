package party.lemons.biomemakeover.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.util.extension.LootBlocker;

@Mixin(LivingEntity.class)
public class LivingEntityMixin_Lootblock implements LootBlocker {
    @Unique
    private boolean isLootBlocked = false;

    @Override
    @Unique
    public boolean isLootBlocked()
    {
        return isLootBlocked;
    }

    @Override
    @Unique
    public void setLootBlocked(boolean block)
    {
        this.isLootBlocked = block;
    }

    @Inject(at = @At("HEAD"), method = "shouldDropLoot", cancellable = true)
    protected void shouldDropLoot(CallbackInfoReturnable<Boolean> cbi)
    {
        if(isLootBlocked())
            cbi.setReturnValue(false);
    }

    @Inject(at = @At("TAIL"), method = "addAdditionalSaveData")
    private void writeAdditional(CompoundTag tag, CallbackInfo cbi)
    {
        tag.putBoolean("BMLootBlock", isLootBlocked);
    }

    @Inject(at = @At("TAIL"), method = "readAdditionalSaveData")
    private void readAdditional(CompoundTag tag, CallbackInfo cbi)
    {
        isLootBlocked = tag.getBoolean("BMLootBlock");
    }
}
