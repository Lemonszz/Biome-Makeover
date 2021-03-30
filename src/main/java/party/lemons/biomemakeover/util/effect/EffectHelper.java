package party.lemons.biomemakeover.util.effect;

import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import party.lemons.biomemakeover.init.BMNetwork;
import party.lemons.biomemakeover.util.NetworkUtil;

public class EffectHelper
{
	public static final int EFF_CURSE_SOUND = 0;

	public static void doEffect(World world, int effect, BlockPos pos)
	{
		if(world.isClient())
			return;

		PacketByteBuf buf = PacketByteBufs.create();
		buf.writeVarInt(effect);
		buf.writeBlockPos(pos);

		NetworkUtil.serverSendTracking(world, pos, BMNetwork.BM_EVENT, buf);
	}
}
