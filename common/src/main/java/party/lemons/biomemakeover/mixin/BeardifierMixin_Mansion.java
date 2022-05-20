package party.lemons.biomemakeover.mixin;

import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pools.JigsawJunction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Beardifier.class)
public class BeardifierMixin_Mansion {

    @Inject(at = @At("TAIL"), method = "<init>")
    protected void onConstruct(ObjectListIterator<Beardifier.Rigid> objectListIterator, ObjectListIterator<JigsawJunction> objectListIterator2, CallbackInfo cbi) {

        //TODO 1.19: Check mansion generation
        /*
        this.rigids.removeIf(p -> {
            if (p instanceof MansionFeature.Piece mp)
                return !mp.doesModifyGround();
            return false;
        });
        pieceIterator = rigids.iterator();

         */
    }
}
