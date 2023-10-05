package party.lemons.biomemakeover.entity.render.forge;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraftforge.client.ForgeHooksClient;
import party.lemons.biomemakeover.entity.HelmitCrabEntity;
import party.lemons.biomemakeover.entity.render.HelmitCrabRender;

import javax.annotation.Nullable;
import java.util.Locale;
import java.util.Map;

public class HelmitCrabRenderHelmitCrabShellRenderLayerImpl
{
	public static Model getHelmetModel(HelmitCrabEntity entity, HumanoidModel bipedModel, ItemStack stack)
	{
		Model model = ForgeHooksClient.getArmorModel(entity, stack, EquipmentSlot.HEAD, bipedModel);
		return model;
    }

	public static void renderHelmetPlatform(HelmitCrabEntity entity, ItemStack stack, PoseStack matrices, MultiBufferSource vertexConsumers, int light, HumanoidModel baseModel)
	{
		matrices.pushPose();

		Model model = getHelmetModel(entity, baseModel, stack);
		if(model instanceof HumanoidModel)
		{
			matrices.translate(0, 0.65, 0.05);
			matrices.scale(1.1F, 1F, 1F);
		}
		else
		{
			matrices.mulPose(Axis.XN.rotationDegrees(10F));
			matrices.translate(0, 1.5F, 0.3);
		}

		ArmorItem armorItem = (ArmorItem) stack.getItem();

		if (armorItem instanceof DyeableLeatherItem dyeable) {
			int color = dyeable.getColor(stack);
			float dyeRed = (float)(color >> 16 & 0xFF) / 255.0F;
			float dyeGreen = (float)(color >> 8 & 0xFF) / 255.0F;
			float dyeBlue = (float)(color & 0xFF) / 255.0F;
			renderModel(matrices, vertexConsumers, light, armorItem, model, false, dyeRed, dyeGreen, dyeBlue, getArmorResource(entity, stack, EquipmentSlot.HEAD, null));
			renderModel(matrices, vertexConsumers, light, armorItem, model, false, 1.0F, 1.0F, 1.0F, getArmorResource(entity, stack, EquipmentSlot.HEAD, "overlay"));
		} else {
			renderModel(matrices, vertexConsumers, light, armorItem, model, false, 1.0F, 1.0F, 1.0F, getArmorResource(entity, stack, EquipmentSlot.HEAD, null));
		}

		ArmorTrim.getTrim(entity.level().registryAccess(), stack).ifPresent(arg3x -> HelmitCrabRender.renderTrim(armorItem.getMaterial(), matrices, vertexConsumers, light, arg3x, model, false));
		if (stack.hasFoil()) {
			model.renderToBuffer(matrices, vertexConsumers.getBuffer(RenderType.armorEntityGlint()), light, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);

		}

		matrices.popPose();
	}

	private static void renderModel(
			PoseStack arg, MultiBufferSource arg2, int i, ArmorItem arg3, Model arg4, boolean bl, float f, float g, float h, ResourceLocation armorResource
	) {
		VertexConsumer vertexconsumer = arg2.getBuffer(RenderType.armorCutoutNoCull(armorResource));
		arg4.renderToBuffer(arg, vertexconsumer, i, OverlayTexture.NO_OVERLAY, f, g, h, 1.0F);
	}

	public static ResourceLocation getArmorResource(Entity entity, ItemStack stack, EquipmentSlot slot, @Nullable String type) {
		ArmorItem item = (ArmorItem)stack.getItem();
		String texture = item.getMaterial().getName();
		String domain = "minecraft";
		int idx = texture.indexOf(58);
		if (idx != -1) {
			domain = texture.substring(0, idx);
			texture = texture.substring(idx + 1);
		}

		String s1 = String.format(
				Locale.ROOT,
				"%s:textures/models/armor/%s_layer_%d%s.png",
				domain,
				texture,
				1,
				type == null ? "" : String.format(Locale.ROOT, "_%s", type)
		);
		s1 = ForgeHooksClient.getArmorTexture(entity, stack, s1, slot, type);
		ResourceLocation resourcelocation = ARMOR_LOCATION_CACHE.get(s1);
		if (resourcelocation == null) {
			resourcelocation = new ResourceLocation(s1);
			ARMOR_LOCATION_CACHE.put(s1, resourcelocation);
		}

		return resourcelocation;
	}

	private static final Map<String, ResourceLocation> ARMOR_LOCATION_CACHE = Maps.newHashMap();

}
