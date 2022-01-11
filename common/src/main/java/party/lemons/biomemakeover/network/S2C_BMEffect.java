package party.lemons.biomemakeover.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import party.lemons.biomemakeover.init.BMNetwork;
import party.lemons.biomemakeover.util.effect.BiomeMakeoverEffect;

public class S2C_BMEffect extends BaseS2CMessage {

    private int effect;
    private BlockPos pos;

    public S2C_BMEffect(int effect, BlockPos pos) {
        this.effect = effect;
        this.pos = pos;
    }

    public S2C_BMEffect(FriendlyByteBuf buf)
    {
        effect = buf.readInt();
        pos = buf.readBlockPos();
    }

    @Override
    public MessageType getType() {
        return BMNetwork.BM_EFFECT;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(effect);
        buf.writeBlockPos(pos);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(()->{
            BiomeMakeoverEffect ef = BiomeMakeoverEffect.values()[effect];
            ef.execute(context.getPlayer().getLevel(), pos);
        });
    }
}
