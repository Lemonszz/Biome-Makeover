package party.lemons.biomemakeover.world.feature;

import com.mojang.serialization.Codec;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.Heightmap;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;
import party.lemons.biomemakeover.entity.MushroomVillagerEntity;
import party.lemons.biomemakeover.world.structure.MushroomHouseGenerator;

public class MushroomHouseFeature extends StructureFeature<DefaultFeatureConfig>
{
	public MushroomHouseFeature(Codec<DefaultFeatureConfig> codec)
	{
		super(codec);
	}

	@Override
	public StructureStartFactory<DefaultFeatureConfig> getStructureStartFactory()
	{
		return Start::new;
	}

	public static class Start extends StructureStart<DefaultFeatureConfig>
	{
		public Start(StructureFeature<DefaultFeatureConfig> feature, int chunkX, int chunkZ, BlockBox box, int references, long seed)
		{
			super(feature, chunkX, chunkZ, box, references, seed);
		}

		public void init(DynamicRegistryManager registryManager, ChunkGenerator chunkGenerator, StructureManager manager, int chunkX, int chunkZ, Biome biome, DefaultFeatureConfig config) {
			int x = chunkX * 16;
			int z = chunkZ * 16;
			int y = chunkGenerator.getHeight(x, z, Heightmap.Type.WORLD_SURFACE_WG);
			BlockPos pos = new BlockPos(x, y, z);
			BlockRotation rotation = BlockRotation.random(this.random);
			MushroomHouseGenerator.addPieces(manager, pos, rotation, this.children);

			this.setBoundingBoxFromChildren();
		}
	}
}
