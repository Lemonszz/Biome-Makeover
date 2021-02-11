package party.lemons.biomemakeover.block;

import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class ItchingIvyBlock extends IvyBlock
{
	public ItchingIvyBlock(Settings settings)
	{
		super(settings);
	}

	@Override
	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
	}

	@Override
	protected boolean doesScheduleAfterSet()
	{
		return true;
	}
}
