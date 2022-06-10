package party.lemons.biomemakeover;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Blocks;
import party.lemons.biomemakeover.block.blockentity.AltarBlockEntity;
import party.lemons.biomemakeover.block.blockentity.render.AltarRenderer;
import party.lemons.biomemakeover.block.blockentity.render.LightningBugBottleRenderer;
import party.lemons.biomemakeover.block.blockentity.render.TapestryRenderer;
import party.lemons.biomemakeover.crafting.AltarScreen;
import party.lemons.biomemakeover.crafting.DirectionDataScreen;
import party.lemons.biomemakeover.crafting.witch.menu.WitchScreen;
import party.lemons.biomemakeover.entity.render.*;
import party.lemons.biomemakeover.init.BMBlockEntities;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.init.BMScreens;
import party.lemons.biomemakeover.item.BMSpawnEggItem;
import party.lemons.biomemakeover.util.color.*;
import party.lemons.biomemakeover.util.sound.AltarCursingSoundInstance;

public class BiomeMakeoverClient
{
    public static void init()
    {
        if (Platform.getEnvironment() == Env.CLIENT) {
            BMEntities.registerModelLayers();
            BMBlocks.initClient();

            LifecycleEvent.SETUP.register(()->{
                BlockEntityRendererRegistry.register(BMBlockEntities.TAPESTRY.get(), TapestryRenderer::new);
                BlockEntityRendererRegistry.register(BMBlockEntities.ALTAR.get(), AltarRenderer::new);
                BlockEntityRendererRegistry.register(BMBlockEntities.LIGHTNING_BUG_BOTTLE.get(), LightningBugBottleRenderer::new);

                initColors();

                MenuRegistry.registerScreenFactory(BMScreens.WITCH.get(), WitchScreen::new);
                MenuRegistry.registerScreenFactory(BMScreens.ALTAR.get(), AltarScreen::new);
                MenuRegistry.registerScreenFactory(BMScreens.DIRECTIONAL_DATA.get(), DirectionDataScreen::new);
            });

            registerModels();
        }
    }

    public static void registerModels()
    {
        EnvExecutor.runInEnv(Env.CLIENT, ()->()->{

            EntityRendererRegistry.register(BMEntities.TUMBLEWEED::get, TumbleweedRender::new);
            EntityRendererRegistry.register(BMEntities.BM_BOAT::get, (c)->new BMBoatRender(c, false));
            EntityRendererRegistry.register(BMEntities.BM_CHEST_BOAT::get, (c)->new BMBoatRender(c, true));
            EntityRendererRegistry.register(BMEntities.LIGHTNING_BOTTLE::get, ThrownItemRenderer::new);

            EntityRendererRegistry.register(BMEntities.GLOWFISH::get, GlowfishRender::new);
            EntityRendererRegistry.register(BMEntities.BLIGHTBAT::get, BlightBatRender::new);
            EntityRendererRegistry.register(BMEntities.MUSHROOM_TRADER::get, MushroomTraderRender::new);
            EntityRendererRegistry.register(BMEntities.SCUTTLER::get, ScuttlerRender::new);
            EntityRendererRegistry.register(BMEntities.GHOST::get, GhostRender::new);
            EntityRendererRegistry.register(BMEntities.COWBOY::get, CowboyRender::new);
            EntityRendererRegistry.register(BMEntities.DECAYED::get, DecayedRender::new);
            EntityRendererRegistry.register(BMEntities.DRAGONFLY::get, DragonflyRender::new);
            EntityRendererRegistry.register(BMEntities.TOAD::get, ToadRender::new);
            EntityRendererRegistry.register(BMEntities.TADPOLE::get, TadpoleRender::new);
            EntityRendererRegistry.register(BMEntities.LIGHTNING_BUG::get, LightningBugRender::new);
            EntityRendererRegistry.register(BMEntities.LIGHTNING_BUG_ALTERNATE::get, LightningBugRender::new);
            EntityRendererRegistry.register(BMEntities.OWL::get, OwlRender::new);
            EntityRendererRegistry.register(BMEntities.MOTH::get, MothRender::new);
            EntityRendererRegistry.register(BMEntities.ROOTLING::get, RootlingRender::new);
            EntityRendererRegistry.register(BMEntities.ADJUDICATOR::get, AdjudicatorRender::new);
            EntityRendererRegistry.register(BMEntities.ADJUDICATOR_MIMIC::get, AdjudicatorMimicRender::new);
            EntityRendererRegistry.register(BMEntities.STONE_GOLEM::get, StoneGolemRender::new);
            EntityRendererRegistry.register(BMEntities.HELMIT_CRAB::get, HelmitCrabRender::new);
        });
    }

    private static void initColors()    //TODO: move these to a block modifier? Need to be careful of sidedness
    {
        ColorProviderHelper.registerSimpleBlockWithItem(new FoliageBlockColorProvider(),
                BMBlocks.ANCIENT_OAK_LEAVES.get(),
                BMBlocks.IVY.get()
        );
        ColorProviderHelper.registerSimpleBlockWithItem(new StaticBlockColorProvider(0x84ab6f),
                BMBlocks.SWAMP_CYPRESS_LEAVES.get()
        );

        ColorProviderHelper.registerSimpleBlockWithItem(new FoliageShiftBlockColorProvider.Lillies(),
                BMBlocks.SMALL_LILY_PAD.get(),
                Blocks.LILY_PAD,
                BMBlocks.WATER_LILY.get()
        );

        ColorProviderHelper.registerSimpleBlockWithItem(new FoliageShiftBlockColorProvider.Willow(),
                BMBlocks.WILLOW_LEAVES.get(),
                BMBlocks.WILLOWING_BRANCHES.get()
        );

        ColorProviderHelper.registerSimpleBlockWithItem(new FoliageShiftBlockColorProvider(35, -10, -5),
                BMBlocks.MOTH_BLOSSOM.get(), BMBlocks.ITCHING_IVY.get()
        );
    }

    //TODO: Find somewhere else for this
    public static void curseSound(AltarBlockEntity altar)
    {
        AltarCursingSoundInstance sound = new AltarCursingSoundInstance(altar, altar.getLevel().getRandom());
        Minecraft.getInstance().getSoundManager().play(sound);
    }
}
