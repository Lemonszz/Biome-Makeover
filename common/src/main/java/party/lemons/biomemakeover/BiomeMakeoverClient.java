package party.lemons.biomemakeover;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import dev.architectury.registry.client.level.entity.EntityRendererRegistry;
import dev.architectury.registry.client.rendering.BlockEntityRendererRegistry;
import dev.architectury.registry.menu.MenuRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HorseModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.VoxelShape;
import party.lemons.biomemakeover.block.SucculentBlock;
import party.lemons.biomemakeover.block.SucculentType;
import party.lemons.biomemakeover.block.blockentity.AltarBlockEntity;
import party.lemons.biomemakeover.block.blockentity.render.AltarRenderer;
import party.lemons.biomemakeover.block.blockentity.render.LightningBugBottleRenderer;
import party.lemons.biomemakeover.block.blockentity.render.TapestryRenderer;
import party.lemons.biomemakeover.crafting.AltarScreen;
import party.lemons.biomemakeover.crafting.DirectionDataScreen;
import party.lemons.biomemakeover.crafting.witch.menu.WitchScreen;
import party.lemons.biomemakeover.entity.render.*;
import party.lemons.biomemakeover.entity.render.feature.CowboyHatRenderLayer;
import party.lemons.biomemakeover.entity.render.feature.ScarabElytraLayer;
import party.lemons.biomemakeover.init.*;
import party.lemons.biomemakeover.util.extension.HorseHat;
import party.lemons.biomemakeover.util.sound.AltarCursingSoundInstance;
import party.lemons.taniwha.client.color.ColorProviderHelper;
import party.lemons.taniwha.client.color.FoliageBlockColorProvider;
import party.lemons.taniwha.client.color.FoliageShiftBlockColorProvider;
import party.lemons.taniwha.client.color.TemperatureGradientColorProvider;
import party.lemons.taniwha.client.model.RenderLayerInjector;
import party.lemons.taniwha.hooks.TClientEvents;
import party.lemons.taniwha.item.animation.CustomUseAnimationRegistry;

public class BiomeMakeoverClient
{
    @Environment(EnvType.CLIENT)
    public static void init()
    {
        BMEntities.registerModelLayers();

        BMBlockEntities.DIRECTIONAL_DATA.listen((b)->{
            BlockEntityRendererRegistry.register(BMBlockEntities.TAPESTRY.get(), TapestryRenderer::new);
            BlockEntityRendererRegistry.register(BMBlockEntities.ALTAR.get(), AltarRenderer::new);
            BlockEntityRendererRegistry.register(BMBlockEntities.LIGHTNING_BUG_BOTTLE.get(), LightningBugBottleRenderer::new);
        });


        initColors();

        BMScreens.DIRECTIONAL_DATA.listen((b)->{
            MenuRegistry.registerScreenFactory(BMScreens.WITCH.get(), WitchScreen::new);
            MenuRegistry.registerScreenFactory(BMScreens.ALTAR.get(), AltarScreen::new);
            MenuRegistry.registerScreenFactory(BMScreens.DIRECTIONAL_DATA.get(), DirectionDataScreen::new);
        });

        registerModels();

        registerAnimations();
    }

    private static void registerAnimations()
    {
        CustomUseAnimationRegistry.register(BMItems.ALOE_VERA, new CustomUseAnimationRegistry.CustomUseAnimation(){
            @Override
            public void transform(Minecraft minecraft, PoseStack poseStack, float f, float swing, HumanoidArm humanoidArm, ItemStack itemStack) {
                this.applyItemArmTransform(poseStack, humanoidArm, swing);

                float time = (float)minecraft.player.getUseItemRemainingTicks() - f + 1.0F;
                float totalTime = time / (float)itemStack.getUseDuration();

                float zz = 1.0F - (float)Math.pow(totalTime, 27.0);
                int xDirection = humanoidArm == HumanoidArm.RIGHT ? 1 : -1;


                float xx = (-0.5F * xDirection) + Mth.sin((xDirection - totalTime) * 8) / 2F;
                float yy = Mth.sin((totalTime) * 10) / 2F;

                poseStack.translate(xx + yy * 0.6F * (float)xDirection, yy * -0.5F, 0.0F);
                poseStack.mulPose(Axis.YP.rotationDegrees((float)xDirection * zz * 90.0F));
                poseStack.mulPose(Axis.XP.rotationDegrees(zz * 10.0F));            }
        });
    }

