package party.lemons.biomemakeover.init;

import net.fabricmc.fabric.api.network.ClientSidePacketRegistry;
import net.minecraft.util.Identifier;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.network.S2C_SpawnEntityCustom;

public class BMNetwork
{
	public static final Identifier SPAWN_ENTITY = new Identifier(BiomeMakeover.MODID, "spawn_entiity");

	public static void initClient()
	{
		ClientSidePacketRegistry.INSTANCE.register(SPAWN_ENTITY, new S2C_SpawnEntityCustom());
	}
}
