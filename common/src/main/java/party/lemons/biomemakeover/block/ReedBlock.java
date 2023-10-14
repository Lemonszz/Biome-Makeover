package party.lemons.biomemakeover.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ReedBlock extends WaterTallFlowerBlock{

    private static final VoxelShape SHAPE_BOTTOM = Shapes.box(0.15, 0, 0.15, 0.85, 1, 0.85);
    private static final VoxelShape SHAPE_TOP = Shapes.box(0.15, 0, 0.15, 0.85, 0.85, 0.85);

    public ReedBlock(BlockBehaviour.Properties properties)
    {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        Vec3 offset = blockState.getOffset(blockGetter, blockPos);
        return (blockState.getValue(HALF) == DoubleBlockHalf.LOWER ? SHAPE_BOTTOM : SHAPE_TOP).move(offset.x, offset.y, offset.z);
    }
    @Override
    public float getMaxHorizontalOffset() {
        return 0.15F;
    }
}