    @Environment(EnvType.CLIENT)
    public static void registerModels()
    {
        EntityRendererRegistry.register(BMEntities.TUMBLEWEED, TumbleweedRender::new);
        EntityRendererRegistry.register(BMEntities.LIGHTNING_BOTTLE, ThrownItemRenderer::new);

        EntityRendererRegistry.register(BMEntities.GLOWFISH, GlowfishRender::new);
        EntityRendererRegistry.register(BMEntities.BLIGHTBAT, BlightBatRender::new);
        EntityRendererRegistry.register(BMEntities.MUSHROOM_TRADER, MushroomTraderRender::new);
        EntityRendererRegistry.register(BMEntities.SCUTTLER, ScuttlerRender::new);
        EntityRendererRegistry.register(BMEntities.GHOST, GhostRender::new);
        EntityRendererRegistry.register(BMEntities.COWBOY, CowboyRender::new);
        EntityRendererRegistry.register(BMEntities.DECAYED, DecayedRender::new);
        EntityRendererRegistry.register(BMEntities.DRAGONFLY, DragonflyRender::new);
        EntityRendererRegistry.register(BMEntities.TOAD, ToadRender::new);
        EntityRendererRegistry.register(BMEntities.TADPOLE, TadpoleRender::new);
        EntityRendererRegistry.register(BMEntities.LIGHTNING_BUG, LightningBugRender::new);
        EntityRendererRegistry.register(BMEntities.LIGHTNING_BUG_ALTERNATE, LightningBugRender::new);
        EntityRendererRegistry.register(BMEntities.OWL, OwlRender::new);
        EntityRendererRegistry.register(BMEntities.MOTH, MothRender::new);
        EntityRendererRegistry.register(BMEntities.ROOTLING, RootlingRender::new);
        EntityRendererRegistry.register(BMEntities.ADJUDICATOR, AdjudicatorRender::new);
        EntityRendererRegistry.register(BMEntities.ADJUDICATOR_MIMIC, AdjudicatorMimicRender::new);
        EntityRendererRegistry.register(BMEntities.STONE_GOLEM, StoneGolemRender::new);
        EntityRendererRegistry.register(BMEntities.HELMIT_CRAB, HelmitCrabRender::new);
        EntityRendererRegistry.register(BMEntities.CHEST_CAMEL, ChestCamelRender::new);
        EntityRendererRegistry.register(BMEntities.BANNER_CAMEL, BannerCamelRender::new);
        EntityRendererRegistry.register(BMEntities.DUST_DEVIL, DustDevilRenderer::new);
        TClientEvents.LAYERS.add((renderLayerParent, entityModelSet) -> new ScarabElytraLayer(renderLayerParent, entityModelSet));

        RenderLayerInjector.inject(
                EntityType.HORSE,
                (ctx)->new CowboyHatRenderLayer(ctx.entityRenderer(), ctx.modelSet()) {

                    @Override
                    protected boolean hasHat(LivingEntity entity) {
                        return ((HorseHat)entity).hasHat();
                    }

                    @Override
                    protected void setup(PoseStack poseStack) {
                        poseStack.scale(1.05F, 1.05F, 1.05F);

                        ((ModelPart)((HorseModel)getParentModel()).headParts().iterator().next()).translateAndRotate(poseStack);
                        poseStack.translate(0F, -0.4F, 0);
                        poseStack.mulPose(Axis.XP.rotationDegrees(-25F));
                    }
            });
    }

