package party.lemons.biomemakeover;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.ColorProviderRegistry;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.client.color.world.FoliageColors;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.item.BlockItem;
import net.minecraft.particle.ParticleType;
import net.minecraft.world.biome.Biome;
import party.lemons.biomemakeover.block.blockentity.render.LightningBugBottleBlockRenderer;
import party.lemons.biomemakeover.crafting.witch.screen.WitchScreen;
import party.lemons.biomemakeover.entity.render.*;
import party.lemons.biomemakeover.init.*;
import party.lemons.biomemakeover.util.MathUtils;
import party.lemons.biomemakeover.util.WoodTypeInfo;
import party.lemons.biomemakeover.util.access.ChunkRenderRegionAccess;
import party.lemons.biomemakeover.world.particle.LightningSparkParticle;

public class BiomeMakeoverClient implements ClientModInitializer
{

	@Override
	public void onInitializeClient()
	{
		EntityRendererRegistry.INSTANCE.register(BMEntities.MUSHROOM_TRADER, (r, c)->new MushroomVillagerRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.BLIGHTBAT, (r, c)->new BlightBatRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.GLOWFISH, (r, c)->new GlowfishRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.TUMBLEWEED, (r, c)->new TumbleweedRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.COWBOY, (r, c)->new CowboyRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.GHOST, (r, c)->new GhostRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.SCUTTLER, (r, c)->new ScuttlerRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.BM_BOAT, (r, c)->new BMBoatRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.TOAD, (r, c)->new ToadRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.TADPOLE, (r, c)->new TadpoleRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.DRAGONFLY, (r, c)->new DragonflyRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.DECAYED, (r, c)->new DecayedRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.LIGHTNING_BUG, (r, c)->new LightningBugRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.LIGHTNING_BUG_ALTERNATE, (r, c)->new LightningBugRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.LIGHTNING_BOTTLE, (r, c)->new FlyingItemEntityRenderer(r, c.getItemRenderer()));
		EntityRendererRegistry.INSTANCE.register(BMEntities.GIANT_SLIME, (r, c)->new GiantSlimeRender(r));
		EntityRendererRegistry.INSTANCE.register(BMEntities.OWL, (r, c)->new OwlEntityRender(r));

		BlockEntityRendererRegistry.INSTANCE.register(BMBlockEntities.LIGHTNING_BUG_BOTTLE, (r)->new LightningBugBottleBlockRenderer(r));

		ScreenRegistry.register(BMScreens.WITCH, WitchScreen::new);

		BMNetwork.initClient();

		ParticleFactoryRegistry.getInstance().register((ParticleType)BMEffects.LIGHTNING_SPARK, LightningSparkParticle.Factory::new);

		//TODO: move this
		ColorProviderRegistry.BLOCK.register(
				(state, world, pos, tintIndex)->world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getDefaultColor());

		ColorProviderRegistry.BLOCK.register(
				(state, world, pos, tintIndex)->0x84ab6f, BMBlocks.SWAMP_CYPRESS_LEAVES);

		ColorProviderRegistry.BLOCK.register(
				(state, world, pos, tintIndex)->{
					int color = world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getDefaultColor();

					int rShift = -10;
					int gShift = 10;
					int bShift = -10;
					if(world instanceof ChunkRenderRegionAccess)
					{
						if(((ChunkRenderRegionAccess)world).getWorld().getBiome(pos).getCategory() == Biome.Category.SWAMP)
						{
							rShift = -20;
							gShift = 40;
							bShift = -20;
						}
					}

					return MathUtils.colourBoost(color, rShift, gShift, bShift);
				}, BMBlocks.SMALL_LILY_PAD, Blocks.LILY_PAD, BMBlocks.WATER_LILY
		);

		ColorProviderRegistry.BLOCK.register(
				(state, world, pos, tintIndex)->{
					int color = world != null && pos != null ? BiomeColors.getFoliageColor(world, pos) : FoliageColors.getDefaultColor();
					if(world instanceof ChunkRenderRegionAccess)
					{
						if(((ChunkRenderRegionAccess)world).getWorld().getBiome(pos).getCategory() == Biome.Category.SWAMP)
						{
							return MathUtils.colourBoost(color, -10, 15, -10);
						}
					}

					return color;
				}, BMBlocks.WILLOWING_BRANCHES, BMBlocks.WILLOW_LEAVES
		);

		ColorProviderRegistry.ITEM.register((stack, tintIndex)->{
			BlockState blockState = ((BlockItem)stack.getItem()).getBlock().getDefaultState();
			return ColorProviderRegistry.BLOCK.get(blockState.getBlock()).getColor(blockState, null, null, tintIndex);
		}, BMBlocks.WILLOWING_BRANCHES.asItem(), BMBlocks.WILLOW_LEAVES.asItem(), Blocks.LILY_PAD, BMBlocks.SMALL_LILY_PAD, BMBlocks.SWAMP_CYPRESS_LEAVES);

		ColorProviderRegistry.ITEM.register((stack, tintIndex)->{
			if(tintIndex == 0)
			{
				BlockState blockState = ((BlockItem) stack.getItem()).getBlock().getDefaultState();
				return ColorProviderRegistry.BLOCK.get(blockState.getBlock()).getColor(blockState, null, null, tintIndex);
			}
			return 0xFFFFFF;
		}, BMBlocks.WATER_LILY);

		//TODO: Move this
		BlockRenderLayerMap.INSTANCE.putBlocks(RenderLayer.getCutout(),
				BMBlocks.MYCELIUM_ROOTS,
				BMBlocks.MYCELIUM_SPROUTS,
				BMBlocks.POTTED_MYCELIUM_ROOTS,
				BMBlocks.POTTED_GREEN_GLOWSHROOM,
				BMBlocks.POTTED_PURPLE_GLOWSHROOM,
				BMBlocks.POTTED_ORANGE_GLOWSHROOM,
				BMBlocks.POTTED_BLIGHTED_BALSA_SAPLING,
				BMBlocks.POTTED_BARREL_CACTUS,
				BMBlocks.POTTED_FLOWERED_BARREL_CACTUS,
				BMBlocks.POTTED_SAGUARO_CACTUS,
				BMBlocks.POTTED_SWAMP_CYPRESS_SAPLING,
				BMBlocks.POTTED_WILLOW_SAPLING,
				BMBlocks.POTTED_SAGUARO_CACTUS,
				BMBlocks.TALL_BROWN_MUSHROOM,
				BMBlocks.TALL_RED_MUSHROOM,
				BMBlocks.PURPLE_GLOWSHROOM,
				BMBlocks.GREEN_GLOWSHROOM,
				BMBlocks.ORANGE_GLOWSHROOM,
				BMBlocks.BLIGHTED_BALSA_SAPLING,
				BMBlocks.TUMBLEWEED,
				BMBlocks.SAGUARO_CACTUS,
				BMBlocks.BARREL_CACTUS,
				BMBlocks.BARREL_CACTUS_FLOWERED,
				BMBlocks.POLTERGEIST,
				BMBlocks.WILLOWING_BRANCHES,
				BMBlocks.CATTAIL,
				BMBlocks.REED,
				BMBlocks.SMALL_LILY_PAD,
				BMBlocks.WILLOW_LEAVES,
				BMBlocks.WILLOW_SAPLING,
				BMBlocks.SWAMP_CYPRESS_SAPLING,
				BMBlocks.WILLOW_WOOD_INFO.getBlock(WoodTypeInfo.Type.DOOR),
				BMBlocks.WILLOW_WOOD_INFO.getBlock(WoodTypeInfo.Type.TRAP_DOOR),
				BMBlocks.SWAMP_CYPRESS_WOOD_INFO.getBlock(WoodTypeInfo.Type.DOOR),
				BMBlocks.SWAMP_CYPRESS_WOOD_INFO.getBlock(WoodTypeInfo.Type.TRAP_DOOR),
				BMBlocks.LIGHTNING_BUG_BOTTLE,
				BMBlocks.WATER_LILY,
				BMBlocks.SWAMP_AZALEA,
				BMBlocks.MARIGOLD,
				BMBlocks.ILLUNITE_CLUSTER
		);
	}
}
