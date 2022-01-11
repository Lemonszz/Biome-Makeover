package party.lemons.biomemakeover.entity.adjudicator.phase;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorEntity;
import party.lemons.biomemakeover.entity.adjudicator.AdjudicatorState;
import party.lemons.biomemakeover.init.BMEffects;
import party.lemons.biomemakeover.util.HorizontalDirection;

public class FangBarragePhase extends TimedPhase
{
    public FangBarragePhase(ResourceLocation id, AdjudicatorEntity adjudicator)
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
            adjudicator.playSound(BMEffects.ADJUDICATOR_SPELL_2, 1F, 1F);
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
            BlockPos blockPos2 = blockPos.below();
            BlockState blockState = adjudicator.level.getBlockState(blockPos2);
            if (blockState.isFaceSturdy(adjudicator.level, blockPos2, Direction.UP)) {
                if (!adjudicator.level.isEmptyBlock(blockPos)) {
                    BlockState blockState2 = adjudicator.level.getBlockState(blockPos);
                    VoxelShape voxelShape = blockState2.getCollisionShape(adjudicator.level, blockPos);
                    if (!voxelShape.isEmpty()) {
                        d = voxelShape.max(Direction.Axis.Y);
                    }
                }

                bl = true;
                break;
            }

            blockPos = blockPos.below();
        } while(blockPos.getY() >= Mth.floor(maxY) - 1);

        if (bl) {
            adjudicator.level.addFreshEntity(new EvokerFangs(adjudicator.level, x, (double)blockPos.getY() + d, z, yaw, warmup, adjudicator));
        }

    }
}