package party.lemons.biomemakeover.mixin.multipart;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
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

    @Inject(method = "handleAddEntity", at = @At(value = "INVOKE", target = "net/minecraft/world/entity/Entity.recreateFromPacket(Lnet/minecraft/network/protocol/game/ClientboundAddEntityPacket;)V"), locals = LocalCapture.CAPTURE_FAILSOFT)
    private void onHandleMobSpawn(ClientboundAddEntityPacket packet, CallbackInfo cbi, EntityType<?> entityType, Entity livingEntity)
    {
        if(livingEntity instanceof MultiPartEntity<?> mpe)
        {
            MultiPartEntity.handleClientSpawn(packet, mpe);

            for(EntityPart<?> p : mpe.getParts())
                this.level.putNonPlayerEntity(p.getId(), p);
        }
    }
}
