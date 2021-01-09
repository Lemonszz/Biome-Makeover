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
			BlockPos pos = new BlockPos(x, 90, z);

			Grid<MansionRoom> roomGrid = layout.getLayout();

			roomGrid.getEntries().forEach(rm->{
				BlockPos offsetPos = new BlockPos(12 * rm.getPosition().getX(), 12 * rm.getPosition().getY(), 12 * rm.getPosition().getZ());
				children.add(new Piece(manager, rm.getTemplate(random), offsetPos, rm.getRotation(random)));
			});
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
}
