package party.lemons.biomemakeover.entity.render.feature;

import com.google.common.collect.Maps;
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
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import party.lemons.biomemakeover.init.BMItems;
import party.lemons.biomemakeover.item.HatItem;

import java.util.Map;

public class HatFeatureRenderer<T extends LivingEntity, M extends EntityModel<T>> extends FeatureRenderer<T, M>
{
	public static Map<Item, EntityModel> MODELS = Maps.newHashMap();
	static{
		MODELS.put(BMItems.COWBOY_HAT, new CowboyHatModel<>());
		MODELS.put(BMItems.WITCH_HAT, new WitchHatModel<>());
	}

	public HatFeatureRenderer(FeatureRendererContext<T, M> context)
	{
		super(context);
	}

	@Override
	public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch)
	{
		ItemStack headSlot = entity.getEquippedStack(EquipmentSlot.HEAD);
		if(!headSlot.isEmpty() && headSlot.getItem() instanceof HatItem)
		{
			matrices.push();
			matrices.scale(1.05F,1.05F,1.05F);
			EntityModel hatModel = MODELS.get(headSlot.getItem());
			((ModelWithHead)this.getContextModel()).getHead().rotate(matrices);

			VertexConsumer vertexConsumer = ItemRenderer.getArmorGlintConsumer(vertexConsumers, hatModel.getLayer(this.getTexture(entity)), true, false);
			hatModel.render(matrices, vertexConsumer, light, OverlayTexture.DEFAULT_UV, 1F, 1F, 1F, 1F);
			matrices.pop();
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
