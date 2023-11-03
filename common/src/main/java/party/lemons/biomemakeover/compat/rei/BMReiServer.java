package party.lemons.biomemakeover.compat.rei;

import me.shedaniel.rei.api.common.display.DisplaySerializerRegistry;
import me.shedaniel.rei.api.common.display.basic.BasicDisplay;
import me.shedaniel.rei.api.common.plugins.REIServerPlugin;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.ListTag;
import party.lemons.biomemakeover.compat.rei.display.grinding.GrindingDisplay;

public class BMReiServer extends BMRei implements REIServerPlugin
{
    @Override
    public void registerDisplaySerializer(DisplaySerializerRegistry registry) {
        registry.register(GRINDING_DISPLAY, BasicDisplay.Serializer.of(GrindingDisplay::new, (display, tag) -> {
            ListTag percentages = new ListTag();
            for(float percent : display.getPercentChances())
                percentages.add(FloatTag.valueOf(percent));

            tag.put("Percentages", percentages);
        }));
    }
}
