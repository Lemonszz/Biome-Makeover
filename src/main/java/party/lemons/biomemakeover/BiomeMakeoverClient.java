package party.lemons.biomemakeover;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.fabricmc.fabric.api.event.player.UseItemCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Blocks;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.TypedActionResult;
import party.lemons.biomemakeover.block.blockentity.render.AltarBlockEntityRenderer;
import party.lemons.biomemakeover.block.blockentity.render.LightningBugBottleBlockRenderer;
import party.lemons.biomemakeover.block.blockentity.render.TapestryBlockEntityRenderer;
import party.lemons.biomemakeover.crafting.witch.screen.WitchScreen;
import party.lemons.biomemakeover.entity.render.*;
import party.lemons.biomemakeover.gui.AltarScreen;
import party.lemons.biomemakeover.gui.DirectionalDataScreen;
import party.lemons.biomemakeover.init.*;
import party.lemons.biomemakeover.util.DebugUtil;
import party.lemons.biomemakeover.util.color.ColorProviderHelper;
import party.lemons.biomemakeover.util.color.FoliageBlockColorProvider;
import party.lemons.biomemakeover.util.color.FoliageShiftBlockColorProvider;
import party.lemons.biomemakeover.util.color.StaticBlockColorProvider;
import party.lemons.biomemakeover.world.particle.BlossomParticle;
import party.lemons.biomemakeover.world.particle.PoltergeistParticle;
import party.lemons.biomemakeover.world.particle.LightningSparkParticle;
import party.lemons.biomemakeover.world.particle.TeleportParticle;

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
		EntityRendererRegistry.INSTANCE.register(BMEntities.LIGHTNING_BOTTLE,
		                                         (r, c)->new FlyingItemEntityRenderer<>(r, c.getItemRenderer())
		);
		EntityRendererRegistry.INSTANCE.register(BMEntities.GIANT_SLIME, (r, c)->new GiantSlimeRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.OWL, (r, c)->new OwlEntityRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.ROOTLING, (r, c)->new RootlingEntityRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.MOTH, (r, c)->new MothEntityRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.ADJUDICATOR, (r, c)->new AdjudicatorEntityRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.ADJUDICATOR_MIMIC, (r, c)->new AdjudicatorMimicRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.STONE_GOLEM, (r, c)->new StoneGolemEntityRender(r));

		BlockEntityRendererRegistry.INSTANCE.register(BMBlockEntities.LIGHTNING_BUG_BOTTLE, LightningBugBottleBlockRenderer::new
		);
		BlockEntityRendererRegistry.INSTANCE.register(BMBlockEntities.ALTAR, AltarBlockEntityRenderer::new);
		BlockEntityRendererRegistry.INSTANCE.register(BMBlockEntities.TAPESTRY, TapestryBlockEntityRenderer::new
		);

		ScreenRegistry.register(BMScreens.WITCH, WitchScreen::new);
		ScreenRegistry.register(BMScreens.ALTAR, AltarScreen::new);
		ScreenRegistry.register(BMScreens.DIRECTIONAL_DATA, DirectionalDataScreen::new);

		BMNetwork.initClient();

		ParticleFactoryRegistry.getInstance().register((ParticleType)BMEffects.LIGHTNING_SPARK, LightningSparkParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register((ParticleType)BMEffects.POLTERGEIST, PoltergeistParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register((ParticleType)BMEffects.TELEPORT, TeleportParticle.Factory::new);
		ParticleFactoryRegistry.getInstance().register((ParticleType)BMEffects.BLOSSOM, BlossomParticle.Factory::new);

		ColorProviderHelper.registerSimpleBlockWithItem(new FoliageBlockColorProvider(),
		                                                BMBlocks.ANCIENT_OAK_LEAVES,
		                                                BMBlocks.IVY
		);
		ColorProviderHelper.registerSimpleBlockWithItem(new StaticBlockColorProvider(0x84ab6f),
		                                                BMBlocks.SWAMP_CYPRESS_LEAVES
		);

		ColorProviderHelper.registerSimpleBlockWithItem(new FoliageShiftBlockColorProvider.Lillies(),
		                                                BMBlocks.SMALL_LILY_PAD,
		                                                Blocks.LILY_PAD,
		                                                BMBlocks.WATER_LILY
		);

		ColorProviderHelper.registerSimpleBlockWithItem(new FoliageShiftBlockColorProvider.Willow(),
		                                                BMBlocks.WILLOW_LEAVES,
		                                                BMBlocks.WILLOWING_BRANCHES
		);

		ColorProviderHelper.registerSimpleBlockWithItem(new FoliageShiftBlockColorProvider(35, -10, -5),
		                                                BMBlocks.MOTH_BLOSSOM, BMBlocks.ITCHING_IVY
		);


		if(ENABLE_CLIENT_DEBUG)
		{
			UseItemCallback.EVENT.register((e, w, h)->
           {
               if(FabricLoader.getInstance().isDevelopmentEnvironment())
               {
	               //DebugUtil.printUntaggedItems();
                   DebugUtil.printMissingLangKeys();
               }
               return TypedActionResult.pass(e.getStackInHand(h));
           });
		}
	}
}
