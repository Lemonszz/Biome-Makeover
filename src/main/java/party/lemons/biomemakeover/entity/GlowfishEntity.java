package party.lemons.biomemakeover.entity;

import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.passive.BatEntity;
import net.minecraft.entity.passive.SalmonEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.init.BMItems;
import party.lemons.biomemakeover.item.BMItem;

public class GlowfishEntity extends SalmonEntity
{
	public AttributeContainer attributeContainer;

	public GlowfishEntity(World world)
	{
		super(BMEntities.GLOWFISH, world);
	}

	protected ItemStack getFishBucketItem() {
		return new ItemStack(BMItems.GLOWFISH_BUCKET);
	}

	@Override
	public AttributeContainer getAttributes()
	{
		if(attributeContainer == null)
			attributeContainer =  new AttributeContainer(
					SalmonEntity.createFishAttributes().build());
		return attributeContainer;
	}
}
