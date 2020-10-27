package party.lemons.biomemakeover.entity.render.feature;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.model.ModelWithHead;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import party.lemons.biomemakeover.item.HatItem;

public class HatFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M>
{
	//TODO: Support more hat models

	public HatFeatureRenderer(FeatureRendererContext<T, M> context)
	{
		super(context);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch)
	{
		if(!entity.getEquippedStack(EquipmentSlot.HEAD).isEmpty() && entity.getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof HatItem)
		{
			matrices.scale(1.05F,1.05F,1.05F);
			CowboyHatModel<T> hatModel = new CowboyHatModel<>();
			((ModelWithHead)this.getContextModel()).getHead().rotate(matrices);

			VertexConsumer vertexConsumer = ItemRenderer.getArmorVertexConsumer(vertexConsumers, hatModel.getLayer(this.getTexture(entity)), true, false);
			hatModel.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1F, 1F, 1F, 1F);
		}
	}

	@Override
	protected Identifier getTexture(T entity)
	{
		if(!entity.getEquippedStack(EquipmentSlot.HEAD).isEmpty() && entity.getEquippedStack(EquipmentSlot.HEAD).getItem() instanceof HatItem)
		{
			return ((HatItem)entity.getEquippedStack(EquipmentSlot.HEAD).getItem()).getHatTexture();
		}
		return super.getTexture(entity);
	}
}
