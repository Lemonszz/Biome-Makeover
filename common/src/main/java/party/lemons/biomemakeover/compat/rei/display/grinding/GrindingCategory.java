package party.lemons.biomemakeover.compat.rei.display.grinding;

import me.shedaniel.math.Point;
import me.shedaniel.math.Rectangle;
import me.shedaniel.rei.api.client.gui.Renderer;
import me.shedaniel.rei.api.client.gui.widgets.Widget;
import me.shedaniel.rei.api.client.gui.widgets.Widgets;
import me.shedaniel.rei.api.client.registry.display.DisplayCategory;
import me.shedaniel.rei.api.common.category.CategoryIdentifier;
import me.shedaniel.rei.api.common.entry.EntryIngredient;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import party.lemons.biomemakeover.compat.rei.BMRei;

import java.util.ArrayList;
import java.util.List;

public class GrindingCategory implements DisplayCategory<GrindingDisplay> {
    private static final int WIDTH = 4;

    @Override
    public int getDisplayWidth(GrindingDisplay display) {
        return 139;
    }

    @Override
    public int getDisplayHeight() {
        return 50;
    }

    @Override
    public CategoryIdentifier<GrindingDisplay> getCategoryIdentifier() {
        return BMRei.GRINDING_DISPLAY;
    }

    @Override
    public List<Widget> setupDisplay(GrindingDisplay display, Rectangle bounds) {
        List<Widget> widgets = new ArrayList<>();
        widgets.add(Widgets.createRecipeBase(bounds));
        widgets.add(Widgets.createArrow(new Point(bounds.getX() + 30, bounds.getCenterY() - 9)));

        widgets.add(Widgets.createSlot(new Point(bounds.getX() + 12, bounds.getCenterY() - 9)).markInput().entries(display.getInputEntries().get(0)));

        int maxY = (display.getOutputEntries().size() - 1) / WIDTH;
        int startY = bounds.getCenterY() - 9 - (9 * maxY);

        for(int i = 0; i < display.getOutputEntries().size(); i++)
        {
            int gX = i % WIDTH;
            int gY = i / WIDTH;


            int xx = bounds.getX() + 55+ (gX * 18);
            EntryIngredient output = display.getOutputEntries().get(i);

            if (!output.isEmpty()) {
                int percent = (int)(display.getPercentChances().get(i) * 100F);
                output = output.map(stack -> stack.copy().tooltip(Component.translatable("text.biomemakeover.chance", percent).withStyle(ChatFormatting.YELLOW)));
            }

            widgets.add(Widgets.createSlot(new Point(xx, (gY * 18) + startY)).markOutput().entries(output));
        }
        return widgets;
    }

    @Override
    public Component getTitle() {
        return Component.translatable("text.biomemakeover.rei.grinding");
    }

    @Override
    public Renderer getIcon() {
        return DustDevilIconRenderer.INSTANCE;
    }
}
