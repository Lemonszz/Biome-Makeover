package party.lemons.biomemakeover.entity.render;

import net.minecraft.client.render.entity.model.DrownedEntityModel;
import net.minecraft.util.math.MathHelper;
import party.lemons.biomemakeover.entity.DecayedEntity;

public class DecayedEntityModel extends DrownedEntityModel<DecayedEntity>
{
	public DecayedEntityModel(float f, float g, int i, int j)
	{
		super(f, g, i, j);
	}

	public DecayedEntityModel(float f, boolean bl)
	{
		super(f, bl);
	}

	@Override
	public boolean isAttacking(DecayedEntity zombieEntity)
	{
		return false;
	}

	@Override
	public void setAngles(DecayedEntity zombieEntity, float f, float g, float delta, float i, float j)
	{
		super.setAngles(zombieEntity, f, g, delta, i, j);
		if(zombieEntity.isDummy())
		{
			this.rightArm.pitch = 9F + (MathHelper.sin(((float) zombieEntity.age / 10F) * 0.6662F) / 32F);
			this.rightArm.roll = -1F + (MathHelper.cos(((float) zombieEntity.age / 10F) * 0.6662F) / 32F);
			this.leftArm.pitch = 9F + (MathHelper.cos(((float) zombieEntity.age / 10F) * 0.6662F) / 32F);
			this.leftArm.roll = 1F + (MathHelper.sin(((float) zombieEntity.age / 10F) * 0.6662F) / 32F);

			leftLeg.pitch = -25;
			rightLeg.pitch = 25;
			this.head.pitch = -0.75F + (MathHelper.sin(((float) zombieEntity.age / 10F) * 0.6662F) / 32F);
			this.head.roll = (MathHelper.sin(((float) zombieEntity.age / 10F) * 0.6662F) / 32F);
		}
	}
}
