package party.lemons.biomemakeover;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import party.lemons.biomemakeover.entity.render.BlightBatRender;
import party.lemons.biomemakeover.entity.render.GlowfishRender;
import party.lemons.biomemakeover.entity.render.MushroomVillagerRender;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.init.BMNetwork;

public class BiomeMakeoverClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		EntityRendererRegistry.INSTANCE.register(BMEntities.MUSHROOM_TRADER, (r, c)->new MushroomVillagerRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.BLIGHTBAT, (r, c)->new BlightBatRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.GLOWFISH, (r, c)->new GlowfishRender(r));

		BMNetwork.initClient();

		//TODO: Move this
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),
				BMBlocks.MYCELIUM_ROOTS,
				BMBlocks.MYCELIUM_SPROUTS,
				BMBlocks.POTTED_MYCELIUM_ROOTS,
				BMBlocks.POTTED_GREEN_GLOWSHROOM,
				BMBlocks.POTTED_PURPLE_GLOWSHROOM,
				BMBlocks.POTTED_ORANGE_GLOWSHROOM,
				BMBlocks.POTTED_BLIGHTED_BALSA_SAPLING,
				BMBlocks.TALL_BROWN_MUSHROOM,
				BMBlocks.TALL_RED_MUSHROOM,
				BMBlocks.PURPLE_GLOWSHROOM,
				BMBlocks.GREEN_GLOWSHROOM,
				BMBlocks.ORANGE_GLOWSHROOM,
				BMBlocks.BLIGHTED_BALSA_SAPLING
		);
	}
}
