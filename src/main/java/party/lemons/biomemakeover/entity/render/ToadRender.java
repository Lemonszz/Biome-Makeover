package party.lemons.biomemakeover.entity.render;

import com.sun.org.apache.bcel.internal.generic.LADD;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.EnderDragonEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import party.lemons.biomemakeover.BiomeMakeover;
import party.lemons.biomemakeover.entity.ToadEntity;

public class ToadRender extends MobEntityRenderer<ToadEntity, ToadEntityModel>
{
	private static final Identifier TEXTURE = BiomeMakeover.ID("textures/entity/toad.png");

	public ToadRender(EntityRenderDispatcher rd)
	{
		super(rd, new ToadEntityModel(), 0.25F);
	}

	@Override
	public Identifier getTexture(ToadEntity entity)
	{
		return TEXTURE;
	}
}
