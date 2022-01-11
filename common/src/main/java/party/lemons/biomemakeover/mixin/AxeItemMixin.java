package party.lemons.biomemakeover.mixin;

import net.minecraft.world.item.AxeItem;
import net.minecraft.world.level.block.Block;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.util.access.AxeItemAccess;

import java.util.HashMap;
import java.util.Map;

@Mixin(AxeItem.class)
public class AxeItemMixin implements AxeItemAccess {
    @Shadow @Final @Mutable protected static Map<Block, Block> STRIPPABLES;

    @Inject(at = @At("TAIL"), method = "<clinit>")
    private static void onInit(CallbackInfo cbi) {
        STRIPPABLES = new HashMap<>(STRIPPABLES);
    }

    @Override
    public void bm_addStrippableLog(Block log, Block stripped) {
        STRIPPABLES.put(log, stripped);
    }
}
