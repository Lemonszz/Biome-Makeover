package party.lemons.biomemakeover.block.blockentity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Tickable;
import party.lemons.biomemakeover.block.PoltergeistBlock;
import party.lemons.biomemakeover.init.BMBlockEntities;
import party.lemons.biomemakeover.world.PoltergeistHandler;

public class PoltergeistBlockEntity extends BlockEntity implements Tickable
{
	public PoltergeistBlockEntity()
	{
		super(BMBlockEntities.POLTERGEIST);
	}

	@Override
	public void tick()
	{
		if(world != null && !world.isClient() && world.getBlockState(pos).get(PoltergeistBlock.ENABLED))
			PoltergeistHandler.doPoltergeist(world, getPos(), 5);
	}
}
