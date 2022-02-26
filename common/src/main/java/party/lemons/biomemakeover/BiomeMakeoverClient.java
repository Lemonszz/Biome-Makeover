package party.lemons.biomemakeover;

import dev.architectury.platform.Platform;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.utils.Env;
import net.minecraft.client.Minecraft;
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
import party.lemons.biomemakeover.util.sound.AltarCursingSoundInstance;
import party.lemons.taniwha.client.color.ColorProviderHelper;
import party.lemons.taniwha.client.color.FoliageBlockColorProvider;
import party.lemons.taniwha.client.color.FoliageShiftBlockColorProvider;
import party.lemons.taniwha.client.color.StaticBlockColorProvider;

public class BiomeMakeoverClient
{
    public static void init()
    {
        if (Platform.getEnvironment() == Env.CLIENT) {
            BMEntities.registerModelLayers();
            BMBlocks.initClient();
            BlockEntityRendererRegistry.register(BMBlockEntities.TAPESTRY, TapestryRenderer::new);
            BlockEntityRendererRegistry.register(BMBlockEntities.ALTAR, AltarRenderer::new);
            BlockEntityRendererRegistry.register(BMBlockEntities.LIGHTNING_BUG_BOTTLE, LightningBugBottleRenderer::new);

            initColors();

            MenuRegistry.registerScreenFactory(BMScreens.WITCH, WitchScreen::new);
            MenuRegistry.registerScreenFactory(BMScreens.ALTAR, AltarScreen::new);
            MenuRegistry.registerScreenFactory(BMScreens.DIRECTIONAL_DATA, DirectionDataScreen::new);
        }
    }

    private static void initColors()
    {
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
    }

    //TODO: Find somewhere else for this
    public static void curseSound(AltarBlockEntity altar)
    {
        AltarCursingSoundInstance sound = new AltarCursingSoundInstance(altar);
        Minecraft.getInstance().getSoundManager().play(sound);
    }
}
