package party.lemons.biomemakeover.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.RavagerEntity;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import party.lemons.biomemakeover.entity.RootlingEntity;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.init.BMItems;

import java.util.Random;

public class RootlingCropBlock extends PlantBlock implements Fertilizable
{
	public static final IntProperty AGE_4 = IntProperty.of("age", 0, 4);
	private static final VoxelShape[] AGE_TO_SHAPE = new VoxelShape[]
			{
					Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
					Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 6.0D, 16.0D),
					Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 10.0D, 16.0D),
					Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D),
					Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 16.0D, 16.0D)
			};


	public RootlingCropBlock(Settings settings)
	{
		super(settings);
		this.setDefaultState(this.getStateManager().getDefaultState().with(AGE_4, 0));
	}

	@Override
	public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved)
	{
		super.onStateReplaced(state, world, pos, newState, moved);

		if(!world.isClient() && newState.getBlock() == this && isMature(newState))
		{
			world.breakBlock(pos, false);
			RootlingEntity rootling = BMEntities.ROOTLING.create(world);
			rootling.updatePositionAndAngles(pos.getX() + 0.5F, pos.getY(), pos.getZ() + 0.5F, world.random.nextFloat(), world.random.nextFloat());
			rootling.setVelocity(0, 0.25F, 0);
			rootling.randomizeFlower();
			world.spawnEntity(rootling);
		}
	}

	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return AGE_TO_SHAPE[state.get(AGE_4)];
	}

	protected boolean canPlantOnTop(BlockState floor, BlockView world, BlockPos pos) {
		return floor.isOf(Blocks.FARMLAND);
	}

	protected int getAge(BlockState state) {
		return state.get(AGE_4);
	}

	public BlockState withAge(int age) {
		return this.getDefaultState().with(AGE_4, age);
	}

	public boolean isMature(BlockState state) {
		return state.get(AGE_4) >= getMaxAge();
	}

	public int getMaxAge()
	{
		return 4;
	}

	public boolean hasRandomTicks(BlockState state) {
		return !this.isMature(state);
	}

	public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		if (world.getBaseLightLevel(pos, 0) >= 9) {
			int i = this.getAge(state);
			if (i < this.getMaxAge()) {
				float f = getAvailableMoisture(this, world, pos);
				if (random.nextInt((int)(25.0F / f) + 1) == 0) {
					world.setBlockState(pos, this.withAge(i + 1), 2);
				}
			}
		}
	}

	public void applyGrowth(World world, BlockPos pos, BlockState state) {
		int i = this.getAge(state) + this.getGrowthAmount(world);
		int j = this.getMaxAge();
		if (i > j) {
			i = j;
		}

		world.setBlockState(pos, this.withAge(i), 2);
	}

	protected int getGrowthAmount(World world) {
		return MathHelper.nextInt(world.random, 1, 2 );
	}

	protected static float getAvailableMoisture(Block block, BlockView world, BlockPos pos) {
		float f = 1.0F;
		BlockPos blockPos = pos.down();

		for(int i = -1; i <= 1; ++i) {
			for(int j = -1; j <= 1; ++j) {
				float g = 0.0F;
				BlockState blockState = world.getBlockState(blockPos.add(i, 0, j));
				if (blockState.isOf(Blocks.FARMLAND)) {
					g = 1.0F;
					if (blockState.get(FarmlandBlock.MOISTURE) > 0) {
						g = 3.0F;
					}
				}

				if (i != 0 || j != 0) {
					g /= 4.0F;
				}

				f += g;
			}
		}

		BlockPos blockPos2 = pos.north();
		BlockPos blockPos3 = pos.south();
		BlockPos blockPos4 = pos.west();
		BlockPos blockPos5 = pos.east();
		boolean bl = block == world.getBlockState(blockPos4).getBlock() || block == world.getBlockState(blockPos5).getBlock();
		boolean bl2 = block == world.getBlockState(blockPos2).getBlock() || block == world.getBlockState(blockPos3).getBlock();
		if (bl && bl2) {
			f /= 2.0F;
		} else {
			boolean bl3 = block == world.getBlockState(blockPos4.north()).getBlock() || block == world.getBlockState(blockPos5.north()).getBlock() || block == world.getBlockState(blockPos5.south()).getBlock() || block == world.getBlockState(blockPos4.south()).getBlock();
			if (bl3) {
				f /= 2.0F;
			}
		}

		return f;
	}

	public boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		return (world.getBaseLightLevel(pos, 0) >= 8 || world.isSkyVisible(pos)) && super.canPlaceAt(state, world, pos);
	}

	public void onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity) {
		if (entity instanceof RavagerEntity && world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
			world.breakBlock(pos, true, entity);
		}

		super.onEntityCollision(state, world, pos, entity);
	}

	@Environment(EnvType.CLIENT)
	protected ItemConvertible getSeedsItem() {
		return BMItems.ROOTLING_SEEDS;
	}

	@Environment(EnvType.CLIENT)
	public ItemStack getPickStack(BlockView world, BlockPos pos, BlockState state) {
		return new ItemStack(this.getSeedsItem());
	}

	public boolean isFertilizable(BlockView world, BlockPos pos, BlockState state, boolean isClient) {
		return !this.isMature(state);
	}

	public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
		return true;
	}

	public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
		this.applyGrowth(world, pos, state);
	}

	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(AGE_4);
	}
}
