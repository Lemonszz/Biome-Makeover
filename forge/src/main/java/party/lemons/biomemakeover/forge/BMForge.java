package party.lemons.biomemakeover.forge;

import dev.architectury.platform.Platform;
import dev.architectury.platform.forge.EventBuses;
import dev.architectury.utils.Env;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.BiomeMakeoverClient;
import party.lemons.biomemakeover.Constants;

@Mod(Constants.MOD_ID)
@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BMForge
{
    public BMForge()
    {
        EventBuses.registerModEventBus(Constants.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());

        FMLJavaModLoadingContext.get().getModEventBus().addListener(BMForge::clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(BMForge::commonSetup);

        BiomeMakeover.init();
        if (Platform.getEnvironment() == Env.CLIENT) {
            FMLJavaModLoadingContext.get().getModEventBus().addListener(BMForge::particleSetup);
            BiomeMakeoverClient.init();
        }
    }

    public static void commonSetup(FMLCommonSetupEvent event)
    {
    }

    public static void clientSetup(FMLClientSetupEvent event)
    {
        if (Platform.getEnvironment() == Env.CLIENT) {
            BiomeMakeoverClient.init();
        }
    }
}
