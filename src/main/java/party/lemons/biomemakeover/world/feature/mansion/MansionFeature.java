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
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.init.BMStructures;
import party.lemons.biomemakeover.util.Grid;
import party.lemons.biomemakeover.world.feature.mansion.room.MansionRoom;

import java.util.*;
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

	@Override
	protected boolean shouldStartAt(ChunkGenerator chunkGenerator, BiomeSource biomeSource, long l, ChunkRandom chunkRandom, int i, int j, Biome biome, ChunkPos chunkPos, DefaultFeatureConfig defaultFeatureConfig)
	{
		Set<Biome> set = biomeSource.getBiomesInArea(i * 16 + 9, chunkGenerator.getSeaLevel(), j * 16 + 9, 32);
		return set.size() <= 2;
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

			int x = chunkX * 16;
			int z = chunkZ * 16;
			BlockPos pos = new BlockPos(x, chunkGenerator.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG), z);
			layout.generateLayout(random, pos.getY());

			Grid<MansionRoom> roomGrid = layout.getLayout();

			//Sort rooms. Roofs are created first to make generation not bad
			Collection<MansionRoom> sortedRooms = roomGrid.getEntries();
			sortedRooms = sortedRooms.stream().sorted(Comparator.comparingInt(MansionRoom::getSortValue)).collect(Collectors.toList());

			sortedRooms.forEach(rm->
			{
				//Room Generate Position
				int xx = pos.getX() + (rm.getPosition().getX() * 12);
				int yy = pos.getY() + (rm.getPosition().getY() * 7);
				int zz = pos.getZ() + (rm.getPosition().getZ() * 12);

				//Rotate/Offset Room to the correct position
				BlockPos offsetPos = new BlockPos(xx, yy, zz);
				BlockRotation rotation = rm.getRotation(random);
				offsetPos = rm.getOffsetForRotation(offsetPos, rotation);

				//Pieces on the ground will modify terrain to fit.
				boolean ground = rm.getPosition().getY() == 0;

				//Add room
				children.add(new Piece(manager, rm.getTemplate(random), offsetPos, rotation, ground, rm.getRoomType() == RoomType.TOWER_MID || rm.getRoomType() == RoomType.TOWER_TOP));

				//Create walls
				BlockPos wallPos = new BlockPos(xx, yy, zz);
				rm.addWalls(random, wallPos, manager, roomGrid, children);
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
		private final boolean ground;
		private final boolean isWall;

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
			BlockIgnoreStructureProcessor processor = isWall ? BlockIgnoreStructureProcessor.IGNORE_AIR_AND_STRUCTURE_BLOCKS : BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS;

			Structure structure = structureManager.getStructureOrBlank(this.template);
			StructurePlacementData structurePlacementData = (new StructurePlacementData()).setMirror(BlockMirror.NONE).setRotation(this.rotation);
			structurePlacementData.addProcessor(processor);

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
			BiomeMakeover.ID("mansion/corridor/straight/corridor_straight_1"),
			BiomeMakeover.ID("mansion/corridor/straight/corridor_straight_2"),
			BiomeMakeover.ID("mansion/corridor/straight/corridor_straight_3"),
			BiomeMakeover.ID("mansion/corridor/straight/corridor_straight_4"),
			BiomeMakeover.ID("mansion/corridor/straight/corridor_straight_5"),
			BiomeMakeover.ID("mansion/corridor/straight/corridor_straight_6"),
			BiomeMakeover.ID("mansion/corridor/straight/corridor_straight_7"),
			BiomeMakeover.ID("mansion/corridor/straight/corridor_straight_8")
	);

	public static List<Identifier> CORRIDOR_CORNER = Lists.newArrayList(
			BiomeMakeover.ID("mansion/corridor/corner/corridor_corner_1")
	);

	public static List<Identifier> CORRIDOR_T = Lists.newArrayList(
			BiomeMakeover.ID("mansion/corridor/t/corridor_t_1")
	);

	public static List<Identifier> CORRIDOR_CROSS = Lists.newArrayList(
			BiomeMakeover.ID("mansion/corridor/cross/corridor_cross_1"),
			BiomeMakeover.ID("mansion/corridor/cross/corridor_cross_2"),
			BiomeMakeover.ID("mansion/corridor/cross/corridor_cross_3"),
			BiomeMakeover.ID("mansion/corridor/cross/corridor_cross_4")
	);

	public static List<Identifier> ROOMS = Lists.newArrayList(BiomeMakeover.ID("mansion/room/room_1"));

	public static List<Identifier> STAIR_UP = Lists.newArrayList(BiomeMakeover.ID("mansion/stairs/up/stairs_up"));

	public static List<Identifier> STAIR_DOWN = Lists.newArrayList(BiomeMakeover.ID("mansion/stairs/down/stairs_down"));

	public static List<Identifier> INNER_WALL = Lists.newArrayList(
			BiomeMakeover.ID("mansion/wall/inner/wall_1"),
			BiomeMakeover.ID("mansion/wall/inner/wall_2"),
			BiomeMakeover.ID("mansion/wall/inner/wall_3"),
			BiomeMakeover.ID("mansion/wall/inner/wall_4"),
			BiomeMakeover.ID("mansion/wall/inner/wall_5"),
			BiomeMakeover.ID("mansion/wall/inner/wall_6"),
			BiomeMakeover.ID("mansion/wall/inner/wall_7"),
			BiomeMakeover.ID("mansion/wall/inner/wall_8"),
			BiomeMakeover.ID("mansion/wall/inner/wall_9")
	);

	public static List<Identifier> FLAT_WALL = Lists.newArrayList(
			BiomeMakeover.ID("mansion/wall/flat/wall_flat_1")
	);

	public static List<Identifier> OUTER_WALL_BASE = Lists.newArrayList(BiomeMakeover.ID("mansion/wall/outer/base/wall_outer_base_1"));
	public static List<Identifier> OUTER_WALL = Lists.newArrayList(BiomeMakeover.ID("mansion/wall/outer/wall_outer_1"));

	public static List<Identifier> OUTER_WINDOW = Lists.newArrayList(BiomeMakeover.ID("mansion/wall/outer/window/wall_window_1"));

	public static List<Identifier> GARDEN = Lists.newArrayList(BiomeMakeover.ID("mansion/garden/garden_1"));

	public static List<Identifier> TOWER_BASE = Lists.newArrayList(BiomeMakeover.ID("mansion/tower/base/tower_base_1"));
	public static List<Identifier> TOWER_MID = Lists.newArrayList(BiomeMakeover.ID("mansion/tower/mid/tower_middle_1"));
	public static List<Identifier> TOWER_TOP = Lists.newArrayList(BiomeMakeover.ID("mansion/tower/top/tower_top_1"));

	public static List<Identifier> ROOF_0 = Lists.newArrayList(BiomeMakeover.ID("mansion/roof/roof_0_1"));

	public static List<Identifier> ROOF_1 = Lists.newArrayList(BiomeMakeover.ID("mansion/roof/roof_1_1"));
	public static List<Identifier> ROOF_2 = Lists.newArrayList(BiomeMakeover.ID("mansion/roof/roof_2_1"));
	public static List<Identifier> ROOF_2_STRAIGHT = Lists.newArrayList(BiomeMakeover.ID("mansion/roof/roof_2_straight_1"));

	public static List<Identifier> ROOF_3 = Lists.newArrayList(BiomeMakeover.ID("mansion/roof/roof_3_1"));

	public static List<Identifier> ROOF_4 = Lists.newArrayList(BiomeMakeover.ID("mansion/roof/roof_4_1"));

	public static List<Identifier> ROOF_SPLIT = Lists.newArrayList(BiomeMakeover.ID("mansion/roof/roof_split_1"));

	public static List<Identifier> DUNGEON_DOOR = Lists.newArrayList(BiomeMakeover.ID("mansion/dungeon/door_1"));
	public static List<Identifier> DUNGEON_WALL = Lists.newArrayList(BiomeMakeover.ID("mansion/dungeon/wall_1"));
	public static List<Identifier> DUNGEON_ROOM = Lists.newArrayList(BiomeMakeover.ID("mansion/dungeon/room_1"));
	public static List<Identifier> DUNGEON_STAIR_BOTTOM = Lists.newArrayList(BiomeMakeover.ID("mansion/dungeon/stair_bottom"));
	public static List<Identifier> DUNGEON_STAIR_MID = Lists.newArrayList(BiomeMakeover.ID("mansion/dungeon/stair_mid"));
	public static List<Identifier> DUNGEON_STAIR_TOP = Lists.newArrayList(BiomeMakeover.ID("mansion/dungeon/stair_top"));
	public static List<Identifier> BOSS_ROOM = Lists.newArrayList(BiomeMakeover.ID("mansion/boss_room"));

	public static List<Identifier> ENTRANCE = Lists.newArrayList(BiomeMakeover.ID("mansion/entrance/entrance_1"));

	public static final Identifier CORNER_FILLER = BiomeMakeover.ID("mansion/corner_filler");
}
