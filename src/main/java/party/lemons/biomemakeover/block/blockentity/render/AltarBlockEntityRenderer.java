package party.lemons.biomemakeover.block.blockentity.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.entity.model.BookModel;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import party.lemons.biomemakeover.block.blockentity.AltarBlockEntity;

public class AltarBlockEntityRenderer extends BlockEntityRenderer<AltarBlockEntity>
{
	public static final SpriteIdentifier BOOK_TEXTURE = new SpriteIdentifier(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE, new Identifier("entity/enchanting_table_book"));
	private final BookModel book = new BookModel();

	public AltarBlockEntityRenderer(BlockEntityRenderDispatcher r)
	{
		super(r);
	}

	@Override
	public void render(AltarBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay)
	{
		matrices.push();
		matrices.translate(0.5D, 0.75D, 0.5D);
		float ticks = (float)entity.ticks + tickDelta;
		matrices.translate(0.0D, (double)(0.1F + MathHelper.sin(ticks * 0.1F) * 0.01F), 0.0D);

		float h;
		for(h = entity.currentAngle - entity.lastAngle; h >= 3.1415927F; h -= 6.2831855F) {
		}

		while(h < -3.1415927F) {
			h += 6.2831855F;
		}

		float k = entity.lastAngle + h * tickDelta;
		matrices.multiply(Vector3f.POSITIVE_Y.getRadialQuaternion(-k));
		matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(80.0F));
		float l = MathHelper.lerp(tickDelta, entity.pageAngle, entity.nextPageAngle);
		float m = MathHelper.fractionalPart(l + 0.25F) * 1.6F - 0.3F;
		float n = MathHelper.fractionalPart(l + 0.75F) * 1.6F - 0.3F;
		float o = MathHelper.lerp(tickDelta, entity.pageTurningSpeed, entity.nextPageTurningSpeed);
		this.book.setPageAngles(ticks, MathHelper.clamp(m, 0.0F, 1.0F), MathHelper.clamp(n, 0.0F, 1.0F), o);
		VertexConsumer vertexConsumer = BOOK_TEXTURE.getVertexConsumer(vertexConsumers, RenderLayer::getEntitySolid);
		this.book.method_24184(matrices, vertexConsumer, light, overlay, 1.0F, 1.0F, 1.0F, 1.0F);
		matrices.pop();
	}
}
