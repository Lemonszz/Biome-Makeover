package party.lemons.biomemakeover.network;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.init.BMNetwork;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.Random;

public class S2C_DoPoltergeistParticle extends BaseS2CMessage {
    BlockPos pos;

    public S2C_DoPoltergeistParticle(BlockPos pos)
    {
        this.pos = pos;
    }

    public S2C_DoPoltergeistParticle(FriendlyByteBuf buf)
    {
        this.pos = buf.readBlockPos();
    }

    @Override
    public MessageType getType() {
        return BMNetwork.POLTERGEIST_PARTICLE;
    }

    @Override
    public void write(FriendlyByteBuf buf)
    {
        buf.writeBlockPos(pos);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        context.queue(()->{
            if(context.getPlayer().level == null)
                return;

            Random random = context.getPlayer().level.random;
            int x = pos.getX();
            int y = pos.getY();
            int z = pos.getZ();

            for(int i = 0; i < 2; i++)
            {
                context.getPlayer().level.addParticle(BMEffects.POLTERGEIST.get(), x + random.nextDouble(), y + random.nextDouble(), z + random.nextDouble(), (random.nextDouble() / 20D) * RandomUtil.randomDirection(1F), 0.025F, (random.nextDouble() / 20D) * RandomUtil.randomDirection(1F));
            }
        });
    }
}
