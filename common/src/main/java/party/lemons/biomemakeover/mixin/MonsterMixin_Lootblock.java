package party.lemons.biomemakeover.mixin;

import net.minecraft.world.entity.monster.Monster;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.util.extension.LootBlocker;

@Mixin(Monster.class)
public class MonsterMixin_Lootblock
{
    @Inject(at = @At("HEAD"), method = "shouldDropLoot", cancellable = true)
    protected void shouldDropLoot(CallbackInfoReturnable<Boolean> cbi)
    {
        if(((LootBlocker) this).isLootBlocked())
            cbi.setReturnValue(false);
    }
}
