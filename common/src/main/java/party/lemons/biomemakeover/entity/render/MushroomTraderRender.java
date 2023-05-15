package party.lemons.biomemakeover.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.VillagerModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.CrossedArmsItemLayer;
import net.minecraft.client.renderer.entity.layers.CustomHeadLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.block.state.BlockState;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.MushroomVillagerEntity;
import party.lemons.biomemakeover.init.BMBlocks;

public class MushroomTraderRender extends MobRenderer<MushroomVillagerEntity, VillagerModel<MushroomVillagerEntity>> {

    private static final ResourceLocation TEXTURE = BiomeMakeover.ID("textures/entity/mushrooming_trader_inner.png");


    public MushroomTraderRender(EntityRendererProvider.Context context) {
        super(context, new VillagerModel<>(context.bakeLayer(ModelLayers.VILLAGER)), 0.5f);

        this.addLayer(new MushroomTraderOverlay(this, new VillagerModel<>(context.bakeLayer(ModelLayers.VILLAGER))));
        this.addLayer(new CustomHeadLayer<>(this, context.getModelSet(), context.getItemInHandRenderer()));
        this.addLayer(new CrossedArmsItemLayer<>(this, context.getItemInHandRenderer()));
    }

    @Override
    public ResourceLocation getTextureLocation(MushroomVillagerEntity entity) {
        return TEXTURE;
    }

    protected void scale(MushroomVillagerEntity wanderingTraderEntity, PoseStack matrixStack, float f)
    {
        float g = 0.9375F;
        matrixStack.scale(g, g, g);
    }

    private static class MushroomTraderOverlay extends RenderLayer<MushroomVillagerEntity, VillagerModel<MushroomVillagerEntity>>
    {
        private static final ResourceLocation TEXTURE = BiomeMakeover.ID("textures/entity/mushrooming_trader_outer.png");
        private final VillagerModel<MushroomVillagerEntity> model;


        public MushroomTraderOverlay(MushroomTraderRender mushroomTraderRender, VillagerModel<MushroomVillagerEntity> model) {
            super(mushroomTraderRender);

            this.model = model;
        }

        @Override
        public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int i, MushroomVillagerEntity entity, float f, float g, float h, float j, float k, float l) {
            renderColoredCutoutModel(model, TEXTURE, poseStack, multiBufferSource, i, entity, 1.0F, 1.0F,1.0F);
        }
    }
}
