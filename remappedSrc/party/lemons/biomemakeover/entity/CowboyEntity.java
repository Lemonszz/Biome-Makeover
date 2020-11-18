package party.lemons.biomemakeover.entity;

import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.mob.PillagerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.World;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.init.BMItems;

public class CowboyEntity extends PillagerEntity
{
	public AttributeContainer attributeContainer;

	public CowboyEntity(World world)
	{
		super(BMEntities.COWBOY, world);
	}

	protected void initEquipment(LocalDifficulty difficulty) {
		super.initEquipment(difficulty);

		this.equipStack(EquipmentSlot.HEAD, new ItemStack(BMItems.COWBOY_HAT));
	}

	@Override
	public AttributeContainer getAttributes()
	{
		if(attributeContainer == null)
			attributeContainer =  new AttributeContainer(
					PillagerEntity.createPillagerAttributes().build());
		return attributeContainer;
	}
}
