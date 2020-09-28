package party.lemons.biomemakeover;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import party.lemons.biomemakeover.entity.render.DragonlingRender;
import party.lemons.biomemakeover.entity.render.GhoulFishRender;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.init.BMNetwork;

public class BiomeMakeoverClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		EntityRendererRegistry.INSTANCE.register(BMEntities.GHOUL_FISH, (r, c)->new GhoulFishRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.DRAGONLING, (r, c)->new DragonlingRender(r));

		BMNetwork.initClient();
	}
}
