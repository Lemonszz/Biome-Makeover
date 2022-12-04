package party.lemons.biomemakeover.init;

import dev.architectury.registry.menu.MenuRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.MenuType;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.Constants;
import party.lemons.biomemakeover.crafting.AltarMenu;
import party.lemons.biomemakeover.crafting.DirectionalDataMenu;
import party.lemons.biomemakeover.crafting.witch.menu.WitchMenu;

import java.util.function.Supplier;

public class BMScreens
{
    private static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(Constants.MOD_ID, Registries.MENU);


    public static final Supplier<MenuType<WitchMenu>> WITCH = MENUS.register(BiomeMakeover.ID("witch"), ()->new MenuType<>(WitchMenu::new));
    public static final Supplier<MenuType<AltarMenu>> ALTAR = MENUS.register(BiomeMakeover.ID("altar"), ()->new MenuType<>(AltarMenu::new));
    public static final Supplier<MenuType<DirectionalDataMenu>> DIRECTIONAL_DATA = MENUS.register(BiomeMakeover.ID("directional_data"), ()->MenuRegistry.ofExtended((id, inventory, buf) -> new DirectionalDataMenu(id, buf)));

    public static void init() {
        MENUS.register();
    }
}
