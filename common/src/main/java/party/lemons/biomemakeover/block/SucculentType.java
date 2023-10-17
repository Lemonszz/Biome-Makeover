package party.lemons.biomemakeover.block;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import party.lemons.biomemakeover.BiomeMakeover;

public enum SucculentType implements StringRepresentable {
    NONE("none"),
    ECHEVERIA("echeveria"),

    CROWN_OF_THORNS("crown_of_thorns"),
    SNAKE_GRASS("snake_grass"),

            ;

    private final String name;
    public final ResourceLocation loottable;
    SucculentType(String name)
    {
        this.name = name;
        this.loottable = BiomeMakeover.ID("blocks/succulent/" + name);
    }

    @Override
    public String getSerializedName() {
        return name;
    }
}