package party.lemons.biomemakeover.forge;

import dev.architectury.platform.forge.EventBuses;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.Constants;

@Mod(Constants.MOD_ID)
@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class BMForge
{
    public BMForge()
    {
        EventBuses.registerModEventBus(Constants.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());


        BiomeMakeover.init();
        EnvExecutor.runInEnv(Env.CLIENT, ()-> BMForgeClient::initClient);

    }
}
