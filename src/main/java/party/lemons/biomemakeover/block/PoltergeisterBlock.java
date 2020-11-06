package party.lemons.biomemakeover.block;

import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.world.BlockView;
import party.lemons.biomemakeover.block.blockentity.PoltergeistBlockEntity;

public class PoltergeisterBlock extends BMBlock implements BlockEntityProvider
{
	public PoltergeisterBlock(Settings settings)
	{
		super(settings);
	}

	@Override
	public BlockEntity createBlockEntity(BlockView world)
	{
		return new PoltergeistBlockEntity();
	}
}
