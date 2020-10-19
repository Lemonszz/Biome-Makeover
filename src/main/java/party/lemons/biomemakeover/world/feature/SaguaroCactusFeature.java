package party.lemons.biomemakeover.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import party.lemons.biomemakeover.block.SaguaroCactusBlock;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.Random;

public class SaguaroCactusFeature extends Feature<DefaultFeatureConfig>
{
	private SaguaroCactusBlock CACTUS = BMBlocks.SAGUARO_CACTUS;
	private final Direction[] NORTH_SOUTH = {Direction.NORTH, Direction.SOUTH};
	private final Direction[] EAST_WEST = {Direction.EAST, Direction.WEST};

	public SaguaroCactusFeature(Codec<DefaultFeatureConfig> configCodec)
	{
		super(configCodec);
	}

	@Override
	public boolean generate(StructureWorldAccess world, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, DefaultFeatureConfig featureConfig)
	{
		int xOffset = random.nextInt(8) - random.nextInt(8);
		int zOffset = random.nextInt(8) - random.nextInt(8);
		int yGenerate = world.getTopY(Heightmap.Type.WORLD_SURFACE_WG, blockPos.getX() + xOffset, blockPos.getZ() + zOffset);

		return generateCactus(world, random.nextBoolean(), new BlockPos(blockPos.getX() + xOffset, yGenerate, blockPos.getZ() + zOffset), random, false);
	}

	public boolean generateCactus(StructureWorldAccess world, boolean northSouth, BlockPos pos, Random random, boolean isBig)
	{
		if(!CACTUS.getDefaultState().canPlaceAt(world, pos))
			return false;

		boolean hasArms = random.nextInt(10) > 1;
		boolean has2Arms = random.nextInt(5) != 0;

		int centerHeight = RandomUtil.randomRange(4, 8);

		BlockPos.Mutable p = new BlockPos.Mutable(pos.getX(), pos.getY(), pos.getZ());
		for(int yy = 0; yy < centerHeight; yy++)
		{
			if(yy > 0)
			{
				if(!world.getBlockState(p).isAir())
					break;
			}

			world.setBlockState(p, CACTUS.getDefaultState(), 2);
			p.move(Direction.UP);
		}

		if(!hasArms)
			return true;

		int centerEndY = p.getY();
		int armStart = RandomUtil.randomRange(1, centerHeight - 2);
		Direction[] directions = northSouth ? NORTH_SOUTH : EAST_WEST;

		if(has2Arms)
		{
			for(int i = 0; i < directions.length; i++)
			{
				Direction d = directions[i];
				generateArm(world, d, p.getX(), pos.getY() + armStart, p.getZ(), centerEndY);
				armStart = RandomUtil.randomRange(1, centerHeight - 2);
			}
		}
		else
		{
			generateArm(world, directions[random.nextInt(directions.length)], p.getX(), pos.getY() + armStart, p.getZ(), centerEndY);
		}

		if(( !isBig && random.nextInt(10) == 0) || (isBig && random.nextInt(50) == 0))
		{
			BlockPos nextPos =  new BlockPos(pos.getX(), centerEndY, pos.getZ());
			if(world.getBlockState(nextPos).isAir())
				generateCactus(world, random.nextBoolean(), nextPos, random, true);
		}
		return true;
	}

	private void generateArm(StructureWorldAccess world, Direction direction, int centerX, int armY, int centerZ, int centerHeight)
	{
		BlockPos.Mutable p = new BlockPos.Mutable(centerX + direction.getOffsetX(), armY, centerZ + direction.getOffsetZ());

		if(!world.getBlockState(p).isAir())
			return;

		BlockPos centerPos = p.offset(direction.getOpposite());
		BlockState centerState = world.getBlockState(centerPos);
		if(!centerState.isOf(CACTUS))
			return;

		world.setBlockState(centerPos, centerState.with(SaguaroCactusBlock.FACING_PROPERTIES.get(direction), true), 2);
		world.setBlockState(p, CACTUS.getDefaultState()
								.with(SaguaroCactusBlock.HORIZONTAL, true)
								.with(SaguaroCactusBlock.HORIZONTAL_DIRECTION, direction.getOpposite())
								.with(SaguaroCactusBlock.FACING_PROPERTIES.get(direction.getOpposite()), true), 2);

		p.move(Direction.UP);
		int amt = Math.max(1, (centerHeight - p.getY()) + RandomUtil.randomRange(-3, -1));
		for(int i = 0; i < amt; i++)
		{
			if(!world.getBlockState(p).isAir())
				return;
			world.setBlockState(p, CACTUS.getDefaultState(), 2);
			p.move(Direction.UP);
		}
	}
}