    private static void initColors()
    {
        ColorProviderHelper.registerSimpleBlockWithItem(new FoliageBlockColorProvider(),
                BMBlocks.ANCIENT_OAK_LEAVES,
                BMBlocks.IVY
        );

        ColorProviderHelper.registerSimpleBlockWithItem(new TemperatureGradientColorProvider(0x729460, 0x84ab6f, 0xa0ab6f),
                BMBlocks.SWAMP_CYPRESS_LEAVES
        );

        ColorProviderHelper.registerSimpleBlockWithItem(new FoliageShiftBlockColorProvider(0, 0, 0)
        {
            @Override
            protected int[] getColorBoosts(BlockAndTintGetter world, BlockState state, BlockPos pos, int tintIndex)
            {
                if(world instanceof ClientLevel && pos != null)
                {
                    if(((ClientLevel) world).getBiome(pos).is(BMFeatures.SWAMP_BIOMES))
                    {
                        return new int[]{-20, 40, -20};
                    }
                }

                return super.getColorBoosts(world, state, pos, tintIndex);
            }
        },
                BMBlocks.SMALL_LILY_PAD,
                ()->Blocks.LILY_PAD,
                BMBlocks.WATER_LILY
        );

        ColorProviderHelper.registerSimpleBlockWithItem(new FoliageShiftBlockColorProvider(0, 0, 0)
        {
            @Override
            protected int[] getColorBoosts(BlockAndTintGetter world, BlockState state, BlockPos pos, int tintIndex)
            {
                if(world instanceof ClientLevel)
                {
                    if(((ClientLevel) world).getBiome(pos).is(BMFeatures.SWAMP_BIOMES))
                    {
                        return new int[]{-10, 15, -10};
                    }
                }

                return super.getColorBoosts(world, state, pos, tintIndex);
            }
        },
            BMBlocks.WILLOW_LEAVES, BMBlocks.WILLOWING_BRANCHES
        );

        ColorProviderHelper.registerSimpleBlockWithItem(new FoliageShiftBlockColorProvider(35, -10, -5),
                BMBlocks.MOTH_BLOSSOM, BMBlocks.ITCHING_IVY
        );
    }
    public static void curseSound(AltarBlockEntity altar)
    {
        AltarCursingSoundInstance sound = new AltarCursingSoundInstance(altar, altar.getLevel().getRandom());
        Minecraft.getInstance().getSoundManager().play(sound);
    }

    public static void succulentBreakParticles(Level level, BlockPos pos, BlockState state)
    {
        SoundType soundtype = state.getSoundType();
        level.playLocalSound(pos, soundtype.getBreakSound(), SoundSource.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F, false);

        VoxelShape voxelShape = state.getShape(level, pos);

        for(EnumProperty<SucculentType> property : SucculentBlock.TYPES) {
            if (SucculentBlock.isActive(state, property)) {
                TextureAtlasSprite particleTexture = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS)
                        .apply(BiomeMakeover.ID("block/succulent/" + state.getValue(property).getSerializedName()));

                float count = 0.5F;
                voxelShape.forAllBoxes(
                        (x1, y1, z1, x2, y2, z2) -> {
                            double xSize = Math.min(1.0, x2 - x1);
                            double ySize = Math.min(1.0, y2 - y1);
                            double zSize = Math.min(1.0, z2 - z1);
                            int xCount = Math.max(2, Mth.ceil(xSize / count));
                            int yCount = Math.max(2, Mth.ceil(ySize / count));
                            int zCount = Math.max(2, Mth.ceil(zSize / count));

                            for (int xx = 0; xx < xCount; ++xx) {
                                for (int yy = 0; yy < yCount; ++yy) {
                                    for (int zz = 0; zz < zCount; ++zz) {
                                        double vX = ((double) xx + 0.5) / (double) xCount;
                                        double vY = ((double) yy + 0.5) / (double) yCount;
                                        double vZ = ((double) zz + 0.5) / (double) zCount;
                                        double xOffset = vX * xSize + x1;
                                        double yOffset = vY * ySize + y1;
                                        double zOffset = vZ * zSize + z1;
                                        TerrainParticle particle = new TerrainParticle(
                                                (ClientLevel) level,
                                                (double) pos.getX() + xOffset,
                                                (double) pos.getY() + yOffset,
                                                (double) pos.getZ() + zOffset,
                                                vX - 0.5, vY - 0.5, vZ - 0.5, state, pos
                                        );
                                        particle.setSprite(particleTexture);

                                        Minecraft.getInstance().particleEngine.add(particle);
                                    }
                                }
                            }
                        }
                );
            }
        }
    }
}
