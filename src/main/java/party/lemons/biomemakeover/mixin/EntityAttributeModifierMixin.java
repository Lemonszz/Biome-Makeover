package party.lemons.biomemakeover.mixin;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import party.lemons.biomemakeover.util.access.EntityAttributeModifierAccess;

import java.util.UUID;

@Mixin(EntityAttributeModifier.class)
public class EntityAttributeModifierMixin implements EntityAttributeModifierAccess
{
	@Shadow @Final @Mutable private UUID uuid;

	@Override
	public void bm_setUUID(UUID id)
	{
		this.uuid = id;
	}
}
