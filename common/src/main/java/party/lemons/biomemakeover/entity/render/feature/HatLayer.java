package party.lemons.biomemakeover.entity.render.feature;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import party.lemons.biomemakeover.init.BMItems;
import party.lemons.biomemakeover.item.HatItem;

import java.util.Map;

public class HatLayer<T extends LivingEntity, M extends EntityModel<T>> extends RenderLayer<T, M> {

    public Map<Item, EntityModel<T>> MODELS = Maps.newHashMap();
    private final EntityModelSet modelSet;
    private final float yOffset;

    public HatLayer(RenderLayerParent<T, M> par, EntityModelSet modelSet) {
        this(par, modelSet, 0);
    }

    public HatLayer(RenderLayerParent<T, M> par, EntityModelSet modelSet, float yOffset) {
        super(par);
        this.yOffset = yOffset;
        this.modelSet = modelSet;
    }

    public HatItem getHatItem(T entity)
    {
        ItemStack headSlot = entity.getItemBySlot(EquipmentSlot.HEAD);
        if(headSlot.getItem() instanceof HatItem hat && !headSlot.isEmpty())
            return hat;
        return null;
    }

    public EntityModel<T> getHatModel(T entity)
    {
        Item hatItem = getHatItem(entity);
        if(hatItem != null)
        {
            if(MODELS.isEmpty()) {  //Doing this here to prevent premature class loading
                MODELS.put(BMItems.COWBOY_HAT.get(), new CowboyHatModel<>(modelSet.bakeLayer(CowboyHatModel.LAYER_LOCATION)));
                MODELS.put(BMItems.WITCH_HAT.get(),new WitchHatModel<>(modelSet.bakeLayer(WitchHatModel.LAYER_LOCATION)));
            }

            return MODELS.get(hatItem);
        }
        return null;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch)
    {
        EntityModel<T> hatModel = getHatModel(entity);
        ItemStack headSlot = entity.getItemBySlot(EquipmentSlot.HEAD);
        if(hatModel != null)
        {
            poseStack.pushPose();
            setupHat(poseStack);

            VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(multiBufferSource, RenderType.armorCutoutNoCull(this.getTextureLocation(entity)), false,  headSlot.hasFoil());
            hatModel.renderToBuffer(poseStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
            poseStack.popPose();
        }
    }

    public void setupHat(PoseStack poseStack)
    {
        ((HeadedModel) this.getParentModel()).getHead().translateAndRotate(poseStack);
        poseStack.scale(1.25F,1.25F,1.25F);
        poseStack.translate(0, yOffset, 0);
    }

    @Override
    protected ResourceLocation getTextureLocation(T entity)
    {
        HatItem hatItem = getHatItem(entity);
        if(hatItem != null)
        {
            return hatItem.getHatTexture();
        }
        return super.getTextureLocation(entity);
    }
}
