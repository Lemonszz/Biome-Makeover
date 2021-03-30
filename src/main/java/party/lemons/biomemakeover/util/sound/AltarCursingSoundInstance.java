package party.lemons.biomemakeover.util.sound;

import net.minecraft.client.sound.AbstractSoundInstance;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import party.lemons.biomemakeover.block.AltarBlock;
import party.lemons.biomemakeover.block.blockentity.AltarBlockEntity;
import party.lemons.biomemakeover.init.BMEffects;

public class AltarCursingSoundInstance extends MovingSoundInstance
{
	private final AltarBlockEntity altar;
	private int age = 0;

	public AltarCursingSoundInstance(AltarBlockEntity altar)
	{
		super(BMEffects.ALTAR_CURSING, SoundCategory.BLOCKS);
		this.altar = altar;
		this.x = (float) altar.getPos().getX();
		this.y = (float) altar.getPos().getY();
		this.z = (float) altar.getPos().getZ();
	}

	@Override
	public void tick()
	{
		age++;
		boolean a1 = !(altar.getWorld().getBlockEntity(altar.getPos()) instanceof AltarBlockEntity);
		boolean a2 = (age > 2 && !altar.getWorld().getBlockState(altar.getPos()).get(AltarBlock.ACTIVE));
		if(!(altar.getWorld().getBlockEntity(altar.getPos()) instanceof AltarBlockEntity) || (age > 2 && age < 280 && !altar.getWorld().getBlockState(altar.getPos()).get(AltarBlock.ACTIVE)))
			setDone();
	}
}
