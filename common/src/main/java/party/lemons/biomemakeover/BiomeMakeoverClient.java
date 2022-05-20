package party.lemons.biomemakeover;

import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.client.rendering.ColorHandlerRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.utils.Env;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.block.Blocks;
import party.lemons.biomemakeover.block.blockentity.AltarBlockEntity;
import party.lemons.biomemakeover.block.blockentity.render.AltarRenderer;
import party.lemons.biomemakeover.block.blockentity.render.LightningBugBottleRenderer;
import party.lemons.biomemakeover.block.blockentity.render.TapestryRenderer;
import party.lemons.biomemakeover.crafting.AltarScreen;
import party.lemons.biomemakeover.crafting.DirectionDataScreen;
import party.lemons.biomemakeover.crafting.witch.menu.WitchScreen;
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
        }
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
