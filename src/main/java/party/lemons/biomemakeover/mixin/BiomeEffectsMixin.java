package party.lemons.biomemakeover.mixin;

import net.minecraft.world.biome.BiomeEffects;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import party.lemons.biomemakeover.util.access.BiomeEffectsAccessor;

@Mixin(BiomeEffects.class)
public class BiomeEffectsMixin implements BiomeEffectsAccessor
{
	@Shadow @Final @Mutable private int waterColor;

	@Override
	public void setWaterColor(int color)
	{
		this.waterColor = color;
	}
}
