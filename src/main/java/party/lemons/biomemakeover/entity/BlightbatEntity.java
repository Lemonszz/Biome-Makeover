package party.lemons.biomemakeover.entity;

import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.world.World;
import party.lemons.biomemakeover.init.BMEntities;

public class BlightbatEntity extends BatEntity
{
	public AttributeContainer attributeContainer;

	public BlightbatEntity(World world)
	{
		super(BMEntities.BLIGHTBAT, world);
	}

	@Override
	public AttributeContainer getAttributes()
	{
		if(attributeContainer == null)
			attributeContainer = new AttributeContainer(BatEntity.createBatAttributes().build());
		return attributeContainer;
	}
}
