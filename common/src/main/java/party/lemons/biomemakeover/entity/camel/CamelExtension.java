package party.lemons.biomemakeover.entity.camel;

import net.minecraft.world.entity.AnimationState;

public interface CamelExtension
{
    void setAnimationStates(AnimationState sitAnim, AnimationState sitPos, AnimationState sitUp, AnimationState idle, AnimationState dash);
}
