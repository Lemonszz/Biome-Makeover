package party.lemons.biomemakeover.entity.render;

import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.GhoulFishEntity;
import software.bernie.geckolib.animation.model.AnimatedEntityModel;
import software.bernie.geckolib.animation.render.AnimatedModelRenderer;
import software.bernie.geckolib.forgetofabric.ResourceLocation;

public class GhoulFishModel extends AnimatedEntityModel<GhoulFishEntity>
{
	private final AnimatedModelRenderer body;
	private final AnimatedModelRenderer bodyback;
	private final AnimatedModelRenderer tail;
	private final AnimatedModelRenderer tailfin;
	private final AnimatedModelRenderer head;
	private final AnimatedModelRenderer jaw;
	private final AnimatedModelRenderer fin;

	public GhoulFishModel()
	{
		textureWidth = 64;
		textureHeight = 64;
		body = new AnimatedModelRenderer(this);
		body.setRotationPoint(1.0F, 21.0F, -3.0F);
		body.setTextureOffset(0, 0).addBox(-3.0F, -3.0F, 0.0F, 6.0F, 6.0F, 8.0F, 0.0F, false);
		body.setModelRendererName("body");
		this.registerModelRenderer(body);

		bodyback = new AnimatedModelRenderer(this);
		bodyback.setRotationPoint(0.0F, 2.0F, 8.0F);
		body.addChild(bodyback);
		bodyback.setTextureOffset(0, 24).addBox(-2.0F, -4.5F, -1.0F, 4.0F, 5.0F, 4.0F, 0.0F, false);
		bodyback.setModelRendererName("bodyback");
		this.registerModelRenderer(bodyback);

		tail = new AnimatedModelRenderer(this);
		tail.setRotationPoint(0.0F, -2.0F, 3.0F);
		bodyback.addChild(tail);
		tail.setTextureOffset(20, 0).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 3.0F, 0.0F, false);
		tail.setModelRendererName("tail");
		this.registerModelRenderer(tail);

		tailfin = new AnimatedModelRenderer(this);
		tailfin.setRotationPoint(0.0F, 0.0F, 2.0F);
		tail.addChild(tailfin);
		tailfin.setTextureOffset(0, 0).addBox(0.0F, -2.0F, 0.0F, 0.0F, 4.0F, 4.0F, 0.0F, false);
		tailfin.setModelRendererName("tailfin");
		this.registerModelRenderer(tailfin);

		head = new AnimatedModelRenderer(this);
		head.setRotationPoint(0.0F, 1.0F, 0.0F);
		body.addChild(head);
		head.setTextureOffset(0, 14).addBox(-3.0F, -4.0F, -5.0F, 6.0F, 5.0F, 5.0F, 0.0F, false);
		head.setTextureOffset(20, 2).addBox(0.0F, -6.0F, -4.0F, 0.0F, 2.0F, 3.0F, 0.0F, false);
		head.setModelRendererName("head");
		this.registerModelRenderer(head);

		jaw = new AnimatedModelRenderer(this);
		jaw.setRotationPoint(0.0F, 2.0F, 0.0F);
		head.addChild(jaw);
		setRotationAngle(jaw, 0.2618F, 0.0F, 0.0F);
		jaw.setTextureOffset(17, 19).addBox(-3.0F, -1.0F, -5.0F, 6.0F, 1.0F, 5.0F, 0.0F, false);
		jaw.setModelRendererName("jaw");
		this.registerModelRenderer(jaw);

		fin = new AnimatedModelRenderer(this);
		fin.setRotationPoint(0.0F, -3.0F, 6.0F);
		body.addChild(fin);
		fin.setTextureOffset(17, 8).addBox(0.0F, -3.0F, -3.0F, 0.0F, 4.0F, 6.0F, 0.0F, false);
		fin.setModelRendererName("fin");
		this.registerModelRenderer(fin);

		this.rootBones.add(body);
	}

	@Override
	public ResourceLocation getAnimationFileLocation()
	{
		return new ResourceLocation(BiomeMakeover.MODID, "animations/ghoul_fish.json");
	}
}