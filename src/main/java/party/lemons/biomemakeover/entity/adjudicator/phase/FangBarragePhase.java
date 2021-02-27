package party.lemons.biomemakeover.entity.adjudicator.phase;

import net.minecraft.block.BlockState;
import net.minecraft.entity.mob.EvokerFangsEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorState;
import party.lemons.biomemakeover.util.HorizontalDirection;

public class FangBarragePhase extends TimedPhase
{
	public FangBarragePhase(Identifier id, AdjudicatorEntity adjudicator)
	{
		super(id, 100, adjudicator);
	}

	@Override
	protected void initAI()
	{

	}

	@Override
	public void onEnterPhase()
	{
		super.onEnterPhase();
		adjudicator.setState(AdjudicatorState.SUMMONING);
	}

	@Override
	public BlockPos getStartPosition()
	{
		return adjudicator.getHomePosition();
	}

	@Override
	public void tick()
	{
		super.tick();
		if(time % 50 == 0)
		{
			for(HorizontalDirection direction : HorizontalDirection.values())
			{
				for(int i = 0; i < 10; i++)
				{
					conjureFangs(adjudicator.getX() + (direction.x * (i + 1)), adjudicator.getZ() + (direction.z * (i + 1)), 10, adjudicator.getY(), (float) (Math.random() * Math.PI), i);
				}
			}
		}
	}

	private void conjureFangs(double x, double z, double maxY, double y, float yaw, int warmup) {
		BlockPos blockPos = new BlockPos(x, y, z);
		boolean bl = false;
		double d = 0.0D;

		do {
			BlockPos blockPos2 = blockPos.down();
			BlockState blockState = adjudicator.world.getBlockState(blockPos2);
			if (blockState.isSideSolidFullSquare(adjudicator.world, blockPos2, Direction.UP)) {
				if (!adjudicator.world.isAir(blockPos)) {
					BlockState blockState2 = adjudicator.world.getBlockState(blockPos);
					VoxelShape voxelShape = blockState2.getCollisionShape(adjudicator.world, blockPos);
					if (!voxelShape.isEmpty()) {
						d = voxelShape.getMax(Direction.Axis.Y);
					}
				}

				bl = true;
				break;
			}

			blockPos = blockPos.down();
		} while(blockPos.getY() >= MathHelper.floor(maxY) - 1);

		if (bl) {
			adjudicator.world.spawnEntity(new EvokerFangsEntity(adjudicator.world, x, (double)blockPos.getY() + d, z, yaw, warmup, adjudicator));
		}

	}
}
