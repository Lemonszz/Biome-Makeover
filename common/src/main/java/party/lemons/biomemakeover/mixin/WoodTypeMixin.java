package party.lemons.biomemakeover.mixin;

import net.minecraft.world.level.block.state.properties.WoodType;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import party.lemons.biomemakeover.util.registry.sign.WoodTypeHelper;

import java.util.Set;

@Mixin(WoodType.class)
public class WoodTypeMixin implements WoodTypeHelper {

    @Shadow @Final private static Set<WoodType> VALUES;

    @Override
    public Set<WoodType> bm_getTypes() {
        return VALUES;
    }
}
