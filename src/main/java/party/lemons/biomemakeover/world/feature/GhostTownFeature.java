package party.lemons.biomemakeover.world.feature;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.structure.processor.*;
import net.minecraft.structure.rule.AlwaysTrueRuleTest;
import net.minecraft.structure.rule.BlockMatchRuleTest;
import net.minecraft.structure.rule.RandomBlockMatchRuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.JigsawFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.init.BMBlocks;
import party.lemons.biomemakeover.util.JigsawHelper;

import java.util.List;

public class GhostTownFeature extends JigsawFeature
{
	public static final StructureProcessorList ROADS_PROCESSOR = JigsawHelper.register(
			new StructureProcessorList(ImmutableList.of(
					new RuleStructureProcessor(
							ImmutableList.of(
									new StructureProcessorRule(new BlockMatchRuleTest(Blocks.GRASS_PATH), new BlockMatchRuleTest(Blocks.WATER), Blocks.OAK_PLANKS.getDefaultState()),
									new StructureProcessorRule(new RandomBlockMatchRuleTest(Blocks.GRASS_PATH, 0.3F), AlwaysTrueRuleTest.INSTANCE, Blocks.RED_SAND.getDefaultState()),
									new StructureProcessorRule(new BlockMatchRuleTest(Blocks.RED_SAND), new BlockMatchRuleTest(Blocks.WATER), Blocks.WATER.getDefaultState()),
									new StructureProcessorRule(new BlockMatchRuleTest(Blocks.DIRT), new BlockMatchRuleTest(Blocks.WATER), Blocks.WATER.getDefaultState())))))
			, "roads_ghosttown");

	//Centers
	private static final StructurePool POOL = StructurePools.register(new StructurePool(BiomeMakeover.ID("ghosttown/centers"), new Identifier("empty"), ImmutableList.of(
			Pair.of(StructurePoolElement.method_30426("biomemakeover:ghosttown/centers/crossroads_01", ROADS_PROCESSOR), 1),
			Pair.of(StructurePoolElement.method_30426("biomemakeover:ghosttown/roads/street_01", ROADS_PROCESSOR), 1)
	), StructurePool.Projection.RIGID));


	public static final StructurePoolFeatureConfig CONFIG = new StructurePoolFeatureConfig(()->POOL, 3);

	public static void init()
	{
		List<StructureProcessorRule> BUILDING_RULES = Lists.newArrayList();
		for(Block bl : BMBlocks.BRICK_TO_TERRACOTTA.keySet())
		{
			BUILDING_RULES.add(new StructureProcessorRule(new RandomBlockMatchRuleTest(bl, 0.25F), AlwaysTrueRuleTest.INSTANCE, BMBlocks.BRICK_TO_TERRACOTTA.get(bl).getDefaultState()));
		}

		StructureProcessorList BUILDING_PROCESSOR = JigsawHelper.register(
				new StructureProcessorList(ImmutableList.of(
						new RuleStructureProcessor(BUILDING_RULES))), "buildings_ghosttown");


		//Roads,
		StructurePools.register(new StructurePool(BiomeMakeover.ID("ghosttown/roads"), new Identifier("village/plains/terminators"), ImmutableList.of(
				Pair.of(StructurePoolElement.method_30426("biomemakeover:ghosttown/roads/street_01", ROADS_PROCESSOR), 1),
				Pair.of(StructurePoolElement.method_30426("biomemakeover:ghosttown/roads/street_02", ROADS_PROCESSOR), 3),
				Pair.of(StructurePoolElement.method_30426("biomemakeover:ghosttown/roads/street_03", ROADS_PROCESSOR), 1)
		), StructurePool.Projection.RIGID));

		//Buildings
		StructurePools.register(new StructurePool(BiomeMakeover.ID("ghosttown/buildings"), new Identifier("village/plains/terminators"), ImmutableList.of(
				Pair.of(StructurePoolElement.method_30426("biomemakeover:ghosttown/houses/house_small_01", BUILDING_PROCESSOR), 2),
				Pair.of(StructurePoolElement.method_30426("biomemakeover:ghosttown/houses/house_small_02", BUILDING_PROCESSOR), 2),
				Pair.of(StructurePoolElement.method_30426("biomemakeover:ghosttown/houses/house_medium_01", BUILDING_PROCESSOR), 2),
				Pair.of(StructurePoolElement.method_30426("biomemakeover:ghosttown/houses/house_medium_02", BUILDING_PROCESSOR), 2),
				Pair.of(StructurePoolElement.method_30426("biomemakeover:ghosttown/houses/house_large_01", BUILDING_PROCESSOR), 2),
				Pair.of(StructurePoolElement.method_30426("biomemakeover:ghosttown/houses/house_large_02", BUILDING_PROCESSOR), 2)
		), StructurePool.Projection.RIGID));

		//Buildings
		StructurePools.register(new StructurePool(BiomeMakeover.ID("ghosttown/decoration"), new Identifier("empty"), ImmutableList.of(
				Pair.of(StructurePoolElement.method_30425("biomemakeover:ghosttown/decoration/barrel_decoration"), 2),
				Pair.of(StructurePoolElement.method_30425("biomemakeover:ghosttown/decoration/hay_decoration"), 2),
				Pair.of(StructurePoolElement.method_30425("biomemakeover:ghosttown/decoration/lamp_decoration"), 2),
				Pair.of(StructurePoolElement.method_30425("biomemakeover:ghosttown/decoration/well_decoration"), 2)
		), StructurePool.Projection.RIGID));
	}

	public GhostTownFeature(Codec<StructurePoolFeatureConfig> codec)
	{
		super(codec, 0, true, true);
	}
}
