package party.lemons.biomemakeover.util.sound;

import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.sounds.SoundSource;
import party.lemons.biomemakeover.block.AltarBlock;
import party.lemons.biomemakeover.block.blockentity.AltarBlockEntity;
import party.lemons.biomemakeover.init.BMEffects;

public class AltarCursingSoundInstance extends AbstractTickableSoundInstance
{
    private final AltarBlockEntity altar;
    private int age = 0;


    public AltarCursingSoundInstance(AltarBlockEntity altar)
    {
        super(BMEffects.ALTAR_CURSING.get(), SoundSource.BLOCKS);
        this.altar = altar;
        this.x = (float) altar.getBlockPos().getX();
        this.y = (float) altar.getBlockPos().getY();
        this.z = (float) altar.getBlockPos().getZ();
    }

    @Override
    public void tick() {
        age++;
        if(altar.getLevel() == null)
            stop();

        if(!(altar.getLevel().getBlockEntity(altar.getBlockPos()) instanceof AltarBlockEntity) || (age > 2 && age < 280 && !altar.getLevel().getBlockState(altar.getBlockPos()).getValue(AltarBlock.ACTIVE)))
            stop();
    }
}
