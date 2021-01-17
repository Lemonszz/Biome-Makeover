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
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.init.BMStructures;
import party.lemons.biomemakeover.util.Grid;
import party.lemons.biomemakeover.world.feature.mansion.room.MansionRoom;
import party.lemons.biomemakeover.world.feature.mansion.room.RoofMansionRoom;

import java.util.Collection;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

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
			Collection<MansionRoom> sortedRooms = roomGrid.getEntries();
			sortedRooms = sortedRooms.stream().sorted((m1, m2)->{
				return m1.getRoomType() == RoomType.ROOF ? -1 : 0;
			}).collect(Collectors.toList());

			sortedRooms.forEach(rm->{
				int xx = pos.getX() + (rm.getPosition().getX() * 12);
				int yy = pos.getY() + (rm.getPosition().getY() * 7);
				int zz = pos.getZ() + (rm.getPosition().getZ() * 12);

				BlockPos offsetPos = new BlockPos(xx, yy, zz);
				BlockRotation rotation =  rm.getRotation(random);
				offsetPos = rm.getOffsetForRotation(offsetPos, rotation);
				boolean ground = rm.getPosition().getY() == 0;
				children.add(new Piece(manager, rm.getTemplate(random), offsetPos, rotation, ground, rm.getRoomType() == RoomType.TOWER_MID || rm.getRoomType() == RoomType.TOWER_TOP || rm instanceof RoofMansionRoom));
				BlockPos wallPos = new BlockPos(xx, yy, zz);

				//Wall
				if(rm.getRoomType().hasWalls())
				{
					if(rm.isConnected(Direction.NORTH))
						children.add(new Piece(manager, getInnerWall(rm, random), wallPos.offset(Direction.NORTH), BlockRotation.NONE, ground, true));
					else if(!roomGrid.contains(rm.getPosition().north()) || !roomGrid.get(rm.getPosition().north()).getRoomType().hasWalls())
						children.add(new Piece(manager, getOuterWall(rm, random), wallPos.offset(Direction.NORTH).offset(Direction.EAST, 11), BlockRotation.CLOCKWISE_180, ground, true));
					if(rm.isConnected(Direction.WEST))
						children.add(new Piece(manager, getInnerWall(rm, random), wallPos.offset(Direction.WEST), BlockRotation.CLOCKWISE_90, ground, true));
					else if(!roomGrid.contains(rm.getPosition().west())  || !roomGrid.get(rm.getPosition().west()).getRoomType().hasWalls())
						children.add(new Piece(manager, getOuterWall(rm, random), wallPos.offset(Direction.WEST).north(), BlockRotation.CLOCKWISE_90, ground, true));

					if(!roomGrid.contains(rm.getPosition().east())  || !roomGrid.get(rm.getPosition().east()).getRoomType().hasWalls())
						children.add(new Piece(manager, getOuterWall(rm, random), wallPos.offset(Direction.EAST, 11).south(11), BlockRotation.COUNTERCLOCKWISE_90, ground, true));
					if(!roomGrid.contains(rm.getPosition().south())  || !roomGrid.get(rm.getPosition().south()).getRoomType().hasWalls())
						children.add(new Piece(manager, getOuterWall(rm, random), wallPos.offset(Direction.SOUTH, 11).west(), BlockRotation.NONE, ground, true));
				}
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
		private boolean ground, isWall;

		public Piece(StructureManager structureManager, Identifier template, BlockPos pos, BlockRotation rotation, boolean needsGroundAdjustment, boolean isWall)
		{
			super(BMStructures.MANSION_PIECE, 0);
			this.template = template;
			this.pos = pos;
			this.rotation = rotation;
			this.ground = needsGroundAdjustment;
			this.isWall = isWall;
			this.initialize(structureManager);
		}

		public Piece(StructureManager manager, CompoundTag tag)
		{
			super(BMStructures.MANSION_PIECE, tag);
			this.template = new Identifier(tag.getString("Template"));
			this.rotation = BlockRotation.valueOf(tag.getString("Rot"));
			this.ground = tag.getBoolean("Ground");
			this.isWall = tag.getBoolean("Wall");

			this.initialize(manager);
		}

		private void initialize(StructureManager structureManager)
		{
			Structure structure = structureManager.getStructureOrBlank(this.template);
			StructurePlacementData structurePlacementData = (new StructurePlacementData()).setMirror(BlockMirror.NONE).setRotation(this.rotation).addProcessor(isWall ? BlockIgnoreStructureProcessor.IGNORE_AIR_AND_STRUCTURE_BLOCKS : BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
			this.setStructureData(structure, this.pos, structurePlacementData);
		}

		protected void toNbt(CompoundTag tag)
		{
			super.toNbt(tag);
			tag.putString("Template", this.template.toString());
			tag.putString("Rot", this.rotation.name());
			tag.putBoolean("Ground", ground);
			tag.putBoolean("Wall", isWall);
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

		public boolean isGround()
		{
			return ground;
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

	public static List<Identifier> OUTER_WALL_BASE = Lists.newArrayList(
			BiomeMakeover.ID("mansion/wall/outer/base/wall_outer_base_1")
	);
	public static List<Identifier> OUTER_WALL = Lists.newArrayList(
			BiomeMakeover.ID("mansion/wall/outer/wall_outer_1")
	);

	public static List<Identifier> GARDEN = Lists.newArrayList(
			BiomeMakeover.ID("mansion/garden/garden_1")
	);

	public static List<Identifier> TOWER_BASE = Lists.newArrayList(
			BiomeMakeover.ID("mansion/tower/base/tower_base_1")
	);
	public static List<Identifier> TOWER_MID = Lists.newArrayList(
			BiomeMakeover.ID("mansion/tower/mid/tower_middle_1")
	);
	public static List<Identifier> TOWER_TOP = Lists.newArrayList(
			BiomeMakeover.ID("mansion/tower/top/tower_top_1")
	);
	public static List<Identifier> ROOF_1 = Lists.newArrayList(
			BiomeMakeover.ID("mansion/roof/roof_1_1")
	);
	public static List<Identifier> ROOF_2 = Lists.newArrayList(
			BiomeMakeover.ID("mansion/empty")
		//	BiomeMakeover.ID("mansion/roof/roof_2_1")
	);
	public static List<Identifier> ROOF_3 = Lists.newArrayList(
			BiomeMakeover.ID("mansion/empty")
	);
	public static List<Identifier> ROOF_4 = Lists.newArrayList(
			BiomeMakeover.ID("mansion/empty")
	);




	public static Identifier getInnerWall(MansionRoom room, Random random)
	{
		return INNER_WALL.get(random.nextInt(INNER_WALL.size()));
	}
	public static Identifier getOuterWall(MansionRoom room, Random random)
	{
		if(room.getPosition().getY() > 0)
		{
			return OUTER_WALL.get(random.nextInt(OUTER_WALL.size()));
		}
		else
		{
			return OUTER_WALL_BASE.get(random.nextInt(OUTER_WALL_BASE.size()));
		}
	}
}
