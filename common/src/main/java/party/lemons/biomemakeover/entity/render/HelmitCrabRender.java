package party.lemons.biomemakeover.entity.render;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.SkullBlock;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.HelmitCrabEntity;
import party.lemons.biomemakeover.entity.render.feature.CowboyHatModel;
import party.lemons.biomemakeover.entity.render.feature.WitchHatModel;
import party.lemons.biomemakeover.init.BMItems;
import party.lemons.biomemakeover.item.HatItem;

import java.util.Map;

public class HelmitCrabRender extends MobRenderer<HelmitCrabEntity, HelmitCrabModel>
{
	private static final ResourceLocation TEXTURE = BiomeMakeover.ID("textures/entity/helmit_crab/helmit_crab.png");


	public HelmitCrabRender(EntityRendererProvider.Context context)
	{
		super(context, new HelmitCrabModel(context.bakeLayer(HelmitCrabModel.LAYER_LOCATION)), 0.25F);
		this.addLayer(new HelmitCrabShellRenderLayer(context, this));
	}

	@Override
	protected void setupRotations(HelmitCrabEntity livingEntity, PoseStack poseStack, float f, float g, float h)
	{
		if(livingEntity.isHiding())
			poseStack.translate(0, -0.05F, 0);

		super.setupRotations(livingEntity, poseStack, f, g, h);
	}

	@Override
	public void render(HelmitCrabEntity mob, float f, float g, PoseStack poseStack, MultiBufferSource multiBufferSource, int i)
	{
		getModel().shell.visible= mob.getShellItemStack().getItem() == Items.NAUTILUS_SHELL;

		shadowRadius = 0.25F;
		poseStack.pushPose();
		if(mob.isBaby())
		{
			poseStack.scale(0.5F, 0.5F, 0.5F);
			shadowRadius = 0.1F;
		}
		super.render(mob, f, g, poseStack, multiBufferSource, i);
		poseStack.popPose();

	}

	@Override
	public ResourceLocation getTextureLocation(HelmitCrabEntity entity)
	{
		return TEXTURE;
	}

	private class HelmitCrabShellRenderLayer extends RenderLayer<HelmitCrabEntity, HelmitCrabModel>
	{
		private final HumanoidModel BIPED_MODEL;
		private final HelmitCrabModel CRAB_MODEL;
		private final ItemInHandRenderer itemInHandRenderer;
		private final EntityRendererProvider.Context context;
		public Map<Item, EntityModel> HAT_MODELS = Maps.newHashMap();
		private final Map<SkullBlock.Type, SkullModelBase> skullModels;


		private static final ResourceLocation SHULKER_TEXTURE = BiomeMakeover.ID("textures/entity/helmit_crab/shulker.png");

		public HelmitCrabShellRenderLayer(EntityRendererProvider.Context context, HelmitCrabRender helmitCrabRender)
		{
			super(helmitCrabRender);

			this.context = context;
			this.skullModels = SkullBlockRenderer.createSkullRenderers(context.getModelSet());

			BIPED_MODEL = new HumanoidModel(context.bakeLayer(ModelLayers.PLAYER_INNER_ARMOR));
			CRAB_MODEL = new HelmitCrabModel(context.bakeLayer(HelmitCrabModel.LAYER_LOCATION));

			BIPED_MODEL.leftArm.visible = false;
			BIPED_MODEL.rightArm.visible = false;
			BIPED_MODEL.leftLeg.visible = false;
			BIPED_MODEL.rightLeg.visible = false;
			BIPED_MODEL.body.visible = false;

			BIPED_MODEL.head.visible = true;
			BIPED_MODEL.hat.visible = true;

			itemInHandRenderer = context.getItemInHandRenderer();
		}

		@Override
		public void render(PoseStack poseStack, MultiBufferSource multiBufferSource, int light, HelmitCrabEntity entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch)
		{
			getModel().shell.visible = false;

			ItemStack shell = entity.getShellItemStack();
			if(shell.isEmpty())
				return;

			if(shell.getItem() == Items.NAUTILUS_SHELL)
				getModel().shell.visible = true;
			else if(shell.getItem() == Items.SHULKER_SHELL)
				renderSpecialShell(SHULKER_TEXTURE, entity, poseStack, multiBufferSource, light, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch);
			else if(shell.getItem() instanceof HatItem)
				renderHat(shell, entity, poseStack, multiBufferSource, light);
			else if(shell.getItem() instanceof ArmorItem)
				renderHelmet(shell, poseStack, multiBufferSource, light);
			else if(shell.getItem() instanceof BlockItem && ((BlockItem)shell.getItem()).getBlock() instanceof AbstractSkullBlock)
				renderSkull(shell, poseStack, multiBufferSource, light, limbAngle);
			else
				renderHeadItem(entity, shell, poseStack, multiBufferSource, light);
		}

		private void renderHeadItem(HelmitCrabEntity crab, ItemStack shell, PoseStack poseStack, MultiBufferSource mbSource, int light)
		{
			float scale = 0.5F;
			poseStack.translate(0, 1.15F, 0.1F);
			poseStack.scale(scale, -scale, -scale);

			itemInHandRenderer.renderItem(crab, shell, ItemTransforms.TransformType.HEAD, false, poseStack, mbSource, light);
		}

