package party.lemons.biomemakeover.util.access;

import net.minecraft.util.SignType;

import java.util.Set;

public interface SignTypeHelper
{
	Set<SignType> bm_getTypes();

	static SignType register(SignType type)
	{
		((SignTypeHelper) type).bm_getTypes().add(type);

		return type;
	}
}
