package party.lemons.biomemakeover.block.blockentity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import party.lemons.biomemakeover.entity.LightningBugEntity;
import party.lemons.biomemakeover.init.BMBlockEntities;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.util.RandomUtil;

public class LightningBugBottleBlockEntity extends BlockEntity
{
	private LightningBugEntity bug;

	public LightningBugBottleBlockEntity()
	{
		super(BMBlockEntities.LIGHTNING_BUG_BOTTLE);
	}

	public LightningBugEntity getEntity()
	{
		if(bug == null)
		{
			bug = BMEntities.LIGHTNING_BUG.create(getWorld());
			bug.setPos(RandomUtil.RANDOM.nextInt(500), RandomUtil.RANDOM.nextInt(200), RandomUtil.RANDOM.nextInt(500));
		}

		return bug;
	}
}
