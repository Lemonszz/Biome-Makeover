package party.lemons.biomemakeover.level.feature;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.NoiseEffect;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.StructurePieceType;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.*;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.minecraft.world.level.material.FluidState;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.init.BMWorldGen;
import party.lemons.biomemakeover.util.RandomUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SunkenRuinFeature extends StructureFeature<SunkenRuinFeature.SunkenRuinFeatureConfig>
{
    private static final ResourceLocation[] LARGE_PIECES = new ResourceLocation[]{BiomeMakeover.ID("sunken_ruins/sunken_1"), BiomeMakeover.ID("sunken_ruins/sunken_2"), BiomeMakeover.ID("sunken_ruins/sunken_3")};
    private static final ResourceLocation[] SMALL_PIECES = new ResourceLocation[]{BiomeMakeover.ID("sunken_ruins/sunken_small_1"), BiomeMakeover.ID("sunken_ruins/sunken_small_2"), BiomeMakeover.ID("sunken_ruins/sunken_small_3"), BiomeMakeover.ID("sunken_ruins/sunken_small_4"), BiomeMakeover.ID("sunken_ruins/sunken_small_5"), BiomeMakeover.ID("sunken_ruins/sunken_small_6")};


    private static final ResourceLocation LOOT = BiomeMakeover.ID("sunken_ruin");

    public SunkenRuinFeature(Codec<SunkenRuinFeatureConfig> codec)
    {
        super(codec, PieceGeneratorSupplier.simple(PieceGeneratorSupplier.checkForBiomeOnTop(Heightmap.Types.WORLD_SURFACE_WG), SunkenRuinFeature::generatePieces));
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.LOCAL_MODIFICATIONS;
    }

    private static <C extends FeatureConfiguration> void generatePieces(StructurePiecesBuilder builder, PieceGenerator.Context<SunkenRuinFeatureConfig> ctx)
    {
        BlockPos blockPos = new BlockPos(ctx.chunkPos().getMinBlockX(), 90, ctx.chunkPos().getMinBlockZ());
        Rotation rotation = Rotation.getRandom(ctx.random());
        addPieces(ctx.structureManager(), blockPos, rotation, builder, ctx.random(), ctx.config());
    }

    public static void addPieces(StructureManager manager, BlockPos blockPos, Rotation rotation, StructurePieceAccessor pieceAccessor, Random random, SunkenRuinFeatureConfig cfg) {
        boolean isLarge = random.nextFloat() <= cfg.largeProbability;
        float f = isLarge ? 0.9f : 0.8f;

        addPiece(manager, blockPos, rotation, pieceAccessor, random, cfg, isLarge, f);
        if (isLarge && random.nextFloat() <= cfg.clusterProbability) {
            addClusterRuins(manager, random, rotation, blockPos, cfg, pieceAccessor);
        }
    }

    private static void addPiece(StructureManager structureManager, BlockPos blockPos, Rotation rotation, StructurePieceAccessor structurePieceAccessor, Random random, SunkenRuinFeatureConfig oceanRuinConfiguration, boolean bl, float f) {
        ResourceLocation resourceLocation = bl ? LARGE_PIECES[random.nextInt(LARGE_PIECES.length)] : SMALL_PIECES[random.nextInt(SMALL_PIECES.length)];
        structurePieceAccessor.addPiece(new SunkenRuinPiece(structureManager, resourceLocation, blockPos, rotation, 1, bl));
    }

    private static void addClusterRuins(StructureManager structureManager, Random random, Rotation rotation, BlockPos blockPos, SunkenRuinFeatureConfig oceanRuinConfiguration, StructurePieceAccessor structurePieceAccessor) {
        BlockPos blockPos2 = new BlockPos(blockPos.getX(), 90, blockPos.getZ());
        BlockPos blockPos3 = StructureTemplate.transform(new BlockPos(15, 0, 15), Mirror.NONE, rotation, BlockPos.ZERO).offset(blockPos2);
        BoundingBox boundingBox = BoundingBox.fromCorners(blockPos2, blockPos3);
        BlockPos blockPos4 = new BlockPos(Math.min(blockPos2.getX(), blockPos3.getX()), blockPos2.getY(), Math.min(blockPos2.getZ(), blockPos3.getZ()));
        List<BlockPos> list = allPositions(random, blockPos4);
        int i = Mth.nextInt(random, 4, 8);
        for (int j = 0; j < i; ++j) {
            Rotation rotation2;
            BlockPos blockPos6;
            int k;
            BlockPos blockPos5;
            BoundingBox boundingBox2;
            if (list.isEmpty() || (boundingBox2 = BoundingBox.fromCorners(blockPos5 = list.remove(k = random.nextInt(list.size())), blockPos6 = StructureTemplate.transform(new BlockPos(5, 0, 6), Mirror.NONE, rotation2 = Rotation.getRandom(random), BlockPos.ZERO).offset(blockPos5))).intersects(boundingBox)) continue;
            addPiece(structureManager, blockPos5, rotation2, structurePieceAccessor, random, oceanRuinConfiguration, false, 1);
        }
    }

    private static List<BlockPos> allPositions(Random random, BlockPos blockPos) {
        ArrayList<BlockPos> list = Lists.newArrayList();
        list.add(blockPos.offset(-16 + Mth.nextInt(random, 1, 8), 0, 16 + Mth.nextInt(random, 1, 7)));
        list.add(blockPos.offset(-16 + Mth.nextInt(random, 1, 8), 0, Mth.nextInt(random, 1, 7)));
        list.add(blockPos.offset(-16 + Mth.nextInt(random, 1, 8), 0, -16 + Mth.nextInt(random, 4, 8)));
        list.add(blockPos.offset(Mth.nextInt(random, 1, 7), 0, 16 + Mth.nextInt(random, 1, 7)));
        list.add(blockPos.offset(Mth.nextInt(random, 1, 7), 0, -16 + Mth.nextInt(random, 4, 6)));
        list.add(blockPos.offset(16 + Mth.nextInt(random, 1, 7), 0, 16 + Mth.nextInt(random, 3, 8)));
        list.add(blockPos.offset(16 + Mth.nextInt(random, 1, 7), 0, Mth.nextInt(random, 1, 7)));
        list.add(blockPos.offset(16 + Mth.nextInt(random, 1, 7), 0, -16 + Mth.nextInt(random, 4, 8)));
        return list;
    }


    public static class SunkenRuinFeatureConfig implements FeatureConfiguration
    {
        public static final Codec<SunkenRuinFeatureConfig> CODEC = RecordCodecBuilder.create((instance)->
                instance.group(Codec.floatRange(0.0F, 1.0F).fieldOf("large_probability").forGetter((cfg)->cfg.largeProbability), Codec.floatRange(0.0F, 1.0F).fieldOf("cluster_probability").forGetter((cfg)->cfg.clusterProbability)).apply(instance, SunkenRuinFeatureConfig::new));

        public final float largeProbability;
        public final float clusterProbability;

        public SunkenRuinFeatureConfig(float largeProbability, float clusterProbability)
        {
            this.largeProbability = largeProbability;
            this.clusterProbability = clusterProbability;
        }
    }

    public static class SunkenRuinPiece
            extends TemplateStructurePiece {
        private final float integrity;
        private final boolean isLarge;

        public SunkenRuinPiece(StructureManager structureManager, ResourceLocation resourceLocation, BlockPos blockPos, Rotation rotation, float f, boolean bl) {
            super(BMWorldGen.Swamp.SUNKEN_RUIN_PIECE, 0, structureManager, resourceLocation, resourceLocation.toString(), makeSettings(rotation), blockPos);
            this.integrity = f;
            this.isLarge = bl;
        }

        public SunkenRuinPiece(StructureManager structureManager, CompoundTag compoundTag) {
            super(BMWorldGen.Swamp.SUNKEN_RUIN_PIECE, compoundTag, structureManager, resourceLocation -> makeSettings(Rotation.valueOf(compoundTag.getString("Rot"))));
            this.integrity = compoundTag.getFloat("Integrity");
            this.isLarge = compoundTag.getBoolean("IsLarge");
        }

        public SunkenRuinPiece(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag compoundTag)
        {
            super(BMWorldGen.Swamp.SUNKEN_RUIN_PIECE, compoundTag, structurePieceSerializationContext.structureManager(), resourceLocation -> makeSettings(Rotation.valueOf(compoundTag.getString("Rot"))));
            this.integrity = compoundTag.getFloat("Integrity");
            this.isLarge = compoundTag.getBoolean("IsLarge");
        }

        private static StructurePlaceSettings makeSettings(Rotation rotation) {
            return new StructurePlaceSettings().setRotation(rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
        }

        @Override
        public NoiseEffect getNoiseEffect() {
            return NoiseEffect.BURY;
        }

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag compoundTag) {
            super.addAdditionalSaveData(structurePieceSerializationContext, compoundTag);
            compoundTag.putString("Rot", this.placeSettings.getRotation().name());
            compoundTag.putFloat("Integrity", this.integrity);
            compoundTag.putBoolean("IsLarge", this.isLarge);
        }

        @Override
        protected void handleDataMarker(String metadata, BlockPos pos, ServerLevelAccessor level, Random random, BoundingBox boundingBox) {
            if("chest".equals(metadata))
            {
                level.setBlock(pos, Blocks.CHEST.defaultBlockState().setValue(ChestBlock.WATERLOGGED, level.getFluidState(pos).is(FluidTags.WATER)), 2);
                BlockEntity blockEntity = level.getBlockEntity(pos);
                if(blockEntity instanceof ChestBlockEntity)
                {
                    ((ChestBlockEntity) blockEntity).setLootTable(LOOT, random.nextLong());
                }
            }
            else if("witch".equals(metadata) && random.nextBoolean())
            {
                if(level.getBlockState(pos.above()).isAir())
                {
                    Witch witch = EntityType.WITCH.create(level.getLevel());
                    witch.setPersistenceRequired();
                    witch.moveTo(pos, 0.0F, 0.0F);
                    witch.finalizeSpawn(level, level.getCurrentDifficultyAt(pos), MobSpawnType.STRUCTURE, null, null);
                    level.addFreshEntityWithPassengers(witch);
                    if(pos.getY() >= level.getSeaLevel())
                    {
                        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
                    }else
                    {
                        level.setBlock(pos, Blocks.WATER.defaultBlockState(), 2);
                    }
                }
            }
        }

        @Override
        public void postProcess(WorldGenLevel worldGenLevel, StructureFeatureManager structureFeatureManager, ChunkGenerator chunkGenerator, Random random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos blockPos) {
            this.placeSettings.clearProcessors().addProcessor(new BlockRotProcessor(this.integrity)).addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
            int i = worldGenLevel.getHeight(Heightmap.Types.OCEAN_FLOOR_WG, this.templatePosition.getX(), this.templatePosition.getZ()) - RandomUtil.randomRange(1, 4);
            this.templatePosition = new BlockPos(this.templatePosition.getX(), i, this.templatePosition.getZ());
            BlockPos blockPos2 = StructureTemplate.transform(new BlockPos(this.template.getSize().getX() - 1, 0, this.template.getSize().getZ() - 1), Mirror.NONE, this.placeSettings.getRotation(), BlockPos.ZERO).offset(this.templatePosition);
            this.templatePosition = new BlockPos(this.templatePosition.getX(), this.getHeight(this.templatePosition, worldGenLevel, blockPos2), this.templatePosition.getZ());
            super.postProcess(worldGenLevel, structureFeatureManager, chunkGenerator, random, boundingBox, chunkPos, blockPos);
        }

        private int getHeight(BlockPos blockPos, BlockGetter blockGetter, BlockPos blockPos2) {
            int i = blockPos.getY();
            int j = 512;
            int k = i - 1;
            int l = 0;
            for (BlockPos blockPos3 : BlockPos.betweenClosed(blockPos, blockPos2)) {
                int m = blockPos3.getX();
                int n = blockPos3.getZ();
                int o = blockPos.getY() - 1;
                BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(m, o, n);
                BlockState blockState = blockGetter.getBlockState(mutableBlockPos);
                FluidState fluidState = blockGetter.getFluidState(mutableBlockPos);
                while ((blockState.isAir() || fluidState.is(FluidTags.WATER) || blockState.is(BlockTags.ICE)) && o > blockGetter.getMinBuildHeight() + 1) {
                    mutableBlockPos.set(m, --o, n);
                    blockState = blockGetter.getBlockState(mutableBlockPos);
                    fluidState = blockGetter.getFluidState(mutableBlockPos);
                }
                j = Math.min(j, o);
                if (o >= k - 2) continue;
                ++l;
            }
            int p = Math.abs(blockPos.getX() - blockPos2.getX());
            if (k - j > 2 && l > p - 2) {
                i = j + 1;
            }
            return i;
        }
    }
}
