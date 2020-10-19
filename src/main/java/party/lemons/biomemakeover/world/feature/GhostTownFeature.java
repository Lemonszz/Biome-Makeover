package party.lemons.biomemakeover.world.feature;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.*;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.structure.processor.*;
import net.minecraft.structure.rule.AlwaysTrueRuleTest;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.structure.rule.RandomBlockMatchRuleTest;
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
import party.lemons.biomemakeover.util.JigsawHelper;

import java.util.List;
import java.util.Random;

public class GhostTownFeature extends JigsawFeature
{
	public static final StructureProcessorList ROADS_PROCESSOR = JigsawHelper.register(
			new StructureProcessorList(ImmutableList.of(new RuleStructureProcessor(ImmutableList.of(new StructureProcessorRule(new BlockMatchRuleTest(Blocks.GRASS_PATH), new BlockMatchRuleTest(Blocks.WATER), Blocks.OAK_PLANKS.getDefaultState()), new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.GRASS_PATH, 0.1F), AlwaysTrueRuleTest.INSTANCE, Blocks.RED_SAND.getDefaultState()), new StructureProcessorRule(new BlockMatchRuleTest(Blocks.RED_SAND), new BlockMatchRuleTest(Blocks.WATER), Blocks.WATER.getDefaultState()), new StructureProcessorRule(new BlockMatchRuleTest(Blocks.DIRT), new BlockMatchRuleTest(Blocks.WATER), Blocks.WATER.getDefaultState())))))
			, "roads_ghosttown");

	static
	{
		StructurePools.register(new StructurePool(BiomeMakeover.ID("ghosttown/roads"), new Identifier("village/plains/terminators"), ImmutableList.of(
				Pair.of(StructurePoolElement.method_30426("biomemakeover:ghosttown/roads/street_01", ROADS_PROCESSOR), 10),
				Pair.of(StructurePoolElement.method_30426("biomemakeover:ghosttown/roads/street_02", ROADS_PROCESSOR), 20),
				Pair.of(StructurePoolElement.method_30426("biomemakeover:ghosttown/roads/street_03", ROADS_PROCESSOR), 5)
		), StructurePool.Projection.RIGID));
		StructurePools.register(new StructurePool(BiomeMakeover.ID("ghosttown/buildings"), new Identifier("village/plains/terminators"), ImmutableList.of(Pair.of(StructurePoolElement.method_30426("biomemakeover:ghosttown/houses/house_small_01", ROADS_PROCESSOR), 2), Pair.of(StructurePoolElement.method_30426("biomemakeover:ghosttown/houses/house_medium_01", ROADS_PROCESSOR), 2), Pair.of(StructurePoolElement.method_30426("biomemakeover:ghosttown/houses/house_large_01", ROADS_PROCESSOR), 2)), StructurePool.Projection.RIGID));
	}

	private static final StructurePool POOL = StructurePools.register(new StructurePool(BiomeMakeover.ID("ghosttown/centers"), new Identifier("empty"), ImmutableList.of(Pair.of(StructurePoolElement.method_30426("biomemakeover:ghosttown/centers/crossroads_01", ROADS_PROCESSOR), 1), Pair.of(StructurePoolElement.method_30426("biomemakeover:ghosttown/roads/street_01", ROADS_PROCESSOR), 1)), StructurePool.Projection.RIGID));


	public static final StructurePoolFeatureConfig CONFIG = new StructurePoolFeatureConfig(()->POOL, 2);

	public GhostTownFeature(Codec<StructurePoolFeatureConfig> codec)
	{
		super(codec, 0, true, true);
	}

	//private static final Identifier CROSSROADS = BiomeMakeover.ID("ghosttown/crossroads");

/*	public static void addPieces(StructureManager manager, BlockPos pos, BlockRotation rotation, List<StructurePiece> pieces)
	{
		pieces.add(new GhostTownPiece(manager, pos, CROSSROADS, rotation));
	}*/

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

	/*
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
	}*/
}
