package party.lemons.biomemakeover.fabric;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.fabricmc.api.ModInitializer;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.BiomeMakeoverClient;


public class BMFabric implements ModInitializer
{
    @Override
    public void onInitialize() {
        BiomeMakeover.init();

        if (Platform.getEnvironment() == Env.CLIENT) {
            BiomeMakeoverClient.init();
        }
    }
}