		private void renderSkull(ItemStack shell, PoseStack poseStack, MultiBufferSource mbSource, int light, float limbAngle)
		{
			float scale = 1;
			if(shell.getItem() == Items.DRAGON_HEAD) {
				scale = 0.6F;
				poseStack.translate(0, 0.6F, -0.07);
			}

			poseStack.scale(scale, -scale, scale);
			poseStack.translate(0, -1.4F, 0.1F);


		//	poseStack.translate(-0.5D, 0.0D, -0.5D);
			CompoundTag compoundTag;

			GameProfile gameProfile = null;
			if (shell.hasTag() && (compoundTag = shell.getTag()).contains("SkullOwner", 10)) {
				gameProfile = NbtUtils.readGameProfile(compoundTag.getCompound("SkullOwner"));
			}
			poseStack.translate(-0.5, 0.0, -0.5);
			SkullBlock.Type type = ((AbstractSkullBlock)((BlockItem)shell.getItem()).getBlock()).getType();
			SkullModelBase skullModelBase = this.skullModels.get(type);
			RenderType renderType = SkullBlockRenderer.getRenderType(type, gameProfile);
			SkullBlockRenderer.renderSkull(null, 180.0f, 0, poseStack, mbSource, light, skullModelBase, renderType);
		}

		private void renderSpecialShell(ResourceLocation texture, HelmitCrabEntity crab, PoseStack poseStack, MultiBufferSource mbSource, int light, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch)
		{
			coloredCutoutModelCopyLayerRender(this.getParentModel(), CRAB_MODEL, texture, poseStack, mbSource, light, crab, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch, 1.0F, 1.0F, 1.0F);
		}

		private void renderHat(ItemStack shell, HelmitCrabEntity crab, PoseStack poseStack, MultiBufferSource mbSource, int light)
		{
			poseStack.pushPose();
			poseStack.mulPose(Vector3f.XN.rotationDegrees(10F));
			poseStack.translate(0, 1.35F, 0.3);

			EntityModel hatModel = getHatModel(shell);

			VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(mbSource, RenderType.armorCutoutNoCull(this.getHatTexture((HatItem) shell.getItem())), false,  shell.hasFoil());
			hatModel.renderToBuffer(poseStack, vertexConsumer, light, OverlayTexture.NO_OVERLAY, 1F, 1F, 1F, 1F);
			poseStack.popPose();
		}

		public EntityModel getHatModel(ItemStack stack)
		{
			if(HAT_MODELS.isEmpty()) {  //Doing this here to prevent premature class loading
				HAT_MODELS.put(BMItems.COWBOY_HAT.get(), new CowboyHatModel(context.getModelSet().bakeLayer(CowboyHatModel.LAYER_LOCATION)));
				HAT_MODELS.put(BMItems.WITCH_HAT.get(),new WitchHatModel(context.getModelSet().bakeLayer(WitchHatModel.LAYER_LOCATION)));
			}

			return HAT_MODELS.get(stack.getItem());
		}

		private void renderHelmet(ItemStack stack, PoseStack matrices, MultiBufferSource vertexConsumers, int light)
		{
			BIPED_MODEL.setAllVisible(false);
			BIPED_MODEL.head.visible = true;
			BIPED_MODEL.hat.visible = true;

			matrices.pushPose();
			matrices.translate(0, 0.63F, 0.05);
			matrices.scale(1.1F, 1.1F, 1.1F);
			ArmorItem armorItem = (ArmorItem)stack.getItem();
			boolean hasGlint = stack.hasFoil();
			if (armorItem instanceof DyeableArmorItem)
			{
				int color = ((DyeableArmorItem)armorItem).getColor(stack);
				float r = (float)(color >> 16 & 255) / 255.0F;
				float g = (float)(color >> 8 & 255) / 255.0F;
				float b = (float)(color & 255) / 255.0F;
				this.renderHelmetPart(matrices, vertexConsumers, light, armorItem, hasGlint, r, g, b, null);
				this.renderHelmetPart(matrices, vertexConsumers, light, armorItem, hasGlint, 1.0F, 1.0F, 1.0F, "overlay");
			}
			else
			{
				this.renderHelmetPart(matrices, vertexConsumers, light, armorItem, hasGlint, 1.0F, 1.0F, 1.0F, null);
			}

			matrices.popPose();
		}

		private void renderHelmetPart(PoseStack ms, MultiBufferSource vcp, int light, ArmorItem armorItem, boolean glint, float r, float g, float b, String extra)
		{
			VertexConsumer vertexConsumer = ItemRenderer.getArmorFoilBuffer(vcp, RenderType.armorCutoutNoCull(this.getArmorTexture(armorItem, extra)), false, glint);
			BIPED_MODEL.renderToBuffer(ms, vertexConsumer, light, OverlayTexture.NO_OVERLAY, r, g, b, 1.0f);

		}

		private static final Map<String, ResourceLocation> ARMOR_TEXTURE_CACHE = Maps.newHashMap();
		private ResourceLocation getArmorTexture(ArmorItem armorItem, String extra) {
			String path = "textures/models/armor/" + armorItem.getMaterial().getName() + "_layer_1" + (extra == null ? "" : "_" + extra) + ".png";
			return ARMOR_TEXTURE_CACHE.computeIfAbsent(path, ResourceLocation::new);
		}

		protected ResourceLocation getHatTexture(HatItem hat)
		{
			return hat.getHatTexture();
		}
	}
}
