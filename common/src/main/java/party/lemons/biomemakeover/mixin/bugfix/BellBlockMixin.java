package party.lemons.biomemakeover.mixin.bugfix;

import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

//https://bugs.mojang.com/browse/MC-253819

@Mixin(BellBlock.class)
public abstract class BellBlockMixin extends BaseEntityBlock
{
	@Shadow @Final public static DirectionProperty FACING;

	private BellBlockMixin(Properties properties)
	{
		super(properties);
	}

	@Override
	public BlockState rotate(BlockState blockState, Rotation rotation) {
		return blockState.setValue(FACING, rotation.rotate(blockState.getValue(FACING)));
	}

	@Override
	public BlockState mirror(BlockState blockState, Mirror mirror) {
		return blockState.rotate(mirror.getRotation(blockState.getValue(FACING)));
	}
}
