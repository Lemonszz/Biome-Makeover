package party.lemons.biomemakeover.world.feature;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.WitchEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.*;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.structure.processor.BlockRotStructureProcessor;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.BlockView;
import net.minecraft.world.Heightmap;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.init.BMStructures;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class SunkenRuinFeature extends StructureFeature<SunkenRuinFeature.SunkenRuinFeatureConfig>
{
	private static final Identifier[] LARGE_PIECES = new Identifier[]{BiomeMakeover.ID("sunken_ruins/sunken_1"), BiomeMakeover.ID("sunken_ruins/sunken_2"), BiomeMakeover.ID("sunken_ruins/sunken_3")};
	private static final Identifier[] SMALL_PIECES = new Identifier[]{BiomeMakeover.ID("sunken_ruins/sunken_small_1"), BiomeMakeover.ID("sunken_ruins/sunken_small_2"), BiomeMakeover.ID("sunken_ruins/sunken_small_3"), BiomeMakeover.ID("sunken_ruins/sunken_small_4"), BiomeMakeover.ID("sunken_ruins/sunken_small_5"), BiomeMakeover.ID("sunken_ruins/sunken_small_6")};


	public SunkenRuinFeature(Codec<SunkenRuinFeatureConfig> codec)
	{
		super(codec);
	}

	@Override
	public StructureStartFactory<SunkenRuinFeatureConfig> getStructureStartFactory()
	{
		return Start::new;
	}

	public static class Start extends StructureStart<SunkenRuinFeatureConfig>
	{
		public Start(StructureFeature<SunkenRuinFeatureConfig> structureFeature, int chunkX, int chunkZ, BlockBox box, int references, long seed)
		{
			super(structureFeature, chunkX, chunkZ, box, references, seed);
		}

		@Override
		public void init(DynamicRegistryManager registryManager, ChunkGenerator chunkGenerator, StructureManager manager, int chunkX, int chunkZ, Biome biome, SunkenRuinFeatureConfig config)
		{
			int x = chunkX * 16;
			int z = chunkZ * 16;
			BlockPos blockPos = new BlockPos(x, 90, z);
			BlockRotation blockRotation = BlockRotation.random(this.random);

			boolean isLarge = random.nextFloat() <= config.largeProbability;
			addPieces(manager, blockPos, blockRotation, children, random, isLarge);
			if(isLarge && random.nextFloat() <= config.clusterProbability)
			{
				createSmaller(manager, random, blockRotation, blockPos, children);
			}

			this.setBoundingBoxFromChildren();
		}
	}

	private static void createSmaller(StructureManager manager, Random random, BlockRotation rotation, BlockPos pos, List<StructurePiece> pieces)
	{
		int x = pos.getX();
		int z = pos.getZ();
		BlockPos blockPos = Structure.transformAround(new BlockPos(15, 0, 15), BlockMirror.NONE, rotation, BlockPos.ORIGIN).add(x, 0, z);
		BlockBox blockBox = BlockBox.create(x, 0, z, blockPos.getX(), 0, blockPos.getZ());
		BlockPos blockPos2 = new BlockPos(Math.min(x, blockPos.getX()), 0, Math.min(z, blockPos.getZ()));
		List<BlockPos> possiblePositions = getRoomPositions(random, blockPos2.getX(), blockPos2.getZ());
		int attempts = MathHelper.nextInt(random, 4, 8);

		for(int l = 0; l < attempts; ++l)
		{
			if(!possiblePositions.isEmpty())
			{
				int index = random.nextInt(possiblePositions.size());
				BlockPos checkPos = possiblePositions.remove(index);
				int checkX = checkPos.getX();
				int checkZ = checkPos.getZ();
				BlockRotation blockRotation = BlockRotation.random(random);
				BlockPos blockPos4 = Structure.transformAround(new BlockPos(5, 0, 6), BlockMirror.NONE, blockRotation, BlockPos.ORIGIN).add(checkX, 0, checkZ);
				BlockBox blockBox2 = BlockBox.create(checkX, 0, checkZ, blockPos4.getX(), 0, blockPos4.getZ());
				if(!blockBox2.intersects(blockBox))
				{
					addPieces(manager, checkPos, blockRotation, pieces, random, false);
				}
			}
		}

	}

	private static List<BlockPos> getRoomPositions(Random random, int x, int z)
	{
		List<BlockPos> list = Lists.newArrayList();
		list.add(new BlockPos(x - 16 + MathHelper.nextInt(random, 1, 8), 90, z + 16 + MathHelper.nextInt(random, 1, 7)));
		list.add(new BlockPos(x - 16 + MathHelper.nextInt(random, 1, 8), 90, z + MathHelper.nextInt(random, 1, 7)));
		list.add(new BlockPos(x - 16 + MathHelper.nextInt(random, 1, 8), 90, z - 16 + MathHelper.nextInt(random, 4, 8)));
		list.add(new BlockPos(x + MathHelper.nextInt(random, 1, 7), 90, z + 16 + MathHelper.nextInt(random, 1, 7)));
		list.add(new BlockPos(x + MathHelper.nextInt(random, 1, 7), 90, z - 16 + MathHelper.nextInt(random, 4, 6)));
		list.add(new BlockPos(x + 16 + MathHelper.nextInt(random, 1, 7), 90, z + 16 + MathHelper.nextInt(random, 3, 8)));
		list.add(new BlockPos(x + 16 + MathHelper.nextInt(random, 1, 7), 90, z + MathHelper.nextInt(random, 1, 7)));
		list.add(new BlockPos(x + 16 + MathHelper.nextInt(random, 1, 7), 90, z - 16 + MathHelper.nextInt(random, 4, 8)));
		return list;
	}

	private static void addPieces(StructureManager manager, BlockPos pos, BlockRotation rotation, List<StructurePiece> pieces, Random random, boolean large)
	{

		Identifier[] identifiers = large ? LARGE_PIECES : SMALL_PIECES;
		int i = random.nextInt(identifiers.length);
		pieces.add(new Piece(manager, identifiers[i], pos, rotation, large));
	}

	public static class Piece extends SimpleStructurePiece
	{
		private final Identifier template;
		private final BlockRotation rotation;
		private final boolean large;

		public Piece(StructureManager structureManager, Identifier template, BlockPos pos, BlockRotation rotation, boolean large)
		{
			super(BMStructures.SUNKEN_RUIN_PIECE, 0);
			this.template = template;
			this.pos = pos;
			this.rotation = rotation;
			this.large = large;
			this.initialize(structureManager);
		}

		public Piece(StructureManager manager, CompoundTag tag)
		{
			super(BMStructures.SUNKEN_RUIN_PIECE, tag);
			this.template = new Identifier(tag.getString("Template"));
			this.rotation = BlockRotation.valueOf(tag.getString("Rot"));
			this.large = tag.getBoolean("IsLarge");
			this.initialize(manager);
		}

		private void initialize(StructureManager structureManager)
		{
			Structure structure = structureManager.getStructureOrBlank(this.template);
			StructurePlacementData structurePlacementData = (new StructurePlacementData()).setRotation(this.rotation).setMirror(BlockMirror.NONE).addProcessor(BlockIgnoreStructureProcessor.IGNORE_AIR_AND_STRUCTURE_BLOCKS);
			this.setStructureData(structure, this.pos, structurePlacementData);
		}

		protected void toNbt(CompoundTag tag)
		{
			super.toNbt(tag);
			tag.putString("Template", this.template.toString());
			tag.putString("Rot", this.rotation.name());
			tag.putBoolean("IsLarge", this.large);
		}

		protected void handleMetadata(String metadata, BlockPos pos, ServerWorldAccess world, Random random, BlockBox box)
		{
			if("chest".equals(metadata))
			{
				world.setBlockState(pos, Blocks.CHEST.getDefaultState().with(ChestBlock.WATERLOGGED, world.getFluidState(pos).isIn(FluidTags.WATER)), 2);
				BlockEntity blockEntity = world.getBlockEntity(pos);
				if(blockEntity instanceof ChestBlockEntity)
				{
					((ChestBlockEntity) blockEntity).setLootTable(LOOT, random.nextLong());
				}
			}else if("witch".equals(metadata) && random.nextBoolean())
			{
				if(world.getBlockState(pos.up()).isAir())
				{
					WitchEntity witch = EntityType.WITCH.create(world.toServerWorld());
					witch.setPersistent();
					witch.refreshPositionAndAngles(pos, 0.0F, 0.0F);
					witch.initialize(world, world.getLocalDifficulty(pos), SpawnReason.STRUCTURE, null, null);
					world.spawnEntityAndPassengers(witch);
					if(pos.getY() >= world.getSeaLevel())
					{
						world.setBlockState(pos, Blocks.AIR.getDefaultState(), 2);
					}else
					{
						world.setBlockState(pos, Blocks.WATER.getDefaultState(), 2);
					}
				}
			}

		}

		public boolean generate(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox boundingBox, ChunkPos chunkPos, BlockPos blockPos)
		{
			this.placementData.clearProcessors().addProcessor(new BlockRotStructureProcessor(1)).addProcessor(BlockIgnoreStructureProcessor.IGNORE_AIR_AND_STRUCTURE_BLOCKS);
			int yWater = world.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, pos.getX(), pos.getZ());
			int yy;
			if(world.getFluidState(new BlockPos(pos.getX(), yWater, pos.getZ())).isIn(FluidTags.WATER)) yy = yWater;
			else yy = world.getTopY(Heightmap.Type.WORLD_SURFACE_WG, this.pos.getX(), this.pos.getZ());

			this.pos = new BlockPos(this.pos.getX(), yy, this.pos.getZ());
			BlockPos blockPos2 = Structure.transformAround(new BlockPos(this.structure.getSize().getX() - 1, 0, this.structure.getSize().getZ() - 1), BlockMirror.NONE, this.rotation, BlockPos.ORIGIN).add(this.pos);
			this.pos = new BlockPos(this.pos.getX(), this.method_14829(this.pos, world, blockPos2) - RandomUtil.randomRange(1, 2), this.pos.getZ());
			boolean generate = super.generate(world, structureAccessor, chunkGenerator, random, boundingBox, chunkPos, blockPos);

			if(generate)
			{
				//TODO: optimise?
				List<Structure.StructureBlockInfo> dirt = this.structure.getInfosForBlock(this.pos, this.placementData, Blocks.DIRT);
				for(Structure.StructureBlockInfo info : dirt)
				{
					if(!world.getBlockState(info.pos.up()).isOpaque())
						world.setBlockState(info.pos, Blocks.GRASS.getDefaultState(), 2);
				}

				List<Structure.StructureBlockInfo> clay = this.structure.getInfosForBlock(this.pos, this.placementData, Blocks.CLAY);
				for(Structure.StructureBlockInfo info : clay)
				{
					if(!world.getBlockState(info.pos.up()).isOpaque())
						world.setBlockState(info.pos, Blocks.GRASS.getDefaultState(), 2);
					else world.setBlockState(info.pos, Blocks.DIRT.getDefaultState(), 2);
				}

				List<Structure.StructureBlockInfo> sand = this.structure.getInfosForBlock(this.pos, this.placementData, Blocks.SAND);
				for(Structure.StructureBlockInfo info : sand)
				{
					if(!world.getBlockState(info.pos.up()).isOpaque())
						world.setBlockState(info.pos, Blocks.GRASS.getDefaultState(), 2);
					else world.setBlockState(info.pos, Blocks.DIRT.getDefaultState(), 2);
				}
			}
			return generate;
		}

		private int method_14829(BlockPos blockPos, BlockView blockView, BlockPos blockPos2)
		{
			int i = blockPos.getY();
			int j = 512;
			int k = i - 1;
			int l = 0;
			Iterator var8 = BlockPos.iterate(blockPos, blockPos2).iterator();

			while(var8.hasNext())
			{
				BlockPos blockPos3 = (BlockPos) var8.next();
				int m = blockPos3.getX();
				int n = blockPos3.getZ();
				int o = blockPos.getY() - 1;
				BlockPos.Mutable mutable = new BlockPos.Mutable(m, o, n);
				BlockState blockState = blockView.getBlockState(mutable);

				for(FluidState fluidState = blockView.getFluidState(mutable); (blockState.isAir() || fluidState.isIn(FluidTags.WATER) || blockState.getBlock().isIn(BlockTags.ICE)) && o > 1; fluidState = blockView.getFluidState(mutable))
				{
					--o;
					mutable.set(m, o, n);
					blockState = blockView.getBlockState(mutable);
				}

				j = Math.min(j, o);
				if(o < k - 2)
				{
					++l;
				}
			}

			int p = Math.abs(blockPos.getX() - blockPos2.getX());
			if(k - j > 2 && l > p - 2)
			{
				i = j + 1;
			}

			return i;
		}
	}

	public static class SunkenRuinFeatureConfig implements FeatureConfig
	{
		public static final Codec<SunkenRuinFeatureConfig> CODEC = RecordCodecBuilder.create((instance)->
		{
			return instance.group(Codec.floatRange(0.0F, 1.0F).fieldOf("large_probability").forGetter((cfg)->cfg.largeProbability), Codec.floatRange(0.0F, 1.0F).fieldOf("cluster_probability").forGetter((cfg)->cfg.clusterProbability)).apply(instance, SunkenRuinFeatureConfig::new);
		});

		public final float largeProbability;
		public final float clusterProbability;

		public SunkenRuinFeatureConfig(float largeProbability, float clusterProbability)
		{
			this.largeProbability = largeProbability;
			this.clusterProbability = clusterProbability;
		}
	}

	private static final Identifier LOOT = BiomeMakeover.ID("sunken_ruin");
}
