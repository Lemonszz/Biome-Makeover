package party.lemons.biomemakeover.util.effect;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import party.lemons.biomemakeover.block.blockentity.AltarBlockEntity;
import party.lemons.biomemakeover.util.sound.AltarCursingSoundInstance;

@Environment(EnvType.CLIENT)
public enum BiomeMakeoverEvent
{
	PLAY_CURSE_SOUND(BiomeMakeoverEvent::playCurseSound);

	private final Event event;

	private BiomeMakeoverEvent(Event event)
	{
		this.event = event;
	}

	public void execute(World world, BlockPos pos)
	{
		event.execute(world, pos);
	}

	private static void playCurseSound(World world, BlockPos pos)
	{
		BlockEntity be = world.getBlockEntity(pos);
		if(be instanceof AltarBlockEntity)
		{
			AltarCursingSoundInstance sound = new AltarCursingSoundInstance((AltarBlockEntity) be);
			MinecraftClient.getInstance().getSoundManager().playNextTick(sound);
		}
	}

	public interface Event
	{
		void execute(World world, BlockPos pos);
	}
}
