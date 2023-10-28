package party.lemons.biomemakeover.compat.rei.display;

import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import party.lemons.biomemakeover.BiomeMakeover;

public class DustDevilIconRenderer implements Renderer {

    public static final ResourceLocation LOCATION = BiomeMakeover.ID("textures/gui/dust_devil_icon.png");
    public static DustDevilIconRenderer INSTANCE = new DustDevilIconRenderer();
    @Override
    public void render(GuiGraphics graphics, Rectangle bounds, int mouseX, int mouseY, float delta) {

        //graphics.drawString(Minecraft.getInstance().font, "???", bounds.getX(), bounds.getY(), 0xFFFFFF, true);

        graphics.blit(LOCATION, bounds.getX(), bounds.getY(), 16, 16, 0, 0,  128, 128, 128, 128);
        // graphics.blit(LOCATION, bounds.getX(), bounds.getY(),0, 0, 128, 128);

    }
}
