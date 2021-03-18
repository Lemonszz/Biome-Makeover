package party.lemons.biomemakeover.entity.ai;

import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class EmptyMobNavigation extends MobNavigation
{
	public EmptyMobNavigation(MobEntity mobEntity, World world)
	{
		super(mobEntity, world);
	}

	@Override
	public Path findPathTo(BlockPos target, int distance)
	{
		return null;
	}
}
