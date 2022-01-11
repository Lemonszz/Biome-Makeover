package party.lemons.biomemakeover.mixin.multipart;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundAddMobPacket;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import party.lemons.biomemakeover.entity.mutipart.EntityPart;
import party.lemons.biomemakeover.entity.mutipart.MultiPartEntity;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin
{
    @Shadow private ClientLevel level;

    @Inject(method = "handleAddMob", at = @At(value = "INVOKE", target = "net/minecraft/world/entity/LivingEntity.recreateFromPacket(Lnet/minecraft/network/protocol/game/ClientboundAddMobPacket;)V"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onHandleMobSpawn(ClientboundAddMobPacket packet, CallbackInfo cbi, LivingEntity livingEntity)
    {
        if(livingEntity instanceof MultiPartEntity<?> mpe)
        {
            MultiPartEntity.handleClientSpawn(packet, mpe);

            for(EntityPart<?> p : mpe.getParts())
                this.level.putNonPlayerEntity(p.getId(), p);
        }
    }
}
