package party.lemons.biomemakeover.entity.render;

import net.minecraft.client.model.DrownedModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.DecayedEntity;

public class DecayedModel extends DrownedModel<DecayedEntity> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(BiomeMakeover.ID("decayed"), "main");
    public static final ModelLayerLocation LAYER_LOCATION_2 = new ModelLayerLocation(BiomeMakeover.ID("decayed2"), "main");
    public static final ModelLayerLocation LAYER_LOCATION_3 = new ModelLayerLocation(BiomeMakeover.ID("decayed3"), "main");


    public DecayedModel(ModelPart modelPart) {
        super(modelPart);
    }

    @Override
    public boolean isAggressive(DecayedEntity zombie) {
        return false;
    }

    @Override
    public void setupAnim(DecayedEntity zombie, float f, float g, float h, float i, float j) {
        super.setupAnim(zombie, f, g, h, i, j);

        if(zombie.isDummy())
        {
            this.rightArm.yRot = 9F + (Mth.sin(((float) zombie.tickCount / 10F) * 0.6662F) / 32F);
            this.rightArm.zRot = -1F + (Mth.cos(((float) zombie.tickCount / 10F) * 0.6662F) / 32F);
            this.leftArm.yRot = 9F + (Mth.cos(((float) zombie.tickCount / 10F) * 0.6662F) / 32F);
            this.leftArm.zRot = 1F + (Mth.sin(((float) zombie.tickCount / 10F) * 0.6662F) / 32F);

            leftLeg.yRot = -25;
            rightLeg.yRot = 25;
            this.head.yRot = -0.75F + (Mth.sin(((float) zombie.tickCount / 10F) * 0.6662F) / 32F);
            this.head.zRot = (Mth.sin(((float) zombie.tickCount / 10F) * 0.6662F) / 32F);
        }
    }
}