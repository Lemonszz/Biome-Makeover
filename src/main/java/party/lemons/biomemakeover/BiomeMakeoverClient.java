package party.lemons.biomemakeover;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.biome.Biome;
import party.lemons.biomemakeover.block.blockentity.render.AltarBlockEntityRenderer;
import party.lemons.biomemakeover.block.blockentity.render.LightningBugBottleBlockRenderer;
import party.lemons.biomemakeover.crafting.witch.screen.WitchScreen;
import party.lemons.biomemakeover.entity.render.*;
import party.lemons.biomemakeover.gui.AltarScreen;
import party.lemons.biomemakeover.init.*;
import party.lemons.biomemakeover.util.DebugUtil;
import party.lemons.biomemakeover.util.MathUtils;
import party.lemons.biomemakeover.util.WoodTypeInfo;
import party.lemons.biomemakeover.util.access.ChunkRenderRegionAccess;
import party.lemons.biomemakeover.util.color.ColorProviderHelper;
import party.lemons.biomemakeover.util.color.FoliageBlockColorProvider;
import party.lemons.biomemakeover.util.color.FoliageShiftBlockColorProvider;
import party.lemons.biomemakeover.util.color.StaticBlockColorProvider;
import party.lemons.biomemakeover.world.particle.LightningSparkParticle;

public class BiomeMakeoverClient implements ClientModInitializer
{
	public static boolean ENABLE_CLIENT_DEBUG = false;

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
		EntityRendererRegistry.INSTANCE.register(BMEntities.TADPOLE, (r, c)->new TadpoleRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.DRAGONFLY, (r, c)->new DragonflyRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.DECAYED, (r, c)->new DecayedRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.LIGHTNING_BUG, (r, c)->new LightningBugRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.LIGHTNING_BUG_ALTERNATE, (r, c)->new LightningBugRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.LIGHTNING_BOTTLE, (r, c)->new FlyingItemEntityRenderer(r, c.getItemRenderer()));
		EntityRendererRegistry.INSTANCE.register(BMEntities.GIANT_SLIME, (r, c)->new GiantSlimeRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.OWL, (r, c)->new OwlEntityRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.ROOTLING, (r, c)->new RootlingEntityRender(r));

		BlockEntityRendererRegistry.INSTANCE.register(BMBlockEntities.LIGHTNING_BUG_BOTTLE, (r)->new LightningBugBottleBlockRenderer(r));
		BlockEntityRendererRegistry.INSTANCE.register(BMBlockEntities.ALTAR, (r)->new AltarBlockEntityRenderer(r));

		ScreenRegistry.register(BMScreens.WITCH, WitchScreen::new);
		ScreenRegistry.register(BMScreens.ALTAR, AltarScreen::new);

		BMNetwork.initClient();

		ParticleFactoryRegistry.getInstance().register((ParticleType)BMEffects.LIGHTNING_SPARK, LightningSparkParticle.Factory::new);

		ColorProviderHelper.registerSimpleBlockWithItem(new FoliageBlockColorProvider(),
				BMBlocks.ANCIENT_OAK_LEAVES, BMBlocks.IVY);
		ColorProviderHelper.registerSimpleBlockWithItem(new StaticBlockColorProvider(0x84ab6f),
				BMBlocks.SWAMP_CYPRESS_LEAVES);

		ColorProviderHelper.registerSimpleBlockWithItem(new FoliageShiftBlockColorProvider.Lillies(),
				BMBlocks.SMALL_LILY_PAD, Blocks.LILY_PAD, BMBlocks.WATER_LILY);

		ColorProviderHelper.registerSimpleBlockWithItem(new FoliageShiftBlockColorProvider.Willow(),
				BMBlocks.WILLOW_LEAVES, BMBlocks.WILLOWING_BRANCHES);


		if(ENABLE_CLIENT_DEBUG)
		{
			UseItemCallback.EVENT.register((e, w, h)->
			{
				if(FabricLoader.getInstance().isDevelopmentEnvironment())
				{
					DebugUtil.printMissingLangKeys();
				}
				return TypedActionResult.pass(e.getStackInHand(h));
			});
		}

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
				BMBlocks.POTTED_SWAMP_CYPRESS_SAPLING,
				BMBlocks.POTTED_ANCIENT_OAK_SAPLING,
				BMBlocks.POTTED_WILLOW_SAPLING,
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
				BMBlocks.CATTAIL,
				BMBlocks.REED,
				BMBlocks.SMALL_LILY_PAD,
				BMBlocks.WILLOW_SAPLING,
				BMBlocks.SWAMP_CYPRESS_SAPLING,
				BMBlocks.ANCIENT_OAK_SAPLING,
				BMBlocks.WILLOW_WOOD_INFO.getBlock(WoodTypeInfo.Type.DOOR),
				BMBlocks.WILLOW_WOOD_INFO.getBlock(WoodTypeInfo.Type.TRAP_DOOR),
				BMBlocks.SWAMP_CYPRESS_WOOD_INFO.getBlock(WoodTypeInfo.Type.DOOR),
				BMBlocks.SWAMP_CYPRESS_WOOD_INFO.getBlock(WoodTypeInfo.Type.TRAP_DOOR),
				BMBlocks.LIGHTNING_BUG_BOTTLE,
				BMBlocks.WATER_LILY,
				BMBlocks.SWAMP_AZALEA,
				BMBlocks.MARIGOLD,
				BMBlocks.ILLUNITE_CLUSTER,
				BMBlocks.ALTAR,
				BMBlocks.ROOTLING_CROP,
				BMBlocks.IVY
		);

		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutoutMipped(),
				BMBlocks.ANCIENT_OAK_LEAVES,
				BMBlocks.WILLOW_LEAVES,
				BMBlocks.WILLOWING_BRANCHES
		);
	}
}
