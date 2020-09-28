package party.lemons.biomemakeover.entity.render;

import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.DragonlingEntity;
import software.bernie.geckolib.animation.model.AnimatedEntityModel;
import software.bernie.geckolib.animation.render.AnimatedModelRenderer;
import software.bernie.geckolib.forgetofabric.ResourceLocation;

public class DragonlingModel extends AnimatedEntityModel<DragonlingEntity> {

	private final AnimatedModelRenderer body;
	private final AnimatedModelRenderer head;
	private final AnimatedModelRenderer jaw;
	private final AnimatedModelRenderer arm_left;
	private final AnimatedModelRenderer hand_left;
	private final AnimatedModelRenderer arm_right;
	private final AnimatedModelRenderer hand_right;
	private final AnimatedModelRenderer leg_left;
	private final AnimatedModelRenderer leg_right;

	public DragonlingModel()
	{
		textureWidth = 64;
		textureHeight = 64;
		body = new AnimatedModelRenderer(this);
		body.setRotationPoint(-0.5F, 14.0F, 0.5F);
		setRotationAngle(body, 0.2618F, 0.0F, 0.0F);
		body.setTextureOffset(0, 16).addBox(-2.5F, -4.0F, -1.5F, 5.0F, 8.0F, 3.0F, 0.0F, false);
		body.setTextureOffset(0, 9).addBox(-0.5F, -2.7071F, 1.2247F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		body.setTextureOffset(0, 0).addBox(-0.5F, 0.4495F, 1.4142F, 1.0F, 2.0F, 1.0F, 0.0F, false);
		body.setModelRendererName("body");
		this.registerModelRenderer(body);

		head = new AnimatedModelRenderer(this);
		head.setRotationPoint(0.1667F, -3.0341F, 1.0745F);
		body.addChild(head);
		setRotationAngle(head, -0.2182F, 0.0F, 0.0F);
		head.setTextureOffset(0, 9).addBox(-3.1667F, -1.0F, -5.3333F, 6.0F, 1.0F, 6.0F, 0.0F, false);
		head.setTextureOffset(13, 17).addBox(-3.1667F, -2.0F, -5.3333F, 6.0F, 1.0F, 1.0F, 0.0F, false);
		head.setModelRendererName("head");
		this.registerModelRenderer(head);

		jaw = new AnimatedModelRenderer(this);
		jaw.setRotationPoint(-0.1667F, -1.0F, 0.1667F);
		head.addChild(jaw);
		jaw.setTextureOffset(0, 0).addBox(-3.0F, -4.0F, -4.5F, 6.0F, 4.0F, 5.0F, 0.0F, false);
		jaw.setTextureOffset(16, 16).addBox(0.0F, -5.0F, -4.0F, 0.0F, 5.0F, 5.0F, 0.0F, false);
		jaw.setModelRendererName("jaw");
		this.registerModelRenderer(jaw);

		arm_left = new AnimatedModelRenderer(this);
		arm_left.setRotationPoint(2.5F, -3.0F, 0.0F);
		body.addChild(arm_left);
		setRotationAngle(arm_left, -0.3491F, 0.0F, 0.0F);
		arm_left.setTextureOffset(14, 26).addBox(0.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);
		arm_left.setModelRendererName("arm_left");
		this.registerModelRenderer(arm_left);

		hand_left = new AnimatedModelRenderer(this);
		hand_left.setRotationPoint(1.0F, 6.5F, 0.0F);
		arm_left.addChild(hand_left);
		hand_left.setTextureOffset(0, 27).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
		hand_left.setModelRendererName("hand_left");
		this.registerModelRenderer(hand_left);

		arm_right = new AnimatedModelRenderer(this);
		arm_right.setRotationPoint(-4.5F, -3.0F, 0.0F);
		body.addChild(arm_right);
		setRotationAngle(arm_right, -0.3491F, 0.0F, 0.0F);
		arm_right.setTextureOffset(24, 9).addBox(0.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, 0.0F, false);
		arm_right.setModelRendererName("arm_right");
		this.registerModelRenderer(arm_right);

		hand_right = new AnimatedModelRenderer(this);
		hand_right.setRotationPoint(1.0F, 6.5F, 0.0F);
		arm_right.addChild(hand_right);
		hand_right.setTextureOffset(26, 17).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 3.0F, 2.0F, 0.0F, false);
		hand_right.setModelRendererName("hand_right");
		this.registerModelRenderer(hand_right);

		leg_left = new AnimatedModelRenderer(this);
		leg_left.setRotationPoint(1.5F, 4.0F, 0.0F);
		body.addChild(leg_left);
		setRotationAngle(leg_left, -0.2618F, 0.0F, 0.0F);
		leg_left.setTextureOffset(24, 24).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 7.0F, 2.0F, 0.0F, false);
		leg_left.setModelRendererName("leg_left");
		this.registerModelRenderer(leg_left);

		leg_right = new AnimatedModelRenderer(this);
		leg_right.setRotationPoint(-1.5F, 4.0F, 0.0F);
		body.addChild(leg_right);
		setRotationAngle(leg_right, -0.2618F, 0.0F, 0.0F);
		leg_right.setTextureOffset(22, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 7.0F, 2.0F, 0.0F, true);
		leg_right.setModelRendererName("leg_right");
		this.registerModelRenderer(leg_right);

		this.rootBones.add(body);
	}

	@Override
	public ResourceLocation getAnimationFileLocation()
	{
		return new ResourceLocation(BiomeMakeover.MODID, "animations/dragonling.json");
	}
}