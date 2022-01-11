package party.lemons.biomemakeover.mixin.multipart;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.EnderDragonPart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.entity.mutipart.EntityPart;
import party.lemons.biomemakeover.util.extension.MultipartHolder;

@Mixin(ServerLevel.class)
public class ServerLevelMixin implements MultipartHolder {

    private Int2ObjectMap<EntityPart<?>> multiparts = new Int2ObjectOpenHashMap<>();

    @Override
    public Int2ObjectMap<EntityPart<?>> getPartMap() {
        return multiparts;
    }

    @Inject(at = @At("TAIL"), method = "getEntityOrPart", cancellable = true)
    public void getEntityOrPart(int i, CallbackInfoReturnable<Entity> cbi) {
        if(getPartMap().containsKey(i)) {
            cbi.setReturnValue(getPartMap().get(i));
        }
    }
}
