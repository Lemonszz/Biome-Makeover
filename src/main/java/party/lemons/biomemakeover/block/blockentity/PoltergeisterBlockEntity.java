package party.lemons.biomemakeover.block.blockentity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Tickable;
import net.minecraft.world.BlockView;
import party.lemons.biomemakeover.init.BMBlockEntities;
import party.lemons.biomemakeover.util.PoltergeistHandler;

public class PoltergeisterBlockEntity extends BlockEntity implements Tickable
{
	public PoltergeisterBlockEntity()
	{
		super(BMBlockEntities.POLTERGEISTER);
	}

	@Override
	public void tick()
	{
		if(world != null && !world.isClient())
			PoltergeistHandler.doPoltergeist(world, getPos(), 5);
	}
}
