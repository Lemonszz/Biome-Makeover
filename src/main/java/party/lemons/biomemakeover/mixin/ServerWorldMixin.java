package party.lemons.biomemakeover.mixin;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.gen.feature.StructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import party.lemons.biomemakeover.init.BMStructures;

@Mixin(ServerWorld.class)
public class ServerWorldMixin
{
	@ModifyVariable(method = "locateStructure", at = @At("HEAD"))
	private StructureFeature<?> changeMansion(StructureFeature<?> feature)
	{
		if(feature == StructureFeature.MANSION)
			return BMStructures.MANSION;

		return feature;
	}
}
