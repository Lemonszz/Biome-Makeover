package party.lemons.biomemakeover.level.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import party.lemons.biomemakeover.level.generate.IgnoreAirProcessor;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.Random;

public class SurfaceFossilFeature extends Feature<NoneFeatureConfiguration> {
    private static final ResourceLocation[] FOSSILS = new ResourceLocation[]{new ResourceLocation("fossil/spine_1"), new ResourceLocation("fossil/spine_2"), new ResourceLocation("fossil/spine_3"), new ResourceLocation("fossil/spine_4"), new ResourceLocation("nether_fossils/fossil_1"), new ResourceLocation("nether_fossils/fossil_2"), new ResourceLocation("nether_fossils/fossil_3"), new ResourceLocation("nether_fossils/fossil_4"), new ResourceLocation("nether_fossils/fossil_5"), new ResourceLocation("nether_fossils/fossil_6"), new ResourceLocation("nether_fossils/fossil_7"), new ResourceLocation("nether_fossils/fossil_8"), new ResourceLocation("nether_fossils/fossil_9"), new ResourceLocation("nether_fossils/fossil_10"), new ResourceLocation("nether_fossils/fossil_11"), new ResourceLocation("nether_fossils/fossil_12"), new ResourceLocation("nether_fossils/fossil_13"), new ResourceLocation("nether_fossils/fossil_14")};

    public SurfaceFossilFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> ctx) {
        RandomSource random = ctx.random();
        WorldGenLevel worldGenLevel = ctx.level();
        BlockPos blockPos = ctx.origin();
        Rotation rotation = Rotation.getRandom(random);
        int fossilIndex = random.nextInt(FOSSILS.length);

        StructureTemplateManager structureManager = worldGenLevel.getLevel().getServer().getStructureManager();
        StructureTemplate structureTemplate = structureManager.getOrCreate(FOSSILS[fossilIndex]);

        ChunkPos chunkPos = new ChunkPos(blockPos);
        BoundingBox boundingBox = new BoundingBox(chunkPos.getMinBlockX() - 16, worldGenLevel.getMinBuildHeight(), chunkPos.getMinBlockZ() - 16, chunkPos.getMaxBlockX() + 16, worldGenLevel.getMaxBuildHeight(), chunkPos.getMaxBlockZ() + 16);
        StructurePlaceSettings structurePlaceSettings = new StructurePlaceSettings().addProcessor(IgnoreAirProcessor.INSTANCE).setRotation(rotation).setBoundingBox(boundingBox).setRandom(random);
        Vec3i size = structureTemplate.getSize(rotation);

        BlockPos position = blockPos.offset(-size.getX() / 2, 0, -size.getZ() / 2);
        int yLevel = blockPos.getY();
        for(int xx = 0; xx < size.getX(); xx++)
        {
            for(int zz = 0; zz < size.getZ(); ++zz)
            {
                yLevel = Math.min(yLevel, worldGenLevel.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, position.getX() + xx, position.getZ() + zz));
            }
        }
        if(fossilIndex < 4) yLevel -= RandomUtil.randomRange(1, Math.max(2, size.getY() - 2));

        BlockPos placePotion = structureTemplate.getZeroPositionWithTransform(position.atY(yLevel), Mirror.NONE, rotation);

        structureTemplate.placeInWorld(worldGenLevel, placePotion, placePotion, structurePlaceSettings, random, 4);

        return true;
    }
}
