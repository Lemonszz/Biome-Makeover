package party.lemons.biomemakeover;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import party.lemons.biomemakeover.entity.render.*;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.init.BMNetwork;
import party.lemons.biomemakeover.util.WoodTypeInfo;

public class BiomeMakeoverClient implements ClientModInitializer
{
	@Override
	public void onInitializeClient()
	{
		EntityRendererRegistry.INSTANCE.register(BMEntities.MUSHROOM_TRADER, (r, c)->new MushroomVillagerRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.BLIGHTBAT, (r, c)->new BlightBatRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.GLOWFISH, (r, c)->new GlowfishRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.TUMBLEWEED, (r, c)->new TumbleweedRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.COWBOY, (r, c)->new CowboyRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.GHOST, (r, c)->new GhostRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.SCUTTLER, (r, c)->new ScuttlerRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.BM_BOAT, (r, c)->new BMBoatRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.TOAD, (r, c)->new ToadRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.DRAGONFLY, (r, c)->new DragonflyRender(r));

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
				BMBlocks.POTTED_BARREL_CACTUS,
				BMBlocks.POTTED_FLOWERED_BARREL_CACTUS,
				BMBlocks.POTTED_SAGUARO_CACTUS,
				BMBlocks.TALL_BROWN_MUSHROOM,
				BMBlocks.TALL_RED_MUSHROOM,
				BMBlocks.PURPLE_GLOWSHROOM,
				BMBlocks.GREEN_GLOWSHROOM,
				BMBlocks.ORANGE_GLOWSHROOM,
				BMBlocks.BLIGHTED_BALSA_SAPLING,
				BMBlocks.TUMBLEWEED,
				BMBlocks.SAGUARO_CACTUS,
				BMBlocks.BARREL_CACTUS,
				BMBlocks.BARREL_CACTUS_FLOWERED,
				BMBlocks.POLTERGEIST,
				BMBlocks.WILLOWING_BRANCHES,
				BMBlocks.CATTAIL,
				BMBlocks.REED,
				BMBlocks.SMALL_LILY_PAD,
				BMBlocks.WILLOW_LEAVES,
				BMBlocks.WILLOW_SAPLING,
				BMBlocks.WILLOW_WOOD_INFO.getBlock(WoodTypeInfo.Type.TRAP_DOOR),
				BMBlocks.WILLOW_WOOD_INFO.getBlock(WoodTypeInfo.Type.DOOR)
		);
	}
}
