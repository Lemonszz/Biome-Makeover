package party.lemons.biomemakeover.mixin.client;

import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.resources.model.Material;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(Sheets.class)
public class SheetsMixin {

    /**
     * Attemped to fix issues where other mods collect sheets before sign types are created (I think)
     */

    @Shadow
    @Final
    private static Map<WoodType, Material> SIGN_MATERIALS;

    @Inject(at = @At("HEAD"), method = "getSignMaterial")
    private static void getSignMaterial(WoodType woodType, CallbackInfoReturnable<Material> cbi) {
        if (!SIGN_MATERIALS.containsKey(woodType)) {
            System.out.println("ERROR GETTING WOOD SHEET, ATTEMPT CREATE?");
            SIGN_MATERIALS.put(woodType, createSignMaterial(woodType));
        }
    }

    @Shadow
    private static Material createSignMaterial(WoodType woodType) {
        throw new AssertionError();
    }
}
