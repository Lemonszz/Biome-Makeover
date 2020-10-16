package party.lemons.biomemakeover.world.feature;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.*;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.structure.processor.StructureProcessorLists;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.Heightmap;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.JigsawFeature;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.init.BMStructures;

import java.util.List;
import java.util.Random;

public class GhostTownFeature extends JigsawFeature
{
	private static final StructurePool POOL = StructurePools.register(new StructurePool(BiomeMakeover.ID("ghost_town/centers"), new Identifier("empty"), ImmutableList.of(Pair.of(StructurePoolElement.method_30426("biomemakeover:ghost_town/centers/crossroads", StructureProcessorLists.STREET_PLAINS), 50)), StructurePool.Projection.RIGID));

	public static final StructurePoolFeatureConfig CONFIG = new StructurePoolFeatureConfig(()->POOL, 6);

	public GhostTownFeature(Codec<StructurePoolFeatureConfig> codec)
	{
		super(codec, 0, true, true);
	}

	private static final Identifier CROSSROADS = BiomeMakeover.ID("ghost_town/crossroads");

	public static void addPieces(StructureManager manager, BlockPos pos, BlockRotation rotation, List<StructurePiece> pieces)
	{
		pieces.add(new GhostTownPiece(manager, pos, CROSSROADS, rotation));
	}

	public static class GhostTownPiece extends SimpleStructurePiece
	{
		private final BlockRotation rotation;
		private final Identifier template;

		public GhostTownPiece(StructureManager structureManager, CompoundTag compoundTag)
		{
			super(BMStructures.GHOST_TOWN_PIECE, compoundTag);
			this.template = new Identifier(compoundTag.getString("Template"));
			this.rotation = BlockRotation.valueOf(compoundTag.getString("Rot"));

			this.initializeStructureData(structureManager);
		}

        public GhostTownPiece(StructureManager structureManager, BlockPos pos, Identifier template, BlockRotation rotation)
		{
			super(BMStructures.GHOST_TOWN_PIECE, 0);

			this.pos = pos;
			this.rotation = rotation;
			this.template = template;

			this.initializeStructureData(structureManager);
		}

		private void initializeStructureData(StructureManager structureManager)
		{
			Structure structure = structureManager.getStructureOrBlank(this.template);
			StructurePlacementData placementData = (new StructurePlacementData())
					.setRotation(this.rotation)
					.setMirror(BlockMirror.NONE)
					.addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
			this.setStructureData(structure, this.pos, placementData);
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

	private static class Start extends StructureStart<DefaultFeatureConfig>
	{
		public Start(StructureFeature<DefaultFeatureConfig> feature, int chunkX, int chunkZ, BlockBox box, int references, long seed)
		{
			super(feature, chunkX, chunkZ, box, references, seed);
		}

		@Override
		public void init(DynamicRegistryManager registryManager, ChunkGenerator chunkGenerator, StructureManager manager, int chunkX, int chunkZ, Biome biome, DefaultFeatureConfig config)
		{
			int x = chunkX * 16;
			int z = chunkZ * 16;
			int y = chunkGenerator.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG);

			BlockPos pos = new BlockPos(x, y, z);
			BlockRotation rotation = BlockRotation.random(random);

			addPieces(manager, pos, rotation, children);
			setBoundingBoxFromChildren();

		}
	}
}
