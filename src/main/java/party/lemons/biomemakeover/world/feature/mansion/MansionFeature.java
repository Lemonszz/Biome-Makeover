package party.lemons.biomemakeover.world.feature.mansion;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.*;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.Heightmap;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.gen.ChunkRandom;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.WoodlandMansionFeature;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.init.BMStructures;
import party.lemons.biomemakeover.util.Grid;

import java.util.List;
import java.util.Random;

public class MansionFeature extends StructureFeature<DefaultFeatureConfig>
{
	public MansionFeature(Codec<DefaultFeatureConfig> configCodec)
	{
		super(configCodec);
	}

	@Override
	public StructureStartFactory<DefaultFeatureConfig> getStructureStartFactory()
	{
		return Start::new;
	}

	private class Start extends StructureStart<DefaultFeatureConfig>
	{
		public Start(StructureFeature<DefaultFeatureConfig> feature, int chunkX, int chunkZ, BlockBox box, int references, long seed)
		{
			super(feature, chunkX, chunkZ, box, references, seed);
		}

		@Override
		public void init(DynamicRegistryManager registryManager, ChunkGenerator chunkGenerator, StructureManager manager, int chunkX, int chunkZ, Biome biome, DefaultFeatureConfig config)
		{
			MansionLayout layout = new MansionLayout();
			layout.generateLayout(random);

			int x = chunkX * 16;
			int z = chunkZ * 16;
			BlockPos pos = new BlockPos(x, chunkGenerator.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG), z);

			Grid<MansionRoom> roomGrid = layout.getLayout();

			roomGrid.getEntries().forEach(rm->{
				int xx = pos.getX() + (rm.getPosition().getX() * 12);
				int yy = pos.getY() + (rm.getPosition().getY() * 7);
				int zz = pos.getZ() + (rm.getPosition().getZ() * 12);

				BlockPos offsetPos = new BlockPos(xx, yy, zz);
				BlockRotation rotation =  rm.getRotation(random);
				switch(rotation)
				{
					case NONE:
						break;
					case CLOCKWISE_90:
						offsetPos = offsetPos.add(10, 0, 0);
						break;
					case CLOCKWISE_180:
						offsetPos = offsetPos.add(10, 0, 10);
						break;
					case COUNTERCLOCKWISE_90:
						offsetPos = offsetPos.add(0, 0, 10);
						break;
				}
				children.add(new Piece(manager, rm.getTemplate(random), offsetPos, rotation));
				BlockPos wallPos = new BlockPos(xx, yy, zz);

				//Wall
				if(rm.isConnected(Direction.SOUTH))
				{
				//	children.add(new Piece(manager, getInnerWall(random), wallPos.offset(Direction.SOUTH, 13),  BlockRotation.NONE));
				}
				if(rm.isConnected(Direction.NORTH))
					children.add(new Piece(manager, getInnerWall(random), wallPos.offset(Direction.NORTH),  BlockRotation.NONE));
				if(rm.isConnected(Direction.WEST))
					children.add(new Piece(manager, getInnerWall(random), wallPos.offset(Direction.WEST), BlockRotation.CLOCKWISE_90));
			});

			this.setBoundingBoxFromChildren();
		}

		@Override
		public void generateStructure(StructureWorldAccess world, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox box, ChunkPos chunkPos)
		{
			super.generateStructure(world, structureAccessor, chunkGenerator, random, box, chunkPos);
		}
	}

	public static class Piece extends SimpleStructurePiece
	{
		private final Identifier template;
		private final BlockRotation rotation;

		public Piece(StructureManager structureManager, Identifier template, BlockPos pos, BlockRotation rotation)
		{
			super(BMStructures.MANSION_PIECE, 0);
			this.template = template;
			this.pos = pos;
			this.rotation = rotation;
			this.initialize(structureManager);
		}

		public Piece(StructureManager manager, CompoundTag tag)
		{
			super(BMStructures.MANSION_PIECE, tag);
			this.template = new Identifier(tag.getString("Template"));
			this.rotation = BlockRotation.valueOf(tag.getString("Rot"));

			this.initialize(manager);
		}

		private void initialize(StructureManager structureManager)
		{
			Structure structure = structureManager.getStructureOrBlank(this.template);
			StructurePlacementData structurePlacementData = (new StructurePlacementData()).setMirror(BlockMirror.NONE).setRotation(this.rotation).addProcessor(BlockIgnoreStructureProcessor.IGNORE_AIR_AND_STRUCTURE_BLOCKS);
			this.setStructureData(structure, this.pos, structurePlacementData);
		}

		protected void toNbt(CompoundTag tag)
		{
			super.toNbt(tag);
			tag.putString("Template", this.template.toString());
			tag.putString("Rot", this.rotation.name());
		}

		@Override
		protected void handleMetadata(String metadata, BlockPos pos, ServerWorldAccess serverWorldAccess, Random random, BlockBox boundingBox)
		{

		}

		@Override
		public boolean generate(StructureWorldAccess structureWorldAccess, StructureAccessor structureAccessor, ChunkGenerator chunkGenerator, Random random, BlockBox boundingBox, ChunkPos chunkPos, BlockPos blockPos)
		{
			return super.generate(structureWorldAccess, structureAccessor, chunkGenerator, random, boundingBox, chunkPos, blockPos);
		}
	}

	public static List<Identifier> CORRIDOR_STRAIGHT = Lists.newArrayList(
			BiomeMakeover.ID("mansion/corridor/straight/corridor_straight_1")
	);

	public static List<Identifier> CORRIDOR_CORNER = Lists.newArrayList(
			BiomeMakeover.ID("mansion/corridor/corner/corridor_corner_1")
	);

	public static List<Identifier> CORRIDOR_T = Lists.newArrayList(
			BiomeMakeover.ID("mansion/corridor/t/corridor_t_1")
	);

	public static List<Identifier> CORRIDOR_CROSS = Lists.newArrayList(
			BiomeMakeover.ID("mansion/corridor/cross/corridor_cross_1")
	);

	public static List<Identifier> ROOMS = Lists.newArrayList(
			BiomeMakeover.ID("mansion/room/room_1")
	);

	public static List<Identifier> STAIR_UP = Lists.newArrayList(
			BiomeMakeover.ID("mansion/stairs/up/stairs_up")
	);

	public static List<Identifier> STAIR_DOWN = Lists.newArrayList(
			BiomeMakeover.ID("mansion/stairs/down/stairs_down")
	);

	public static List<Identifier> INNER_WALL = Lists.newArrayList(
			BiomeMakeover.ID("mansion/wall/inner/wall_1")
	);

	public static Identifier getInnerWall(Random random)
	{
		return INNER_WALL.get(random.nextInt(INNER_WALL.size()));
	}
}
