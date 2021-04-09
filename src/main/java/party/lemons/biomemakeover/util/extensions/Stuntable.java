package party.lemons.biomemakeover.util.extensions;

import net.minecraft.entity.passive.PassiveEntity;

public interface Stuntable
{
	boolean isStunted();

	void setStunted(boolean stunted);

	static void setStunted(PassiveEntity entity, boolean stunted)
	{
		((Stuntable)entity).setStunted(stunted);
	}
}
