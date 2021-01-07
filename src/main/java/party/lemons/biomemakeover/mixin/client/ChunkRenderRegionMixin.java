package party.lemons.biomemakeover.mixin.client;

import net.minecraft.client.render.chunk.ChunkRendererRegion;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import party.lemons.biomemakeover.util.access.ChunkRenderRegionAccess;

@Mixin(ChunkRendererRegion.class)
public class ChunkRenderRegionMixin implements ChunkRenderRegionAccess
{

	@Shadow @Final protected World world;

	@Override
	public World getWorld()
	{
		return world;
	}
}
