package party.lemons.biomemakeover.entity.render;

import net.minecraft.client.render.entity.model.DrownedEntityModel;
import net.minecraft.entity.mob.ZombieEntity;
import party.lemons.biomemakeover.entity.DecayedEntity;

public class DecayedEntityModel<T extends ZombieEntity> extends DrownedEntityModel<T>
{
	public DecayedEntityModel(float f, float g, int i, int j)
	{
		super(f, g, i, j);
	}

	public DecayedEntityModel(float f, boolean bl)
	{
		super(f, bl);
	}

	@Override
	public boolean isAttacking(T zombieEntity)
	{
		return false;
	}
}
