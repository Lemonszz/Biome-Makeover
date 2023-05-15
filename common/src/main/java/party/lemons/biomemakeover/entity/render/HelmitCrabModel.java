package party.lemons.biomemakeover.entity.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HeadedModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.util.Mth;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.HelmitCrabEntity;
import party.lemons.biomemakeover.util.AnimationHelper;

public class HelmitCrabModel extends EntityModel<HelmitCrabEntity> implements HeadedModel
{
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(BiomeMakeover.ID("helmit_crab"), "main");
	public final ModelPart body, head, shell, hide_head, leg_front_right, leg_front_left, leg_back_right, leg_back_left, claw_right, claw_bottom_right,
			claw_top_right, claw_left, claw_top_left, claw_bottom_left, hide_claw_left, hide_claw_top_left, hide_claw_bottom_left, hide_claw_right,
			claw_bottom_right2, claw_top_right2;


	public HelmitCrabModel(ModelPart root) {
		this.body = root.getChild("body");
		this.hide_head = body.getChild("hide_head");
		this.head = body.getChild("head");
		this.shell = body.getChild("shell");
		this.leg_front_right = body.getChild("leg_front_right");
		this.leg_front_left = body.getChild("leg_front_left");
		this.leg_back_right = body.getChild("leg_back_right");
		this.leg_back_left = body.getChild("leg_back_left");
		this.claw_right = body.getChild("claw_right");
		this.claw_bottom_right = claw_right.getChild("claw_bottom_right");
		this.claw_top_right = claw_right.getChild("claw_top_right");
		this.claw_left = body.getChild("claw_left");
		this.claw_top_left = claw_left.getChild("claw_top_left");
		this.claw_bottom_left = claw_left.getChild("claw_bottom_left");
		this.hide_claw_left = body.getChild("hide_claw_left");
		this.hide_claw_top_left = hide_claw_left.getChild("hide_claw_top_left");
		this.hide_claw_bottom_left = hide_claw_left.getChild("hide_claw_bottom_left");
		this.hide_claw_right = body.getChild("hide_claw_right");
		this.claw_bottom_right2 = hide_claw_right.getChild("claw_bottom_right2");
		this.claw_top_right2 = hide_claw_right.getChild("claw_top_right2");

	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition body = partdefinition.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 14).addBox(-3.5F, -3.0F, -3.0F, 7.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 20.0F, 1.0F));

		PartDefinition hide_head = body.addOrReplaceChild("hide_head", CubeListBuilder.create().texOffs(0, 14).addBox(0.5F, -2.0F, -2.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(20, 27).addBox(-1.5F, -2.0F, -2.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, -0.6F, -1.9F, 0.5672F, 0.0F, 0.0F));

		PartDefinition head = body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 14).addBox(0.5F, -3.0F, -3.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(20, 27).addBox(-1.5F, -3.0F, -3.0F, 1.0F, 3.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(20, 14).addBox(-2.5F, -1.0F, -2.0F, 5.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 1.0F, -3.0F));

		PartDefinition shell = body.addOrReplaceChild("shell", CubeListBuilder.create().texOffs(0, 0).addBox(-4.0F, -4.0F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		PartDefinition leg_front_right = body.addOrReplaceChild("leg_front_right", CubeListBuilder.create(), PartPose.offset(-3.5F, 1.5F, -2.5F));

		PartDefinition legfrontright_r1 = leg_front_right.addOrReplaceChild("legfrontright_r1", CubeListBuilder.create().texOffs(26, 19).addBox(-1.5F, -0.5F, -0.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.5F, 0.0F, 0.0F, 0.0F, -0.3927F));

		PartDefinition leg_back_right = body.addOrReplaceChild("leg_back_right", CubeListBuilder.create(), PartPose.offset(-3.5F, 1.5F, 0.5F));

		PartDefinition legbackright_r1 = leg_back_right.addOrReplaceChild("legbackright_r1", CubeListBuilder.create().texOffs(24, 5).addBox(-1.5F, -0.5F, 2.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.5F, -3.0F, 0.0F, 0.0F, -0.3927F));

		PartDefinition leg_front_left = body.addOrReplaceChild("leg_front_left", CubeListBuilder.create(), PartPose.offset(3.5F, 1.5F, -2.5F));

		PartDefinition legfrontleft_r1 = leg_front_left.addOrReplaceChild("legfrontleft_r1", CubeListBuilder.create().texOffs(0, 3).addBox(-1.5F, -0.5F, -1.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
				.texOffs(0, 3).addBox(-1.5F, -0.5F, -1.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.5F, 1.0F, 0.0F, 0.0F, 0.3927F));

		PartDefinition leg_back_left = body.addOrReplaceChild("leg_back_left", CubeListBuilder.create(), PartPose.offset(3.5F, 1.5F, 0.5F));

		PartDefinition legbackleft_r1 = leg_back_left.addOrReplaceChild("legbackleft_r1", CubeListBuilder.create().texOffs(0, 0).addBox(-1.5F, -0.5F, 1.5F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.5F, -2.0F, 0.0F, 0.0F, 0.3927F));

		PartDefinition claw_right = body.addOrReplaceChild("claw_right", CubeListBuilder.create(), PartPose.offset(-3.0F, 1.5F, -4.0F));

		PartDefinition claw_bottom_right = claw_right.addOrReplaceChild("claw_bottom_right", CubeListBuilder.create(), PartPose.offset(-1.0F, 0.5F, -0.5F));

		PartDefinition clawbottomright_r1 = claw_bottom_right.addOrReplaceChild("clawbottomright_r1", CubeListBuilder.create().texOffs(10, 26).addBox(-1.0F, -0.5F, -1.5F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, -1.0F, 0.2618F, 0.0F, 0.0F));

		PartDefinition claw_top_right = claw_right.addOrReplaceChild("claw_top_right", CubeListBuilder.create(), PartPose.offset(-1.0F, -0.5F, -1.5F));

		PartDefinition clawtopright_r1 = claw_top_right.addOrReplaceChild("clawtopright_r1", CubeListBuilder.create().texOffs(24, 0).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2618F, 0.0F, 0.0F));

		PartDefinition claw_left = body.addOrReplaceChild("claw_left", CubeListBuilder.create(), PartPose.offset(3.0F, 1.5F, -4.0F));

		PartDefinition claw_top_left = claw_left.addOrReplaceChild("claw_top_left", CubeListBuilder.create(), PartPose.offset(1.0F, -0.5F, -1.5F));

		PartDefinition clawtopleft_r1 = claw_top_left.addOrReplaceChild("clawtopleft_r1", CubeListBuilder.create().texOffs(23, 23).addBox(6.5F, -1.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 0.0F, 0.0F, -0.2618F, 0.0F, 0.0F));

		PartDefinition claw_bottom_left = claw_left.addOrReplaceChild("claw_bottom_left", CubeListBuilder.create(), PartPose.offset(1.0F, 0.5F, -0.5F));

		PartDefinition clawbottomleft_r1 = claw_bottom_left.addOrReplaceChild("clawbottomleft_r1", CubeListBuilder.create().texOffs(0, 26).addBox(7.0F, -0.5F, -1.5F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 0.5F, -1.0F, 0.2618F, 0.0F, 0.0F));

		PartDefinition hide_claw_left = body.addOrReplaceChild("hide_claw_left", CubeListBuilder.create(), PartPose.offsetAndRotation(3.0F, 1.5F, -2.0F, 0.0F, 0.8727F, 0.0F));

		PartDefinition hide_claw_top_left = hide_claw_left.addOrReplaceChild("hide_claw_top_left", CubeListBuilder.create(), PartPose.offset(1.0F, -0.5F, -1.5F));

		PartDefinition clawtopleft_r2 = hide_claw_top_left.addOrReplaceChild("clawtopleft_r2", CubeListBuilder.create().texOffs(23, 23).addBox(6.5F, -1.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 0.0F, 0.0F, -0.2618F, 0.0F, 0.0F));

		PartDefinition hide_claw_bottom_left = hide_claw_left.addOrReplaceChild("hide_claw_bottom_left", CubeListBuilder.create(), PartPose.offset(1.0F, 0.5F, -0.5F));

		PartDefinition clawbottomleft_r2 = hide_claw_bottom_left.addOrReplaceChild("clawbottomleft_r2", CubeListBuilder.create().texOffs(0, 26).addBox(7.0F, -0.5F, -1.5F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-8.0F, 0.5F, -1.0F, 0.2618F, 0.0F, 0.0F));

		PartDefinition hide_claw_right = body.addOrReplaceChild("hide_claw_right", CubeListBuilder.create(), PartPose.offsetAndRotation(-3.0F, 1.5F, -2.0F, 0.0F, -0.8727F, 0.0F));

		PartDefinition claw_bottom_right2 = hide_claw_right.addOrReplaceChild("claw_bottom_right2", CubeListBuilder.create(), PartPose.offset(-1.0F, 0.5F, -0.5F));

		PartDefinition clawbottomright_r2 = claw_bottom_right2.addOrReplaceChild("clawbottomright_r2", CubeListBuilder.create().texOffs(10, 26).addBox(-1.0F, -0.5F, -1.5F, 2.0F, 1.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.5F, -1.0F, 0.2618F, 0.0F, 0.0F));

		PartDefinition claw_top_right2 = hide_claw_right.addOrReplaceChild("claw_top_right2", CubeListBuilder.create(), PartPose.offset(-1.0F, -0.5F, -1.5F));

		PartDefinition clawtopright_r2 = claw_top_right2.addOrReplaceChild("clawtopright_r2", CubeListBuilder.create().texOffs(24, 0).addBox(-1.5F, -1.0F, -1.5F, 3.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(0.0F, 0.0F, 0.0F, -0.2618F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 64, 64);
	}

	@Override
	public void setupAnim(HelmitCrabEntity entity, float limbAngle, float limbDistance, float animationProgress, float headYaw, float headPitch)
	{
		if(!entity.isHiding())
		{
			head.visible = true;
			hide_head.visible = false;
			hide_claw_left.visible = false;
			hide_claw_right.visible = false;
			leg_front_right.visible = true;
			leg_back_right.visible = true;
			leg_front_left.visible = true;
			leg_back_left.visible = true;
			claw_left.visible = true;
			claw_right.visible = true;

			float limbScale = 0.8F;
			leg_back_right.zRot = Mth.cos(limbAngle * 1 + (float) Math.PI) * limbScale * limbDistance;
			leg_front_right.zRot = Mth.cos(limbAngle * 1 + (float) Math.PI) * limbScale * limbDistance;
			leg_front_left.zRot = Mth.cos(limbAngle * 1) * limbScale * limbDistance;
			leg_back_left.zRot = Mth.cos(limbAngle * 1) * limbScale * limbDistance;

			AnimationHelper.swingLimb(claw_left, claw_right, limbAngle, limbDistance, 0.4F);
		}
		else
		{
			head.visible = false;
			hide_head.visible = true;
			hide_claw_left.visible = true;
			hide_claw_right.visible = true;
			claw_left.visible = false;
			claw_right.visible = false;
			leg_front_right.visible = false;
			leg_back_right.visible = false;
			leg_front_left.visible = false;
			leg_back_left.visible = false;
		}
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		body.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}

	@Override
	public ModelPart getHead()
	{
		return body;
	}
}