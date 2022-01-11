package party.lemons.biomemakeover.util.registry;

import net.minecraft.client.renderer.RenderType;

public enum RType
{
    SOLID,
    TRIPWIRE,
    CUTOUT_MIPPED,
    CUTOUT,
    TRANSLUCENT;

    public RenderType getAsRenderType()
    {
        switch(this)
        {
            case SOLID:
                return RenderType.solid();
            case TRIPWIRE:
                return RenderType.tripwire();
            case CUTOUT_MIPPED:
                return RenderType.cutoutMipped();
            case CUTOUT:
                return RenderType.cutout();
            case TRANSLUCENT:
                return RenderType.translucent();
        }
        return null;
    }
}