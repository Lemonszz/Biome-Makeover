package party.lemons.biomemakeover.init;

import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.Constants;

public class BMTab
{
	private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Constants.MOD_ID, Registries.CREATIVE_MODE_TAB);
	public static final RegistrySupplier<CreativeModeTab> TAB = TABS.register(BiomeMakeover.ID("biomemakeover"), ()-> CreativeTabRegistry.create(Component.translatable("advancements.biomemakeover.root.title"), ()->new ItemStack(BMItems.ICON_ITEM.get())));


	public static void init()
	{
		TABS.register();
	}
}
