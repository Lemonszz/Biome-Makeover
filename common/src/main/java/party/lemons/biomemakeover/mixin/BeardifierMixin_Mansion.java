package party.lemons.biomemakeover.mixin;

import it.unimi.dsi.fastutil.objects.ObjectList;
import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Beardifier;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.level.feature.mansion.MansionFeature;

@Mixin(Beardifier.class)
public class BeardifierMixin_Mansion {

    @Shadow
    @Final
    @Mutable
    protected ObjectListIterator<StructurePiece> pieceIterator;

    @Shadow
    @Final
    protected ObjectList<StructurePiece> rigids;

    @Inject(at = @At("TAIL"), method = "<init>")
    protected void onConstruct(StructureFeatureManager structureFeatureManager, ChunkAccess chunkAccess, CallbackInfo cbi) {

        this.rigids.removeIf(p -> {
            if (p instanceof MansionFeature.Piece mp)
                return !mp.doesModifyGround();
            return false;
        });
        pieceIterator = rigids.iterator();
    }
}
