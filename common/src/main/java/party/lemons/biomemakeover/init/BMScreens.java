package party.lemons.biomemakeover.init;

import dev.architectury.registry.menu.MenuRegistry;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;
import party.lemons.biomemakeover.Constants;
import party.lemons.biomemakeover.crafting.AltarMenu;
import party.lemons.biomemakeover.crafting.DirectionalDataMenu;
import party.lemons.biomemakeover.crafting.witch.menu.WitchMenu;
import party.lemons.taniwha.registry.RegistryHelper;

public class BMScreens
{
    public static final MenuType<WitchMenu> WITCH = MenuRegistry.of(WitchMenu::new);
    public static final MenuType<AltarMenu> ALTAR = MenuRegistry.of(AltarMenu::new);
    public static final MenuType<DirectionalDataMenu> DIRECTIONAL_DATA = MenuRegistry.ofExtended((id, inventory, buf) -> new DirectionalDataMenu(id, buf));

    public static void init() {
        RegistryHelper.register(Constants.MOD_ID, Registry.MENU, MenuType.class, BMScreens.class);
    }
}
