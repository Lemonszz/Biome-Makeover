package party.lemons.biomemakeover.mixin.client;

import com.google.common.collect.Lists;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemGroup;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.crafting.itemgroup.TabbedItemGroup;
import party.lemons.biomemakeover.gui.ItemGroupTabWidget;

import java.util.List;

@Mixin(CreativeInventoryScreen.class)
public abstract class CreativeInventoryScreenMixin extends AbstractInventoryScreen<CreativeInventoryScreen.CreativeScreenHandler>
{
	private final List<ItemGroupTabWidget> tabButtons = Lists.newArrayList();
	private ItemGroupTabWidget selectedSubtab;

	@Inject(at = @At("HEAD"), method = "setSelectedTab(Lnet/minecraft/item/ItemGroup;)V")
	private void setSelectedTab(ItemGroup group, CallbackInfo cbi)
	{
		buttons.removeAll(tabButtons);
		tabButtons.clear();

		if(group instanceof TabbedItemGroup)
		{
			TabbedItemGroup tGroup = (TabbedItemGroup) group;
			if(!tGroup.hasInitialized())
				tGroup.initialize();

			for(int i = 0; i < tGroup.getTabs().size(); i++)
			{
				int selectTab = i;
				ItemGroupTabWidget b = new ItemGroupTabWidget(x - 24, (y + 12) + (i * 24), tGroup.getTabs().get(i), (btn)->{
					tGroup.setSelectedTab(selectTab);
					MinecraftClient.getInstance().openScreen(this);
					((ItemGroupTabWidget)btn).isSelected = true;
					selectedSubtab = (ItemGroupTabWidget) btn;
				});

				if(i == tGroup.getSelectedTabIndex())
				{
					selectedSubtab = b;
					b.isSelected = true;
				}

				tabButtons.add(b);
				addButton(b);
			}
		}
	}

	@Inject(at = @At("TAIL"), method = "render(Lnet/minecraft/client/util/math/MatrixStack;IIF)V")
	public void render(MatrixStack matrixStack, int mouseX, int mouseY, float delta, CallbackInfo cbi)
	{
		tabButtons.forEach(b->{
			if(b.isHovered())
			{
				renderTooltip(matrixStack, b.getMessage(), mouseX, mouseY);
			}
		});
	}

	public CreativeInventoryScreenMixin(CreativeInventoryScreen.CreativeScreenHandler screenHandler, PlayerInventory playerInventory, Text text)
	{
		//NOFU
		super(screenHandler, playerInventory, text);
	}
}