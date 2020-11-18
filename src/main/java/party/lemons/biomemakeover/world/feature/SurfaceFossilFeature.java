package party.lemons.biomemakeover.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.structure.Structure;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.BlockIgnoreStructureProcessor;
import net.minecraft.structure.processor.BlockRotStructureProcessor;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.FossilFeature;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.Random;

public class SurfaceFossilFeature extends FossilFeature
{
	private static final Identifier[] FOSSILS = new Identifier[]{
			new Identifier("fossil/spine_1"),
			new Identifier("fossil/spine_2"),
			new Identifier("fossil/spine_3"),
			new Identifier("fossil/spine_4"),
			new Identifier("nether_fossils/fossil_1"),
			new Identifier("nether_fossils/fossil_2"),
			new Identifier("nether_fossils/fossil_3"),
			new Identifier("nether_fossils/fossil_4"),
			new Identifier("nether_fossils/fossil_5"),
			new Identifier("nether_fossils/fossil_6"),
			new Identifier("nether_fossils/fossil_7"),
			new Identifier("nether_fossils/fossil_8"),
			new Identifier("nether_fossils/fossil_9"),
			new Identifier("nether_fossils/fossil_10"),
			new Identifier("nether_fossils/fossil_11"),
			new Identifier("nether_fossils/fossil_12"),
			new Identifier("nether_fossils/fossil_13"),
			new Identifier("nether_fossils/fossil_14")
	};

	public SurfaceFossilFeature(Codec<DefaultFeatureConfig> codec)
	{
		super(codec);
	}

	@Override
	public boolean generate(StructureWorldAccess structureWorldAccess, ChunkGenerator chunkGenerator, Random random, BlockPos blockPos, DefaultFeatureConfig defaultFeatureConfig)
	{
		BlockRotation rot = BlockRotation.random(random);
		int fossilIndex = random.nextInt(FOSSILS.length);

		StructureManager structureManager = structureWorldAccess.toServerWorld().getServer().getStructureManager();
		Structure structure = structureManager.getStructureOrBlank(FOSSILS[fossilIndex]);

		ChunkPos chunkPos = new ChunkPos(blockPos);
		BlockBox blockBox = new BlockBox(chunkPos.getStartX(), 0, chunkPos.getStartZ(), chunkPos.getEndX(), 256, chunkPos.getEndZ());
		StructurePlacementData placeData = (new StructurePlacementData()).setRotation(rot).setBoundingBox(blockBox).setRandom(random).addProcessor(BlockIgnoreStructureProcessor.IGNORE_STRUCTURE_BLOCKS);
		BlockPos size = structure.getRotatedSize(rot);
		int x = random.nextInt(16 - size.getX());
		int z = random.nextInt(16 - size.getZ());

		int y = 256;
		for(int xx = 0; xx < size.getX(); xx++) {
			for(int zz = 0; zz < size.getZ(); ++zz) {
				y = Math.min(y, structureWorldAccess.getTopY(Heightmap.Type.OCEAN_FLOOR_WG, blockPos.getX() + xx + x, blockPos.getZ() + zz + z));
			}
		}
		if(fossilIndex < 4)
			y -= RandomUtil.randomRange(1, Math.max(2, size.getY() - 2));

		BlockPos generatePos = structure.offsetByTransformedSize(blockPos.add(x, y, z), BlockMirror.NONE, rot);
		BlockRotStructureProcessor process = new BlockRotStructureProcessor(1F);
		placeData.clearProcessors().addProcessor(process);
		structure.place(structureWorldAccess, generatePos, generatePos, placeData, random, 4);
		return true;
	}
}
