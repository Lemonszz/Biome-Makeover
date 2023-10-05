package party.lemons.biomemakeover.entity.render.fabric;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer;
import net.fabricmc.fabric.impl.client.rendering.ArmorRendererRegistryImpl;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.HumanoidArmorLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.DyeableLeatherItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.armortrim.ArmorTrim;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import party.lemons.biomemakeover.entity.HelmitCrabEntity;
import party.lemons.biomemakeover.entity.render.HelmitCrabRender;

import java.util.Locale;
import java.util.Map;

public class HelmitCrabRenderHelmitCrabShellRenderLayerImpl
{

	public static void renderHelmetPlatform(HelmitCrabEntity entity, ItemStack stack, PoseStack matrices, MultiBufferSource vertexConsumers, int light, HumanoidModel bipedModel)
	{
		matrices.pushPose();
		ArmorRenderer renderer = ArmorRendererRegistryImpl.get(stack.getItem());

		if (renderer != null) {
			matrices.mulPose(Axis.XN.rotationDegrees(10F));
			matrices.translate(0, 1.5F, 0.3);
			renderer.render(matrices, vertexConsumers, stack, entity, EquipmentSlot.HEAD, light, bipedModel);
			matrices.popPose();
			return;
		}

		Item item = stack.getItem();
		boolean hasGlint = stack.hasFoil();

		if (item instanceof DyeableLeatherItem dyable) {
			int dyeColor = dyable.getColor(stack);
			float dyeRed = (float)(dyeColor >> 16 & 0xFF) / 255.0F;
			float dyeGreen = (float)(dyeColor >> 8 & 0xFF) / 255.0F;
			float dyeBlue = (float)(dyeColor & 0xFF) / 255.0F;
			renderModel(matrices, vertexConsumers, light, stack, bipedModel, hasGlint, dyeRed, dyeGreen, dyeBlue, null);
			renderModel(matrices, vertexConsumers, light, stack, bipedModel, hasGlint, 1.0F, 1.0F, 1.0F,  "overlay");
		} else {
			renderModel(matrices, vertexConsumers, light, stack, bipedModel, hasGlint, 1.0F, 1.0F, 1.0F, null);
		}

		if(item instanceof ArmorItem armorItem) {
			ArmorTrim.getTrim(entity.level().registryAccess(), stack).ifPresent(arg3x -> HelmitCrabRender.renderTrim(armorItem.getMaterial(), matrices, vertexConsumers, light, arg3x, bipedModel, false));
		}
		matrices.popPose();
	}

	private static void renderModel(
			PoseStack poseStack,
			MultiBufferSource multiBufferSource,
			int light,
			ItemStack armorItem,
			HumanoidModel humanoidModel,
			boolean bl,
			float f,
			float g,
			float h,
			@Nullable String string
	) {
		poseStack.pushPose();
		poseStack.translate(0, 0.65, 0.05);
		poseStack.scale(1.1F, 1F, 1F);
		VertexConsumer vertexConsumer = multiBufferSource.getBuffer(RenderType.armorCutoutNoCull(getArmorLocation((ArmorItem)armorItem.getItem(), bl, string)));
		humanoidModel.renderToBuffer(poseStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, f, g, h, 1.0F);
		poseStack.popPose();
	}

	private static ResourceLocation getArmorLocation(ArmorItem item, boolean secondLayer, String overlay) {
		final String name = item.getMaterial().getName();
		final int separator = name.indexOf(ResourceLocation.NAMESPACE_SEPARATOR);

		if (separator != -1) {
			final String namespace = name.substring(0, separator);
			final String path = name.substring(separator + 1);
			final String texture = String.format(Locale.ROOT, "%s:textures/models/armor/%s_layer_%d%s.png", namespace, path, secondLayer ? 2 : 1, overlay == null ? "" : "_" + overlay);

			return ARMOR_TEXTURE_CACHE.computeIfAbsent(texture, ResourceLocation::new);
		}

		String string2 = "textures/models/armor/" + item.getMaterial().getName() + "_layer_1" + (overlay == null ? "" : "_" + overlay) + ".png";
		return ARMOR_TEXTURE_CACHE.computeIfAbsent(string2, ResourceLocation::new);
	}
	private static final Map<String, ResourceLocation> ARMOR_TEXTURE_CACHE = Maps.newHashMap();

}
