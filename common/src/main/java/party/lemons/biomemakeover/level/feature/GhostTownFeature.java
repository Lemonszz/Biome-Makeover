package party.lemons.biomemakeover.level.feature;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.data.worldgen.ProcessorLists;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.JigsawFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import org.jetbrains.annotations.Nullable;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.Constants;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.init.BMEntities;
import party.lemons.biomemakeover.init.BMWorldGen;
import party.lemons.biomemakeover.util.RandomUtil;
import party.lemons.biomemakeover.util.registry.RegistryHelper;

import java.util.List;
import java.util.stream.Collectors;

public class GhostTownFeature extends JigsawFeature {

    public static final WeightedRandomList<MobSpawnSettings.SpawnerData> SPAWNS = WeightedRandomList.create(Lists.newArrayList(new MobSpawnSettings.SpawnerData(BMEntities.GHOST, 8, 1, 1)));

    public static final StructureProcessorList ROADS_PROCESSOR = Registry.register(BuiltinRegistries.PROCESSOR_LIST, BiomeMakeover.ID("roads_ghosttown"),
            new StructureProcessorList(
                            ImmutableList.of(
                                    new RuleProcessor(ImmutableList.of(new ProcessorRule(new BlockMatchTest(Blocks.DIRT_PATH), new BlockMatchTest(Blocks.WATER), Blocks.OAK_PLANKS.defaultBlockState()))),
                                    new RuleProcessor(ImmutableList.of(new ProcessorRule(new RandomBlockMatchTest(Blocks.DIRT_PATH, 0.3F), AlwaysTrueTest.INSTANCE, Blocks.RED_SAND.defaultBlockState()))),
                                    new RuleProcessor(ImmutableList.of(new ProcessorRule(new BlockMatchTest(Blocks.RED_SAND), new BlockMatchTest(Blocks.WATER), Blocks.WATER.defaultBlockState()))),
                                    new RuleProcessor(ImmutableList.of(new ProcessorRule(new BlockMatchTest(Blocks.DIRT), new BlockMatchTest(Blocks.WATER), Blocks.WATER.defaultBlockState())))
            )));

    public static final StructureProcessorList BUILDING_PROCESSOR = Registry.register(BuiltinRegistries.PROCESSOR_LIST, BiomeMakeover.ID("buildings_ghosttown"),new StructureProcessorList(
            ImmutableList.of(
                    new RuleProcessor(BMBlocks.BRICK_TO_TERRACOTTA.keySet().stream().map(b->new ProcessorRule(new RandomBlockMatchTest(b, 0.25F), AlwaysTrueTest.INSTANCE, BMBlocks.BRICK_TO_TERRACOTTA.get(b).defaultBlockState())).collect(Collectors.toList())))));

    private static final StructureTemplatePool POOL = Pools.register(
            new StructureTemplatePool(
                    BiomeMakeover.ID("ghosttown/centers"),
                    new ResourceLocation("empty"),
                    ImmutableList.of(
                            Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/roads/street_01", ROADS_PROCESSOR), 2),
                            Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/roads/street_02", ROADS_PROCESSOR), 3),
                            Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/roads/street_03", ROADS_PROCESSOR), 2),
                            Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/roads/street_04", ROADS_PROCESSOR), 2),
                            Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/roads/street_05", ROADS_PROCESSOR), 1),
                            Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/roads/street_06", ROADS_PROCESSOR), 2),
                            Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/roads/street_07", ROADS_PROCESSOR), 1)
                    ),
                    StructureTemplatePool.Projection.RIGID));

    public static final JigsawConfiguration CONFIG = new JigsawConfiguration(()->POOL, 3);

    public GhostTownFeature(Codec<JigsawConfiguration> codec) {
        super(codec, 0, true, true, context -> true);
    }

    @Override
    public GenerationStep.Decoration step() {
        return GenerationStep.Decoration.SURFACE_STRUCTURES;
    }

    public static final GhostTownFeature.GhostTownLootProcessor GHOST_TOWN_LOOT_PROCESSOR = new GhostTownLootProcessor();
    public static final Codec<GhostTownFeature.GhostTownLootProcessor> GHOST_TOWN_PROCESSOR_CODEC = Codec.unit(()-> GHOST_TOWN_LOOT_PROCESSOR);


    public static final StructureProcessorType<GhostTownFeature.GhostTownLootProcessor> GHOST_TOWN_PROCESSOR = ()->GHOST_TOWN_PROCESSOR_CODEC;



