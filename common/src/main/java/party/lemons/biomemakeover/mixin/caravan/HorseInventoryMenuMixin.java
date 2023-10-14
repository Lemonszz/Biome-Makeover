package party.lemons.biomemakeover.mixin.caravan;

import net.minecraft.world.Container;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.HorseInventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.entity.camel.InventoryMount;

@Mixin(HorseInventoryMenu.class)
public abstract class HorseInventoryMenuMixin extends AbstractContainerMenu {

    protected HorseInventoryMenuMixin(@Nullable MenuType<?> menuType, int i) {
        super(menuType, i);
    }
    @Inject(at = @At("TAIL"), method = "<init>")
    public void onInit(int i, Inventory inventory, Container container, AbstractHorse abstractHorse, CallbackInfo cbi)
    {
        if (abstractHorse instanceof InventoryMount mount && mount.hasChest()) {
            for(int l = 0; l < 3; ++l) {
                for(int m = 0; m < mount.getInventoryColumns(); ++m) {
                    this.addSlot(new Slot(container, 2 + m + l *  mount.getInventoryColumns(), 80 + m * 18, 18 + l * 18));
                }
            }
        }
    }
}
