package party.lemons.biomemakeover.fabric;

import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.BiomeMakeoverClient;
import party.lemons.biomemakeover.entity.render.fabric.HatArmorRenderer;
import party.lemons.biomemakeover.init.BMItems;


public class BMFabric implements ModInitializer
{
    @Override
    public void onInitialize() {
        BiomeMakeover.init();
    }
}
