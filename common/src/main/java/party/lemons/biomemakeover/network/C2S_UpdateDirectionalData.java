package party.lemons.biomemakeover.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.Level;
import party.lemons.biomemakeover.block.blockentity.DirectionalDataBlockEntity;
import party.lemons.biomemakeover.init.BMNetwork;

public class C2S_UpdateDirectionalData extends BaseC2SMessage
{
    private BlockPos pos;
    private String value;

    public C2S_UpdateDirectionalData(BlockPos blockPos, String value)
    {
        this.pos = blockPos;
        this.value = value;
    }

    public C2S_UpdateDirectionalData(FriendlyByteBuf buf)
    {
        this(buf.readBlockPos(), buf.readUtf());
    }

    @Override
    public MessageType getType() {
        return BMNetwork.CL_UPDATE_DIR_DATA;
    }

    @Override
    public void write(FriendlyByteBuf buf)
    {
        buf.writeBlockPos(pos);
        buf.writeUtf(value);
    }

    @Override
    public void handle(NetworkManager.PacketContext context)
    {
        context.queue(()->{
            if(!context.getPlayer().isCreative())
                return;

            Level level = context.getPlayer().level();
            if(level.getBlockEntity(pos) instanceof DirectionalDataBlockEntity data)
            {
                data.setMetadata(value);
                data.setChanged();
                level.sendBlockUpdated(pos, level.getBlockState(pos), level.getBlockState(pos), 3);
            }
        });
    }
}
