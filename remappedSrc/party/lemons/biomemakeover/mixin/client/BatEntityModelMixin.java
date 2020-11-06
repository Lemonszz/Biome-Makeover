package party.lemons.biomemakeover.mixin.client;

import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BatEntityModel;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import party.lemons.biomemakeover.util.access.BatEntityModelAccessor;

@Mixin(BatEntityModel.class)
public class BatEntityModelMixin implements BatEntityModelAccessor
{
    @Shadow @Final public ModelPart head;

    @Override
    public ModelPart getHead() {
        return head;
    }
}