    public static void init()
    {

        RegistryHelper.register(Constants.MOD_ID, Registry.STRUCTURE_PROCESSOR, StructureProcessorType.class, BMWorldGen.Badlands.class);


        //Roads,
        Pools.register(new StructureTemplatePool(BiomeMakeover.ID("ghosttown/roads"), new ResourceLocation("village/plains/terminators"), ImmutableList.of(Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/roads/street_01", ROADS_PROCESSOR), 2), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/roads/street_02", ROADS_PROCESSOR), 5), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/roads/street_03", ROADS_PROCESSOR), 2), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/roads/street_04", ROADS_PROCESSOR), 2), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/roads/street_05", ROADS_PROCESSOR), 1), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/roads/street_06", ROADS_PROCESSOR), 2)), StructureTemplatePool.Projection.RIGID));

        //Buildings
        Pools.register(new StructureTemplatePool(BiomeMakeover.ID("ghosttown/buildings"), new ResourceLocation("village/plains/terminators"), ImmutableList.of(Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_small_01", BUILDING_PROCESSOR), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_small_02", BUILDING_PROCESSOR), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_small_03", BUILDING_PROCESSOR), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_small_04", BUILDING_PROCESSOR), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_small_05", BUILDING_PROCESSOR), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_small_06", BUILDING_PROCESSOR), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_small_07", BUILDING_PROCESSOR), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_small_08", BUILDING_PROCESSOR), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_small_09", BUILDING_PROCESSOR), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_small_10", BUILDING_PROCESSOR), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_small_11", BUILDING_PROCESSOR), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_small_12", BUILDING_PROCESSOR), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_medium_01", BUILDING_PROCESSOR), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_medium_02", BUILDING_PROCESSOR), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_medium_03", BUILDING_PROCESSOR), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_medium_04", BUILDING_PROCESSOR), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_medium_05", BUILDING_PROCESSOR), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_medium_06", BUILDING_PROCESSOR), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_large_01", BUILDING_PROCESSOR), 8), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_large_02", BUILDING_PROCESSOR), 8), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_large_03", BUILDING_PROCESSOR), 8), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_large_04", BUILDING_PROCESSOR), 8), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_large_05", BUILDING_PROCESSOR), 8), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/decoration/water_tower_1", BUILDING_PROCESSOR), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/decoration/water_tower_2", BUILDING_PROCESSOR), 15)), StructureTemplatePool.Projection.RIGID));
      //  Pools.register(new StructureTemplatePool(BiomeMakeover.ID("ghosttown/buildings"), new ResourceLocation("village/plains/terminators"), ImmutableList.of(Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_small_01"), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_small_02"), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_small_03"), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_small_04"), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_small_05"), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_small_06"), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_small_07"), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_small_08"), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_small_09"), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_small_10"), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_small_11"), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_small_12"), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_medium_01"), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_medium_02"), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_medium_03"), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_medium_04"), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_medium_05"), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_medium_06"), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_large_01"), 8), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_large_02"), 8), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_large_03"), 8), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_large_04"), 8), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/houses/house_large_05"), 8), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/decoration/water_tower_1"), 15), Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/decoration/water_tower_2"), 15)), StructureTemplatePool.Projection.RIGID));

        //Buildings

        Pools.register(new StructureTemplatePool(BiomeMakeover.ID("ghosttown/decoration"), new ResourceLocation("empty"), ImmutableList.of(
                Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/decoration/barrel_decoration"), 2),
                Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/decoration/hay_decoration"), 2),
                Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/decoration/hay_decoration_2"), 2),
                Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/decoration/lamp_decoration"), 2),
                Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/decoration/well_decoration"), 2),
                Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/decoration/trough_decoration"), 2),
                Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/decoration/lamp_decoration_2"), 2),
                Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/decoration/cactus_decoration"), 2),
                Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/decoration/bell_decoration_1"), 2),
                Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/decoration/hay_well_decoration"), 2),
                Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/decoration/tree_decoration_1"), 2),
                Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/decoration/tree_decoration_2"), 2),
                Pair.of(StructurePoolElement.legacy("biomemakeover:ghosttown/decoration/water_tower_2"), 2)
        ), StructureTemplatePool.Projection.RIGID));
    }

    public static class GhostTownLootProcessor extends StructureProcessor
    {
        public static final GhostTownLootProcessor INSTANCE = new GhostTownLootProcessor();
        public static final Codec<GhostTownLootProcessor> CODEC = Codec.unit(()->INSTANCE);

        @Nullable
        @Override
        public StructureTemplate.StructureBlockInfo processBlock(LevelReader worldView, BlockPos pos, BlockPos blockPos, StructureTemplate.StructureBlockInfo info, StructureTemplate.StructureBlockInfo info2, StructurePlaceSettings data)
        {
            BlockState blockState = info2.state;
            if(blockState.getBlock() == Blocks.BARREL)
            {
                BlockEntity be = worldView.getBlockEntity(pos);
                if(be instanceof BarrelBlockEntity)
                {
                    BarrelBlockEntity barrel = (BarrelBlockEntity) be;
                    barrel.setLootTable(BiomeMakeover.ID("ghost_town/loot_" + RandomUtil.RANDOM.nextInt(3)), RandomUtil.RANDOM.nextLong());
                }
            }

            return info2;
        }

        @Override
        protected StructureProcessorType<?> getType()
        {
            return GHOST_TOWN_PROCESSOR;
        }
    }

    //  private static final List<SpawnSettings.SpawnEntry> SPAWNS = Lists.newArrayList(new SpawnSettings.SpawnEntry(BMEntities.GHOST, 8, 1, 1));

}
