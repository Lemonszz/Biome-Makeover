package party.lemons.biomemakeover.fabric;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import party.lemons.biomemakeover.BiomeMakeoverClient;
import party.lemons.biomemakeover.entity.render.fabric.HatArmorRenderer;
import party.lemons.biomemakeover.init.BMItems;

public class BMFabricClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BiomeMakeoverClient.init();

        ArmorRenderer.register(new HatArmorRenderer(), BMItems.WITCH_HAT.get(), BMItems.COWBOY_HAT.get());  //TODO: automate
    }
}
