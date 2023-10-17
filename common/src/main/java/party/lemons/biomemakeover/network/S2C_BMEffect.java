package party.lemons.biomemakeover.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;
import party.lemons.biomemakeover.init.BMNetwork;
import party.lemons.biomemakeover.util.effect.BiomeMakeoverEffect;

public class S2C_BMEffect extends BaseS2CMessage {

    private int effect;
    private BlockPos pos;
    private BlockState state;

    public S2C_BMEffect(int effect, BlockPos pos, BlockState state) {
        this.effect = effect;
        this.pos = pos;
        this.state = state;
    }

    public S2C_BMEffect(FriendlyByteBuf buf)
    {
        effect = buf.readInt();
        pos = buf.readBlockPos();
        state = buf.readJsonWithCodec(BlockState.CODEC);
    }

    @Override
    public MessageType getType() {
        return BMNetwork.BM_EFFECT;
    }

    @Override
    public void write(FriendlyByteBuf buf) {
        buf.writeInt(effect);
        buf.writeBlockPos(pos);
        buf.writeJsonWithCodec(BlockState.CODEC, state);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(()->{
            BiomeMakeoverEffect ef = BiomeMakeoverEffect.values()[effect];
            ef.execute(context.getPlayer().level(), pos, state);
        });
    }
}
