package party.lemons.biomemakeover.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import party.lemons.biomemakeover.init.BMWorldGen;

@Mixin(ServerLevel.class)
public class ServerLevelMixin_Mansion
{
    @ModifyVariable(method = "findNearestMapFeature", at = @At("HEAD"), argsOnly = true)
    private StructureFeature<?> changeMansion(StructureFeature<?> feature)
    {
        if(feature == StructureFeature.WOODLAND_MANSION)
             return BMWorldGen.DarkForest.MANSION;

        return feature;
    }
}
