package party.lemons.biomemakeover.entity.adjudicator;

import com.google.common.collect.Lists;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class AdjudicatorRoomListener
{
	private static List<AdjudicatorEntity> activeAdjudicators = Lists.newArrayList();

	public static void init()
	{
		PlayerBlockBreakEvents.AFTER.register(
				(world, player, pos, state, blockEntity)->
	              {
	              	if(world.isClient())
	              		return;

	              	activeAdjudicators.removeIf((e)->!e.isAlive() || e.removed);
                  for(AdjudicatorEntity adj : activeAdjudicators)
                  {
                      if(adj.getArenaBounds() != null && adj.getArenaBounds().contains(new Vec3d(pos.getX(), pos.getY(), pos.getZ())))
                      {
                          adj.setActive();
                      }
                  }
	              });

		//TODO: NO PLACE EVENT?????
	}

	//TOOD: use place event
	public static void onPlaceBlock(BlockPos pos)
	{
		activeAdjudicators.removeIf((e)->!e.isAlive() || e.removed);
		for(AdjudicatorEntity adj : activeAdjudicators)
		{
			if(adj.getArenaBounds() != null && adj.getArenaBounds().contains(new Vec3d(pos.getX(), pos.getY(), pos.getZ())))
			{
				adj.setActive();
			}
		}
	}

	public static void enableAdjudicator(AdjudicatorEntity entity)
	{
		activeAdjudicators.add(entity);
	}

	public static void disableAdjudicator(AdjudicatorEntity entity)
	{
		activeAdjudicators.remove(entity);
	}

}
