package party.lemons.biomemakeover.entity.render;

import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.SkullModelBase;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.blockentity.SkullBlockRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.*;
import net.minecraft.world.item.armortrim.ArmorTrim;
import net.minecraft.world.level.block.AbstractSkullBlock;
import net.minecraft.world.level.block.SkullBlock;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.HelmitCrabEntity;
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

	public static void renderTrim(ArmorMaterial arg, PoseStack arg2, MultiBufferSource arg3, int i, ArmorTrim arg4, Model arg5, boolean bl) {
		TextureAtlasSprite textureatlassprite = Minecraft.getInstance().getModelManager().getAtlas(Sheets.ARMOR_TRIMS_SHEET).getSprite(bl ? arg4.innerTexture(arg) : arg4.outerTexture(arg));
		VertexConsumer vertexconsumer = textureatlassprite.wrap(arg3.getBuffer(Sheets.armorTrimsSheet()));
		arg5.renderToBuffer(arg2, vertexconsumer, i, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
	}

	private class HelmitCrabShellRenderLayer extends RenderLayer<HelmitCrabEntity, HelmitCrabModel>
	{
		private final HumanoidModel BIPED_MODEL;
		private final HelmitCrabModel CRAB_MODEL;
		private final ItemInHandRenderer itemInHandRenderer;
		private final EntityRendererProvider.Context context;
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
			else if(shell.getItem() instanceof ArmorItem)
				renderHelmet(entity, shell, poseStack, multiBufferSource, light);
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

			itemInHandRenderer.renderItem(crab, shell, ItemDisplayContext.HEAD, false, poseStack, mbSource, light);
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

			CompoundTag compoundTag;
			GameProfile gameProfile = null;
			if (shell.hasTag() && (compoundTag = shell.getTag()).contains("SkullOwner", 10)) {
				gameProfile = NbtUtils.readGameProfile(compoundTag.getCompound("SkullOwner"));
			}
			poseStack.translate(-0.5, 0.0, -0.5);
			SkullBlock.Type type = ((AbstractSkullBlock)((BlockItem)shell.getItem()).getBlock()).getType();
			SkullModelBase skullModelBase = this.skullModels.get(type);
			RenderType renderType = SkullBlockRenderer.getRenderType(type, gameProfile);
			SkullBlockRenderer.renderSkull(null, 180, limbAngle, poseStack, mbSource, light, skullModelBase, renderType);
		}

		private void renderSpecialShell(ResourceLocation texture, HelmitCrabEntity crab, PoseStack poseStack, MultiBufferSource mbSource, int light, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch)
		{
			coloredCutoutModelCopyLayerRender(this.getParentModel(), CRAB_MODEL, texture, poseStack, mbSource, light, crab, limbAngle, limbDistance, tickDelta, animationProgress, headYaw, headPitch, 1.0F, 1.0F, 1.0F);
		}

		public Model getHatModel(ItemStack stack)
		{
			return HatModels.getHatModel(stack.getItem(), BIPED_MODEL.getHead());
		}

		private void renderHelmet(HelmitCrabEntity entity, ItemStack stack, PoseStack matrices, MultiBufferSource vertexConsumers, int light)
		{
			BIPED_MODEL.setAllVisible(false);
			BIPED_MODEL.head.visible = true;
			BIPED_MODEL.hat.visible = true;

			renderHelmetPlatform(entity, stack, matrices, vertexConsumers, light, BIPED_MODEL);
		}

		@ExpectPlatform
		public static void renderHelmetPlatform(HelmitCrabEntity entity, ItemStack stack, PoseStack matrices, MultiBufferSource vertexConsumers, int light, HumanoidModel BIPED_MODEL)
		{
			throw new AssertionError();
		}
	}
}
